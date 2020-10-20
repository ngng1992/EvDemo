package com.mfinance.everjoy.app;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mfinance.everjoy.app.bo.ContractObj;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.pojo.NewsSummary;
import com.mfinance.everjoy.app.pojo.PriceAlertHistoryObject;
import com.mfinance.everjoy.app.util.CommonFunction;
import com.mfinance.everjoy.app.util.Utility;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static com.mfinance.everjoy.app.constant.ServiceFunction.SRV_ANNOUNCEMENT;
import com.mfinance.everjoy.R;


public class PriceAlertHistoryActivity extends BaseActivity {
    private List<PriceAlertHistoryObject> priceAlertHistory = Collections.EMPTY_LIST;
    private Runnable updateInternalUI;

    public class ViewHolder {
        TextView lbContract;
        TextView lbType;
        TextView lbPrice;
        TextView lbTime;
    }

    @Override
    public void bindEvent() {

    }

    @Override
    public void handleByChild(Message msg) {

    }

    @Override
    public void loadLayout() {
        setContentView(R.layout.v_pricealerthistory);
        ListView listViewPriceAlertHistory = findViewById(R.id.listViewPriceAlertHistory);
        Drawable backgroundOdd = getDrawable(R.drawable.list_row_odd);
        Drawable backgroundEven = getDrawable(R.drawable.list_row_even);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        BaseAdapter adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return priceAlertHistory.size();
            }

            @Override
            public Object getItem(int position) {
                return priceAlertHistory.get(position);
            }

            @Override
            public long getItemId(int position) {
                return priceAlertHistory.get(position).getId();
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v;
                ViewHolder vh;
                if (convertView == null) {
                    v = getLayoutInflater().inflate(R.layout.list_item_pricealert_history, null, true);
                    vh = new ViewHolder();
                    vh.lbContract = v.findViewById(R.id.lbContract);
                    vh.lbType = v.findViewById(R.id.lbType);
                    vh.lbPrice = v.findViewById(R.id.lbPrice);
                    vh.lbTime = v.findViewById(R.id.lbTime);
                    if (position % 2 > 0) {
                        v.setBackground(backgroundOdd);
                    } else {
                        v.setBackground(backgroundEven);
                    }
                    v.setTag(vh);
                } else {
                    v = convertView;
                    vh = (ViewHolder) v.getTag();
                }
                PriceAlertHistoryObject pricehistory = priceAlertHistory.get(position);
                if (pricehistory == null) {
                    return v;
                }
                ContractObj contract = app.data.getContract(pricehistory.getMkt());
                vh.lbContract.setText(contract.getContractName(getLanguage()));
                vh.lbType.setText( Utility.getStringById("lb_pricealert_type"+Integer.toString(pricehistory.getType()), getResources()));
                String sTmp = Utility.round(pricehistory.getAlertPrice(), contract.iRateDecPt, contract.iRateDecPt);
                vh.lbPrice.setText(sTmp);
                vh.lbTime.setText(pricehistory.getDatecreated());

                return v;
            }

            @Override
            public boolean hasStableIds() {
                return true;
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
        listViewPriceAlertHistory.setAdapter(adapter);
        updateInternalUI = adapter::notifyDataSetChanged;
        priceAlertHistory = app.data.getPriceAlertHistoryList();
        updateInternalUI.run();
    }

    @Override
    public void updateUI() {
        priceAlertHistory = app.data.getPriceAlertHistoryList();
        updateInternalUI.run();
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
        return SRV_ANNOUNCEMENT;
    }

    @Override
    public int getActivityServiceCode() {
        return SRV_ANNOUNCEMENT;
    }
}
