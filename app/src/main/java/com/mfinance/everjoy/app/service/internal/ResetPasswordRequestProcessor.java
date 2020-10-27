package com.mfinance.everjoy.app.service.internal;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.mfinance.everjoy.app.constant.IDDictionary;
import com.mfinance.everjoy.app.constant.Protocol;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.model.DataRepository;
import com.mfinance.everjoy.app.service.FxMobileTraderService;
import com.mfinance.everjoy.app.util.MessageObj;

import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

public class ResetPasswordRequestProcessor implements MessageProcessor {
    private final String TAG = "ResetPasswordRequestProcessor";
    @Override
    public boolean processMessage(Message msg, FxMobileTraderService service) {
        Bundle data = msg.getData();
        MessageObj message = MessageObj.getMessageObj(IDDictionary.SERVER_IO_SERVICE_TYPE, IDDictionary.SERVER_IO_REQUEST_CHANGE_PASSWORD);
        message.setField(Protocol.ResetPasswordRequest.EMAIL, service.app.data.getStrEmail());
        message.setField(Protocol.ResetPasswordRequest.OLD_PASSWORD, service.app.getPassword());
        message.setField(Protocol.ResetPasswordRequest.NEW_PASSWORD, data.getString(ServiceFunction.RESETPASSWORD_NEWPASSWORD));

        service.connection.sendMessage(message.convertToString(true));
        Log.i(TAG, message.convertToString(false));
        Log.i(TAG, "[ResetPasswordRequestProcessor]["+message.convertToString(true)+"]");
        Log.i(TAG, "[ResetPasswordRequestProcessor]["+message.convertToString(false)+"]");
        return true;
    }
}
