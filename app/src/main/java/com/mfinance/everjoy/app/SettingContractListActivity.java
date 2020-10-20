package com.mfinance.everjoy.app;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mfinance.everjoy.app.bo.ContractObj;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.pojo.ContractDefaultSetting;
import com.mfinance.everjoy.app.util.Utility;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.mfinance.everjoy.app.constant.ServiceFunction.SRV_SETTING_CONTRACT;
import com.mfinance.everjoy.R;
public class SettingContractListActivity extends BaseActivity {
    public class ViewHolder {
        TextView tvContract;
        TextView tvLot;
        TextView tvSlippage;
        TextView tvLimit;
        TextView tvStop;
    }

    private Handler mainHandler;
    private Map<String, ContractDefaultSetting> contractDefaultSettingMap = Collections.EMPTY_MAP;
    private List<Pair<String, ContractDefaultSetting>> contractList = Collections.EMPTY_LIST;
    private Runnable updateIUInternal = () -> {
    };

    @Override
    public void bindEvent() {

    }

    @Override
    public void handleByChild(Message msg) {

    }


    @Override
    public void loadLayout() {
        setContentView(R.layout.v_setup_contract_list);
        mainHandler = new Handler(getMainLooper());
        BaseAdapter adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return contractList.size();
            }

            @Override
            public Object getItem(int position) {
                return contractList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return contractList.get(position).first.hashCode();
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v;
                ViewHolder vh;
                if (convertView == null) {
                    v = getLayoutInflater().inflate(R.layout.list_item_setting_contract, null, true);
                    vh = new ViewHolder();
                    vh.tvContract = v.findViewById(R.id.tvContract);
                    vh.tvLimit = v.findViewById(R.id.tvLimit);
                    vh.tvLot = v.findViewById(R.id.tvLot);
                    vh.tvSlippage = v.findViewById(R.id.tvSlippage);
                    vh.tvStop = v.findViewById(R.id.tvStop);
                    v.setBackgroundResource(position % 2 != 0 ? R.drawable.list_row_odd_normal : R.drawable.list_row_even_normal);
                    v.setTag(vh);
                } else {
                    v = convertView;
                    vh = (ViewHolder) v.getTag();
                }
                Pair<String, ContractDefaultSetting> pair = contractList.get(position);
                if (pair == null) {
                    return v;
                }
                v.setOnClickListener(v1 -> {
                    app.setSelectedContract(app.data.getContract(pair.first));
                    Intent intent = new Intent(SettingContractListActivity.this, SettingContractActivity.class);
                    intent.putExtra(ServiceFunction.REQUIRE_LOGIN, app.bLogon);
                    startActivity(intent);
                });
                vh.tvContract.setText(app.data.getContract(pair.first).getContractName(getLanguage()));
                vh.tvLot.setText(Utility.getDecimalFormatLotSize().format(pair.second.getDefaultLotSize()));
                vh.tvSlippage.setText(pair.second.getDefaultSlippage().toString());
                vh.tvLimit.setText(pair.second.getDefaultTakeProfitOrderPips().map(Object::toString).orElse("-"));
                vh.tvStop.setText(pair.second.getDefaultStopLossOrderPips().map(Object::toString).orElse("-"));

                return v;
            }

            @Override
            public int getItemViewType(int position) {
                return position % 2;
            }

            @Override
            public int getViewTypeCount() {
                return 2;
            }
        };
        ListView lvRecord = findViewById(R.id.lvRecord);
        lvRecord.setSelected(true);
        lvRecord.setAdapter(adapter);
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        updateIUInternal = adapter::notifyDataSetChanged;
        contractList = Utility.getViewableContract(app.data)
                .map(contractObj -> new Pair<>(contractObj.strContractCode, contractDefaultSettingMap.getOrDefault(contractObj.strContractCode, Utility.EMPTY_CONTRACT_DEFAULT_SETTING)))
                .collect(Collectors.toList());
        updateIUInternal.run();
    }

    @Override
    public void updateUI() {
        Map<String, ContractDefaultSetting> oldMap = contractDefaultSettingMap;
        contractDefaultSettingMap = app.data.getContractDefaultSettingMap();
        if (oldMap != contractDefaultSettingMap) {
            contractList = Utility.getViewableContract(app.data)
                    .map(contractObj -> new Pair<>(contractObj.strContractCode, contractDefaultSettingMap.getOrDefault(contractObj.strContractCode, Utility.EMPTY_CONTRACT_DEFAULT_SETTING)))
                    .collect(Collectors.toList());
            updateIUInternal.run();
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
        return SRV_SETTING_CONTRACT;
    }

    @Override
    public int getActivityServiceCode() {
        return SRV_SETTING_CONTRACT;
    }
}
