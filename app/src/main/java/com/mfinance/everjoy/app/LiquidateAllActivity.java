package com.mfinance.everjoy.app;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.bo.ContractObj;
import com.mfinance.everjoy.app.bo.OpenPositionRecord;
import com.mfinance.everjoy.app.bo.SystemMessage;
import com.mfinance.everjoy.app.bo.TransactionObj;
import com.mfinance.everjoy.app.bo.TransactionObjBuilder;
import com.mfinance.everjoy.app.constant.Protocol.LiquidateAllResponse;
import com.mfinance.everjoy.app.constant.Protocol.LiquidateAllRequest;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.model.DataRepository;
import com.mfinance.everjoy.app.util.ListViewAdapterReloader;
import com.mfinance.everjoy.app.util.ListViewOnItemListener;
import com.mfinance.everjoy.app.util.MessageMapping;
import com.mfinance.everjoy.app.util.Utility;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LiquidateAllActivity extends BaseActivity {
    ArrayList<String> ppidArray = new ArrayList<String>();
    enum LiquidationMethod {
        FIRST_IN_FIRST_OUT, LAST_IN_FIRST_OUT
    }

    enum BuySellAll {
        BUY("BUY", "1"), SELL("SELL", "2"), ALL("ALL", "0");
        private String text;
        private String type;

        BuySellAll(String text, String type) {
            this.text = text;
            this.type = type;
        }

        public String getText() {
            return text;
        }

        public String getType() {
            return type;
        }

        public static BuySellAll fromText(String text) {
            for (BuySellAll t : BuySellAll.values()) {
                if (t.getText() == text) {
                    return t;
                }
            }
            return BuySellAll.ALL;
        }
    }

    private Handler mainHandler = new Handler(Looper.getMainLooper());
    private TextView tvTitle;
    private PopupString popupContractAll;
    private Button btnSlippage;
    private PopupSlippage popupSlippage;
    private Button btnBuySellAll;
    private PopupString popupBuySellAll;
    private ToggleButton toggleButton;
    private IPhoneToggle toggleButtonWrapper;
    private View vLiquidateLot;
    private View vLiquidateLot2;
    private TextView tvLiquidateRemainingLot;
    private Button btnLiquidateAmount;
    private View llSelectAll;
    private TextView tvSelection;
    private ListView lvPosition;
    private ListViewAdapterReloader reloader;
    private Button btnCancel;
    private Button btnSubmit;
    private OpenPositionListAdapter positionListAdapter;

    private LiquidateAllActivityState state = new LiquidateAllActivityStateBuilder()
            .setSlippage(0)
            .setLiquidationMethod(LiquidationMethod.LAST_IN_FIRST_OUT)
            .setBuySellAll(BuySellAll.ALL)
            .setSelectedContract("")
            .setSelectedAllContract(true)
            .setSelectMultipleShortcutAll(true)
            .build();
    private static final Comparator<OpenPositionRecord> lifoComparator = (o1, o2) -> o2.iRef - o1.iRef;
    private static final Comparator<OpenPositionRecord> fifoComparator = (o1, o2) -> o1.iRef - o2.iRef;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        LiquidateAllActivityStateBuilder builder = new LiquidateAllActivityStateBuilder(state);
        Optional.ofNullable(intent.getStringExtra(ServiceFunction.SRV_OPEN_POSITION_CONTRACT))
                .ifPresent(selectedContract -> builder.setSelectedContract(selectedContract).setSelectedAllContract(false));
        Optional.ofNullable(intent.getStringExtra(ServiceFunction.SRV_OPEN_POSITION_BUY_SELL))
                .map(s -> s.equals("S") ? BuySellAll.SELL : BuySellAll.BUY)
                .ifPresent(builder::setBuySellAll);
        builder.setAllowSelectMultiple(intent.getBooleanExtra("allowMultipleSelection", false));
        state = builder.build();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void bindEvent() {

    }

    @Override
    public void handleByChild(Message msg) {
        if (msg.what != ServiceFunction.ACT_TRADER_RECEIVE_MULTIPLE_LIQUIDATE_RETURN) {
            return;
        }
        Bundle data = msg.getData();
        try {
            Map<String, String> response = (Map<String, String>) data.getSerializable("response");
            try {
                if (Integer.parseInt(response.getOrDefault("status", "9")) < 0) {
                    app.data.addSystemMessage(new SystemMessage(response.getOrDefault("msgcd1", "719")));
                }
            } catch (NumberFormatException e) {

            }
//            if (response.getOrDefault(LiquidateAllResponse.PID, "").equals(this.state.pid)) {
//                this.finish();
//            }
        } catch (Exception ex) {

        }


    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        super.onServiceConnected(name, service);
        if (state.allowSelectMultiple) {
            LiquidateAllActivityStateBuilder builder = new LiquidateAllActivityStateBuilder(state);
            ContractObj contract = app.data.getContract(state.selectedContract);
            builder.setSelectedPositionIds(sortFilterOpenPositions(app.data.getOpenPositions().values(), state).map(p -> p.iRef).collect(Collectors.toSet()));
            builder.setSelectMultipleAmount(getLiquidateSelectedMaxLot(builder.build(), contract).multiply(BigDecimal.valueOf(contract.iContractSize)));
            internalStateUpdate(builder.build());
        }
    }

    private Stream<ContractObj> getContract(DataRepository data) {
        return Utility.getViewableContract(data)
                .filter(contract -> {
                    for (OpenPositionRecord p : data.getOpenPositions().values()) {
                        if (Objects.equals(p.strContract, contract.strContractCode)) {
                            return true;
                        }
                    }
                    return false;
                });
    }

    @Override
    public void loadLayout() {
        setContentView(R.layout.v_liquidation_all);
        ArrayList<String> selectList = new ArrayList<>(state.allowSelectMultiple ? Collections.emptyList() : Collections.singletonList("ALL"));
        ArrayList<String> selectListTranslated = new ArrayList<>(state.allowSelectMultiple? Collections.emptyList() : Collections.singletonList(getResources().getString(R.string.lb_all)));
        selectList.addAll(
                getContract(app.data)
                        .map(c -> c.strContractCode)
                        .collect(Collectors.toList())
        );
        Locale locale = getLanguage();
        selectListTranslated.addAll(
                getContract(app.data)
                        .map(contract -> contract.getContractName(locale))
                        .collect(Collectors.toList())
        );
        popupContractAll = new PopupString(getApplicationContext(), findViewById(R.id.rlTop), selectListTranslated);
        popupContractAll.btnCommit.setOnClickListener(v -> {
            LiquidateAllActivityStateBuilder builder = new LiquidateAllActivityStateBuilder(state);
            if (state.allowSelectMultiple) {
                builder.setSelectedAllContract(false);
                builder.setSelectedContract(selectList.get(popupContractAll.wvString.getCurrentItem()));
                LiquidateAllActivityState state = builder.build();
                ContractObj contract = app.data.getContract(state.selectedContract);
                builder.setSelectedPositionIds(sortFilterOpenPositions(app.data.getOpenPositions().values(), state).map(p -> p.iRef).collect(Collectors.toSet()));
                builder.setSelectMultipleAmount(getLiquidateSelectedMaxLot(builder.build(), contract).multiply(BigDecimal.valueOf(contract.iContractSize)));
            } else {
                builder.setSelectedAllContract(popupContractAll.wvString.getCurrentItem() == 0);
                builder.setSelectedContract(selectList.get(popupContractAll.wvString.getCurrentItem()));
            }
            popupContractAll.dismiss();
            internalStateUpdate(builder.build());
        });
        popupContractAll.btnClose.setOnClickListener(v -> popupContractAll.dismiss());
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setOnClickListener(v -> {
            popupContractAll.setSelected(state.selectedAllContract ? "ALL" : state.selectedContract);
            popupContractAll.showLikeQuickAction();
        });
        popupSlippage = new PopupSlippage(getApplicationContext(), findViewById(R.id.rlTop));
        popupSlippage.btnCommit.setOnClickListener(v -> {
            LiquidateAllActivityStateBuilder builder = new LiquidateAllActivityStateBuilder(state);
            builder.setSlippage(Integer.parseInt(popupSlippage.getValue()));
            popupSlippage.dismiss();
            internalStateUpdate(builder.build());
        });
        popupSlippage.btnClose.setOnClickListener(v -> popupSlippage.dismiss());
        btnSlippage = findViewById(R.id.btnSlippage);
        btnSlippage.setOnClickListener(v -> {
            popupSlippage.setValue(Integer.toString(state.slippage));
            popupSlippage.showLikeQuickAction();
        });

        if (state.allowSelectMultiple) {
            popupBuySellAll = new PopupString(
                    getApplicationContext(),
                    findViewById(R.id.rlTop),
                    Arrays.asList(res.getString(R.string.lb_buy), res.getString(R.string.lb_sell))
            );
            popupBuySellAll.btnCommit.setOnClickListener(v -> {
                LiquidateAllActivityStateBuilder builder = new LiquidateAllActivityStateBuilder(state);
                switch (popupBuySellAll.wvString.getCurrentItem()) {
                    case 0:
                        builder.setBuySellAll(BuySellAll.BUY);
                        break;
                    default:
                        builder.setBuySellAll(BuySellAll.SELL);
                        break;
                }
                LiquidateAllActivityState state = builder.build();
                ContractObj contract = app.data.getContract(state.selectedContract);
                builder.setSelectedPositionIds(sortFilterOpenPositions(app.data.getOpenPositions().values(), state).map(p -> p.iRef).collect(Collectors.toSet()));
                builder.setSelectMultipleAmount(getLiquidateSelectedMaxLot(builder.build(), contract).multiply(BigDecimal.valueOf(contract.iContractSize)));
                popupBuySellAll.dismiss();
                internalStateUpdate(builder.build());
            });
        } else {
            popupBuySellAll = new PopupString(
                    getApplicationContext(),
                    findViewById(R.id.rlTop),
                    Arrays.asList(res.getString(R.string.lb_all), res.getString(R.string.lb_buy), res.getString(R.string.lb_sell))
            );
            popupBuySellAll.btnCommit.setOnClickListener(v -> {
                LiquidateAllActivityStateBuilder builder = new LiquidateAllActivityStateBuilder(state);
                switch (popupBuySellAll.wvString.getCurrentItem()) {
                    case 0:
                        builder.setBuySellAll(BuySellAll.ALL);
                        break;
                    case 1:
                        builder.setBuySellAll(BuySellAll.BUY);
                        break;
                    default:
                        builder.setBuySellAll(BuySellAll.SELL);
                        break;
                }
                popupBuySellAll.dismiss();
                internalStateUpdate(builder.build());
            });
        }
        popupBuySellAll.btnClose.setOnClickListener(v -> popupBuySellAll.dismiss());
        btnBuySellAll = findViewById(R.id.btnBuySellAll);
        btnBuySellAll.setOnClickListener(v -> {
            popupBuySellAll.setSelected(state.buySellAll.getText());
            popupBuySellAll.showLikeQuickAction();
        });
        toggleButton = findViewById(R.id.tbLiquidateMethod);
        toggleButtonWrapper = new IPhoneToggle(toggleButton, res);
        toggleButton.setOnClickListener(v -> {
            LiquidateAllActivityStateBuilder builder = new LiquidateAllActivityStateBuilder(state);
            builder.setLiquidationMethod(toggleButton.isChecked() ? LiquidationMethod.LAST_IN_FIRST_OUT : LiquidationMethod.FIRST_IN_FIRST_OUT);
            internalStateUpdate(builder.build());
        });
        vLiquidateLot = findViewById(R.id.vLiquidateLot);
        vLiquidateLot2 = findViewById(R.id.vLiquidateLot2);
        tvLiquidateRemainingLot = findViewById(R.id.tvLiquidateRemainingLot);
        btnLiquidateAmount = findViewById(R.id.btnLiquidateLot);
        btnLiquidateAmount.setOnClickListener((v) -> {
            ContractObj contract = app.data.getContract(state.selectedContract);
            PopupLOT popupLOT = new PopupLOT(this, findViewById(R.id.rlTop), getLiquidateSelectedMaxLot(state, contract));
            popupLOT.setValue(state.selectMultipleAmount.divide(BigDecimal.valueOf(app.data.getContract(this.state.selectedContract).iContractSize), RoundingMode.HALF_DOWN));
            popupLOT.showLikeQuickAction();
            popupLOT.btnCommit.setOnClickListener(v1 -> {
                LiquidateAllActivityStateBuilder builder = new LiquidateAllActivityStateBuilder(state);
                builder.setSelectMultipleAmount(popupLOT.getDecimalValue().multiply(BigDecimal.valueOf(contract.iContractSize)));
                internalStateUpdate(builder.build());
                popupLOT.dismiss();
            });
            popupLOT.btnClose.setOnClickListener(v1 -> popupLOT.dismiss());
        });
        llSelectAll = findViewById(R.id.llSelectAll);
        llSelectAll.setOnClickListener(v -> {
            boolean selectAll = !state.selectMultipleShortcutAll;
            LiquidateAllActivityStateBuilder builder = new LiquidateAllActivityStateBuilder(state);
            builder.setSelectMultipleShortcutAll(selectAll);
            if (selectAll) {
                builder.setSelectedPositionIds(sortFilterOpenPositions(app.data.getOpenPositions().values(), state).map(p -> p.iRef).collect(Collectors.toSet()));
            } else {
                builder.setSelectedPositionIds(Collections.emptySet());
            }
            ContractObj contract = app.data.getContract(state.selectedContract);
            builder.setSelectMultipleAmount(getLiquidateSelectedMaxLot(builder.build(), contract).multiply(BigDecimal.valueOf(contract.iContractSize)));
            internalStateUpdate(builder.build());
        });
        tvSelection = findViewById(R.id.tvSelection);
        lvPosition = findViewById(R.id.lvPosition);
        lvPosition.setSelected(true);
        ListViewOnItemListener listener = new ListViewOnItemListener();
        positionListAdapter = new OpenPositionListAdapter(
                this,
                getListItem(sortFilterOpenPositions(app.data.getOpenPositions().values(), state), state),
                v -> {
                    if (state.allowSelectMultiple) {
                        LiquidateAllActivityStateBuilder builder = new LiquidateAllActivityStateBuilder(state);
                        HashSet<Integer> newSet = new HashSet<>(state.selectedPositionIds);
                        int id = v.getPosition().iRef;
                        if (newSet.contains(id)) {
                            newSet.remove(id);
                        } else {
                            newSet.add(id);
                        }
                        builder.setSelectedPositionIds(newSet);
                        ContractObj contract = app.data.getContract(state.selectedContract);
                        BigDecimal maxAmount = getLiquidateSelectedMaxLot(builder.build(), contract).multiply(BigDecimal.valueOf(contract.iContractSize));
                        builder.setSelectMultipleAmount(maxAmount);
                        internalStateUpdate(builder.build());
                    }
                },
                listener
        );
        lvPosition.setAdapter(positionListAdapter);
        lvPosition.setItemsCanFocus(true);
        reloader = new ListViewAdapterReloader(lvPosition, positionListAdapter);
        reloader.reload();
        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(v -> LiquidateAllActivity.this.finish());
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(v -> {
            AlertDialog dialog = new AlertDialog.Builder(this, CompanySettings.alertDialogTheme).create();
            dialog.setMessage(res.getText(R.string.confirm_liquidation));
            dialog.setButton(
                    DialogInterface.BUTTON_POSITIVE,
                    res.getText(R.string.btn_confirm),
                    (DialogInterface d, int which) -> {
                        String pid = UUID.randomUUID().toString();
                        ArrayList<TransactionObj> transactions = new ArrayList<>();
                        HashMap<String, String> m = new HashMap<>();
                        LiquidateAllActivityState state = LiquidateAllActivity.this.state;
                        HashMap<String, ContractObj> contracts = LiquidateAllActivity.this.app.data.getContracts();
                        m.put(LiquidateAllRequest.PID, pid);
                        m.put(LiquidateAllRequest.TYPE, state.buySellAll.getType());
                        m.put(LiquidateAllRequest.MARKET_RANGE, Integer.toString(state.slippage));
                        m.put(LiquidateAllRequest.NUMBER_OF_CONTRACT, Integer.toString(contracts.size()));
                        int i = 1;
                        for (Map.Entry<String, ContractObj> e : contracts.entrySet()) {
                            m.put(LiquidateAllRequest.ITEM_CONTRACT + i, e.getKey());
                            m.put(LiquidateAllRequest.ITEM_BID + i, Double.toString(e.getValue().dBid));
                            m.put(LiquidateAllRequest.ITEM_ASK + i, Double.toString(e.getValue().dAsk));
                            m.put(LiquidateAllRequest.ITEM_TAG + i, e.getValue().tag);
                            i++;
                        }
                        List<OpenPositionListItem> listItem = getListItem(sortFilterOpenPositions(app.data.getOpenPositions().values(), state), state);
                        i = 0;
                        for (OpenPositionListItem item : listItem) {
                            if (item.getAmountSelected() > 0.0) {
                                OpenPositionRecord openPositionRecord = item.getPosition();
                                String strContractCode = openPositionRecord.contract.strContractCode;
                                String ppid = UUID.randomUUID().toString();
                                i++;
                                m.put(LiquidateAllRequest.ITEM_POSITION_REF + i, Integer.toString(openPositionRecord.iRef));
                                m.put(LiquidateAllRequest.ITEM_POSITION_AMOUNT + i, Double.toString(item.getAmountSelected()));
                                m.put(LiquidateAllRequest.ITEM_POSITION_PPID + i, ppid);
                                double dDealRate = openPositionRecord.isBuyOrder ? openPositionRecord.contract.dBidAsk[0] : openPositionRecord.contract.dBidAsk[1];
                                TransactionObj transaction = new TransactionObjBuilder().setsTransactionID(ppid).setsRef("").setsContract(strContractCode).setsAccount(app.data.getBalanceRecord().strAccount).setsBuySell(openPositionRecord.strBuySell.equals("B") ? "S" : "B").setdRequestRate(dDealRate).setiStatus(0).setsStatusMsg(MessageMapping.getMessageByCode(res, "917", app.locale)).setiMsg(923).setiRemarkCode(923).setiType(0).setdAmount((int) openPositionRecord.dAmount).setContract(openPositionRecord.contract).createTransactionObj();
                                transaction.sMsgCode = "917";
                                transaction.iStatusMsg = 917;
                                transaction.sLiqRef = Integer.toString(openPositionRecord.iRef);
                                transactions.add(transaction);
                                ppidArray.add(ppid);
                            }
                        }
                        m.put(LiquidateAllRequest.NUMBER_OF_POSITION, Integer.toString(i));
                        Message message = Message.obtain(null, ServiceFunction.SRV_LIQUIDATE_ALL);
                        message.arg1 = 1;
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("request", m);
                        bundle.putParcelableArrayList("trans", transactions);
                        message.setData(bundle);
                        try {
                            internalStateUpdate(new LiquidateAllActivityStateBuilder(state).setPid(pid).build());
                            mService.send(message);
                        } catch (RemoteException ex) {
                            Log.e(LiquidateAllActivity.class.getSimpleName(), ex.getMessage());
                        }
                    });
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, res.getText(R.string.btn_cancel), (DialogInterface d, int which) -> {});
            dialog.show();
        });
        internalStateUpdate(this.state);
    }

    private Stream<OpenPositionRecord> sortFilterOpenPositions(Collection<OpenPositionRecord> positions, LiquidateAllActivityState state) {
        return positions.stream()
                .filter((openPositionRecord -> state.selectedAllContract || Objects.equals(openPositionRecord.contract.strContractCode, state.selectedContract)))
                .filter((openPositionRecord -> state.buySellAll == BuySellAll.ALL || state.buySellAll == BuySellAll.BUY && openPositionRecord.isBuyOrder || state.buySellAll == BuySellAll.SELL && !openPositionRecord.isBuyOrder))
                .sorted(state.liquidationMethod == LiquidationMethod.LAST_IN_FIRST_OUT ? lifoComparator : fifoComparator);
    }

    private BigDecimal getLiquidateSelectedMaxLot(LiquidateAllActivityState state, ContractObj contract) {
        BigDecimal allPositionSelectedLotSize = sortFilterOpenPositions(app.data.getOpenPositions().values(), state)
                .filter(r -> state.selectedPositionIds.contains(r.iRef))
                .map(r -> BigDecimal.valueOf(r.dAmount).divide(BigDecimal.valueOf(r.contract.iContractSize), RoundingMode.HALF_DOWN))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal maxAmount = BigDecimal.valueOf(contract.getFinalMaxLotLimit());
        if (state.buySellAll == BuySellAll.ALL) {
            return allPositionSelectedLotSize.min(maxAmount.multiply(BigDecimal.valueOf(2)));
        } else {
            return allPositionSelectedLotSize.min(maxAmount);
        }
    }

    private List<OpenPositionListItem> getListItem(Stream<OpenPositionRecord> sorted, LiquidateAllActivityState state) {
        BigDecimal accumulatedAmountAll = BigDecimal.ZERO;
        List<OpenPositionListItem> returnInstance = new ArrayList<>();
        Map<String, Double> buyContractLimit = new HashMap<>();
        Map<String, Double> sellContractLimit = new HashMap<>();
        Iterator<OpenPositionRecord> iterator = sorted.iterator();
        while (iterator.hasNext()) {
            OpenPositionRecord openPositionRecord = iterator.next();
            if (state.allowSelectMultiple && !state.selectedPositionIds.contains(openPositionRecord.iRef)) {
                returnInstance.add(new OpenPositionListItem(openPositionRecord, true, 0.0, false));
                continue;
            }
            Map<String, Double> contractLimit = openPositionRecord.isBuyOrder ? buyContractLimit : sellContractLimit;
            String strContractCode = openPositionRecord.contract.strContractCode;
            Double prevAmount = contractLimit.getOrDefault(strContractCode, 0.0);
            double currentAmount = prevAmount + openPositionRecord.dAmount;
            contractLimit.put(strContractCode, currentAmount);
            ContractObj contract = app.data.getContract(strContractCode);
            BigDecimal maxAmount = BigDecimal.valueOf(contract.getFinalMaxLotLimit()).multiply(new BigDecimal(contract.iContractSize));
            BigDecimal currentAmountDecimal = new BigDecimal(currentAmount);
            BigDecimal remainingLimit = maxAmount.subtract(BigDecimal.valueOf(prevAmount));
            if (currentAmountDecimal.compareTo(maxAmount) <= 0) {
                if (state.allowSelectMultiple) {
                    if (accumulatedAmountAll.add(BigDecimal.valueOf(openPositionRecord.dAmount)).compareTo(state.selectMultipleAmount) > 0) {
                        returnInstance.add(new OpenPositionListItem(openPositionRecord, true, state.selectMultipleAmount.subtract(accumulatedAmountAll).max(BigDecimal.ZERO).doubleValue(), false));
                    } else {
                        returnInstance.add(new OpenPositionListItem(openPositionRecord, true, openPositionRecord.dAmount, false));
                    }
                    accumulatedAmountAll = accumulatedAmountAll.add(BigDecimal.valueOf(openPositionRecord.dAmount));
                } else {
                    returnInstance.add(new OpenPositionListItem(openPositionRecord, true, openPositionRecord.dAmount, false));
                }
            } else if (remainingLimit.compareTo(BigDecimal.ZERO) > 0) {
                if (state.allowSelectMultiple) {
                    if (accumulatedAmountAll.add(remainingLimit).compareTo(state.selectMultipleAmount) > 0) {
                        returnInstance.add(new OpenPositionListItem(openPositionRecord, true, state.selectMultipleAmount.subtract(accumulatedAmountAll).max(BigDecimal.ZERO).doubleValue(), false));
                    } else {
                        returnInstance.add(new OpenPositionListItem(openPositionRecord, true, remainingLimit.doubleValue(), false));
                    }
                    accumulatedAmountAll = accumulatedAmountAll.add(remainingLimit);
                } else {
                    returnInstance.add(new OpenPositionListItem(openPositionRecord, true, remainingLimit.doubleValue(), false));
                }
            } else {
                returnInstance.add(new OpenPositionListItem(openPositionRecord, true, 0.0, false));
            }
        }
        return returnInstance;
    }

    private void internalStateUpdate(LiquidateAllActivityState state) {
        LiquidateAllActivityState oldState = this.state;
        this.state = state;
        btnSlippage.setText(Integer.toString(state.slippage));
        switch (state.buySellAll) {
            case BUY:
                btnBuySellAll.setText(R.string.lb_buy);
                break;
            case SELL:
                btnBuySellAll.setText(R.string.lb_sell);
                break;
            case ALL:
            default:
                btnBuySellAll.setText(R.string.lb_all);
                break;
        }
        toggleButtonWrapper.setChecked(false, state.liquidationMethod == LiquidationMethod.LAST_IN_FIRST_OUT);
        toggleButton.setChecked(state.liquidationMethod == LiquidationMethod.LAST_IN_FIRST_OUT);
        StringBuilder title = new StringBuilder(getResources().getString(state.allowSelectMultiple ? R.string.title_multiple_liquidate : R.string.title_liquidate_all));
        if (state.selectedAllContract) {
            tvTitle.setText(title);
        } else {
            tvTitle.setText(title.append(" (" + app.data.getContract(this.state.selectedContract).getContractName(getLanguage()) + ")"));
        }
        if (state.allowSelectMultiple) {
            btnLiquidateAmount.setText(Utility.formatLot(state.selectMultipleAmount.divide(BigDecimal.valueOf(app.data.getContract(this.state.selectedContract).iContractSize), RoundingMode.HALF_DOWN)));
            tvSelection.setText(getResources().getText(state.selectMultipleShortcutAll ? R.string.lb_unselect : R.string.lb_select));
            vLiquidateLot.setVisibility(View.VISIBLE);
            vLiquidateLot2.setVisibility(View.VISIBLE);
        } else {
            vLiquidateLot.setVisibility(View.GONE);
            vLiquidateLot2.setVisibility(View.GONE);
        }
        if (!Objects.equals(oldState.selectedContract, state.selectedContract) || oldState.selectedAllContract != state.selectedAllContract) {
            updateUI();
        } else if (!Objects.equals(oldState.liquidationMethod, state.liquidationMethod) || !Objects.equals(oldState.buySellAll, state.buySellAll)) {
            updateUI();
        }
    }

    @Override
    public void updateUI() {
        LiquidateAllActivityState state = this.state;
        mainHandler.post(
                () -> {
                    positionListAdapter.setSelectionMode(state.allowSelectMultiple);
                    if (this.state.allowSelectMultiple) {
                        for (Integer id : state.selectedPositionIds) {
                            positionListAdapter.getSelected().put(id, true);
                        }
                        for (Map.Entry<Integer, Boolean> entry : positionListAdapter.getSelected().entrySet()) {
                            if (!state.selectedPositionIds.contains(entry.getKey())) {
                                positionListAdapter.getSelected().put(entry.getKey(), false);
                            }
                        }
                        ContractObj contract = app.data.getContract(state.selectedContract);
                        BigDecimal maxLot = getLiquidateSelectedMaxLot(state, contract);
                        tvLiquidateRemainingLot.setText(String.format("/%s", Utility.formatLot(maxLot)));
                        if (state.selectMultipleAmount.compareTo(maxLot.multiply(BigDecimal.valueOf(contract.iContractSize))) > 0) {
                            mainHandler.post(
                                    () -> {
                                        LiquidateAllActivityStateBuilder builder = new LiquidateAllActivityStateBuilder(state);
                                        builder.setSelectMultipleAmount(getLiquidateSelectedMaxLot(state, contract).multiply(BigDecimal.valueOf(contract.iContractSize)));
                                        internalStateUpdate(builder.build());
                                    }
                            );
                        }
                    }
                    positionListAdapter.reload(getListItem(sortFilterOpenPositions(app.data.getOpenPositions().values(), state), state));
                    reloader.reload();
                }
        );

        if( ppidArray.size() > 0) {
            for (int i = 0 ; i < ppidArray.size() ; i ++) {
                String pid = ppidArray.get(i);
                TransactionObj t = app.data.getTransaction(pid);
                if (t != null) {
                    if (t.iStatusMsg == 916) {
                        ppidArray.remove(i);
                        this.finish();
                        goTo(ServiceFunction.SRV_OPEN_POSITION_SUMMARY);
                    } else if (t.iStatusMsg != 917 && t.iStatusMsg != -1) {
                        ppidArray.remove(i);
                        if (!CompanySettings.newinterface) {
                            this.finish();
                        }
                    }
                }
            }
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
    public void onDestroy() {
        popupSlippage.dismiss();
        popupBuySellAll.dismiss();
        super.onDestroy();
    }

    @Override
    public int getServiceId() {
        return ServiceFunction.SRV_LIQUIDATE_ALL;
    }

    @Override
    public int getActivityServiceCode() {
        return ServiceFunction.SRV_LIQUIDATE_ALL;
    }
}
