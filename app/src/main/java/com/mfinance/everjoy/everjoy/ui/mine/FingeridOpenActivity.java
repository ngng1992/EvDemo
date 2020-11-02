package com.mfinance.everjoy.everjoy.ui.mine;

import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.CompanySettings;
import com.mfinance.everjoy.app.constant.FXConstants;
import com.mfinance.everjoy.everjoy.base.BaseViewActivity;
import com.mfinance.everjoy.everjoy.ui.home.MainActivity;

import androidx.core.hardware.fingerprint.FingerprintManagerCompat;
import androidx.core.os.CancellationSignal;
import butterknife.OnClick;

/**
 * 打开指纹识别
 */
public class FingeridOpenActivity extends BaseViewActivity {

    @Override
    protected boolean isFullStatusByView() {
        return true;
    }

    @Override
    protected boolean isRemoveAppBar() {
        return true;
    }

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_fingerid_open;
    }

    @Override
    protected void initView(View currentView) {

    }

    @OnClick({R.id.iv_back, R.id.tv_open, R.id.tv_no_open})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_open:
                activate();
                break;
            case R.id.tv_no_open:
                toMainPage();
                break;
            default:
                break;
        }
    }

    CancellationSignal cancellationSignal;
    FingerprintManagerCompat fingerprintManager;
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alertDialog;

    private void activate() {
        alertDialogBuilder = new AlertDialog.Builder(this, CompanySettings.alertDialogTheme);
        alertDialogBuilder.setTitle(res.getString(R.string.db_setting_id));
        fingerprintManager = FingerprintManagerCompat.from(this);
        //首先检查硬件设备-指纹识别是否支持
        if (!fingerprintManager.isHardwareDetected()) {
            alertDialogBuilder
                    .setMessage(res.getString(R.string.id_not_support))
                    .setCancelable(false)
                    .setNegativeButton(res.getString(R.string.btn_cancel),new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            dialog.cancel();
                            pauseTips();
                        }
                    });

            alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            return;
        }

        //安全性检查，只要少一样都用不起来，认为必须要求有屏幕锁
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        if (!keyguardManager.isKeyguardSecure()) {
            alertDialogBuilder
                    .setMessage(res.getString(R.string.id_no_passcode))
                    .setCancelable(false)
                    .setNegativeButton(res.getString(R.string.btn_cancel),new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            dialog.cancel();
                            pauseTips();
                        }
                    });

            alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            return;
        }
        //是否设置了指纹
        if (!fingerprintManager.hasEnrolledFingerprints()) {
            alertDialogBuilder
                    .setMessage(res.getString(R.string.id_no_fingerprint))
                    .setCancelable(false)
                    .setNegativeButton(res.getString(R.string.btn_cancel),new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            dialog.cancel();
                            pauseTips();
                        }
                    });

            alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            return;
        }
        CancellationSignal cancellationSignal = new CancellationSignal();

        startFingerprintListening();
    }

    private void startFingerprintListening()
    {
        alertDialogBuilder
                .setMessage(res.getString(R.string.id_scaning))
                .setCancelable(false)
                .setNegativeButton(res.getString(R.string.btn_cancel),new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                        pauseTips();
                    }
                });

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        cancellationSignal = new CancellationSignal();

        fingerprintManager.authenticate(null, //crypto objects 的 wrapper class，可以透過它讓 authenticate 過程更為安全，但也可以不使用。
                0, //用來取消 authenticate 的 object
                cancellationSignal, //optional flags; should be 0
                mAuthenticationCallback, //callback 用來接收 authenticate 成功與否，有三個 callback method
                null); //optional 的參數，如果有使用，FingerprintManager 會透過它來傳遞訊息
    }


    FingerprintManagerCompat.AuthenticationCallback mAuthenticationCallback
            = new FingerprintManagerCompat.AuthenticationCallback()
    {
        @Override
        public void onAuthenticationError(int errorCode, CharSequence errString)
        {
            alertDialog.dismiss();
            alertDialogBuilder
                    .setMessage(errString)
                    .setCancelable(false)
                    .setNegativeButton(res.getString(R.string.btn_cancel),new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            dialog.cancel();
                            pauseTips();
                        }
                    });

            alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        @Override
        public void onAuthenticationFailed()
        {
            alertDialog.dismiss();
            alertDialogBuilder
                    .setMessage(res.getString(R.string.id_fail))
                    .setCancelable(false)
                    .setNegativeButton(res.getString(R.string.btn_cancel),new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            dialog.cancel();
                            pauseTips();
                        }
                    });

            alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        @Override
        public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result)
        {
            alertDialog.dismiss();
            app.setFingerID(true);
            toMainPage(); //Completed Finger registration
        }
    };

    private void pauseTips(){
        if (cancellationSignal!=null) {
            cancellationSignal.cancel();
            cancellationSignal = null;
        }
    }

    private void toMainPage(){
        startActivity(new Intent(this, MainActivity.class));
    }
}
