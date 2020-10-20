package net.mfinance.commonlib.timer;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * 计时Handler
 *
 * 验证码：120s
 */
public class CountDownHandler extends Handler {

    public static final int TIME_WHAT = 30;
    private long time = 120;
    private WeakReference<Activity> mWeakReference;
    private OnTimerCallBack onTimerCallBack;

    public CountDownHandler(Activity activity) {
        mWeakReference = new WeakReference<>(activity);
    }

    public void setOnTimerCallBack(OnTimerCallBack onTimerCallBack) {
        this.onTimerCallBack = onTimerCallBack;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        Activity activity = mWeakReference.get();
        if (activity != null) {
            int what = msg.what;
            if (what == TIME_WHAT) {
                time--;
                if (null != onTimerCallBack) {
                    onTimerCallBack.onCallBack(time);
                }
            }
        }
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }
}
