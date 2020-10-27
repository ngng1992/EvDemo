package com.mfinance.everjoy.app.util;

import java.io.UnsupportedEncodingException ;
import java.security.* ;
import java.text.SimpleDateFormat;
import java.io.IOException;
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