package com.mfinance.everjoy.everjoy.ui.mine;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.PhoneUtils;
import com.mfinance.everjoy.R;
import com.mfinance.everjoy.everjoy.base.BaseViewActivity;
import com.mfinance.everjoy.everjoy.ui.mine.securities.FinanceActivity;
import com.mfinance.everjoy.everjoy.utils.ToolsUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 在线咨询
 */
public class ContactActivity extends BaseViewActivity {

    public static void startContactActivity(Activity activity) {
        activity.startActivity(new Intent(activity, ContactActivity.class));
    }

    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_whatsapp)
    TextView tvWhatsapp;
    @BindView(R.id.tv_wechat)
    TextView tvWechat;

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_contact;
    }

    @Override
    protected void initView(View currentView) {
        setTitleCenter(R.string.str_appointment_consultation);
    }

    @OnClick({R.id.rl_phone, R.id.rl_whatsapp, R.id.rl_wechat})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_phone:
                String phone = tvPhone.getText().toString();
                if (!TextUtils.isEmpty(phone)) {
                    PhoneUtils.dial(phone);
                }
                break;
            case R.id.rl_whatsapp:
                String whatsapp = tvWhatsapp.getText().toString();
                if (!TextUtils.isEmpty(whatsapp)) {
                    ToolsUtils.copyText(this, whatsapp);
                }
                break;
            case R.id.rl_wechat:
                String wechat = tvWechat.getText().toString();
                if (!TextUtils.isEmpty(wechat)) {
                    ToolsUtils.copyText(this, wechat);
                }
                break;
            default:
                break;
        }
    }
}
