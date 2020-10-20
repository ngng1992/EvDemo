package com.mfinance.everjoy.everjoy.network.okhttp;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;

/**
 * OkHttp简单封装
 */
public class OkHttpUtils {

    /**
     * get请求
     * @param url 完整的url地址
     */
    public static void getRequest(String url, OnHttpCompleteListener onHttpCompleteListener) {
        onHttpCompleteListener.onStart();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        Call call = client.newCall(request);
        // 异步调用
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                onHttpCompleteListener.onError(e.getMessage());
                onHttpCompleteListener.onFinish();
            }

            @Override
            public void onResponse(@NotNull Call call, okhttp3.@NotNull Response response) throws IOException {
                ResponseBody body = response.body();
                if (body == null) {
                    onHttpCompleteListener.onError("result == null");
                } else {
                    String string = body.string();
                    Log.d("Okhttp", "string = " + string);
                    onHttpCompleteListener.onComplete(string);
                }
                onHttpCompleteListener.onFinish();
            }
        });
    }

}
