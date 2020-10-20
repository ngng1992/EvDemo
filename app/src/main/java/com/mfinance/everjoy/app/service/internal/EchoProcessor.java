package com.mfinance.everjoy.app.service.internal;

import android.os.Message;

import com.mfinance.everjoy.app.service.FxMobileTraderService;


/**
 * A processor that simply broadcast all message with same service code
 */
public class EchoProcessor implements MessageProcessor {
    @Override
    public boolean processMessage(Message msg, FxMobileTraderService service) {
        service.broadcast(msg.what, msg.getData());
        return true;
    }
}
