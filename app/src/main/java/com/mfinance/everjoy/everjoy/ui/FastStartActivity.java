package com.mfinance.everjoy.everjoy.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.mfinance.everjoy.R;

/**
 * 8.0版本，设置了android:Theme.Translucent.NoTitleBar.Fullscreen不能设置android:screenOrientation="portrait"，
 * 否则会报错：java.lang.IllegalStateException: Only fullscreen opaque activities can request orientation
 * 8.1以上版本已修复
 * 快速启动页，设置透明主题色，达到秒开APP的效果，避免出现白屏或黑屏
 */
public class FastStartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 先在Manifest.xml中设置启动FastStartStyle，启动后再设置成默认的AppTheme
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        // 这里判断一次，在某些手机上按home和back键会使app重启，这里处理不在重启，在哪个界面home就显示在哪个界面
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        startActivity(new Intent(this, SplashActivity.class));
        finish();
    }
}
