package com.mfinance.everjoy.app;

import android.os.Message;
import com.mfinance.everjoy.R;

public class TermsNConditionActivity extends BaseActivity {
    @Override
    public void bindEvent() {

    }

    @Override
    public void handleByChild(Message msg) {

    }

    @Override
    public boolean isTopBarExist() {
        return true;
    }

    @Override
    public boolean isBottonBarExist() {
        return false;
    }

    @Override
    public void loadLayout() {
        setContentView(R.layout.v_terms);

    }

    @Override
    public void updateUI() {

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
        return false;
    }

    @Override
    public boolean showPlatformType() {
        return false;
    }
}
