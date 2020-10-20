package com.mfinance.everjoy.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.bo.ContractObj;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.model.DataRepository;
import com.mfinance.everjoy.app.pojo.ContractDefaultSetting;
import com.mfinance.everjoy.app.pojo.ContractDefaultSettingBuilder;
import com.mfinance.everjoy.app.pojo.PriceAlertHistoryObject;
import com.mfinance.everjoy.app.pojo.PriceAlertObject;
import com.mfinance.everjoy.app.util.CommonFunction;
import com.mfinance.everjoy.app.util.MessageMapping;
import com.mfinance.everjoy.app.util.Utility;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import static com.mfinance.everjoy.app.constant.ServiceFunction.SRV_SETTING_CONTRACT;

public class NewPriceAlertActivity extends BaseActivity {
    private Handler mainHandler;
    Button btnContract;
    TextView txtPrice;
    Button btnType;
    Button btnPrice;
    Button btnGoodTill;
    int iAlertType;

    ArrayList<ContractObj> popupContractList;

    PopupContract popContract;
    PopupGoodTill popType;
    PopupAlertRate popRate;
    PopupGoodTill popGoodTill;

    private class State {
        State(String contractCode, ContractDefaultSetting contractDefaultSetting) {
            this.contractCode = contractCode;
            this.contractDefaultSetting = contractDefaultSetting;
        }

        public final String contractCode;
        public final ContractDefaultSetting contractDefaultSetting;
    }

    private State state;

