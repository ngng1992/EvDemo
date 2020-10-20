package com.mfinance.everjoy.everjoy.network.okgo;

import android.app.Application;

import com.blankj.utilcode.util.AppUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.model.HttpHeaders;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.OkHttpClient;

/**
 * OkGo网络框架
 */
public class OkGoInit {

    /**
     * 初始化，设置语言之后，再次初始化
     */
    public static void init(Application application, boolean isDebug) {
        // header不支持中文，不允许有特殊字符
//        HttpHeaders headers = new HttpHeaders();
//        headers.put("version", String.valueOf(AppUtils.getAppVersionCode()));
//        headers.put("device", "Android");
//        headers.put("lang", "SC");
        // 必须调用初始化
        OkGo.getInstance().init(application)
                //建议设置OkHttpClient，不设置将使用默认的
                .setOkHttpClient(getHttpClient(isDebug))
                //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheMode(CacheMode.NO_CACHE)
                //全局统一缓存时间，默认永不过期，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)
                //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
                .setRetryCount(3);
                //全局公共头
//                .addCommonHeaders(headers);
        //全局公共参数
//                .addCommonParams(params);
    }

    /**
     * 配置OkHttpClient
     *
     * @return OkHttpClient
     */
    private static OkHttpClient getHttpClient(boolean isDebug) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        // TODO 只有调试时才会打印log，发包后不打印
        Level level = isDebug ? Level.INFO : Level.OFF;
        HttpLoggingInterceptor.Level httpLevel = isDebug ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE;

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        loggingInterceptor.setPrintLevel(httpLevel);
        loggingInterceptor.setColorLevel(level);
        builder.addInterceptor(loggingInterceptor);

        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);

        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();
        builder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        builder.hostnameVerifier(HttpsUtils.UnSafeHostnameVerifier);
        return builder.build();
    }


}
