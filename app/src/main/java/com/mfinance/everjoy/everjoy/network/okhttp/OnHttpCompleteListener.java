package com.mfinance.everjoy.everjoy.network.okhttp;

import android.graphics.Bitmap;

public abstract class OnHttpCompleteListener {

    public void onStart(){}

    public abstract void onComplete(String result);

    public void onError(String msg){}

    public void onFinish(){}

    public void onBitmap(Bitmap bitmap){};
}
