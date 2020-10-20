package com.mfinance.everjoy.app.util;

import java.io.UnsupportedEncodingException ;
import java.security.* ;
import java.text.SimpleDateFormat;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.zip.*;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.mfinance.everjoy.app.CompanySettings;

import com.mfinance.everjoy.app.bo.ContractObj;
import com.mfinance.everjoy.app.bo.OpenPositionRecord;
import com.mfinance.everjoy.app.bo.OrderRecord;
import com.mfinance.everjoy.app.constant.FXConstants;
import com.mfinance.everjoy.app.constant.Protocol;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.model.DataRepository;
import com.mfinance.everjoy.app.service.internal.CashMovementRequestProcessor;

public class CommonFunction
{
    public CommonFunction()
    {
        this(false);
    }

    public CommonFunction(boolean bNeedCompress)
    {
        m_bNeedCompress = bNeedCompress;
    }

	private boolean setKeyCommon(final byte[] byKey){
		int i,j,k,nLen,nTemp ;
		nBox = new int[MAXKEYLENGTH] ;
		nKey = new int[MAXKEYLENGTH] ;
		for (i=0; i<MAXKEYLENGTH; i++)
			nBox[i] = i ;
		nLen = byKey.length ;
		j = k = 0 ;
		for (i=0; i<MAXKEYLENGTH; i++)
		{
			k = ((byKey[j] & 0xFF)+nBox[i]+k) & 0xFF ;
			nTemp = nBox[i] ;
			nBox[i] = nBox[k] ;
			nBox[k] = nTemp ;
			j = (j+1) % nLen ;
		}
		for (i=0; i<MAXKEYLENGTH; i++)
			nKey[i] = nBox[i] ;
		return(true) ;
	}

	public boolean setKeyByte(final byte[] byKey){
		if ((byKey == null) || (byKey.length > MAXKEYLENGTH))
			return(false) ;
		return setKeyCommon(byKey);
	}

    public boolean setKey(String szKey)
    {
        int i,j,k,nLen,nTemp ;
        byte[] byKey ;
        
        if ((szKey == null) || (szKey.length() > MAXKEYLENGTH))
            return(false) ;
        try
        {
            byKey = szKey.getBytes(CHARSETFORMAT) ;
        }
        catch (UnsupportedEncodingException uee)
        {
            return(false) ;
        }
        nBox = new int[MAXKEYLENGTH] ;
        nKey = new int[MAXKEYLENGTH] ;
        for (i=0; i<MAXKEYLENGTH; i++)
            nBox[i] = i ;
        nLen = byKey.length ;
        j = k = 0 ;
        for (i=0; i<MAXKEYLENGTH; i++)
        {
            k = ((byKey[j] & 0xFF)+nBox[i]+k) & 0xFF ;
            nTemp = nBox[i] ;
            nBox[i] = nBox[k] ;
            nBox[k] = nTemp ;
            j = (j+1) % nLen ;
        }
        for (i=0; i<MAXKEYLENGTH; i++)
            nKey[i] = nBox[i] ;
        return(true) ;
    }
    
    public void resetKey()
    {
        nBox = null ;
        nKey = null ;
    }
    
    public String encryptText(String szInput)
    {
        byte[] byInput ;
        
        if ((szInput == null) || (nBox == null))
            return(null) ;

        if (m_bNeedCompress)
        {
            byInput = compress3(szInput);
        }
        else
        {
            try
            {
                byInput = szInput.getBytes(CHARSETFORMAT) ;
            }
            catch (UnsupportedEncodingException uee)
            {
                return(null) ;
            }
        }

        return(performOperation(byInput,true)) ;
    }
    
    public String decryptText(String szInput)
    {
        byte[] byInput = null;
        if ((szInput == null) || (nBox == null))
            return(null) ;

        byInput = hexDecode(szInput);
            
        return(performOperation(byInput,false)) ;
    }
    
    public String hexEncode(byte[] byInput)
    {
        byte byTemp ;
        
        StringBuffer strbuf = new StringBuffer(byInput.length*2) ;
        for (int i=0; i<byInput.length; i++)
        {
            byTemp = byInput[i] ;
            strbuf.append(digits[(byTemp & 0xF0) >> 4]) ;
            strbuf.append(digits[byTemp & 0x0F]) ;
        }
        return(strbuf.toString()) ;
    }
    
    public byte[] hexDecode(String szInput)
    {
        int nDigit1,nDigit2 ;
        
        if (szInput == null)
            return(null) ;
        int nLen = szInput.length() ;
        byte[] byRet = new byte[nLen/2] ;
        for (int i=0; i<byRet.length; i++)
        {
            nDigit1 = szInput.charAt(i*2) ;
            nDigit2 = szInput.charAt(i*2+1) ;
            if ((nDigit1 >= '0') && (nDigit1 <= '9'))
                nDigit1 -= '0' ;
            else
                if ((nDigit1 >= 'a') && (nDigit1 <= 'f'))
                    nDigit1 -= 'a' - 10 ;
                else
                    return(null) ;
            if ((nDigit2 >= '0') && (nDigit2 <= '9'))
                nDigit2 -= '0' ;
            else
                if ((nDigit2 >= 'a') && (nDigit2 <= 'f'))
                    nDigit2 -= 'a' - 10 ;
                else
                    return(null) ;
            byRet[i] = (byte)((nDigit1 << 4) + nDigit2) ;
        }
        return(byRet) ;
    }
    
    public String performOperation(byte[] byInput,boolean bEncode)
    {
        String szRet=null ;
        int i,j,k,nTemp,nXorIndex ;

        //David 20100518 add checking
        if(byInput == null)
        	return szRet;
        
        for (i=0; i<MAXKEYLENGTH; i++)
            nBox[i] = nKey[i] ;        
        int nLen = byInput.length ;
        byte[] byCipher = new byte[nLen] ;
        j = k = 0 ;
        for (i=0; i<nLen; i++)
        {
            j = (j+1) & 0xFF ;
            k = (nBox[j]+k) & 0xFF ;
            nTemp = nBox[j] ;
            nBox[j] = nBox[k] ;
            nBox[k] = nTemp ;
            nXorIndex = (nBox[j]+nBox[k]) & 0xFF ;
            byCipher[i] = (byte)(byInput[i] ^ nBox[nXorIndex]) ;
        }
        if (bEncode)
        {
            szRet = hexEncode(byCipher);
        }
        else
        {
            if (m_bNeedCompress)
            {
                szRet = uncompress3(byCipher);
            }
            else
            {
                try
                {
                    szRet = new String(byCipher,CHARSETFORMAT);
                }
                catch (UnsupportedEncodingException uee)
                {
                }
            }           
        }
        return(szRet) ;
    }

