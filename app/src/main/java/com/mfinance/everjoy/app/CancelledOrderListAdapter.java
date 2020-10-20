package com.mfinance.everjoy.app;

import android.content.Context;
import android.content.res.Resources;
import android.os.Messenger;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.bo.CancelledOrder;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.util.CommonFunction;
import com.mfinance.everjoy.app.util.ListViewOnItemListener;
import com.mfinance.everjoy.app.util.Utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * A adapter for storing canceled order record
 *
 * @author : justin.lai
 * @version : v1.00
 * @ModificationHistory Who            When			Version			What<br>
 * -------------------------------------------------------------------------------<br>
 * justin.lai	2011-04-14		v1.00			Creation<br>
 */
public class CancelledOrderListAdapter extends BaseAdapter {

    Resources res;

    /**
     * Tag name for logging
     */
    private final String TAG = this.getClass().getSimpleName();
    /**
     * A HashMap for storing data
     */
    private HashMap<Integer, CancelledOrder> hmCancelledOrder = null;
    /**
     * ArrayList for storing key value
     */
    private ArrayList<Integer> alRef = new ArrayList<Integer>();
    /**
     * Context View
     */
    private Context context = null;
    /**
     * This class is used to instantiate layout XML file into its corresponding View objects
     */
    private LayoutInflater m_inflater;
    /**
     * Application instance
     */
    private MobileTraderApplication app = null;
    /**
     * A messenger for sending message to service
     */
    protected Messenger mService = null;
    /**
     * A receiver for receiving message from service
     */
    protected Messenger mServiceMessengerHandler = null;
    private final ListViewOnItemListener listener;

    /**
     * Constructor
     *
     * @param context                  - parent view
     * @param hmOrder                  - canceled order record
     * @param mService                 - a messenger for sending message to service
     * @param mServiceMessengerHandler - a receiver for receiving message from service
     */
    public CancelledOrderListAdapter(Context context, HashMap<Integer, CancelledOrder> hmOrder, Messenger mService, Messenger mServiceMessengerHandler, ListViewOnItemListener listener) {
        this.context = context;
        m_inflater = LayoutInflater.from(context);
        app = (MobileTraderApplication) context.getApplicationContext();
        this.mService = mService;
        this.mServiceMessengerHandler = mServiceMessengerHandler;
        res = context.getResources();
        this.listener = listener;
        reload(hmOrder);
    }

    @Override
    public int getCount() {
        return hmCancelledOrder.size();
    }

