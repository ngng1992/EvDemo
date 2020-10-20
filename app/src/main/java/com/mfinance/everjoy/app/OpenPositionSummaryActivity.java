package com.mfinance.everjoy.app;


import android.content.ComponentName;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.util.CommonFunction;
import com.mfinance.everjoy.app.util.ListViewAdapterReloader;
import com.mfinance.everjoy.app.util.ListViewOnItemListener;

public class OpenPositionSummaryActivity extends BaseActivity {
    private ListView lvSummary = null;
    private ListViewAdapterReloader reloader = null;
    private OpenPositionSummaryAdapter summaryAdapter = null;
    private Button btnGoToLiquidateAll;
    private String selectedContract = "";
    private String selectedBuySell = "B";
    private Runnable goToOpenPositionLiquidate = () -> {
        Bundle data = new Bundle();
        data.putString(ServiceFunction.SRV_OPEN_POSITION_BUY_SELL, selectedBuySell);
        data.putString(ServiceFunction.SRV_OPEN_POSITION_CONTRACT, selectedContract);
        data.putBoolean(ServiceFunction.SRV_OPEN_POSITION_FROM_SUMMARY, true);
        CommonFunction.moveTo(mService, mServiceMessengerHandler, ServiceFunction.SRV_OPEN_POSITION, data);
    };
    private Runnable goToLiquidateAll = () -> {
        Bundle data = new Bundle();
        data.putString(ServiceFunction.SRV_OPEN_POSITION_BUY_SELL, selectedBuySell);
        data.putString(ServiceFunction.SRV_OPEN_POSITION_CONTRACT, selectedContract);
        data.putBoolean("allowMultipleSelection", true);
        goTo(ServiceFunction.SRV_LIQUIDATE_ALL, data);
    };
    private PopupOpenPositingSummaryDetail popupDetail;

    @Override
    public void bindEvent() {
    }


    @Override
    public void handleByChild(Message msg) {

    }

    @Override
    public void loadLayout() {
        if (CompanySettings.newinterface)
            setContentView(R.layout.v_open_summary_new);
        else
            setContentView(R.layout.v_open_summary);
        View vGoToLiquidateAll = findViewById(R.id.vGoToLiquidateAll);
        vGoToLiquidateAll.setVisibility(CompanySettings.ENABLE_LIQ_ALL ? View.VISIBLE : View.GONE);
        btnGoToLiquidateAll = findViewById(R.id.btnGoToLiquidateAll);
        btnGoToLiquidateAll.setOnClickListener(v -> goTo(ServiceFunction.SRV_LIQUIDATE_ALL));
        lvSummary = findViewById(R.id.lvOpenSummary);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        super.onServiceConnected(name, service);
        if (summaryAdapter == null) {
            ListViewOnItemListener listener = new ListViewOnItemListener();
            summaryAdapter = new OpenPositionSummaryAdapter(this,
                    app.data.getContracts(),
                    mService,
                    mServiceMessengerHandler,
                    (contract, buySell) -> {
                        if (popupDetail == null) {
                            popupDetail = new PopupOpenPositingSummaryDetail(
                                    this,
                                    findViewById(R.id.rlTop),
                                    app,
                                    mService,
                                    mServiceMessengerHandler,
                                    () -> {
                                        if (CompanySettings.ENABLE_MULTIPLE_LIQ_PARTIAL_LIQ) {
                                            goToLiquidateAll.run();
                                        } else {
                                            goToOpenPositionLiquidate.run();
                                        }
                                        popupDetail.dismiss();
                                    }
                            );
                        }
                        selectedContract = contract;
                        selectedBuySell = buySell;
                        app.setSelectedContract(contract);
                        app.setSelectedBuySell(buySell);
                        popupDetail.showLikeQuickAction();
                    },
                    listener);
            lvSummary.setAdapter(summaryAdapter);
            lvSummary.setItemsCanFocus(true);
            reloader = new ListViewAdapterReloader(lvSummary, summaryAdapter);
            reloader.reload();
        }
    }

    @Override
    public void updateUI() {
        if (summaryAdapter != null) {
            summaryAdapter.reload(app.data.getContracts());
            reloader.reload();
        }
        if (popupDetail != null) {
            popupDetail.updateUI();
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
        return ServiceFunction.SRV_OPEN_POSITION_SUMMARY;
    }

    @Override
    public int getActivityServiceCode() {
        return ServiceFunction.SRV_OPEN_POSITION_SUMMARY;
    }
}