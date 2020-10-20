package com.mfinance.everjoy.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.util.ColorController;
import com.mfinance.everjoy.app.util.GUIUtility;
import com.mfinance.everjoy.app.util.Utility;
import com.mfinance.everjoy.app.util.function.Consumer;

import java.util.Collections;
import java.util.List;

public class RunningOrderListAdapter extends BaseAdapter {
    private class ViewHolder {
        TextView lbContract;
        TextView lbTranDate;
        ImageView ivBuy;
        TextView lbBuy;
        ImageView ivSell;
        TextView lbLimit;
        TextView lbStop;
        TextView lbReqPrice;
        TextView lbCurPrice;
        ImageView ivOco;
        ImageView ivLiq;
        View lselect;
        ImageView ivSelect;
    }
    private final String TAG = this.getClass().getSimpleName();
    private List<RunningOrderItem> items = Collections.EMPTY_LIST;
    private final Context context;
    private final LayoutInflater m_inflater;
    private final Consumer<Integer> onClickItem;
    private final Drawable checkedDraw;
    private final Drawable uncheckedDraw;
    private final Drawable buyIcon;
    private final Drawable sellIcon;

    public RunningOrderListAdapter(Context context, Consumer<Integer> onClickItem) {
        this.context = context;
        this.m_inflater = LayoutInflater.from(context);
        this.onClickItem = onClickItem;
        Drawable drawable = context.getDrawable(R.drawable.icon_checkedbox);
        if (drawable instanceof BitmapDrawable) {
            checkedDraw = new BitmapDrawable(context.getResources(), Bitmap.createScaledBitmap(((BitmapDrawable) drawable).getBitmap(), 50, 50, true));
        } else {
            checkedDraw = drawable;
        }
        Drawable drawable1 = context.getDrawable(R.drawable.icon_checkbox);
        if (drawable1 instanceof BitmapDrawable) {
            uncheckedDraw = new BitmapDrawable(context.getResources(), Bitmap.createScaledBitmap(((BitmapDrawable)  drawable1).getBitmap(), 50, 50, true));
        } else {
            uncheckedDraw = drawable1;
        }
        buyIcon = context.getDrawable(R.drawable.ico_buy);
        sellIcon = context.getDrawable(R.drawable.ico_sell);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).iRef;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RunningOrderItem item, oldItem;
        try {
            item = items.get(position);
        } catch (Exception ex) {
            return convertView;
        }
        if (convertView == null) {
            if (CompanySettings.newinterface) {
                convertView = m_inflater.inflate(R.layout.list_item_running_order_new, null);
            } else {
                convertView = m_inflater.inflate(R.layout.list_item_running_order, null);
            }
            ViewHolder holder = new ViewHolder();
            holder.lbContract = convertView.findViewById(R.id.lbContract);
            holder.lbTranDate = convertView.findViewById(R.id.lbTranDate);
            holder.lbTranDate = convertView.findViewById(R.id.lbTranDate);
            holder.ivBuy = convertView.findViewById(R.id.ivBuy);
            holder.lbBuy = convertView.findViewById(R.id.lbBuy);
            holder.ivSell = convertView.findViewById(R.id.ivSell);
            holder.lbLimit = convertView.findViewById(R.id.lbLimit);
            holder.lbStop = convertView.findViewById(R.id.lbStop);
            holder.lbReqPrice = convertView.findViewById(R.id.lbReqPrice);
            holder.lbCurPrice = convertView.findViewById(R.id.lbCurPrice);
            holder.ivOco = convertView.findViewById(R.id.ivOco);
            holder.ivLiq = convertView.findViewById(R.id.ivLiq);
            holder.lselect = convertView.findViewById(R.id.lSelect);
            holder.ivSelect = convertView.findViewById(R.id.ivSelect);
            convertView.setTag(CompanySettings.newinterface ? R.layout.list_item_running_order_new : R.layout.list_item_running_order, holder);

            View vItem = convertView.findViewById(R.id.llOrder);
            if (position % 2 != 0) {
                vItem.setBackgroundResource(R.drawable.list_row_odd);
            } else {
                vItem.setBackgroundResource(R.drawable.list_row_even);
            }
            convertView.setTag(new RunningOrderItemBuilder().createRunningOrderItem());
        }
        oldItem = (RunningOrderItem) convertView.getTag();
        convertView.setTag(item);
        convertView.setOnClickListener(v -> onClickItem.accept(position));
        ViewHolder holder = (ViewHolder) convertView.getTag(CompanySettings.newinterface ? R.layout.list_item_running_order_new : R.layout.list_item_running_order);

