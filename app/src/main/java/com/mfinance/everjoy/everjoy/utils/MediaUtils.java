package com.mfinance.everjoy.everjoy.utils;

import com.ywl5320.wlmedia.WlMedia;
import com.ywl5320.wlmedia.enums.WlPlayModel;
import com.ywl5320.wlmedia.listener.WlOnCompleteListener;
import com.ywl5320.wlmedia.listener.WlOnPreparedListener;
import com.ywl5320.wlmedia.listener.WlOnTimeInfoListener;

public class MediaUtils {


    private WlMedia wlMedia;
    private WlOnTimeInfoListener wlOnTimeInfoListener;
    private WlOnCompleteListener wlOnCompleteListener;

    public void setWlOnCompleteListener(WlOnCompleteListener wlOnCompleteListener) {
        this.wlOnCompleteListener = wlOnCompleteListener;
    }

    public void setWlOnTimeInfoListener(WlOnTimeInfoListener wlOnTimeInfoListener) {
        this.wlOnTimeInfoListener = wlOnTimeInfoListener;
    }

    public MediaUtils() {

    }

    public void start(String voiceUrl) {
        wlMedia = new WlMedia();
        wlMedia.setPlayModel(WlPlayModel.PLAYMODEL_ONLY_AUDIO);
        wlMedia.setSource(voiceUrl);
        wlMedia.setOnPreparedListener(new WlOnPreparedListener() {
            @Override
            public void onPrepared() {
                wlMedia.start();
            }
        });
        wlMedia.setOnTimeInfoListener(wlOnTimeInfoListener);
        wlMedia.setOnCompleteListener(wlOnCompleteListener);
        wlMedia.prepared();
    }


    public double getDuration() {
       if (wlMedia == null) {
           return 0;
       }
       return wlMedia.getDuration();
    }


    public void setSpeed() {
       if (wlMedia == null) {
          return;
       }
       wlMedia.setSpeed(2f);
    }

    public void setSeek(double seek) {
       if (wlMedia == null) {
          return;
       }
       wlMedia.seek(seek);
    }

    public void stop() {
        if (wlMedia == null) {
            return;
        }
        wlMedia.stop();
    }

    public void pause() {
        if (wlMedia == null) {
            return;
        }
        wlMedia.pause();

    }

    public void resume() {
        if (wlMedia == null) {
            return;
        }
        wlMedia.resume();
    }


    public void destroy() {
        if (wlMedia == null) {
            return;
        }
        wlMedia.release();
    }

}
