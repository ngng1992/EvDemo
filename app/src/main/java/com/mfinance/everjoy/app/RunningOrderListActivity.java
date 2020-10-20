package com.mfinance.everjoy.app;

import com.mfinance.everjoy.app.bo.ContractObj;
import com.mfinance.everjoy.app.bo.OpenPositionRecord;
import com.mfinance.everjoy.app.bo.OrderRecord;
import com.mfinance.everjoy.app.bo.TransactionObj;
import com.mfinance.everjoy.app.constant.Protocol;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.service.internal.CancelOrderRequestProcessor;
import com.mfinance.everjoy.app.util.ListViewAdapterReloader;
import com.mfinance.everjoy.app.util.MessageMapping;
import com.mfinance.everjoy.app.util.Utility;
import com.mfinance.everjoy.app.util.function.Consumer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import com.mfinance.everjoy.R;


public class RunningOrderListActivity extends BaseActivity {
    private ListView lvOrder = null;
    private RunningOrderListAdapter orderAdapter = null;
    private PopupPendingOrderDetail popupDetail;
    private ListViewAdapterReloader reloader;
    private Map<Integer, OrderRecord> runningOrders = Collections.EMPTY_MAP;
    private List<RunningOrderItem> items = Collections.EMPTY_LIST;
    private boolean multipleLiquidate = false;
    private Handler mainHandler;

    @Override
    public void bindEvent() {

    }

    @Override
    public void handleByChild(Message msg) {

    }

