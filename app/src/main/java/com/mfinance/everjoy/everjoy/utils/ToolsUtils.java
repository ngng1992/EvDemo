package com.mfinance.everjoy.everjoy.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.blankj.utilcode.util.RegexUtils;
import com.mfinance.everjoy.BuildConfig;
import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.CompanySettings;
import com.mfinance.everjoy.app.MobileTraderApplication;
import com.mfinance.everjoy.app.util.AppLauncherLib;
import com.mfinance.everjoy.app.util.Utility;
import com.mfinance.everjoy.everjoy.config.Constants;

import net.mfinance.commonlib.toast.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;


/**
 * 通用的一些方法
 */
public class ToolsUtils {

    /**
     * 是否打开了通知栏权限
     */
    public static boolean isNotificationEnabled(Context context) {
        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        // areNotificationsEnabled方法的有效性官方只最低支持到API 19，低于19的仍可调用此方法不过只会返回true，即默认为用户已经开启了通知。
        return manager.areNotificationsEnabled();
    }

    /**
     * RecyclerView添加item分割线，这种方式如添加了底部view和空view，底部会有一个分割线
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    public static void setRecyclerViewItemDecoration(Context context, RecyclerView recyclerView) {
        DividerItemDecoration decor = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        decor.setDrawable(context.getResources().getDrawable(R.drawable.line_grayde_05dp));
        recyclerView.addItemDecoration(decor);
    }

    /**
     * 设置SwipeRefreshLayout的颜色
     */
    public static void setSwipeRefreshLayoutColor(Context context, SwipeRefreshLayout swipeRefreshLayout) {
        swipeRefreshLayout.setColorSchemeColors(context.getResources().getColor(R.color.blue03),
                context.getResources().getColor(R.color.blue03),
                context.getResources().getColor(R.color.blue03),
                context.getResources().getColor(R.color.blue03));
    }

    /**
     * 复制到剪贴板
     */
    public static void copyText(Context context, String text) {
        // 获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", text);
        // 将ClipData内容放到系统剪贴板里。
        if (cm != null) {
            cm.setPrimaryClip(mClipData);
        }
        ToastUtils.showToast(context, R.string.toast_copied);
    }

