package com.mfinance.everjoy.everjoy.network;

import android.text.TextUtils;

import com.blankj.utilcode.util.AppUtils;
import com.mfinance.everjoy.everjoy.sp.UserSharedPUtils;


import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 拦截token是否过期，测试10分钟
 * <p>
 * token有效期10分钟
 * refreshToken有效期30天
 * <p>
 * token过期，则使用接口刷新token
 * refreshToken过期，则提示去登录
 * <p>
 * 在闪屏页去做，因为token的有效期会大于1天
 */
public class TokenInterceptor implements Interceptor {

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        String token = UserSharedPUtils.getToken();
        if (!TextUtils.isEmpty(token)) {
            builder.addHeader("Authorization", token);
        }
        builder.addHeader("version", String.valueOf(AppUtils.getAppVersionCode()));
        builder.addHeader("device", "Android");
        // SC/EN/TC 没做语言
        builder.addHeader("lang", "SC");
        Request request = builder.build();
        Response response = chain.proceed(request);
        return response;
    }
}
