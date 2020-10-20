package com.mfinance.everjoy.app.service.internal;

import android.os.Message;
import android.util.Log;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.mfinance.everjoy.app.CompanySettings;
import com.mfinance.everjoy.app.constant.IDDictionary;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.model.DataRepository;
import com.mfinance.everjoy.app.pojo.ConnectionStatus;
import com.mfinance.everjoy.app.service.FxMobileTraderService;
import com.mfinance.everjoy.app.service.external.MessageQueueable;
import com.mfinance.everjoy.app.service.external.ServerMessageHandler;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;


/**
 * 不需要登录，推送报价，获取报价信息
 */
public class PriceAgentConnectionProcessor implements MessageProcessor {
    public enum ActionType {
        CONNECT(0), RECONNECT(1), DISCONNECT(2), RESET(3);
        private final int value;
        ActionType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
        public static ActionType fromValue(int i) {
            for (ActionType t: values()) {
                if (t.value == i) {
                    return t;
                }
            }
            return CONNECT;
        }
    }
    private final Executor mainExecutor;
    private final boolean guest;
    private final ThreadFactory factory;
    private final AtomicReference<Socket> priceAgentSocketRef;
    private ExecutorService priceAgentExecutor;
    private final Runnable disconnectRunnable;

    public PriceAgentConnectionProcessor(Executor mainExecutor, boolean guest) {
        factory = new ThreadFactoryBuilder().setNameFormat("sad-guest-thread-%d").build();
        this.guest = guest;
        this.mainExecutor = mainExecutor;
        this.priceAgentSocketRef = new AtomicReference<>();
        priceAgentExecutor = null;
        disconnectRunnable = () -> {
            Socket socket = priceAgentSocketRef.get();
            if (socket != null) {
                try {
                    socket.close();
                } catch (Exception ex) {

                }
            }
        };
    }

    @Override
    public boolean processMessage(Message msg, FxMobileTraderService service) {
        ActionType actionType = ActionType.fromValue(msg.arg1);
        switch (actionType) {
            case CONNECT:
                ConnectionSelector.initPriceAgentDefaultSetting(ConnectionSelector.parsePriceAgent(CompanySettings.PriceAgentIP, CompanySettings.PriceAgentDisplayName));
                service.app.setPriceAgentConnectListRef(ConnectionSelector.getPriceAgent(CompanySettings.m_strWebLink, service.app.getSelfIP()));
                service.app.data.setGuestPriceAgentConnectionId(msg.arg2);
                runPriceAgent(false, service);
                break;
            case RECONNECT:
                service.app.data.setGuestPriceAgentConnectionId(msg.arg2);
                runPriceAgent(true, service);
                break;
            case DISCONNECT:
                Socket socket = priceAgentSocketRef.get();
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (Exception ex) {

                    }
                }
                break;
            case RESET:
                mainExecutor.execute(() -> {
                    //service.app.bPriceReload = true;a
                    service.app.data.setGuestPriceAgentConnectionStatus(ConnectionStatus.INITIAL);
                    service.broadcast(ServiceFunction.ACT_UPDATE_UI, null);
                });
            default:
                break;
        }

