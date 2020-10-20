package com.mfinance.everjoy.everjoy.dialog;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.everjoy.dialog.base.BaseBottomDialog;

/**
 * 选择称谓
 */
public class SelectNikenameDialog extends BaseBottomDialog {

    private int selectIndex = 0;

    public SelectNikenameDialog(@NonNull Context context, int selectIndex) {
        super(context);
        this.selectIndex = selectIndex;
    }

    @Override
    protected void initView() {
        RelativeLayout rl_ms = findViewById(R.id.rl_ms);
        TextView tvMr = findViewById(R.id.tv_mr);
        ImageView iv_mr = findViewById(R.id.iv_mr);

        RelativeLayout rl_mrs = findViewById(R.id.rl_mrs);
        TextView tv_mrs = findViewById(R.id.tv_mrs);
        ImageView iv_mrs = findViewById(R.id.iv_mrs);

        RelativeLayout rl_miss = findViewById(R.id.rl_miss);
        TextView tv_miss = findViewById(R.id.tv_miss);
        ImageView iv_miss = findViewById(R.id.iv_miss);

        iv_mr.setVisibility(View.GONE);
        iv_mrs.setVisibility(View.GONE);
        iv_miss.setVisibility(View.GONE);

        if (selectIndex == 0) {
            tvMr.setSelected(true);
//            iv_mr.setVisibility(View.VISIBLE);
            tv_mrs.setSelected(false);
//            iv_mrs.setVisibility(View.GONE);
            tv_miss.setSelected(false);
//            iv_miss.setVisibility(View.GONE);
        } else if (selectIndex == 1) {
            tvMr.setSelected(false);
//            iv_mr.setVisibility(View.GONE);
            tv_mrs.setSelected(true);
//            iv_mrs.setVisibility(View.VISIBLE);
            tv_miss.setSelected(false);
//            iv_miss.setVisibility(View.GONE);
        } else {
            tvMr.setSelected(false);
//            iv_mr.setVisibility(View.GONE);
//            iv_mrs.setVisibility(View.GONE);
//            iv_miss.setVisibility(View.VISIBLE);
            tv_mrs.setSelected(false);
            tv_miss.setSelected(true);
        }
        rl_ms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onClickDialogOrFragmentViewListener != null) {
                    onClickDialogOrFragmentViewListener.onClickView(tvMr, 0);
                }
            }
        });
        rl_mrs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onClickDialogOrFragmentViewListener != null) {
                    onClickDialogOrFragmentViewListener.onClickView(tvMr, 1);
                }
            }
        });
        rl_miss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onClickDialogOrFragmentViewListener != null) {
                    onClickDialogOrFragmentViewListener.onClickView(tvMr, 2);
                }
            }
        });


    }

    @Override
    protected int setLayout() {
        return R.layout.dialog_select_nikename;
    }
}
