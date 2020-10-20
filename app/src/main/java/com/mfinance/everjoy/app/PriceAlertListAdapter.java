package com.mfinance.everjoy.app;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Messenger;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.mfinance.everjoy.app.bo.ContractObj;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.model.DataRepository;
import com.mfinance.everjoy.app.pojo.PriceAlertObject;
import com.mfinance.everjoy.app.util.ColorController;
import com.mfinance.everjoy.app.util.CommonFunction;
import com.mfinance.everjoy.app.util.Utility;
import com.mfinance.everjoy.app.util.function.Consumer;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import com.mfinance.everjoy.R;

public class PriceAlertListAdapter extends BaseAdapter {
    private class ViewHolder {
        ImageView btnEnable;
        TextView lbContract;
        TextView lbType;
        TextView lbPrice;
        TextView lbBid;
        TextView lbAsk;
        ImageView btnMenu;
    }
    private final String TAG = this.getClass().getSimpleName();
    private List<PriceAlertObject> items = Collections.EMPTY_LIST;
    private final Context context;
    private final LayoutInflater m_inflater;
    private final Consumer<Integer> onClickItem;
    private final Drawable checkedDraw;
    private final Drawable uncheckedDraw;
    private HashMap<String, ContractObj> contractList;
    private Locale locale;
    private Messenger mService;
    private Messenger mServiceMessengerHandler;
    private Resources res;

    public PriceAlertListAdapter(Context context, Consumer<Integer> onClickItem, HashMap<String, ContractObj> contractList, Locale locale, Messenger mService, Messenger mServiceMessengerHandler, Resources res) {
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
        this.contractList = contractList;
        this.locale = locale;
        this.mService = mService;
        this.mServiceMessengerHandler = mServiceMessengerHandler;
        this.res = res;
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
        return items.get(position).getId();
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PriceAlertObject item, oldItem;
        try {
            item = items.get(position);
        } catch (Exception ex) {
            return convertView;
        }
        if (convertView == null) {
            convertView = m_inflater.inflate(R.layout.list_item_pricealert, null);
            ViewHolder holder = new ViewHolder();
            holder.btnEnable = convertView.findViewById(R.id.img_checked);
            holder.lbContract = convertView.findViewById(R.id.lbContract);
            holder.lbType = convertView.findViewById(R.id.lbType);
            holder.lbPrice = convertView.findViewById(R.id.lbPrice);
            holder.lbBid = convertView.findViewById(R.id.lbBid);
            holder.lbAsk = convertView.findViewById(R.id.lbAsk);
            holder.btnMenu = convertView.findViewById(R.id.btn_menu);
            convertView.setTag(R.layout.list_item_pricealert, holder);

            View vItem = convertView.findViewById(R.id.llHistory);
            if (position % 2 != 0) {
                vItem.setBackgroundResource(R.drawable.list_row_odd);
            } else {
                vItem.setBackgroundResource(R.drawable.list_row_even);
            }
            convertView.setTag(new RunningOrderItemBuilder().createRunningOrderItem());
        }
        convertView.setTag(item);
        convertView.setOnClickListener(v -> onClickItem.accept(position));
        PriceAlertListAdapter.ViewHolder holder = (PriceAlertListAdapter.ViewHolder) convertView.getTag(R.layout.list_item_pricealert);

        ContractObj contract = contractList.get(item.getMkt());
        if (contract != null) {
            holder.lbContract.setText(contract.getContractName(locale));
            holder.lbType.setText(Utility.getStringById("lb_pricealert_type" + Integer.toString(item.getType()), context.getResources()));
            String sTmp = Utility.round(item.getAlertPrice(), contract.iRateDecPt, contract.iRateDecPt);
            holder.lbPrice.setText(sTmp);
            String alertPrice = sTmp;
            sTmp = Utility.round(contract.getBidAsk()[0], contract.iRateDecPt, contract.iRateDecPt);
            holder.lbBid.setText(sTmp);
            sTmp = Utility.round(contract.getBidAsk()[1], contract.iRateDecPt, contract.iRateDecPt);
            holder.lbAsk.setText(sTmp);

            if (item.getActive() == 1 && item.getExpired() == 0)
                holder.btnEnable.setImageResource(R.drawable.icon_checkedbox);
            else
                holder.btnEnable.setImageResource(R.drawable.icon_checkbox);

            holder.btnEnable.setOnClickListener(v1 -> {
                DataRepository.getInstance().setPriceAlertUpdateEnable(true);
                String mode = "1";
                String strMkt = item.getMkt();
                String type = Integer.toString(item.getType());
                String goodtill = Integer.toString(item.getGoodTill());
                String alertvolatility = Integer.toString(item.getAlertVolatility());
                String sId = Integer.toString(item.getId());
                String active = (item.getActive() == 0 || item.getExpired() == 1) ? "1" : "0";
                CommonFunction.sendPriceAlertRequest(this.mService, this.mServiceMessengerHandler, mode, strMkt, type, goodtill, alertPrice, alertvolatility, sId, active);
            });

            holder.btnMenu.setOnClickListener(v -> {
                PopupMenu popup = new PopupMenu(this.context, v);
                popup.setOnMenuItemClickListener((item2) -> {
                    switch (item2.getItemId()) {
                        case R.id.menuItemEdit:
                            Bundle data = new Bundle();
                            data.putString(ServiceFunction.PRICE_ALERT_REF, Integer.toString(item.getId()));
                            CommonFunction.moveTo(mService, mServiceMessengerHandler, ServiceFunction.SRV_NEW_PRICE_ALERT, data);
                            break;
                        case R.id.menuItemDelete:
                            AlertDialog dialog = new AlertDialog.Builder(this.context, CompanySettings.alertDialogTheme).create();
                            dialog.setMessage(res.getText(R.string.lb_pricealert_delete_confirm));
                            dialog.setButton(DialogInterface.BUTTON_POSITIVE, res.getText(R.string.yes),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            String mode = "2";
                                            String strMkt = item.getMkt();
                                            String sId = Integer.toString(item.getId());
                                            CommonFunction.sendPriceAlertRequest(mService, mServiceMessengerHandler, mode, strMkt, "", "", "", "", sId, "");
                                        }
                                    }
                            );

                            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, res.getText(R.string.no),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {}
                                    }
                            );
                            dialog.show();

                            break;
                        default:
                            break;
                    }
                    return true;
                });
                popup.inflate(R.menu.pricealert_menu);
                popup.show();
            });
        }

        return convertView;
    }

    public void reload(List<PriceAlertObject> items, HashMap<String, ContractObj> contractList) {
        this.items = Collections.unmodifiableList(items);
        this.contractList = contractList;
    }
}
