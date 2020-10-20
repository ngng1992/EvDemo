package com.mfinance.everjoy.app;

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
import android.widget.ToggleButton;

import com.mfinance.everjoy.app.bo.ContractObj;
import com.mfinance.everjoy.app.pojo.ContractDefaultSetting;
import com.mfinance.everjoy.app.pojo.ContractDefaultSettingBuilder;
import com.mfinance.everjoy.app.util.GUIUtility;
import com.mfinance.everjoy.app.util.MessageMapping;
import com.mfinance.everjoy.app.util.Utility;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static com.mfinance.everjoy.app.constant.ServiceFunction.SRV_SETTING_CONTRACT;
import com.mfinance.everjoy.R;
public class SettingContractActivity extends BaseActivity {
    private Handler mainHandler;

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

    }

    @Override
    public void handleByChild(Message msg) {

    }

    private Runnable updateInternalUI = () -> {

    };

    @Override
    public void loadLayout() {
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
        setContentView(R.layout.v_setup_contract);
        String contractCode = app.getSelectedContract().strContractCode;
        state = new State(contractCode, app.data.getContractDefaultSettingMap().getOrDefault(contractCode, Utility.EMPTY_CONTRACT_DEFAULT_SETTING));
        View rlTop = findViewById(R.id.rlTop);
        mainHandler = new Handler(getMainLooper());
        ArrayList<ContractObj> tradableContractList = app.data.getTradableContractList();
        TextView tvTitleContractSetting = findViewById(R.id.tvTitleContractSetting);
        ContractObj contract1 = app.getSelectedContract();
        PopupLOT popupLOT = new PopupLOT(getApplicationContext(), rlTop, BigDecimal.valueOf(contract1.getFinalMaxLotLimit()));
        PopupSlippage popSlippage = new PopupSlippage(getApplicationContext(), rlTop);
        Button btnSlippage = findViewById(R.id.btnSlippage);
        Button btnDefaultLot = findViewById(R.id.btnDefaultLot);
        TextView tvLimit = findViewById(R.id.tvLimit);
        Button btnLimitPips = findViewById(R.id.btnLimitPips);
        PopupSlippage popupLimitPips = new PopupSlippage(getApplicationContext(), rlTop);
        TextView tvStop = findViewById(R.id.tvStop);
        Button btnStopPips = findViewById(R.id.btnStopPips);
        PopupSlippage popupStopPips = new PopupSlippage(getApplicationContext(), rlTop);
        Button btnOK = findViewById(R.id.btnOK);
        Button btnCancel = findViewById(R.id.btnCancel);
        btnOK.setOnClickListener((v) -> {

            SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            JSONObject o = new JSONObject();
            app.data.getContractDefaultSettingMap().forEach((key, value) -> {
                try {
                    Utility.putContractDefaultSetting(o, key, value);
                } catch (Exception ex) {

                }
            });
            try {
                Utility.putContractDefaultSetting(o, state.contractCode, state.contractDefaultSetting);
            } catch (Exception ex) {

            }
            JSONObject defaultContractSetting;
            try {
                defaultContractSetting = new JSONObject(sharedPreferences.getString("DEFAULT_CONTRACT_SETTING", "{}"));
                defaultContractSetting.put(Optional.ofNullable(app.data.getBalanceRecord()).map(b -> b.strAccount).orElse(""), o);
            } catch (Exception ex) {
                defaultContractSetting = new JSONObject();
            }
            edit.putString("DEFAULT_CONTRACT_SETTING", defaultContractSetting.toString());
            edit.apply();
            finish();
        });
        btnCancel.setOnClickListener(v -> this.finish());
        updateInternalUI = () -> {
            ContractObj contract = app.data.getContract(state.contractCode);
            StringBuilder title = new StringBuilder();
            title.append(getResources().getText(R.string.title_contract_setting)).append(" (").append(contract.getContractName(getLanguage())).append(')');
            tvTitleContractSetting.setText(title);
            btnDefaultLot.setText(Utility.getDecimalFormatLotSize().format(state.contractDefaultSetting.getDefaultLotSize()));
            btnSlippage.setText(state.contractDefaultSetting.getDefaultSlippage().toString());
            btnLimitPips.setText(state.contractDefaultSetting.getDefaultTakeProfitOrderPips().map(Object::toString).orElse(Integer.toString(contract.iOrderPips)));
            btnLimitPips.setEnabled(state.contractDefaultSetting.getDefaultTakeProfitOrderPips().isPresent());
            tvLimit.setCompoundDrawablesWithIntrinsicBounds(state.contractDefaultSetting.getDefaultTakeProfitOrderPips().isPresent() ? checkedDraw : uncheckedDraw, null, null, null);
            btnStopPips.setEnabled(state.contractDefaultSetting.getDefaultTakeProfitOrderPips().isPresent());
            btnStopPips.setText(state.contractDefaultSetting.getDefaultStopLossOrderPips().map(Object::toString).orElse(Integer.toString(contract.iOrderPips)));
            btnStopPips.setEnabled(state.contractDefaultSetting.getDefaultStopLossOrderPips().isPresent());
            tvStop.setCompoundDrawablesWithIntrinsicBounds(state.contractDefaultSetting.getDefaultStopLossOrderPips().isPresent() ? checkedDraw : uncheckedDraw, null, null, null);
        };
        btnSlippage.setOnClickListener(v -> {
            popSlippage.setValue(state.contractDefaultSetting.getDefaultSlippage().toString());
            popSlippage.showLikeQuickAction();
        });
        btnDefaultLot.setOnClickListener(v -> {
            popupLOT.setValue(state.contractDefaultSetting.getDefaultLotSize());
            popupLOT.showLikeQuickAction();
        });
        tvLimit.setOnClickListener(v -> {
            ContractDefaultSettingBuilder builder = new ContractDefaultSettingBuilder(state.contractDefaultSetting);
            if (state.contractDefaultSetting.getDefaultTakeProfitOrderPips().isPresent()) {
                builder.setDefaultTakeProfitOrderPips(Optional.empty());
            } else {
                Optional<ContractObj> contract = Optional.ofNullable(app.data.getContract(state.contractCode));
                builder.setDefaultTakeProfitOrderPips(Optional.of(contract.map(c -> c.iOrderPips).orElse(10)));
            }
            state = new State(state.contractCode, builder.createContractDefaultSetting());
            mainHandler.post(updateInternalUI);
        });
        btnLimitPips.setOnClickListener(v -> {
            Optional<Integer> defaultTakeProfitOrderPips = state.contractDefaultSetting.getDefaultTakeProfitOrderPips();
            if (defaultTakeProfitOrderPips.isPresent()) {
                popupLimitPips.setValue(defaultTakeProfitOrderPips.get().toString());
                popupLimitPips.showLikeQuickAction();
            }
        });
        popupLimitPips.btnCommit.setOnClickListener(v -> {
            ContractDefaultSettingBuilder builder = new ContractDefaultSettingBuilder(state.contractDefaultSetting);
            builder.setDefaultTakeProfitOrderPips(Optional.of(Integer.parseInt(popupLimitPips.getValue())));
            state = new State(state.contractCode, builder.createContractDefaultSetting());
            popupLimitPips.dismiss();
            mainHandler.post(updateInternalUI);
        });
        popupLimitPips.btnClose.setOnClickListener(v -> {
            popupLimitPips.dismiss();
        });
        tvStop.setOnClickListener(v -> {
            ContractDefaultSettingBuilder builder = new ContractDefaultSettingBuilder(state.contractDefaultSetting);
            if (state.contractDefaultSetting.getDefaultStopLossOrderPips().isPresent()) {
                builder.setDefaultStopLossOrderPips(Optional.empty());
            } else {
                Optional<ContractObj> contract = Optional.ofNullable(app.data.getContract(state.contractCode));
                builder.setDefaultStopLossOrderPips(Optional.of(contract.map(c -> c.iOrderPips).orElse(10)));
            }
            state = new State(state.contractCode, builder.createContractDefaultSetting());
            mainHandler.post(updateInternalUI);
        });
        btnStopPips.setOnClickListener(v -> {
            Optional<Integer> defaultStopLossOrderPips = state.contractDefaultSetting.getDefaultStopLossOrderPips();
            if (defaultStopLossOrderPips.isPresent()) {
                popupStopPips.setValue(defaultStopLossOrderPips.get().toString());
                popupStopPips.showLikeQuickAction();
            }
        });
        popupStopPips.btnCommit.setOnClickListener(v -> {
            ContractDefaultSettingBuilder builder = new ContractDefaultSettingBuilder(state.contractDefaultSetting);
            builder.setDefaultStopLossOrderPips(Optional.of(Integer.parseInt(popupStopPips.getValue())));
            state = new State(state.contractCode, builder.createContractDefaultSetting());
            popupStopPips.dismiss();
            mainHandler.post(updateInternalUI);
        });
        popupStopPips.btnClose.setOnClickListener(v -> {
            popupStopPips.dismiss();
        });
        popupLOT.btnCommit.setOnClickListener(v -> {
            BigDecimal decimal = new BigDecimal(popupLOT.getValue());
            if (CompanySettings.VALIDATE_MINLOT_AND_INCLOT) {
                double minLot = app.getSelectedContract().dMinLot > 0 ? app.getSelectedContract().dMinLot : app.data.getBalanceRecord().dMinLotLimit;
                if (decimal.compareTo(BigDecimal.valueOf(minLot)) < 0) {
                    Toast.makeText(this, MessageMapping.getMessageByCode(res, "620", app.locale).replaceFirst("#s", String.valueOf(minLot)), Toast.LENGTH_LONG).show();
                    return;
                }
                if (app.getSelectedContract().dIncLot > 0) {
                    if (((1000 * Double.valueOf(popupLOT.getValue())) % (1000 * app.getSelectedContract().dIncLot)) != 0) {
                        Toast.makeText(this, MessageMapping.getMessageByCode(res, "621" ,app.locale).replaceFirst("#s", String.valueOf(app.getSelectedContract().dIncLot)), Toast.LENGTH_LONG).show();
                        return;
                    }
                } else if (app.data.getBalanceRecord() != null) {
                    if (((1000 * Double.valueOf(popupLOT.getValue())) % (1000 * app.data.getBalanceRecord().dMinLotIncrementUnit)) != 0) {
                        Toast.makeText(this, MessageMapping.getMessageByCode(res, "621" ,app.locale).replaceFirst("#s", String.valueOf(app.data.getBalanceRecord().dMinLotIncrementUnit)), Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            }
            ContractDefaultSettingBuilder builder = new ContractDefaultSettingBuilder(state.contractDefaultSetting);
            state = new State(
                    state.contractCode,
                    builder.setDefaultLotSize(decimal).createContractDefaultSetting()
            );
            popupLOT.dismiss();
            mainHandler.post(updateInternalUI);
        });
        popupLOT.btnClose.setOnClickListener(v -> popupLOT.dismiss());
        popSlippage.btnCommit.setOnClickListener(v -> {
            ContractDefaultSettingBuilder builder = new ContractDefaultSettingBuilder(state.contractDefaultSetting);
            state = new State(
                    state.contractCode,
                    builder.setDefaultSlippage(Integer.parseInt(popSlippage.getValue())).createContractDefaultSetting()
            );
            popSlippage.dismiss();
            mainHandler.post(updateInternalUI);
        });
        popSlippage.btnClose.setOnClickListener(v -> popSlippage.dismiss());
        updateInternalUI.run();
    }

    @Override
    public void updateUI() {
        ContractObj selectedContract = app.getSelectedContract();
        if (!selectedContract.strContractCode.equals(state.contractCode)) {
            state = new State(selectedContract.strContractCode, app.data.getContractDefaultSettingMap().getOrDefault(selectedContract.strContractCode, Utility.EMPTY_CONTRACT_DEFAULT_SETTING));
            updateInternalUI.run();
        }
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
