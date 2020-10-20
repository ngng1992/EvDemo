package com.mfinance.everjoy.everjoy.ui.mine.securities;

import android.hardware.Camera;
import android.view.TextureView;
import android.view.View;

import androidx.annotation.NonNull;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.everjoy.base.BaseViewActivity;

/**
 * 拍摄2秒的视频
 */
public class SecuritiesVideoActivity extends BaseViewActivity {

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
        return R.layout.activity_secutities_video;
    }

    @Override
    protected void initView(View currentView) {

    }

    private Camera camera = null;

    private void openCamera(){
        Camera c = null;
        try {
            Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
        } catch (Exception e){
            e.printStackTrace();
            // 打开失败，可能是别的资源占用，不支持前置
        }
    }

//    private TextureView.SurfaceTextureListener surfaceTextureListener = new TextureView.SurfaceTextureListener() {
//        @Override
//        public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
////            openCamera(width, height);
//        }
//
//        @Override
//        public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {
////            configureTransform(width, height);
//        }
//
//        @Override
//        public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
//            return true;
//        }
//
//        @Override
//        public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {
//
//        }
//    };
}
