package com.mfinance.everjoy.everjoy.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.everjoy.dialog.base.BaseDialog;

/**
 * 正在加载
 */
public class LoadingDialog extends BaseDialog {

    private String message;
    private TextView tvContent;

    public LoadingDialog(Context context) {
       this(context, "");
    }

    public LoadingDialog(Context context, String message) {
        super(context, STYLE_BRIGHT);
        this.message = message;
    }

    public LoadingDialog(Context context, int message) {
        this(context, context.getString(message));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.include_loading);
        tvContent = findViewById(R.id.tv_loading_msg);
        tvContent.setText(TextUtils.isEmpty(message) ? context.getString(R.string.str_loading) : message);
    }

    /**
     * 设置标题
     */
    public void setMessage(String message) {
        this.message = message;
        tvContent.setText(this.message);
    }

}
