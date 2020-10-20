package com.mfinance.everjoy.app;

import android.content.ComponentName;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.bo.CashMovementRecord;
import com.mfinance.everjoy.app.bo.CashMovementRecordBuilder;
import com.mfinance.everjoy.app.bo.SystemMessage;
import com.mfinance.everjoy.app.constant.Protocol;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.pojo.CancelCastMovementRequest;
import com.mfinance.everjoy.app.pojo.CancelCastMovementRequestBuilder;
import com.mfinance.everjoy.app.service.internal.CashMovementRequestProcessor;
import com.mfinance.everjoy.app.util.CommonFunction;
import com.mfinance.everjoy.app.util.Utility;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class CashMovementHistoryListActivity extends BaseActivity {
    private ListView lvCashMovementHistory = null;
    private CashMovementHistoryListAdapter cashMovementHistorydapter = null;
    private Map<String, CashMovementRecord> cashMovements = Collections.EMPTY_MAP;
    private Map<String, CashMovementRecord> cashMovementsFiltered = Collections.EMPTY_MAP;
    private Map<Integer, SystemMessage> systemMessageMap = Collections.EMPTY_MAP;
    private CancelCastMovementRequest request, pendingRequest;
    private String fromDate;
    private String toDate;
    private CashMovementRecord selectedRecord;
    private PopupDate popDate;
    private PopupCashMovementDetail popupDetail;

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
        popDate.btnClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                popDate.dismiss();
            }
        });

    }

    @Override
    public void handleByChild(Message msg) {

    }

    @Override
    public void loadLayout() {
        setContentView(R.layout.v_cashmovement_history);
        lvCashMovementHistory = (ListView) findViewById(R.id.lvRecord);
        lvCashMovementHistory.setSelected(true);
        popDate = new PopupDate(getApplicationContext(), findViewById(R.id.rlTop));
        app.data.clearCashMovementRecord();
    }

    @Override
    public void updateUI() {
        Map<String, CashMovementRecord> old = cashMovements;
        cashMovements = app.data.getCashMovementRecords();
        Map<Integer, SystemMessage> systemMessageMapOld = systemMessageMap;
        systemMessageMap = app.data.getSystemMessages();
        if (old != cashMovements) {
            cashMovementsFiltered = new HashMap<>();
            for (Map.Entry<String, CashMovementRecord> entry : cashMovements.entrySet()) {
                BigDecimal amount = BigDecimal.ZERO;
                try {
                    amount = new BigDecimal(entry.getValue().sAmount);
                } catch (Exception ex) {

                }
                if (amount.signum() < 0) {
                    CashMovementRecord record = new CashMovementRecordBuilder(entry.getValue()).setsAmount(amount.negate().toPlainString()).createCashMovementRecord();
                    cashMovementsFiltered.put(entry.getKey(), record);
                }
            }
            if (cashMovementHistorydapter != null) {
                cashMovementHistorydapter.reload(cashMovementsFiltered);
            }
        }
        CancelCastMovementRequest oldRequest = request;
        request = app.data.getCancelCashMovementRequest();
        if (!Objects.equals(oldRequest, request) && pendingRequest != null && request != null) {
            if (Objects.equals(pendingRequest.getRequestID(), request.getRequestID()) && !request.isPending()) {
                app.data.clearCashMovementRecord();
                CommonFunction.enqueryCashMovementHistory(mService, mServiceMessengerHandler, fromDate, toDate);
                this.pendingRequest = null;
            }
        }

        if (popupDetail != null)
            popupDetail.updateUI();
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
        return ServiceFunction.SRV_CASH_MOVEMENT_HISTORY;
    }

    @Override
    public int getActivityServiceCode() {
        return ServiceFunction.SRV_CASH_MOVEMENT_HISTORY;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        super.onServiceConnected(name, service);
        cashMovementHistorydapter = new CashMovementHistoryListAdapter(
                CashMovementHistoryListActivity.this,
                cashMovementsFiltered,
                mService,
                mServiceMessengerHandler,
                (key) -> {
                    selectedRecord = cashMovementsFiltered.get(key);
                    if (popupDetail == null) {
                        popupDetail = new PopupCashMovementDetail(
                                CashMovementHistoryListActivity.this,
                                findViewById(R.id.rlTop),
                                app,
                                mService,
                                mServiceMessengerHandler,
                                () -> selectedRecord,
                                s -> getStatusString(s),
                                () -> {
                                    String requestID = UUID.randomUUID().toString();
                                    Message historyRequestMsg = Message.obtain(null, ServiceFunction.SRV_SEND_CASH_MOVEMENT_REQUEST);
                                    historyRequestMsg.arg1 = CashMovementRequestProcessor.RequestType.CANCEL_REQUEST.getInteger();
                                    historyRequestMsg.replyTo = mServiceMessengerHandler;
                                    HashMap<String, String> request = new HashMap<>();
                                    request.put(Protocol.CashMovementRequest.AMOUNT, new BigDecimal(selectedRecord.sAmount).negate().toPlainString());
                                    request.put(Protocol.CashMovementRequest.BANKACCOUNT, selectedRecord.sBankAccount);
                                    request.put(Protocol.CashMovementRequest.REF, selectedRecord.sRef);
                                    request.put(Protocol.CashMovementRequest.TYPE, "3");
                                    historyRequestMsg.getData().putSerializable("request", request);
                                    historyRequestMsg.getData().putString("requestID", requestID);
                                    CancelCastMovementRequest requestTemp = new CancelCastMovementRequestBuilder().setPending(true).setRequestID(requestID).setSuccessful(false).createCancelCastMovementRequest();
                                    try {
                                        this.pendingRequest = requestTemp;
                                        app.data.setCancelCashMovementRequest(requestTemp);
                                        mService.send(historyRequestMsg);
                                    } catch (RemoteException e) {
                                        Log.e(CommonFunction.class.getSimpleName(), "Unable to send CashMovement message", e.fillInStackTrace());
                                    }
                                    popupDetail.dismiss();
                                }
                        );
                    }
                    popupDetail.showLikeQuickAction();
                },
                s -> getStatusString(s)
        );
        fromDate = Utility.dateToString(app.dtTradeDate);
        toDate = fromDate;
        CommonFunction.enqueryCashMovementHistory(mService, mServiceMessengerHandler, fromDate, toDate);
        lvCashMovementHistory.setAdapter(cashMovementHistorydapter);
        findViewById(R.id.btnToday).setOnClickListener(v -> {
            fromDate = Utility.dateToString(app.dtTradeDate);
            toDate = fromDate;
            app.data.clearCashMovementRecord();
            CommonFunction.enqueryCashMovementHistory(mService, mServiceMessengerHandler, fromDate, toDate);
        });
        popDate.btnCommit.setOnClickListener(v -> {
            String[] values = popDate.getValue();
            fromDate = values[0];
            toDate = values[1];
            popDate.dismiss();
            app.data.clearCashMovementRecord();
            CommonFunction.enqueryCashMovementHistory(mService, mServiceMessengerHandler, fromDate, toDate);
        });
    }

    @Override
    protected void onDestroy() {
        if (popupDetail != null) {
            popupDetail.dismiss();
        }
        super.onDestroy();
    }

    private String getStatusString(String sStatus){
        if ("1".equals(sStatus)) {
            return getResources().getString(R.string.txt_cashmovement_Verified);
        } else if ("2".equals(sStatus)) {
            return getResources().getString(R.string.txt_cashmovement_Confirmed);
        } else if ("3".equals(sStatus)) {
            return getResources().getString(R.string.txt_cashmovement_Rejected);
        } else if ("4".equals(sStatus)) {
            return getResources().getString(R.string.txt_cashmovement_Cancelled);
        } else {
            return getResources().getString(R.string.txt_cashmovement_NA);
        }
    }
}