    @Override
    public void loadLayout() {
        mainHandler = new Handler(getMainLooper());
        if (CompanySettings.newinterface) {
            setContentView(R.layout.v_running_order_new);
            View lSelectionHeader = findViewById(R.id.lSelectionHeader);
            View actionBelow = findViewById(R.id.llactionBelow);
            TextView tvTitle = findViewById(R.id.tvTitle);
            Button btnCancel = findViewById(R.id.btnCancel);
            Button btnToggleMultipleSelection = findViewById(R.id.btnToggleMultipleSelection);
            btnToggleMultipleSelection.setVisibility(CompanySettings.ENABLE_MULTI_CANCEL_ORDER ? View.VISIBLE : View.GONE);
            Runnable updateUI = () -> {
                actionBelow.setVisibility(multipleLiquidate ? View.VISIBLE : View.GONE);
                lSelectionHeader.setVisibility(multipleLiquidate ? View.VISIBLE : View.GONE);
                btnToggleMultipleSelection.setVisibility(multipleLiquidate ? View.GONE : View.VISIBLE);
                tvTitle.setText(getResources().getText(multipleLiquidate ? R.string.title_cancel_multiple_pending_order : R.string.title_pending_order));
                items = items.stream()
                        .map(item -> new RunningOrderItemBuilder(item)
                                .setShowSelection(multipleLiquidate)
                                .setSelected(false)
                                .createRunningOrderItem()
                        )
                        .collect(Collectors.toList());
                orderAdapter.reload(items);
                reloader.reload();
            };
            btnToggleMultipleSelection.setOnClickListener((v) -> {
                multipleLiquidate = !multipleLiquidate;
                updateUI.run();
            });
            btnCancel.setOnClickListener((v) -> {
                multipleLiquidate = false;
                updateUI.run();
            });
            Button btnSubmit = findViewById(R.id.btnSubmit);
            btnSubmit.setOnClickListener((v) -> {
                AlertDialog dialog = new AlertDialog.Builder(this, CompanySettings.alertDialogTheme).create();
                dialog.setMessage(res.getText(R.string.confirm_cancel_order));
                dialog.setButton(
                        DialogInterface.BUTTON_POSITIVE,
                        res.getText(R.string.btn_confirm),
                        (DialogInterface d, int which) -> {
                            Bundle data = new Bundle();
                            data.putIntegerArrayList("ordernos", items.stream()
                                    .filter((item) -> item.selected)
                                    .map(item -> item.iRef)
                                    .collect(Collectors.toCollection(() -> new ArrayList<>()))
                            );
                            Message message = Message.obtain(null, ServiceFunction.SRV_SEND_CANCEL_ORDER_REQUEST);
                            message.arg1 = CancelOrderRequestProcessor.RequestType.CANCEL_MULTIPLE.getValue();
                            message.replyTo = mServiceMessengerHandler;
                            message.setData(data);
                            try {
                                mService.send(message);
                            } catch (RemoteException e) {
                                Log.e(getClass().getSimpleName(), "Unable to send SEND_CANCEL_ORDER_REQUEST_ORDER_REF", e.fillInStackTrace());
                            }
                            multipleLiquidate = false;
                            updateUI.run();
                        });
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, res.getText(R.string.btn_cancel), (DialogInterface d, int which) -> {});
                dialog.show();
            });

        } else {
            setContentView(R.layout.v_running_order);
        }
        lvOrder = findViewById(R.id.lvOrder);
        lvOrder.setSelected(true);
        orderAdapter = new RunningOrderListAdapter(RunningOrderListActivity.this, integer -> {
            RunningOrderItem order;
            try {
                order = items.get(integer);
            } catch (Exception ex) {
                return;
            }

            if (multipleLiquidate) {
                mainHandler.post(() -> {
                    items = items.stream()
                            .map(item -> {
                                if (!item.iRef.equals(order.iRef)) {
                                    return item;
                                }
                                return new RunningOrderItemBuilder(item)
                                        .setSelected(!item.selected)
                                        .createRunningOrderItem();
                            })
                            .collect(Collectors.toList());
                    orderAdapter.reload(items);
                    reloader.reload();
                });
            } else {
                app.setSelectedRunningOrder(order.iRef);
                if (popupDetail == null) {
                    popupDetail = new PopupPendingOrderDetail(RunningOrderListActivity.this, findViewById(R.id.rlTop), app, mService, mServiceMessengerHandler);
                }
                popupDetail.showLikeQuickAction();
            }
        });
        lvOrder.setAdapter(orderAdapter);
        reloader = new ListViewAdapterReloader(lvOrder, orderAdapter);
    }

    @Override
    public void updateUI() {
        Map<Integer, OrderRecord> oldRunningOrder = runningOrders;
        runningOrders = app.data.getRunningOrders();
        if (oldRunningOrder != runningOrders) {
            items = runningOrders.values()
                    .stream()
                    .map((order) -> {
                        RunningOrderItemBuilder builder = items.contains(order.iRef) ? new RunningOrderItemBuilder(items.get(order.iRef)) : new RunningOrderItemBuilder();
                        String curPrice;
                        double[] dBidAsk = order.contract.getBidAsk();
                        if (("B".equals(order.strBuySell) && order.contract.getBSD() == false) || ("S".equals(order.strBuySell) && order.contract.getBSD() == true)) {
                            curPrice = Utility.round(dBidAsk[1], order.contract.iRateDecPt, order.contract.iRateDecPt);
                        } else {
                            curPrice = Utility.round(dBidAsk[0], order.contract.iRateDecPt, order.contract.iRateDecPt);
                        }
                        return builder.setiRef(order.iRef)
                                .setContractName(order.contract.getContractName(getLanguage()))
                                .setTradeDate(Utility.displayListingDate(order.strTradeDate))
                                .setStrBuySell(order.strBuySell)
                                .setiLimitStop(order.iLimitStop)
                                .setLot(Utility.formatLot(order.dAmount / order.contract.iContractSize))
                                .setRequestPrice(Utility.round(order.dRequestRate, order.contract.iRateDecPt, order.contract.iRateDecPt))
                                .setCurPrice(curPrice)
                                .setbChangeBidAsk(order.contract.bChangeBidAsk)
                                .setiBidUpDown(order.contract.iBidUpDown)
                                .setiOCORef(order.iOCORef)
                                .setIsLiq((order.sTargetPosition != null && !"".equals(order.sTargetPosition)))
                                .createRunningOrderItem();
                    })
                    .sorted((i1, i2) -> i1.iRef.compareTo(i2.iRef))
                    .collect(Collectors.toList());
            orderAdapter.reload(items);
            reloader.reload();
        } else {
            items = this.items.stream()
                    .map((item) -> {
                        OrderRecord order = runningOrders.get(item.iRef);
                        String curPrice;
                        double[] dBidAsk = order.contract.getBidAsk();
                        if (("B".equals(order.strBuySell) && order.contract.getBSD() == false) || ("S".equals(order.strBuySell) && order.contract.getBSD() == true)) {
                            curPrice = Utility.round(dBidAsk[1], order.contract.iRateDecPt, order.contract.iRateDecPt);
                        } else {
                            curPrice = Utility.round(dBidAsk[0], order.contract.iRateDecPt, order.contract.iRateDecPt);
                        }
                        return new RunningOrderItemBuilder(item)
                                .setContractName(order.contract.getContractName(getLanguage()))
                                .setTradeDate(Utility.displayListingDate(order.strTradeDate))
                                .setiLimitStop(order.iLimitStop)
                                .setLot(Utility.formatLot(order.dAmount / order.contract.iContractSize))
                                .setRequestPrice(Utility.round(order.dRequestRate, order.contract.iRateDecPt, order.contract.iRateDecPt))
                                .setCurPrice(curPrice)
                                .setbChangeBidAsk(order.contract.bChangeBidAsk)
                                .setiBidUpDown(order.contract.iBidUpDown)
                                .setiOCORef(order.iOCORef)
                                .setIsLiq((order.sTargetPosition != null && !"".equals(order.sTargetPosition)))
                                .createRunningOrderItem();
                    })
                    .collect(Collectors.toList());
            orderAdapter.reload(items);
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