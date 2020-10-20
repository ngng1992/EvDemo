package com.mfinance.everjoy.everjoy.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mfinance.everjoy.R;
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

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public MineFragment() {

    }

    public static MineFragment newInstance(String param1, String param2) {
        MineFragment fragment = new MineFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int setLayoutResId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView(View currentView) {

    }

    @OnClick({R.id.iv_setting, R.id.tv_login, R.id.tv_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_setting:
                Intent intent = new Intent(getContext(), ContactActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_login:
                intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_register:
                break;
            default:
                break;
        }
    }
}
