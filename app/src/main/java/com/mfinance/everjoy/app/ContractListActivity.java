package com.mfinance.everjoy.app;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.bo.ContractObj;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.model.DataRepository;
import com.mfinance.everjoy.app.util.CommonFunction;
import com.mfinance.everjoy.app.util.ListViewAdapterReloader;
import com.mfinance.everjoy.app.util.ListViewOnItemListener;
import com.mfinance.everjoy.app.util.MessageMapping;
import com.mfinance.everjoy.app.util.Utility;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ContractListActivity extends BaseActivity {
	ListView lvPrice = null;
    ListViewAdapterReloader reloader = null;
//	TextView tvDefLOT = null;
	ContractListAdapter contractAdapter = null;
	ArrayAdapter<String> adapter;
	BiConsumer<ContractObj, Boolean> onClickBidAsk;
	Consumer<ContractObj> onClickContract;


	@Override
	public void bindEvent() {
		onClickBidAsk = (selectedContract, isBid) -> {
			if (!app.bLogon)
				return;

			if (isOneClickTradeEnable()) {
				if (!app.getPriceAgentConnectionStatus()) {
					Toast.makeText(this, MessageMapping.getMessageByCode(res, "307", getLanguage()), Toast.LENGTH_LONG).show();
					return;
				}
				String s1;
				String s2 = selectedContract.getContractName(getLanguage());
				String s3 = getString(R.string.lb_lot);
				String s4 = getDefaultLOT();
				String s5 = getString(R.string.are_you_sure);
				AlertDialog dialog = new AlertDialog.Builder(this, CompanySettings.alertDialogTheme).create();
				if (selectedContract.getBSD() == true) {
					if (isBid) {
						s1 = getString(R.string.lb_ask);
						dialog.setTitle(R.string.pop_action_buy);
					}
					else {
						s1 = getString(R.string.lb_bid);
						dialog.setTitle(R.string.pop_action_sell);
					}
				}
				else {
					if (!isBid) {
						s1 = getString(R.string.lb_ask);
						dialog.setTitle(R.string.pop_action_buy);
					}
					else {
						s1 = getString(R.string.lb_bid);
						dialog.setTitle(R.string.pop_action_sell);
					}
				}
				dialog.setMessage(String.format("%s %s\n%s %s\n%s", s1, s2, s3, s4, s5));
				dialog.setButton(DialogInterface.BUTTON_POSITIVE, res.getText(R.string.yes),
						(dialog1, which) -> {
							String pid = CommonFunction.getTransactionID(DataRepository.getInstance().getBalanceRecord().strAccount);
							if (selectedContract.getBSD() == true)
								CommonFunction.sendDealRequest(mService, mServiceMessengerHandler, selectedContract, isBid ? "B" : "S", s4, pid,  Long.toString(app.dServerDateTime.getTime()));
							else
								CommonFunction.sendDealRequest(mService, mServiceMessengerHandler, selectedContract, isBid ? "S" : "B", s4, pid,  Long.toString(app.dServerDateTime.getTime()));
						}
				);

				dialog.setButton(DialogInterface.BUTTON_NEGATIVE, res.getText(R.string.no),
						(dialog12, which) -> {

						}
				);
				dialog.show();
			} else {
				Bundle data = new Bundle();
				data.putString(ServiceFunction.DEAL_CONTRACT, selectedContract.strContractCode);
				if (selectedContract.getBSD()) {
					data.putString(ServiceFunction.DEAL_BUY_SELL, isBid ? "B" : "S");
				} else {
					data.putString(ServiceFunction.DEAL_BUY_SELL, isBid ? "S" : "B");
				}
				CommonFunction.moveTo(mService, mServiceMessengerHandler, ServiceFunction.SRV_DEAL, data);
			}
		};
		onClickContract = (selectedContract) -> {
			Bundle data = new Bundle();
			data.putString(ServiceFunction.CONTRACT_DETAIL_CONTRACT, selectedContract.strContractCode);
			CommonFunction.moveTo(mService, mServiceMessengerHandler, ServiceFunction.SRV_CONTRACT_DETAIL, data);
		};
	}

	@Override
	public void handleByChild(Message msg) {}

	@Override
	public void loadLayout() {
		setContentView(R.layout.v_price);
		lvPrice = findViewById(R.id.lvPrice);
		TextView tvDefLOT = findViewById(R.id.tvDefLot);
		View lbDefLot = findViewById(R.id.lbDefLot);
		tvDefLOT.setVisibility(!CompanySettings.ENABLE_CONTRACT_LEVEL_SETTING && CompanySettings.ENABLE_ONE_CLICK_TRADE ? View.VISIBLE : View.GONE);
		tvDefLOT.setText(getDefaultLOT());
		lbDefLot.setVisibility(!CompanySettings.ENABLE_CONTRACT_LEVEL_SETTING && CompanySettings.ENABLE_ONE_CLICK_TRADE ? View.VISIBLE : View.GONE);

		Optional<ImageView> btnSort = Optional.ofNullable(findViewById(R.id.btnSort));
        btnSort.ifPresent(s -> {
           s.setOnClickListener(v -> {
               PopupMenu popup = new PopupMenu(ContractListActivity.this, v);
               popup.setOnMenuItemClickListener((item) -> {
                   switch (item.getItemId()) {
                       case R.id.menuItemSort:
                           CommonFunction.moveTo(mService, mServiceMessengerHandler, ServiceFunction.SRV_CONTRACT_SORT, null);
                           break;
                       case R.id.menuItemContractSetting:
                           if (app.getSelectedContract() == null) {
                               app.setSelectedContract(app.getDefaultContract());
                           }
                           Intent intent = new Intent(ContractListActivity.this, SettingContractListActivity.class);
                           intent.putExtra(ServiceFunction.REQUIRE_LOGIN, app.bLogon);
                           startActivity(intent);
                           break;
                       default:
                           break;
                   }
                   return true;
               });
               popup.inflate(R.menu.price_setting);
               popup.getMenu().findItem(R.id.menuItemSort).setEnabled(CompanySettings.ENABLE_CONTRACT_SORT).setVisible(CompanySettings.ENABLE_CONTRACT_SORT);
               popup.getMenu().findItem(R.id.menuItemContractSetting).setEnabled(CompanySettings.ENABLE_CONTRACT_LEVEL_SETTING).setVisible(CompanySettings.ENABLE_CONTRACT_LEVEL_SETTING);
               popup.show();
           });
           s.setVisibility(CompanySettings.ENABLE_CONTRACT_SORT || CompanySettings.ENABLE_CONTRACT_LEVEL_SETTING ? View.VISIBLE : View.GONE);
        });
	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		super.onServiceConnected(name, service);
		ListViewOnItemListener listener = new ListViewOnItemListener();
		contractAdapter = new ContractListAdapter(
				this,
				Utility.getViewableContract(app.data).collect(Collectors.toList()),
				mService,
				mServiceMessengerHandler,
				(c) -> onClickBidAsk.accept(c, true),
				(c) -> onClickBidAsk.accept(c, false),
				onClickContract,
				listener
		);
		lvPrice.setAdapter(contractAdapter);
		lvPrice.setOnItemClickListener(listener);
		lvPrice.setItemsCanFocus(true);
        reloader = new ListViewAdapterReloader(lvPrice, contractAdapter);
		reloader.reload();
	}

	@Override
	public void updateUI() {
		if (contractAdapter != null) {
			contractAdapter.reload(Utility.getViewableContract(app.data).collect(Collectors.toList()));
            reloader.reload();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
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