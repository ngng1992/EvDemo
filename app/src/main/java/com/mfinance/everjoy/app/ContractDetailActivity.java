package com.mfinance.everjoy.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mfinance.chart.library.ChartController;
import com.mfinance.chart.library.ChartControllerBuilder;
import com.mfinance.chart.library.ChartControllerListener;
import com.mfinance.chart.library.Plot;
import com.mfinance.chart.library.TimeScale;
import com.mfinance.everjoy.BuildConfig;
import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.bo.ContractObj;
import com.mfinance.everjoy.app.bo.OpenPositionRecord;
import com.mfinance.everjoy.app.bo.TransactionObj;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.model.DataRepository;
import com.mfinance.everjoy.app.pojo.ConnectionStatus;
import com.mfinance.everjoy.app.service.internal.PriceAgentConnectionProcessor;
import com.mfinance.everjoy.app.util.ColorController;
import com.mfinance.everjoy.app.util.CommonFunction;
import com.mfinance.everjoy.app.util.MessageMapping;
import com.mfinance.everjoy.app.util.Utility;
import com.mfinance.everjoy.app.widget.quickaction.CustomPopupWindow;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A view for display contract detail
 *
 * @author : justin.lai
 * @version : v1.00
 * @ModificationHistory Who            When			Version			What<br>
 * -------------------------------------------------------------------------------<br>
 * justin.lai	20110413		v1.00			Creation<br>
 * <p>
 * <p>
 * *
 */
