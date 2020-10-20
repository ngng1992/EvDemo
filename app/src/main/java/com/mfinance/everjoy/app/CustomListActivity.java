package com.mfinance.everjoy.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.mfinance.everjoy.BuildConfig;
import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.bo.ContractObj;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.model.DataRepository;
import com.mfinance.everjoy.app.util.CommonFunction;
import com.mfinance.everjoy.app.util.ListViewOnItemListener;
import com.mfinance.everjoy.app.util.MessageMapping;
import com.mfinance.everjoy.app.widget.dragndroplist.DragNDropListView;
import com.mfinance.everjoy.app.widget.dragndroplist.DropListener;
import com.mfinance.everjoy.app.widget.dragndroplist.RemoveListener;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CustomListActivity extends BaseActivity implements RemoveListener, DropListener {
    ListView lvPrice = null;
//    TextView tvDefLOT = null;
    ContractListAdapter contractAdapter = null;
    Button btnEdit = null;
    TextView TextView04 = null;
    ArrayList<String> custList = new ArrayList<String>();
    PopupCustomList popContract;
    ArrayList<ContractObj> popupContractList;
    BiConsumer<ContractObj, Boolean> onClickBidAsk;
    Consumer<ContractObj> onClickContract;

    @Override
    public void bindEvent() {
        onClickBidAsk = (selectedContract, isBid) -> {
            if (!app.bLogon)
                return;

            if (isOneClickTradeEnable()) {
                if (!app.getPriceAgentConnectionStatus()) {
                    Toast.makeText(this, MessageMapping.getMessageByCode(res, "307", getLanguage()), Toast.LENGTH_LONG).show();
                    return;
                }

                String s1 = getString(R.string.lb_ask);
                String s2 = selectedContract.getContractName(getLanguage());
                String s3 = getString(R.string.lb_lot);
                String s4 = getDefaultLOT();
                String s5 = getString(R.string.are_you_sure);
                AlertDialog dialog = new AlertDialog.Builder(this, CompanySettings.alertDialogTheme).create();
                if (selectedContract.getBSD())
                    dialog.setTitle(R.string.pop_action_sell);
                else
                    dialog.setTitle(R.string.pop_action_buy);
                dialog.setMessage(String.format("%s %s\n%s %s\n%s", s1, s2, s3, s4, s5));
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, res.getText(R.string.yes),
                        (dialog1, which) -> {
                            String pid = CommonFunction.getTransactionID(DataRepository.getInstance().getBalanceRecord().strAccount);
                            if (selectedContract.getBSD())
                                CommonFunction.sendDealRequest(mService, mServiceMessengerHandler, selectedContract, isBid ? "B" : "S", s4, pid,  Long.toString(app.dServerDateTime.getTime()));
                            else
                                CommonFunction.sendDealRequest(mService, mServiceMessengerHandler, selectedContract, isBid ? "S" : "B", s4, pid,  Long.toString(app.dServerDateTime.getTime()));
                        }
                );

                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, res.getText(R.string.no),
                        (dialog12, which) -> {

                        }
                );
                dialog.show();
            } else {
                Bundle data = new Bundle();
                data.putString(ServiceFunction.DEAL_CONTRACT, selectedContract.strContractCode);
                if (selectedContract.getBSD() == true)
                    data.putString(ServiceFunction.DEAL_BUY_SELL, isBid ? "B" : "S");
                else
                    data.putString(ServiceFunction.DEAL_BUY_SELL, isBid ? "S" : "B");
                CommonFunction.moveTo(mService, mServiceMessengerHandler, ServiceFunction.SRV_DEAL, data);
            }
        };
        onClickContract = (selectedContract) -> {
            Bundle data = new Bundle();
            data.putString(ServiceFunction.CONTRACT_DETAIL_CONTRACT, selectedContract.strContractCode);
            CommonFunction.moveTo(mService, mServiceMessengerHandler, ServiceFunction.SRV_CONTRACT_DETAIL, data);
        };
        btnEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                popContract.showLikeQuickAction();
            }
        });

        popContract.btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> set = new ArrayList<String>();
                String sets = new String();
                for (int i = 0; i<popContract.alCustName.size(); i++){
                    for (int j=0; j<popupContractList.size(); j++){
                        if (popContract.alCustName.get(i).equals(popupContractList.get(j).getContractName(app.locale))){

                            set.add(popupContractList.get(j).strContractCode);
                        }
                    }
                }

                for (String s : set){
                    sets += s+";";
                }

                if (sets.equals("")){
                    sets = null;
                }

                SharedPreferences.Editor editor = setting.edit();
                editor.putString("CUSTOMIZE_LIST", sets);
                editor.commit();

                updateUI();

                popContract.dismiss();
            }
        });

        popContract.btnClose.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                popContract.dismiss();
            }
        });

    }

    @Override
    public void handleByChild(Message msg) {}

    @Override
    public void loadLayout() {
        setContentView(R.layout.v_price);
        lvPrice = findViewById(R.id.lvPrice);
//        tvDefLOT = (TextView)findViewById(R.id.tvDefLot);
        btnEdit = (Button)findViewById(R.id.btnEdit);
        TextView04 = (TextView)findViewById(R.id.TextView04);
        TextView04.setText(res.getString(R.string.db_customize_list));

        String set = setting.getString("CUSTOMIZE_LIST", null);
        if (set != null && set !="")
            custList = new ArrayList<String>(Arrays.asList(set.split(";")));

        if (custList.size() == 0){
            AlertDialog.Builder builder = new AlertDialog.Builder(CustomListActivity.this, CompanySettings.alertDialogTheme);
            builder.setMessage("Please edit and add the customized price list")
                    .setTitle("Customize List");
            builder.setPositiveButton(res.getString(R.string.confirm), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                }
            });
            builder.show();
        }

        if( app.bLogon == true )
        {
            popupContractList = app.data.getTradableContractList();
        }
        else
        {
            popupContractList = app.data.getContractList();
        }

        for (int i = 0; i<popupContractList.size(); i++){
            for (int j=0; j<custList.size(); j++){
                if (popupContractList.get(i).strContractCode.equals(custList.get(j))){
                    custList.set(j, popupContractList.get(i).getContractName(app.locale));
                }
            }
        }

        popContract = new PopupCustomList(getApplicationContext(), findViewById(R.id.rlTop), popupContractList, custList);
