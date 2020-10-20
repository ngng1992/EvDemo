package com.mfinance.everjoy.everjoy.ui.news;

import android.os.Bundle;
import android.view.View;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.everjoy.base.BaseViewFragment;

/**
 * 新闻
 */
public class NewsFragment extends BaseViewFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public NewsFragment() {

    }

    public static NewsFragment newInstance(String param1, String param2) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int setLayoutResId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initView(View currentView) {

    }
}
