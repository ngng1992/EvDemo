package com.mfinance.everjoy.app;

import com.mfinance.everjoy.app.bo.ContractObj;
import com.mfinance.everjoy.app.bo.OpenPositionRecord;
import com.mfinance.everjoy.app.bo.OrderRecord;
import com.mfinance.everjoy.app.bo.TransactionObj;
import com.mfinance.everjoy.app.constant.Protocol;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.pojo.PriceAlertObject;
import com.mfinance.everjoy.app.service.internal.CancelOrderRequestProcessor;
import com.mfinance.everjoy.app.util.CommonFunction;
import com.mfinance.everjoy.app.util.ListViewAdapterReloader;
import com.mfinance.everjoy.app.util.ListViewOnItemListener;
import com.mfinance.everjoy.app.util.MessageMapping;
import com.mfinance.everjoy.app.util.Utility;
import com.mfinance.everjoy.app.util.function.Consumer;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import com.mfinance.everjoy.R;

public class PriceAlertActivity extends BaseActivity {
    private ListView lvOrder = null;
    private PriceAlertListAdapter priceAlertAdapter = null;
    private PopupPendingOrderDetail popupDetail;
    private ListViewAdapterReloader reloader;
    private List<PriceAlertObject> runningOrders = Collections.EMPTY_LIST;
    private List<PriceAlertObject> priceAlert = Collections.EMPTY_LIST;
    private PriceAlertObject items;
    private boolean multipleLiquidate = false;
    private Handler mainHandler;

    @Override
    public void bindEvent() {
        Button btnGoToNewPriceAlert = findViewById(R.id.btnGoToNewPriceAlert);
        btnGoToNewPriceAlert.setOnClickListener( v -> {
            CommonFunction.moveTo(mService, mServiceMessengerHandler, ServiceFunction.SRV_NEW_PRICE_ALERT, null);
        });
    }

    @Override
    public void handleByChild(Message msg) {

    }

    @Override
    public void loadLayout() {
        mainHandler = new Handler(getMainLooper());
        setContentView(R.layout.v_pricealert);
        lvOrder = findViewById(R.id.lvPriceAlertList);
        lvOrder.setSelected(true);
    }

    @Override
    public void updateUI() {
        runningOrders = app.data.getPriceAlertList();
        priceAlertAdapter.reload(runningOrders, app.data.getContracts());
            reloader.reload();

        if (popupDetail != null) {
            popupDetail.updateUI();
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        super.onServiceConnected(name, service);
        ListViewOnItemListener listener = new ListViewOnItemListener();
        priceAlertAdapter = new PriceAlertListAdapter(PriceAlertActivity.this, integer -> {
            PriceAlertObject order;
            try {
                order = items;
            } catch (Exception ex) {
                return;
            }
        }, app.data.getContracts(), getLanguage(), mService, mServiceMessengerHandler, res);
        lvOrder.setAdapter(priceAlertAdapter);
        reloader = new ListViewAdapterReloader(lvOrder, priceAlertAdapter);
        reloader.reload();
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
        return ServiceFunction.SRV_RUNNING_ORDER;
    }

    @Override
    public int getActivityServiceCode() {
        return ServiceFunction.SRV_RUNNING_ORDER;
    }

    @Override
    protected void onDestroy() {
        if (popupDetail != null) {
            popupDetail.dismiss();
        }
        app.setSelectedRunningOrder(-1);
        super.onDestroy();
    }
}