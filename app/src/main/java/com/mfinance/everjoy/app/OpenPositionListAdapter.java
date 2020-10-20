package com.mfinance.everjoy.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.bo.OpenPositionRecord;
import com.mfinance.everjoy.app.util.ColorController;
import com.mfinance.everjoy.app.util.GUIUtility;
import com.mfinance.everjoy.app.util.ListViewOnItemListener;
import com.mfinance.everjoy.app.util.Utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

public class OpenPositionListAdapter extends BaseAdapter {
    private final String TAG = this.getClass().getSimpleName();
    private List<OpenPositionListItem> positions = Collections.EMPTY_LIST;
    private HashMap<Integer, Boolean> hmSelect = new HashMap<Integer, Boolean>();
    private Context context;
    private LayoutInflater m_inflater;
    private final MobileTraderApplication app;
    private boolean bSelectionMode = false;
    private final Consumer<OpenPositionListItem> onItemClicked;
    private final Drawable checkedDraw;
    private final Drawable uncheckedDraw;
    private final ListViewOnItemListener listener;

    public OpenPositionListAdapter(Context context, List<OpenPositionListItem> positions, Consumer<OpenPositionListItem> onItemClicked, ListViewOnItemListener listener) {
        super();
        this.context = context;
        m_inflater = LayoutInflater.from(context);
        app = (MobileTraderApplication) context.getApplicationContext();
        this.onItemClicked = onItemClicked;
        Drawable drawable = context.getDrawable(R.drawable.icon_checkedbox);
        if (drawable instanceof BitmapDrawable) {
            checkedDraw = new BitmapDrawable(context.getResources(), Bitmap.createScaledBitmap(((BitmapDrawable) drawable).getBitmap(), 40, 40, true));
        } else {
            checkedDraw = drawable;
        }
        Drawable drawable1 = context.getDrawable(R.drawable.icon_checkbox);
        if (drawable1 instanceof BitmapDrawable) {
            uncheckedDraw = new BitmapDrawable(context.getResources(), Bitmap.createScaledBitmap(((BitmapDrawable)  drawable1).getBitmap(), 40, 40, true));
        } else {
            uncheckedDraw = drawable1;
        }
        this.listener = listener;
        reload(positions);
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

    @Override
    public int getCount() {
        return positions.size();
    }

    @Override
    public Object getItem(int position) {
        return positions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return positions.get(position).getPosition().iRef;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            ListView listView = (ListView) parent;
            if (CompanySettings.newinterface)
                convertView = m_inflater.inflate(R.layout.list_item_open_position_new, null);
            else
                convertView = m_inflater.inflate(R.layout.list_item_open_position, null);
            if (position % 2 == 0) {
                convertView.setBackgroundResource(R.drawable.list_row_odd);
            } else {
                convertView.setBackgroundResource(R.drawable.list_row_even);
            }
            final View convertViewFinal = convertView;
            OnClickListener onClickListener = (v) -> {
                OpenPositionListItem openPositionListItem = positions.get(listView.getPositionForView(convertViewFinal));
                if (openPositionListItem != null) {
                    onItemClicked.accept(openPositionListItem);
                }
            };
            convertView.setOnClickListener(onClickListener);
            View vItem = convertView.findViewById(R.id.llPosition);
            vItem.setOnTouchListener(listener);
            vItem.setOnClickListener(onClickListener);
        }
        OpenPositionListItem openPositionListItem;
        try {
            openPositionListItem = positions.get(position);
        } catch (Exception ex) {
            openPositionListItem = null;
        }
        if (openPositionListItem != null) {
            OpenPositionRecord positionObj = openPositionListItem.getPosition();
            String sTmp;
            Locale l = LocaleUtility.getLanguage(PreferenceManager.getDefaultSharedPreferences(context));

            TextView tvTmp = convertView.findViewById(R.id.lbContract);
            tvTmp.setText(positionObj.contract.getContractName(l));

            tvTmp = convertView.findViewById(R.id.lbTranDate);
            sTmp = Utility.displayListingDate(positionObj.strTradeDate);
            tvTmp.setText(sTmp);
            //lbBuy / lbSell
            if (CompanySettings.newinterface) {
                ImageView imgTmp = convertView.findViewById(R.id.img_bs_deal);
                if ("B".equals(positionObj.strBuySell)) {
                    imgTmp.setImageResource(R.drawable.ico_buy);
                } else {
                    imgTmp.setImageResource(R.drawable.ico_sell);
                }
            }

            if (!CompanySettings.SHOW_BS_COLUMN_ON_LISTVIEW) {
                if (CompanySettings.newinterface) {
                    tvTmp = convertView.findViewById(R.id.lbBuy);
                    ((TextView) convertView.findViewById(R.id.lbSell)).setText("");
                } else {
                    if ("B".equals(positionObj.strBuySell)) {
                        tvTmp = convertView.findViewById(R.id.lbBuy);
                        ((TextView) convertView.findViewById(R.id.lbSell)).setText("");
                    } else {
                        tvTmp = convertView.findViewById(R.id.lbSell);
                        ((TextView) convertView.findViewById(R.id.lbBuy)).setText("");
                    }
                }
            } else {
                tvTmp = convertView.findViewById(R.id.lbSell);
                if ("B".equals(positionObj.strBuySell)) {
                    ((TextView) convertView.findViewById(R.id.lbBuy)).setText(Utility.getStringById("lb_b", context.getResources()));
                } else {
                    ((TextView) convertView.findViewById(R.id.lbBuy)).setText(Utility.getStringById("lb_s", context.getResources()));
                }
            }
            String amountText = Utility.formatLot(positionObj.dAmount / positionObj.contract.iContractSize);
            StringBuilder amountSB = new StringBuilder();
            if (openPositionListItem.isShowAmountSelection()) {
                amountSB.append(Utility.formatLot(openPositionListItem.getAmountSelected() / positionObj.contract.iContractSize))
                        .append("/")
                        .append(amountText);
            } else {
                amountSB.append(amountText);
            }
            tvTmp.setText(amountSB);

            if (convertView.findViewById(R.id.lbBuyRate) != null) {
                tvTmp = convertView.findViewById(R.id.lbBuyRate);
                tvTmp.setText(Utility.round(positionObj.dDealRate, positionObj.contract.iRateDecPt, positionObj.contract.iRateDecPt));
            }

            if (convertView.findViewById(Utility.getIdById("lbPrice")) != null) {
                tvTmp = convertView.findViewById(Utility.getIdById("lbPrice"));
                tvTmp.setText(positionObj.strDealRate);
            }

            if (convertView.findViewById(R.id.lbDealPrice) != null) {
                tvTmp = convertView.findViewById(R.id.lbDealPrice);
                tvTmp.setText(Utility.round(positionObj.strDealRate, positionObj.contract.iRateDecPt, positionObj.contract.iRateDecPt));
            }

            TextView lbCurPrice = convertView.findViewById(R.id.lbCurPrice);

            double[] dBidAsk = positionObj.contract.getBidAsk();
            if (openPositionListItem.isShowCurrentPrice()) {
                if (("B".equals(positionObj.strBuySell) && positionObj.contract.getBSD() == false) || ("S".equals(positionObj.strBuySell) && positionObj.contract.getBSD() == true)) {
                    sTmp = Utility.round(dBidAsk[0], positionObj.contract.iRateDecPt, positionObj.contract.iRateDecPt);
                } else {
                    sTmp = Utility.round(dBidAsk[1], positionObj.contract.iRateDecPt, positionObj.contract.iRateDecPt);
                }
                lbCurPrice.setText(sTmp);
                lbCurPrice.setVisibility(View.VISIBLE);
            } else {
                lbCurPrice.setVisibility(View.GONE);
            }


            if (positionObj.contract.bChangeBidAsk) {
                ColorController.setPriceColor(context.getResources(), positionObj.contract.iBidUpDown, lbCurPrice, R.color.list_level_2_text);
            } else {
                ColorController.setPriceColor(context.getResources(), 0, lbCurPrice, R.color.list_level_2_text);
            }

            TextView lbFloat = convertView.findViewById(R.id.lbFloat);
            double dFloating;
            if (openPositionListItem.isShowAmountSelection()) {
                dFloating = positionObj.dAmount > 0 ? positionObj.dFloating * openPositionListItem.getAmountSelected() / positionObj.dAmount : 0;
            } else {
                dFloating = positionObj.dFloating;
            }
            lbFloat.setText(Utility.formatValue(dFloating));

            ColorController.setNumberColor(context.getResources(), dFloating >= 0, lbFloat);

            if (CompanySettings.newinterface) {
                TextView lbExePrice_deal = convertView.findViewById(R.id.lbExePrice_deal);
                if (openPositionListItem.isShowCurrentPrice()) {
                    lbExePrice_deal.setText(Utility.round(positionObj.dDealRate, positionObj.contract.iRateDecPt, positionObj.contract.iRateDecPt) + "/");
                    lbExePrice_deal.setGravity(Gravity.BOTTOM);
                } else {
                    lbExePrice_deal.setText(Utility.round(positionObj.dDealRate, positionObj.contract.iRateDecPt, positionObj.contract.iRateDecPt));
                    lbExePrice_deal.setGravity(Gravity.CENTER);
                }

            }

            View vItem = convertView.findViewById(R.id.llPosition);
            vItem.setTag(positionObj.iRef);
            convertView.setTag(positionObj.iRef);

            if (!hmSelect.containsKey(positionObj.iRef)) {
                hmSelect.put(positionObj.iRef, false);
            }

            if (CompanySettings.newinterface) {
                ImageView imgChecked = convertView.findViewById(R.id.img_checked);
                if (isInSelectionMode()) {
                    if (hmSelect.getOrDefault(positionObj.iRef, false)) {
                        imgChecked.setImageDrawable(checkedDraw);
                    } else {
                        imgChecked.setImageDrawable(uncheckedDraw);
                    }
                }
                imgChecked.setVisibility(isInSelectionMode() ? View.VISIBLE : View.GONE);
            }
        } else {
            convertView = m_inflater.inflate(R.layout.list_item_open_position, null);
        }
        return convertView;
    }

    public void reload(List<OpenPositionListItem> positions) {
        try {
            this.positions = positions;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSelectionMode(boolean bSelectionMode) {
        this.bSelectionMode = bSelectionMode;
    }

    public void applySelectionToAll(boolean bSelect) {
        for (OpenPositionListItem r : positions) {
            hmSelect.put(r.getPosition().iRef, bSelect);
        }
        notifyDataSetChanged();
    }

    public boolean isInSelectionMode() {
        return bSelectionMode;
    }

    public HashMap<Integer, Boolean> getSelected() {
        return hmSelect;
    }

    public List<OpenPositionRecord> liquidateSelected() {
        ArrayList<OpenPositionRecord> alLiqList = new ArrayList<>();
        Iterator<Integer> itKey = hmSelect.keySet().iterator();
        for (OpenPositionListItem r : positions) {
            int iKey = itKey.next();
            if (hmSelect.containsKey(r.getPosition().iRef) && hmSelect.get(r.getPosition().iRef) ) {
                OpenPositionRecord position = app.data.getOpenPosition(r.getPosition().iRef);
                if (position != null) {
                    alLiqList.add(position);
                }
            }
        }
        return alLiqList;
        //return !alLiqList.isEmpty();
    }

}
