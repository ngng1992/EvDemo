package net.mfinance.commonlib.timer;

import android.app.Activity;
import android.os.Message;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 倒计时
 */
public class CountDownHelper {

    private Timer timer;
    private TimerTask timerTask;
    private CountDownHandler identifyingCodeHandler;

    public CountDownHelper(Activity activity) {
        identifyingCodeHandler = new CountDownHandler(activity);
    }

    public void setOnTimerTaskCallBack(OnTimerCallBack onTimerCallBack) {
        if (identifyingCodeHandler != null && onTimerCallBack != null) {
            identifyingCodeHandler.setOnTimerCallBack(onTimerCallBack);
        }
    }

    public void startTimer() {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                long time = getTime();
                if (time <= 0) {
                    stopTimer();
                } else {
                    Message message = identifyingCodeHandler.obtainMessage();
                    message.what = CountDownHandler.TIME_WHAT;
                    identifyingCodeHandler.sendMessage(message);
                }
            }
        };
        timer.schedule(timerTask, 0, 1000);
    }

    /**
     * 停止任务
     */
    public void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        if (identifyingCodeHandler != null) {
            identifyingCodeHandler.removeMessages(CountDownHandler.TIME_WHAT);
        }
    }

    /**
     * 设置倒计时总时长
     */
    public void setTime(long totalTime) {
        if (identifyingCodeHandler != null) {
            identifyingCodeHandler.setTime(totalTime);
        }
    }

    /**
     * 执行总时间，单位s
     *
     * @return 时间
     */
    public long getTime() {
        return identifyingCodeHandler == null ? 0L : identifyingCodeHandler.getTime();
    }
}
