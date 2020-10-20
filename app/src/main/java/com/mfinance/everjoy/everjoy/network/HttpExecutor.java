package com.mfinance.everjoy.everjoy.network;

import android.annotation.SuppressLint;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;

/**
 * 接口请求执行器
 */
public class HttpExecutor {

    private ApiService apiService;

    private static class SingletonHolder {
        private static HttpExecutor INSTANCE = new HttpExecutor();
    }

    public static HttpExecutor getInstance() {
        return HttpExecutor.SingletonHolder.INSTANCE;
    }

    public ApiService geApiService() {
        Retrofit retrofit = RetrofitClient.getInstance().getRetrofit();
        if (apiService == null) {
            apiService = retrofit.create(ApiService.class);
        }
        return apiService;
    }

    /**
     * string类型的请求体
     */
    public RequestBody getRequestBody(String data) {
        return RequestBody.create(data, MediaType.parse("application/json;charset=UTF-8"));
    }

    /**
     * file类型的请求体
     */
    public List<MultipartBody.Part> getMultipartBody(File... fileList) {
        List<MultipartBody.Part> list = new ArrayList<>();
        for (File file : fileList) {
            RequestBody requestBody = RequestBody.create(file, MediaType.parse("multipart/form-data"));
            MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            list.add(part);
        }
        return list;
    }

    /**
     * 自定义的service
     */
    public <T> T create(final Class<T> service) {
        if (service == null) {
            throw new RuntimeException("Api service is null!");
        }
        return RetrofitClient.getInstance().getRetrofit().create(service);
    }

    /**
     * 执行rxjava
     */
    @SuppressLint("CheckResult")
    public <T> T execute(Observable<T> observable, Observer<T> subscriber) {
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        return null;
    }

}
