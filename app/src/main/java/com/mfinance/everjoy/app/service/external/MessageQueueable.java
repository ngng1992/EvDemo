package com.mfinance.everjoy.app.service.external;

import com.mfinance.everjoy.app.util.MessageObj;

/**
 *
 * @author hk.ng
 */
public interface MessageQueueable {
    void addToDeliveryQueue(MessageObj msgObj);
}
