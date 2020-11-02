package com.mfinance.everjoy.everjoy.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.mfinance.everjoy.R;

/**
 * 开通证券账户填写资料的步骤
 */
public class AccountStepView extends LinearLayout {

    private LinearLayout ll_edit_info;
    private LinearLayout ll_open_account;
    private LinearLayout ll_risk_disclosure;
    private LinearLayout ll_signing_agreement;

    public AccountStepView(Context context) {
        super(context);
        init(context, null);
    }

    public AccountStepView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AccountStepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public AccountStepView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AccountStepView);
        int selectIndex = a.getInt(R.styleable.AccountStepView_selectIndex, 0);
        a.recycle();

        LayoutInflater.from(context).inflate(R.layout.view_account_step, this);
        ll_edit_info = findViewById(R.id.ll_edit_info);
        ll_open_account = findViewById(R.id.ll_open_account);
        ll_risk_disclosure = findViewById(R.id.ll_risk_disclosure);
        ll_signing_agreement = findViewById(R.id.ll_signing_agreement);
        scrollStep(selectIndex);
    }

    /**
     * 滚动到第几个位置了，从0开始
     */
    public void scrollStep(int selectIndex) {
        if (selectIndex == 0) {
            int childCount0 = ll_edit_info.getChildCount();
            for (int i = 0; i < childCount0; i++) {
                View childAt = ll_edit_info.getChildAt(i);
                childAt.setVisibility(VISIBLE);
                childAt.setAlpha(1f);
            }

            // 保留1个，并透明度0.5
            int childCount1 = ll_open_account.getChildCount();
            for (int i = 0; i < childCount1; i++) {
                View childAt = ll_open_account.getChildAt(i);
                childAt.setVisibility(i == 0 || i == 1 ? VISIBLE : GONE);
                childAt.setAlpha(0.5f);
            }

            int childCount2 = ll_risk_disclosure.getChildCount();
            for (int i = 0; i < childCount2; i++) {
                View childAt = ll_risk_disclosure.getChildAt(i);
                childAt.setVisibility(i == 0 || i == 1 ? VISIBLE : GONE);
                childAt.setAlpha(0.5f);
            }

            int childCount3 = ll_signing_agreement.getChildCount();
            for (int i = 0; i < childCount3; i++) {
                View childAt = ll_signing_agreement.getChildAt(i);
                childAt.setVisibility(i == 0 ? VISIBLE : GONE);
                childAt.setAlpha(0.5f);
            }
        } else if (selectIndex == 1) {
            int childCount0 = ll_edit_info.getChildCount();
            for (int i = 0; i < childCount0; i++) {
                View childAt = ll_edit_info.getChildAt(i);
                childAt.setVisibility(i == 0 || i == 1 ? VISIBLE : GONE);
                childAt.setAlpha(0.5f);
            }

            int childCount1 = ll_open_account.getChildCount();
            for (int i = 0; i < childCount1; i++) {
                View childAt = ll_open_account.getChildAt(i);
                childAt.setVisibility(VISIBLE);
                childAt.setAlpha(1f);
            }

            int childCount2 = ll_risk_disclosure.getChildCount();
            for (int i = 0; i < childCount2; i++) {
                View childAt = ll_risk_disclosure.getChildAt(i);
                childAt.setVisibility(i == 0 || i == 1 ? VISIBLE : GONE);
                childAt.setAlpha(0.5f);
            }

            int childCount3 = ll_signing_agreement.getChildCount();
            for (int i = 0; i < childCount3; i++) {
                View childAt = ll_signing_agreement.getChildAt(i);
                childAt.setVisibility(i == 0 ? VISIBLE : GONE);
                childAt.setAlpha(0.5f);
            }
        } else if (selectIndex == 2) {
            int childCount0 = ll_edit_info.getChildCount();
            for (int i = 0; i < childCount0; i++) {
                View childAt = ll_edit_info.getChildAt(i);
                childAt.setVisibility(i == 0 || i == 1 ? VISIBLE : GONE);
                childAt.setAlpha(0.5f);
            }

            int childCount1 = ll_open_account.getChildCount();
            for (int i = 0; i < childCount1; i++) {
                View childAt = ll_open_account.getChildAt(i);
                childAt.setVisibility(i == 0 || i == 1 ? VISIBLE : GONE);
                childAt.setAlpha(0.5f);
            }

            int childCount2 = ll_risk_disclosure.getChildCount();
            for (int i = 0; i < childCount2; i++) {
                View childAt = ll_risk_disclosure.getChildAt(i);
                childAt.setVisibility(VISIBLE);
                childAt.setAlpha(1f);
            }

            int childCount3 = ll_signing_agreement.getChildCount();
            for (int i = 0; i < childCount3; i++) {
                View childAt = ll_signing_agreement.getChildAt(i);
                childAt.setVisibility(i == 0 ? VISIBLE : GONE);
                childAt.setAlpha(0.5f);
            }
        } else {
            int childCount0 = ll_edit_info.getChildCount();
            for (int i = 0; i < childCount0; i++) {
                View childAt = ll_edit_info.getChildAt(i);
                childAt.setVisibility(i == 0 || i == 1 ? VISIBLE : GONE);
                childAt.setAlpha(0.5f);
            }

            int childCount1 = ll_open_account.getChildCount();
            for (int i = 0; i < childCount1; i++) {
                View childAt = ll_open_account.getChildAt(i);
                childAt.setVisibility(i == 0 || i == 1 ? VISIBLE : GONE);
                childAt.setAlpha(0.5f);
            }

            int childCount2 = ll_risk_disclosure.getChildCount();
            for (int i = 0; i < childCount2; i++) {
                View childAt = ll_risk_disclosure.getChildAt(i);
                childAt.setVisibility(i == 0 || i == 1 ? VISIBLE : GONE);
                childAt.setAlpha(0.5f);
            }

            int childCount3 = ll_signing_agreement.getChildCount();
            for (int i = 0; i < childCount3; i++) {
                View childAt = ll_signing_agreement.getChildAt(i);
                childAt.setVisibility(VISIBLE);
                childAt.setAlpha(1f);
            }
        }
    }

}
