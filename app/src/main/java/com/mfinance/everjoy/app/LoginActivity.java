package com.mfinance.everjoy.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.mfinance.everjoy.BuildConfig;
import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.constant.FXConstants;
import com.mfinance.everjoy.app.constant.Protocol;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.model.DataRepository;
import com.mfinance.everjoy.app.model.LoginProgress;
import com.mfinance.everjoy.app.pojo.ConnectionStatus;
import com.mfinance.everjoy.app.service.FxMobileTraderService;
import com.mfinance.everjoy.app.service.internal.PriceAgentConnectionProcessor;
import com.mfinance.everjoy.app.util.CommonFunction;
import com.mfinance.everjoy.app.util.MessageObj;
import com.mfinance.everjoy.app.util.Utility;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import androidx.core.hardware.fingerprint.FingerprintManagerCompat;
import androidx.core.os.CancellationSignal;

/**
 * It is a login view.
 *
 * @author justin.lai
 */
public class LoginActivity extends BaseActivity {
    Timer loginTimer;
    EditText etID;
    EditText etRptgrp;
    EditText etPassword;
    IPhoneToggle tbSavePassword;
    IPhoneToggle tbPlatformType;
    boolean bSavePassword;
    PopupForgotPassword popForgot;
    PopupRegister popRegister;
    Button btnLanguage;
    Button btnLanguageEng;
    Button btnLanguageSc;
    Button btnLanguageTc;

    PopupString popLanguage;
    //View vLanguage;

    Button btnLogin;
    Button btnRegister;
    RelativeLayout btnTips;
    String sLang;

    ImageView img_savepassword;
    ImageView img_prd;
    ImageView img_demo;
    TextView label_savepassword;
    TextView label_prd;
    TextView label_demo;
    LinearLayout view_register;
    RelativeLayout view_tips;
    RelativeLayout view_lang;
    ImageView bg_lang;

    CancellationSignal cancellationSignal;
    FingerprintManagerCompat fingerprintManager;
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alertDialog;

    private String LastLoginID = "";
    public static boolean backFromIdentityCheck = false;
    public static boolean identityPassed = false;
    private static boolean FakeLogin = false;
    private static boolean IDLogin = false;

    class Task extends TimerTask {
        @Override
        public void run() {
            if (dialog != null && dialog.isShowing()) {
                boolean isFinished = false;

                // Timeout occur
                int RoundRobinIndex = 0;
                int iTrialIndex = 0;

                boolean demo = false;
                if (CompanySettings.newinterface) {
                    if (label_demo.getCurrentTextColor() == (res.getColor(R.color.theme_text)))
                        demo = true;
                } else {
                    if (tbPlatformType.isChecked())
                        demo = true;
                }

                if (demo) {
                    app.iTrialIndexDemo = (app.iTrialIndexDemo + 1) % app.alLoginInfoDemo.size();
                    iTrialIndex = app.iTrialIndexDemo;
                    RoundRobinIndex = app.RoundRobinIndexDemo;
                } else {
                    if (CompanySettings.checkProdServer() == 1) {
                        app.iTrialIndexProd = (app.iTrialIndexProd + 1) % app.alLoginInfoProd.size();
                        iTrialIndex = app.iTrialIndexProd;
                        RoundRobinIndex = app.RoundRobinIndexProd;
                    } else if (CompanySettings.checkProdServer() == 2) {
                        app.iTrialIndexProd2 = (app.iTrialIndexProd2 + 1) % app.alLoginInfoProd2.size();
                        iTrialIndex = app.iTrialIndexProd2;
                        RoundRobinIndex = app.RoundRobinIndexProd2;
                    } else if (CompanySettings.checkProdServer() == 3) {
                        app.iTrialIndexProd3 = (app.iTrialIndexProd3 + 1) % app.alLoginInfoProd3.size();
                        iTrialIndex = app.iTrialIndexProd3;
                        RoundRobinIndex = app.RoundRobinIndexProd3;
                    } else if (CompanySettings.checkProdServer() == 4) {
                        app.iTrialIndexProd4 = (app.iTrialIndexProd4 + 1) % app.alLoginInfoProd4.size();
                        iTrialIndex = app.iTrialIndexProd4;
                        RoundRobinIndex = app.RoundRobinIndexProd4;
                    } else if (CompanySettings.checkProdServer() == 5) {
                        app.iTrialIndexProd5 = (app.iTrialIndexProd5 + 1) % app.alLoginInfoProd5.size();
                        iTrialIndex = app.iTrialIndexProd5;
                        RoundRobinIndex = app.RoundRobinIndexProd5;
                    } else if (CompanySettings.checkProdServer() == 6) {
                        app.iTrialIndexProd6 = (app.iTrialIndexProd6 + 1) % app.alLoginInfoProd6.size();
                        iTrialIndex = app.iTrialIndexProd6;
                        RoundRobinIndex = app.RoundRobinIndexProd6;
                    } else if (CompanySettings.checkProdServer() == 7) {
                        app.iTrialIndexProd7 = (app.iTrialIndexProd7 + 1) % app.alLoginInfoProd7.size();
                        iTrialIndex = app.iTrialIndexProd7;
                        RoundRobinIndex = app.RoundRobinIndexProd7;
                    } else if (CompanySettings.checkProdServer() == 8) {
                        app.iTrialIndexProd8 = (app.iTrialIndexProd8 + 1) % app.alLoginInfoProd8.size();
                        iTrialIndex = app.iTrialIndexProd8;
                        RoundRobinIndex = app.RoundRobinIndexProd8;
                    } else if (CompanySettings.checkProdServer() == 9) {
                        app.iTrialIndexProd9 = (app.iTrialIndexProd9 + 1) % app.alLoginInfoProd9.size();
                        iTrialIndex = app.iTrialIndexProd9;
                        RoundRobinIndex = app.RoundRobinIndexProd9;
                    } else if (CompanySettings.checkProdServer() == 10) {
                        app.iTrialIndexProd10 = (app.iTrialIndexProd10 + 1) % app.alLoginInfoProd10.size();
                        iTrialIndex = app.iTrialIndexProd10;
                        RoundRobinIndex = app.RoundRobinIndexProd10;
                    }
                }

                if (CompanySettings.ENABLE_FATCH_SERVER || CompanySettings.FOR_TEST || CompanySettings.ENABLE_FATCH_REPORT_GROUP_OTX || iTrialIndex == RoundRobinIndex) {
                    isFinished = true;
                    app.isLoading = false;
                    // If IP is fetch from Server or OTX Mode, or RR Trial has finished
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            dialog = null;
                            Toast.makeText(app, R.string.msg_306,
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                Message msg = Message.obtain(null, ServiceFunction.SRV_LOGOUT);
                msg.replyTo = mServiceMessengerHandler;
                try {
                    Bundle data = new Bundle();
                    data.putBoolean(Protocol.Logout.REDIRECT, false);
                    msg.setData(data);
                    mService.send(msg);
                } catch (RemoteException e) {
                    Log.e(TAG, "Unable to send login message", e.fillInStackTrace());
                }

                if (isFinished == false) {
                    // Wait 2 seconds to let the previous connection close
                    try {
                        Thread.sleep(2000);
                        (new Thread(moveToLogin)).start();
                        loginTimer.schedule(new Task(), 60 * 1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            } else {
                // The login progress is interrupted, change the isLoading to false
                app.isLoading = false;
            }

        }
    }

    Runnable moveToLogin = new Runnable() {
        @Override
        public void run() {
            try {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog == null)
                            dialog = ProgressDialog.show(LoginActivity.this, "", res.getString(R.string.please_wait), true);
                    }
                });
                DataRepository.getInstance().clear();
                DataRepository.getInstance().setStrUser(etID.getEditableText().toString());
                if (checkNetwork()) {
                    //Thread.sleep(2000);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            ConnectionStatus connectionStatus = app.data.getGuestPriceAgentConnectionStatus();
                            switch (connectionStatus) {
                                case CONNECTING:
                                case CONNECTED:
                                    Message message = Message.obtain(null, ServiceFunction.SRV_GUEST_PRICE_AGENT);
                                    message.arg1 = PriceAgentConnectionProcessor.ActionType.DISCONNECT.getValue();
                                    try {
                                        mService.send(message);
                                    } catch (Exception ex) {

                                    }
                                    Message message1 = Message.obtain(null, ServiceFunction.SRV_GUEST_PRICE_AGENT);
                                    message1.arg1 = PriceAgentConnectionProcessor.ActionType.RESET.getValue();
                                    try {
                                        mService.send(message1);
                                    } catch (Exception ex) {

                                    }
                                    break;
                                default:
                                    break;
                            }
                            String sID = etID.getEditableText().toString();
                            String sRptgrp = etRptgrp.getEditableText().toString();
                            String sPass = etPassword.getEditableText().toString();

                            if (sRptgrp.length() > 0)
                                CompanySettings.COMPANY_PREFIX = sRptgrp + "_";
                            SharedPreferences.Editor editor = setting.edit();

                            //editor.putBoolean(FXConstants.SETTING_SAVE_LOGIN_INFO, tbSavePassword.isChecked());

//							    if(tbSavePassword.isChecked()){
//							    	editor.putString(FXConstants.SETTING_USER_ID, etID.getEditableText().toString());
//							    	CommonFunction cf = new CommonFunction();
//						            cf.setKey(Utility.getINIPasswdKey());
//							    	editor.putString(FXConstants.SETTING_USER_PASSWORD, cf.encryptText(sPass));
//							    	editor.putString("report_group", etRptgrp.getEditableText().toString());
//							    }

                            //editor.putBoolean(FXConstants.SETTING_PLATFORM_TYPE, tbPlatformType.isChecked());

                            editor.commit();

                            if (CompanySettings.ENABLE_FATCH_REPORT_GROUP)
                                try {
                                    boolean isDemo = false; //tbPlatformType.isChecked();
                                    if (CompanySettings.newinterface) {
                                        if (label_demo.getTextColors().equals(res.getColor(R.color.theme_text)))
                                            isDemo = true;
                                    } else
                                        isDemo = tbPlatformType.isChecked();

                                    if (CompanySettings.ENABLE_FATCH_PLATFORM_ID_FROM_MOBILE_SERVICE)
                                        CompanySettings.PLATFORM_ID_FROM_MOBILE_SERVICE = 1;

                                    String URL = app.getReportGroupURL(isDemo)
                                            + "id=" + sID;
                                    if (isDemo)
                                        URL += "&isDemo=1";
                                    else
                                        URL += "&isDemo=0";
                                    String result = fetch(URL);

                                    if (BuildConfig.DEBUG)
                                        Log.e("REPORT_GROUP", result);

                                    String[] results = result.split("\\|");
                                    if (results.length > 1) {
                                        CompanySettings.PLATFORM_ID_FROM_MOBILE_SERVICE = Integer.parseInt(results[0]);
                                        result = results[1];
                                    }

                                    if (isDemo)
                                        CompanySettings.DEMO_REPORT_GROUP = result;
                                    else
                                        CompanySettings.PRODUCTION_REPORT_GROUP = result;
                                } catch (Exception e) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            dialog.dismiss();
                                            dialog = null;
                                            Toast.makeText(app, R.string.msg_306,
                                                    Toast.LENGTH_SHORT).show();
                                            app.isLoading = false;
                                        }
                                    });
                                    return;
                                }

