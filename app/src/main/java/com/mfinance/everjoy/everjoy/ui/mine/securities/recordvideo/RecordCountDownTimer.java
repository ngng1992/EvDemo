package com.mfinance.everjoy.everjoy.ui.mine.securities.recordvideo;

import android.os.CountDownTimer;

public class RecordCountDownTimer extends CountDownTimer {

    /**
     *
     * @param millisInFuture 总时长
     * @param countDownInterval 每次间隔多次时间
     */
    RecordCountDownTimer(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if (recordCallBack != null) {
            recordCallBack.progress(millisUntilFinished);
        }
    }

    @Override
    public void onFinish() {
        if (recordCallBack != null) {
            recordCallBack.finish();
        }
    }

    private RecordCallBack recordCallBack;

    public void setRecordCallBack(RecordCallBack recordCallBack) {
        this.recordCallBack = recordCallBack;
    }

}