package com.mfinance.everjoy.app;

import android.os.Bundle;
import android.os.Message;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.mfinance.everjoy.R;

import static com.mfinance.everjoy.app.constant.ServiceFunction.SRV_WEB_VIEW;
import com.mfinance.everjoy.R;
public class WebViewActivity extends BaseActivity {
    @Override
    public void bindEvent() {

    }

    @Override
    public void handleByChild(Message msg) {

    }

    @Override
    public void loadLayout() {
        setContentView(R.layout.v_webview);
        Bundle extras = getIntent().getExtras();
        TextView tvTitle = findViewById(R.id.tvTitle);
        WebView vwContent = findViewById(R.id.vwContent);
        tvTitle.setText(extras.getString("title"));
        vwContent.loadUrl(extras.getString("content"));
        WebSettings settings = vwContent.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
    }

    @Override
    public void updateUI() {

    }


    @Override
    public boolean isBottonBarExist() {
        return app.bLogon;
    }

    @Override
    public boolean isTopBarExist() {
        return true;
    }

    @Override
    public boolean showLogout() {
        return false;
    }

    @Override
    public boolean showTopNav() {
        return true;
    }

    @Override
    public boolean showConnected() {
        return app.bLogon;
    }

    @Override
    public boolean showPlatformType() {
        return true;
    }

    @Override
    public int getServiceId() {
        return SRV_WEB_VIEW;
    }

    @Override
    public int getActivityServiceCode() {
        return SRV_WEB_VIEW;
    }

}