    public String RandomGenerate(int iLen)
    {
        int iRandNum = 0;
        StringBuffer strRandomText = new StringBuffer();
        
        int size = 36;
        if (FXConstants.PASSWORD_ONLY_NUMBERIC) {
        	size = 10;
        }

        for (int i = 0; i < iLen; i++)
        {
            iRandNum = (int) (size * Math.random());
            strRandomText.append(digits[iRandNum]);
        }

        return strRandomText.toString();
    }

    public static byte[] compress3(String s)
	{
		Deflater defl = new Deflater(Deflater.BEST_COMPRESSION);
//		defl.setInput(s.getBytes(CHARSETFORMAT));
        try
        {
            defl.setInput(s.getBytes(CHARSETFORMAT));
        }
        catch (UnsupportedEncodingException uee)
        {
        }
		defl.finish();
		boolean done = false;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		while (!done)
		{
			byte[] buf = new byte[256];
			int bufnum = defl.deflate(buf);
			bos.write(buf, 0, bufnum);
			if (bufnum < buf.length)
				done = true;
		}
		try 
		{
			bos.flush();
			bos.close();
		} 
		catch(IOException ioe) {}

		return(bos.toByteArray());
	}

	public static String uncompress3(byte[] b)
	{
		Inflater infl = new Inflater();
		infl.setInput(b);

		String szRet = null;
		boolean done = false;

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
		while (!done)
		{
			byte[] buf = new byte[256];
			try
			{
				int bufnum = infl.inflate(buf);
                bos.write(buf, 0, bufnum);
                
				if (bufnum < buf.length)
					done = true;
			} 
			catch(DataFormatException dfe)
			{
				done = true;
			}
		}

        try
        {
            szRet = new String(bos.toByteArray(), CHARSETFORMAT);
        }
        catch (UnsupportedEncodingException uee)
        {
        }

        bos = null;

        return szRet;
	}
    
    private MessageDigest hMDigest ;
    private int[] nBox ;
    private int[] nKey ;

    private boolean m_bNeedCompress = true;

    public static final String CHARSETFORMAT="UTF8" ;
    
