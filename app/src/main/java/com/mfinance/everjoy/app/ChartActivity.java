package com.mfinance.everjoy.app;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.hardware.SensorManager;
import android.os.Message;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.mfinance.chart.library.ChartController;
import com.mfinance.chart.library.ChartControllerBuilder;
import com.mfinance.chart.library.ChartControllerListener;
import com.mfinance.chart.library.Indicator;
import com.mfinance.chart.library.LineDrawing;
import com.mfinance.chart.library.LineDrawingType;
import com.mfinance.chart.library.Plot;
import com.mfinance.chart.library.TimeScale;
import com.mfinance.everjoy.BuildConfig;
import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.bo.ContractObj;
import com.mfinance.everjoy.app.util.Utility;

import java.util.ArrayList;
import java.util.Locale;

public class ChartActivity extends BaseActivity implements
        ChartControllerListener {
    boolean SHOW_INDICATOR_DIALOG = false;
    ArrayList<ContractObj> popupContractList;

    ChartController chartController;

    String[] alTypeString;
    int selectedType = 0;

    String[] alScaleString;
    int selectedScale = 0;

    String[] alContractString;

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
    private Button btnContract;
    /**
     * A button for trigger Period pop up
     */
    private Button btnPeriod;
    /**
     * A button for trigger Type pop up
     */
    private Button btnType;

    boolean isFromSensor = false;

    private Locale pLocale;
    /**
     * Task for update chart image
     */
    String[] periodMap = {"Minute", "FiveMinute", "Hourly", "Daily", "Weekly", "Monthly"};

    final static String[] indicatorString;

    static {
        indicatorString = new String[Indicator.class.getEnumConstants().length];
        for (int i = 0; i < Indicator.class.getEnumConstants().length; i++) {
            indicatorString[i] = Indicator.class.getEnumConstants()[i].name();
        }
    }

    boolean[] indicatorSelected = new boolean[indicatorString.length];

    private Runnable updateChart = new Runnable() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (app.getSelectedContract() == null) {
                        hideChart();
                        return;
                    }
                    if (Utility.getIdById("btnDraw") != -1 && findViewById(Utility.getIdById("btnDraw")) != null) {
                        ToggleButton btnDraw = (ToggleButton) findViewById(Utility.getIdById("btnDraw"));
                        btnDraw.setChecked(false);
                    }

                    //indicator workaround
					/*
					if(Utility.getIdById("indicatorBtn")!=-1&&findViewById(Utility.getIdById("indicatorBtn"))!=null){
						View tb = (findViewById(Utility.getIdById("indicatorBtn")));
						if(tb.isSelected()){
							indicatorBtn(tb);
							tb.setSelected(true);
						}
					}
					*/
                    try {
                        if (app.data
                                .isChartAva(app.getSelectedContract().strContractCode)) {
                            //Log.i("show Chart",
                            //		app.getSelectedContract().strContractCode);
                            showLoading();
                            changeSeries(
                                    app.getSelectedContract().strContractCode,
                                    selectedScale);
                        } else {
                            if (BuildConfig.DEBUG)
                                Log.i("Hide Chart",
                                        app.getSelectedContract().strContractCode);
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
        findViewById(R.id.btnClose).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnContract.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                popContract.updateSelectedContract(null, btnContract.getText()
                        .toString());
                popContract.showLikeQuickAction();
            }
        });

        popContract.btnCommit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                popContract.dismiss();
                String sContract = popContract.getValue();
                // do not look up contract by name since it may not be unique!
                app.setSelectedContract(popupContractList.get(popContract.getSelectedIndex()));
                btnContract.setText(sContract);

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
                selectedScale = popPeriod.getIndex(btnPeriod.getText()
                        .toString());
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
                if (isPortrait == null) {
                    isPortrait = !isLandscape(orientation);
                    return;
                }

                if (!isPortrait && isPortrait(orientation)) {
                    finish();
                } else {
                    isPortrait = isPortrait(orientation);
                }
            }
        };
    }

    String[] alTypeStringMap = {"Line", "Candle", "OHLC"};

    public void changeType(String sType) {
        popType.setSelected(sType);
        btnType.setText(sType);

        int iType = popType.getIndex(sType);
        selectedType = iType;
        chartController.changePlot(Plot.valueOf(alTypeStringMap[iType]));
    }

    @Override
    public void handleByChild(Message msg) {

    }

    @Override
    public boolean isBottonBarExist() {
        return false;
    }

    @Override
    public boolean isTopBarExist() {
        return false;
    }

    @Override
    public void loadLayout() {

        pLocale = getLanguage();

        if (release >= 5.0) {
            if (pLocale.toLanguageTag().equals("zh-TW"))
                app.changeLang(ChartActivity.this, "zh", "tw");
            else if (pLocale.toLanguageTag().equals("zh-CN"))
                app.changeLang(ChartActivity.this, "zh", "cn");
            else
                app.changeLang(ChartActivity.this, "en", null);
        }

        setContentView(R.layout.v_chart);

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
        isFromSensor = this.getIntent().getBooleanExtra("bySensor", false);

        if (Utility.getIdById("screenLockBtn") != -1 && findViewById(Utility.getIdById("screenLockBtn")) != null && !isFromSensor) {
            ToggleButton screenLockBtn = (ToggleButton) findViewById(Utility.getIdById("screenLockBtn"));
            screenLockBtn.setChecked(true);
        }

        int scale = this.getIntent().getIntExtra("scale",
                CompanySettings.CHART_DEFAULT_SELECTION[0]);
        int type = this.getIntent().getIntExtra("type",
                CompanySettings.CHART_DEFAULT_SELECTION[1]);

        View chartV = findViewById(R.id.chartView);

        btnContract = (Button) findViewById(R.id.btnContract);
        if (app.bLogon) {
            popupContractList = app.data.getTradableContractList();
        } else {
            popupContractList = app.data.getContractList();
        }
        popContract = new PopupContract(getApplicationContext(), chartV,
                popupContractList);

        btnContract.setText(app.getSelectedContract().getContractName(
                pLocale));

        CharSequence[] alPeriod = res.getTextArray(R.array.sp_chart_period);
        popPeriod = new PopupString(getApplicationContext(), chartV,
                Utility.toArrayList(alPeriod));
        popPeriod.setSelected(alPeriod[scale].toString());
        btnPeriod = (Button) findViewById(R.id.btnPeriod);
        btnPeriod.setText(alPeriod[scale]);

        CharSequence[] alType = res.getTextArray(R.array.sp_chart_type);
        popType = new PopupString(getApplicationContext(), chartV,
                Utility.toArrayList(alType));
        popType.setSelected(alType[type].toString());
        btnType = (Button) findViewById(R.id.btnType);
        btnType.setText(alType[type]);

        int arrayId = Utility.getArrayIdById("sp_chart_period_map");
        if (arrayId != -1)
            periodMap = getResources().getStringArray(arrayId);

        //new type button
        {
            alTypeString = new String[alType.length];
            int i = 0;
            for (CharSequence ch : alType) {
                alTypeString[i++] = ch.toString();
            }
            selectedType = type;
            if (Utility.getIdById("btnType2") != -1 && findViewById(Utility.getIdById("btnType2")) != null)
                ((Button) findViewById(Utility.getIdById("btnType2")))
                        .setText(alTypeString[type]);
        }

        //new period button
        {
            alScaleString = new String[alPeriod.length];
            int i = 0;
            for (CharSequence ch : alPeriod) {
                alScaleString[i++] = ch.toString();
            }
            selectedScale = scale;
            if (Utility.getIdById("btnPeriod2") != -1 && findViewById(Utility.getIdById("btnPeriod2")) != null)
                ((Button) findViewById(Utility.getIdById("btnPeriod2")))
                        .setText(alScaleString[selectedScale]);
        }

        //new contract button
        {
            alContractString = new String[popupContractList.size()];
            int i = 0;
            for (ContractObj co : popupContractList) {
                alContractString[i++] = co.getContractName(pLocale);
            }
            if (Utility.getIdById("btnContract2") != -1 && findViewById(Utility.getIdById("btnContract2")) != null)
                ((Button) findViewById(Utility.getIdById("btnContract2")))
                        .setText(app.getSelectedContract().getContractName(
                                pLocale));
        }
    }

    public void changeSeries(String symbol, int iScale) {
        String currencies = Utility.getChartCode(symbol);

        if (currencies != null) {
            symbol = currencies;
        }

        chartController.changeSeries(symbol, TimeScale.valueOf(periodMap[iScale]));
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (CompanySettings.ENABLE_CHART_SERSOR_ROTATION && Utility.getIdById("screenLockBtn") != -1 && findViewById(Utility.getIdById("screenLockBtn")) != null) {
            ToggleButton screenLockBtn = (ToggleButton) findViewById(Utility.getIdById("screenLockBtn"));
            screenLockBtn(screenLockBtn);
        }

        if (!chartController.isDataLoaded()) {
            handler.post(updateChart);
        } else {
            showChart();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        orientationListener.disable();
    }

    @Override
    public boolean showLogout() {
        return false;
    }

    @Override
    public boolean showTopNav() {
        return false;
    }

    @Override
    public void updateUI() {
        ContractObj contract = app.getSelectedContract();
        if (contract == null) {
            return;
        }
    }

    @Override
    public boolean showConnected() {
        return false;
    }

    @Override
    public boolean showPlatformType() {
        return false;
    }

    ProgressDialog dialog;

    public void hideLoading() {
        if (dialog != null) {
            synchronized (dialog) {
                dialog.dismiss();
            }
        }
    }

    public void showLoading() {
        if (dialog == null)
            dialog = ProgressDialog.show(ChartActivity.this, "",
                    res.getString(R.string.please_wait), true);
        else
            synchronized (dialog) {
                try {
                    dialog.show();
                } catch (Exception e) {
                    dialog = ProgressDialog.show(ChartActivity.this, "",
                            res.getString(R.string.please_wait), true);
                }
            }
    }

    public void hideChart() {
        try {
            findViewById(R.id.txNoChart).setVisibility(View.VISIBLE);
            hideLoading();
        } catch (Exception e) {
        }
    }

    public void showChart() {
        try {
            findViewById(R.id.txNoChart).setVisibility(View.INVISIBLE);
            hideLoading();
        } catch (Exception e) {
        }
    }

    @Override
    public void onChangeSeriesComplete() {
        chartController.changePlot(Plot.valueOf(alTypeStringMap[selectedType]));
		
		/*
		if(Utility.getIdById("indicatorBtn")!=-1&&findViewById(Utility.getIdById("indicatorBtn"))!=null){
			View tb = (findViewById(Utility.getIdById("indicatorBtn")));
			if(tb!=null&&tb.isSelected()){
				tb.setSelected(false);
				indicatorBtn(tb);
			}
		}
		*/
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
    }

    OrientationEventListener orientationListener;
    private static final int THRESHOLD = 5;
    private Boolean isPortrait = null;

    private boolean isLandscape(int orientation) {
        return orientation >= (270 - THRESHOLD) && orientation <= (270 + THRESHOLD);
    }

    private boolean isPortrait(int orientation) {
        return (orientation >= (360 - THRESHOLD) && orientation <= 360) || (orientation >= 0 && orientation <= THRESHOLD);
    }

    public void autoScaleBtn(View v) {
        ToggleButton autoScaleBtn = (ToggleButton) v;
        if (autoScaleBtn.isChecked())
            chartController.setAutoScale(true);
        else
            chartController.setAutoScale(false);
    }

    public void screenLockBtn(View v) {
        ToggleButton screenLockBtn = (ToggleButton) v;
        if (screenLockBtn.isChecked())
            orientationListener.disable();
        else
            orientationListener.enable();
    }

    public void btnDraw(View v) {
        ToggleButton btnDraw = (ToggleButton) v;
        if (btnDraw.isChecked())
            chartController.startDrawLine(LineDrawing.createLineDrawing(LineDrawingType.Line));
        else
            chartController.stopDrawLine();
    }

    public void btnDelete(View v) {
        chartController.removeSelectedDrawing();
    }

    public void btnReset(View v) {
        //ToggleButton autoScaleBtn = (ToggleButton)findViewById(Utility.getIdById("autoScaleBtn"));
        //ToggleButton screenLockBtn = (ToggleButton)findViewById(Utility.getIdById("screenLockBtn"));
        //ToggleButton crosshairBtn = (ToggleButton)findViewById(Utility.getIdById("crosshairBtn"));
        View indicatorBtn = findViewById(Utility.getIdById("indicatorBtn"));
        ToggleButton btnDraw = (ToggleButton) findViewById(Utility.getIdById("btnDraw"));
        //autoScaleBtn.setChecked(false);
        //autoScaleBtn(autoScaleBtn);
        //screenLockBtn.setChecked(false);
        //screenLockBtn(screenLockBtn);
        //crosshairBtn.setChecked(false);
        //crosshairBtn(crosshairBtn);
        indicatorBtn.setSelected(false);
        btnDraw.setChecked(false);
        chartController.removeAllDrawing();
        chartController.removeAllIndicator();

        for (int i = 0; i < indicatorSelected.length; i++)
            indicatorSelected[i] = false;
    }

    public void crosshairBtn(View v) {
        ToggleButton crosshairBtn = (ToggleButton) v;
        if (crosshairBtn.isChecked())
            chartController.setCrosshairOn(true);
        else
            chartController.setCrosshairOn(false);
    }

    public void indicatorBtn(View v) {
        if (SHOW_INDICATOR_DIALOG)
            showIndicatorDialog();
        else {
            View indicatorBtn = v;
            if (!indicatorBtn.isSelected()) {
                indicatorBtn.setSelected(true);
                chartController.addIndicator(Indicator.Bollinger);
                chartController.addIndicator(Indicator.RSI);
                chartController.addIndicator(Indicator.MACD);
                chartController.addIndicator(Indicator.SMA10);
                chartController.addIndicator(Indicator.SMA20);
            } else {
                chartController.removeAllIndicator();
                indicatorBtn.setSelected(false);
            }
        }

    }

    public void onMenuTypeClick(View v) {
        doMenuClick(v);
        addMenu("type", alTypeString, selectedType);
    }

    public void onMenuPeriodClick(View v) {
        doMenuClick(v);
        addMenu("period", alScaleString, selectedScale);
    }

    public void onMenuContractClick(View v) {
        doMenuClick(v);
        int i = 0;
        for (String name : alContractString) {
            if (app.getSelectedContract().getContractName(pLocale).equals(name))
                break;
            i++;
        }

        addMenu("contract", alContractString, i);
    }

    public void onMenuButtonClick(View v) {
        ButtonTag tag = (ButtonTag) v.getTag();
        if (tag.tag.equals("type")) {
            selectedType = tag.index;
            chartController.changePlot(Plot.valueOf(alTypeStringMap[tag.index]));
            ((Button) findViewById(Utility.getIdById("btnType2"))).setText(alTypeString[tag.index]);
        } else if (tag.tag.equals("period")) {
            selectedScale = tag.index;
            handler.post(updateChart);
            ((Button) findViewById(Utility.getIdById("btnPeriod2"))).setText(alScaleString[tag.index]);
        } else if (tag.tag.equals("contract")) {
            app.setSelectedContract(popupContractList.get(tag.index));
            handler.post(updateChart);
            ((Button) findViewById(Utility.getIdById("btnContract2"))).setText(app.getSelectedContract().getContractName(pLocale));
        }
        findViewById(Utility.getIdById("menuRootView")).setVisibility(View.GONE);
        deselectAllBtn();
    }

    private class ButtonTag {
        String tag;
        int index;

        private ButtonTag(String tag, int index) {
            this.tag = tag;
            this.index = index;
        }
    }

    private void addMenu(String tag, String[] items, int selectedIndex) {
        if (items == null || items.length == 0 || selectedIndex < 0 || selectedIndex > items.length - 1 || Utility.getIdById("menuView") == -1)
            return;
        LinearLayout menuView = (LinearLayout) findViewById(Utility.getIdById("menuView"));
        menuView.removeAllViews();
        int i = 0;
        for (String item : items) {
            Button btn = (Button) this.getLayoutInflater().inflate(Utility.getLayoutIdById("chart_btn"), menuView, false);
            btn.setText(item);
            if (selectedIndex == i)
                btn.setSelected(true);
            btn.setTag(new ButtonTag(tag, i));

            menuView.addView(btn);
            i++;
        }
    }

    private void deselectAllBtn() {
        findViewById(Utility.getIdById("btnPeriod2")).setSelected(false);
        findViewById(Utility.getIdById("btnType2")).setSelected(false);
        findViewById(Utility.getIdById("btnContract2")).setSelected(false);
    }

    private void doMenuClick(View v) {
        if (Utility.getIdById("menuRootView") == -1)
            return;
        if (v.isSelected()) {
            v.setSelected(false);
            findViewById(Utility.getIdById("menuRootView")).setVisibility(View.GONE);
            return;
        } else {
            deselectAllBtn();
            v.setSelected(true);
            findViewById(Utility.getIdById("menuRootView")).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onStartDrawLineComplete() {
        ToggleButton btnDraw = (ToggleButton) findViewById(Utility.getIdById("btnDraw"));
        btnDraw.setChecked(false);
    }

    @Override
    public void onSetSecondSeriesComplete() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onGetAvailableSymbolsComplete(String[] symbols) {
        // TODO Auto-generated method stub

    }

    //
    // Indicator helper function(s)
    //

    private void showIndicatorDialog() {
        AlertDialog.Builder d = new AlertDialog.Builder(this, CompanySettings.alertDialogTheme);
        d.setMultiChoiceItems(indicatorString, indicatorSelected, new OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {

            }
        });
        d.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                onClickIndicatorDialogPositiveButton();
            }
        });
        d.show();
    }

    private void onClickIndicatorDialogPositiveButton() {
        boolean allFalse = true;
        for (int i = 0; i < indicatorSelected.length; i++) {
            if (indicatorSelected[i]) {
                chartController.addIndicator(Indicator.valueOf(indicatorString[i]));
                if (allFalse)
                    allFalse = false;
            } else {
                chartController.removeIndicator(Indicator.valueOf(indicatorString[i]));
            }
        }

        if (allFalse)
            (findViewById(Utility.getIdById("indicatorBtn"))).setSelected(false);
        else
            (findViewById(Utility.getIdById("indicatorBtn"))).setSelected(true);
    }
}
