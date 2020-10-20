package com.mfinance.everjoy.everjoy.network;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * RetrofitClient封装单例类, 实现网络请求
 * <p>
 * 未设置缓存
 */
public class RetrofitClient {

    public static final long DEFAULT_MILLISECONDS = 60000;      //默认的超时时间


    private static Retrofit retrofit;
    private static boolean mIsDebug;

    /**
     * Applicaton中设置是否是调试模式
     *
     * @param isDebug 调试
     */
    public static void init(boolean isDebug) {
        mIsDebug = isDebug;
    }

    private static class SingletonHolder {
        private static RetrofitClient INSTANCE = new RetrofitClient();
    }

    public static RetrofitClient getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private RetrofitClient() {
        retrofit = new Retrofit.Builder()
                .client(getHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(RetrofitURL.BASE_URL)
                .build();
    }

    /**
     * 与OKGO配置一样
     * <p>
     * 配置OkHttpClient
     *
     * @return OkHttpClient
     */
    private OkHttpClient getHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        // 添加请求头，与打印日志的顺序不要搞错了
        TokenInterceptor tokenInterceptor = new TokenInterceptor();
        builder.addInterceptor(tokenInterceptor);

        // 调试打印log
        Level level = mIsDebug ? Level.INFO : Level.OFF;
        HttpLoggingInterceptor.Level httpLevel = mIsDebug ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE;
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("Retrofit");
        loggingInterceptor.setPrintLevel(httpLevel);
        loggingInterceptor.setColorLevel(level);
        builder.addInterceptor(loggingInterceptor);

        builder.readTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        builder.writeTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        builder.connectTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);

        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();
        builder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        builder.hostnameVerifier(HttpsUtils.UnSafeHostnameVerifier);

        return builder.build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
