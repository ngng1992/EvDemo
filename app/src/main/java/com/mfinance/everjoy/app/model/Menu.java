package com.mfinance.everjoy.app.model;

import android.os.Bundle;
import android.view.View;

import com.mfinance.everjoy.app.constant.ServiceFunction;

import java.util.Collections;
import java.util.List;

public class Menu {
    private final View menuid;
    private final int serviceCode;
    private final String activity;
    private final List<Menu> subMenu;
    private final boolean showAfterLogin;
    private final boolean isContentMode;
    private final Bundle bundle;

    public Menu(View menuid, int serviceCode, String activity, List<Menu> submenu, boolean showAfterLogin, boolean isContentMode) {
        this(menuid, serviceCode, activity, submenu, showAfterLogin, isContentMode, new Bundle());
    }

    public Menu(View menuid, int serviceCode, String activity, List<Menu> submenu, boolean showAfterLogin, boolean isContentMode, Bundle bundle) {
        this.menuid = menuid;
        this.serviceCode = serviceCode;
        this.activity = activity;
        this.subMenu = submenu != null ? Collections.unmodifiableList(submenu) : Collections.EMPTY_LIST;
        this.showAfterLogin = showAfterLogin;
        this.isContentMode = isContentMode;
        this.bundle = bundle;
        this.bundle.putBoolean(ServiceFunction.REQUIRE_LOGIN, showAfterLogin);
    }

    public View getMenuid() {
        return menuid;
    }

    public int getServiceCode() {
        return serviceCode;
    }

    public String getActivity() {
        return activity;
    }

    public List<Menu> getSubMenu() {
        return subMenu;
    }

    public boolean isShowAfterLogin() {
        return showAfterLogin;
    }

    public boolean isContentMode() {
        return isContentMode;
    }

    public Bundle getBundle() {
        return bundle;
    }
}
