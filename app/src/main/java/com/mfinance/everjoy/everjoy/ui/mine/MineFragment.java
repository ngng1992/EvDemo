package com.mfinance.everjoy.everjoy.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.MobileTraderApplication;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.everjoy.base.BaseViewFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的界面
 */
public class MineFragment extends BaseViewFragment {

    @BindView(R.id.ll_no_login)
    LinearLayout ll_no_login;
    @BindView(R.id.iv_mine_msg)
    ImageView ivMineMsg;
    @BindView(R.id.iv_setting)
    ImageView ivSetting;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.tv_register)
    TextView tvRegister;
    @BindView(R.id.ll_login)
    LinearLayout llLogin;

    @BindView(R.id.tv_logout)
    TextView tvLogout;
    @BindView(R.id.tv_reset)
    TextView tvReset;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private static MobileTraderApplication iApp;
    private static Messenger iService;
    private static Messenger iServiceMessage;

    public MineFragment() {

    }

    public static MineFragment newInstance(MobileTraderApplication app, Messenger service, Messenger serviceMessage, String param1, String param2) {
        MineFragment fragment = new MineFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        iApp = app;
        iService = service;
        iServiceMessage = serviceMessage;
        return fragment;
    }

    @Override
    protected int setLayoutResId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView(View currentView) {
        if (!iApp.bLogon){ //Level 1 display
            tvReset.setVisibility(View.GONE);
            tvRegister.setVisibility(View.VISIBLE);
            tvLogin.setVisibility(View.VISIBLE);
            tvLogout.setVisibility(View.GONE);
        }
        else if (iApp.bLogon && !iApp.bSecurityLogon) { //Level 2 display
            if (iApp.getOpenID() == null || (iApp.getOpenID() != null && iApp.getOpenID().equals("null")))
                tvReset.setVisibility(View.VISIBLE);
            else
                tvReset.setVisibility(View.GONE);
            tvRegister.setVisibility(View.VISIBLE);
            tvLogin.setVisibility(View.VISIBLE);
            tvLogout.setVisibility(View.VISIBLE);
        }
        if (iApp.bLogon && iApp.bSecurityLogon) { //Level 3 display
            tvRegister.setVisibility(View.GONE);
            tvLogin.setVisibility(View.GONE);
            tvReset.setVisibility(View.VISIBLE);
            tvLogout.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.iv_setting, R.id.tv_login, R.id.tv_register, R.id.tv_reset, R.id.tv_logout})
    public void onViewClicked(View view) {
        String type = "1";
        if (iApp.bLogon && !iApp.bSecurityLogon)
            type = "2";
        else if (iApp.bLogon && iApp.bSecurityLogon)
            type = "3";
        switch (view.getId()) {
            case R.id.iv_setting:
                Intent intent = new Intent(getContext(), ContactActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_login:
                if(type.equals("1")) { // Move to level 2 login
                    intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                }else if (type.equals("2")) { //Move to level 3 login
                    intent = new Intent(getContext(), LoginSecurityActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.tv_register:
                break;
            case R.id.tv_logout:
                if (type.equals("2"))
                    logout();
                if (type.equals("3"))
                    logoutSecurity();
                break;
            case R.id.tv_reset:
                if (!type.isEmpty()) {
                    Bundle data = new Bundle();
                    data.putString(ServiceFunction.RESETPASSWORD_TYPE, type); //Reset login type level "2"/"3"
                    intent = new Intent(getContext(), ResetPwdActivity.class);
                    intent.putExtras(data);
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }

    public void logout() {
        Message loginMsg = Message.obtain(null, ServiceFunction.SRV_LOGOUT);
        loginMsg.replyTo = iServiceMessage;
        try {
            iService.send(loginMsg);
        } catch (RemoteException e) {
            Log.e("login", "Unable to send logout message", e.fillInStackTrace());
        }
    }

    public void logoutSecurity() {
        Message loginMsg = Message.obtain(null, ServiceFunction.SRV_LOGOUT_SECURITY);
        loginMsg.replyTo = iServiceMessage;
        try {
            iService.send(loginMsg);
        } catch (RemoteException e) {
            Log.e("login", "Unable to send logout message", e.fillInStackTrace());
        }
    }
}
