package com.mfinance.everjoy.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Messenger;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.bo.ContractObj;
import com.mfinance.everjoy.app.bo.TransactionObj;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.model.DataRepository;
import com.mfinance.everjoy.app.util.ColorController;
import com.mfinance.everjoy.app.util.CommonFunction;
import com.mfinance.everjoy.app.util.GUIUtility;
import com.mfinance.everjoy.app.util.InterestRateMethod;
import com.mfinance.everjoy.app.util.ListViewAdapterReloader;
import com.mfinance.everjoy.app.util.ListViewOnItemListener;
import com.mfinance.everjoy.app.util.Utility;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

public class ContractListAdapter extends BaseAdapter {
    private final String TAG = this.getClass().getSimpleName();
    private String pid = null;
    List<ContractObj> contracts;
    Context context;
    LayoutInflater m_inflater;
    Resources res;
    protected Messenger mService = null;
    protected Messenger mServiceMessengerHandler = null;
    MobileTraderApplication theApp;
    private final Consumer<ContractObj> onClickBidListener;
    private final Consumer<ContractObj> onClickAskListener;
    private final Consumer<ContractObj> onClickContractFlag;
    private final ListViewOnItemListener listener;
    private final DecimalFormat interestRateFormat;
    private final DecimalFormat interestFixedFormat;

    public ContractListAdapter(Context context, List<ContractObj> contracts, Messenger mService, Messenger mServiceMessengerHandler,
                               Consumer<ContractObj> onClickBidListener,
                               Consumer<ContractObj> onClickAskListener,
                               Consumer<ContractObj> onClickContractFlag,
                               ListViewOnItemListener listener) {
        this.context = context;
        this.mService = mService;
        this.mServiceMessengerHandler = mServiceMessengerHandler;
        m_inflater = LayoutInflater.from(context);
        res = context.getResources();
        this.onClickBidListener = onClickBidListener;
        this.onClickAskListener = onClickAskListener;
        this.onClickContractFlag = onClickContractFlag;
        this.contracts = Collections.unmodifiableList(contracts);
        this.listener = listener;
        interestRateFormat = new DecimalFormat();
        interestRateFormat.setMaximumFractionDigits(CompanySettings.INTEREST_RATE_PERCENTAGE_DECIMAL_PLACE);
        interestRateFormat.setMinimumFractionDigits(CompanySettings.INTEREST_RATE_PERCENTAGE_DECIMAL_PLACE);
        interestFixedFormat = new DecimalFormat();
        interestFixedFormat.setMaximumFractionDigits(2);
        interestFixedFormat.setMinimumFractionDigits(2);
        reload(contracts);
    }

    @Override
    public int getCount() {
        return contracts.size();
    }

    @Override
    public Object getItem(int position) {
        return contracts.get(position);
    }

    @Override
    public long getItemId(int position) {
        ContractObj contractObj = contracts.get(position);
        if (contractObj == null) {
            return 0;
        }
        return contractObj.strContractCode.hashCode();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = m_inflater.inflate(R.layout.list_item_price, null);
            View llAsk = convertView.findViewById(R.id.llAsk);
            llAsk.setOnTouchListener(listener);
            llAsk.setOnClickListener(v -> onClickAskListener.accept((ContractObj) v.getTag()));
            View llBid = convertView.findViewById(R.id.llBid);
            llBid.setOnTouchListener(listener);
            llBid.setOnClickListener(v -> onClickBidListener.accept((ContractObj) v.getTag()));
            View llFlag = convertView.findViewById(R.id.llFlag);
            llFlag.setOnTouchListener(listener);
            llFlag.setOnClickListener(v -> onClickContractFlag.accept((ContractObj) v.getTag()));
        }

        final Locale l = LocaleUtility.getLanguage(PreferenceManager.getDefaultSharedPreferences(context));
        String sContractName = "";
        ContractObj contract;
        try {
            contract = contracts.get(position);
        } catch (Exception ex) {
            contract = null;
        }

        if (contract == null) {
            return m_inflater.inflate(R.layout.list_item_price, null);
        }
        double[] dBidAsk = contract.getBidAsk();

        View tvTmp = convertView.findViewById(R.id.price);

        sContractName = contract.getContractName(l);

        ((TextView) tvTmp).setText(sContractName);

        String sBid = Utility.round(dBidAsk[0], contract.iRateDecPt, contract.iRateDecPt);
        String sAsk = Utility.round(dBidAsk[1], contract.iRateDecPt, contract.iRateDecPt);