                            if (CompanySettings.ENABLE_FATCH_REPORT_GROUP_OTX)
                                try {
                                    CommonFunction cf = new CommonFunction(false);
                                    cf.setKey(Utility.getHttpKey());

                                    String rptgrp = CompanySettings.COMPANY_PREFIX.replace("_", "");
                                    String platform = "";
                                    String result = "";
                                    if (CompanySettings.newinterface) {
                                        platform = "production";//tbPlatformType.isChecked()?"demo":"production";
                                        result = fetch(app.getReportGroupURL(false)
                                                + "param=" + cf.encryptText("rptgroup=" + CompanySettings.COMPANY_PREFIX.replace("_", "") + "&ios=false"));
                                    } else {
                                        platform = tbPlatformType.isChecked() ? "demo" : "production";
                                        result = fetch(app.getReportGroupURL(tbPlatformType.isChecked())
                                                + "param=" + cf.encryptText("rptgroup=" + CompanySettings.COMPANY_PREFIX.replace("_", "") + "&ios=false"));
                                    }
                                    result = cf.decryptText(result);
                                    //Log.e("FATCH_REQ", "rptgroup=" + CompanySettings.COMPANY_PREFIX.replace("_", "")+ "&ios=false");
                                    Log.i("FATCH_URL", app.getReportGroupURL(false)
                                            + "param=" + cf.encryptText("rptgroup=" + CompanySettings.COMPANY_PREFIX.replace("_", "") + "&ios=false"));
                                    Log.i("FATCH_REP", result);
                                    JSONObject jo = new JSONObject(result);
                                    if (platform.equals("demo")) {
                                        app.loginInfoDemo.sURL = jo.getString("ip");
                                        app.loginInfoDemo.sPort = jo.getString("port");
                                        CompanySettings.DEMO_REPORT_GROUP = rptgrp;
                                    } else {
                                        app.loginInfoProd.sURL = jo.getString("ip");
                                        app.loginInfoProd.sPort = jo.getString("port");
                                        CompanySettings.PRODUCTION_REPORT_GROUP = rptgrp;
                                    }
                                    MessageObj.setKey(Integer.parseInt(jo.getString("keyCode")), jo.getString("messageKey"));
                                } catch (Exception e) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            dialog.dismiss();
                                            dialog = null;
                                            Toast.makeText(app, R.string.msg_306,
                                                    Toast.LENGTH_SHORT).show();
                                            app.isLoading = false;
                                        }
                                    });
                                    return;
                                }

                            if (CompanySettings.ENABLE_FATCH_SERVER)
                                try {
                                    String platform = "";
                                    if (CompanySettings.newinterface)
                                        platform = "production";//tbPlatformType.isChecked()?"demo":"production";
                                    else
                                        platform = tbPlatformType.isChecked() ? "demo" : "production";

                                    String result = fetch(app.getServerByIdURL()
                                            + "id=" + sID + "&platform=" + platform);
                                    //Log.e("FATCH_SERVER", result);
                                    JSONObject jo = new JSONObject(result);
                                    if (platform.equals("demo")) {
                                        app.loginInfoDemo.sURL = jo.getString("url");
                                        app.loginInfoDemo.sPort = jo.getString("port");
                                    } else {
                                        app.loginInfoProd.sURL = jo.getString("url");
                                        app.loginInfoProd.sPort = jo.getString("port");
                                    }
                                } catch (Exception e) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            dialog.dismiss();
                                            dialog = null;
                                            Toast.makeText(app, R.string.msg_306,
                                                    Toast.LENGTH_SHORT).show();
                                            app.isLoading = false;
                                        }
                                    });
                                    return;
                                }

                            login(sID, sPass);
                        }
                    });
                } else {
                    if (CompanySettings.ENABLE_FATCH_SERVER || CompanySettings.FOR_TEST || CompanySettings.ENABLE_FATCH_REPORT_GROUP_OTX) {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                dialog.dismiss();
                                dialog = null;
                            }
                        });
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void bindEvent() {
        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if no view has focus:
                View view = getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                if (!app.isLoading) {
                    if (FakeLogin) {
                        dialog = ProgressDialog.show(LoginActivity.this, "", res.getString(R.string.please_wait), true);
                        Timer timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(
                                                LoginActivity.this,
                                                res.getText(R.string.msg_317),
                                                Toast.LENGTH_LONG)
                                                .show();
                                        dialog.dismiss();
                                        dialog = null;
                                        app.isLoading = false;
                                    }
                                });
                            }
                        }, 60 * 1000);
                        return;
                    }

                    if (LastLoginID.equals(etID.getEditableText().toString()) == false)
                        identityPassed = false;

                    LastLoginID = etID.getEditableText().toString();

                    if ("".equals(etID.getEditableText().toString())) {
                        Bundle bundle = new Bundle();
                        bundle.putString(ServiceFunction.MESSAGE, (String) res.getText(R.string.msg_empty_username));
                        goTo(ServiceFunction.SRV_SHOW_TOAST, bundle);
                        return;
                    }

                    if ("".equals(etPassword.getEditableText().toString())) {
                        Bundle bundle = new Bundle();
                        bundle.putString(ServiceFunction.MESSAGE, (String) res.getText(R.string.msg_empty_password));
                        goTo(ServiceFunction.SRV_SHOW_TOAST, bundle);
                        return;
                    }

                    if (CompanySettings.ENABLE_FATCH_REPORT_GROUP_OTX && "".equals(etRptgrp.getEditableText().toString())) {
                        Bundle bundle = new Bundle();
                        bundle.putString(ServiceFunction.MESSAGE, (String) res.getText(Utility.getStringIdById("msg_empty_report_group")));
                        goTo(ServiceFunction.SRV_SHOW_TOAST, bundle);
                        return;
                    }
                    app.isLoading = true;

                    LoginProgress.reset();
                    if (loginTimer != null) {
                        loginTimer.cancel();
                    }
                    loginTimer = new Timer();
                    loginTimer.schedule(new Task(), 600 * 1000);
                    (new Thread(moveToLogin)).start();
                }

            }
        });

        if (CompanySettings.newinterface) {
            ((TextView) findViewById(R.id.label_forgotpassword)).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    displayForgetPassword();
                }
            });

            ((Button) findViewById(R.id.btnForgot)).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    displayForgetPassword();
                }
            });
        } else {
            findViewById(R.id.btnClear).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            etID.setText("");
                            etPassword.setText("");
                            etRptgrp.setText("");
                        }
                    });
                }
            });

            ((Button) findViewById(R.id.btnForgot)).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    displayForgetPassword();
                }
            });
        }


        popForgot.btnClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        popForgot.dismiss();
                    }
                });
            }
        });

        popForgot.popup.findViewById(R.id.btnSubmit).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (popForgot.getURL() == null) return;
                        dialog = ProgressDialog.show(LoginActivity.this, "", res.getString(R.string.please_wait), true);
                        asyncfetch(LoginActivity.this, popForgot.getURL(),
                                new OnFetchCompleteListener() {
                                    @Override
                                    public void onSuccess(String result) {
                                        dialog.dismiss();
                                        dialog = null;
                                        try {
                                            int iResult = Integer
                                                    .parseInt(result);
                                            if (iResult == 0) {
                                                popForgot.clearFields();
                                                Toast.makeText(
                                                        LoginActivity.this,
                                                        res.getText(R.string.msg_reset_password_success),
                                                        Toast.LENGTH_LONG)
                                                        .show();
                                                popForgot.dismiss();
                                            } else {
                                                Toast.makeText(
                                                        LoginActivity.this,
                                                        res.getText(R.string.msg_reset_password_fail),
                                                        Toast.LENGTH_LONG)
                                                        .show();
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Toast.makeText(
                                                    LoginActivity.this,
                                                    res.getText(R.string.msg_reset_password_fail),
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    }
                });

        btnLanguage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CompanySettings.newinterface)
                    view_lang.setVisibility(View.VISIBLE);
                else {
                    popLanguage.setSelected(btnLanguage.getText().toString());
                    popLanguage.showLikeQuickAction();
                }
            }
        });


        if (CompanySettings.newinterface) {
            btnLanguageEng.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    view_lang.setVisibility(View.INVISIBLE);
                    setLanguage("English");
                    recreate();
                }
            });

            btnLanguageTc.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    view_lang.setVisibility(View.INVISIBLE);
                    setLanguage("繁體");
                    recreate();
                }
            });

            btnLanguageSc.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    view_lang.setVisibility(View.INVISIBLE);
                    setLanguage("简体");
                    recreate();
                }
            });

            bg_lang.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    view_lang.setVisibility(View.INVISIBLE);
                }
            });
        } else {
            popLanguage.btnCommit.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    popLanguage.dismiss();
                    sLang = popLanguage.getValue();
                    if (CompanySettings.newinterface) {
                        if (popLanguage.getIndex(popLanguage.getValue()) == 0)
                            btnLanguage.setText("Eng");
                        else if (popLanguage.getIndex(popLanguage.getValue()) == 1)
                            btnLanguage.setText("繁");
                        else if (popLanguage.getIndex(popLanguage.getValue()) == 2)
                            btnLanguage.setText("简");
                    }
                    //btnLanguage.setText(sLang);
                    setLanguage(sLang);
                    recreate();
                }
            });

            popLanguage.btnClose.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    popLanguage.dismiss();
                }
            });
        }

        findViewById(R.id.btnRegister).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean bWebView = false;
                if (CompanySettings.ENABLE_WEBVIEW_DEMO_REGISTRATION == true) {
                    if (app.hmDemoRegistrationUrls == null)
                        bWebView = false;
                    else if (getLanguage() == Locale.ENGLISH && app.hmDemoRegistrationUrls.containsKey("EN")) {
                        bWebView = true;

                    } else if (getLanguage() == Locale.SIMPLIFIED_CHINESE && app.hmDemoRegistrationUrls.containsKey("SC")) {
                        bWebView = true;
                    } else if (getLanguage() == Locale.TRADITIONAL_CHINESE && app.hmDemoRegistrationUrls.containsKey("TC")) {
                        bWebView = true;
                    }
                }

                if (bWebView == true) {
                    goTo(ServiceFunction.SRV_MOVE_TO_DEMO_REGISTRATION);
                } else
                    popRegister.showLikeQuickAction();
            }
        });

        popRegister.btnClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                popRegister.dismiss();
            }
        });

        if (popRegister.popup.findViewById(R.id.btnSubmit) != null) {
            popRegister.popup.findViewById(R.id.btnSubmit).setOnClickListener(
                    new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (popRegister.getURL() == null) return;
                            dialog = ProgressDialog.show(LoginActivity.this, "", res.getString(R.string.please_wait), true);
                            asyncfetch(LoginActivity.this, popRegister.getURL(),
                                    new OnFetchCompleteListener() {

                                        @Override
                                        public void onSuccess(String result) {
                                            dialog.dismiss();
                                            dialog = null;
                                            try {
                                                int iResult = Integer
                                                        .parseInt(result);
                                                switch (iResult) {
                                                    case 0:
                                                        if (CompanySettings.AUTO_FILL_LOGIN_AFTER_REG) {
                                                            etID.setText(popRegister
                                                                    .getUserName());
                                                            etPassword
                                                                    .setText(popRegister
                                                                            .getPassword());
                                                        }
                                                        popRegister.clearFields();
                                                        Toast.makeText(
                                                                LoginActivity.this,
                                                                res.getString(R.string.msg_register_success),
                                                                Toast.LENGTH_LONG)
                                                                .show();
                                                        popRegister.dismiss();
                                                        break;
                                                    case 1:
                                                        Toast.makeText(
                                                                LoginActivity.this,
                                                                res.getString(R.string.msg_register_account_in_used),
                                                                Toast.LENGTH_LONG)
                                                                .show();
                                                        break;
                                                    case 2:
                                                        Toast.makeText(
                                                                LoginActivity.this,
                                                                res.getString(R.string.msg_register_account_in_used),
                                                                Toast.LENGTH_LONG)
                                                                .show();
                                                        break;
                                                    default:
                                                        Toast.makeText(
                                                                LoginActivity.this,
                                                                res.getString(R.string.msg_register_fail),
                                                                Toast.LENGTH_LONG)
                                                                .show();
                                                        break;
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                Toast.makeText(
                                                        LoginActivity.this,
                                                        res.getString(R.string.msg_register_fail),
                                                        Toast.LENGTH_LONG)
                                                        .show();
                                            }
                                        }
                                    });

                        }
                    });
        }
    }

    @Override
    public void handleByChild(Message msg) {
    }

    @Override
    public void loadLayout() {
        if (CompanySettings.newinterface == true) {
            setContentView(R.layout.v_login_new);
            getWindow().setBackgroundDrawableResource(R.drawable.bg_main);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            if (CompanySettings.blackNotificationBarText)
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else
            setContentView(R.layout.v_login);

        if (getIntent().getBooleanExtra("disconnected", false)) {
            AlertDialog dialog2 = new AlertDialog.Builder(this, CompanySettings.alertDialogTheme).create();
            dialog2.setMessage(res.getString(R.string.msg_disconnect));
            dialog2.setButton(DialogInterface.BUTTON_POSITIVE, res.getText(R.string.btn_ok),
                    (dialog, which) -> dialog.dismiss()
            );
            dialog2.show();
        }
        if (!CompanySettings.VISIBLE_DEMO_LOGIN) {
            if (CompanySettings.newinterface) {
                this.findViewById(R.id.label_demo).setEnabled(false);
                this.findViewById(R.id.label_demo).setVisibility(View.INVISIBLE);
                this.findViewById(R.id.img_demo).setEnabled(false);
            } else
                this.findViewById(R.id.llPlatformType).setVisibility(View.INVISIBLE);
        }
        if (!CompanySettings.VISIBLE_DEMO_REGISTER) {
            if (CompanySettings.newinterface)
                this.findViewById(R.id.view_register).setVisibility(View.GONE);
            else
                this.findViewById(R.id.ll_register).setVisibility(View.GONE);
        }
        if (!CompanySettings.VISIBLE_FORGOT_PASSWORD) {
            if (CompanySettings.newinterface) {
                this.findViewById(R.id.label_forgotpassword).setVisibility(View.INVISIBLE);
                this.findViewById(R.id.btnForgot).setVisibility(View.INVISIBLE);
            } else
                this.findViewById(R.id.ll_forgotpassword).setVisibility(View.INVISIBLE);
        }
        if (CompanySettings.SHOW_DASHBOARD_BEFORE_LOGIN) {
            this.findViewById(R.id.llLanguage).setVisibility(View.GONE);
        }
        if (!CompanySettings.VISIBLE_PLATFORM_SWITCH) {
            this.findViewById(R.id.llPlatformType).setVisibility(View.INVISIBLE);
        }
        if (CompanySettings.newinterface) {
            btnLanguageEng = (Button) findViewById(R.id.btnLanguageEng);
            btnLanguageTc = (Button) findViewById(R.id.btnLanguageTc);
            btnLanguageSc = (Button) findViewById(R.id.btnLanguageSc);
            img_savepassword = (ImageView) findViewById(R.id.img_savepassword);
            label_savepassword = (TextView) findViewById(R.id.label_savepassword);
            img_prd = (ImageView) findViewById(R.id.img_prd);
            img_demo = (ImageView) findViewById(R.id.img_demo);
            label_prd = (TextView) findViewById(R.id.label_prd);
            label_demo = (TextView) findViewById(R.id.label_demo);
            btnRegister = (Button) findViewById(R.id.btnRegister);
            btnTips = (RelativeLayout) findViewById(R.id.btn_tips_register);
            view_register = (LinearLayout) findViewById(R.id.view_register);
            view_tips = (RelativeLayout) findViewById(R.id.view_tips);
            view_lang = (RelativeLayout) findViewById(R.id.view_lang_bg);
            bg_lang = (ImageView) findViewById(R.id.bg_lang);
        } else {
            tbSavePassword = new IPhoneToggle((ToggleButton) findViewById(R.id.tbSavePassword), res, R.string.tb_on, R.string.tb_off);
            tbPlatformType = new IPhoneToggle((ToggleButton) findViewById(R.id.tbPlatformType), res, R.string.tb_on, R.string.tb_off);
            tbSavePassword.setChecked(true, false);
            tbPlatformType.setChecked(true, false);
        }
        btnLanguage = (Button) findViewById(R.id.btnLanguage);
        etID = (EditText) findViewById(R.id.userID);
        etPassword = (EditText) findViewById(R.id.userPassword);


        if (Utility.getIdById("reportGroup") != -1) {
            etRptgrp = (EditText) findViewById(Utility.getIdById("reportGroup"));
        } else {
            etRptgrp = new EditText(this);
        }

        boolean bSaveLoginInfo = setting.getBoolean(FXConstants.SETTING_SAVE_LOGIN_INFO, false);
        boolean bSaveTouchID = setting.getBoolean(FXConstants.SETTING_SAVE_TOUCH_ID, false);

        if (!CompanySettings.newinterface)
            tbSavePassword.setChecked(true, bSaveLoginInfo);

        if (bSaveLoginInfo) {
            if (CompanySettings.newinterface) {
                bSavePassword = true;
                etID.setText(setting.getString(FXConstants.SETTING_USER_ID, ""));
                CommonFunction cf = new CommonFunction();
                cf.setKey(Utility.getINIPasswdKey());
                if (!bSaveTouchID)
                    etPassword.setText(cf.decryptText(setting.getString(FXConstants.SETTING_USER_PASSWORD, "")));
                else
                    etPassword.setText(null);
                etRptgrp.setText(setting.getString("report_group", ""));
                //tbPlatformType.setChecked(true, true);
                img_savepassword.setImageResource(R.drawable.icon_checkedbox);
                label_savepassword.setTextColor(res.getColor(R.color.theme_text));
            } else {
                etID.setText(setting.getString(FXConstants.SETTING_USER_ID, ""));
                CommonFunction cf = new CommonFunction();
                cf.setKey(Utility.getINIPasswdKey());
                etPassword.setText(cf.decryptText(setting.getString(FXConstants.SETTING_USER_PASSWORD, "")));
                etRptgrp.setText(setting.getString("report_group", ""));
                //tbPlatformType.setChecked(true, true);
            }
        } else {
            if (CompanySettings.newinterface) {
                bSavePassword = false;
                img_savepassword.setImageResource(R.drawable.icon_checkbox);
                label_savepassword.setTextColor(Color.WHITE);
            }
            etID.setText("");
            etPassword.setText("");
            etRptgrp.setText("");
            //tbPlatformType.setChecked(true, true);
        }

        if (CompanySettings.newinterface) {
            if (!bSaveTouchID || !setting.contains(FXConstants.SETTING_USER_ID) || !setting.contains(FXConstants.SETTING_USER_PASSWORD)) {
                btnTips.setVisibility(View.GONE);
                //view_tips.setY(view_tips.getY()-100);
            }
        }


        if (CompanySettings.newinterface) {
            IDLogin = false;

            //tbPlatformType.setChecked(true, setting.getBoolean(FXConstants.SETTING_PLATFORM_TYPE, false));
            if (setting.getBoolean(FXConstants.SETTING_PLATFORM_TYPE, false)) {
                img_demo.setImageResource(R.drawable.btn_tag_selected);
                label_demo.setTextColor(res.getColor(R.color.theme_text));
                img_prd.setImageResource(R.drawable.btn_tag);
                label_prd.setTextColor(Color.WHITE);
            } else {
                img_prd.setImageResource(R.drawable.btn_tag_selected);
                label_prd.setTextColor(res.getColor(R.color.theme_text));
                img_demo.setImageResource(R.drawable.btn_tag);
                label_demo.setTextColor(Color.WHITE);
            }

            ArrayList<String> alString = new ArrayList<String>(Arrays.asList(CompanySettings.AVALIABLE_LANGUAGE));

//		popLanguage = new PopupStringLang(getApplicationContext(), findViewById(R.id.rlTop), alString);
            popRegister = new PopupRegister(getApplicationContext(), findViewById(R.id.rlTop), res, app.getRegisterURL());
            popForgot = new PopupForgotPassword(getApplicationContext(), findViewById(R.id.rlTop), res, app.getForgotPasswordURL());

            if (getLanguageName().equals("English")) {
                btnLanguage.setText("Eng");
            } else if (getLanguageName().equals("繁體")) {
                btnLanguage.setText("繁");
            } else if (getLanguageName().equals("简体")) {
                btnLanguage.setText("简");
            } else
                btnLanguage.setText(app.isArrivedDashBoard && getNextLocale() != null ? getNextLocale() : getLanguageName());

            btnLogin = ((Button) findViewById(R.id.btnLogin));

            btnLanguageEng.setText("Eng");
            btnLanguageTc.setText("繁");
            btnLanguageSc.setText("简");

            img_savepassword.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    bSavePassword = !bSavePassword;
                    SharedPreferences.Editor editor = setting.edit();
                    editor.putBoolean(FXConstants.SETTING_SAVE_LOGIN_INFO, bSavePassword);
                    if (bSavePassword) {
                        editor.putString(FXConstants.SETTING_USER_ID, etID.getEditableText().toString());
                        CommonFunction cf = new CommonFunction();
                        cf.setKey(Utility.getINIPasswdKey());
                        editor.putString(FXConstants.SETTING_USER_PASSWORD, cf.encryptText(etPassword.getEditableText().toString()));
                        editor.putString("report_group", etRptgrp.getEditableText().toString());
                        img_savepassword.setImageResource(R.drawable.icon_checkedbox);
                        label_savepassword.setTextColor(res.getColor(R.color.theme_text));
                    } else {
                        editor.remove(FXConstants.SETTING_USER_ID);
                        editor.remove(FXConstants.SETTING_USER_PASSWORD);
                        editor.remove("report_group");
                        img_savepassword.setImageResource(R.drawable.icon_checkbox);
                        label_savepassword.setTextColor(Color.WHITE);
                    }
                    editor.commit();
                }

            });

            label_savepassword.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    bSavePassword = !bSavePassword;
                    SharedPreferences.Editor editor = setting.edit();
                    editor.putBoolean(FXConstants.SETTING_SAVE_LOGIN_INFO, bSavePassword);
                    if (bSavePassword) {
                        editor.putString(FXConstants.SETTING_USER_ID, etID.getEditableText().toString());
                        CommonFunction cf = new CommonFunction();
                        cf.setKey(Utility.getINIPasswdKey());
                        editor.putString(FXConstants.SETTING_USER_PASSWORD, cf.encryptText(etPassword.getEditableText().toString()));
                        editor.putString("report_group", etRptgrp.getEditableText().toString());
                        img_savepassword.setImageResource(R.drawable.icon_checkedbox);
                        label_savepassword.setTextColor(res.getColor(R.color.theme_text));
                    } else {
                        editor.remove(FXConstants.SETTING_USER_ID);
                        editor.remove(FXConstants.SETTING_USER_PASSWORD);
                        editor.remove("report_group");
                        img_savepassword.setImageResource(R.drawable.icon_checkbox);
                        label_savepassword.setTextColor(Color.WHITE);
                    }
                    editor.commit();
                }

            });

            img_prd.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    img_prd.setImageResource(R.drawable.btn_tag_selected);
                    label_prd.setTextColor(res.getColor(R.color.theme_text));
                    img_demo.setImageResource(R.drawable.btn_tag);
                    label_demo.setTextColor(Color.WHITE);
                    SharedPreferences.Editor editor = setting.edit();
                    editor.putBoolean(FXConstants.SETTING_PLATFORM_TYPE, false);
                    editor.commit();
                }
            });

            img_demo.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    img_demo.setImageResource(R.drawable.btn_tag_selected);
                    label_demo.setTextColor(res.getColor(R.color.theme_text));
                    img_prd.setImageResource(R.drawable.btn_tag);
                    label_prd.setTextColor(Color.WHITE);
                    SharedPreferences.Editor editor = setting.edit();
                    editor.putBoolean(FXConstants.SETTING_PLATFORM_TYPE, true);
                    editor.commit();
                }
            });

            btnTips.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialogBuilder = new AlertDialog.Builder(getApplicationContext(), CompanySettings.alertDialogTheme);
                    alertDialogBuilder.setTitle("Finger Print");
                    fingerprintManager = FingerprintManagerCompat.from(getApplicationContext());
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
            });
        } else {
            tbPlatformType.setChecked(true, setting.getBoolean(FXConstants.SETTING_PLATFORM_TYPE, false));

            ArrayList<String> alString = new ArrayList<String>(Arrays.asList(CompanySettings.AVALIABLE_LANGUAGE));

            popLanguage = new PopupString(getApplicationContext(), findViewById(R.id.rlTop), alString);
            popRegister = new PopupRegister(getApplicationContext(), findViewById(R.id.rlTop), res, app.getRegisterURL());
            popForgot = new PopupForgotPassword(getApplicationContext(), findViewById(R.id.rlTop), res, app.getForgotPasswordURL());

            btnLanguage.setText(app.isArrivedDashBoard && getNextLocale() != null ? getNextLocale() : getLanguageName());

            //vLanguage = findViewById(R.id.llLanguage);
            btnLogin = ((Button) findViewById(R.id.btnLogin));
		/*
		if(app.firstActivity == null)
			app.firstActivity = this;
		*/

            if (CompanySettings.PLAYFORM_TYPE == CompanySettings.PlatformType.DEMO) {
                tbPlatformType.setChecked(true, true);
                tbPlatformType.tb.setEnabled(false);
            } else if (CompanySettings.PLAYFORM_TYPE == CompanySettings.PlatformType.PRODUCTION) {
                tbPlatformType.setChecked(true, false);
                tbPlatformType.tb.setEnabled(false);
            }

            tbSavePassword.tb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {
                    tbSavePassword.setChecked(false, isChecked);
                    SharedPreferences.Editor editor = setting.edit();
                    editor.putBoolean(FXConstants.SETTING_SAVE_LOGIN_INFO, tbSavePassword.isChecked());
                    if (tbSavePassword.isChecked()) {
                        editor.putString(FXConstants.SETTING_USER_ID, etID.getEditableText().toString());
                        CommonFunction cf = new CommonFunction();
                        cf.setKey(Utility.getINIPasswdKey());
                        editor.putString(FXConstants.SETTING_USER_PASSWORD, cf.encryptText(etPassword.getEditableText().toString()));
                        editor.putString("report_group", etRptgrp.getEditableText().toString());
                    } else {
                        editor.remove(FXConstants.SETTING_USER_ID);
                        editor.remove(FXConstants.SETTING_USER_PASSWORD);
                        editor.remove("report_group");
                    }
                    editor.commit();
                }
            });

            tbPlatformType.tb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {
                    tbPlatformType.setChecked(false, isChecked);
                    SharedPreferences.Editor editor = setting.edit();
                    editor.putBoolean(FXConstants.SETTING_PLATFORM_TYPE, tbPlatformType.isChecked());
                    editor.commit();
                }
            });
        }


    }

    private void startFingerprintListening() {
        alertDialogBuilder = new AlertDialog.Builder(this, CompanySettings.alertDialogTheme);
        alertDialogBuilder.setTitle(res.getString(R.string.db_setting_id));
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

//			Bundle bundle = new Bundle();
//			bundle.putString(ServiceFunction.MESSAGE, errString.toString());
//			goTo(ServiceFunction.SRV_SHOW_TOAST, bundle);
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

//			Bundle bundle = new Bundle();
//			bundle.putString(ServiceFunction.MESSAGE, res.getString(R.string.id_fail));
//			goTo(ServiceFunction.SRV_SHOW_TOAST, bundle);
        }

        @Override
        public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
            alertDialog.dismiss();
            IDLogin = true;
            View view = getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            if (!app.isLoading) {
                if (FakeLogin) {
                    dialog = ProgressDialog.show(LoginActivity.this, "", res.getString(R.string.please_wait), true);
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(
                                            LoginActivity.this,
                                            res.getText(R.string.msg_317),
                                            Toast.LENGTH_LONG)
                                            .show();
                                    dialog.dismiss();
                                    dialog = null;
                                    app.isLoading = false;
                                }
                            });
                        }
                    }, 60 * 1000);
                    return;
                }

                if (LastLoginID.equals(etID.getEditableText().toString()) == false)
                    identityPassed = false;

                LastLoginID = etID.getEditableText().toString();

                if ("".equals(etID.getEditableText().toString())) {
                    Bundle bundle = new Bundle();
                    bundle.putString(ServiceFunction.MESSAGE, (String) res.getText(R.string.msg_empty_username));
                    goTo(ServiceFunction.SRV_SHOW_TOAST, bundle);
                    return;
                }

                if (CompanySettings.ENABLE_FATCH_REPORT_GROUP_OTX && "".equals(etRptgrp.getEditableText().toString())) {
                    Bundle bundle = new Bundle();
                    bundle.putString(ServiceFunction.MESSAGE, (String) res.getText(Utility.getStringIdById("msg_empty_report_group")));
                    goTo(ServiceFunction.SRV_SHOW_TOAST, bundle);
                    return;
                }
                app.isLoading = true;

                LoginProgress.reset();
                if (loginTimer != null) {
                    loginTimer.cancel();
                }
                loginTimer = new Timer();
                loginTimer.schedule(new Task(), 600 * 1000);
                (new Thread(moveToLogin)).start();
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();

        if (backFromIdentityCheck == true) {
            backFromIdentityCheck = false;
            if (identityPassed == true) {
                AlertDialog dialog = new AlertDialog.Builder(LoginActivity.this, CompanySettings.alertDialogTheme).create();
                dialog.setMessage(res.getText(R.string.msg_318));
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, res.getText(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                displayForgetPassword();
                            }
                        }
                );

                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, res.getText(R.string.no),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }
                );
                dialog.show();
            } else
                FakeLogin = true;
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        super.onServiceConnected(name, service);
        if (app.data.getBalanceRecord() != null)
            goTo(ServiceFunction.SRV_DASHBOARD);
    }

    @Override
    public void updateUI() {

    }

    @Override
    public boolean isBottonBarExist() {
        return true;
    }

    @Override
    public boolean isTopBarExist() {
        return true;
    }

    public void close() {
        Message loginMsg = Message.obtain(null, ServiceFunction.SRV_CLOSE_SERVCIE);
        loginMsg.replyTo = mServiceMessengerHandler;

        try {
            mService.send(loginMsg);
        } catch (RemoteException e) {
            Log.e(TAG, "Unable to send login message", e.fillInStackTrace());
        }
    }

    public void login(String strID, String strPassword) {

        Message loginMsg = Message.obtain(null, ServiceFunction.SRV_LOGIN);
        loginMsg.replyTo = mServiceMessengerHandler;

        boolean demo = false;

        if (CompanySettings.newinterface) {
            if (label_demo.getCurrentTextColor() == (res.getColor(R.color.theme_text)))
                demo = true;
        } else {
            if (tbPlatformType.isChecked())
                demo = true;
        }

        if (demo) {
            loginMsg.getData().putString(ServiceFunction.LOGIN_PLATFORM_TYPE, FXConstants.DEMO);
        } else {
            loginMsg.getData().putString(ServiceFunction.LOGIN_PLATFORM_TYPE, FXConstants.PRODUCT);
        }

        if (CompanySettings.newinterface) {
            if (IDLogin) {
                CommonFunction cf = new CommonFunction();
                cf.setKey(Utility.getINIPasswdKey());
                strID = setting.getString(FXConstants.SETTING_USER_ID, "");
                strPassword = cf.decryptText(setting.getString(FXConstants.SETTING_USER_PASSWORD, ""));
            }
            if (bSavePassword) {
                SharedPreferences.Editor editor = setting.edit();
                editor.putBoolean(FXConstants.SETTING_SAVE_LOGIN_INFO, bSavePassword);
                editor.putString(FXConstants.SETTING_USER_ID, etID.getEditableText().toString());
                CommonFunction cf = new CommonFunction();
                cf.setKey(Utility.getINIPasswdKey());
                if (!IDLogin && etPassword.getEditableText().toString() != null)
                    editor.putString(FXConstants.SETTING_USER_PASSWORD, cf.encryptText(etPassword.getEditableText().toString()));
                editor.putString("report_group", etRptgrp.getEditableText().toString());
                editor.commit();
            }
        }
        // williamto 08012020: [Tanrich] old and new layout uses the same flow for save password
        else {
            if (tbSavePassword.isChecked()) {
                SharedPreferences.Editor editor = setting.edit();
                editor.putBoolean(FXConstants.SETTING_SAVE_LOGIN_INFO, tbSavePassword.isChecked());
                editor.putString(FXConstants.SETTING_USER_ID, etID.getEditableText().toString());
                CommonFunction cf = new CommonFunction();
                cf.setKey(Utility.getINIPasswdKey());
                if (!IDLogin && etPassword.getEditableText().toString() != null)
                    editor.putString(FXConstants.SETTING_USER_PASSWORD, cf.encryptText(etPassword.getEditableText().toString()));
                editor.putString("report_group", etRptgrp.getEditableText().toString());
                editor.commit();
            }
        }

        loginMsg.getData().putString(ServiceFunction.LOGIN_ID, strID);
        loginMsg.getData().putString(ServiceFunction.LOGIN_PASSWORD, strPassword);
        if (!CompanySettings.ENABLE_FATCH_SERVER && !CompanySettings.FOR_TEST && !CompanySettings.ENABLE_FATCH_REPORT_GROUP_OTX) {
            if (demo)
                loginMsg.getData().putInt(ServiceFunction.LOGIN_CONN_INDEX, app.iTrialIndexDemo);
            else if (CompanySettings.checkProdServer() == 1)
                loginMsg.getData().putInt(ServiceFunction.LOGIN_CONN_INDEX, app.iTrialIndexProd);
            else if (CompanySettings.checkProdServer() == 2)
                loginMsg.getData().putInt(ServiceFunction.LOGIN_CONN_INDEX, app.iTrialIndexProd2);
            else if (CompanySettings.checkProdServer() == 3)
                loginMsg.getData().putInt(ServiceFunction.LOGIN_CONN_INDEX, app.iTrialIndexProd3);
            else if (CompanySettings.checkProdServer() == 4)
                loginMsg.getData().putInt(ServiceFunction.LOGIN_CONN_INDEX, app.iTrialIndexProd4);
            else if (CompanySettings.checkProdServer() == 5)
                loginMsg.getData().putInt(ServiceFunction.LOGIN_CONN_INDEX, app.iTrialIndexProd5);
            else if (CompanySettings.checkProdServer() == 6)
                loginMsg.getData().putInt(ServiceFunction.LOGIN_CONN_INDEX, app.iTrialIndexProd6);
            else if (CompanySettings.checkProdServer() == 7)
                loginMsg.getData().putInt(ServiceFunction.LOGIN_CONN_INDEX, app.iTrialIndexProd7);
            else if (CompanySettings.checkProdServer() == 8)
                loginMsg.getData().putInt(ServiceFunction.LOGIN_CONN_INDEX, app.iTrialIndexProd8);
            else if (CompanySettings.checkProdServer() == 9)
                loginMsg.getData().putInt(ServiceFunction.LOGIN_CONN_INDEX, app.iTrialIndexProd9);
            else if (CompanySettings.checkProdServer() == 10)
                loginMsg.getData().putInt(ServiceFunction.LOGIN_CONN_INDEX, app.iTrialIndexProd10);
        } else
            loginMsg.getData().putInt(ServiceFunction.LOGIN_CONN_INDEX, -1);

        try {
            //Save id, pwd for 2FA
            app.data.setTwoFAMessage(loginMsg);

            mService.send(loginMsg);
        } catch (RemoteException e) {
            Log.e(TAG, "Unable to send login message", e.fillInStackTrace());
        }
    }

    @Override
    public boolean showLogout() {
        return false;
    }

    @Override
    public boolean showTopNav() {
        if (CompanySettings.SHOW_DASHBOARD_BEFORE_LOGIN || CompanySettings.ENABLE_CONTENT || CompanySettings.ENABLE_CONTENT_WEB_VIEW)
            return true;
        else
            return false;
    }

    @Override
    public boolean showConnected() {
        return false;
    }

    @Override
    public boolean showPlatformType() {
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    private void pauseTips() {
        if (loginTimer != null)
            loginTimer.cancel();

        if (CompanySettings.newinterface && cancellationSignal != null) {
            cancellationSignal.cancel();
            cancellationSignal = null;
        }
    }

    public static void asyncfetch(final Activity activity, final String address, final OnFetchCompleteListener listener) {
        new Thread(new Runnable() {
            String result;

            @Override
            public void run() {

                try {
                    result = fetch(address);
                } catch (Exception e) {
                    e.printStackTrace();
                    result = "-1";
                }
                if (activity != null)
                    activity.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            listener.onSuccess(result);
                        }
                    });
                else
                    listener.onSuccess(result);
            }
        }).start();
    }

    public static interface OnFetchCompleteListener {
        void onSuccess(String result);
    }

    public static String fetch(String address) throws Exception {
        HttpClient client = new DefaultHttpClient();
        HttpParams params = client.getParams();
        HttpConnectionParams.setConnectionTimeout(params, 10000);
        HttpConnectionParams.setSoTimeout(params, 10000);

        HttpGet request = new HttpGet(address);
        HttpResponse response = client.execute(request);

        //String html = "";
        InputStream in = response.getEntity().getContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder str = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            str.append(line);
        }
        in.close();
        return str.toString();
    }

    @Override
    public void onBackPressed() {
        if (!CompanySettings.SHOW_DASHBOARD_BEFORE_LOGIN) {
            //super.onBackPressed();
            //System.out.println("Back");
            new AlertDialog.Builder(LoginActivity.this, CompanySettings.alertDialogTheme)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(res.getString(R.string.title_information))
                    .setMessage(res.getString(R.string.msg_quit))
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (getNextLocale() != null) {
                                setLanguage(getNextLocale());
                                setNextLocale(null);
                            }
                            app.bQuit = true;
                            finish();
                        }

                    })
                    .setNegativeButton(R.string.no, null)
                    .show();
        } else {
            goTo(ServiceFunction.SRV_DASHBOARD);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (app.bQuit && (getParent() == null)) {
            Intent intent = new Intent(this, FxMobileTraderService.class);
            stopService(intent);
        }
    }

    public boolean checkNetwork() {
        boolean bOK = false;
        int iCount = 10;
        while (!bOK && iCount > 0) {

            if (isNetworkAvailable()) {
                if (isServerAvailable()) {
                    bOK = true;
                    //System.out.println("OK");
                } else {
                    //System.out.println("no server");
                }
            } else {
                //System.out.println("no network");
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            iCount--;
        }
        if (!bOK) {
            if (CompanySettings.ENABLE_FATCH_SERVER || CompanySettings.FOR_TEST || CompanySettings.ENABLE_FATCH_REPORT_GROUP_OTX) {
                Bundle bundle = new Bundle();
                bundle.putString(ServiceFunction.MESSAGE, (String) res.getText(R.string.msg_network));
                goTo(ServiceFunction.SRV_SHOW_TOAST, bundle);
                app.isLoading = false;
            } else {
                if (loginTimer != null)
                    loginTimer.cancel();
                loginTimer = new Timer();
                loginTimer.schedule(new Task(), 0);
            }
        }
        return bOK;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public boolean isServerAvailable() {
        if (!CompanySettings.newinterface && tbPlatformType.isChecked()) {
            if (CompanySettings.ENABLE_FATCH_SERVER || CompanySettings.FOR_TEST == true || CompanySettings.ENABLE_FATCH_REPORT_GROUP_OTX)
                return isServerAvailable(app.loginInfoDemo.sURL, Utility.toInteger(app.loginInfoDemo.sPort, 15000));
            else
                return isServerAvailable(app.alLoginInfoDemo.get(app.iTrialIndexDemo).sURL, Utility.toInteger(app.loginInfoDemo.sPort, 15000));
        } else {
            if (CompanySettings.ENABLE_FATCH_SERVER || CompanySettings.FOR_TEST == true || CompanySettings.ENABLE_FATCH_REPORT_GROUP_OTX)
                return isServerAvailable(app.loginInfoProd.sURL, Utility.toInteger(app.loginInfoProd.sPort, 15000)) && (app.loginInfoProd2 == null || isServerAvailable(app.loginInfoProd2.sURL, Utility.toInteger(app.loginInfoProd2.sPort, 15000)));
            else {
                if (CompanySettings.checkProdServer() == 1)
                    return isServerAvailable(app.alLoginInfoProd.get(app.iTrialIndexProd).sURL, Utility.toInteger(app.alLoginInfoProd.get(app.iTrialIndexProd).sPort, 15000));
                else if (CompanySettings.checkProdServer() == 2)
                    return isServerAvailable(app.alLoginInfoProd2.get(app.iTrialIndexProd2).sURL, Utility.toInteger(app.alLoginInfoProd2.get(app.iTrialIndexProd2).sPort, 15000));
                else if (CompanySettings.checkProdServer() == 3)
                    return isServerAvailable(app.alLoginInfoProd3.get(app.iTrialIndexProd3).sURL, Utility.toInteger(app.alLoginInfoProd3.get(app.iTrialIndexProd3).sPort, 15000));
                else if (CompanySettings.checkProdServer() == 4)
                    return isServerAvailable(app.alLoginInfoProd4.get(app.iTrialIndexProd4).sURL, Utility.toInteger(app.alLoginInfoProd4.get(app.iTrialIndexProd4).sPort, 15000));
                else if (CompanySettings.checkProdServer() == 5)
                    return isServerAvailable(app.alLoginInfoProd5.get(app.iTrialIndexProd5).sURL, Utility.toInteger(app.alLoginInfoProd5.get(app.iTrialIndexProd5).sPort, 15000));
                else if (CompanySettings.checkProdServer() == 6)
                    return isServerAvailable(app.alLoginInfoProd6.get(app.iTrialIndexProd6).sURL, Utility.toInteger(app.alLoginInfoProd6.get(app.iTrialIndexProd6).sPort, 15000));
                else if (CompanySettings.checkProdServer() == 7)
                    return isServerAvailable(app.alLoginInfoProd7.get(app.iTrialIndexProd7).sURL, Utility.toInteger(app.alLoginInfoProd7.get(app.iTrialIndexProd7).sPort, 15000));
                else if (CompanySettings.checkProdServer() == 8)
                    return isServerAvailable(app.alLoginInfoProd8.get(app.iTrialIndexProd8).sURL, Utility.toInteger(app.alLoginInfoProd8.get(app.iTrialIndexProd8).sPort, 15000));
                else if (CompanySettings.checkProdServer() == 9)
                    return isServerAvailable(app.alLoginInfoProd9.get(app.iTrialIndexProd9).sURL, Utility.toInteger(app.alLoginInfoProd9.get(app.iTrialIndexProd9).sPort, 15000));
                else
                    return isServerAvailable(app.alLoginInfoProd10.get(app.iTrialIndexProd10).sURL, Utility.toInteger(app.alLoginInfoProd10.get(app.iTrialIndexProd10).sPort, 15000));
            }
        }
    }

    public boolean isServerAvailable(String host, int port) {

        Socket server = null;
        try {
            server = new Socket();
            InetSocketAddress address = new InetSocketAddress(host, port);
            server.connect(address, 2000);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (server != null)
                try {
                    server.close();
                } catch (IOException e) {
                }
        }
        return false;
    }

    @Override
    public boolean isLoginActivity() {
        return true;
    }

    public void displayForgetPassword() {
        boolean bWebView = false;
        boolean isDemo = false;

        if (!CompanySettings.newinterface)
            isDemo = tbPlatformType.isChecked();
        else {
            if (label_demo.getCurrentTextColor() == (res.getColor(R.color.theme_text)))
                isDemo = true;
        }

        if (CompanySettings.ENABLE_WEBVIEW_LOST_PWD == true) {
            if (getLanguage() == Locale.ENGLISH && !isDemo && app.hmLostPwdUrls.containsKey("PROD_EN")) {
                app.isDemoPlatform = false;
                bWebView = true;
            } else if (getLanguage() == Locale.SIMPLIFIED_CHINESE && !isDemo && app.hmLostPwdUrls.containsKey("PROD_SC")) {
                app.isDemoPlatform = false;
                bWebView = true;
            } else if (getLanguage() == Locale.TRADITIONAL_CHINESE && !isDemo && app.hmLostPwdUrls.containsKey("PROD_TC")) {
                app.isDemoPlatform = false;
                bWebView = true;
            } else if (getLanguage() == Locale.ENGLISH && isDemo && app.hmLostPwdUrls.containsKey("DEMO_EN")) {
                app.isDemoPlatform = true;
                bWebView = true;
            } else if (getLanguage() == Locale.SIMPLIFIED_CHINESE && isDemo && app.hmLostPwdUrls.containsKey("DEMO_SC")) {
                app.isDemoPlatform = true;
                bWebView = true;
            } else if (getLanguage() == Locale.TRADITIONAL_CHINESE && isDemo && app.hmLostPwdUrls.containsKey("DEMO_TC")) {
                app.isDemoPlatform = true;
                bWebView = true;
            }
        }

        if (bWebView == true) {
            if ((CompanySettings.PROD2_LICENSE_KEY != null ||
                    CompanySettings.PROD3_LICENSE_KEY != null ||
                    CompanySettings.PROD4_LICENSE_KEY != null ||
                    CompanySettings.PROD5_LICENSE_KEY != null ||
                    CompanySettings.PROD6_LICENSE_KEY != null ||
                    CompanySettings.PROD7_LICENSE_KEY != null ||
                    CompanySettings.PROD8_LICENSE_KEY != null ||
                    CompanySettings.PROD9_LICENSE_KEY != null ||
                    CompanySettings.PROD10_LICENSE_KEY != null) && (!CompanySettings.newinterface && tbPlatformType.isChecked() == false)) {
                if ("".equals(etID.getEditableText().toString())) {
                    Bundle bundle = new Bundle();
                    bundle.putString(ServiceFunction.MESSAGE, (String) res.getText(R.string.msg_empty_username));
                    goTo(ServiceFunction.SRV_SHOW_TOAST, bundle);
                    return;
                } else {
                    DataRepository.getInstance().setStrUser(etID.getEditableText().toString());
                }
            }
            goTo(ServiceFunction.SRV_MOVE_TO_LOST_PASSWORD);
        } else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    popForgot.showLikeQuickAction();
                }
            });
        }
    }
/*
http://218.213.79.103/uat/lostpwd.asp?lang=tw

       if (m_currentLocale==FXConstants.TRAD_CHINESE)
            strURL.append("tw");
        else if (m_currentLocale == FXConstants.SIMP_CHINESE)
            strURL.append("cn");
        else if (m_currentLocale == FXConstants.JAPANESE)
            strURL.append("jp");
        else
            strURL.append("en");
	
 */

}