        return true;
    }

    /**
     * launch runnable on separate executor to login price agent and receive price
     *
     * @param rotateConnection true if rotate connection from set up
     */
    private void runPriceAgent(boolean rotateConnection, FxMobileTraderService service) {
        mainExecutor.execute(() -> {
            service.app.data.setGuestPriceAgentConnectionStatus(ConnectionStatus.CONNECTING);
            service.broadcast(ServiceFunction.ACT_UPDATE_UI, null);
        });
        if (priceAgentExecutor != null) {
            priceAgentExecutor.shutdown();
        }
        priceAgentExecutor = Executors.newSingleThreadExecutor(factory);
        final Socket priceAgentSocket = new Socket();
        Socket lastClosedSocket = priceAgentSocketRef.getAndSet(priceAgentSocket);
        if (lastClosedSocket != null && !lastClosedSocket.isClosed()) {
            try {
                lastClosedSocket.close();
            } catch (Exception ex) {

            }
        }
        try {
            try {
                priceAgentSocket.setTcpNoDelay(true);
            } catch (SocketException expected) {
                // no big deal there
            }
            try {
                priceAgentSocket.setSoTimeout(service.app.m_connectionTimeout / 2);
            } catch (SocketException expected) {

            }
            if (rotateConnection || lastClosedSocket == null || lastClosedSocket.getRemoteSocketAddress() == null) {
                ConnectionSelector.ConnectionEntry next = service.app.getPriceAgentConnectListRef();
                priceAgentSocket.connect(next.getSocketAddress());
            } else {
                priceAgentSocket.connect(lastClosedSocket.getRemoteSocketAddress());
            }

            final ReadableByteChannel inputChannel = Channels.newChannel(priceAgentSocket.getInputStream());
            final WritableByteChannel outputChannel = Channels.newChannel(priceAgentSocket.getOutputStream());
            Optional<Set<String>> optionalTradableContractSet;
            if (DataRepository.getInstance().getTradableContract().size() > 0) {
                Set<String> set = new HashSet<String>(DataRepository.getInstance().getTradableContract());
                optionalTradableContractSet = Optional.of(set);
            } else {
                optionalTradableContractSet = Optional.empty();
            }

            final Future<Optional<byte[]>> priceAgentMessageSecretFuture = priceAgentExecutor.submit(
                    new PriceAgentLoginHandler(
                            outputChannel,
                            inputChannel,
                            optionalTradableContractSet,
                            guest
                    )
            );
            priceAgentExecutor.submit(() -> {
                try {
                    Optional<byte[]> generateSecret = priceAgentMessageSecretFuture.get();
                    if (generateSecret.isPresent() && !priceAgentSocket.isClosed()) {
                        final PriceAgentMessageDelivery messageDelivery = new PriceAgentMessageDelivery(
                                inputChannel,
                                outputChannel,
                                generateSecret.get(),
                                Executors.newSingleThreadScheduledExecutor()
                        );
                        messageDelivery.addMessageDeliverListener(new MessageQueueable() {
                            AtomicBoolean connected = new AtomicBoolean(false);

                            @Override
                            public void addToDeliveryQueue(MessageObj msgObj) {
                                if (!connected.getAndSet(true)) {
                                    mainExecutor.execute(() -> {
                                        service.addClearServiceListener(disconnectRunnable);
                                        service.app.data.setGuestPriceAgentConnectionStatus(ConnectionStatus.CONNECTED);
                                        service.broadcast(ServiceFunction.ACT_UPDATE_UI, null);
                                    });
                                    //service.app.setPriceAgentConnectionStatus(true);
                                }
                                Log.d("test", "service: " + msgObj.getServiceType() + " function: " + msgObj.getFunctionType());
                                if (msgObj.getServiceType() != IDDictionary.TRADER_HEARTBEAT_TYPE) {
                                    HashMap<String, ServerMessageHandler> serverMessageHandler = service.getServerMessageHandler();
                                    ServerMessageHandler handler = serverMessageHandler.get(msgObj.getKey());
                                    if (handler != null) {
                                        mainExecutor.execute(() -> handler.handleMessage(msgObj));
                                    }
                                }
                            }
                        });
                        messageDelivery.addPriceDeliverDisconnectListener(e -> {
                            mainExecutor.execute(() -> {
                                service.removeClearServiceListener(disconnectRunnable);
                                service.app.data.setGuestPriceAgentConnectionStatus(ConnectionStatus.DISCONNECTED);
                                service.broadcast(ServiceFunction.ACT_UPDATE_UI, null);
                            });
                        });
                        priceAgentExecutor.execute(messageDelivery);
                    } else {
                        mainExecutor.execute(() -> {
                            service.app.data.setGuestPriceAgentConnectionStatus(ConnectionStatus.DISCONNECTED);
                            service.broadcast(ServiceFunction.ACT_UPDATE_UI, null);
                        });
                    }
                } catch (InterruptedException ex) {
                } catch (ExecutionException ex) {
                    mainExecutor.execute(() -> {
                        service.app.data.setGuestPriceAgentConnectionStatus(ConnectionStatus.DISCONNECTED);
                        service.broadcast(ServiceFunction.ACT_UPDATE_UI, null);
                    });
                }
            });
        } catch (IOException ex) {
            mainExecutor.execute(() -> {
                service.app.data.setGuestPriceAgentConnectionStatus(ConnectionStatus.DISCONNECTED);
                service.broadcast(ServiceFunction.ACT_UPDATE_UI, null);
            });
        }
    }
}