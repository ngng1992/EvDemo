package com.mfinance.everjoy.app;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import android.content.Context;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;


import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.bo.ContractObj;
import com.mfinance.everjoy.app.util.GUIUtility;
import com.mfinance.everjoy.app.util.Utility;
import com.mfinance.everjoy.app.widget.quickaction.CustomPopupWindow;
import com.mfinance.everjoy.app.widget.wheel.OnWheelChangedListener;
import com.mfinance.everjoy.app.widget.wheel.WheelView;
import com.mfinance.everjoy.app.widget.wheel.adapter.ArrayWheelAdapter;

public class PopupCustomList {
    Context context;
    public CustomPopupWindow popup;
    public WheelView wvAllContract;
    public WheelView wvCustContract;

    String[] months = new String[12];
    Button btnSelected = null;
    public Button btnCommit;
    public Button btnClose;
    ArrayList<String> alName;
    ArrayList<String> alCustName;

    public ImageView btnAdd;
    public ImageView btnRemove;

    public PopupCustomList(Context context, View vParent, ArrayList<ContractObj> alContract, ArrayList<String> alCustName){
        this.context = context;
        popup = new CustomPopupWindow(vParent);
        popup.setContentView(R.layout.popup_customlist);
        alName = getContractName(alContract);
        this.alCustName = alCustName;

        wvAllContract = (WheelView) popup.findViewById(R.id.p1);
        wvCustContract = (WheelView) popup.findViewById(R.id.p3);

        alName.removeAll(alCustName);
        updateWheel();

        btnCommit = (Button)popup.findViewById(R.id.btnOK);
        btnClose = (Button)popup.findViewById(R.id.btnClose);

        btnAdd = (ImageView)popup.findViewById(R.id.btnAdd);
        btnRemove = (ImageView)popup.findViewById(R.id.btnRemove);

        bindEvent();
    }

    public ArrayList<String> getContractName(ArrayList<ContractObj> alContract){
        Locale l = LocaleUtility.getLanguage(PreferenceManager.getDefaultSharedPreferences(context));
        ArrayList<String> alName = new ArrayList<String>();
        for(ContractObj contract: alContract){
            alName.add(contract.getContractName(l));
        }
        return alName;
    }

    public void bindEvent(){

        btnAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (alName.size()>0) {
                    alName.get(wvAllContract.getCurrentItem());
                    System.out.println(alName.get(wvAllContract.getCurrentItem()));
                    alCustName.add(alName.get(wvAllContract.getCurrentItem()));
                    alName.remove(wvAllContract.getCurrentItem());
                    updateWheel();
                }
            }
        });

        btnRemove.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (alCustName.size()>0) {
                    alCustName.get(wvCustContract.getCurrentItem());
                    System.out.println(alCustName.get(wvCustContract.getCurrentItem()));
                    alName.add(alCustName.get(wvCustContract.getCurrentItem()));
                    alCustName.remove(wvCustContract.getCurrentItem());
                    updateWheel();
                }
            }
        });

    }

    public void showLikeQuickAction(){
        updateWheel();
        popup.showLikeQuickAction();
    }
    public void dismiss(){
        popup.dismiss();
    }

    public void updateWheel(){
        GUIUtility.initWheel(context, wvAllContract, alName, 5, 0);
        GUIUtility.initWheel(context, wvCustContract, alCustName, 5, 0);
    }

}