public class ContractDetailActivity extends BaseActivity implements
        ChartControllerListener, OnClickListener {
    ChartController chartController;
    List<ContractObj> popupContractList;
    ArrayList<OpenPositionRecord> pendingLiqList;
    private ConnectionStatus connectionStatus = ConnectionStatus.INITIAL;
    private int priceAgentConnectionId = -2;
    private boolean guest = false;
    private String pid;
    private boolean isNewPosition = true;
    /**
     * Exsiting contract
     */
    private ContractObj contract;
    /**
     * A pop up for contract selection
     */
    private PopupContract popContract;
    /**
     * A pop up for good till selection
     */
    private PopupString popPeriod;
    /**
     * A pop up for chart type selection
     */
    private PopupString popType;
    /**
     * A button for trigger Contract pop up
     */
    private ImageButton btnContract;
    private Button btnContract_b;
    private TextView label_contract;
    private ImageButton btnPriceAlert;
    /**
     * A button for trigger Period pop up
     */
    private Button btnPeriod;
    /**
     * A button for trigger Type pop up
     */
    private Button btnType;
    /**
     * A pop up for display action pop up
     */
    private CustomPopupWindow popAction;
    private Button btnAction;
    private ScheduledExecutorService scheduledExecutorService;
    private ScheduledFuture<?> reconnectFuture;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    }

    DialogAdapter dialog = null;

    String[] periodMap = {"Minute", "FiveMinute", "Hourly", "Daily", "Weekly", "Monthly"};

    public static class DialogAdapter {

        View v;
        Activity a;

        public DialogAdapter(Activity a, View v) {
            this.v = v;
            this.a = a;
        }

        public static DialogAdapter show(Activity a, View v) {
            DialogAdapter d = new DialogAdapter(a, v);
            d.show();
            return d;
        }

        public void dismiss() {
            a.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    v.setVisibility(View.GONE);
                }
            });
        }

        public void show() {
            a.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    v.setVisibility(View.VISIBLE);
                }
            });
        }

        public boolean isShowing() {
            return v.getVisibility() == View.VISIBLE ? true : false;
        }
    }

    private Runnable updateChart = new Runnable() {

        @Override
        public void run() {
            //dialog = ProgressDialog.show(ContractDetailActivity.this, "", res.getString(R.string.please_wait), true);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
					/*
					public static int MINUTE_DATA=1;
					public static int FIVE_MINUTE_DATA=6;
					public static int HOUR_DATA=2;
					public static int DAY_DATA=3;
					public static int WEEK_DATA=4;
					public static int MONTH_DATA=5;
					*/

                    if (app.getSelectedContract() == null) {
                        hideChart();
                        return;
                    }

                    int iType = popPeriod.getIndex(btnPeriod.getText().toString());

                    try {
                        if (app.data.isChartAva(app.getSelectedContract().strContractCode)) {
                            if (BuildConfig.DEBUG)
                                Log.i("isChartAva", "show Chart: " + app.getSelectedContract().strContractCode);
                            showLoading();
                            changeSeries(app.getSelectedContract().strContractCode, iType);
                        } else {
                            if (BuildConfig.DEBUG)
                                Log.i("isChartAva", "Hide Chart: " + app.getSelectedContract().strContractCode);
                            hideChart();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        hideChart();
                    }
                }
            });
        }
    };

    @Override
    public void bindEvent() {
        btnAction.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                popAction.showLikeQuickAction();
            }
        });

        popAction.findViewById(R.id.btnClose).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                popAction.dismiss();
            }
        });

        popAction.findViewById(R.id.llBuy).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle data = new Bundle();
                data.putString(ServiceFunction.DEAL_CONTRACT, app.getSelectedContract().strContractCode);
                data.putString(ServiceFunction.DEAL_BUY_SELL, "B");
                CommonFunction.moveTo(mService, mServiceMessengerHandler, ServiceFunction.SRV_DEAL, data);
                popAction.dismiss();
            }
        });

        popAction.findViewById(R.id.llSell).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle data = new Bundle();
                data.putString(ServiceFunction.DEAL_CONTRACT, app.getSelectedContract().strContractCode);
                data.putString(ServiceFunction.DEAL_BUY_SELL, "S");
                CommonFunction.moveTo(mService, mServiceMessengerHandler, ServiceFunction.SRV_DEAL, data);
                popAction.dismiss();
            }
        });

        popAction.findViewById(R.id.llBuyLimit).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle data = new Bundle();
                data.putString(ServiceFunction.ORDER_CONTRACT, app.getSelectedContract().strContractCode);
                data.putString(ServiceFunction.ORDER_BUY_SELL, "B");
                data.putInt(ServiceFunction.ORDER_LIMIT_STOP, 0);
                data.putInt(ServiceFunction.ORDER_DEAL_REF, -1);
                CommonFunction.moveTo(mService, mServiceMessengerHandler, ServiceFunction.SRV_ORDER, data);
                popAction.dismiss();
            }
        });

        popAction.findViewById(R.id.llSellLimit).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle data = new Bundle();
                data.putString(ServiceFunction.ORDER_CONTRACT, app.getSelectedContract().strContractCode);
                data.putString(ServiceFunction.ORDER_BUY_SELL, "S");
                data.putInt(ServiceFunction.ORDER_LIMIT_STOP, 0);
                data.putInt(ServiceFunction.ORDER_DEAL_REF, -1);
                CommonFunction.moveTo(mService, mServiceMessengerHandler, ServiceFunction.SRV_ORDER, data);
                popAction.dismiss();
            }
        });

        popAction.findViewById(R.id.llBuyStop).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle data = new Bundle();
                data.putString(ServiceFunction.ORDER_CONTRACT, app.getSelectedContract().strContractCode);
                data.putString(ServiceFunction.ORDER_BUY_SELL, "B");
                data.putInt(ServiceFunction.ORDER_LIMIT_STOP, 1);
                data.putInt(ServiceFunction.ORDER_DEAL_REF, -1);
                CommonFunction.moveTo(mService, mServiceMessengerHandler, ServiceFunction.SRV_ORDER, data);
                popAction.dismiss();
            }
        });

        popAction.findViewById(R.id.llSellStop).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle data = new Bundle();
                data.putString(ServiceFunction.ORDER_CONTRACT, app.getSelectedContract().strContractCode);
                data.putString(ServiceFunction.ORDER_BUY_SELL, "S");
                data.putInt(ServiceFunction.ORDER_LIMIT_STOP, 1);
                data.putInt(ServiceFunction.ORDER_DEAL_REF, -1);
                CommonFunction.moveTo(mService, mServiceMessengerHandler, ServiceFunction.SRV_ORDER, data);
                popAction.dismiss();
            }
        });


        findViewById(R.id.rlBid).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!app.bLogon)
                    return;

                if (isOneClickTradeEnable()) {
                    //CommonFunction.sendDealRequest(mService, mServiceMessengerHandler, contract, "S", ContractDetailActivity.this.getDefaultLOT());
                    //Toast.makeText(v.getContext(), "Sell deal request sent", Toast.LENGTH_LONG).show();
                    if (!app.getPriceAgentConnectionStatus()) {
                        Toast.makeText(ContractDetailActivity.this, MessageMapping.getMessageByCode(res, "307", app.locale), Toast.LENGTH_LONG).show();
                        return;
                    }

                    String s1 = getString(R.string.lb_bid);
                    String s2 = contract.getContractName(getLanguage());
                    String s3 = getString(R.string.lb_lot);
                    String s4 = ContractDetailActivity.this.getDefaultLOT();
                    String s5 = getString(R.string.are_you_sure);
                    AlertDialog dialog = new AlertDialog.Builder(ContractDetailActivity.this, CompanySettings.alertDialogTheme).create();
                    if (contract.getBSD() == true)
                        dialog.setTitle(R.string.pop_action_buy);
                    else
                        dialog.setTitle(R.string.pop_action_sell);
                    dialog.setMessage(String.format("%s %s\n%s %s\n%s", s1, s2, s3, s4, s5));
                    dialog.setButton(DialogInterface.BUTTON_POSITIVE, res.getText(R.string.yes),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    pid = CommonFunction.getTransactionID(DataRepository.getInstance().getBalanceRecord().strAccount);
                                    isNewPosition = true;
                                    if (contract.getBSD() == true)
                                        CommonFunction.sendDealRequest(mService, mServiceMessengerHandler, contract, "B", ContractDetailActivity.this.getDefaultLOT(), pid, Long.toString(app.dServerDateTime.getTime()));
                                    else
                                        CommonFunction.sendDealRequest(mService, mServiceMessengerHandler, contract, "S", ContractDetailActivity.this.getDefaultLOT(), pid, Long.toString(app.dServerDateTime.getTime()));
                                    Toast.makeText(ContractDetailActivity.this, res.getString(R.string.msg_request_sent), Toast.LENGTH_LONG).show();
                                }
                            }
                    );

                    dialog.setButton(DialogInterface.BUTTON_NEGATIVE, res.getText(R.string.no),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }
                    );

                    dialog.show();

                } else {
                    Bundle data = new Bundle();
                    data.putString(ServiceFunction.DEAL_CONTRACT, app.getSelectedContract().strContractCode);
                    if (contract.getBSD() == true)
                        data.putString(ServiceFunction.DEAL_BUY_SELL, "B");
                    else
                        data.putString(ServiceFunction.DEAL_BUY_SELL, "S");
                    CommonFunction.moveTo(mService, mServiceMessengerHandler, ServiceFunction.SRV_DEAL, data);
                }
            }
        });

        findViewById(R.id.rlAsk).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!app.bLogon)
                    return;

                if (isOneClickTradeEnable()) {
                    //CommonFunction.sendDealRequest(mService, mServiceMessengerHandler, contract, "B", ContractDetailActivity.this.getDefaultLOT());
                    //Toast.makeText(v.getContext(), "Buy deal request sent", Toast.LENGTH_LONG).show();
                    if (!app.getPriceAgentConnectionStatus()) {
                        Toast.makeText(ContractDetailActivity.this, MessageMapping.getMessageByCode(res, "307", app.locale), Toast.LENGTH_LONG).show();
                        return;
                    }

                    String s1 = getString(R.string.lb_ask);
                    String s2 = contract.getContractName(getLanguage());
                    String s3 = getString(R.string.lb_lot);
                    String s4 = ContractDetailActivity.this.getDefaultLOT();
                    String s5 = getString(R.string.are_you_sure);
                    AlertDialog dialog = new AlertDialog.Builder(ContractDetailActivity.this, CompanySettings.alertDialogTheme).create();
                    if (contract.getBSD() == true)
                        dialog.setTitle(R.string.pop_action_sell);
                    else
                        dialog.setTitle(R.string.pop_action_buy);
                    dialog.setMessage(String.format("%s %s\n%s %s\n%s", s1, s2, s3, s4, s5));
                    dialog.setButton(DialogInterface.BUTTON_POSITIVE, res.getText(R.string.yes),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    pid = CommonFunction.getTransactionID(DataRepository.getInstance().getBalanceRecord().strAccount);
                                    isNewPosition = true;
                                    if (contract.getBSD() == true)
                                        CommonFunction.sendDealRequest(mService, mServiceMessengerHandler, contract, "S", ContractDetailActivity.this.getDefaultLOT(), pid, Long.toString(app.dServerDateTime.getTime()));
                                    else
                                        CommonFunction.sendDealRequest(mService, mServiceMessengerHandler, contract, "B", ContractDetailActivity.this.getDefaultLOT(), pid, Long.toString(app.dServerDateTime.getTime()));
                                    Toast.makeText(ContractDetailActivity.this, res.getString(R.string.msg_request_sent), Toast.LENGTH_LONG).show();
                                }
                            }
                    );

                    dialog.setButton(DialogInterface.BUTTON_NEGATIVE, res.getText(R.string.no),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }
                    );
                    dialog.show();

                } else {
                    Bundle data = new Bundle();
                    data.putString(ServiceFunction.DEAL_CONTRACT, app.getSelectedContract().strContractCode);
                    if (contract.getBSD() == true)
                        data.putString(ServiceFunction.DEAL_BUY_SELL, "S");
                    else
                        data.putString(ServiceFunction.DEAL_BUY_SELL, "B");
                    CommonFunction.moveTo(mService, mServiceMessengerHandler, ServiceFunction.SRV_DEAL, data);
                }
            }
        });

        findViewById(R.id.btnAskLiq).setOnClickListener(this);

        findViewById(R.id.btnBidLiq).setOnClickListener(this);

        findViewById(R.id.btnChart).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openChart(false);
            }
        });

        if (CompanySettings.newinterface) {
            btnContract.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    popContract.updateSelectedContract(null, label_contract.getText().toString());
                    popContract.showLikeQuickAction();
                }
            });

            btnPriceAlert.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle data = new Bundle();
                    data.putString(ServiceFunction.PRICE_ALERT_CONTRACT, app.getSelectedContract().strContractCode);
                    CommonFunction.moveTo(mService, mServiceMessengerHandler, ServiceFunction.SRV_NEW_PRICE_ALERT, data);
                }
            });
        } else {
            btnContract_b.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    popContract.updateSelectedContract(null, btnContract_b.getText().toString());
                    popContract.showLikeQuickAction();
                }
            });

            findViewById(R.id.btnReload).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    handler.post(updateChart);
                }
            });
            findViewById(R.id.btnBackToContractList).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    goTo(ServiceFunction.SRV_PRICE);
                }
            });
        }

        popContract.btnCommit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String sContract = popContract.getValue();
                popContract.dismiss();
                // do not look up contract by name since it may not be unique!
                ContractObj contractObj = popupContractList.get(popContract.getSelectedIndex());
                if (contractObj == null) {
                    return;
                }
                app.setSelectedContract(contractObj);
                if (CompanySettings.newinterface)
                    label_contract.setText(sContract);
                else
                    btnContract_b.setText(sContract);
                handler.post(updateChart);
            }
        });

        popContract.btnClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                popContract.dismiss();
            }
        });

        btnPeriod.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                popPeriod.setSelected(btnPeriod.getText().toString());
                popPeriod.showLikeQuickAction();
            }
        });

        popPeriod.btnCommit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                popPeriod.dismiss();
                String sPeriod = popPeriod.getValue();
                btnPeriod.setText(sPeriod);

                handler.post(updateChart);
            }
        });

        popPeriod.btnClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                popPeriod.dismiss();
            }
        });

        btnType.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                popType.setSelected(btnType.getText().toString());
                popType.showLikeQuickAction();
            }
        });

        popType.btnCommit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                popType.dismiss();
                String sType = popType.getValue();
                changeType(sType);
            }
        });

        popType.btnClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                popType.dismiss();
            }
        });

        orientationListener = new OrientationEventListener(this, SensorManager.SENSOR_DELAY_UI) {
            public void onOrientationChanged(int orientation) {
                if (isLandscape == null) {
                    isLandscape = !isPortrait(orientation);
                    return;
                }

                if (!isLandscape && isLandscape(orientation)) {
                    openChart(true);
                } else {
                    isLandscape = isLandscape(orientation);
                }
            }
        };
    }

    String[] alTypeStringMap = {"Line", "Candle", "OHLC"};

    public void changeType(String sType) {
        btnType.setText(sType);

        popType.setSelected(sType);
        int iType = popType.getIndex(sType);
        chartController.changePlot(Plot.valueOf(alTypeStringMap[iType]));
    }

    @Override
    public void handleByChild(Message msg) {
        if (msg.what == ServiceFunction.ACT_TRADER_LIQUIDATE_RETURN) {
            synchronized (pendingLiqList) {
                for (int i = 0; i < pendingLiqList.size(); i++) {
                    if (pendingLiqList.get(i).iRef == Integer.parseInt(msg.getData().getString("liqref"))) {
                        pendingLiqList.remove(i);
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void loadLayout() {
        if (CompanySettings.newinterface)
            setContentView(R.layout.v_price_detail_new);
        else
            setContentView(R.layout.v_price_detail);

        contract = app.getSelectedContract();
        guest = getIntent().getExtras().getBoolean("guest", false);
        connectionStatus = app.data.getGuestPriceAgentConnectionStatus();

        if (app.bLogon) {
            popupContractList = Utility.getViewableContract(app.data).collect(Collectors.toList());
        } else {
            popupContractList = Utility.getViewableContract(app.data, true).collect(Collectors.toList());
        }
        popContract = new PopupContract(getApplicationContext(), findViewById(R.id.rlTop), popupContractList);
        if (CompanySettings.newinterface) {
            btnContract = (ImageButton) findViewById(R.id.btnContract);
            label_contract = (TextView) findViewById(R.id.label_contract);
            btnPriceAlert = (ImageButton) findViewById(R.id.btnPriceAlert);
            if (!CompanySettings.ENABLE_PRICE_ALERT || !app.bLogon)
                btnPriceAlert.setVisibility(View.INVISIBLE);
        } else
            btnContract_b = (Button) findViewById(R.id.btnContract);

        CharSequence[] alPeriod = res.getTextArray(R.array.sp_chart_period);
        popPeriod = new PopupString(getApplicationContext(), findViewById(R.id.rlTop), Utility.toArrayList(alPeriod));
        popPeriod.setSelected(alPeriod[CompanySettings.CHART_DEFAULT_SELECTION[0]].toString());
        btnPeriod = (Button) findViewById(R.id.btnPeriod);
        btnPeriod.setText(alPeriod[CompanySettings.CHART_DEFAULT_SELECTION[0]]);

        CharSequence[] alType = res.getTextArray(R.array.sp_chart_type);
        popType = new PopupString(getApplicationContext(), findViewById(R.id.rlTop), Utility.toArrayList(alType));
        popType.setSelected(alType[CompanySettings.CHART_DEFAULT_SELECTION[1]].toString());
        btnType = (Button) findViewById(R.id.btnType);
        btnType.setText(alType[CompanySettings.CHART_DEFAULT_SELECTION[1]]);

        int arrayId = Utility.getArrayIdById("sp_chart_period_map");

        if (arrayId != -1)
            periodMap = getResources().getStringArray(arrayId);

        initActionPopUp();

        String chartUrl = app.chartDomain != null ? app.chartDomain : CompanySettings.CHART_URL;
        try {
            chartController = new ChartControllerBuilder(this, R.id.chartView)
                    .setUpColor(CompanySettings.chartUpColor)
                    .setDownColor(CompanySettings.chartDownColor)
                    .setChartControllerListener(this)
                    .setDomain(chartUrl)
                    .setPort(app.CHART_PORT)
                    .setRealTimePort(app.REALTIME_CHART_PORT)
                    .create();
        } catch (Exception e) {
            e.printStackTrace();
        }
        btnAction = findViewById(R.id.btnAction);
        loginOrLogoutScreenUpdate();
    }

    public void changeSeries(String symbol, int iScale) {
        String currencies = Utility.getChartCode(symbol);

        if (currencies != null) {
            symbol = currencies;
        }

        chartController.changeSeries(symbol, TimeScale.valueOf(periodMap[iScale]));
        contract = app.getSelectedContract();
        if (contract == null) {
            return;
        }
    }

    /**
     * Initial action pop up
     */
    private void initActionPopUp() {
        popAction = new CustomPopupWindow(findViewById(R.id.btnAccount));
        popAction.setContentView(R.layout.popup_action);
    }

    @Override
    public void updateUI() {
        contract = app.getSelectedContract();
        if (contract == null) {
            return;
        }

        if (guest) {
            ConnectionStatus oldConnectStatus = connectionStatus;
            connectionStatus = app.data.getGuestPriceAgentConnectionStatus();
            int oldId = priceAgentConnectionId;
            priceAgentConnectionId = app.data.getGuestPriceAgentConnectionId();
            if (oldConnectStatus != connectionStatus || priceAgentConnectionId != oldId) {
                switch (connectionStatus) {
                    case CONNECTED:
                        if (reconnectFuture != null && !reconnectFuture.isDone()) {
                            reconnectFuture.cancel(false);
                        }
                        break;
                    case DISCONNECTED:
                        if (reconnectFuture != null || reconnectFuture.isDone()) {
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
                }
            }
        }
        double[] dBidAsk = contract.getBidAsk();

        TextView tvBid1 = (TextView) findViewById(R.id.tvBid1);
        TextView tvBid2 = (TextView) findViewById(R.id.tvBid2);
        TextView tvAsk1 = (TextView) findViewById(R.id.tvAsk1);
        TextView tvAsk2 = (TextView) findViewById(R.id.tvAsk2);

        TextView tvHigh = (TextView) findViewById(R.id.tvHigh);
        TextView tvLow = (TextView) findViewById(R.id.tvLow);

        String sBid = Utility.round(dBidAsk[0], contract.iRateDecPt, contract.iRateDecPt);
        String sAsk = Utility.round(dBidAsk[1], contract.iRateDecPt, contract.iRateDecPt);

        if (contract.dHigh != -1) {
            String sHigh = Utility.round(contract.dHigh, contract.iRateDecPt, contract.iRateDecPt);
            String sLow = Utility.round(contract.dLow, contract.iRateDecPt, contract.iRateDecPt);

            tvHigh.setText(sHigh);
            tvLow.setText(sLow);
        } else if (contract.dHighBid != -1) {
            String sHighBid = Utility.round(contract.dHighBid, contract.iRateDecPt, contract.iRateDecPt);
            String sLowBid = Utility.round(contract.dLowBid, contract.iRateDecPt, contract.iRateDecPt);
            String sHighAsk = Utility.round(contract.dHighAsk, contract.iRateDecPt, contract.iRateDecPt);
            String sLowAsk = Utility.round(contract.dLowAsk, contract.iRateDecPt, contract.iRateDecPt);

            Function<String, String> formatAsk = (s) -> {
                int pt_pos = s.indexOf(".");
                int iLen = s.length();

                if (pt_pos == -1 || pt_pos < iLen - 2)
                    pt_pos = 2;
                else
                    pt_pos = iLen - pt_pos + 1;
                return s.substring(iLen - pt_pos);
            };

            String sHigh = sHighBid + "/" + formatAsk.apply(sHighAsk);
            String sLow = sLowBid + "/" + formatAsk.apply(sLowAsk);

            tvHigh.setText(sHigh);
            tvLow.setText(sLow);
        } else {
            tvHigh.setText("-");
            tvLow.setText("-");
        }

        ColorController.updateRate(
                tvBid1,
                tvBid2,
                sBid
        );

        ColorController.updateRate(
                tvAsk1,
                tvAsk2,
                sAsk
        );

        if (contract.bChangeBidAsk) {
            //ColorController.setPriceColor(res, contract.iBidUpDown, (ViewGroup)findViewById(R.id.llRate));
            ColorController.updateBackground(res, contract.iBidUpDown, findViewById(R.id.rlBid), R.drawable.pd_normal_bid, R.drawable.pd_up_bid, R.drawable.pd_down_bid);
            ColorController.updateBackground(res, contract.iBidUpDown, findViewById(R.id.btnBidLiq), R.drawable.pd_normal_liq, R.drawable.pd_up_liq, R.drawable.pd_down_liq);
            ColorController.updateBackground(res, contract.iAskUpDown, findViewById(R.id.rlAsk), R.drawable.pd_normal_ask, R.drawable.pd_up_ask, R.drawable.pd_down_ask);
            ColorController.updateBackground(res, contract.iAskUpDown, findViewById(R.id.btnAskLiq), R.drawable.pd_normal_liq, R.drawable.pd_up_liq, R.drawable.pd_down_liq);
        } else {
            //ColorController.setPriceColor(res, 0, (ViewGroup)findViewById(R.id.llRate));
            ColorController.updateBackground(res, 0, findViewById(R.id.rlBid), R.drawable.pd_normal_bid, R.drawable.pd_up_bid, R.drawable.pd_down_bid);
            ColorController.updateBackground(res, 0, findViewById(R.id.btnBidLiq), R.drawable.pd_normal_liq, R.drawable.pd_up_liq, R.drawable.pd_down_liq);
            ColorController.updateBackground(res, 0, findViewById(R.id.rlAsk), R.drawable.pd_normal_ask, R.drawable.pd_up_ask, R.drawable.pd_down_ask);
            ColorController.updateBackground(res, 0, findViewById(R.id.btnAskLiq), R.drawable.pd_normal_liq, R.drawable.pd_up_liq, R.drawable.pd_down_liq);
        }

        if (!CompanySettings.ENABLE_ORDER || (app.data.getBalanceRecord() != null && app.data.getBalanceRecord().hedged == true && CompanySettings.ALLOW_STP_ORDER == false)) {
            popAction.findViewById(R.id.llBuyLimit).setVisibility(android.view.View.INVISIBLE);
            popAction.findViewById(R.id.llSellLimit).setVisibility(android.view.View.INVISIBLE);
            popAction.findViewById(R.id.llBuyStop).setVisibility(android.view.View.INVISIBLE);
            popAction.findViewById(R.id.llSellStop).setVisibility(android.view.View.INVISIBLE);
        }

        if (pid != null) {
            TransactionObj t = app.data.getTransaction(pid);
            if (t != null && t.iStatusMsg == 916) {
                pid = null;
                ContractDetailActivity.this.finish();
                if (isNewPosition == true)
                    goTo(ServiceFunction.SRV_OPEN_POSITION);
                else
                    goTo(ServiceFunction.SRV_LIQUIDATION_HISTORY);
            } else if (t != null && t.iStatusMsg != 917) {
                pid = null;
            }
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        super.onServiceConnected(name, service);
        if (guest) {
            switch (connectionStatus) {
                case DISCONNECTED:
                case INITIAL:
                    Random r = new Random();
                    Message message = Message.obtain(null, ServiceFunction.SRV_GUEST_PRICE_AGENT);
                    message.arg1 = PriceAgentConnectionProcessor.ActionType.CONNECT.getValue();
                    message.arg2 = r.nextInt();
                    try {
                        mService.send(message);
                    } catch (Exception ex) {

                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        isLandscape = null;
        if (CompanySettings.ENABLE_CHART_SERSOR_ROTATION)
            orientationListener.enable();

        if (app.getSelectedContract() == null) {
            if (app.getDefaultContract() == null)
                return;
            app.setSelectedContract(app.getDefaultContract());
        }

        if (CompanySettings.newinterface)
            label_contract.setText(app.getSelectedContract().getContractName(getLanguage()));
        else
            btnContract_b.setText(app.getSelectedContract().getContractName(getLanguage()));

        if (!chartController.isDataLoaded()) {
            handler.post(updateChart);
        } else {
            showChart();
        }

        if (pendingLiqList == null)
            pendingLiqList = new ArrayList<OpenPositionRecord>();
        else
            pendingLiqList.clear();
    }

    @Override
    protected void onPause() {
        super.onPause();
        orientationListener.disable();
    }

    /**
     * Get image from URL
     * @param url Target URL
     * @return Drawable image
     * @throws MalformedURLException
     * @throws IOException
     */
	/*
	private Drawable getImage(String url) throws MalformedURLException, IOException {
		InputStream is = (InputStream) this.fetch(url);
		Drawable d = Drawable.createFromStream(is, "src");
		return d;
	}
	 */

    /**
     * Get object from URL
     *
     * @param address Target URl
     * @return object from URL
     * @throws MalformedURLException
     * @throws IOException
     */
    public Object fetch(String address) throws MalformedURLException, IOException {
        URL url = new URL(address);
        Object content = url.getContent();
        return content;
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
        return ServiceFunction.SRV_CONTRACT_DETAIL;
    }

    @Override
    public int getActivityServiceCode() {
        return ServiceFunction.SRV_CONTRACT_DETAIL;
    }

    @Override
    public void loginOrLogoutScreenUpdate() {
        if (!CompanySettings.SHOW_DASHBOARD_BEFORE_LOGIN && !CompanySettings.ENABLE_CONTENT_WEB_VIEW)
            return;
        if ((app).bLogon) {
            if (btnAction != null) {
                btnAction.setEnabled(Boolean.TRUE);
                btnAction.setVisibility(View.VISIBLE);
            }
            findViewById(R.id.btnBidLiq).setVisibility(View.VISIBLE);
            findViewById(R.id.btnAskLiq).setVisibility(View.VISIBLE);
        } else {
            if (btnAction != null) {
                btnAction.setEnabled(Boolean.FALSE);
                btnAction.setVisibility(View.INVISIBLE);
            }
            findViewById(R.id.btnBidLiq).setVisibility(View.INVISIBLE);
            findViewById(R.id.btnAskLiq).setVisibility(View.INVISIBLE);
        }
    }

    public void hideChart() {
        if (findViewById(R.id.txNoChart) != null)
            findViewById(R.id.txNoChart).setVisibility(View.VISIBLE);
        hideLoading();
    }

    public void showChart() {
        if (findViewById(R.id.txNoChart) != null)
            findViewById(R.id.txNoChart).setVisibility(View.INVISIBLE);
        hideLoading();
    }

    public void hideLoading() {
        if (dialog != null) {
            synchronized (dialog) {
                dialog.dismiss();
            }
        }
    }

    public void showLoading() {
        if (dialog == null) {
            dialog = DialogAdapter.show(ContractDetailActivity.this,
                    ContractDetailActivity.this
                            .findViewById(R.id.loadingDialog));
        } else
            synchronized (dialog) {
                try {
                    dialog.show();
                } catch (Exception e) {
                    dialog = DialogAdapter.show(ContractDetailActivity.this,
                            ContractDetailActivity.this
                                    .findViewById(R.id.loadingDialog));
                }
            }
    }

    @Override
    public void onChangeSeriesComplete() {
        int iType = popType.getIndex(btnType.getText().toString());
        chartController.changePlot(Plot.valueOf(alTypeStringMap[iType]));
        showChart();
    }

    @Override
    public void onChangeSeriesFail() {
        hideChart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        chartController.release();
        scheduledExecutorService.shutdown();
    }

    public void openChart(boolean bySensor) {
        Bundle data = new Bundle();
        data.putString(ServiceFunction.CHART_CONTRACT, app.getSelectedContract().strContractCode);

        data.putInt("type", popType.getIndex(popType.getValue()));
        data.putInt("scale", popPeriod.getIndex(popPeriod.getValue()));
        data.putBoolean("bySensor", bySensor);
        CommonFunction.moveTo(mService, mServiceMessengerHandler, ServiceFunction.SRV_CHART, data);
    }

    OrientationEventListener orientationListener;
    private static final int THRESHOLD = 5;
    private Boolean isLandscape = null;

    private boolean isLandscape(int orientation) {
        return orientation >= (270 - THRESHOLD) && orientation <= (270 + THRESHOLD);
    }

    private boolean isPortrait(int orientation) {
        return (orientation >= (360 - THRESHOLD) && orientation <= 360) || (orientation >= 0 && orientation <= THRESHOLD);
    }

    @Override
    public void onSetSecondSeriesComplete() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStartDrawLineComplete() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onGetAvailableSymbolsComplete(String[] symbols) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onClick(View v) {
        final OpenPositionRecord op;
        boolean isBuy = false;
        if (!app.bLogon) {
            return;
        }
        switch (v.getId()) {
            case R.id.btnBidLiq:
                isBuy = CompanySettings.ENABLE_CONTRACT_DETAIL_LIQUIDATE_BID_SHOW_BUY_POSITION;
                break;
            case R.id.btnAskLiq:
                isBuy = !CompanySettings.ENABLE_CONTRACT_DETAIL_LIQUIDATE_BID_SHOW_BUY_POSITION;
                break;
            default:
                return;
        }
        op = isBuy ? getLastBuyPosition() : getLastSellPosition();
        if (op == null) {
            Toast.makeText(ContractDetailActivity.this, res.getString(R.string.msg_no_deal_available), Toast.LENGTH_SHORT).show();
            return;
        }

        if (isOneClickTradeEnable()) {
            if (!app.getPriceAgentConnectionStatus()) {
                Toast.makeText(ContractDetailActivity.this, MessageMapping.getMessageByCode(res, "307", app.locale), Toast.LENGTH_LONG).show();
                return;
            }

            String s1 = getString(R.string.lb_liquidate);
            String s2 = contract.getContractName(getLanguage());
            String s5 = getString(R.string.lb_lot);
            String s6 = String.valueOf(op.dAmount / op.contract.iContractSize);
            String s3 = getString(R.string.lb_ref);
            String s4 = op.getViewRef();
            String s7 = getString(R.string.are_you_sure);
            AlertDialog dialog = new AlertDialog.Builder(ContractDetailActivity.this, CompanySettings.alertDialogTheme).create();
            dialog.setTitle(R.string.title_liquidation);
            dialog.setMessage(String.format("%s %s\n%s %s\n%s %s\n%s", s1, s2, s3, s4, s5, s6, s7));
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, res.getText(R.string.yes),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            synchronized (pendingLiqList) {
                                pendingLiqList.add(op);
                            }
                            pid = CommonFunction.getTransactionID(DataRepository.getInstance().getBalanceRecord().strAccount);
                            CommonFunction.sendLiquidationRequest(mService, mServiceMessengerHandler, op, pid, Long.toString(app.dServerDateTime.getTime()));
                            isNewPosition = false;
                            Toast.makeText(ContractDetailActivity.this, res.getString(R.string.msg_request_sent), Toast.LENGTH_SHORT).show();
                        }
                    }
            );

            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, res.getText(R.string.no),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }
            );
            dialog.show();
        } else {
            Bundle data = new Bundle();
            data.putInt(ServiceFunction.SEND_LIQUIDATION_REQUEST_REF, op.iRef);
            data.putBoolean("isBuy", isBuy);
            data.putString("contract", op.strContract);
            data.putBoolean("showAllPosition", !CompanySettings.ENABLE_CONTRACT_DETAIL_LIQUIDATE_SELECTION);
            CommonFunction.moveTo(mService, mServiceMessengerHandler, ServiceFunction.SRV_LIQUIDATE, data);
        }
    }

    private OpenPositionRecord getLastBuyPosition() {
        ArrayList<OpenPositionRecord> list = contract.getAllBuyPosition();
        if (list == null)
            return null;
        synchronized (pendingLiqList) {
            for (int i = list.size() - 1; i >= 0; i--) {
                if (pendingLiqList.indexOf(list.get(i)) == -1)
                    return list.get(i);
            }
        }
        return null;
    }

    private OpenPositionRecord getLastSellPosition() {
        ArrayList<OpenPositionRecord> list = contract.getAllSellPosition();
        if (list == null)
            return null;
        synchronized (pendingLiqList) {
            for (int i = list.size() - 1; i >= 0; i--) {
                if (pendingLiqList.indexOf(list.get(i)) == -1)
                    return list.get(i);
            }
        }
        return null;
    }
}