package com.mfinance.everjoy.everjoy.ui.mine.securities;

import android.view.View;

import com.blankj.utilcode.util.TimeUtils;
import com.mfinance.everjoy.R;
import com.mfinance.everjoy.everjoy.base.BaseViewActivity;
import com.mfinance.everjoy.everjoy.dialog.SelectBirthplaceDialog;
import com.mfinance.everjoy.everjoy.dialog.SelectDateDialog;
import com.mfinance.everjoy.everjoy.dialog.impl.OnClickDialogOrFragmentViewListener;
import com.mfinance.everjoy.everjoy.ui.home.MainActivity;
import com.mfinance.everjoy.everjoy.utils.DateFormatUtils;
import com.mfinance.everjoy.everjoy.view.AccountEditorInfoView;

import java.util.Arrays;
import java.util.Calendar;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 预约见证
 */
public class AppointmentWitnessActivity extends BaseViewActivity {

    @BindArray(R.array.array_time)
    String[] array_time;

    @BindView(R.id.ll_name)
    AccountEditorInfoView ll_name;
    @BindView(R.id.ll_email)
    AccountEditorInfoView ll_email;
    @BindView(R.id.ll_phone)
    AccountEditorInfoView ll_phone;
    @BindView(R.id.ll_date)
    AccountEditorInfoView ll_date;
    @BindView(R.id.ll_time)
    AccountEditorInfoView ll_time;

    @Override
    protected boolean isRemoveAppBar() {
        return true;
    }

    @Override
    protected boolean isFullStatusByView() {
        return true;
    }

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_appointment_witness;
    }

    @Override
    protected void initView(View currentView) {
        String editorContent = ll_name.getEditorContent();
        String emailEditorContent = ll_email.getEditorContent();
        String phoneEditorContent = ll_phone.getEditorContent();
        ll_date.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showDate();
            }
        });
        ll_time.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showTime();
            }
        });
    }

    private int selectTimeIndex = 0;

    private void showTime() {
        SelectBirthplaceDialog selectBirthplaceDialog = new SelectBirthplaceDialog(this, selectTimeIndex, Arrays.asList(array_time));
        selectBirthplaceDialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                selectTimeIndex = object == null ? 0 : (int) object;
                String nameType = array_time[selectTimeIndex];
                ll_time.setEditorContent(nameType);
            }
        });
        selectBirthplaceDialog.show();
    }

    private Calendar selectCalendar;

    /**
     * 选择日期
     */
    private void showDate() {
        SelectDateDialog selectDateDialog = new SelectDateDialog(this, selectCalendar);
        selectDateDialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                selectCalendar = (Calendar) object;
                long timeInMillis = selectCalendar.getTimeInMillis();
                String millis2String = TimeUtils.millis2String(timeInMillis, DateFormatUtils.getDateFormat3());
                ll_date.setEditorContent(millis2String);
            }
        });
        selectDateDialog.show();
    }

    @OnClick({R.id.iv_back, R.id.tv_start})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_start:
                MainActivity.startMainActivity2(this);
                finish();
                break;
            default:
                break;
        }
    }
}