        if (CompanySettings.newinterface) {
            holder.lselect.setVisibility(item.showSelection ? View.VISIBLE : View.GONE);
            if (item.showSelection) {
                holder.ivSelect.setImageDrawable(item.selected ? checkedDraw : uncheckedDraw);
            }
        }

        if (item.showSelection) {
            holder.lbContract.setText(item.contractName);
        } else {
            holder.lbContract.setText(item.contractName);
        }
        
        holder.lbTranDate.setText(item.tradeDate);

        if (CompanySettings.newinterface) {
            if ("B".equals(item.strBuySell)) {
                if (holder.ivBuy != null) {
                    holder.ivBuy.setVisibility(View.VISIBLE);
                    if (!item.strBuySell.equals(oldItem.strBuySell)) {
                        holder.ivBuy.setImageDrawable(buyIcon);
                    }
                }
                if (holder.lbBuy != null) {
                    holder.lbBuy.setText(Utility.getStringById("lb_b", context.getResources()));
                }
            } else {
                if (holder.ivBuy != null) {
                    holder.ivBuy.setVisibility(View.VISIBLE);
                    if (!item.strBuySell.equals(oldItem.strBuySell)) {
                        holder.ivBuy.setImageDrawable(sellIcon);
                    }
                }

                if (holder.lbBuy != null) {
                    holder.lbBuy.setText(Utility.getStringById("lb_s", context.getResources()));
                }
            }
        } else {
            if ("B".equals(item.strBuySell)) {
                if (holder.ivBuy != null)
                    holder.ivBuy.setVisibility(View.VISIBLE);
                if (holder.ivSell != null)
                    holder.ivSell.setVisibility(View.INVISIBLE);
                if (holder.lbBuy != null)
                    holder.lbBuy.setText(Utility.getStringById("lb_b", context.getResources()));
            } else {
                if (holder.ivBuy != null)
                    holder.ivBuy.setVisibility(View.INVISIBLE);
                if (holder.ivSell != null)
                    holder.ivSell.setVisibility(View.VISIBLE);
                if (holder.lbBuy != null)
                    holder.lbBuy.setText(Utility.getStringById("lb_s", context.getResources()));
            }
        }

//lot store at Limit or Stop
        if (!CompanySettings.SHOW_ORDERTYPE_COLUMN_ON_LISTVIEW) {
            if (item.iLimitStop == 0) {
                holder.lbLimit.setText(item.lot);
                holder.lbStop.setText("");
            } else {
                holder.lbStop.setText("");
                holder.lbLimit.setText(item.lot);
            }
        } else {
            holder.lbLimit.setText(item.lot);
        }
        holder.lbReqPrice.setText(item.requestPrice);
        
        holder.lbCurPrice.setText(item.curPrice);

        if (item.bChangeBidAsk) {
            ColorController.setPriceColor(context.getResources(), item.iBidUpDown, holder.lbCurPrice, R.color.list_level_2_text);
        } else {
            ColorController.setPriceColor(context.getResources(), 0, holder.lbCurPrice, R.color.list_level_2_text);
        }

        holder.ivOco.setVisibility(item.iOCORef > 0 ? View.VISIBLE : View.INVISIBLE);
        if (holder.ivLiq != null)
            holder.ivLiq.setVisibility(item.isLiq ? View.VISIBLE : View.INVISIBLE);

        if (CompanySettings.SHOW_ORDERTYPE_COLUMN_ON_LISTVIEW) {
            if (item.isLiq) {
                if (item.iLimitStop == 0) {
                    holder.lbStop.setText(R.string.tb_limit);
                } else {
                    holder.lbStop.setText(R.string.tb_stop);
                }
            } else {
                holder.lbStop.setText(Utility.getStringById("tb_new", context.getResources()));
            }
        }

        return convertView;
    }

    public void reload(List<RunningOrderItem> items) {
        this.items = Collections.unmodifiableList(items);
    }
}
