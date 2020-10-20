package com.mfinance.everjoy.app.service.external;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.mfinance.everjoy.BuildConfig;
import com.mfinance.everjoy.app.LoginActivity;

import com.mfinance.everjoy.app.constant.IDDictionary;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.service.FxMobileTraderService;
import com.mfinance.everjoy.app.util.MessageObj;

import java.util.Date;

/**
 * Created by johnny.ng on 17/9/2019.
 */

public class SystemAlertMessageHandler extends ServerMessageHandler {

    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alertDialog;
    Context mContext;

    public SystemAlertMessageHandler(FxMobileTraderService service) {
        super(service);
    }

    @Override
    public void handleMessage(MessageObj msgObj) {
        if(msgObj == null)
            return;
        service.app.data.setSystemAlertMessage(msgObj.getField("msg1"));
        service.broadcast(ServiceFunction.ACT_SYSTEM_ALERT_MESSAGE, null);
    }

    @Override
    public boolean isStatusLess() {
        return false;
    }

    @Override
    public boolean isBalanceRecalRequire() {
        return false;
    }

}