    @Override
    public void bindEvent() {
        btnContract.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                popContract.updateSelectedContract(null, btnContract.getText().toString());
                popContract.showLikeQuickAction();
            }
        });

        popContract.btnCommit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                popContract.dismiss();
                String sContract = popContract.getValue();
                // do not look up contract by name since it may not be unique!
                app.setSelectedContract(popupContractList.get(popContract.getSelectedIndex()));
                btnContract.setText(sContract);
                updateUI();
                updateButton();
            }
        });

        popContract.btnClose.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                popContract.dismiss();
            }
        });

        btnPrice.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                popRate.upateRate(btnPrice.getText().toString(), btnPrice.getText().toString());
                popRate.showLikeQuickAction();
            }
        });

        popRate.btnCommit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                btnPrice.setText(popRate.getValue());
                popRate.dismiss();
            }
        });

        popRate.btnClose.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                popRate.dismiss();
            }
        });

        btnType.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                popType.updateSelectedGoodTill(btnType.getText().toString());
                popType.showLikeQuickAction();
            }
        });

        popType.btnCommit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                btnType.setText(popType.getValue());
                iAlertType = popType.getSelectedIndex();
                if (iAlertType == 2 || iAlertType == 3)
                    iAlertType +=2;
                popType.dismiss();
                updateButton();
            }
        });

        popType.btnClose.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                popType.dismiss();
            }
        });

        btnGoodTill.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                popGoodTill.updateSelectedGoodTill(btnGoodTill.getText().toString());
                popGoodTill.showLikeQuickAction();
            }
        });

        popGoodTill.btnCommit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                btnGoodTill.setText(popGoodTill.getValue());
                popGoodTill.dismiss();
            }
        });

        popGoodTill.btnClose.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                popGoodTill.dismiss();
            }
        });
    }

    @Override
    public void handleByChild(Message msg) {

    }

    private Runnable updateInternalUI = () -> {

    };

    @Override
    public void loadLayout() {
        PriceAlertObject refObj = app.data.getSelectedPriceAlertRef();
        boolean isEdit = refObj != null? true: false;
        if (isEdit)
            iAlertType = refObj.getType();
        else
            iAlertType = 0;
        int type = iAlertType;
        if (iAlertType == 4 || iAlertType == 5)
            type -= 2;

        Drawable checkedDraw;
        Drawable uncheckedDraw;
        Drawable drawable = getDrawable(R.drawable.icon_checkedbox);
        if (drawable instanceof BitmapDrawable) {
            checkedDraw = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(((BitmapDrawable) drawable).getBitmap(), 23, 23, true));
        } else {
            checkedDraw = drawable;
        }
        Drawable drawable1 = getDrawable(R.drawable.icon_checkbox);
        if (drawable1 instanceof BitmapDrawable) {
            uncheckedDraw = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(((BitmapDrawable)  drawable1).getBitmap(), 23, 23, true));
        } else {
            uncheckedDraw = drawable1;
        }
        setContentView(R.layout.v_add_pricealert);
        String contractCode = app.getSelectedContract().strContractCode;
        state = new State(contractCode, app.data.getContractDefaultSettingMap().getOrDefault(contractCode, Utility.EMPTY_CONTRACT_DEFAULT_SETTING));
        View rlTop = findViewById(R.id.rlTop);
        mainHandler = new Handler(getMainLooper());
        ArrayList<ContractObj> tradableContractList = app.data.getTradableContractList();
        TextView tvTitleContractSetting = findViewById(R.id.tvTitlePriceAlert);
        if (isEdit)
            tvTitleContractSetting.setText(res.getString(R.string.lb_pricealert_edit_title));
        ContractObj contract1 = app.getSelectedContract();
        PopupLOT popupLOT = new PopupLOT(getApplicationContext(), rlTop, BigDecimal.valueOf(contract1.getFinalMaxLotLimit()));
        PopupSlippage popSlippage = new PopupSlippage(getApplicationContext(), rlTop);

        ContractObj contract = app.getSelectedContract();

        btnContract = (Button)findViewById(R.id.btnContract);
        popupContractList = app.data.getTradableContractList();
        popContract = new PopupContract(getApplicationContext(), findViewById(R.id.rlTop), popupContractList);
        btnContract.setText(contract.getContractName(getLanguage()));
        if (isEdit) {
            btnContract.setEnabled(false);
            btnContract.setAlpha((float)0.5);
        }

        txtPrice = (TextView) findViewById(R.id.txtPrice);
        txtPrice.setText(Utility.round(contract.getBidAsk()[0], contract.iRateDecPt, contract.iRateDecPt) + "/" + Utility.round(contract.getBidAsk()[1], contract.iRateDecPt, contract.iRateDecPt));

        btnType = (Button)findViewById(R.id.btnType);
        ArrayList<String> alType = new ArrayList<String>();
        alType.add(getResources().getString(R.string.lb_pricealert_type0));
        alType.add(getResources().getString(R.string.lb_pricealert_type1));
        alType.add(getResources().getString(R.string.lb_pricealert_type4));
        alType.add(getResources().getString(R.string.lb_pricealert_type5));
        popType = new PopupGoodTill(getApplicationContext(), findViewById(R.id.rlTop), alType);
        btnType.setText(alType.get(type));

        btnPrice = (Button)findViewById(R.id.btnPrice);
        double pips = CompanySettings.getPriceAlertDefaultPips / Math.pow(10, contract.iRateDecPt);
        double dPrice = 0.0;
        if (type == 0 || type == 1)
            dPrice = contract.getBidAsk()[0];
        else
            dPrice = contract.getBidAsk()[1];
        if (type == 0 || type == 2)
            btnPrice.setText(Utility.round(dPrice + pips, contract.iRateDecPt, contract.iRateDecPt));
        else
            btnPrice.setText(Utility.round(dPrice - pips, contract.iRateDecPt, contract.iRateDecPt));
        if (isEdit)
            btnPrice.setText(Utility.round(refObj.getAlertPrice(), contract.iRateDecPt, contract.iRateDecPt));
        popRate = new PopupAlertRate(getApplicationContext(), findViewById(R.id.rlTop));

        btnGoodTill = (Button)findViewById(R.id.btnGoodtil);
        ArrayList<String> alGoodTill = new ArrayList<String>();
        alGoodTill.add(getResources().getString(R.string.lb_pricealert_add_goodtil0));
        alGoodTill.add(getResources().getString(R.string.lb_pricealert_add_goodtil1));
        alGoodTill.add(getResources().getString(R.string.lb_pricealert_add_goodtil2));
        popGoodTill = new PopupGoodTill(getApplicationContext(), findViewById(R.id.rlTop), alGoodTill);
        btnGoodTill.setText(alGoodTill.get(0));
        if (isEdit)
            btnGoodTill.setText(alGoodTill.get(refObj.getGoodTill()));

        Button btnOK = findViewById(R.id.btnOK);
        Button btnCancel = findViewById(R.id.btnCancel);
        btnOK.setOnClickListener((v) -> {
            int iType = iAlertType;
            if (iAlertType == 4 || iAlertType == 5)
                iType -= 2;

            ContractObj contractObj = app.getSelectedContract();
            double minPrice = 0.0;
            if (iType == 0)
                minPrice = contractObj.getBidAsk()[0] + CompanySettings.getPriceAlertMinPips / Math.pow(10, contractObj.iRateDecPt);
            else if (iType == 1)
                minPrice = contractObj.getBidAsk()[0] - CompanySettings.getPriceAlertMinPips / Math.pow(10, contractObj.iRateDecPt);
            else if (iType == 2)
                minPrice = contractObj.getBidAsk()[1] + CompanySettings.getPriceAlertMinPips / Math.pow(10, contractObj.iRateDecPt);
            else if (iType == 3)
                minPrice = contractObj.getBidAsk()[1] - CompanySettings.getPriceAlertMinPips / Math.pow(10, contractObj.iRateDecPt);
            minPrice = Double.parseDouble(Utility.round(minPrice, contract.iRateDecPt, contract.iRateDecPt));

            double dBtnPrice = Double.parseDouble(btnPrice.getText().toString());

            List<PriceAlertObject> priceAlertList = app.data.getPriceAlertList();
            int contractAlertCount = 0;
            for (int i = 0 ; i < priceAlertList.size() ; i++){
                PriceAlertObject priceAlertObject = priceAlertList.get(i);
                if (priceAlertObject.getMkt().equals(contractObj.strContractCode) && iAlertType == priceAlertObject.getType()){
                    contractAlertCount++;
                }
            }

            if ((iType == 0 || iType == 2) && dBtnPrice*10000 < minPrice*10000){
                Toast.makeText(NewPriceAlertActivity.this, res.getString(R.string.lb_pricealert_add_error_lower).replace("#s", Utility.round(minPrice, contract.iRateDecPt, contract.iRateDecPt)), Toast.LENGTH_LONG).show();
                return;
            }
            else if ((iType == 1 || iType == 3) && dBtnPrice*10000 > minPrice*10000){
                Toast.makeText(NewPriceAlertActivity.this, res.getString(R.string.lb_pricealert_add_error_higher).replace("#s", Utility.round(minPrice, contract.iRateDecPt, contract.iRateDecPt)), Toast.LENGTH_LONG).show();
                return;
            }

            AlertDialog dialog = new AlertDialog.Builder(NewPriceAlertActivity.this, CompanySettings.alertDialogTheme).create();
            dialog.setMessage(res.getText(R.string.lb_pricealert_add_added_confirm));
            if (isEdit)
                dialog.setMessage(res.getText(R.string.lb_pricealert_edit_edited_confirm));
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, res.getText(R.string.yes),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (!isEdit) {
                                String mode = "0";
                                String strMkt = contractObj.strContractCode;
                                String goodtill = Integer.toString(popGoodTill.getSelectedIndex());
                                String strType = Integer.toString(iAlertType);
                                String price = btnPrice.getText().toString();
                                CommonFunction.sendPriceAlertRequest(mService, mServiceMessengerHandler, mode, strMkt, strType, goodtill, price, "0", "", "");
//                                Toast.makeText(NewPriceAlertActivity.this, res.getString(R.string.lb_pricealert_add_added), Toast.LENGTH_LONG).show();
//                                NewPriceAlertActivity.this.finish();
                            }
                            else {
                                String mode = "1";
                                String strMkt = refObj.getMkt();
                                String sId = Integer.toString(refObj.getId());
                                String sActive = Integer.toString(refObj.getActive());
                                String goodtill = Integer.toString(popGoodTill.getSelectedIndex());
                                String strType = Integer.toString(iAlertType);
                                String price = btnPrice.getText().toString();
                                CommonFunction.sendPriceAlertRequest(mService, mServiceMessengerHandler, mode, strMkt, strType, goodtill, price, "0", sId, sActive);
//                                Toast.makeText(NewPriceAlertActivity.this, res.getString(R.string.lb_pricealert_edit_edited), Toast.LENGTH_LONG).show();
//                                NewPriceAlertActivity.this.finish();
                            }
                        }
                    }
            );

            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, res.getText(R.string.no),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {}
                    }
            );
            dialog.show();

        });
        btnCancel.setOnClickListener(v -> this.finish());
        updateInternalUI.run();
    }

    @Override
    public void updateUI() {
        ContractObj selectedContract = app.getSelectedContract();
        if (!selectedContract.strContractCode.equals(state.contractCode)) {
            state = new State(selectedContract.strContractCode, app.data.getContractDefaultSettingMap().getOrDefault(selectedContract.strContractCode, Utility.EMPTY_CONTRACT_DEFAULT_SETTING));
            updateInternalUI.run();
        }

        ContractObj contract = app.getSelectedContract();
        txtPrice.setText(Utility.round(contract.getBidAsk()[0], contract.iRateDecPt, contract.iRateDecPt) + "/" + Utility.round(contract.getBidAsk()[1], contract.iRateDecPt, contract.iRateDecPt));
    }

    private void updateButton(){
        ContractObj contract = app.getSelectedContract();
        txtPrice.setText(Utility.round(contract.getBidAsk()[0], contract.iRateDecPt, contract.iRateDecPt) + "/" + Utility.round(contract.getBidAsk()[1], contract.iRateDecPt, contract.iRateDecPt));

        int type = iAlertType;
        if (iAlertType == 4 || iAlertType == 5)
            type -= 2;

        double pips = CompanySettings.getPriceAlertDefaultPips / Math.pow(10, contract.iRateDecPt);
        double dPrice = 0.0;
        if (type == 0 || type == 1)
            dPrice = contract.getBidAsk()[0];
        else
            dPrice = contract.getBidAsk()[1];
        if (type == 0 || type == 2)
            btnPrice.setText(Utility.round(dPrice + pips, contract.iRateDecPt, contract.iRateDecPt));
        else
            btnPrice.setText(Utility.round(dPrice - pips, contract.iRateDecPt, contract.iRateDecPt));
    }

    @Override
    public boolean isBottonBarExist() {
        return true;
    }

    @Override
    public boolean isTopBarExist() {
        return true;
    }

    @Override
    public boolean showLogout() {
        return true;
    }

    @Override
    public boolean showTopNav() {
        return true;
    }

    @Override
    public boolean showConnected() {
        return true;
    }

    @Override
    public boolean showPlatformType() {
        return true;
    }

    @Override
    public int getServiceId() {
        return SRV_SETTING_CONTRACT;
    }

    @Override
    public int getActivityServiceCode() {
        return SRV_SETTING_CONTRACT;
    }
}
