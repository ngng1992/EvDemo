package com.mfinance.everjoy.everjoy.ui.mine.securities.recordvideo;

import android.content.Context;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Build;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import net.mfinance.chatlib.utils.ConfigUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * 拍照,录像:  并保存图片，视频到本地
 */
public class CameraUtils {

    /**
     * 录制的时长
     */
    public static final int RECORD_TIME = 2300;

    //录制视频比特率
    public static final int MEDIA_QUALITY_HIGH = 20 * 100000;
    public static final int MEDIA_QUALITY_MIDDLE = 16 * 100000;
    public static final int MEDIA_QUALITY_LOW = 12 * 100000;
    public static final int MEDIA_QUALITY_POOR = 8 * 100000;
    public static final int MEDIA_QUALITY_FUNNY = 4 * 100000;
    public static final int MEDIA_QUALITY_DESPAIR = 2 * 100000;
    public static final int MEDIA_QUALITY_SORRY = 1 * 80000;

    private MediaRecorder mediaRecorder;
    private Camera mCamera;
    /*** 标识当前是前摄像头还是后摄像头  back:0  front:1*/
    private int backOrFtont = 0;
    private SurfaceHolder.Callback callback;
    private Context context;
    private SurfaceView surfaceView;
    /***录制视频的videoSize*/
    private int height, width;
    /***photo的height ,width*/
    private int heightPhoto, widthPhoto;

    // 录制定时器2s, 大于2s
    private RecordCountDownTimer timer = new RecordCountDownTimer(RECORD_TIME, 1000);

    private RecordCallBack recordCallBack;

    public void setRecordCallBack(RecordCallBack recordCallBack) {
        this.recordCallBack = recordCallBack;
        timer.setRecordCallBack(this.recordCallBack);
    }

    public void create(SurfaceView surfaceView, Context context) {
        this.context = context;
        this.surfaceView = surfaceView;
        surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceView.setKeepScreenOn(true);
        callback = new SurfaceHolder.Callback() {
            public void surfaceCreated(SurfaceHolder holder) {
                open();
            }

            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {
                doChange(holder);
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                if (mCamera != null) {
                    mCamera.release();
                    mCamera = null;
                }
            }
        };
        surfaceView.getHolder().addCallback(callback);
    }

