package com.mfinance.everjoy.app.service.external;


import com.mfinance.everjoy.app.CompanySettings;
import com.mfinance.everjoy.app.constant.IDDictionary;
import com.mfinance.everjoy.app.model.DataRepository;
import com.mfinance.everjoy.app.service.FxMobileTraderService;
import com.mfinance.everjoy.app.util.ConnectionSelector;
import com.mfinance.everjoy.app.util.MessageObj;
import com.mfinance.everjoy.app.util.PriceAgentLoginHandler;
import com.mfinance.everjoy.app.util.PriceAgentMessageDelivery;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import me.pushy.sdk.Pushy;

/**
 * Created by johnny.ng on 1/2/2019.
 */

public class PriceAgentPushNotificationHandler extends ServerMessageHandler {

    public PriceAgentPushNotificationHandler(FxMobileTraderService service) {
        super(service);
    }

    @Override
    public void handleMessage(MessageObj msgObj) {
        try {
            String deviceToken = Pushy.register(service.getApplicationContext());
            System.out.println("PriceAgentPushNotificationHandler " + deviceToken);
        }catch (Exception exc) {
            System.out.println("PriceAgentPushNotificationHandler "+exc);
        }
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
