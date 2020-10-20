package com.mfinance.everjoy.app;


import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mfinance.everjoy.app.constant.FXConstants;
import com.mfinance.everjoy.app.constant.ServiceFunction;

import androidx.core.hardware.fingerprint.FingerprintManagerCompat;
import androidx.core.os.CancellationSignal;

/* -- Facebook
import com.facebook.android.Facebook;
*/

import com.mfinance.everjoy.R;

public class SettingIDActivity extends BaseActivity {


    Button btnOK;
    Button btnCancel;
    boolean bSaveTouchID;
    CancellationSignal cancellationSignal;
    FingerprintManagerCompat fingerprintManager;
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alertDialog;

    @Override
    public void bindEvent() {
        btnOK.setOnClickListener(v -> {
            if (bSaveTouchID)
                deactivate();
            else {
                if (!setting.contains(FXConstants.SETTING_USER_ID) || !setting.contains(FXConstants.SETTING_USER_PASSWORD)) {
                    Toast.makeText(this, R.string.id_not_save_password, Toast.LENGTH_LONG).show();
                    return;
                }
                activate();
            }
            updateUI();
        });

        btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void deactivate() {
        alertDialogBuilder = new AlertDialog.Builder(this, CompanySettings.alertDialogTheme);
        alertDialogBuilder.setTitle(res.getString(R.string.db_setting_id));
        alertDialogBuilder
                .setMessage(res.getString(R.string.Deactivate) + "?")
                .setCancelable(false)
                .setPositiveButton(res.getString(R.string.btn_ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences.Editor editor = setting.edit();
                        editor.putBoolean(FXConstants.SETTING_SAVE_TOUCH_ID, false);
                        editor.commit();
                        pauseTips();
                        updateUI();
                    }
                })
                .setNegativeButton(res.getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        pauseTips();
                    }
                });
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        return;
    }

    private void activate() {
        alertDialogBuilder = new AlertDialog.Builder(this, CompanySettings.alertDialogTheme);
        alertDialogBuilder.setTitle(res.getString(R.string.db_setting_id));
        fingerprintManager = FingerprintManagerCompat.from(this);
        //首先检查硬件设备-指纹识别是否支持
        if (!fingerprintManager.isHardwareDetected()) {
            alertDialogBuilder
                    .setMessage(res.getString(R.string.id_not_support))
                    .setCancelable(false)
                    .setNegativeButton(res.getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
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
                    .setNegativeButton(res.getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
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
                    .setNegativeButton(res.getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
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

    private void startFingerprintListening() {
        alertDialogBuilder
                .setMessage(res.getString(R.string.id_scaning))
                .setCancelable(false)
                .setNegativeButton(res.getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
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
            = new FingerprintManagerCompat.AuthenticationCallback() {
        @Override
        public void onAuthenticationError(int errorCode, CharSequence errString) {
            alertDialog.dismiss();
            alertDialogBuilder
                    .setMessage(errString)
                    .setCancelable(false)
                    .setNegativeButton(res.getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            pauseTips();
                        }
                    });

            alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        @Override
        public void onAuthenticationFailed() {
            alertDialog.dismiss();
            alertDialogBuilder
                    .setMessage(res.getString(R.string.id_fail))
                    .setCancelable(false)
                    .setNegativeButton(res.getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            pauseTips();
                        }
                    });

            alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        @Override
        public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
            alertDialog.dismiss();
            SharedPreferences.Editor editor = setting.edit();
            editor.putBoolean(FXConstants.SETTING_SAVE_TOUCH_ID, true);
            editor.commit();
            updateUI();
        }
    };

    @Override
    public void onPause() {
        super.onPause();
    }

    private void pauseTips() {
        if (cancellationSignal != null) {
            cancellationSignal.cancel();
            cancellationSignal = null;
        }
    }


    @Override
    public void handleByChild(Message msg) {
    }

    @Override
    public void loadLayout() {
        setContentView(R.layout.v_setup_id);

        btnOK = (Button) findViewById(R.id.btnSubmit);
        btnCancel = (Button) findViewById(R.id.btnCancel2);
        bSaveTouchID = setting.getBoolean(FXConstants.SETTING_SAVE_TOUCH_ID, false);

    }

    @Override
    public void updateUI() {
        bSaveTouchID = setting.getBoolean(FXConstants.SETTING_SAVE_TOUCH_ID, false);

        if (bSaveTouchID) {
            btnOK.setText(res.getString(R.string.Deactivate));
            btnOK.setBackgroundResource(R.drawable.btn_deactivate);
            ((ImageView) findViewById(R.id.img_touch)).setImageResource(R.drawable.img_done);
            ((TextView) findViewById(R.id.txt_id)).setText(res.getString(R.string.id_text_ok));
            ((TextView) findViewById(R.id.txt_done)).setVisibility(View.VISIBLE);
        } else {
            btnOK.setText(res.getString(R.string.Activate));
            btnOK.setBackgroundResource(R.drawable.btn_submit);
            ((ImageView) findViewById(R.id.img_touch)).setImageResource(R.drawable.img_finger);
            ((TextView) findViewById(R.id.txt_id)).setText(res.getString(R.string.id_text));
            ((TextView) findViewById(R.id.txt_done)).setVisibility(View.GONE);
        }
    }

    @Override
    public boolean isBottonBarExist() {
        return true;
    }

    @Override
    public boolean isTopBarExist() {
        return true;
    }

    @Override
    public boolean showLogout() {
        return true;
    }

    @Override
    public boolean showTopNav() {
        return true;
    }

    @Override
    public boolean showConnected() {
        return true;
    }

    @Override
    public boolean showPlatformType() {
        return true;
    }

    @Override
    public int getServiceId() {
        return ServiceFunction.SRV_SETTING_ID;
    }

    @Override
    public int getActivityServiceCode() {
        return ServiceFunction.SRV_SETTING_ID;
    }

}