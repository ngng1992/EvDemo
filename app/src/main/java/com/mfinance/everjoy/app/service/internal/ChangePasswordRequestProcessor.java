package com.mfinance.everjoy.app.service.internal;

import android.os.Bundle;
import android.os.Message;

import com.mfinance.everjoy.app.constant.IDDictionary;
import com.mfinance.everjoy.app.constant.Protocol;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.model.DataRepository;
import com.mfinance.everjoy.app.pojo.ChangePasswordRequest;
import com.mfinance.everjoy.app.pojo.ChangePasswordRequestBuilder;
import com.mfinance.everjoy.app.service.FxMobileTraderService;
import com.mfinance.everjoy.app.util.MessageObj;

import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

public class ChangePasswordRequestProcessor implements MessageProcessor {
    private final String TAG = this.getClass().getSimpleName();
    private ReentrantLock lock = new ReentrantLock();

    @Override
    public boolean processMessage(Message msg, FxMobileTraderService service) {
        Bundle data = msg.getData();
        MessageObj message = MessageObj.getMessageObj(IDDictionary.SERVER_IO_SERVICE_TYPE, IDDictionary.SERVER_IO_REQUEST_CHANGE_PASSWORD);
        message.setField(Protocol.ChangePassword.USER_ID, service.app.data.getStrUser());
        message.setField(Protocol.ChangePassword.OLD_PASSWORD, data.getString(Protocol.ChangePassword.OLD_PASSWORD));
        message.setField(Protocol.ChangePassword.NEW_PASSWORD, data.getString(Protocol.ChangePassword.NEW_PASSWORD));

        lock.lock();
        ChangePasswordRequest changePasswordRequest = null;
        try {
            ChangePasswordRequest oldRequest = service.app.data.getChangePasswordRequest();
            if (oldRequest == DataRepository.EMPTY_CHANGE_PASSWORD_REQUEST || !oldRequest.isPending()) {
                ChangePasswordRequestBuilder builder = new ChangePasswordRequestBuilder();
                changePasswordRequest = builder.setPending(true)
                        .setId(UUID.randomUUID().toString())
                        .createChangePasswordRequest();
                service.connection.sendMessage(message);
                service.app.data.setChangePasswordRequest(changePasswordRequest);
            }
        } finally {
            lock.unlock();
        }
        if (changePasswordRequest != null) {
            service.broadcast(ServiceFunction.ACT_UPDATE_UI, null);
        }

        return false;
    }
}
