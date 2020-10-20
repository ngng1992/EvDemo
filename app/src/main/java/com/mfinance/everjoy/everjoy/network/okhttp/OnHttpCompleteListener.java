package com.mfinance.everjoy.everjoy.network.okhttp;

public abstract class OnHttpCompleteListener {

    public void onStart(){}

    public abstract void onComplete(String result);

    public void onError(String msg){}

    public void onFinish(){}
}
