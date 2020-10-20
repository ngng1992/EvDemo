package com.mfinance.everjoy.everjoy.network;

import android.text.TextUtils;

import com.blankj.utilcode.util.ToastUtils;
import com.mfinance.everjoy.everjoy.bean.base.BaseBean;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 结果回调
 *
 * @param <T>
 */
public abstract class HttpResultCallBack<T extends BaseBean> implements Callback<T> {

    public abstract void onShowResponse(T t);

    public void onShowFailure(String message) {
        // 这里是响应报错，统一写在这里
        if (TextUtils.isEmpty(message)) return;
        ToastUtils.showShort(message);
    }

    public abstract void onFinish();

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        boolean successful = response.isSuccessful();
        if (successful) {
            T body = response.body();
            if (body == null) {
                onShowFailure("");
                return;
            }
            int code = body.getCode();
            if (code == HttpConstant.RESULT_SUCCESS_0) {
                onShowResponse(response.body());
            } else {
                onShowFailure(body.getMessage());
            }
        }else {
            onShowFailure(response.message());
        }
        onFinish();
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        t.printStackTrace();
        onShowFailure(t.getMessage());
        onFinish();
    }
}
