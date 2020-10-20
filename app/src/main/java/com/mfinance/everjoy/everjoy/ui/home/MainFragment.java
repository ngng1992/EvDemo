package com.mfinance.everjoy.everjoy.ui.home;

import android.os.Bundle;
import android.view.View;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.everjoy.base.BaseViewFragment;

/**
 * 首页，股票价格
 */
public class MainFragment extends BaseViewFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public MainFragment() {

    }

    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    protected int setLayoutResId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initView(View currentView) {

    }
}