    @Override
    public Object getItem(int position) {
        return hmCancelledOrder.get(alRef.get(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        synchronized (alRef) {
            if (alRef.size() > 0) {
                View vItem;
                if (convertView == null) {
                    if (CompanySettings.newinterface)
                        convertView = m_inflater.inflate(R.layout.list_item_cancelled_order_new, null);
                    else
                        convertView = m_inflater.inflate(R.layout.list_item_cancelled_order, null);
                    vItem = convertView.findViewById(R.id.llOrder);
                    vItem.setOnTouchListener(listener);
                } else {
                    vItem = convertView.findViewById(R.id.llOrder);
                }

                CancelledOrder order = hmCancelledOrder.get(alRef.get(position));

                if (order != null) {
                    String sTmp;
                    vItem.setTag(order.iRef);

                    TextView tvTmp = (TextView) convertView.findViewById(R.id.lbContract);
                    tvTmp.setText(order.contract.getContractName(app.locale));

                    tvTmp = (TextView) convertView.findViewById(R.id.lbCancelledDate);
                    tvTmp.setText(Utility.displayListingDate(order.sCancelledDate));

                    if (CompanySettings.newinterface) {
                        if ("B".equals(order.strBuySell)) {
                            ((ImageView) convertView.findViewById(R.id.ivBuy)).setImageResource(R.drawable.ico_buy);
                            if (convertView.findViewById(R.id.ivBuy) != null)
                                convertView.findViewById(R.id.ivBuy).setVisibility(View.VISIBLE);
                            if (convertView.findViewById(R.id.lbBuy) != null)
                                ((TextView) convertView.findViewById(R.id.lbBuy)).setText(Utility.getStringById("lb_b", this.context.getResources()));
                        } else {
                            ((ImageView) convertView.findViewById(R.id.ivBuy)).setImageResource(R.drawable.ico_sell);
                            if (convertView.findViewById(R.id.ivBuy) != null)
                                convertView.findViewById(R.id.ivBuy).setVisibility(View.VISIBLE);
                            if (convertView.findViewById(R.id.lbBuy) != null)
                                ((TextView) convertView.findViewById(R.id.lbBuy)).setText(Utility.getStringById("lb_s", this.context.getResources()));
                        }

                        //lot store at Limit or Stop
                        sTmp = Utility.formatLot(order.dAmount / order.contract.iContractSize);
                        if (!CompanySettings.SHOW_ORDERTYPE_COLUMN_ON_LISTVIEW) {
                            if (order.iLimitStop == 0) {
                                ((TextView) convertView.findViewById(R.id.lbLimit)).setText(sTmp);
                                ((TextView) convertView.findViewById(R.id.lbStop)).setText("");
                            } else {
                                ((TextView) convertView.findViewById(R.id.lbStop)).setText("");
                                ((TextView) convertView.findViewById(R.id.lbLimit)).setText(sTmp);
                            }
                        } else {
                            ((TextView) convertView.findViewById(R.id.lbLimit)).setText(sTmp);
                        }
                    } else {
                        if ("B".equals(order.strBuySell)) {
                            if (convertView.findViewById(R.id.ivBuy) != null)
                                convertView.findViewById(R.id.ivBuy).setVisibility(View.VISIBLE);
                            if (convertView.findViewById(R.id.ivSell) != null)
                                convertView.findViewById(R.id.ivSell).setVisibility(View.INVISIBLE);
                            if (convertView.findViewById(R.id.lbBuy) != null)
                                ((TextView) convertView.findViewById(R.id.lbBuy)).setText(Utility.getStringById("lb_b", this.context.getResources()));
                        } else {
                            if (convertView.findViewById(R.id.ivBuy) != null)
                                convertView.findViewById(R.id.ivBuy).setVisibility(View.INVISIBLE);
                            if (convertView.findViewById(R.id.ivSell) != null)
                                convertView.findViewById(R.id.ivSell).setVisibility(View.VISIBLE);
                            if (convertView.findViewById(R.id.lbBuy) != null)
                                ((TextView) convertView.findViewById(R.id.lbBuy)).setText(Utility.getStringById("lb_s", this.context.getResources()));
                        }

                        //lot store at Limit or Stop
                        sTmp = Utility.formatLot(order.dAmount / order.contract.iContractSize);
                        if (!CompanySettings.SHOW_ORDERTYPE_COLUMN_ON_LISTVIEW) {
                            if (order.iLimitStop == 0) {
                                ((TextView) convertView.findViewById(R.id.lbLimit)).setText(sTmp);
                                ((TextView) convertView.findViewById(R.id.lbStop)).setText("");
                            } else {
                                ((TextView) convertView.findViewById(R.id.lbLimit)).setText("");
                                ((TextView) convertView.findViewById(R.id.lbStop)).setText(sTmp);
                            }
                        } else {
                            ((TextView) convertView.findViewById(R.id.lbLimit)).setText(sTmp);
                        }
                    }

                    tvTmp = (TextView) convertView.findViewById(R.id.lbCancelledTime);
                    sTmp = order.sCancelledTime;
                    tvTmp.setText(Utility.timeToEngString(sTmp));

                    tvTmp = (TextView) convertView.findViewById(R.id.lbReqPrice);
                    sTmp = Utility.round(order.dRequestRate, order.contract.iRateDecPt, order.contract.iRateDecPt);
                    tvTmp.setText(sTmp);

                    boolean isLiq = order.iLiquidationMethod == 3;
                    convertView.findViewById(R.id.ivOco).setVisibility(order.iOCORef > 0 ? View.VISIBLE : View.INVISIBLE);
                    if (convertView.findViewById(R.id.ivLiq) != null)
                        convertView.findViewById(R.id.ivLiq).setVisibility(isLiq ? View.VISIBLE : View.INVISIBLE);

                    if (CompanySettings.SHOW_ORDERTYPE_COLUMN_ON_LISTVIEW) {
                        if (isLiq) {
                            if (order.iLimitStop == 0) {
                                ((TextView) convertView.findViewById(R.id.lbStop)).setText(R.string.tb_limit);
                            } else {
                                ((TextView) convertView.findViewById(R.id.lbStop)).setText(R.string.tb_stop);
                            }
                        } else {
                            ((TextView) convertView.findViewById(R.id.lbStop)).setText(Utility.getStringById("tb_new", this.context.getResources()));
                        }
                    }

                    vItem.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int iRef = (Integer) v.getTag();
                            app.setSelectedCancelledOrder(iRef);
                            CommonFunction.moveTo(mService, mServiceMessengerHandler, ServiceFunction.SRV_CANCELLED_ORDER_DETAIL, null);
                        }
                    });

                    if (position % 2 != 0)
                        vItem.setBackgroundResource(R.drawable.list_row_odd);
                    else
                        vItem.setBackgroundResource(R.drawable.list_row_even);

                }


                return convertView;
            } else {
                return convertView = m_inflater.inflate(R.layout.list_item_running_order, null);
            }
        }
    }

    /**
     * Reload data
     *
     * @param hmOrder - canceled order
     */
    public void reload(HashMap<Integer, CancelledOrder> hmOrder) {
        synchronized (alRef) {
            this.hmCancelledOrder = (HashMap<Integer, CancelledOrder>) hmOrder.clone();
            alRef.clear();
            alRef.addAll(hmOrder.keySet());

            Collections.sort(alRef, new Comparator<Integer>() {

                @Override
                public int compare(Integer i1, Integer i2) {
                    if (i1 > i2) {
                        return -1;
                    } else if (i2 > i1) {
                        return 1;
                    } else
                        return 0;
                }

            });
        }
    }

}
