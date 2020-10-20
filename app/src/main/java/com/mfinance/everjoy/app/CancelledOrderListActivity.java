package com.mfinance.everjoy.app;

import android.content.ComponentName;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.util.CommonFunction;
import com.mfinance.everjoy.app.util.ListViewAdapterReloader;
import com.mfinance.everjoy.app.util.ListViewOnItemListener;
import com.mfinance.everjoy.app.util.Utility;

/**
 * A view for canceled order list
 *
 * @author : justin.lai
 * @version : v1.00
 * @ModificationHistory Who            When			Version			What<br>
 * -------------------------------------------------------------------------------<br>
 * justin.lai	20110414		v1.00			Creation<br>
 */
public class CancelledOrderListActivity extends BaseActivity {
    /**
     * A view for display canceled order
     */
    private ListView lvCancelledOrder = null;
    /**
     * Adapter for storing canceled order
     */
    private CancelledOrderListAdapter cancelledOrderAdapter = null;
    private ListViewAdapterReloader reloader = null;
    /**
     * A pop up for searching criteria (Date)
     */
    private PopupDate popDate;

    private PopupCancelledOrderDetail popupDetail;

    @Override
    public void bindEvent() {
        findViewById(R.id.btnSelectDate).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                popDate.showLikeQuickAction();
                popDate.setFromToValue(Utility.dateToString(app.dtTradeDate), Utility.dateToString(app.dtTradeDate));
                popDate.setCurrDate(Utility.dateToString(app.dtTradeDate).split("-"));
            }
        });

        findViewById(R.id.btnToday).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String sDate = Utility.dateToEngString(app.dtTradeDate);
                app.data.clearCancelledOrder();
                CommonFunction.equeryCancelledOrder(mService, mServiceMessengerHandler, sDate, sDate);
            }
        });


        popDate.btnCommit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] values = popDate.getValue();
                String sFrom = Utility.dateToEngString(values[0]);
                String sTo = Utility.dateToEngString(values[1]);
                popDate.dismiss();
                app.data.clearCancelledOrder();
                CommonFunction.equeryCancelledOrder(mService, mServiceMessengerHandler, sFrom, sTo);
            }
        });
        popDate.btnClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                popDate.dismiss();
            }
        });
    }

    @Override
    public void handleByChild(Message msg) {
        switch (msg.what) {
            case ServiceFunction.ACT_CANCELLED_ORDER_DETAIL:
                if (popupDetail == null) {
                    popupDetail = new PopupCancelledOrderDetail(CancelledOrderListActivity.this, findViewById(R.id.rlTop), app, mService, mServiceMessengerHandler);
                }
                popupDetail.showLikeQuickAction();
                break;
        }
    }

    @Override
    public void loadLayout() {
        if (CompanySettings.newinterface)
            setContentView(R.layout.v_cancelled_order_new);
        else
            setContentView(R.layout.v_cancelled_order);
        lvCancelledOrder = (ListView) findViewById(R.id.lvOrder);
        lvCancelledOrder.setSelected(true);
        popDate = new PopupDate(getApplicationContext(), findViewById(R.id.rlTop));
    }

    @Override
    public void updateUI() {
        if (cancelledOrderAdapter != null) {
            cancelledOrderAdapter.reload(app.data.getCancelledOrders());
            reloader.reload();
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        super.onServiceConnected(name, service);
        ListViewOnItemListener listener = new ListViewOnItemListener();
        cancelledOrderAdapter = new CancelledOrderListAdapter(CancelledOrderListActivity.this, app.data.getCancelledOrders(), mService, mServiceMessengerHandler, listener);
        lvCancelledOrder.setAdapter(cancelledOrderAdapter);
        lvCancelledOrder.setOnItemClickListener(listener);
        lvCancelledOrder.setItemsCanFocus(true);
        reloader = new ListViewAdapterReloader(lvCancelledOrder, cancelledOrderAdapter);
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
        return ServiceFunction.SRV_CANCELLED_ORDER;
    }

    @Override
    public int getActivityServiceCode() {
        return ServiceFunction.SRV_CANCELLED_ORDER;
    }
}