//
    }

    @Override
    public void updateUI() {
        List<String> contractSequence = app.data.getContractSequence();
        List<String> tradableContract = app.data.getTradableContract();
        Predicate<ContractObj> filter = contractObj -> contractObj.isViewable() && (CompanySettings.SHOW_NON_TRADEABLE_ITEM || tradableContract.contains(contractObj.strContractCode));
        Comparator<ContractObj> comparator = (contractObj, contractObj1) -> {
            if (contractSequence.contains(contractObj.strContractCode) && contractSequence.contains(contractObj1.strContractCode)) {
                return contractSequence.indexOf(contractObj.strContractCode) - contractSequence.indexOf(contractObj1.strContractCode);
            }
            return contractObj.strContractCode.compareTo(contractObj1.strContractCode);
        };
        List<ContractObj> contractObjList = app.data.getContractList().stream()
                .filter(filter)
                .sorted(comparator)
                .collect(Collectors.toList());
        if (contractAdapter == null) {
            if (BuildConfig.DEBUG)
                Log.d("getContracts", "" + app.data.getContracts().size());
            ListViewOnItemListener listener = new ListViewOnItemListener();
            contractAdapter = new ContractListAdapter(CustomListActivity.this, contractObjList, mService, mServiceMessengerHandler,
                    (c) -> onClickBidAsk.accept(c, true),
                    (c) -> onClickBidAsk.accept(c, false),
                    onClickContract,
                    listener
            );
            lvPrice.setAdapter(contractAdapter);
            lvPrice.setOnItemClickListener(listener);
        } else {
            contractAdapter.reload(contractObjList);
            contractAdapter.notifyDataSetChanged();
        }

        btnEdit.setVisibility(View.VISIBLE);
        updateCustomList();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
    public void onRemove(int which) {
        ArrayList<String> alContract = app.data.getContractSequence();
        synchronized(alContract){
            if (which < 0 || which > alContract.size()) return;
            alContract.remove(which);
        }
    }
    @Override
    public void onDrop(int from, int to) {
        ArrayList<String> alContract = app.data.getContractSequence();
        synchronized(alContract){
            String temp = alContract.get(from);
            alContract.remove(from);
            alContract.add(to,temp);
            contractAdapter.notifyDataSetChanged();
            String sSeq = "";
            for(String sContract : alContract){
                sSeq += sContract + "|";
            }
            setContractSequence(sSeq);
        }
    }

    @Override
    public int getServiceId() {
        return ServiceFunction.SRV_PRICE;
    }

    @Override
    public int getActivityServiceCode(){
        return ServiceFunction.SRV_PRICE;
    }

    private void updateCustomList(){
        String set = setting.getString("CUSTOMIZE_LIST", null);
        if (set != null && set !="") {
            custList = new ArrayList<String>(Arrays.asList(set.split(";")));
        }
    }
}