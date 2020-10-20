package net.mfinance.commonlib.timer.timerhelper;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import net.mfinance.commonlib.timer.OnTimerCallBack;

import java.lang.ref.WeakReference;

/**
 * 计时器
 */
public class TimeHandler extends Handler {

    public static final int TIME_WHAT = 40;
    private long time = 0;
    private WeakReference<Activity> mWeakReference;
    private OnTimerCallBack onTimerTaskCallBack;

    TimeHandler(Activity activity) {
        mWeakReference = new WeakReference<>(activity);
    }

    public void setOnTimerTaskCallBack(OnTimerCallBack onTimerTaskCallBack) {
        this.onTimerTaskCallBack = onTimerTaskCallBack;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        Activity activity = mWeakReference.get();
        if (activity != null) {
            int what = msg.what;
            if (what == TIME_WHAT) {
                time++;
                if (null != onTimerTaskCallBack) {
                    onTimerTaskCallBack.onCallBack(time);
                }
            }
        }
    }

    public long getTime() {
        return time;
    }

    /**
     * 设置从哪个时间开始
     *
     * @param startTime 开始时间
     */
    public void setStartTime(long startTime) {
        time = time + startTime;
    }
}