        TextView tvBid1 = convertView.findViewById(R.id.tvBid1);
        TextView tvBid2 = convertView.findViewById(R.id.tvBid2);
        TextView tvAsk1 = convertView.findViewById(R.id.tvAsk1);
        TextView tvAsk2 = convertView.findViewById(R.id.tvAsk2);
        TextView tvHigh = convertView.findViewById(R.id.tvHigh);
        TextView tvLow = convertView.findViewById(R.id.tvLow);
        ToggleButton tb = convertView.findViewById(R.id.tbArrow);

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

            int pt_pos = sHighBid.indexOf(".");
            int iLen = sHighBid.length();

            if (pt_pos == -1 || pt_pos < iLen - 2)
                pt_pos = 2;
            else
                pt_pos = iLen - pt_pos + 1;
            try {
                String sHigh = sHighBid + "/" + sHighAsk.substring(iLen - pt_pos);
                String sLow = sLowBid + "/" + sLowAsk.substring(iLen - pt_pos);

                tvHigh.setText(sHigh);
                tvLow.setText(sLow);
            } catch (Exception e) {

            }
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
            ColorController.setPriceColor(res, contract.iBidUpDown, tvBid1);
            ColorController.setPriceColor(res, contract.iBidUpDown, tvBid2);
            ColorController.setPriceColor(res, contract.iAskUpDown, tvAsk1);
            ColorController.setPriceColor(res, contract.iAskUpDown, tvAsk2);
            ColorController.setVisible(contract.iBidUpDown, tb);
        } else {
            ColorController.setPriceColor(res, 0, tvBid1, R.color.price_list_no_change);
            ColorController.setPriceColor(res, 0, tvBid2, R.color.price_list_no_change);
            ColorController.setPriceColor(res, 0, tvAsk1, R.color.price_list_no_change);
            ColorController.setPriceColor(res, 0, tvAsk2, R.color.price_list_no_change);
            ColorController.setVisible(0, tb);
        }

        tvTmp = convertView.findViewById(R.id.llAsk);
        tvTmp.setTag(contract);

        tvTmp = convertView.findViewById(R.id.llBid);
        tvTmp.setTag(contract);

        View llFlag = convertView.findViewById(R.id.llFlag);
        llFlag.setTag(contract);

        ImageView iv = convertView.findViewById(R.id.ivCur1);
        if (contract.iCurr1Image != -1) {
            iv.setImageResource(contract.iCurr1Image);
        }

        iv = convertView.findViewById(R.id.ivCur2);

        if (contract.iCurr2Image != -1) {
            iv.setImageResource(contract.iCurr2Image);
        }

        TextView tvBidInterest = convertView.findViewById(R.id.tvBidInterest);
        TextView tvAskInterest = convertView.findViewById(R.id.tvAskInterest);
        tvBidInterest.setVisibility(CompanySettings.ENABLE_INTEREST_RATE_DISPLAY ? View.VISIBLE : View.GONE);
        tvAskInterest.setVisibility(CompanySettings.ENABLE_INTEREST_RATE_DISPLAY ? View.VISIBLE : View.GONE);
        if (CompanySettings.ENABLE_INTEREST_RATE_DISPLAY) {
            switch (contract.getInterestRateMethod()) {
                case PERCENTAGE:
                    String suffix = "%";
                    tvBidInterest.setText(interestRateFormat.format(contract.dBidInterest) + suffix);
                    tvAskInterest.setText(interestRateFormat.format(contract.dAskInterest) + suffix);
                    break;
                default:
                    tvBidInterest.setText(interestFixedFormat.format(contract.dBidInterest));
                    tvAskInterest.setText(interestFixedFormat.format(contract.dAskInterest));
                    break;
            }
        } else {
            tvBidInterest.setText("-");
            tvAskInterest.setText("-");
        }

        return convertView;
    }


    public void reload(List<ContractObj> contracts) {

        this.contracts = Collections.unmodifiableList(contracts);
        if (pid != null) {
            TransactionObj t = DataRepository.getInstance().getTransaction(pid);
            if (t != null && t.iStatusMsg == 916) {
                pid = null;
                ((Activity) context).finish();
                CommonFunction.moveTo(mService, mServiceMessengerHandler, ServiceFunction.SRV_OPEN_POSITION, null);
            } else if (t != null && t.iStatusMsg != 917) {
                pid = null;
            }
        }
    }
}
