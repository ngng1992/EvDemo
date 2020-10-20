package com.mfinance.everjoy.everjoy.network;

import android.util.Log;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 自定义观察者接口，方便后期扩展
 * <p>
 * 1. 一个完整的网络请求：onSubscribe -> onNext -> onComplete
 * 如果发生错误：onSubscribe -> onError -> onFinish 方便做进度条的操作
 */
public abstract class HttpResultObserver<T> implements Observer<T> {

    /**
     * 当前的Activity是否有关联，还没有被销毁
     */
    private boolean isAttached = false;

    public HttpResultObserver(boolean isAttached) {
        this.isAttached = isAttached;
    }

    @Override
    public void onSubscribe(Disposable d) {
        // 开始
        Log.e("http", "onSubscribe");
    }

    @Override
    public void onNext(T t) {
        // 结果，服务器返回错误信息
        Log.e("http", "onNext：isAttached = " + isAttached);
        if (isAttached) {
            onSuccess(t);
        }else {
            Log.e("http", "onNext：当前activity被销毁，不执行成功代码");
        }
    }

    @Override
    public void onError(Throwable e) {
        // 在这里可自定义异常，http层返回错误
        e.printStackTrace();
        onFinish();
        // 有可能是解析json、网络等问题
        Log.e("http", "onError：" + e.getMessage());
    }

    @Override
    public void onComplete() {
        onFinish();
        Log.e("http", "onComplete");
    }

    public void onSuccess(T t){

    }

    public void onFinish() {

    }

}
