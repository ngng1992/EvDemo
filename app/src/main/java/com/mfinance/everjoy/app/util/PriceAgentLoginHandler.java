package com.mfinance.everjoy.app.util;

import com.mfinance.everjoy.app.CompanySettings;
import com.mfinance.everjoy.app.constant.IDDictionary;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Callable;

import javax.crypto.KeyAgreement;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;

/**
 *
 */
public class PriceAgentLoginHandler implements Callable<Optional<byte[]>> {

    final WritableByteChannel writeChannel;
    final ReadableByteChannel readChannel;
    final ByteBuffer buffer;
    final KeyExchangeMessageBuffer keyExchangeMessageBuffer;
    final Set<String> tradableContractSet;
    final boolean isAllTradableContract;
    private final boolean guest;
    KeyAgreement bobKeyAgree;
    KeyPairGenerator bobKpairGen;
    KeyFactory bobKeyFac;

    /**
     *
     * @param writeChannel channel that is already open between price agent and client
     * @param readChannel channel that is already open between price agent and client
     * @param optionalTradableContractSet tradable contract to be sent during login process
     */
    public PriceAgentLoginHandler(WritableByteChannel writeChannel, ReadableByteChannel readChannel, Optional<Set<String>> optionalTradableContractSet, boolean guest) {
        this.buffer = ByteBuffer.allocate(4048);
        this.writeChannel = writeChannel;
        this.readChannel = readChannel;
        this.guest = guest;
        this.keyExchangeMessageBuffer = new KeyExchangeMessageBuffer(buffer);
        if(optionalTradableContractSet.isPresent()){
            this.tradableContractSet = optionalTradableContractSet.get();
        } else {
            this.tradableContractSet = new HashSet<String>();
        }
        isAllTradableContract = !optionalTradableContractSet.isPresent();
        try {
            bobKeyFac = KeyFactory.getInstance("DH");
            bobKpairGen = KeyPairGenerator.getInstance("DH");
            bobKeyAgree = KeyAgreement.getInstance("DH");
        } catch (NoSuchAlgorithmException ex) {
            ;
        }
    }

    /**
     * Execute login process.
     * @return Optional secret message generated in key exchange. Absent if the login process is fail.
     * @throws Exception not used
     */
    @Override
    public Optional<byte[]> call() throws Exception {
        try {
            KeyExchangeMessage keyExchangeMessage = null;
            readChannel.read(buffer);
            buffer.flip();
            try {
                keyExchangeMessage = keyExchangeMessageBuffer.get();
            } catch (BufferUnderflowException ex) {

            }
            buffer.clear();
            PublicKey alicePubKey = bobKeyFac.generatePublic(keyExchangeMessage.getEncodedKeySpec());
            DHParameterSpec dhParamFromAlicePubKey = ((DHPublicKey) alicePubKey).getParams();

            bobKpairGen.initialize(dhParamFromAlicePubKey);
            KeyPair bobKpair = bobKpairGen.generateKeyPair();
            bobKeyAgree.init(bobKpair.getPrivate());

            keyExchangeMessage = new KeyExchangeMessage();
            keyExchangeMessage.setEncodedKey(bobKpair.getPublic().getEncoded());
            keyExchangeMessageBuffer.put(keyExchangeMessage);
            buffer.flip();
            writeChannel.write(buffer);

            bobKeyAgree.doPhase(alicePubKey, true);

            buffer.clear();
            byte[] generateSecret = bobKeyAgree.generateSecret();
            MessageBuffer messageBuffer = new MessageBuffer(buffer, generateSecret);
            MessageObj loginMessage = new MessageObj(IDDictionary.SERVER_LOGIN_SERVICE_TYPE, IDDictionary.SERVER_LOGIN_PRICE_AGENT);
            if (!isAllTradableContract) {
                StringBuilder sb = new StringBuilder();
                Iterator<String> iterator = tradableContractSet.iterator();
                if (iterator.hasNext()) {
                    sb.append(iterator.next());
                }
                while (iterator.hasNext()) {
                    sb.append("|");
                    sb.append(iterator.next());
                }
                loginMessage.setField("contracts", sb.toString());
            } else {
                loginMessage.setField("contracts", "ALL");
            }
            loginMessage.setField("secret", CompanySettings.PRICEAGENT_LOGIN_SECRET);
            loginMessage.setField("guest", guest ? "1" : "0");
            messageBuffer.put(loginMessage);
            buffer.flip();
            writeChannel.write(buffer);
            return Optional.of(generateSecret);
        } catch (Exception ex) {
            try {
                readChannel.close();
            } catch (Exception ex1) {

            }
            try {
                readChannel.close();
            } catch (Exception ex1) {

            }

            return Optional.empty();
        }

    }

}
