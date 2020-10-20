package com.mfinance.everjoy.app;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.os.Build;

import java.util.Locale;

public class LocaleContextWrapper extends ContextWrapper {
    private Context baseContext;

    public LocaleContextWrapper(Context base) {
        super(base);
        this.baseContext = base;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        this.baseContext = base;
    }

    public void changeLocale(Locale locale) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            this.baseContext = LocaleUtility.updateResourcesLocale(this, locale);
        } else {
            this.baseContext = LocaleUtility.updateResourcesLocaleLegacy(this, locale);
        }
    }

    @Override
    public Resources getResources() {
        return baseContext.getResources();
    }
}
