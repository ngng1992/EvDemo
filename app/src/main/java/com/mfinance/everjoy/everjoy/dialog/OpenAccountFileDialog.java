package com.mfinance.everjoy.everjoy.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.everjoy.dialog.base.BaseBottomDialog;

/**
 * 开户文件
 */
public class OpenAccountFileDialog extends BaseBottomDialog {


    public OpenAccountFileDialog(@NonNull Context context) {
        super(context);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        ImageView ivClose = findViewById(R.id.iv_close);
        TextView tv_file1 = findViewById(R.id.tv_file1);
        TextView tv_file2 = findViewById(R.id.tv_file2);
        TextView tv_file3 = findViewById(R.id.tv_file3);
        TextView tv_file4 = findViewById(R.id.tv_file4);
        TextView tv_file5 = findViewById(R.id.tv_file5);
        TextView tv_file6 = findViewById(R.id.tv_file6);
        TextView tv_file7 = findViewById(R.id.tv_file7);


        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        tv_file1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                // 查看相关文件
            }
        });
        tv_file2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

            }
        });
        tv_file3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

            }
        });
        tv_file4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

            }
        });
        tv_file5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

            }
        });
        tv_file6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

            }
        });
        tv_file7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

            }
        });

    }

    @Override
    protected int setLayout() {
        return R.layout.dialog_open_account_file;
    }
}
