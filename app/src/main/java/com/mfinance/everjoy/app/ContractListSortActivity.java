package com.mfinance.everjoy.app;


import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.bo.ContractObj;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.util.Utility;
import com.mfinance.everjoy.app.widget.dslv.DragSortController;
import com.mfinance.everjoy.app.widget.dslv.DragSortListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ContractListSortActivity extends BaseActivity {
    ImageView btnSave = null;
    ImageView btnBack = null;
    ImageView btnReset = null;
    DragSortListView listView;
    ArrayAdapter<String> adapter;
    boolean changeFlag = false;

    private DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
        @Override
        public void drop(int from, int to) {
            if (from != to) {
                String item = adapter.getItem(from);
                adapter.remove(item);
                adapter.insert(item, to);
                changeFlag=true;
                btnReset.setVisibility(View.VISIBLE);
            }
        }
    };

    private DragSortListView.RemoveListener onRemove = new DragSortListView.RemoveListener() {
        @Override
        public void remove(int which) {
            adapter.remove(adapter.getItem(which));
            changeFlag=true;
            btnReset.setVisibility(View.VISIBLE);
        }
    };



    @Override
    public void bindEvent() {
        btnSave.setOnClickListener(v -> {
            saveContractSeq();
            ContractListSortActivity.super.onBackPressed();
            Toast.makeText(ContractListSortActivity.this, R.string.lb_successful_save, Toast.LENGTH_LONG).show();
        });

        btnBack.setOnClickListener(v -> saveOrNotBeforeLeave());

        btnReset.setOnClickListener(v -> confirmReset());

    }

    @Override
    public void handleByChild(Message msg) {}

    @Override
    public void loadLayout() {
        setContentView(R.layout.v_list_contract_sort);
        listView = findViewById(R.id.listView);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);
        btnReset = findViewById(R.id.btnReset);

        loadContractList();
    }

    public void loadContractList(){
        List<String> contractList = Utility.getViewableContract(app.data, true).map(c -> c.strContractCode).collect(Collectors.toList());
        //display reset button if the sorting is not same as the default setting
        btnReset.setVisibility(View.INVISIBLE);
        changeFlag = false;
        adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.list_item_contract, R.id.ContractName, contractList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                final View view;
                final TextView text;

                if (convertView == null) {
                    view = inflater.inflate(R.layout.list_item_contract, parent, false);
                } else {
                    view = convertView;
                }
                text = view.findViewById(R.id.ContractName);
                final String item = getItem(position);
                ContractObj contract = app.data.getContract(item);
                if (contract != null) {
                    text.setText(contract.getContractName(getLanguage()));
                }
                return view;
            }
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                return getView(position, convertView, parent);
            }
        };
        listView.setAdapter(adapter);
        listView.setDropListener(onDrop);
        listView.setRemoveListener(onRemove);

        DragSortController controller = new DragSortController(listView);
        controller.setDragHandleId(R.id.ContractName_bg2);
        //controller.setClickRemoveId(R.id.);
        controller.setRemoveEnabled(false);
        controller.setSortEnabled(true);
        controller.setDragInitMode(1);
        //controller.setRemoveMode(removeMode);

        listView.setFloatViewManager(controller);
        listView.setOnTouchListener(controller);
        listView.setDragEnabled(true);
    }

    @Override
    public void updateUI() {
    }

    @Override
    public void onBackPressed() {
        saveOrNotBeforeLeave();
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
    public int getServiceId() {
        return ServiceFunction.SRV_CONTRACT_SORT;
    }

    @Override
    public int getActivityServiceCode(){
        return ServiceFunction.SRV_CONTRACT_SORT;
    }

    public void saveContractSeq(){
        ArrayList<String> sSeq = new ArrayList<>();
        for(int i=0 ; i<adapter.getCount() ; i++) {
            sSeq.add(adapter.getItem(i));
        }
        setContractSequenceSort(sSeq);
    }

    public void saveOrNotBeforeLeave(){
        if(changeFlag) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this,CompanySettings.alertDialogTheme);
            builder.setTitle(R.string.lb_caution);
            builder.setMessage(R.string.msg_save_or_not);
            builder.setPositiveButton(R.string.yes, (dialog, id) -> {
                saveContractSeq();
                ContractListSortActivity.super.onBackPressed();
                Toast.makeText(ContractListSortActivity.this, R.string.lb_successful_save, Toast.LENGTH_LONG).show();
            });
            builder.setNegativeButton(R.string.no, (dialog, id) -> ContractListSortActivity.super.onBackPressed());
            builder.show();
        }else{
            super.onBackPressed();
        }
    }

    public void confirmReset(){
        loadContractList();
    }

    public void setContractSequenceSort(List<String> sSeq){
        SharedPreferences.Editor editor = setting.edit();
        JSONObject o;
        try {
            o = new JSONObject(setting.getString("CONTRACT_SEQUENCE_SORT", "{}"));
        } catch (Exception ex) {
            o = new JSONObject();
        }
        String accountName = app.bLogon ? app.data.getStrUser() : "";
        try {
            o.put(accountName, new JSONArray(sSeq));
        } catch (Exception ignore) {

        }
        editor.putString("CONTRACT_SEQUENCE_SORT", o.toString());
        editor.apply();
    }
}