    private static final int MAXKEYLENGTH=256 ;
    private static final char[] digits = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f',
                                          'g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v',
                                          'w','x','y','z'} ;
    
    public static String getTransactionID(String sAcc){
    	return sAcc+System.currentTimeMillis();
    }
    
    
	public static void login(Messenger mService, Messenger mServiceMessengerHandler, String strID, String strPassword){
		Message loginMsg = Message.obtain(null, ServiceFunction.SRV_LOGIN);
		loginMsg.replyTo = mServiceMessengerHandler;

		String strURL = "192.168.122.43";
		//String strURL = "192.168.123.43";
		//String strURL = "218.213.79.103";
		int iPort = 15000; 
		
		loginMsg.getData().putString(ServiceFunction.LOGIN_ID, strID);
		loginMsg.getData().putString(ServiceFunction.LOGIN_PASSWORD, strPassword);
		loginMsg.getData().putString(ServiceFunction.LOGIN_URL, strURL);
		loginMsg.getData().putInt(ServiceFunction.LOGIN_PORT, iPort);
					
		try {
			mService.send(loginMsg);
		} catch (RemoteException e) {
			Log.e(CommonFunction.class.getSimpleName(), "Unable to send login message", e.fillInStackTrace());
		}
	}	
    
	public static void sendDealRequest(Messenger mService, Messenger mServiceMessengerHandler, ContractObj contract, String sBuySell, String sLot, String pid, String ctime){
		Message dealMsg = Message.obtain(null, ServiceFunction.SRV_SEND_DEAL_REQUEST);
		dealMsg.replyTo = mServiceMessengerHandler;
		
		double dDealRate = 0.0;
		
		if( ("B".equals(sBuySell) && contract.getBSD() == false) || ("S".equals(sBuySell) && contract.getBSD() == true) ){
			dDealRate = contract.dBidAsk[1];
		}else{
			dDealRate = contract.dBidAsk[0];
		}

		if (contract.enableDepth.equals("1")){
			String sRate = null;
			double lot = Utility.toBigDecimal(sLot, "0").doubleValue();
			double [] bidLot = contract.bidLotDepth;
			double [] askLot = contract.askLotDepth;

			if (("S".equals(sBuySell) && contract.getBSD() == false) || ("B".equals(sBuySell) && contract.getBSD() == true)) {
				sRate = Utility.round(GUIUtility.getMin(contract.bidPriceDepth), contract.iRateDecPt, contract.iRateDecPt);
				double accLot = 0;
				for (int i = 0 ; i < bidLot.length ; i++){
					accLot += bidLot[i];
					if (lot <= accLot) {
						sRate = Utility.round(contract.bidPriceDepth[i], contract.iRateDecPt, contract.iRateDecPt);
						break;
					}
				}
				dDealRate = Utility.roundToDouble(Double.parseDouble(sRate) + (contract.dOffset + contract.dBidAdjust) * Math.pow(10.0, -contract.iRateDecPt), contract.iRateDecPt, contract.iRateDecPt);
			} else {
				sRate = Utility.round(GUIUtility.getMax(contract.askPriceDepth), contract.iRateDecPt, contract.iRateDecPt);
				double accLot = 0;
				for (int i = 0 ; i < askLot.length ; i++){
					accLot += askLot[i];
					if (lot <= accLot) {
						sRate = Utility.round(contract.askPriceDepth[i], contract.iRateDecPt, contract.iRateDecPt);
						break;
					}
				}
				dDealRate = Utility.roundToDouble(Double.parseDouble(sRate) + (contract.dOffset + contract.dAskAdjust) * Math.pow(10.0, -contract.iRateDecPt), contract.iRateDecPt, contract.iRateDecPt);
			}
		}
		
		dealMsg.getData().putString(ServiceFunction.SEND_DEAL_REQUEST_ID, pid);
		dealMsg.getData().putString(ServiceFunction.SEND_DEAL_REQUEST_CONTRACT, contract.strContractCode);
		dealMsg.getData().putInt(ServiceFunction.SEND_DEAL_REQUEST_CONTRACT_SIZE, contract.iContractSize);
		dealMsg.getData().putString(ServiceFunction.SEND_DEAL_REQUEST_BUY_SELL, sBuySell);
		dealMsg.getData().putDouble(ServiceFunction.SEND_DEAL_REQUEST_REQUEST_RATE, dDealRate);
		dealMsg.getData().putString(ServiceFunction.SEND_DEAL_REQUEST_LOT, sLot);
		// Default Slippage as 0
		dealMsg.getData().putString(ServiceFunction.SEND_DEAL_REQUEST_SLIPPAGE, "0");

		if (CompanySettings.AESCrypto) {
			dealMsg.getData().putString(ServiceFunction.SEND_DEAL_REQUEST_UNRESTRICTEDMARKET, CompanySettings.ENABLE_SLIPPAGE ? "false" : "true");
			dealMsg.getData().putString(ServiceFunction.SEND_DEAL_REQUEST_TAG, contract.tag);
			dealMsg.getData().putString(ServiceFunction.SEND_DEAL_REQUEST_CTIME, ctime);
		}
		
		try {
			mService.send(dealMsg);
		} catch (RemoteException e) {
			Log.e(CommonFunction.class.getSimpleName(), "Unable to send login message", e.fillInStackTrace());
		}
	}
	

	public static void sendDealInputFrameOpenClose(Messenger mService, Messenger mServiceMessengerHandler, String Account,  String Contract, String sBuySell, String Act)
	{
		if (CompanySettings.ENABLE_BUY_SELL_STRENGTH_PRICE_ADJUST && Account != null && Contract != null && sBuySell != null)
		{
			try {
				Bundle bundle = new Bundle();
				
				SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
			    Date now = new Date();
			    String strDate = sdfDate.format(now);
			    
				Message dealMsg = Message.obtain(null, ServiceFunction.SRV_DEAL_INPUT_FRAME_OPEN_CLOSE);
				dealMsg.replyTo = mServiceMessengerHandler;
				bundle.putString(ServiceFunction.SEND_DEAL_FRAME_OPEN_CLOSE_ACTION, Act);
				bundle.putString(ServiceFunction.SEND_DEAL_FRAME_OPEN_CLOSE_ACCOUNT, Account);
				bundle.putString(ServiceFunction.SEND_DEAL_FRAME_OPEN_CLOSE_CONTRACT, Contract);
				bundle.putString(ServiceFunction.SEND_DEAL_FRAME_OPEN_CLOSE_TIME, strDate);
				bundle.putString(ServiceFunction.SEND_DEAL_FRAME_OPEN_CLOSE_BUY_SELL, sBuySell);
				bundle.putString(ServiceFunction.SEND_DEAL_FRAME_OPEN_NUM_OF_ITEM, "1");
								
				dealMsg.setData(bundle);
				mService.send(dealMsg);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	

	public static void sendDealInputFrameOpenClose(Messenger mService, Messenger mServiceMessengerHandler, String Account, String Act)
	{
		if (mService != null && mServiceMessengerHandler != null && CompanySettings.ENABLE_BUY_SELL_STRENGTH_PRICE_ADJUST && Account != null && Act != null)
		{
			try {
				Bundle bundle = new Bundle();
				Message dealMsg = Message.obtain(null, ServiceFunction.SRV_DEAL_INPUT_FRAME_OPEN_CLOSE);
				dealMsg.replyTo = mServiceMessengerHandler;
				bundle.putString(ServiceFunction.SEND_DEAL_FRAME_OPEN_CLOSE_ACTION, Act);
				bundle.putString(ServiceFunction.SEND_DEAL_FRAME_OPEN_CLOSE_ACCOUNT, Account);
				dealMsg.setData(bundle);
				mService.send(dealMsg);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void sendDealAndOrderRequest(Messenger mService, Messenger mServiceMessengerHandler, ContractObj contract, String sBuySell, String sLot,String sLPrice, String sLGT, String sSPrice, String sSGT, String sSlippage, String pid, String ctime)
	{

        
		Message dealMsg = Message.obtain(null, ServiceFunction.SRV_SEND_DEAL_REQUEST);
		dealMsg.replyTo = mServiceMessengerHandler;
		
		double dDealRate = 0.0;
		
		if(("B".equals(sBuySell) && contract.getBSD() == false) || ("S".equals(sBuySell) && contract.getBSD() == true)){
			dDealRate = contract.dBidAsk[1];
		}else{
			dDealRate = contract.dBidAsk[0];
		}
		dealMsg.getData().putString(ServiceFunction.SEND_DEAL_REQUEST_ID, pid);
		dealMsg.getData().putString(ServiceFunction.SEND_DEAL_REQUEST_CONTRACT, contract.strContractCode);
		dealMsg.getData().putInt(ServiceFunction.SEND_DEAL_REQUEST_CONTRACT_SIZE, contract.iContractSize);
		dealMsg.getData().putString(ServiceFunction.SEND_DEAL_REQUEST_BUY_SELL, sBuySell);
		dealMsg.getData().putDouble(ServiceFunction.SEND_DEAL_REQUEST_REQUEST_RATE, dDealRate);
		dealMsg.getData().putString(ServiceFunction.SEND_DEAL_REQUEST_LOT, sLot);

        if(sLPrice != null && !sLPrice.equals("-1")){
        	dealMsg.getData().putString(ServiceFunction.SEND_DEAL_REQUEST_LIMIT_PRICE, sLPrice);
        	dealMsg.getData().putString(ServiceFunction.SEND_DEAL_REQUEST_LIMIT_GT, sLGT);
        }
	
        if(sSPrice != null && !sSPrice.equals("-1")){	
        	dealMsg.getData().putString(ServiceFunction.SEND_DEAL_REQUEST_STOP_PRICE, sSPrice);
        	dealMsg.getData().putString(ServiceFunction.SEND_DEAL_REQUEST_STOP_GT, sSGT);
        }

        if (CompanySettings.AESCrypto) {
			dealMsg.getData().putString(ServiceFunction.SEND_DEAL_REQUEST_UNRESTRICTEDMARKET, CompanySettings.ENABLE_SLIPPAGE? "false" : "true");
			dealMsg.getData().putString(ServiceFunction.SEND_DEAL_REQUEST_TAG, contract.tag);
			dealMsg.getData().putString(ServiceFunction.SEND_DEAL_REQUEST_CTIME, ctime);
		}
        
        dealMsg.getData().putString(ServiceFunction.SEND_DEAL_REQUEST_SLIPPAGE, sSlippage);

		try {
			mService.send(dealMsg);
		} catch (RemoteException e) {
			Log.e(CommonFunction.class.getSimpleName(), "Unable to send login message", e.fillInStackTrace());
		}
	}

	public static void sendDealAndOrderRequestDepth(Messenger mService, Messenger mServiceMessengerHandler, ContractObj contract, String sBuySell, String sLot,String sLPrice, String sLGT, String sSPrice, String sSGT, String sSlippage, String pid, String reqRate, String ctime)
	{


		Message dealMsg = Message.obtain(null, ServiceFunction.SRV_SEND_DEAL_REQUEST);
		dealMsg.replyTo = mServiceMessengerHandler;

		double dDealRate = 0.0;

//		if(("B".equals(sBuySell) && contract.getBSD() == false) || ("S".equals(sBuySell) && contract.getBSD() == true)){
//			dDealRate = contract.dBidAsk[1];
//		}else{
//			dDealRate = contract.dBidAsk[0];
//		}

		dDealRate = Double.parseDouble(reqRate);

		dealMsg.getData().putString(ServiceFunction.SEND_DEAL_REQUEST_ID, pid);
		dealMsg.getData().putString(ServiceFunction.SEND_DEAL_REQUEST_CONTRACT, contract.strContractCode);
		dealMsg.getData().putInt(ServiceFunction.SEND_DEAL_REQUEST_CONTRACT_SIZE, contract.iContractSize);
		dealMsg.getData().putString(ServiceFunction.SEND_DEAL_REQUEST_BUY_SELL, sBuySell);
		dealMsg.getData().putDouble(ServiceFunction.SEND_DEAL_REQUEST_REQUEST_RATE, dDealRate);
		dealMsg.getData().putString(ServiceFunction.SEND_DEAL_REQUEST_LOT, sLot);

		if(sLPrice != null && !sLPrice.equals("-1")){
			dealMsg.getData().putString(ServiceFunction.SEND_DEAL_REQUEST_LIMIT_PRICE, sLPrice);
			dealMsg.getData().putString(ServiceFunction.SEND_DEAL_REQUEST_LIMIT_GT, sLGT);
		}

		if(sSPrice != null && !sSPrice.equals("-1")){
			dealMsg.getData().putString(ServiceFunction.SEND_DEAL_REQUEST_STOP_PRICE, sSPrice);
			dealMsg.getData().putString(ServiceFunction.SEND_DEAL_REQUEST_STOP_GT, sSGT);
		}

		dealMsg.getData().putString(ServiceFunction.SEND_DEAL_REQUEST_SLIPPAGE, sSlippage);

		if (CompanySettings.AESCrypto) {
			dealMsg.getData().putString(ServiceFunction.SEND_DEAL_REQUEST_UNRESTRICTEDMARKET, CompanySettings.ENABLE_SLIPPAGE? "false" : "true");
			dealMsg.getData().putString(ServiceFunction.SEND_DEAL_REQUEST_TAG, contract.tag);
			dealMsg.getData().putString(ServiceFunction.SEND_DEAL_REQUEST_CTIME, ctime);
		}

		try {
			mService.send(dealMsg);
		} catch (RemoteException e) {
			Log.e(CommonFunction.class.getSimpleName(), "Unable to send login message", e.fillInStackTrace());
		}
	}

	public static void sendLiquidationRequest(Messenger mService, Messenger mServiceMessengerHandler, OpenPositionRecord position, String sLot, String sSlippage, String pid, String ctime){
		try {
			Message liquidationMsg = Message.obtain(null, ServiceFunction.SRV_SEND_LIQUIDATION_REQUEST);
			liquidationMsg.replyTo = mServiceMessengerHandler;
			
			
			String sBuySell = null;
			double dDealRate = 0.0;
			
			double[] dBidAsk = position.contract.getBidAsk();
			
			if(position.isBuyOrder){
				sBuySell = "S";
				if( position.contract.getBSD() == false)
					dDealRate =dBidAsk[0];
				else
					dDealRate =dBidAsk[1];
			}else{
				sBuySell = "B";
				if( position.contract.getBSD() == false)
					dDealRate = dBidAsk[1];
				else
					dDealRate = dBidAsk[0];
			}

			ContractObj contract = position.contract;

			if (contract.enableDepth.equals("1")){
				String sRate = null;
				double lot = Utility.toBigDecimal(sLot, "0").doubleValue();
				double [] bidLot = contract.bidLotDepth;
				double [] askLot = contract.askLotDepth;

				if (("B".equals(position.strBuySell) && contract.getBSD() == false) || ("S".equals(position.strBuySell) && contract.getBSD() == true)) {
					sRate = Utility.round(GUIUtility.getMin(contract.bidPriceDepth), contract.iRateDecPt, contract.iRateDecPt);
					double accLot = 0;
					for (int i = 0 ; i < bidLot.length ; i++){
						accLot += bidLot[i];
						if (lot <= accLot) {
							sRate = Utility.round(contract.bidPriceDepth[i], contract.iRateDecPt, contract.iRateDecPt);
							break;
						}
					}
					dDealRate = Utility.roundToDouble(Double.parseDouble(sRate) + (contract.dOffset + contract.dBidAdjust) * Math.pow(10.0, -contract.iRateDecPt), contract.iRateDecPt, contract.iRateDecPt);
				} else {
					sRate = Utility.round(GUIUtility.getMax(contract.askPriceDepth), contract.iRateDecPt, contract.iRateDecPt);
					double accLot = 0;
					for (int i = 0 ; i < askLot.length ; i++){
						accLot += askLot[i];
						if (lot <= accLot) {
							sRate = Utility.round(contract.askPriceDepth[i], contract.iRateDecPt, contract.iRateDecPt);
							break;
						}
					}
					dDealRate = Utility.roundToDouble(Double.parseDouble(sRate) + (contract.dOffset + contract.dAskAdjust) * Math.pow(10.0, -contract.iRateDecPt), contract.iRateDecPt, contract.iRateDecPt);
				}
			}
			
			double dAmount = Utility.toBigDecimal(sLot, "0").multiply(new BigDecimal(String.valueOf(position.contract.iContractSize))).doubleValue();
			
			liquidationMsg.getData().putString(ServiceFunction.SEND_LIQUIDATION_REQUEST_ID, pid);
			liquidationMsg.getData().putInt(ServiceFunction.SEND_LIQUIDATION_REQUEST_REF, position.iRef);
			liquidationMsg.getData().putString(ServiceFunction.SEND_LIQUIDATION_REQUEST_CONTRACT, position.contract.strContractCode);
			liquidationMsg.getData().putInt(ServiceFunction.SEND_LIQUIDATION_REQUEST_CONTRACT_SIZE, position.contract.iContractSize);
			liquidationMsg.getData().putString(ServiceFunction.SEND_LIQUIDATION_REQUEST_BUY_SELL, sBuySell);
			liquidationMsg.getData().putDouble(ServiceFunction.SEND_LIQUIDATION_REQUEST_REQUEST_RATE, dDealRate);
			liquidationMsg.getData().putDouble(ServiceFunction.SEND_LIQUIDATION_REQUEST_AMOUNT, dAmount);
			liquidationMsg.getData().putString(ServiceFunction.SEND_LIQUIDATION_REQUEST_SLIPPAGE, sSlippage);

			if (CompanySettings.AESCrypto) {
				liquidationMsg.getData().putString(ServiceFunction.SEND_LIQUIDATION_REQUEST_UNRESTRICTEDMARKET, CompanySettings.ENABLE_SLIPPAGE? "false" : "true");
				liquidationMsg.getData().putString(ServiceFunction.SEND_LIQUIDATION_REQUEST_TAG, contract.tag);
				liquidationMsg.getData().putString(ServiceFunction.SEND_LIQUIDATION_REQUEST_CTIME, ctime);
			}
			
			mService.send(liquidationMsg);
		} catch (RemoteException e) {
			Log.e(CommonFunction.class.getSimpleName(), "Unable to send login message", e.fillInStackTrace());
		}
	}
	
	public static void sendLiquidationRequest(Messenger mService, Messenger mServiceMessengerHandler, OpenPositionRecord position, String pid, String ctime){
		try {
			Message liquidationMsg = Message.obtain(null, ServiceFunction.SRV_SEND_LIQUIDATION_REQUEST);
			liquidationMsg.replyTo = mServiceMessengerHandler;
			
			
			String sBuySell = null;
			double dDealRate = 0.0;
			
			double[] dBidAsk = position.contract.getBidAsk();
			
			if(position.isBuyOrder){
				sBuySell = "S";
				if( position.contract.getBSD() == false)
					dDealRate =dBidAsk[0];
				else
					dDealRate =dBidAsk[1];
			}else{
				sBuySell = "B";
				if( position.contract.getBSD() == false)
					dDealRate = dBidAsk[1];
				else
					dDealRate = dBidAsk[0];
			}	
			
			liquidationMsg.getData().putString(ServiceFunction.SEND_LIQUIDATION_REQUEST_ID, pid);
			liquidationMsg.getData().putInt(ServiceFunction.SEND_LIQUIDATION_REQUEST_REF, position.iRef);
			liquidationMsg.getData().putString(ServiceFunction.SEND_LIQUIDATION_REQUEST_CONTRACT, position.contract.strContractCode);
			liquidationMsg.getData().putInt(ServiceFunction.SEND_LIQUIDATION_REQUEST_CONTRACT_SIZE, position.contract.iContractSize);
			liquidationMsg.getData().putString(ServiceFunction.SEND_LIQUIDATION_REQUEST_BUY_SELL, sBuySell);
			liquidationMsg.getData().putDouble(ServiceFunction.SEND_LIQUIDATION_REQUEST_REQUEST_RATE, dDealRate);
			liquidationMsg.getData().putDouble(ServiceFunction.SEND_LIQUIDATION_REQUEST_AMOUNT, position.dAmount);
			// Default Slippage as 0
			liquidationMsg.getData().putString(ServiceFunction.SEND_LIQUIDATION_REQUEST_SLIPPAGE, "0");

			if (CompanySettings.AESCrypto) {
				liquidationMsg.getData().putString(ServiceFunction.SEND_LIQUIDATION_REQUEST_UNRESTRICTEDMARKET, CompanySettings.ENABLE_SLIPPAGE? "false" : "true");
				liquidationMsg.getData().putString(ServiceFunction.SEND_LIQUIDATION_REQUEST_TAG, position.contract.tag);
				liquidationMsg.getData().putString(ServiceFunction.SEND_LIQUIDATION_REQUEST_CTIME, ctime);
			}

			mService.send(liquidationMsg);
		} catch (RemoteException e) {
			Log.e(CommonFunction.class.getSimpleName(), "Unable to send login message", e.fillInStackTrace());
		}
	}		
	
	public static boolean sendLiquidationRequest(Messenger mService, Messenger mServiceMessengerHandler, ContractObj contract, String sBuySell, String ctime){
		OpenPositionRecord op;
		
		if("B".equals(sBuySell)){
			op = contract.getLastBuyPosition();
		}else{
			op = contract.getLastSellPosition();		
		}
		
		String strAccount = getTransactionID(DataRepository.getInstance().getBalanceRecord().strAccount);
		if(op != null){
			CommonFunction.sendLiquidationRequest(mService, mServiceMessengerHandler, op, getTransactionID(strAccount), ctime);
			return true;
		}else{
			return false;
		}
	}	
	
	public static boolean sendLiquidationRequest1(Messenger mService, Messenger mServiceMessengerHandler, List<OpenPositionRecord> alLiqList, String ctime, String pid){
		int iTotalAmount = 0;
		if( alLiqList.size() == 0 )
			return false;
		String strContractCode = alLiqList.get(0).contract.strContractCode;
		String sBuySell = null;
		double dDealRate = 0.0;
		int iContractSize = alLiqList.get(0).contract.iContractSize;
		
		double[] dBidAsk = alLiqList.get(0).contract.getBidAsk();
		
		if("B".equals(alLiqList.get(0).strBuySell)){
			sBuySell = "S";
			if(alLiqList.get(0).contract.getBSD() == false)
				dDealRate = dBidAsk[0];
			else
				dDealRate = dBidAsk[1];
		}else{
			sBuySell = "B";
			if(alLiqList.get(0).contract.getBSD() == false)
				dDealRate = dBidAsk[1];
			else
				dDealRate = dBidAsk[0];
		}	
		
		String strliqRefs = "";
		for(OpenPositionRecord position :alLiqList){
			if( position.contract.strContractCode.compareTo(strContractCode) != 0)
			{
				return false;
			}
			
			strliqRefs += position.iRef + "|" + position.dAmount + ",";
			iTotalAmount += position.dAmount;
		}
		
		try {
			Message liquidationMsg = Message.obtain(null, ServiceFunction.SRV_SEND_LIQUIDATION_REQUEST);
			liquidationMsg.replyTo = mServiceMessengerHandler;
		
			String strAccount = getTransactionID(DataRepository.getInstance().getBalanceRecord().strAccount);
			liquidationMsg.getData().putString(ServiceFunction.SEND_LIQUIDATION_REQUEST_ID, pid);//getTransactionID(strAccount)
			liquidationMsg.getData().putString(ServiceFunction.SEND_LIQUIDATION_REQUEST_REFS, strliqRefs);
			liquidationMsg.getData().putString(ServiceFunction.SEND_LIQUIDATION_REQUEST_CONTRACT, strContractCode);
			liquidationMsg.getData().putInt(ServiceFunction.SEND_LIQUIDATION_REQUEST_CONTRACT_SIZE, iContractSize);
			liquidationMsg.getData().putString(ServiceFunction.SEND_LIQUIDATION_REQUEST_BUY_SELL, sBuySell);
			liquidationMsg.getData().putDouble(ServiceFunction.SEND_LIQUIDATION_REQUEST_REQUEST_RATE, dDealRate);
			liquidationMsg.getData().putDouble(ServiceFunction.SEND_LIQUIDATION_REQUEST_AMOUNT, iTotalAmount);
			// Default Slippage as 0
			liquidationMsg.getData().putString(ServiceFunction.SEND_LIQUIDATION_REQUEST_SLIPPAGE, "0");

			if (CompanySettings.AESCrypto) {
				liquidationMsg.getData().putString(ServiceFunction.SEND_LIQUIDATION_REQUEST_UNRESTRICTEDMARKET, CompanySettings.ENABLE_SLIPPAGE? "false" : "true");
				liquidationMsg.getData().putString(ServiceFunction.SEND_LIQUIDATION_REQUEST_TAG, alLiqList.get(0).contract.tag);
				liquidationMsg.getData().putString(ServiceFunction.SEND_LIQUIDATION_REQUEST_CTIME, ctime);
			}
			
			mService.send(liquidationMsg);
			Thread.sleep(500);
			return true;
		} catch (Exception e) {
			Log.e(CommonFunction.class.getSimpleName(), "Unable to send login message", e.fillInStackTrace());
			return false;
		}
	}	
	
	public static void addOrder(Messenger mService, Messenger mServiceMessengerHandler, 
			ContractObj contract, String sBuySell, String sLot, String sRequestRate, 
			int iGT, int iLimitStop, String sPositionRef, String sOCORef, 
			int iTrail, String limitRate, String stopRate, String pid){
		
		Message orderMsg = Message.obtain(null, ServiceFunction.SRV_SEND_ADD_ORDER_REQUEST);
		orderMsg.replyTo = mServiceMessengerHandler;

		double dCurrRate = 0.0;
		
		double[] dBidAsk = contract.getBidAsk();
		
		if( ("B".equals(sBuySell)  && contract.bBSD == true) || (sBuySell.equals("S") && contract.bBSD == false) ){
			dCurrRate = dBidAsk[0];
		}else{
			dCurrRate = dBidAsk[1];
		}	

		orderMsg.getData().putString(ServiceFunction.SEND_ORDER_REQUEST_ACTION, "new");
		orderMsg.getData().putString(ServiceFunction.SEND_ORDER_REQUEST_CONTRACT, contract.strContractCode);
		orderMsg.getData().putInt(ServiceFunction.SEND_ORDER_REQUEST_CONTRACT_SIZE, contract.iContractSize);
		orderMsg.getData().putString(ServiceFunction.SEND_ORDER_REQUEST_BUY_SELL, sBuySell);
		orderMsg.getData().putString(ServiceFunction.SEND_ORDER_REQUEST_REQUEST_RATE, sRequestRate);		
		orderMsg.getData().putString(ServiceFunction.SEND_ORDER_REQUEST_LOT, sLot);
		orderMsg.getData().putInt(ServiceFunction.SEND_ORDER_REQUEST_LIMIT_STOP, iLimitStop);
		orderMsg.getData().putInt(ServiceFunction.SEND_ORDER_REQUEST_GOOD_TILL, iGT);
		orderMsg.getData().putString(ServiceFunction.SEND_ORDER_REQUEST_POSITION_REF, sPositionRef);
		//orderMsg.getData().putDouble(ServiceFunction.SEND_ORDER_REQUEST_POSITION_AMOUNT, dAmount);
		orderMsg.getData().putString(ServiceFunction.SEND_ORDER_REQUEST_OCO, sOCORef);
		orderMsg.getData().putInt(ServiceFunction.SEND_ORDER_REQUEST_TRAILING_STOP_SIZE, iTrail);
		orderMsg.getData().putDouble(ServiceFunction.SEND_ORDER_REQUEST_CURR_RATE, dCurrRate);
		orderMsg.getData().putString(ServiceFunction.SEND_ORDER_REQUEST_PROFIT_RATE, limitRate);
		orderMsg.getData().putString(ServiceFunction.SEND_ORDER_REQUEST_CUT_RATE, stopRate);
		orderMsg.getData().putString("pid", pid);
		
		try {
			mService.send(orderMsg);
		} catch (RemoteException e) {
			Log.e(CommonFunction.class.getSimpleName(), "Unable to send login message", e.fillInStackTrace());
		}
	}	
	 
	public static void cancelOrder(Messenger mService, Messenger mServiceMessengerHandler, int iRef){		
		Message cancelOrderMsg = Message.obtain(null, ServiceFunction.SRV_SEND_CANCEL_ORDER_REQUEST);
		cancelOrderMsg.replyTo = mServiceMessengerHandler;
		cancelOrderMsg.getData().putInt(ServiceFunction.SEND_CANCEL_ORDER_REQUEST_ORDER_REF, iRef);
		
		try {
			mService.send(cancelOrderMsg);
		} catch (RemoteException e) {
			Log.e(CommonFunction.class.getSimpleName(), "Unable to send login message", e.fillInStackTrace());
		}
	}		
/*	 
	public static void editOrder(Messenger mService, Messenger mServiceMessengerHandler, OrderRecord order, 
			String sLot, String sRequestRate, int iGT, String sOCORef){		
		
		Message editOrderMsg = Message.obtain(null, ServiceFunction.SRV_SEND_EDIT_ORDER_REQUEST);

		double dCurrRate = 0.0;		
		double[] dBidAsk = order.contract.getBidAsk();
		
		if("B".equals(order.strBuySell)){
			dCurrRate = dBidAsk[1];
		}else{
			dCurrRate = dBidAsk[0];
		}	
		
		editOrderMsg.getData().putString(ServiceFunction.SEND_EDIT_ORDER_REQUEST_ACTION, "amend");		
		editOrderMsg.getData().putString(ServiceFunction.SEND_EDIT_ORDER_REQUEST_REQUEST_RATE, sRequestRate);		
		editOrderMsg.getData().putString(ServiceFunction.SEND_EDIT_ORDER_REQUEST_OCO, sOCORef);
		editOrderMsg.getData().putString(ServiceFunction.SEND_EDIT_ORDER_REQUEST_LOT, sLot);
		editOrderMsg.getData().putInt(ServiceFunction.SEND_EDIT_ORDER_REQUEST_GOOD_TILL, iGT);		
		editOrderMsg.getData().putDouble(ServiceFunction.SEND_EDIT_ORDER_REQUEST_CURR_RATE, dCurrRate);
		
		try {
			mService.send(editOrderMsg);
		} catch (RemoteException e) {
			Log.e(CommonFunction.class.getSimpleName(), "Unable to send login message", e.fillInStackTrace());
		}
	}	
*/	
	public static void editOrder(Messenger mService, Messenger mServiceMessengerHandler, 
			OrderRecord order, String sLot, String sRequestRate, 
			int iGT, String sPositionRef, double dAmount, String sOCORef, int iTrail, String limitRate, String stopRate, String pid){
		
		Message editOrderMsg = Message.obtain(null, ServiceFunction.SRV_SEND_EDIT_ORDER_REQUEST);
		editOrderMsg.replyTo = mServiceMessengerHandler;

		double dCurrRate = 0.0;
		
		double[] dBidAsk = order.contract.getBidAsk();
		
		if( ("B".equals(order.strBuySell) && order.contract.getBSD() == false) || ("S".equals(order.strBuySell) && order.contract.getBSD() == true) ){
			dCurrRate = dBidAsk[1];
		}else{
			dCurrRate = dBidAsk[0];
		}	
		
		editOrderMsg.getData().putInt(ServiceFunction.SEND_EDIT_ORDER_REQUEST_ORDER_REF, order.iRef);
		editOrderMsg.getData().putString(ServiceFunction.SEND_EDIT_ORDER_REQUEST_ACTION, "amend");
		editOrderMsg.getData().putString(ServiceFunction.SEND_EDIT_ORDER_REQUEST_CONTRACT, order.contract.strContractCode);
		editOrderMsg.getData().putInt(ServiceFunction.SEND_EDIT_ORDER_REQUEST_CONTRACT_SIZE, order.contract.iContractSize);
		editOrderMsg.getData().putString(ServiceFunction.SEND_EDIT_ORDER_REQUEST_BUY_SELL, order.strBuySell);
		editOrderMsg.getData().putString(ServiceFunction.SEND_EDIT_ORDER_REQUEST_REQUEST_RATE, sRequestRate);		
		editOrderMsg.getData().putString(ServiceFunction.SEND_EDIT_ORDER_REQUEST_LOT, sLot);
		editOrderMsg.getData().putInt(ServiceFunction.SEND_EDIT_ORDER_REQUEST_LIMIT_STOP, order.iLimitStop);
		editOrderMsg.getData().putInt(ServiceFunction.SEND_EDIT_ORDER_REQUEST_GOOD_TILL, iGT);
		editOrderMsg.getData().putString(ServiceFunction.SEND_EDIT_ORDER_REQUEST_LIQ_METHOD, sPositionRef.equals("-1")?"-1":"3");
		editOrderMsg.getData().putString(ServiceFunction.SEND_EDIT_ORDER_REQUEST_POSITION_REF, sPositionRef);
		editOrderMsg.getData().putDouble(ServiceFunction.SEND_EDIT_ORDER_REQUEST_POSITION_AMOUNT, dAmount);
		editOrderMsg.getData().putString(ServiceFunction.SEND_EDIT_ORDER_REQUEST_OCO, sOCORef);
		editOrderMsg.getData().putInt(ServiceFunction.SEND_EDIT_ORDER_REQUEST_TRAILING_STOP_SIZE, iTrail);
		editOrderMsg.getData().putDouble(ServiceFunction.SEND_EDIT_ORDER_REQUEST_CURR_RATE, dCurrRate);
		editOrderMsg.getData().putString(ServiceFunction.SEND_ORDER_REQUEST_PROFIT_RATE, limitRate);
		editOrderMsg.getData().putString(ServiceFunction.SEND_ORDER_REQUEST_CUT_RATE, stopRate);
		editOrderMsg.getData().putString("pid", pid);
		
		try {
			mService.send(editOrderMsg);
		} catch (RemoteException e) {
			Log.e(CommonFunction.class.getSimpleName(), "Unable to send login message", e.fillInStackTrace());
		}
	}	
	
	public static void equeryExecutedOrder(Messenger mService, Messenger mServiceMessengerHandler, 
			String sFrom, String sTo){
		
		Message historyRequestMsg = Message.obtain(null, ServiceFunction.SRV_SEND_EXECUTED_ORDER_HISTORY_REQUEST);
		historyRequestMsg.replyTo = mServiceMessengerHandler;

		historyRequestMsg.getData().putString(ServiceFunction.SRV_SEND_EXECUTED_ORDER_HISTORY_REQUEST_FROM, sFrom);
		historyRequestMsg.getData().putString(ServiceFunction.SRV_SEND_EXECUTED_ORDER_HISTORY_REQUEST_TO, sTo);
		
		try {
			mService.send(historyRequestMsg);
		} catch (RemoteException e) {
			Log.e(CommonFunction.class.getSimpleName(), "Unable to send executed order history message", e.fillInStackTrace());
		}
	}	
	
	public static void equeryCancelledOrder(Messenger mService, Messenger mServiceMessengerHandler, 
			String sFrom, String sTo){
		
		Message historyRequestMsg = Message.obtain(null, ServiceFunction.SRV_SEND_CANCELLED_ORDER_HISTORY_REQUEST);
		historyRequestMsg.replyTo = mServiceMessengerHandler;

		historyRequestMsg.getData().putString(ServiceFunction.SRV_SEND_CANCELLED_ORDER_HISTORY_REQUEST_FROM, sFrom);
		historyRequestMsg.getData().putString(ServiceFunction.SRV_SEND_CANCELLED_ORDER_HISTORY_REQUEST_TO, sTo);
		
		try {
			mService.send(historyRequestMsg);
		} catch (RemoteException e) {
			Log.e(CommonFunction.class.getSimpleName(), "Unable to send executed order history message", e.fillInStackTrace());
		}
	}	
	
	public static void equeryLiquidationHistory(Messenger mService, Messenger mServiceMessengerHandler, 
			String sFrom, String sTo){
		
		Message historyRequestMsg = Message.obtain(null, ServiceFunction.SRV_SEND_LIQUIDATION_HISTORY_REQUEST);
		historyRequestMsg.replyTo = mServiceMessengerHandler;

		historyRequestMsg.getData().putString(ServiceFunction.SRV_SEND_LIQUIDATION_HISTORY_REQUEST_FROM, sFrom);
		historyRequestMsg.getData().putString(ServiceFunction.SRV_SEND_LIQUIDATION_HISTORY_REQUEST_TO, sTo);
		
		try {
			mService.send(historyRequestMsg);
		} catch (RemoteException e) {
			Log.e(CommonFunction.class.getSimpleName(), "Unable to send executed order history message", e.fillInStackTrace());
		}
	}		
	
	public static void sendCashMovementRequest(Messenger mService, Messenger mServiceMessengerHandler,
											   String amount, CashMovementRequestProcessor.RequestType type, String bankacc){

		Message historyRequestMsg = Message.obtain(null, ServiceFunction.SRV_SEND_CASH_MOVEMENT_REQUEST);
		historyRequestMsg.arg1 = type.getInteger();
		historyRequestMsg.replyTo = mServiceMessengerHandler;
		HashMap<String, String> request = new HashMap<>();
		request.put(Protocol.CashMovementRequest.AMOUNT, amount);
		request.put(Protocol.CashMovementRequest.BANKACCOUNT, bankacc);
		historyRequestMsg.getData().putSerializable("request", request);
		try {
			mService.send(historyRequestMsg);
		} catch (RemoteException e) {
			Log.e(CommonFunction.class.getSimpleName(), "Unable to send CashMovement message", e.fillInStackTrace());
		}
	}

	public static void sendBankInfoRequest(Messenger mService, Messenger mServiceMessengerHandler){

		Message historyRequestMsg = Message.obtain(null, ServiceFunction.SRV_SEND_BANK_INFO_REQUEST);
		historyRequestMsg.replyTo = mServiceMessengerHandler;

		try {
			mService.send(historyRequestMsg);
		} catch (RemoteException e) {
			Log.e(CommonFunction.class.getSimpleName(), "Unable to sendBankInfoRequest message", e.fillInStackTrace());
		}
	}

	public static void sendCompanyInfoRequest(Messenger mService, Messenger mServiceMessengerHandler){

		Message historyRequestMsg = Message.obtain(null, ServiceFunction.SRV_SEND_COMPANY_INFO_REQUEST);
		historyRequestMsg.replyTo = mServiceMessengerHandler;

		try {
			mService.send(historyRequestMsg);
		} catch (RemoteException e) {
			Log.e(CommonFunction.class.getSimpleName(), "Unable to sendBankInfoRequest message", e.fillInStackTrace());
		}
	}

	public static void enqueryCashMovementHistory(Messenger mService, Messenger mServiceMessengerHandler,
												  String sFrom, String sTo){
		
		Message historyRequestMsg = Message.obtain(null, ServiceFunction.SRV_SEND_CASH_MOVEMENT_HISTORY_REQUEST);
		historyRequestMsg.replyTo = mServiceMessengerHandler;

		historyRequestMsg.getData().putString(ServiceFunction.SRV_SEND_LIQUIDATION_HISTORY_REQUEST_FROM, sFrom);
		historyRequestMsg.getData().putString(ServiceFunction.SRV_SEND_LIQUIDATION_HISTORY_REQUEST_TO, sTo);
		
		try {
			mService.send(historyRequestMsg);
		} catch (RemoteException e) {
			Log.e(CommonFunction.class.getSimpleName(), "Unable to send enqueryCashMovementHistory message", e.fillInStackTrace());
		}
	}

	public static void sendPriceAlertRequest(Messenger mService, Messenger mServiceMessengerHandler, String mode, String contract, String type, String goodtill, String alertPrice, String alertVolatility, String id, String active)
	{
		Message priceAlertMsg = Message.obtain(null, ServiceFunction.SRV_PRICE_ALERT_UPDATE);
		priceAlertMsg.replyTo = mServiceMessengerHandler;

		priceAlertMsg.getData().putString(ServiceFunction.SEND_PRICE_ALERT_MODE, mode);
		priceAlertMsg.getData().putString(ServiceFunction.SEND_PRICE_ALERT_CONTRACT, contract);
		String strAccount = DataRepository.getInstance().getBalanceRecord().strAccount;
		priceAlertMsg.getData().putString(ServiceFunction.SEND_PRICE_ALERT_ACCOUNT, strAccount);
		priceAlertMsg.getData().putString(ServiceFunction.SEND_PRICE_ALERT_TYPE, type);
		priceAlertMsg.getData().putString(ServiceFunction.SEND_PRICE_ALERT_GOODTILL, goodtill);
		priceAlertMsg.getData().putString(ServiceFunction.SEND_PRICE_ALERT_PRICE, alertPrice);
		priceAlertMsg.getData().putString(ServiceFunction.SEND_PRICE_ALERT_VOLATILITY, alertVolatility);
		priceAlertMsg.getData().putString(ServiceFunction.SEND_PRICE_ALERT_ID, id);
		priceAlertMsg.getData().putString(ServiceFunction.SEND_PRICE_ALERT_ACTIVE, active);

		try {
			mService.send(priceAlertMsg);
		} catch (RemoteException e) {
			Log.e(CommonFunction.class.getSimpleName(), "Unable to send priceAlertMsg message", e.fillInStackTrace());
		}
	}
	
	public static void moveTo(Messenger mService, Messenger mServiceMessengerHandler, int iMoTo, Bundle data){
		Message msMoveTo = Message.obtain(null, iMoTo);
		
		msMoveTo.replyTo = mServiceMessengerHandler;	
		msMoveTo.setData(data);
		try {
			mService.send(msMoveTo);
		} catch (RemoteException e) {
			Log.e(CommonFunction.class.getSimpleName(), "Unable to send moving to contract detail", e.fillInStackTrace());
		} catch (NullPointerException n){
			//
		}
	}

	public String decryptText(CharSequence szInput)
	{
		byte[] byInput = null;
		if ((szInput == null) || (nBox == null))
			return(null) ;

		byInput = hexDecode(szInput);

		return(performOperation(byInput,false)) ;
	}

	public byte[] hexDecode(CharSequence szInput)
	{
		int nDigit1,nDigit2 ;

		if (szInput == null)
			return(null) ;
		int nLen = szInput.length() ;
		byte[] byRet = new byte[nLen/2] ;
		for (int i=0; i<byRet.length; i++)
		{
			nDigit1 = szInput.charAt(i*2) ;
			nDigit2 = szInput.charAt(i*2+1) ;
			if ((nDigit1 >= '0') && (nDigit1 <= '9'))
				nDigit1 -= '0' ;
			else
			if ((nDigit1 >= 'a') && (nDigit1 <= 'f'))
				nDigit1 -= 'a' - 10 ;
			else
				return(null) ;
			if ((nDigit2 >= '0') && (nDigit2 <= '9'))
				nDigit2 -= '0' ;
			else
			if ((nDigit2 >= 'a') && (nDigit2 <= 'f'))
				nDigit2 -= 'a' - 10 ;
			else
				return(null) ;
			byRet[i] = (byte)((nDigit1 << 4) + nDigit2) ;
		}
		return(byRet) ;
	}
    
    
}