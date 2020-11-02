package com.mfinance.everjoy.everjoy.ui.mine.securities;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.everjoy.base.BaseViewActivity;
import com.mfinance.everjoy.everjoy.config.Constants;
import com.mfinance.everjoy.everjoy.config.FileConfig;
import com.mfinance.everjoy.everjoy.sp.SecuritiesSharedPUtils;
import com.mfinance.everjoy.everjoy.ui.mine.securities.recordvideo.CameraUtils;
import com.mfinance.everjoy.everjoy.ui.mine.securities.recordvideo.RecordCallBack;

import net.mfinance.chatlib.utils.ConfigUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 录制视频
 */
public class RecordVideoActivity extends BaseViewActivity {


    public static void startRecordVideoActivity(Activity activity, int cardType) {
        Intent intent = new Intent(activity, RecordVideoActivity.class);
        intent.putExtra(Constants.CARD_TYPE, cardType);
        activity.startActivity(intent);
    }

    @BindView(R.id.tv_desc)
    TextView tv_desc;
    @BindView(R.id.tv_count_time)
    TextView tv_count_time;
    @BindView(R.id.tv_verfi_success)
    TextView tv_verfi_success;
    @BindView(R.id.tv_camera)
    TextView tvCamera;

    private CameraUtils cameraUtils;
    // 文件路径
    private String videoPath;
    private String videoName;
    private boolean isSuccess;
    private int cardType;

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
        return R.layout.activity_record_video;
    }

    @Override
    protected void initView(View currentView) {
        FileConfig.initFileConfig();
        cardType = getIntent().getIntExtra(Constants.CARD_TYPE, Constants.CARD_HK_TYPE);

        SurfaceView surfaceView = currentView.findViewById(R.id.surfaceView);

        tv_desc.setVisibility(View.VISIBLE);
        tv_count_time.setVisibility(View.GONE);
        tv_verfi_success.setVisibility(View.GONE);

        cameraUtils = new CameraUtils();
        cameraUtils.create(surfaceView, this);
        cameraUtils.setRecordCallBack(new RecordCallBack() {
            @Override
            public void finish() {
                tv_desc.setVisibility(View.GONE);
                tv_count_time.setVisibility(View.GONE);
                tv_verfi_success.setVisibility(View.VISIBLE);
                tvCamera.setAlpha(1f);
                tvCamera.setClickable(true);
                tvCamera.setText(R.string.txt_finish);
                isSuccess = true;
            }

            @Override
            public void start() {
                tv_desc.setVisibility(View.GONE);
                tv_count_time.setVisibility(View.VISIBLE);
                tv_verfi_success.setVisibility(View.GONE);
                tv_count_time.setText(String.valueOf(CameraUtils.RECORD_TIME / 1000));
                tvCamera.setAlpha(0.5f);
                tvCamera.setClickable(false);
                isSuccess = false;
            }

            @Override
            public void progress(long millis) {
                tv_count_time.setText(String.valueOf((millis / 1000)));
            }

            @Override
            public void filePath(String videoPath, String videoName) {
                RecordVideoActivity.this.videoPath = videoPath;
                RecordVideoActivity.this.videoName = videoName;
            }
        });
    }

    @OnClick({R.id.tv_back, R.id.tv_camera})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                onBackPressed();
                break;
            case R.id.tv_camera:
                if (isSuccess) {
                    // 上传证件/保存
                    saveToSp();
                    UploadCardActivity.startUploadCardActivity(this, cardType);
                    finish();
                } else {
                    cameraUtils.startRecord();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        saveToSp();
        finish();
    }

    private void saveToSp() {
        cameraUtils.destroy();
        if (isSuccess) {
            if (!TextUtils.isEmpty(videoPath)) {
                SecuritiesSharedPUtils.setVideoPath(videoPath);
            }
            if (!TextUtils.isEmpty(videoName)) {
                SecuritiesSharedPUtils.setVideoName(videoName);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("video", "onStop: ");
        isSuccess = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("video", "onDestroy: ");
        isSuccess = false;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );

    }
}
