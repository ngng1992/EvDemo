package com.mfinance.everjoy.app.util;

import com.mfinance.everjoy.app.constant.FXConstants;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.spec.X509EncodedKeySpec;

/**
 *
 * @author hk.ng
 */
public class KeyExchangeMessageBuffer {

    final ByteBuffer buffer;

    public KeyExchangeMessageBuffer(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    public KeyExchangeMessage get() throws BufferUnderflowException, NumberFormatException {
        byte serviceCodeByte, functionCodeByte, delimiter;
        int messageLength;
        delimiter = buffer.get();
        serviceCodeByte = buffer.get();
        functionCodeByte = buffer.get();
        byte[] messageLengthByte = new byte[FXConstants.MESSAGE_NORMAL_LENGTH];
        buffer.get(messageLengthByte);
        messageLength = Integer.parseInt(
                new String(messageLengthByte, Charset.forName("UTF-8")).trim()
        );

        if (buffer.remaining() < messageLength) {
            throw new BufferUnderflowException();
        }
        KeyExchangeMessage instance = new KeyExchangeMessage();
        byte[] tempByte = new byte[messageLength];
        buffer.get(tempByte);
        instance.setEncodedKeySpec(new X509EncodedKeySpec(tempByte));
        return instance;
    }

    public KeyExchangeMessageBuffer put(KeyExchangeMessage message) throws BufferOverflowException {
        byte[] encoded = message.getEncodedKey();
        String strBodySize = String.valueOf(encoded.length);
        while (strBodySize.length() < FXConstants.MESSAGE_NORMAL_LENGTH) {
            strBodySize += " ";
        }

        buffer.put((byte) 19)
                .put((byte) message.getServiceType())
                .put((byte) message.getFunctionType())
                .put(strBodySize.getBytes(Charset.forName("UTF-8")));
        buffer.put(encoded);
        return this;
    }
}