    /**
     * 检测3次是否有网络
     */
    public static boolean checkNetwork(Context context) {
        boolean bOK = false;
        int iCount = 3;
        while (!bOK && iCount > 0) {
            if (isNetworkAvailable(context)) {
                bOK = true;
            }
            try {
                // 停留500ms再做检测
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            iCount--;
        }
        return bOK;
    }

    /**
     * 检查网络是否有效
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    /**
     * 检测applaucher是否有效
     */
    public static boolean isAppLaucherURLAvailable() {
        boolean appLaucherURL1OK = isServerAvailable(MobileTraderApplication.APP_LAUNCHER_URL_DOMAIN_1, 2083);
        boolean appLaucherURL2OK = isServerAvailable(MobileTraderApplication.APP_LAUNCHER_URL_DOMAIN_2, 2083);
        return appLaucherURL1OK || appLaucherURL2OK;
    }


    /**
     * 检查连接的服务是否有效
     *
     * @param host ip
     * @param port 端口
     */
    public static boolean isServerAvailable(String host, int port) {
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
                    e.printStackTrace();
                }
        }
        return false;
    }

    /**
     * 检测服务是否有效
     */
    public static boolean isServerAvailable(MobileTraderApplication app) {
        if (CompanySettings.FOR_TEST) {
            app.loginInfoDemo = new MobileTraderApplication.LoginInfo(CompanySettings.loginInfoTest.sURL, CompanySettings.loginInfoTest.sPort);
            app.loginInfoDemoOrg = new MobileTraderApplication.LoginInfo(CompanySettings.loginInfoTest.sURL, CompanySettings.loginInfoTest.sPort);
            app.loginInfoProd = new MobileTraderApplication.LoginInfo(CompanySettings.loginInfoTest.sURL, CompanySettings.loginInfoTest.sPort);
            app.loginInfoProd2 = new MobileTraderApplication.LoginInfo(CompanySettings.loginInfoTest.sURL, CompanySettings.loginInfoTest.sPort);
            if (BuildConfig.DEBUG)
                Log.e("loginTestURLOK", app.loginInfoDemo.sURL + ":" + app.loginInfoDemo.sPort);
            return true;
        }
        if (!CompanySettings.CHECK_SERVER_AVALIABLE) {
            if (app.alLoginInfoDemo.size() > 0) {
                app.loginInfoDemo = app.alLoginInfoDemo.get(0);
                app.loginInfoDemoOrg = new MobileTraderApplication.LoginInfo(app.alLoginInfoDemo.get(0).sURL, app.alLoginInfoDemo.get(0).sPort);
            }
            if (app.alLoginInfoProd.size() > 0)
                app.loginInfoProd = app.alLoginInfoProd.get(0);
            if (app.alLoginInfoProd2.size() > 0)
                app.loginInfoProd2 = app.alLoginInfoProd2.get(0);
            return true;
        }
        boolean loginDemoURLOK = false;
        boolean loginProdURLOK = false;
        boolean loginProd2URLOK = false;
        for (int i = 0; i < app.alLoginInfoDemo.size(); i++) {
            if (isServerAvailable(app.alLoginInfoDemo.get(i).sURL, Utility.toInteger(app.alLoginInfoDemo.get(i).sPort, 15000))) {
                if (BuildConfig.DEBUG)
                    Log.e("loginDemoURLOK", app.alLoginInfoDemo.get(i).sURL + ":" + app.alLoginInfoDemo.get(i).sPort);
                loginDemoURLOK = true;
                app.loginInfoDemo = app.alLoginInfoDemo.get(i);
                app.loginInfoDemoOrg = new MobileTraderApplication.LoginInfo(app.alLoginInfoDemo.get(i).sURL, app.alLoginInfoDemo.get(i).sPort);
            }
        }

        for (int i = 0; i < app.alLoginInfoProd.size(); i++) {
            if (isServerAvailable(app.alLoginInfoProd.get(i).sURL, Utility.toInteger(app.alLoginInfoProd.get(i).sPort, 15000))) {
                if (BuildConfig.DEBUG)
                    Log.e("loginProdURLOK", app.alLoginInfoProd.get(i).sURL + ":" + app.alLoginInfoProd.get(i).sPort);
                loginProdURLOK = true;
                app.loginInfoProd = app.alLoginInfoProd.get(i);
            }
        }

        if (app.alLoginInfoProd2.size() == 0)
            loginProd2URLOK = true;
        else
            for (int i = 0; i < app.alLoginInfoProd2.size(); i++) {
                if (isServerAvailable(app.alLoginInfoProd2.get(i).sURL, Utility.toInteger(app.alLoginInfoProd2.get(i).sPort, 15000))) {
                    if (BuildConfig.DEBUG)
                        Log.e("loginProd2URLOK", app.alLoginInfoProd2.get(i).sURL + ":" + app.alLoginInfoProd2.get(i).sPort);
                    loginProd2URLOK = true;
                    app.loginInfoProd2 = app.alLoginInfoProd2.get(i);
                }
            }

        return (loginDemoURLOK && loginProdURLOK && loginProd2URLOK);
    }

    /**
     * 检查版本
     *
     * @param app 全局的app
     */
    public static boolean checkVersionOK(MobileTraderApplication app) {
        if (CompanySettings.FOR_TEST && !CompanySettings.CHECK_APPLAUNCHER_IN_TEST)
            return true;

        boolean appLaucherURL1OK = isServerAvailable(Constants.APP_LAUNCHER_URL_DOMAIN_1, 2083);
        boolean appLaucherURL2OK = isServerAvailable(Constants.APP_LAUNCHER_URL_DOMAIN_2, 2083);

        String result_demo = "";
        String result_prod = "";
        String result_prod2 = "";
        String result_prod3 = "";
        String result_prod4 = "";
        String result_prod5 = "";
        String result_prod6 = "";
        String result_prod7 = "";
        String result_prod8 = "";
        String result_prod9 = "";
        String result_prod10 = "";
        try {
            if (CompanySettings.ENABLE_WEBVIEW_MASTER && CompanySettings.MASTER_KEYS != null) {
                String result_en, result_sc, result_tc;
                result_en = AppLauncherLib.getSetting(CompanySettings.MASTER_KEYS.get("EN"));
                result_sc = AppLauncherLib.getSetting(CompanySettings.MASTER_KEYS.get("SC"));
                result_tc = AppLauncherLib.getSetting(CompanySettings.MASTER_KEYS.get("TC"));
                app.hmMasterUrls = new HashMap<String, String>();
                app.hmMasterUrls.put("EN", result_en);
                app.hmMasterUrls.put("SC", result_sc);
                app.hmMasterUrls.put("TC", result_tc);
            }
            if (CompanySettings.ENABLE_WEBVIEW_CONTACT_US && CompanySettings.CONTACT_US_KEYS != null) {
                String result_en, result_sc, result_tc;
                result_en = AppLauncherLib.getSetting(CompanySettings.CONTACT_US_KEYS.get("EN"));
                result_sc = AppLauncherLib.getSetting(CompanySettings.CONTACT_US_KEYS.get("SC"));
                result_tc = AppLauncherLib.getSetting(CompanySettings.CONTACT_US_KEYS.get("TC"));
                app.hmContactUSUrls = new HashMap<String, String>();
                app.hmContactUSUrls.put("EN", result_en);
                app.hmContactUSUrls.put("SC", result_sc);
                app.hmContactUSUrls.put("TC", result_tc);
            }
            if (CompanySettings.ENABLE_WEBVIEW_DEMO_REGISTRATION && CompanySettings.DEMO_REGISTRATION_KEYS != null) {
                String result_en, result_sc, result_tc;
                result_en = AppLauncherLib.getSetting(CompanySettings.DEMO_REGISTRATION_KEYS.get("EN"));
                result_sc = AppLauncherLib.getSetting(CompanySettings.DEMO_REGISTRATION_KEYS.get("SC"));
                result_tc = AppLauncherLib.getSetting(CompanySettings.DEMO_REGISTRATION_KEYS.get("TC"));

                app.hmDemoRegistrationUrls = new HashMap<String, String>();
                app.hmDemoRegistrationUrls.put("EN", result_en);
                app.hmDemoRegistrationUrls.put("SC", result_sc);
                app.hmDemoRegistrationUrls.put("TC", result_tc);
            }
            if (CompanySettings.ENABLE_WEBVIEW_LOST_PWD && CompanySettings.LOST_PWD_KEYS != null) {
                app.hmLostPwdUrls = new HashMap<String, String>();
                String result_en, result_sc, result_tc;
                result_en = AppLauncherLib.getSetting(CompanySettings.LOST_PWD_KEYS.get("PROD_EN"));
                result_sc = AppLauncherLib.getSetting(CompanySettings.LOST_PWD_KEYS.get("PROD_SC"));
                result_tc = AppLauncherLib.getSetting(CompanySettings.LOST_PWD_KEYS.get("PROD_TC"));

                app.hmLostPwdUrls.put("PROD_EN", result_en);
                app.hmLostPwdUrls.put("PROD_SC", result_sc);
                app.hmLostPwdUrls.put("PROD_TC", result_tc);

                if (CompanySettings.LOST_PWD_KEYS.get("PROD2_EN") != null) {
                    result_en = AppLauncherLib.getSetting(CompanySettings.LOST_PWD_KEYS.get("PROD2_EN"));
                    result_sc = AppLauncherLib.getSetting(CompanySettings.LOST_PWD_KEYS.get("PROD2_SC"));
                    result_tc = AppLauncherLib.getSetting(CompanySettings.LOST_PWD_KEYS.get("PROD2_TC"));
                    app.hmLostPwdUrls.put("PROD2_EN", result_en);
                    app.hmLostPwdUrls.put("PROD2_SC", result_sc);
                    app.hmLostPwdUrls.put("PROD2_TC", result_tc);
                }

                if (CompanySettings.LOST_PWD_KEYS.get("PROD3_EN") != null) {
                    result_en = AppLauncherLib.getSetting(CompanySettings.LOST_PWD_KEYS.get("PROD3_EN"));
                    result_sc = AppLauncherLib.getSetting(CompanySettings.LOST_PWD_KEYS.get("PROD3_SC"));
                    result_tc = AppLauncherLib.getSetting(CompanySettings.LOST_PWD_KEYS.get("PROD3_TC"));
                    app.hmLostPwdUrls.put("PROD3_EN", result_en);
                    app.hmLostPwdUrls.put("PROD3_SC", result_sc);
                    app.hmLostPwdUrls.put("PROD3_TC", result_tc);
                }

                if (CompanySettings.LOST_PWD_KEYS.get("PROD4_EN") != null) {
                    result_en = AppLauncherLib.getSetting(CompanySettings.LOST_PWD_KEYS.get("PROD4_EN"));
                    result_sc = AppLauncherLib.getSetting(CompanySettings.LOST_PWD_KEYS.get("PROD4_SC"));
                    result_tc = AppLauncherLib.getSetting(CompanySettings.LOST_PWD_KEYS.get("PROD4_TC"));
                    app.hmLostPwdUrls.put("PROD4_EN", result_en);
                    app.hmLostPwdUrls.put("PROD4_SC", result_sc);
                    app.hmLostPwdUrls.put("PROD4_TC", result_tc);
                }

                if (CompanySettings.LOST_PWD_KEYS.get("PROD5_EN") != null) {
                    result_en = AppLauncherLib.getSetting(CompanySettings.LOST_PWD_KEYS.get("PROD5_EN"));
                    result_sc = AppLauncherLib.getSetting(CompanySettings.LOST_PWD_KEYS.get("PROD5_SC"));
                    result_tc = AppLauncherLib.getSetting(CompanySettings.LOST_PWD_KEYS.get("PROD5_TC"));
                    app.hmLostPwdUrls.put("PROD5_EN", result_en);
                    app.hmLostPwdUrls.put("PROD5_SC", result_sc);
                    app.hmLostPwdUrls.put("PROD5_TC", result_tc);
                }

                if (CompanySettings.LOST_PWD_KEYS.get("PROD6_EN") != null) {
                    result_en = AppLauncherLib.getSetting(CompanySettings.LOST_PWD_KEYS.get("PROD6_EN"));
                    result_sc = AppLauncherLib.getSetting(CompanySettings.LOST_PWD_KEYS.get("PROD6_SC"));
                    result_tc = AppLauncherLib.getSetting(CompanySettings.LOST_PWD_KEYS.get("PROD6_TC"));
                    app.hmLostPwdUrls.put("PROD6_EN", result_en);
                    app.hmLostPwdUrls.put("PROD6_SC", result_sc);
                    app.hmLostPwdUrls.put("PROD6_TC", result_tc);
                }

                if (CompanySettings.LOST_PWD_KEYS.get("PROD7_EN") != null) {
                    result_en = AppLauncherLib.getSetting(CompanySettings.LOST_PWD_KEYS.get("PROD7_EN"));
                    result_sc = AppLauncherLib.getSetting(CompanySettings.LOST_PWD_KEYS.get("PROD7_SC"));
                    result_tc = AppLauncherLib.getSetting(CompanySettings.LOST_PWD_KEYS.get("PROD7_TC"));
                    app.hmLostPwdUrls.put("PROD7_EN", result_en);
                    app.hmLostPwdUrls.put("PROD7_SC", result_sc);
                    app.hmLostPwdUrls.put("PROD7_TC", result_tc);
                }

                if (CompanySettings.LOST_PWD_KEYS.get("PROD8_EN") != null) {
                    result_en = AppLauncherLib.getSetting(CompanySettings.LOST_PWD_KEYS.get("PROD8_EN"));
                    result_sc = AppLauncherLib.getSetting(CompanySettings.LOST_PWD_KEYS.get("PROD8_SC"));
                    result_tc = AppLauncherLib.getSetting(CompanySettings.LOST_PWD_KEYS.get("PROD8_TC"));
                    app.hmLostPwdUrls.put("PROD8_EN", result_en);
                    app.hmLostPwdUrls.put("PROD8_SC", result_sc);
                    app.hmLostPwdUrls.put("PROD8_TC", result_tc);
                }

                if (CompanySettings.LOST_PWD_KEYS.get("PROD9_EN") != null) {
                    result_en = AppLauncherLib.getSetting(CompanySettings.LOST_PWD_KEYS.get("PROD9_EN"));
                    result_sc = AppLauncherLib.getSetting(CompanySettings.LOST_PWD_KEYS.get("PROD9_SC"));
                    result_tc = AppLauncherLib.getSetting(CompanySettings.LOST_PWD_KEYS.get("PROD9_TC"));
                    app.hmLostPwdUrls.put("PROD9_EN", result_en);
                    app.hmLostPwdUrls.put("PROD9_SC", result_sc);
                    app.hmLostPwdUrls.put("PROD9_TC", result_tc);
                }

                if (CompanySettings.LOST_PWD_KEYS.get("PROD10_EN") != null) {
                    result_en = AppLauncherLib.getSetting(CompanySettings.LOST_PWD_KEYS.get("PROD10_EN"));
                    result_sc = AppLauncherLib.getSetting(CompanySettings.LOST_PWD_KEYS.get("PROD10_SC"));
                    result_tc = AppLauncherLib.getSetting(CompanySettings.LOST_PWD_KEYS.get("PROD10_TC"));
                    app.hmLostPwdUrls.put("PROD10_EN", result_en);
                    app.hmLostPwdUrls.put("PROD10_SC", result_sc);
                    app.hmLostPwdUrls.put("PROD10_TC", result_tc);
                }

                result_en = AppLauncherLib.getSetting(CompanySettings.LOST_PWD_KEYS.get("DEMO_EN"));
                result_sc = AppLauncherLib.getSetting(CompanySettings.LOST_PWD_KEYS.get("DEMO_SC"));
                result_tc = AppLauncherLib.getSetting(CompanySettings.LOST_PWD_KEYS.get("DEMO_TC"));

                app.hmLostPwdUrls.put("DEMO_EN", result_en);
                app.hmLostPwdUrls.put("DEMO_SC", result_sc);
                app.hmLostPwdUrls.put("DEMO_TC", result_tc);
            }

            if (CompanySettings.ECONOMIC_IND_HTTP_KEY != null) {
                try {
                    app.economicUri = Uri.parse(AppLauncherLib.getSetting(CompanySettings.ECONOMIC_IND_HTTP_KEY));
                    app.economicUriSC = Uri.parse(AppLauncherLib.getSetting(CompanySettings.ECONOMIC_IND_HTTP_KEY_SC));
                    app.economicUriTC = Uri.parse(AppLauncherLib.getSetting(CompanySettings.ECONOMIC_IND_HTTP_KEY_TC));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            if (CompanySettings.NEWS_HTTP_KEY != null) {
                try {
                    app.newsUri = Uri.parse(AppLauncherLib.getSetting(CompanySettings.NEWS_HTTP_KEY));
                    app.newsUriSC = Uri.parse(AppLauncherLib.getSetting(CompanySettings.NEWS_HTTP_KEY_SC));
                    app.newsUriTC = Uri.parse(AppLauncherLib.getSetting(CompanySettings.NEWS_HTTP_KEY_TC));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            if (CompanySettings.CONTACT_US_HTTP_KEY != null) {
                try {
                    app.contactUsUri = Uri.parse(AppLauncherLib.getSetting(CompanySettings.CONTACT_US_HTTP_KEY));
                    app.contactUsUriSC = Uri.parse(AppLauncherLib.getSetting(CompanySettings.CONTACT_US_HTTP_KEY_SC));
                    app.contactUsUriTC = Uri.parse(AppLauncherLib.getSetting(CompanySettings.CONTACT_US_HTTP_KEY_TC));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            if (CompanySettings.TERMS_HTTP_KEY != null) {
                try {
                    app.termsUri = Uri.parse(AppLauncherLib.getSetting(CompanySettings.TERMS_HTTP_KEY));
                    app.termsUriSC = Uri.parse(AppLauncherLib.getSetting(CompanySettings.TERMS_HTTP_KEY_SC));
                    app.termsUriTC = Uri.parse(AppLauncherLib.getSetting(CompanySettings.TERMS_HTTP_KEY_TC));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            if (CompanySettings.ANNOUNCEMENT_HTTP_KEY != null) {
                try {
                    app.announcementUri = Uri.parse(AppLauncherLib.getSetting(CompanySettings.ANNOUNCEMENT_HTTP_KEY));
                    app.announcementUriSC = Uri.parse(AppLauncherLib.getSetting(CompanySettings.ANNOUNCEMENT_HTTP_KEY_SC));
                    app.announcementUriTC = Uri.parse(AppLauncherLib.getSetting(CompanySettings.ANNOUNCEMENT_HTTP_KEY_TC));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            if (CompanySettings.CHART_SERVER_URL_KEY != null) {
                try {
                    String setting = AppLauncherLib.getSetting(CompanySettings.CHART_SERVER_URL_KEY);
                    if (setting != null) {
                        String[] split = setting.split(":");
                        if (split.length >= 3) {
                            String domain = split[0];
                            int chartDataPort = Integer.parseInt(split[1]);
                            int realTimePort = Integer.parseInt(split[2]);
                            app.chartDomain = domain;
                            app.CHART_PORT = chartDataPort;
                            app.REALTIME_CHART_PORT = realTimePort;
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }


            if (CompanySettings.IDENTITY_CHECK_KEYS != null) {
                app.hmIdentityCheckUrls = new HashMap<String, String>();
                result_prod = AppLauncherLib.getSetting(CompanySettings.IDENTITY_CHECK_KEYS.get("IDENTITY_CHECK"));
                app.hmIdentityCheckUrls.put("IDENTITY_CHECK", result_prod);
                result_prod = AppLauncherLib.getSetting(CompanySettings.IDENTITY_CHECK_KEYS.get("IDENTITY_CHECK_SUBMIT"));
                app.hmIdentityCheckUrls.put("IDENTITY_CHECK_SUBMIT", result_prod);
            }

            if (appLaucherURL1OK) {
                result_demo = AppLauncherLib.getMetaData(Utility.getKey(CompanySettings.DEMO_LICENSE_KEY), app.getPackageManager().getPackageInfo(app.getPackageName(), 0).versionName, MobileTraderApplication.APP_LAUNCHER_URL_1, CompanySettings.APP_UPDATE_URL_TYPE);
                result_prod = AppLauncherLib.getMetaData(Utility.getKey(CompanySettings.PROD_LICENSE_KEY), app.getPackageManager().getPackageInfo(app.getPackageName(), 0).versionName, MobileTraderApplication.APP_LAUNCHER_URL_1, CompanySettings.APP_UPDATE_URL_TYPE);
                result_prod2 = CompanySettings.PROD2_LICENSE_KEY != null ? AppLauncherLib.getMetaData(CompanySettings.PROD2_LICENSE_KEY, app.getPackageManager().getPackageInfo(app.getPackageName(), 0).versionName, MobileTraderApplication.APP_LAUNCHER_URL_1, CompanySettings.APP_UPDATE_URL_TYPE) : "";
                result_prod3 = CompanySettings.PROD3_LICENSE_KEY != null ? AppLauncherLib.getMetaData(CompanySettings.PROD3_LICENSE_KEY, app.getPackageManager().getPackageInfo(app.getPackageName(), 0).versionName, MobileTraderApplication.APP_LAUNCHER_URL_1, CompanySettings.APP_UPDATE_URL_TYPE) : "";
                result_prod4 = CompanySettings.PROD4_LICENSE_KEY != null ? AppLauncherLib.getMetaData(CompanySettings.PROD4_LICENSE_KEY, app.getPackageManager().getPackageInfo(app.getPackageName(), 0).versionName, MobileTraderApplication.APP_LAUNCHER_URL_1, CompanySettings.APP_UPDATE_URL_TYPE) : "";
                result_prod5 = CompanySettings.PROD5_LICENSE_KEY != null ? AppLauncherLib.getMetaData(CompanySettings.PROD5_LICENSE_KEY, app.getPackageManager().getPackageInfo(app.getPackageName(), 0).versionName, MobileTraderApplication.APP_LAUNCHER_URL_1, CompanySettings.APP_UPDATE_URL_TYPE) : "";
                result_prod6 = CompanySettings.PROD6_LICENSE_KEY != null ? AppLauncherLib.getMetaData(CompanySettings.PROD6_LICENSE_KEY, app.getPackageManager().getPackageInfo(app.getPackageName(), 0).versionName, MobileTraderApplication.APP_LAUNCHER_URL_1, CompanySettings.APP_UPDATE_URL_TYPE) : "";
                result_prod7 = CompanySettings.PROD7_LICENSE_KEY != null ? AppLauncherLib.getMetaData(CompanySettings.PROD7_LICENSE_KEY, app.getPackageManager().getPackageInfo(app.getPackageName(), 0).versionName, MobileTraderApplication.APP_LAUNCHER_URL_1, CompanySettings.APP_UPDATE_URL_TYPE) : "";
                result_prod8 = CompanySettings.PROD8_LICENSE_KEY != null ? AppLauncherLib.getMetaData(CompanySettings.PROD8_LICENSE_KEY, app.getPackageManager().getPackageInfo(app.getPackageName(), 0).versionName, MobileTraderApplication.APP_LAUNCHER_URL_1, CompanySettings.APP_UPDATE_URL_TYPE) : "";
                result_prod9 = CompanySettings.PROD9_LICENSE_KEY != null ? AppLauncherLib.getMetaData(CompanySettings.PROD9_LICENSE_KEY, app.getPackageManager().getPackageInfo(app.getPackageName(), 0).versionName, MobileTraderApplication.APP_LAUNCHER_URL_1, CompanySettings.APP_UPDATE_URL_TYPE) : "";
                result_prod10 = CompanySettings.PROD10_LICENSE_KEY != null ? AppLauncherLib.getMetaData(CompanySettings.PROD10_LICENSE_KEY, app.getPackageManager().getPackageInfo(app.getPackageName(), 0).versionName, MobileTraderApplication.APP_LAUNCHER_URL_1, CompanySettings.APP_UPDATE_URL_TYPE) : "";
                if (!CompanySettings.FOR_TEST) {
                    app.convertJsonToArrayDemo(result_demo);
                    app.convertJsonToArrayProd(result_prod);
                    if (!result_prod2.equals("")) app.convertJsonToArrayProd2(result_prod2);
                    if (!result_prod3.equals("")) app.convertJsonToArrayProd3(result_prod3);
                    if (!result_prod4.equals("")) app.convertJsonToArrayProd4(result_prod4);
                    if (!result_prod5.equals("")) app.convertJsonToArrayProd5(result_prod5);
                    if (!result_prod6.equals("")) app.convertJsonToArrayProd6(result_prod6);
                    if (!result_prod7.equals("")) app.convertJsonToArrayProd7(result_prod7);
                    if (!result_prod8.equals("")) app.convertJsonToArrayProd8(result_prod8);
                    if (!result_prod9.equals("")) app.convertJsonToArrayProd9(result_prod9);
                    if (!result_prod10.equals("")) app.convertJsonToArrayProd10(result_prod10);
                }
            }
            if (appLaucherURL2OK) {
                if ("".equals(result_demo))
                    result_demo = AppLauncherLib.getMetaData(Utility.getKey(CompanySettings.DEMO_LICENSE_KEY), app.getPackageManager().getPackageInfo(app.getPackageName(), 0).versionName, MobileTraderApplication.APP_LAUNCHER_URL_2, CompanySettings.APP_UPDATE_URL_TYPE);
                if ("".equals(result_prod))
                    result_prod = AppLauncherLib.getMetaData(Utility.getKey(CompanySettings.PROD_LICENSE_KEY), app.getPackageManager().getPackageInfo(app.getPackageName(), 0).versionName, MobileTraderApplication.APP_LAUNCHER_URL_2, CompanySettings.APP_UPDATE_URL_TYPE);
                if ("".equals(result_prod2))
                    result_prod2 = CompanySettings.PROD2_LICENSE_KEY != null ? AppLauncherLib.getMetaData(CompanySettings.PROD2_LICENSE_KEY, app.getPackageManager().getPackageInfo(app.getPackageName(), 0).versionName, MobileTraderApplication.APP_LAUNCHER_URL_2, CompanySettings.APP_UPDATE_URL_TYPE) : "";
                if ("".equals(result_prod3))
                    result_prod3 = CompanySettings.PROD3_LICENSE_KEY != null ? AppLauncherLib.getMetaData(CompanySettings.PROD3_LICENSE_KEY, app.getPackageManager().getPackageInfo(app.getPackageName(), 0).versionName, MobileTraderApplication.APP_LAUNCHER_URL_2, CompanySettings.APP_UPDATE_URL_TYPE) : "";
                if ("".equals(result_prod4))
                    result_prod4 = CompanySettings.PROD4_LICENSE_KEY != null ? AppLauncherLib.getMetaData(CompanySettings.PROD4_LICENSE_KEY, app.getPackageManager().getPackageInfo(app.getPackageName(), 0).versionName, MobileTraderApplication.APP_LAUNCHER_URL_2, CompanySettings.APP_UPDATE_URL_TYPE) : "";
                if ("".equals(result_prod5))
                    result_prod5 = CompanySettings.PROD5_LICENSE_KEY != null ? AppLauncherLib.getMetaData(CompanySettings.PROD5_LICENSE_KEY, app.getPackageManager().getPackageInfo(app.getPackageName(), 0).versionName, MobileTraderApplication.APP_LAUNCHER_URL_2, CompanySettings.APP_UPDATE_URL_TYPE) : "";
                if ("".equals(result_prod6))
                    result_prod6 = CompanySettings.PROD6_LICENSE_KEY != null ? AppLauncherLib.getMetaData(CompanySettings.PROD6_LICENSE_KEY, app.getPackageManager().getPackageInfo(app.getPackageName(), 0).versionName, MobileTraderApplication.APP_LAUNCHER_URL_2, CompanySettings.APP_UPDATE_URL_TYPE) : "";
                if ("".equals(result_prod7))
                    result_prod7 = CompanySettings.PROD7_LICENSE_KEY != null ? AppLauncherLib.getMetaData(CompanySettings.PROD7_LICENSE_KEY, app.getPackageManager().getPackageInfo(app.getPackageName(), 0).versionName, MobileTraderApplication.APP_LAUNCHER_URL_2, CompanySettings.APP_UPDATE_URL_TYPE) : "";
                if ("".equals(result_prod8))
                    result_prod8 = CompanySettings.PROD8_LICENSE_KEY != null ? AppLauncherLib.getMetaData(CompanySettings.PROD8_LICENSE_KEY, app.getPackageManager().getPackageInfo(app.getPackageName(), 0).versionName, MobileTraderApplication.APP_LAUNCHER_URL_2, CompanySettings.APP_UPDATE_URL_TYPE) : "";
                if ("".equals(result_prod9))
                    result_prod9 = CompanySettings.PROD9_LICENSE_KEY != null ? AppLauncherLib.getMetaData(CompanySettings.PROD9_LICENSE_KEY, app.getPackageManager().getPackageInfo(app.getPackageName(), 0).versionName, MobileTraderApplication.APP_LAUNCHER_URL_2, CompanySettings.APP_UPDATE_URL_TYPE) : "";
                if ("".equals(result_prod10))
                    result_prod10 = CompanySettings.PROD10_LICENSE_KEY != null ? AppLauncherLib.getMetaData(CompanySettings.PROD10_LICENSE_KEY, app.getPackageManager().getPackageInfo(app.getPackageName(), 0).versionName, MobileTraderApplication.APP_LAUNCHER_URL_2, CompanySettings.APP_UPDATE_URL_TYPE) : "";

                if (!CompanySettings.FOR_TEST) {
                    if (app.alLoginInfoDemo.size() == 0)
                        app.convertJsonToArrayDemo(result_demo);

                    if (app.alLoginInfoProd.size() == 0)
                        app.convertJsonToArrayProd(result_prod);

                    if (app.alLoginInfoProd2.size() == 0)
                        if (!result_prod2.equals("")) app.convertJsonToArrayProd2(result_prod2);

                    if (app.alLoginInfoProd3.size() == 0)
                        if (!result_prod3.equals("")) app.convertJsonToArrayProd3(result_prod3);

                    if (app.alLoginInfoProd4.size() == 0)
                        if (!result_prod4.equals("")) app.convertJsonToArrayProd4(result_prod4);

                    if (app.alLoginInfoProd5.size() == 0)
                        if (!result_prod5.equals("")) app.convertJsonToArrayProd5(result_prod5);

                    if (app.alLoginInfoProd6.size() == 0)
                        if (!result_prod6.equals("")) app.convertJsonToArrayProd6(result_prod6);

                    if (app.alLoginInfoProd7.size() == 0)
                        if (!result_prod7.equals("")) app.convertJsonToArrayProd7(result_prod7);

                    if (app.alLoginInfoProd8.size() == 0)
                        if (!result_prod8.equals("")) app.convertJsonToArrayProd8(result_prod8);

                    if (app.alLoginInfoProd9.size() == 0)
                        if (!result_prod9.equals("")) app.convertJsonToArrayProd9(result_prod9);

                    if (app.alLoginInfoProd10.size() == 0)
                        if (!result_prod10.equals("")) app.convertJsonToArrayProd10(result_prod10);
                }
            }

            // Use appLauncher Parameter cmdline to identify the message key index for talking to FxServer
            try {
                JSONObject json1 = new JSONObject(result_demo);
                try {
                    app.AppLauncherMessageKeyIndexForDemo = json1.getInt("cmdline");
                } catch (JSONException e) {
                    e.printStackTrace();
                    app.AppLauncherMessageKeyIndexForDemo = -1;
                }
                if (CompanySettings.APP_UPDATE_URL_TYPE != null)
                    CompanySettings.APP_UPDATE_URL_DEMO = json1.getString("package");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                JSONObject json1 = new JSONObject(result_prod);
                try {
                    app.AppLauncherMessageKeyIndexForProd1 = json1.getInt("cmdline");
                } catch (JSONException e) {
                    e.printStackTrace();
                    app.AppLauncherMessageKeyIndexForProd1 = -1;
                }
                if (CompanySettings.APP_UPDATE_URL_TYPE != null)
                    CompanySettings.APP_UPDATE_URL_PROD1 = json1.getString("package");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                JSONObject json1 = new JSONObject(result_prod2);
                try {
                    app.AppLauncherMessageKeyIndexForProd2 = json1.getInt("cmdline");
                } catch (JSONException e) {
                    e.printStackTrace();
                    app.AppLauncherMessageKeyIndexForProd2 = -1;
                }
                if (CompanySettings.APP_UPDATE_URL_TYPE != null)
                    CompanySettings.APP_UPDATE_URL_PROD2 = json1.getString("package");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                JSONObject json1 = new JSONObject(result_prod3);
                try {
                    app.AppLauncherMessageKeyIndexForProd3 = json1.getInt("cmdline");
                } catch (JSONException e) {
                    e.printStackTrace();
                    app.AppLauncherMessageKeyIndexForProd3 = -1;
                }
                if (CompanySettings.APP_UPDATE_URL_TYPE != null)
                    CompanySettings.APP_UPDATE_URL_PROD3 = json1.getString("package");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                JSONObject json1 = new JSONObject(result_prod4);
                try {
                    app.AppLauncherMessageKeyIndexForProd4 = json1.getInt("cmdline");
                } catch (JSONException e) {
                    e.printStackTrace();
                    app.AppLauncherMessageKeyIndexForProd4 = -1;
                }
                if (CompanySettings.APP_UPDATE_URL_TYPE != null)
                    CompanySettings.APP_UPDATE_URL_PROD4 = json1.getString("package");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                JSONObject json1 = new JSONObject(result_prod5);
                try {
                    app.AppLauncherMessageKeyIndexForProd5 = json1.getInt("cmdline");
                } catch (JSONException e) {
                    e.printStackTrace();
                    app.AppLauncherMessageKeyIndexForProd5 = -1;
                }
                if (CompanySettings.APP_UPDATE_URL_TYPE != null)
                    CompanySettings.APP_UPDATE_URL_PROD5 = json1.getString("package");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                JSONObject json1 = new JSONObject(result_prod6);
                try {
                    app.AppLauncherMessageKeyIndexForProd6 = json1.getInt("cmdline");
                } catch (JSONException e) {
                    e.printStackTrace();
                    app.AppLauncherMessageKeyIndexForProd6 = -1;
                }
                if (CompanySettings.APP_UPDATE_URL_TYPE != null)
                    CompanySettings.APP_UPDATE_URL_PROD6 = json1.getString("package");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                JSONObject json1 = new JSONObject(result_prod7);
                try {
                    app.AppLauncherMessageKeyIndexForProd7 = json1.getInt("cmdline");
                } catch (JSONException e) {
                    e.printStackTrace();
                    app.AppLauncherMessageKeyIndexForProd7 = -1;
                }
                if (CompanySettings.APP_UPDATE_URL_TYPE != null)
                    CompanySettings.APP_UPDATE_URL_PROD7 = json1.getString("package");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                JSONObject json1 = new JSONObject(result_prod8);
                try {
                    app.AppLauncherMessageKeyIndexForProd8 = json1.getInt("cmdline");
                } catch (JSONException e) {
                    e.printStackTrace();
                    app.AppLauncherMessageKeyIndexForProd8 = -1;
                }
                if (CompanySettings.APP_UPDATE_URL_TYPE != null)
                    CompanySettings.APP_UPDATE_URL_PROD8 = json1.getString("package");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                JSONObject json1 = new JSONObject(result_prod9);
                try {
                    app.AppLauncherMessageKeyIndexForProd9 = json1.getInt("cmdline");
                } catch (JSONException e) {
                    e.printStackTrace();
                    app.AppLauncherMessageKeyIndexForProd9 = -1;
                }
                if (CompanySettings.APP_UPDATE_URL_TYPE != null)
                    CompanySettings.APP_UPDATE_URL_PROD9 = json1.getString("package");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                JSONObject json1 = new JSONObject(result_prod10);
                try {
                    app.AppLauncherMessageKeyIndexForProd10 = json1.getInt("cmdline");
                } catch (JSONException e) {
                    e.printStackTrace();
                    app.AppLauncherMessageKeyIndexForProd10 = -1;
                }
                if (CompanySettings.APP_UPDATE_URL_TYPE != null)
                    CompanySettings.APP_UPDATE_URL_PROD10 = json1.getString("package");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            //System.out.println("result_demo:" + result_demo);
            //System.out.println("result_prod:" + result_prod);
            //System.out.println("result_prod2:" + result_prod2);
            return result_demo.indexOf("\"" + app.getPackageManager().getPackageInfo(app.getPackageName(), 0).versionName + "\"") > 0 &&
                    result_prod.indexOf("\"" + app.getPackageManager().getPackageInfo(app.getPackageName(), 0).versionName + "\"") > 0 &&
                    (CompanySettings.PROD2_LICENSE_KEY == null || result_prod2.indexOf("\"" + app.getPackageManager().getPackageInfo(app.getPackageName(), 0).versionName + "\"") > 0) &&
                    (CompanySettings.PROD3_LICENSE_KEY == null || result_prod3.indexOf("\"" + app.getPackageManager().getPackageInfo(app.getPackageName(), 0).versionName + "\"") > 0) &&
                    (CompanySettings.PROD4_LICENSE_KEY == null || result_prod4.indexOf("\"" + app.getPackageManager().getPackageInfo(app.getPackageName(), 0).versionName + "\"") > 0) &&
                    (CompanySettings.PROD5_LICENSE_KEY == null || result_prod5.indexOf("\"" + app.getPackageManager().getPackageInfo(app.getPackageName(), 0).versionName + "\"") > 0) &&
                    (CompanySettings.PROD6_LICENSE_KEY == null || result_prod6.indexOf("\"" + app.getPackageManager().getPackageInfo(app.getPackageName(), 0).versionName + "\"") > 0) &&
                    (CompanySettings.PROD7_LICENSE_KEY == null || result_prod7.indexOf("\"" + app.getPackageManager().getPackageInfo(app.getPackageName(), 0).versionName + "\"") > 0) &&
                    (CompanySettings.PROD8_LICENSE_KEY == null || result_prod8.indexOf("\"" + app.getPackageManager().getPackageInfo(app.getPackageName(), 0).versionName + "\"") > 0) &&
                    (CompanySettings.PROD9_LICENSE_KEY == null || result_prod9.indexOf("\"" + app.getPackageManager().getPackageInfo(app.getPackageName(), 0).versionName + "\"") > 0) &&
                    (CompanySettings.PROD10_LICENSE_KEY == null || result_prod10.indexOf("\"" + app.getPackageManager().getPackageInfo(app.getPackageName(), 0).versionName + "\"") > 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 打开安卓市场
     */
    public static void openAndroidMarket(Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri UpdateUri;
        if (CompanySettings.APP_UPDATE_URL_TYPE != null && !CompanySettings.APP_UPDATE_URL_PROD1.equals("-")) {
            // since we cannot determine user login platform at this moment , hard code using prod1 now!
            UpdateUri = Uri.parse(CompanySettings.APP_UPDATE_URL_PROD1);
        } else {
            UpdateUri = Uri.parse(CompanySettings.APP_UPDATE_URL != null ? CompanySettings.APP_UPDATE_URL : "market://details?id=" + context.getPackageName());
            PackageManager pm = context.getApplicationContext().getPackageManager();
            boolean isInstalled = isPackageInstalled("com.android.browser", pm);
            if (isInstalled)
                intent.setPackage("com.android.browser");
        }
        intent.setData(UpdateUri);
        context.startActivity(intent);
    }

    private static boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    /**
     * 校验密码：
     * 8到16位
     * 包含至少一个数字
     * 包含至少一个大写英文
     * 包含至少一个小写英文
     * 包含至少一个字符
     * <p>
     * 参考：https://www.jianshu.com/p/3222ac7921cc
     */
    public static boolean verfiPwd(String pwd) {
        String reg = "/(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[\\W_]).{8,16}/";
        return RegexUtils.isMatch(reg, pwd);
    }


    /**
     * 打开pdf、doc、txt等文档
     * 不是在线预览
     * 先下载 -> 工具查看
     */
    public static void openDoc(Activity activity, String docUrl) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setData(Uri.parse(docUrl));
        activity.startActivity(intent);
    }

}
