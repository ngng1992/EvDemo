package com.mfinance.everjoy.everjoy.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.CompanySettings;
import com.mfinance.everjoy.app.InitialActivity;
import com.mfinance.everjoy.everjoy.base.BaseEverjoyActivity;
import com.mfinance.everjoy.everjoy.network.okhttp.OkHttpUtils;
import com.mfinance.everjoy.everjoy.network.okhttp.OnHttpCompleteListener;
import com.mfinance.everjoy.everjoy.ui.home.MainActivity;
import com.mfinance.everjoy.everjoy.utils.ToolsUtils;

import net.mfinance.commonlib.permission.PermissionController;

/**
 * 闪屏页
 */
public class SplashActivity extends BaseEverjoyActivity {

    private RelativeLayout rlSplash;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        rlSplash = findViewById(R.id.rl_sp);
        initAnim();
    }

    private void initAnim() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.splash_alpha);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // 请求读写存储卡权限
                showPermissions();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        // 开始播放动画
        rlSplash.startAnimation(animation);
    }

    private void showPermissions() {
        PermissionController permissionController = new PermissionController(this);
        permissionController.setPermissions(PermissionController.AUDIO_CAMERA_STORAGE);
        permissionController.setOnPermissionListener(new PermissionController.OnPermissionListener() {
            @Override
            public void onHasPermission(boolean hasPermission) {
                if (hasPermission) {
                    startActivity(new Intent(SplashActivity.this, InitialActivity.class));
//                    startActivity(new Intent(SplashActivity.this, RegisterSuccessOpenAccountActivity.class));
                    finish();
                }
//                if (hasPermission) {
//                startMainActivityAndCheckNetwork();
//                }else {
                // 不申请权限不能进入
//                    finish();
//                }
            }
        });
        permissionController.requestPermission();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (rlSplash != null) {
            rlSplash.clearAnimation();
        }
    }

    @Override
    public void onBackPressed() {
        // 启动页按返回键不退出
//        super.onBackPressed();
    }

    /**
     * 进入app首页，游客可以进入首页
     */
    private void startMainActivityAndCheckNetwork() {
        if (ToolsUtils.checkNetwork(this)) {
            if (ToolsUtils.isAppLaucherURLAvailable()) {
                if (!ToolsUtils.checkVersionOK(app)) {
                    // 版本不符合要求，去安卓市场下载
                    ToolsUtils.openAndroidMarket(this);
                } else {
                    if (ToolsUtils.isServerAvailable(app)) {
                        // 网络请求一次url，保存
                        String contractListURL = app.getContractListURL();
                        OkHttpUtils.getRequest(contractListURL, new OnHttpCompleteListener() {
                            @Override
                            public void onComplete(String result) {
                                CompanySettings.parseJson(app, result);
                            }

                        });

                        startMainActivity(4);
                    } else {
                        // 显示进度条
                        startMainActivity(3);
                    }
                }
            } else {
                // 显示进度条
                startMainActivity(2);
            }
        } else {
            startMainActivity(1);
        }
    }

    /**
     * 网络，进入首页
     *
     * @param i i是标记哪种状态进入首页的，无实际意义
     */
    private void startMainActivity(int i) {
        Log.e("Splash", "进入首页=====   i ==" + i);
        MainActivity.startMainActivity(this);
        finish();
    }
}
