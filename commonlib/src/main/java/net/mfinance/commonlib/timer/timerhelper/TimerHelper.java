package net.mfinance.commonlib.timer.timerhelper;

import android.app.Activity;
import android.os.Message;

import net.mfinance.commonlib.timer.OnTimerCallBack;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 定时器
 */
public class TimerHelper {

    private Timer timer;
    private TimerTask timerTask;
    private TimeHandler handlerHelper;

    public TimerHelper(Activity activity) {
        handlerHelper = new TimeHandler(activity);
    }

    public void setOnTimerTaskCallBack(OnTimerCallBack onTimerTaskCallBack) {
        if (handlerHelper != null && onTimerTaskCallBack != null) {
            handlerHelper.setOnTimerTaskCallBack(onTimerTaskCallBack);
        }
    }

    public void startTimer() {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Message message = handlerHelper.obtainMessage();
                message.what = TimeHandler.TIME_WHAT;
                handlerHelper.sendMessage(message);
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
        if (handlerHelper != null) {
            handlerHelper.removeMessages(TimeHandler.TIME_WHAT);
        }
    }

    /**
     * 恢复任务
     */
    public void resumeTimer() {
        // 避免没有关闭线程
        stopTimer();
        startTimer();
    }

    /**
     * 执行总时间，单位s
     *
     * @return 时间
     */
    public long getTime() {
        return handlerHelper == null ? 0L : handlerHelper.getTime();
    }

    /**
     * 设置时间，单位s
     */
    public void setStartTime(long startTime) {
        handlerHelper.setStartTime(startTime);
    }
}
