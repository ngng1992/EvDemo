package com.mfinance.everjoy.everjoy.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.everjoy.dialog.base.BaseBottomDialog;

import java.util.Calendar;

import top.defaults.view.DateTimePickerView;

/**
 * 选择出生日期
 */
public class SelectBirthdayDialog extends BaseBottomDialog {

    private Calendar selectDate;

    public SelectBirthdayDialog(@NonNull Context context, Calendar selectDate) {
        super(context);
        this.selectDate = selectDate;
    }

    @Override
    protected void initView() {
        TextView tv_cancel = findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        TextView tv_define = findViewById(R.id.tv_define);
        tv_define.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onClickDialogOrFragmentViewListener != null) {
                    onClickDialogOrFragmentViewListener.onClickView(tv_define, selectDate == null ? Calendar.getInstance() : selectDate);
                }
            }
        });

        DateTimePickerView datePickerView = findViewById(R.id.datePickerView);

        Calendar startDate = Calendar.getInstance();
        int year = startDate.get(Calendar.YEAR);
        int month = startDate.get(Calendar.MONTH);
        int date = startDate.get(Calendar.DATE);
        // 月份从0开始
        startDate.set(year - 100, month, date);

        Calendar endDate = Calendar.getInstance();
        datePickerView.setStartDate(startDate);
        datePickerView.setSelectedDate(selectDate == null ? endDate : selectDate);
        datePickerView.setEndDate(Calendar.getInstance());

        datePickerView.setOnSelectedDateChangedListener(new DateTimePickerView.OnSelectedDateChangedListener() {
            @Override
            public void onSelectedDateChanged(Calendar date) {
                selectDate = date;
//                int year = date.get(Calendar.YEAR);
//                int month = date.get(Calendar.MONTH);
//                int dayOfMonth = date.get(Calendar.DAY_OF_MONTH);
//                String dateString = String.format(Locale.getDefault(), "%d-%02d-%02d", year, month + 1, dayOfMonth);
//                Log.e("date", "new date: " + dateString);
            }
        });

    }

    @Override
    protected int setLayout() {
        return R.layout.dialog_select_birthday;
    }
}