    public void open() {
        try {
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            int numberOfCameras = Camera.getNumberOfCameras();
            for (int i = 0; i < numberOfCameras; i++) {
                Camera.getCameraInfo(i, cameraInfo);
                // 如果有前置就打开
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    mCamera = Camera.open(i);
                    getVideoSize();
                    if (Build.VERSION.SDK_INT > 17 && this.mCamera != null) {
                        try {
                            this.mCamera.enableShutterSound(false);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("camera", "enable shutter sound faild");
                        }
                    }
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doChange(SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.setDisplayOrientation(90);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 切换摄像头
     */
    public void changeCamera() {
        int cameraCount = 0;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();
        for (int i = 0; i < cameraCount; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT && backOrFtont == 0) {
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
                mCamera = Camera.open(i);
                try {
                    mCamera.setPreviewDisplay(surfaceView.getHolder());
                    mCamera.setDisplayOrientation(90);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                backOrFtont = 1;
                mCamera.startPreview();
                break;
            } else if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK && backOrFtont == 1) {
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
                mCamera = Camera.open(i);
                try {
                    mCamera.setPreviewDisplay(surfaceView.getHolder());
                    mCamera.setDisplayOrientation(90);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mCamera.startPreview();
                backOrFtont = 0;
                break;
            }
        }

    }

    public void stopRecord() {
        mediaRecorder.release();
        mCamera.release();
        mediaRecorder = null;
        mCamera = Camera.open();
        mediaRecorder = new MediaRecorder();
        doChange(surfaceView.getHolder());
    }


    public void stop() {
        if (mediaRecorder != null) {
            mediaRecorder.release();
        }
        if (mCamera != null) {
            mCamera.release();
        }
        if (timer != null) {
            timer.cancel();
        }
    }

    public void destroy() {
        if (mediaRecorder != null) {
            mediaRecorder.release();
            mediaRecorder = null;
        }
        if (mCamera != null) {
            surfaceView.getHolder().removeCallback(callback);
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.lock();
            mCamera.release();
            mCamera = null;
        }
        if (timer != null) {
            timer.cancel();
        }
    }

    /**
     * 开始录制
     */
    public void startRecord() {
        if (recordCallBack != null) {
            recordCallBack.start();
        }
        mediaRecorder = new MediaRecorder();
        if (parameters == null) {
            parameters = mCamera.getParameters();
        }

        mCamera.unlock();

        mediaRecorder.reset();
        mediaRecorder.setCamera(mCamera);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        Camera.Size videoSize;
        if (parameters.getSupportedVideoSizes() == null) {
            videoSize = CameraParamUtil.getCloselyPreSize(true,
                    CameraParamUtil.getScreenWidth(context),
                    CameraParamUtil.getScreenHeight(context),
                    parameters.getSupportedPreviewSizes());
        } else {
            videoSize = CameraParamUtil.getCloselyPreSize(true,
                    CameraParamUtil.getScreenWidth(context),
                    CameraParamUtil.getScreenHeight(context),
                    parameters.getSupportedVideoSizes());
        }
        Log.e("video", "setVideoSize    width = " + videoSize.width + "height = " + videoSize.height);
        if (videoSize.width == videoSize.height) {
            mediaRecorder.setVideoSize(width, height);
        } else {
            mediaRecorder.setVideoSize(videoSize.width, videoSize.height);
        }
        mediaRecorder.setOrientationHint(270);

        if (DeviceUtil.isHuaWeiRongyao()) {
            mediaRecorder.setVideoEncodingBitRate(4 * 100000);
        } else {
            mediaRecorder.setVideoEncodingBitRate(MEDIA_QUALITY_MIDDLE);
        }
        mediaRecorder.setPreviewDisplay(surfaceView.getHolder().getSurface());
        // 设置视频路径
        String videoNamePath = ConfigUtils.getVideoNamePath();
        if (recordCallBack != null) {
            recordCallBack.filePath(videoNamePath);
        }
        mediaRecorder.setOutputFile(videoNamePath);
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            Log.e("vidoe", "startRecord IllegalStateException");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("vidoe", "startRecord IOException");
        } catch (RuntimeException e) {
            Log.e("vidoe", "startRecord RuntimeException");
        }
        timer.start();
    }

    private Camera.Parameters parameters;

    /***
     * 获取SupportedVideoSizes 控制输出视频width
     * 获取PictureSize的大小
     */
    private void getVideoSize() {
        try {
            parameters = mCamera.getParameters();

            // 设置最佳的预览尺寸，避免拉伸
            Camera.Size preSize = CameraParamUtil.getCloselyPreSize(true,
                    CameraParamUtil.getScreenWidth(context),
                    CameraParamUtil.getScreenHeight(context),
                    parameters.getSupportedPreviewSizes());
            parameters.setPreviewSize(preSize.width, preSize.height);

            List<String> focusModes = parameters.getSupportedFocusModes();
            if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            }
            mCamera.setParameters(parameters);
            mCamera.setPreviewDisplay(surfaceView.getHolder());  //SurfaceView
            mCamera.setDisplayOrientation(90);//浏览角度
            mCamera.setPreviewCallback(new Camera.PreviewCallback() {
                @Override
                public void onPreviewFrame(byte[] data, Camera camera) {
                    //每一帧回调
                }
            });
            mCamera.startPreview();//启动浏览
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void takePicture(String photoPath, String photoName) {
        mCamera.takePicture(null, null, new PictureCallBack(photoPath, photoName));
    }

    /*** 拍照功能*/
    private class PictureCallBack implements Camera.PictureCallback {
        /*** 照片保存的路径和名称*/
        private String path;
        private String name;

        public PictureCallBack(String path, String name) {
            this.path = path;
            this.name = name;
        }

        @Override
        public void onPictureTaken(byte[] bytes, Camera camera) {
            File file1 = new File(path);
            if (!file1.exists()) {
                file1.mkdirs();
            }
            File file = new File(path, name);
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                try {
                    fos.write(bytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            camera.startPreview();
        }
    }


}