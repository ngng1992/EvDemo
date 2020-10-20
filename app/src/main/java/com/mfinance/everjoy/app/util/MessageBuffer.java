package com.mfinance.everjoy.app.util;

import com.mfinance.everjoy.app.CompanySettings;
import com.mfinance.everjoy.app.constant.FXConstants;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import java.util.Arrays;

/**
 *
 * @author hk.ng
 */
public class MessageBuffer {

    final ByteBuffer buffer;
    final CommonFunction cf;
    final Charset charset;

    private AESLib aes = null;
    private byte[] aesKey;

    public MessageBuffer(ByteBuffer buffer, CommonFunction cf) {
        this.buffer = buffer;
        this.charset = Charset.forName("UTF-8");
        this.cf = cf;
    }

    public MessageBuffer(ByteBuffer buffer, byte[] encryptKey) {
        this.buffer = buffer;
        this.charset = Charset.forName("UTF-8");
        this.cf = new CommonFunction(true);
        this.cf.setKeyByte(encryptKey);
        this.aesKey = Arrays.copyOfRange(encryptKey, 0, 32);
    }

    public MessageObj get() throws BufferUnderflowException, NumberFormatException {
        byte serviceCodeByte, functionCodeByte, delimiter;
        int messageLength;

        delimiter = buffer.get();
        serviceCodeByte = buffer.get();
        functionCodeByte = buffer.get();
        byte[] messageLengthByte = new byte[FXConstants.MESSAGE_NORMAL_LENGTH];
        messageLength = Integer.parseInt(
                new String(messageLengthByte, Charset.forName("UTF-8")).trim()
        );

        if (buffer.remaining() < messageLength) {
            throw new BufferUnderflowException();
        }
        MessageObj instance = new MessageObj(serviceCodeByte, functionCodeByte);
        CharBuffer decode = charset.decode(buffer);
        char[] charArray = new char[messageLength];
        decode.get(charArray);
        if (CompanySettings.AESCrypto) {
            instance.parse(
                    cf.decryptText(String.valueOf(charArray))
            );
        }
        else {
            instance.parse(
                    decrypt(String.valueOf(charArray))
            );
        }
        return instance;
    }

    public MessageBuffer put(MessageObj message) throws BufferOverflowException {
        String convertToStringWithoutHeader = message.convertToStringWithoutHeader();
        String encryptText = "";
        if (CompanySettings.AESCrypto){
            encryptText = encrypt(convertToStringWithoutHeader);
        }
        else {
            encryptText = cf.encryptText(convertToStringWithoutHeader);
        }
        String strBodySize = String.valueOf(encryptText.length());

        while (strBodySize.length() < FXConstants.MESSAGE_NORMAL_LENGTH) {
            strBodySize += " ";
        }

        buffer.put((byte) 19)
                .put((byte) message.getServiceType())
                .put((byte) message.getFunctionType())
                .put(strBodySize.getBytes(Charset.forName("UTF-8")));
        CharsetEncoder encoder = charset.newEncoder()
                .onMalformedInput(CodingErrorAction.REPLACE)
                .onUnmappableCharacter(CodingErrorAction.REPLACE);
        CoderResult result = CoderResult.UNDERFLOW;
        CharBuffer wrappedBuffer = CharBuffer.wrap(encryptText);
        CoderResult coderResult = encoder.encode(
                wrappedBuffer,
                buffer,
                true
        );
        if (result.isOverflow()) {
            throw new BufferOverflowException();
        } else {
            encoder.flush(buffer);
        }
        return this;
    }

    private String encrypt(String str)
    {
        if (aes == null)
        {
            aes = new AESLib(aesKey);
        }
        return aes.encrypt(str);
    }

    private String decrypt(String str) {
        if (aes == null) {
            aes = new AESLib(aesKey);
        }
        return aes.decrypt(str);
    }
}
