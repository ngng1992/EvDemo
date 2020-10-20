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

/**
 * Created by johnny.ng on 1/2/2019.
 */

public class PriceAgentContractUpdateMessageHandler extends ServerMessageHandler {

    public PriceAgentContractUpdateMessageHandler(FxMobileTraderService service) {
        super(service);
    }

    @Override
    public void handleMessage(MessageObj msgObj) {
        ConnectionSelector.initPriceAgentDefaultSetting(ConnectionSelector.parsePriceAgent(CompanySettings.PriceAgentIP,CompanySettings.PriceAgentDisplayName));
        service.app.setPriceAgentConnectListRef(ConnectionSelector.getPriceAgent(CompanySettings.m_strWebLink, service.app.getSelfIP()));
        runPriceAgent();
    }

    @Override
    public boolean isStatusLess() {
        return false;
    }

    @Override
    public boolean isBalanceRecalRequire() {
        return false;
    }

    public void runPriceAgent(){
        runPriceAgent(false);
    }

    /**
     * launch runnable on separate executor to login price agent and receive price
     * @param rotateConnection true if rotate connection from set up
     */
    private void runPriceAgent(boolean rotateConnection) {
        if (service.app.executor.isShutdown()){
            service.app.executor = service.app.getNewExecutor();
        }
        Socket lastClosedSocket = service.app.getPriceAgentSocketRef();
        if (lastClosedSocket != null && !lastClosedSocket.isClosed()) {
            // Socket is not closed. Price agent is currently running.
            return;
        }
        final Socket priceAgentSocket = new Socket();
        if (!service.app.setPriceAgentSocketRef(lastClosedSocket, priceAgentSocket)){
            // Price agent is reconnecting in other thread.
            return;
        }
        try {
            try {
                priceAgentSocket.setTcpNoDelay(true);
            } catch (SocketException expected) {
                // no big deal there
            }
            try {
                priceAgentSocket.setSoTimeout(service.app.m_connectionTimeout/2);
            } catch (SocketException expected) {

            }
            if (rotateConnection || lastClosedSocket == null || lastClosedSocket.getRemoteSocketAddress() == null) {
                ConnectionSelector.ConnectionEntry next = service.app.getPriceAgentConnectListRef();
                //headerPanel.setCurrentPriceConnection(Optional.of(next));
                priceAgentSocket.connect(next.getSocketAddress());
            } else {
                priceAgentSocket.connect(lastClosedSocket.getRemoteSocketAddress());
            }

            final ReadableByteChannel inputChannel = Channels.newChannel(priceAgentSocket.getInputStream());
            final WritableByteChannel outputChannel = Channels.newChannel(priceAgentSocket.getOutputStream());
            Optional<Set<String>> optionalTradableContractSet;
            if(DataRepository.getInstance().getTradableContract().size() > 0){
                Set<String> set = new HashSet<String>(DataRepository.getInstance().getTradableContract());
                optionalTradableContractSet = Optional.of(set);
            } else {
                optionalTradableContractSet = Optional.empty();
            }

            final Future<Optional<byte[]>> priceAgentMessageSecretFuture = service.app.executor.submit(
                    new PriceAgentLoginHandler(
                            outputChannel,
                            inputChannel,
                            optionalTradableContractSet,
                            false
                    )
            );
            service.app.executor.submit(new Runnable(){
                @Override
                public void run() {
                    try {
                        Optional<byte[]> generateSecret = priceAgentMessageSecretFuture.get();
                        if (generateSecret.isPresent() && !priceAgentSocket.isClosed()) {
                            final PriceAgentMessageDelivery messageDelivery = new PriceAgentMessageDelivery(
                                    inputChannel,
                                    outputChannel,
                                    generateSecret.get(),
                                    service.app.executor
                            );
                            messageDelivery.addMessageDeliverListener(new MessageQueueable() {
                                AtomicBoolean connected = new AtomicBoolean(false);
                                @Override
                                public void addToDeliveryQueue(MessageObj msgObj) {
                                    if(connected.getAndSet(true) == false) {
                                        //service.app.setConnectionStatus(1);
                                        service.app.setPriceAgentConnectionStatus(true);
                                    }
                                    if (msgObj.getServiceType() != IDDictionary.TRADER_HEARTBEAT_TYPE) {
                                        //m_deliverMessageThread.addToDeliveryQueue(msgObj);
                                        //System.out.println(msgObj.convertToString());
                                        service.postMessage(msgObj);
                                    }
                                }
                            });
                            messageDelivery.addPriceDeliverDisconnectListener(new MessageDeliverDisconnectListener() {
                                @Override
                                public void onMessageDeliverDisconnection(MessageDeliverDiscountEvent e) {
                                    rerunPriceAgent(priceAgentSocket);
                                }
                            });
                            //messageDelivery.addPriceDeliverDisconnectListener(this);
                            service.app.executor.submit(messageDelivery);
                        } else {
                            rerunPriceAgent(priceAgentSocket);
                        }
                    } catch (InterruptedException ex) {
                    } catch (ExecutionException ex) {
                        rerunPriceAgent(priceAgentSocket);
                    }
                }
            });
        } catch (IOException ex) {
            rerunPriceAgent(priceAgentSocket);
        }
    }

    /**
     * Helper function from runPriceAgent() to rerun price agent
     * Not designed to call outside runPriceAgent()
     * @param inputSocket socket used to connect price agent
     */
    private void rerunPriceAgent(final Socket inputSocket){

        try {
            inputSocket.close();
        } catch (IOException expected) {

        } finally {
            //priceService.onMessageDeliverDisconnection(new MessageDeliverDiscountEvent());
            //if (!m_bIsQuit && m_bIsConnected) {
            //service.app.setConnectionStatus(0);
            if (service.app.executor.isShutdown() && service.app.bLogon){
                service.app.executor = service.app.getNewExecutor();
            }
            service.app.setPriceAgentConnectionStatus(false);
            service.app.executor.schedule(new Runnable() {
                @Override
                public void run() {
                    runPriceAgent(true);
                }
            }, 500, TimeUnit.MILLISECONDS);
            //}
        }
    }

}
