package com.mfinance.everjoy.everjoy.ui.mine.securities.photo;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.everjoy.base.BaseViewActivity;
import com.mfinance.everjoy.everjoy.config.Constants;
import com.mfinance.everjoy.everjoy.config.FileConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 拍摄证件照
 */
public class CardIdActivity extends BaseViewActivity implements View.OnClickListener {

    public static void openCardIdActivity(Activity activity) {
        openCardIdActivity(activity, false);
    }

    public static void openCardIdActivity(Activity activity, boolean isNeedLand) {
        Intent intent = new Intent(activity, CardIdActivity.class);
        intent.putExtra(Constants.IS_NEED_LAND, isNeedLand);
        activity.startActivityForResult(intent, Constants.KEY_CAMERA_REQUEST_CODE);
    }

    private CameraPreview cameraPreview;
    private TextView tvIdcardDesc;
    private LinearLayout cameraOption;
    private LinearLayout resultView;
    private ImageView flashImageView;

    /**
     * 文件路径
     */
    private String filePath = "";
    private String fileName = "";

    private boolean isNeedLand;

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        isNeedLand = intent.getBooleanExtra(Constants.IS_NEED_LAND, false);
        if (isNeedLand) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

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
        return R.layout.activity_camera_cardid;
    }

    @Override
    protected void initView(View currentView) {
        cameraPreview = (CameraPreview) currentView.findViewById(R.id.camera_surface);
        //获取屏幕最小边，设置为cameraPreview较窄的一边
        float screenMinSize = Math.min(getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels);
        //根据screenMinSize，计算出cameraPreview的较宽的一边，长宽比为标准的16:9
        float maxSize = screenMinSize / 9.0f * 16.0f;
        RelativeLayout.LayoutParams layoutParams;
        layoutParams = new RelativeLayout.LayoutParams((int) screenMinSize, (int) maxSize);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        cameraPreview.setLayoutParams(layoutParams);

        flashImageView = (ImageView) currentView.findViewById(R.id.camera_flash);
        cameraOption = (LinearLayout) currentView.findViewById(R.id.camera_option);
        tvIdcardDesc = (TextView) currentView.findViewById(R.id.tv_idcard_desc);
        resultView = (LinearLayout) currentView.findViewById(R.id.camera_result);
        cameraPreview.setOnClickListener(this);
        currentView.findViewById(R.id.camera_close).setOnClickListener(this);
        currentView.findViewById(R.id.camera_take).setOnClickListener(this);
        flashImageView.setOnClickListener(this);
        currentView.findViewById(R.id.camera_result_ok).setOnClickListener(this);
        currentView.findViewById(R.id.camera_result_cancel).setOnClickListener(this);

//        if (isNeedLand) {
//            float height = (int) (screenMinSize * 0.8);
//            float width = (int) (height * 43.0f / 30.0f);
//            LinearLayout.LayoutParams containerParams = new LinearLayout.LayoutParams((int) width, ViewGroup.LayoutParams.MATCH_PARENT);
//            LinearLayout.LayoutParams cropParams = new LinearLayout.LayoutParams((int) width, (int) height);
//            containerView.setLayoutParams(containerParams);
//            cropView.setLayoutParams(cropParams);
//        } else{
//            float width = (int) (screenMinSize * 0.8);
//            float height = (int) (width * 43.0f / 30.0f);
//            LinearLayout.LayoutParams containerParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) height);
//            LinearLayout.LayoutParams cropParams = new LinearLayout.LayoutParams((int) width, (int) height);
//            containerView.setLayoutParams(containerParams);
//            cropView.setLayoutParams(cropParams);
//        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.camera_surface) {
            cameraPreview.focus();
        } else if (id == R.id.camera_close) {
            if (cameraPreview != null) {
                cameraPreview.release();
            }
            goBack();
        } else if (id == R.id.camera_take) {
            takePhoto();
        } else if (id == R.id.camera_flash) {
            boolean isFlashOn = cameraPreview.switchFlashLight();
            flashImageView.setImageResource(isFlashOn ? R.mipmap.camera_flash_on : R.mipmap.camera_flash_off);
        } else if (id == R.id.camera_result_ok) {
            if (cameraPreview != null) {
                cameraPreview.release();
            }
            goBack();
        } else if (id == R.id.camera_result_cancel) {
            cameraOption.setVisibility(View.VISIBLE);
            cameraPreview.setEnabled(true);
            resultView.setVisibility(View.GONE);
            cameraPreview.startPreview();
        }
    }

    private void takePhoto() {
        cameraPreview.setEnabled(false);
        cameraPreview.takePhoto(new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(final byte[] data, Camera camera) {
                camera.stopPreview();
                //子线程处理图片，防止ANR
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            fileName = "idcard_" + FileConfig.getImageName();
                            filePath = FileConfig.FILE_IDCARDS + "/" + fileName;
                            File originalFile = new File(filePath);
                            FileOutputStream originalFileOutputStream = new FileOutputStream(originalFile);
                            originalFileOutputStream.write(data);
                            originalFileOutputStream.close();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    cameraOption.setVisibility(View.GONE);
                                    tvIdcardDesc.setVisibility(View.GONE);
                                    resultView.setVisibility(View.VISIBLE);
                                }
                            });
                            return;
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                cameraPreview.setEnabled(true);
                                cameraOption.setVisibility(View.VISIBLE);
                                tvIdcardDesc.setVisibility(View.VISIBLE);
                                resultView.setVisibility(View.GONE);
                            }
                        });
                    }
                }).start();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraPreview != null) {
            cameraPreview.release();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        // 全屏
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        goBack();
    }

    /**
     * 返回
     */
    private void goBack() {
        Intent intent = new Intent();
        intent.putExtra("imgPath", filePath);
        intent.putExtra("imgName", fileName);
        setResult(Constants.KEY_CAMERA_RESULT_OK, intent);
        finish();
    }
}
