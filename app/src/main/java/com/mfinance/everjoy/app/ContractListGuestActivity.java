package com.mfinance.everjoy.app;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.pojo.ConnectionStatus;
import com.mfinance.everjoy.app.service.internal.PriceAgentConnectionProcessor;
import com.mfinance.everjoy.app.util.CommonFunction;
import com.mfinance.everjoy.app.util.ListViewAdapterReloader;
import com.mfinance.everjoy.app.util.ListViewOnItemListener;
import com.mfinance.everjoy.app.util.Utility;

import java.util.Collections;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.mfinance.everjoy.app.pojo.ConnectionStatus.CONNECTED;

public class ContractListGuestActivity extends BaseActivity  {
    private ConnectionStatus connectionStatus = ConnectionStatus.INITIAL;
    private int priceAgentConnectionId = -2;
    private ListView lvPrice;
    private ContractListAdapter contractAdapter = null;
    private ListViewAdapterReloader reloader = null;
    private ProgressBar progressBar;
    private ScheduledExecutorService scheduledExecutorService;
    private ScheduledFuture<?> reconnectFuture;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public void loadLayout() {
        this.setContentView(R.layout.v_price_guest);
        Optional<View> btnSort = Optional.ofNullable(findViewById(R.id.btnSort));
        lvPrice = findViewById(R.id.lvPrice);
        progressBar = findViewById(R.id.progressBar);
        btnSort.ifPresent(s -> {
            s.setOnClickListener(v -> {
                PopupMenu popup = new PopupMenu(this, v);
                popup.setOnMenuItemClickListener((item) -> {
                    Bundle data = new Bundle();
                    data.putBoolean(ServiceFunction.REQUIRE_LOGIN, false);
                    switch (item.getItemId()) {
                        case R.id.menuItemSort:
                            CommonFunction.moveTo(mService, mServiceMessengerHandler, ServiceFunction.SRV_CONTRACT_SORT, data);
                            break;
                        default:
                            break;
                    }
                    return true;
                });
                popup.inflate(R.menu.price_setting);
                popup.getMenu().findItem(R.id.menuItemSort).setEnabled(CompanySettings.ENABLE_CONTRACT_SORT).setVisible(CompanySettings.ENABLE_CONTRACT_SORT);
                popup.getMenu().findItem(R.id.menuItemContractSetting).setEnabled(false).setVisible(false);
                popup.show();
            });
            s.setVisibility(CompanySettings.ENABLE_CONTRACT_SORT ? View.VISIBLE : View.GONE);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private int lastSize = 0;
    @Override
    public void updateUI() {
        ConnectionStatus oldConnectStatus = connectionStatus;
        connectionStatus = app.data.getGuestPriceAgentConnectionStatus();
        int oldId = priceAgentConnectionId;
        priceAgentConnectionId = app.data.getGuestPriceAgentConnectionId();
        if (oldConnectStatus != connectionStatus || priceAgentConnectionId != oldId) {
            switch (connectionStatus) {
                case CONNECTED:
                    progressBar.setVisibility(View.GONE);
                    if (reconnectFuture != null && !reconnectFuture.isDone()) {
                        reconnectFuture.cancel(false);
                    }
                    break;
                case DISCONNECTED:
                    progressBar.setVisibility(View.GONE);
                    if (reconnectFuture == null || reconnectFuture.isDone()) {
                        reconnectFuture = scheduledExecutorService.schedule(() -> {
                            Random r = new Random();
                            Message message = Message.obtain(null, ServiceFunction.SRV_GUEST_PRICE_AGENT);
                            message.arg1 = PriceAgentConnectionProcessor.ActionType.RECONNECT.getValue();
                            message.arg2 = r.nextInt();
                            try {
                                mService.send(message);
                            } catch (Exception ex) {

                            }
                        }, 5, TimeUnit.SECONDS);
                    }
                    break;
                default:
                    progressBar.setVisibility(View.GONE);
                    break;
            }
        }
        if (connectionStatus == CONNECTED) {
            contractAdapter.reload(Utility.getViewableContract(app.data, true).collect(Collectors.toList()));
        } else {
            //contractAdapter.reload(Collections.emptyList());
        }
        reloader.reload();
    }

    @Override
    public void bindEvent() {

    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        super.onServiceConnected(name, service);
        ListViewOnItemListener listener = new ListViewOnItemListener();
        contractAdapter = new ContractListAdapter(this, Utility.getViewableContract(app.data, true).collect(Collectors.toList()), mService, mServiceMessengerHandler, (v) -> {}, (v) -> {}, (selectedContract) -> {
            Bundle data = new Bundle();
            data.putBoolean(ServiceFunction.REQUIRE_LOGIN, false);
            data.putString(ServiceFunction.CONTRACT_DETAIL_CONTRACT, selectedContract.strContractCode);
            data.putBoolean("guest", true);
            CommonFunction.moveTo(mService, mServiceMessengerHandler, ServiceFunction.SRV_CONTRACT_DETAIL, data);
        }, listener);
        lvPrice.setAdapter(contractAdapter);
        lvPrice.setItemsCanFocus(true);
        lvPrice.setOnItemClickListener(listener);
        reloader = new ListViewAdapterReloader(lvPrice, contractAdapter);
        reloader.reload();

        connectionStatus = app.data.getGuestPriceAgentConnectionStatus();
        switch (connectionStatus) {
            case DISCONNECTED:
            case INITIAL:
                progressBar.setVisibility(View.VISIBLE);
                Random r = new Random();
                Message message = Message.obtain(null, ServiceFunction.SRV_GUEST_PRICE_AGENT);
                message.arg1 = PriceAgentConnectionProcessor.ActionType.CONNECT.getValue();
                message.arg2 = r.nextInt();
                try {
                    mService.send(message);
                } catch (Exception ex) {

                }
                break;
            case CONNECTED:
                progressBar.setVisibility(View.GONE);
                break;
            default:
                progressBar.setVisibility(View.GONE);
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        scheduledExecutorService.shutdown();
    }

    @Override
    public void handleByChild(Message msg) {

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
        return ServiceFunction.SRV_PRICE;
    }

    @Override
    public int getActivityServiceCode(){
        return ServiceFunction.SRV_PRICE;
    }
}
