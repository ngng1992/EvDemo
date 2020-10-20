package com.mfinance.everjoy.app.util;

import com.mfinance.everjoy.app.constant.FXConstants;
import com.mfinance.everjoy.app.constant.IDDictionary;
import com.mfinance.everjoy.app.service.external.MessageDeliverDisconnectListener;
import com.mfinance.everjoy.app.service.external.MessageDeliverDiscountEvent;
import com.mfinance.everjoy.app.service.external.MessageQueueable;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.util.ArrayList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class PriceAgentMessageDelivery implements Runnable {

    enum ParseStatus {
        ReadBodyDelimiter, ReadServiceType, ReadFunctionCode, ReadBodyLength, ReadBody, ParseBody
    }
    ParseStatus parseStatus = ParseStatus.ReadBodyDelimiter;
    final boolean needEncrypt;
    final ReadableByteChannel readChannel;
    final WritableByteChannel writeChannel;
    final ByteBuffer buffer;
    final ByteBuffer bufferOut;
    byte serviceType, functionCode;
    int bodySize;
    int remainingBodySize;
    CharBuffer bodyBuffer = CharBuffer.allocate(1024);
    final CharsetDecoder utf8Encoder = Charset.forName("UTF-8").newDecoder()
            .onMalformedInput(CodingErrorAction.REPLACE)
            .onUnmappableCharacter(CodingErrorAction.REPLACE);
    final CharsetDecoder iso8859Encoder = Charset.forName("ISO-8859-1").newDecoder()
            .onMalformedInput(CodingErrorAction.REPLACE)
            .onUnmappableCharacter(CodingErrorAction.REPLACE);
    ArrayList<MessageQueueable> messageDeliverables;
    ArrayList<MessageDeliverDisconnectListener> disconnectionListeners = new ArrayList<MessageDeliverDisconnectListener>();
    ScheduledExecutorService executor;
    final MessageBuffer messageBufferOut;

    /**
     *
     * @param readChannel channel that is already open between price agent and
     * client
     * @param writeChannel channel that is already open between price
     * agent and client
     * @param priceServerSecret encryption key for sending message to price
     * agent
     * @param executor to send heartbeat
     */
    public PriceAgentMessageDelivery(ReadableByteChannel readChannel, WritableByteChannel writeChannel, byte[] priceServerSecret, ScheduledExecutorService executor) {
        this.messageDeliverables = new ArrayList<MessageQueueable>();
        this.readChannel = readChannel;
        this.buffer = ByteBuffer.allocate(256);
        this.bufferOut = ByteBuffer.allocate(256);
        this.needEncrypt = true;
        this.writeChannel = writeChannel;
        this.messageBufferOut = new MessageBuffer(bufferOut, priceServerSecret);
        this.executor = executor;
    }

    /**
     *
     * @param e listener that will receive parsed message from price agent
     */
    public void addMessageDeliverListener(MessageQueueable e) {
        messageDeliverables.add(e);
    }

    /**
     *
     * @param e
     * @return <tt>true</tt> if this list contained the specified element
     */
    public boolean removeEventListener(MessageQueueable e) {
        return messageDeliverables.remove(e);
    }

    public void addPriceDeliverDisconnectListener(MessageDeliverDisconnectListener e) {
        disconnectionListeners.add(e);
    }

    /**
     * Start sending heartbeat and receive price message and contract update.
     * Will block the thread until the end of socket.
     */
    @Override
    public void run() {
        // Start sending heart beat message to keep the connection alive
        executor.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                try {
                    bufferOut.clear();
                    messageBufferOut.put(new MessageObj(IDDictionary.SERVER_HEARTBEAT_SERVICE_TYPE, 0));
                    bufferOut.flip();
                    int byteWrote = writeChannel.write(bufferOut);
                    if (byteWrote <= 0) {
                        throw new IOException();
                    }
                } catch (IOException ex) {
                    try {
                        readChannel.close();
                        writeChannel.close();
                    } catch (IOException expected) {

                    }
                    executor.shutdown();
                }
            }
        }, 1, 1, TimeUnit.SECONDS);
        try {
            ByteBuffer priceMessageView = ByteBuffer.allocate(1024);
            while (readChannel.isOpen()) {
                if (readChannel.read(buffer) < 0) {
                    //CommonLog.logMessage(CommonLog.SYSTEM_LOG, CommonLog.INFORMATION, "connection lost to " + readChannel);
                    break;
                }
                buffer.flip();
                boolean needMoreByte = false;
                try {
                    while (buffer.hasRemaining() && !needMoreByte) {
                        switch (parseStatus) {
                            case ReadBodyDelimiter:
                                if (buffer.get() == 19) {
                                    parseStatus = ParseStatus.ReadServiceType;
                                }
                                break;
                            case ReadServiceType:
                                serviceType = buffer.get();
                                if (serviceType != 0 && serviceType != 19) {
                                    parseStatus = ParseStatus.ReadFunctionCode;
                                } else {
                                    parseStatus = ParseStatus.ReadBodyDelimiter;
                                }
                                break;
                            case ReadFunctionCode:
                                functionCode = buffer.get();
                                if (functionCode != 19) {
                                    parseStatus = ParseStatus.ReadBodyLength;
                                } else {
                                    parseStatus = ParseStatus.ReadFunctionCode;
                                }
                                break;
                            case ReadBodyLength:
                                int length;
                                if (isPriceMessage(serviceType, functionCode)) {
                                    length = 2;
                                } else {
                                    length = FXConstants.MESSAGE_NORMAL_LENGTH;
                                }
                                if (buffer.remaining() >= length) {
                                    if (isPriceMessage(serviceType, functionCode)) {
                                        bodySize = buffer.getShort();
                                    } else {
                                        byte[] temp = new byte[length];
                                        buffer.get(temp);
                                        CharsetDecoder newDecoder = this.utf8Encoder;
                                        String strBodyLength = newDecoder.decode(ByteBuffer.wrap(temp)).toString();
                                        newDecoder.reset();
                                        strBodyLength = strBodyLength.trim();
                                        try {
                                            if (MessageObj.isHexBodySizeMessage(serviceType, functionCode)) {
                                                bodySize = Utility.hexToDec(strBodyLength);
                                            } else {
                                                bodySize = Integer.parseInt(strBodyLength);
                                            }
                                            if (bodySize < 0) {
                                                throw new NumberFormatException(strBodyLength);
                                            }
                                        } catch (NumberFormatException e) {
                                            //CommonLog.logMessage(CommonLog.SYSTEM_LOG, CommonLog.ERROR, "NumberFormatException -> strHeader : " + (char) serviceType + (char) functionCode + strBodyLength);
                                            parseStatus = parseStatus.ReadBodyDelimiter;
                                            break;
                                        }

                                    }
                                    remainingBodySize = bodySize;
                                    parseStatus = ParseStatus.ReadBody;
                                    bodyBuffer.clear();
                                    if (bodySize > bodyBuffer.capacity()) {
                                        bodyBuffer = CharBuffer.allocate(bodySize);
                                    }
                                } else {
                                    needMoreByte = true;
                                }
                                break;
                            case ReadBody:
                                if (isPriceMessage(serviceType, functionCode)) {
                                    if (buffer.remaining() >= remainingBodySize) {
                                        if (remainingBodySize > priceMessageView.capacity()) {
                                            priceMessageView = ByteBuffer.allocate(remainingBodySize + 1024);
                                        }
                                        priceMessageView.clear();
                                        ByteBuffer temp = buffer.slice();
                                        temp.limit(remainingBodySize);
                                        priceMessageView.put(temp);
                                        priceMessageView.flip();
                                        buffer.position(buffer.position() + remainingBodySize);
                                        parseStatus = ParseStatus.ParseBody;
                                    } else {
                                        needMoreByte = true;
                                    }
                                } else {
                                    CharsetDecoder newDecoder = this.utf8Encoder;
                                    ByteBuffer temp = buffer.duplicate();
                                    try {
                                        if (buffer.remaining() >= remainingBodySize) {
                                            temp.limit(buffer.position() + remainingBodySize);
                                            newDecoder.decode(temp, bodyBuffer, true);
                                            newDecoder.flush(bodyBuffer);
                                            newDecoder.reset();
                                            bodyBuffer.flip();
                                            parseStatus = ParseStatus.ParseBody;
                                        } else {
                                            remainingBodySize = remainingBodySize - buffer.remaining();
                                            newDecoder.decode(temp, bodyBuffer, false);
                                        }
                                    } catch (Exception ex) {
                                        //CommonLog.logException(CommonLog.SYSTEM_LOG, CommonLog.ERROR, ex);
                                        parseStatus = ParseStatus.ReadBodyDelimiter;
                                    }
                                    buffer.position(temp.position());
                                }
                                break;
                            case ParseBody:
                                boolean bDecodable = false;
                                MessageObj msgIncoming;
                                if (isPriceMessage(serviceType, functionCode)) {
                                    PriceMessageObj priceMessageIncoming = new PriceMessageObj(serviceType, functionCode);
                                    msgIncoming = priceMessageIncoming;
                                    bDecodable = priceMessageIncoming.parse(priceMessageView);
                                } else {
                                    msgIncoming = new MessageObj(serviceType, functionCode);
                                    try {
                                        if (needEncrypt) {
                                            bDecodable = msgIncoming.parse(bodyBuffer, false, true);
                                        } else {
                                            bDecodable = msgIncoming.parse(bodyBuffer);
                                        }
                                    } catch (Exception ex) {
                                        bDecodable = false;
                                    }

                                }
                                if (bDecodable) {
                                    if (serviceType != IDDictionary.TRADER_HEARTBEAT_TYPE) {
                                        String parsedString = msgIncoming.convertToString();
                                        //CommonLog.logMessage(CommonLog.TRACE_LOG, CommonLog.TRACE, "msgIncoming.convertToString : " + parsedString + "\r\nmsgIncoming.convertToString length : " + parsedString.length());
                                    }

                                    for (MessageQueueable d : messageDeliverables) {
                                        //0System.out.println("msgIncoming "+msgIncoming);
                                        d.addToDeliveryQueue(msgIncoming);
                                    }

                                } else {
                                    //System.out.println("Cannot decode the incoming message");
                                    //CommonLog.logMessage(CommonLog.SYSTEM_LOG, CommonLog.ERROR, "Cannot decode the incoming message");
                                }
                                parseStatus = ParseStatus.ReadBodyDelimiter;
                                break;
                        }
                    }
                    buffer.compact();
                } catch (BufferUnderflowException ex) {
                    // read buffer. can be handled.
                }
            }
        } catch (IOException ex) {
            //CommonLog.logException(CommonLog.SYSTEM_LOG, CommonLog.ERROR, ex);
        } finally {
            System.out.println("------------------------executor.shutdown();");
            try {
                if (readChannel.isOpen()) {
                    readChannel.close();
                }
            } catch (IOException ex) {
                //CommonLog.logException(CommonLog.SYSTEM_LOG, CommonLog.ERROR, ex);
            }

            executor.shutdown();
            for (MessageDeliverDisconnectListener e : disconnectionListeners) {
                e.onMessageDeliverDisconnection(new MessageDeliverDiscountEvent());
            }
        }

    }

    private boolean isPriceMessage(byte serviceType, byte functionCode){
        return (serviceType == IDDictionary.TRADER_LIVE_PRICE_TYPE && functionCode == IDDictionary.TRADER_UPDATE_STREAM_PRICE);
    }
}
