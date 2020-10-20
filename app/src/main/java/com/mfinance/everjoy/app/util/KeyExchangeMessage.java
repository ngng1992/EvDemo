package com.mfinance.everjoy.app.util;

import java.security.spec.X509EncodedKeySpec;
import com.mfinance.everjoy.app.constant.IDDictionary;

/**
 *
 * @author hk.ng
 */
public class KeyExchangeMessage {

    public boolean bIsMessageKeyLastVersion = false;
    //    private CommonFunction cf = new CommonFunction(true);
    protected byte[] encodedKey;
    protected X509EncodedKeySpec encodedKeySpec;
    protected int functionType;
    protected int serviceType;

    public KeyExchangeMessage() {
        serviceType = IDDictionary.SERVER_LOGIN_SERVICE_TYPE;
        functionType = IDDictionary.SERVER_LOGIN_KEYEXHANGE;
    }

    public KeyExchangeMessage(int iServiceType, int iFunctionType) {
        serviceType = iServiceType;
        functionType = iFunctionType;
    }
    /**
     * @return the encodedKey
     */
    public byte[] getEncodedKey() {
        if (this.encodedKey == null){
            this.encodedKey = this.encodedKeySpec.getEncoded();
        }
        return encodedKey;
    }
    /**
     * @param encodedKey the encodedKey to set
     */
    public void setEncodedKey(byte[] encodedKey) {
        this.encodedKeySpec = null;
        this.encodedKey = encodedKey;
    }
    /**
     * @return the encodedKeySpec
     */
    public X509EncodedKeySpec getEncodedKeySpec() {
        if(this.encodedKeySpec == null){
            this.encodedKeySpec = new X509EncodedKeySpec(this.encodedKey);
        }
        return encodedKeySpec;
    }
    /**
     * @param encodedKeySpec the encodedKeySpec to set
     */
    public void setEncodedKeySpec(X509EncodedKeySpec encodedKeySpec) {
        this.encodedKey = null;
        this.encodedKeySpec = encodedKeySpec;
    }

    /**
     * @return the functionType
     */
    public int getFunctionType() {
        return functionType;
    }

    /**
     * @param functionType the functionType to set
     */
    public void setFunctionType(int functionType) {
        this.functionType = functionType;
    }

    /**
     * @return the serviceType
     */
    public int getServiceType() {
        return serviceType;
    }

    /**
     * @param serviceType the serviceType to set
     */
    public void setServiceType(int serviceType) {
        this.serviceType = serviceType;
    }
}
