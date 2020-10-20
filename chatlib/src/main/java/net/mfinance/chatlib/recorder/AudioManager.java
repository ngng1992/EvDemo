package net.mfinance.chatlib.recorder;

import android.media.MediaRecorder;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 录音，音频管理
 */
public class AudioManager {

    private MediaRecorder mMediaRecorder;
    private String mDir;
    private String mCurrentFilePath;

    private static AudioManager mInstance;

    private boolean isPrepared;

    private AudioManager(String dir) {
        mDir = dir;
    }

    /**
     * 回调准备完毕
     */
    public interface AudioStateListener {
        void wellPrepared();
    }

    private AudioStateListener mListener;

    public void setOnAudioStateListener(AudioStateListener listener) {
        mListener = listener;
    }

    public static AudioManager getInstance(String dir) {
        if (mInstance == null) {
            synchronized (AudioManager.class) {
                if (mInstance == null) {
                    mInstance = new AudioManager(dir);
                }
            }
        }
        return mInstance;
    }


    /**
     * 准备
     */
    public void prepareAudio() {
        try {
            isPrepared = false;
            File dir = new File(mDir);
            if (!dir.exists()) {
                dir.mkdir();
            }
            String fileName = generateFileName();

            File file = new File(dir, fileName);

            mCurrentFilePath = file.getAbsolutePath();

            mMediaRecorder = new MediaRecorder();
            //设置输出文件
            mMediaRecorder.setOutputFile(file.getAbsolutePath());
            //设置MediaRecorder的音频源为麦克风
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            //设置音频格式
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
            //设置音频的格式为amr
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mMediaRecorder.prepare();
            mMediaRecorder.start();
            //准备结束
            isPrepared = true;
            if (mListener != null) {
                mListener.wellPrepared();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成UUID唯一标示符
     * 算法的核心思想是结合机器的网卡、当地时间、一个随即数来生成GUID
     * .amr音频文件
     */
    private String generateFileName() {
        return UUID.randomUUID().toString() + ".amr";
    }

    public int getVoiceLevel(int maxLevel) {
        if (isPrepared) {
            //获得最大的振幅getMaxAmplitude() 1-32767
            try {
                // mMediaRecorder可能是null
                return maxLevel * mMediaRecorder.getMaxAmplitude() / 32768 + 1;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 1;
    }

    public void release() {
        // 调用recorder.stop()时抛出异常，调用stop()之前将OnErrorListener、OnInfoListener和PreviewDisplay置空:
        if (mMediaRecorder != null) {
            mMediaRecorder.setOnErrorListener(null);
            mMediaRecorder.setOnInfoListener(null);
            mMediaRecorder.setPreviewDisplay(null);
            try {
                mMediaRecorder.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
    }

    public void cancel() {
        release();
        if (mCurrentFilePath != null) {
            // 删除录音文件
            File file = new File(mCurrentFilePath);
            file.delete();
            mCurrentFilePath = null;
        }
    }

    public String getCurrentFilePath() {
        return mCurrentFilePath;
    }

}
