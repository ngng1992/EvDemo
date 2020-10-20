package com.mfinance.everjoy.app;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.bo.ContractObj;
import com.mfinance.everjoy.app.bo.OpenPositionRecord;
import com.mfinance.everjoy.app.bo.TransactionObj;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.util.CommonFunction;
import com.mfinance.everjoy.app.util.ListViewAdapterReloader;
import com.mfinance.everjoy.app.util.ListViewOnItemListener;
import com.mfinance.everjoy.app.util.MessageMapping;
import com.mfinance.everjoy.app.util.Utility;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class OpenPositionListActivity extends BaseActivity{
    private ListView lvPosition = null;
    private ListViewAdapterReloader reloader = null;
    private View vFunction = null;
    private OpenPositionListAdapter positionListAdapter = null;
	boolean bLiqMode = false;
	boolean bSelectAll = true;
	private TextView tvSelection;
	private PopupOpenPositingDetail popupDetail;
    private boolean bSummary = false;
    private String sBuySell = "";
    private String sContract = "";
    private String pid = null;

    private Runnable updateTitle = () -> {
        ContractObj contract = app.data.getContract(sContract);
        TextView tv = findViewById(R.id.tvTitle);

        if (bSummary){
        	if (contract != null) {
				String strTitle;
				strTitle = res.getString(R.string.title_open_position) + "("+contract.getContractName(getLanguage())+")";
				tv.setText(strTitle);
			}
        }else{
            tv.setText(R.string.title_open_position);
        }
    };
	@Override
	public void bindEvent() {
		Button btnLiq = findViewById(R.id.btnLiq);
		btnLiq.setOnClickListener((View v) -> {
			if (!app.getPriceAgentConnectionStatus()) {
				Toast.makeText(OpenPositionListActivity.this, MessageMapping.getMessageByCode(res, "307", app.locale), Toast.LENGTH_LONG).show();
				return;
			}

			if (bLiqMode) {
				if (positionListAdapter.getSelected().size() > 0) {
					List<OpenPositionRecord> alLiqList = positionListAdapter.liquidateSelected();
					ContractObj contract = app.data.getContract(sContract);
					BigDecimal sum = alLiqList.stream().map(r -> BigDecimal.valueOf(r.dAmount)).reduce(BigDecimal.ZERO, BigDecimal::add);
					BigDecimal limit = BigDecimal.valueOf(contract.getFinalMaxLotLimit()).multiply(BigDecimal.valueOf(contract.iContractSize));
					if (sum.compareTo(limit) > 0) {
						Toast.makeText(OpenPositionListActivity.this, res.getString(R.string.msg_947), Toast.LENGTH_LONG).show();
						return;
					}

					if (alLiqList.size() <=0 ){
						Toast.makeText(OpenPositionListActivity.this, res.getString(R.string.liquidation_error), Toast.LENGTH_LONG).show();
						return;
					}

					AlertDialog dialog = new AlertDialog.Builder(OpenPositionListActivity.this, CompanySettings.alertDialogTheme).create();
					dialog.setMessage(res.getText(R.string.confirm_liquidation));
					dialog.setButton(
							DialogInterface.BUTTON_POSITIVE,
							res.getText(R.string.btn_confirm),
							(dialog1, which) -> {
								pid = CommonFunction.getTransactionID(app.data.getBalanceRecord().strAccount);
								if (CommonFunction.sendLiquidationRequest1(mService, mServiceMessengerHandler, alLiqList, Long.toString(app.dServerDateTime.getTime()), pid)) {
									Toast.makeText(OpenPositionListActivity.this, res.getString(R.string.msg_request_sent), Toast.LENGTH_LONG).show();
								} else {
									AlertDialog dialog2 = new AlertDialog.Builder(this, CompanySettings.alertDialogTheme).create();
									dialog2.setMessage(res.getText(R.string.liquidation_fail));
									dialog2.show();
								}
								bLiqMode = !bLiqMode;
								changeLiqMode(bLiqMode);
								btnLiq.setText(R.string.confirm);
							}
					);
					dialog.setButton(
							DialogInterface.BUTTON_NEGATIVE,
							res.getText(R.string.btn_cancel),
							(dialog12, which) -> {
								bLiqMode = !bLiqMode;
								changeLiqMode(bLiqMode);
							}
					);
					dialog.show();
				}

			} else {
				bLiqMode = !bLiqMode;
				changeLiqMode(bLiqMode);
			}
		});
		Optional.<View>ofNullable(findViewById(R.id.btnCancel))
                .ifPresent(v -> v.setOnClickListener(v1 -> {
                	if (bLiqMode) {
                		bLiqMode = false;
                		changeLiqMode(bLiqMode);
					}
				}));

        Optional.<View>ofNullable(findViewById(R.id.llSelectAll))
                .ifPresent(v ->
                    v.setOnClickListener(v1 -> positionListAdapter.applySelectionToAll(true))
                );
		View llUnSelectAll = findViewById(R.id.llUnSelectAll);
		if (CompanySettings.newinterface) {
			llUnSelectAll.setOnClickListener(v -> {
				positionListAdapter.applySelectionToAll(bSelectAll);
				bSelectAll = !bSelectAll;
				tvSelection.setText(bSelectAll ? R.string.lb_select : R.string.lb_unselect);
			});
		} else {
			llUnSelectAll.setOnClickListener(v -> {
				positionListAdapter.applySelectionToAll(false);
			});
		}

	}
	
	
	
	private void changeLiqMode(boolean bLiqMode) {
		if (bLiqMode) {
			runOnUiThread(() -> {
				((Button)findViewById(R.id.btnLiq)).setText(R.string.btn_submit);
				Optional.<View>ofNullable(findViewById(R.id.btnCancel))
						.ifPresent(v -> v.setVisibility(View.VISIBLE));
				positionListAdapter.applySelectionToAll(false);
				positionListAdapter.setSelectionMode(true);
				if (bSummary) {
					vFunction.setVisibility(View.VISIBLE);
				} else {
					vFunction.setVisibility(View.INVISIBLE); // Do not need this any more!
				}
				positionListAdapter.notifyDataSetChanged();
                updateTitle.run();
                if (CompanySettings.newinterface) {
					bSelectAll = true;
					tvSelection.setText(R.string.lb_select);
				}
			});
		} else {
			runOnUiThread(() -> {
				((Button)findViewById(R.id.btnLiq)).setText(R.string.lb_liquidate);
				Optional.<View>ofNullable(findViewById(R.id.btnCancel))
						.ifPresent(v -> v.setVisibility(View.GONE));
				positionListAdapter.setSelectionMode(false);
				vFunction.setVisibility(View.INVISIBLE);
				positionListAdapter.notifyDataSetChanged();
                updateTitle.run();
			});
		}
	}

	@Override
	public void handleByChild(Message msg) {
		switch(msg.what){
		case ServiceFunction.ACT_OPEN_POSITION_DETAIL:
			if (popupDetail==null){
				popupDetail = new PopupOpenPositingDetail(OpenPositionListActivity.this, findViewById(R.id.rlTop), app,mService, mServiceMessengerHandler);
			}
			popupDetail.showLikeQuickAction();
			break;
		}
	}

	@Override
	public void loadLayout() {
		if (CompanySettings.newinterface)
			setContentView(R.layout.v_open_position_new);
		else
			setContentView(R.layout.v_open_position);
		lvPosition = findViewById(R.id.lvPosition);
		lvPosition.setSelected(true);
		vFunction = findViewById(R.id.llFunction);
		vFunction.setVisibility(View.INVISIBLE);
		Bundle extras = getIntent().getExtras();
		try {
			bSummary = extras.getBoolean(ServiceFunction.SRV_OPEN_POSITION_FROM_SUMMARY);
			sBuySell = extras.getString(ServiceFunction.SRV_OPEN_POSITION_BUY_SELL);
			sContract = extras.getString(ServiceFunction.SRV_OPEN_POSITION_CONTRACT);
		} catch(Exception e) {
			bSummary = false;
		}
		if( !bSummary || app.data.getBalanceRecord().hedged == true || !CompanySettings.ENABLE_OPENPOSITION_SUMMARY_LIQUIDATION) {
            findViewById(R.id.btnLiq).setVisibility(View.INVISIBLE);
            Optional.<View>ofNullable(findViewById(R.id.llLiquidate)).ifPresent(
                    v -> v.setVisibility(View.GONE)
            );
        }


		if (CompanySettings.newinterface) {
			TextView tv = findViewById(R.id.lb_market_price);
			tv.setText(Utility.getStringById("lb_price_open_position", getResources()) + "/" + Utility.getStringById("lb_market_price_shortform", getResources()));
			tvSelection = findViewById(R.id.tvSelection);
			tvSelection.setText(R.string.lb_unselect);
		}
	}
	
	@Override
	public void updateUI() {
		ContractObj contract = app.getSelectedContract();
		//System.out.println(contract);

		if (!bSummary) {
			List<OpenPositionListItem> positions = app.data.getOpenPositions()
					.values()
					.stream()
					.sorted(Utility.getFIFOComparator())
					.map(r -> new OpenPositionListItem(r, false, 0.0, true))
					.collect(Collectors.toList());

			for (int i = 0 ; i < positions.size() ; i ++){
				System.out.println("debug updateUI OP "+positions.get(i).getPosition().iRef);
			}

			if (positionListAdapter == null) {
				ListViewOnItemListener listener = new ListViewOnItemListener();
				positionListAdapter = new OpenPositionListAdapter(
				        OpenPositionListActivity.this,
                        positions,
						(v) -> {
				        	Integer iRef = v.getPosition().iRef;
							if (positionListAdapter.isInSelectionMode()) {
								// force the state of other rows to false! (Restrict Single Select)
								Iterator<HashMap.Entry<Integer, Boolean>> entries = positionListAdapter.getSelected().entrySet().iterator();
								while (entries.hasNext()) {
									HashMap.Entry<Integer, Boolean> entry = entries.next();
									if (entry.getKey().compareTo(iRef) != 0) {
										entry.setValue(false);
									}
								}
								positionListAdapter.getSelected().put(iRef, !positionListAdapter.getSelected().get(iRef));
								positionListAdapter.notifyDataSetChanged();
							} else {
								app.setSelectedOpenPosition(iRef);
								CommonFunction.moveTo(mService, mServiceMessengerHandler, ServiceFunction.SRV_OPEN_POSITION_DETAIL, null);
							}
						},
						listener);
				lvPosition.setAdapter(positionListAdapter);
				lvPosition.setItemsCanFocus(true);
			} else {
				positionListAdapter.reload(positions);
			}
		} else {
			HashMap<Integer, OpenPositionRecord> hmPosition;
			if ("B".equals(app.getSelectedBuySell())) {
				hmPosition = contract.getBuyPositions();
			} else {
				hmPosition = contract.getSellPositions();
			}
			List<OpenPositionListItem> positions = hmPosition.values()
					.stream()
					.sorted(Utility.getFIFOComparator())
					.map(r -> new OpenPositionListItem(r, false, 0.0, true))
					.collect(Collectors.toList());
			if (positionListAdapter == null) {
				ListViewOnItemListener listener = new ListViewOnItemListener();
				positionListAdapter = new OpenPositionListAdapter(
				        OpenPositionListActivity.this,
                        positions,
						(v) -> {
				        	Integer iRef = v.getPosition().iRef;
							if (positionListAdapter.isInSelectionMode()) {
								positionListAdapter.getSelected().put(iRef, !positionListAdapter.getSelected().get(iRef));
								positionListAdapter.notifyDataSetChanged();
							} else {
								app.setSelectedOpenPosition(iRef);
								CommonFunction.moveTo(mService, mServiceMessengerHandler, ServiceFunction.SRV_OPEN_POSITION_DETAIL, null);
							}
						},
						listener);
				lvPosition.setAdapter(positionListAdapter);
			} else {
				positionListAdapter.reload(positions);
			}
		}
		if (reloader == null)
			reloader = new ListViewAdapterReloader(lvPosition, positionListAdapter);
		reloader.reload();
		if (popupDetail != null)
			popupDetail.updateUI();

//		if( pid != null)
//		{
//			TransactionObj t = app.data.getTransaction(pid);
//			if( t != null && t.iStatusMsg == 916 )
//			{
//				pid = null;
//				OpenPositionListActivity.this.finish();
//				goTo(ServiceFunction.SRV_LIQUIDATION_HISTORY);
//			}
//			else if( t != null && t.iStatusMsg != 917 )
//			{
//				pid = null;
//				OpenPositionListActivity.this.finish();
//			}
//		}
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
	protected void onResume() {
		super.onResume();
        updateTitle.run();
	}
	
	@Override
	public int getServiceId() {
		return ServiceFunction.SRV_OPEN_POSITION;
	}
	
	@Override
	public int getActivityServiceCode(){
		return ServiceFunction.SRV_OPEN_POSITION;
	}
}