package com.mfinance.everjoy.app;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Messenger;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.presenter.CancelledOrderDetailPresenter;
import com.mfinance.everjoy.app.widget.quickaction.CustomPopupWindow;

public class PopupCancelledOrderDetail implements PopupCancelledOrderDetailListener {

    CancelledOrderDetailPresenter presenter;
    Context context;
    public CustomPopupWindow popup;

    public PopupCancelledOrderDetail(Context context, View vParent, MobileTraderApplication app, Messenger mService, Messenger mServiceMessengerHandler) {
        this.context = context;
        presenter = new CancelledOrderDetailPresenter(this, context.getResources(), app, mService, mServiceMessengerHandler);
        popup = new CustomPopupWindow(vParent) {
            @Override
            protected void preShow() {
                window.setBackgroundDrawable(new BitmapDrawable());
                window.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
                window.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
                window.setTouchable(true);
                window.setFocusable(true);
                window.setOutsideTouchable(true);
            }
        };
        if (CompanySettings.newinterface)
            popup.setContentView(R.layout.v_cancelled_order_detail_new);
        else
            popup.setContentView(R.layout.v_cancelled_order_detail);

    }

    public void showLikeQuickAction() {
        popup.showLikeQuickAction();
        presenter.bindEvent();
        presenter.updateUI();
    }

    public void updateUI() {
        presenter.updateUI();
    }

    @Override
    public void dismiss() {
        popup.dismiss();
    }

    @Override
    public TextView getRefField() {
        return (TextView) popup.findViewById(R.id.tvRef);
    }

    @Override
    public TextView getLotField() {
        return (TextView) popup.findViewById(R.id.tvLot);
    }

    @Override
    public TextView getContractField() {
        return (TextView) popup.findViewById(R.id.tvContract);
    }

    @Override
    public TextView getAmountField() {
        return (TextView) popup.findViewById(R.id.tvAmount);
    }

    @Override
    public TextView getBuySellField() {
        return (TextView) popup.findViewById(R.id.tvBuySell);
    }

    @Override
    public Button getCloseBtn() {
        return (Button) popup.findViewById(R.id.btnClose);
    }

    @Override
    public TextView getTPriceField() {
        return (TextView) popup.findViewById(R.id.tvTPrice);
    }

    @Override
    public TextView getLimitStopField() {
        return (TextView) popup.findViewById(R.id.tvLimitStop);
    }

    @Override
    public TextView getOCOField() {
        return (TextView) popup.findViewById(R.id.tvOCO);
    }

    @Override
    public TextView getCancelDateField() {
        return (TextView) popup.findViewById(R.id.tvCancelDate);
    }

    @Override
    public TextView getLiqField() {
        return (TextView) popup.findViewById(R.id.tvLiquid);
    }

    @Override
    public TextView getGoodTillField() {
        return (TextView) popup.findViewById(R.id.tvGdTill);
    }

}
