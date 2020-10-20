package com.mfinance.everjoy.app.util;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.mfinance.everjoy.app.CompanySettings;
import com.mfinance.everjoy.app.MobileTraderApplication;
import com.mfinance.everjoy.app.constant.IDDictionary;
import com.mfinance.everjoy.app.model.DataRepository;


public class MessageObj
{	
	private static ConcurrentLinkedQueue<MessageObj> clqMessageObj = new ConcurrentLinkedQueue<MessageObj>();
	private static ConcurrentLinkedQueue<PriceMessageObj> clqPriceMessageObj = new ConcurrentLinkedQueue<PriceMessageObj>();
	
	private static int KEY_CODE = -1;
	private static String ENCRYTION_KEY = null;	
	
	public static final char ENCRYPTED_MESSAGE = 19;
	public static final char PLAIN_MESSAGE = 147;
	
    public static final char fieldDelimitor = 17;
    public static final char pairDelimitor = 18;

    public static final char bodyFieldDelimitor = 19;
    public static final char bodyPairDelimitor = 20;

    protected int m_serviceType;
    protected int m_functionType;
    protected Hashtable<String, String> m_body;

    public boolean bIsMessageKeyLastVersion = false;

    private CommonFunction cf = new CommonFunction(true);

    private AESLib aes = null;

    private static final String EQUAL = "=";
    private static final String AMP = "&";

    private static ArrayList<String> tradeCheckSumIgnoreKeysList = new ArrayList<String>();

    static
    {
        tradeCheckSumIgnoreKeysList.add("uuid");
        tradeCheckSumIgnoreKeysList.add("gc");
        tradeCheckSumIgnoreKeysList.add("sum");
        tradeCheckSumIgnoreKeysList.add("frid");
        tradeCheckSumIgnoreKeysList.add("frname");
        tradeCheckSumIgnoreKeysList.add("hasDealer");
        tradeCheckSumIgnoreKeysList.add("accountID");
        tradeCheckSumIgnoreKeysList.add("hier");
        tradeCheckSumIgnoreKeysList.add("ip");
        tradeCheckSumIgnoreKeysList.add("mydealer");
        tradeCheckSumIgnoreKeysList.add("pid");
        tradeCheckSumIgnoreKeysList.add("pkey");
        tradeCheckSumIgnoreKeysList.add("role");
        tradeCheckSumIgnoreKeysList.add("userType");
    }

    protected MessageObj()
	{
        m_body = new Hashtable<String, String>();
        m_serviceType = 0;
        m_functionType = 0;
	}
	public MessageObj(int iServiceType, int iFunctionType)
	{
        m_body = new Hashtable<String, String>();
        m_serviceType = iServiceType;
        m_functionType = iFunctionType;       
	}
	
	protected static MessageObj getMessageObj(){
		MessageObj msgObj = null;
		
		if(!MessageObj.clqMessageObj.isEmpty()){
			msgObj = MessageObj.clqMessageObj.poll();
		}
		
		if(msgObj == null){
			msgObj = new MessageObj();
		}
		return msgObj;
	}
	
	protected static MessageObj getPriceMessageObj(){
		MessageObj msgObj = null;
		
		if(!MessageObj.clqPriceMessageObj.isEmpty()){
			msgObj = MessageObj.clqPriceMessageObj.poll();
		}
		
		if(msgObj == null){
			msgObj = new PriceMessageObj();
		}
		return msgObj;
	}
	
	public static MessageObj getMessageObj(int iServiceType, int iFunctionType){
		MessageObj msgObj;
		if( CompanySettings.FORCE_NEW_PRICE_STREAMING_PROTOCOL == true &&
				iServiceType == IDDictionary.TRADER_LIVE_PRICE_TYPE && iFunctionType == IDDictionary.TRADER_UPDATE_STREAM_PRICE)
			msgObj = getPriceMessageObj();
		else
			msgObj = getMessageObj();
		
		msgObj.setType(iServiceType, iFunctionType);
		return msgObj;
	}
	
	public void release(){
		//System.out.println("Message Poll release - "+MessageObj.clqMessageObj.size());
		clearBody();

		if( CompanySettings.FORCE_NEW_PRICE_STREAMING_PROTOCOL == true &&
				m_serviceType == IDDictionary.TRADER_LIVE_PRICE_TYPE && m_functionType == IDDictionary.TRADER_UPDATE_STREAM_PRICE)
			MessageObj.clqPriceMessageObj.add((PriceMessageObj) this);
		else
			MessageObj.clqMessageObj.add(this);
		
		m_serviceType = 0;
		m_functionType = 0;
	}
	

    public String convertToString()
    {
        return convertToString(false, false);
    }

    public String convertToString(boolean bNeedEncrypt)
    {
        return convertToString(bNeedEncrypt, false);
    }

    public String convertToString(boolean bNeedEncrypt, boolean bIsOld)
    {
        String tmpMsg = null;
        StringBuffer tmpBuf = new StringBuffer();
        String strFieldId;
        String strFieldValue;
        Iterator<String> currIterator;
            
        currIterator = m_body.keySet().iterator();
        while (currIterator.hasNext())
        {
            strFieldId = currIterator.next();
            strFieldValue = m_body.get(strFieldId);

            //tmpMsg += strFieldId + fieldDelimitor + strFieldValue + pairDelimitor;
			tmpBuf.append(strFieldId).append(fieldDelimitor).append(strFieldValue).append(pairDelimitor);			
        }

		try {
            if (bNeedEncrypt)
            {
                if (CompanySettings.AESCrypto){
                    tmpMsg = encrypt(tmpBuf.toString());
                }else {
                    if (ENCRYTION_KEY == null /*&& bIsMessageKeyLastVersion*/)
                        cf.setKey(swapChar(Utility.getMessageKey()));
                    else
                        cf.setKey(ENCRYTION_KEY);
                    tmpMsg = cf.encryptText(tmpBuf.toString());
                }
            }
            else
                tmpMsg = tmpBuf.toString();
		}
		catch(Throwable t)
		{
			Utility.printStackTrace(t);
			return "";
		}
        
		String strHeader = constructHeader(m_serviceType, m_functionType, tmpMsg.length());

        strFieldId = null;
        strFieldValue = null;
        currIterator = null;

        //return strHeader + tmpMsg;

		tmpBuf.setLength(0);
		tmpBuf.append(strHeader).append(tmpMsg);

		return tmpBuf.toString();
    }

    public boolean parse(String strBody)
    {
        return parse(strBody, false, false, false);
    }

    public boolean parse(String strBody, boolean bBodyDelimitor)
    {
        return parse(strBody, bBodyDelimitor, false, false);
    }

    public boolean parse(String strBody, boolean bBodyDelimitor, boolean bNeedDecrypt)
    {
        return parse(strBody, bBodyDelimitor, bNeedDecrypt, false);
	}

    public boolean parse (String strBody, boolean bBodyDelimitor, boolean bNeedDecrypt, boolean bIsOld)
    {
      //if (strBody == null || strBody.compareTo("") == 0)
      //{
      //return false;
      //}
  	  m_body.clear();
      String decryptedBody;

      if (bNeedDecrypt)
      {
          if (CompanySettings.AESCrypto){
              decryptedBody = decrypt(strBody.toString());
          }
          else {
              if (ENCRYTION_KEY == null)
                  cf.setKey(swapChar(Utility.getMessageKey()));
              else
                  cf.setKey(ENCRYTION_KEY);
              decryptedBody = cf.decryptText(strBody);
          }
      }
      else
      {
        decryptedBody = strBody;
      }

      char pDelimitor, fDelimitor;
      if (bBodyDelimitor)
      {
        pDelimitor = bodyPairDelimitor;
        fDelimitor = bodyFieldDelimitor;
      }
      else
      {
        pDelimitor = pairDelimitor;
        fDelimitor = fieldDelimitor;
      }

      if (!m_body.isEmpty())
      {
        m_body.clear();
      }

      try
      {
        //StringTokenizer st = new StringTokenizer(strBody, pDelimitor + "");
      	int pairIndex = -1;
      	int currentCursor = -1;
        //StringTokenizer st = new StringTokenizer(decryptedBody, String.valueOf(pDelimitor));
        //StringTokenizer pair;
        String strFieldId;
        String strFieldValue;
       // while (st.hasMoreElements())
        //System.out.println(pairIndex=decryptedBody.indexOf(pDelimitor,count+1));
        while ((pairIndex=decryptedBody.indexOf(pDelimitor,currentCursor+1))>0)
        {
      	  //String token = st.nextToken();
      	  int fieldIndex = decryptedBody.indexOf(fDelimitor,currentCursor+1);
      	  if (fieldIndex>0) //has field
      	  {
      		  strFieldId = decryptedBody.substring(currentCursor+1,fieldIndex);
      		  strFieldValue = decryptedBody.substring(fieldIndex+1,pairIndex);
      		  m_body.put(strFieldId, strFieldValue);
      	  }
      	  currentCursor = pairIndex;
      	  
          /*pair = new StringTokenizer(token, String.valueOf(fDelimitor));
          strFieldId = pair.nextToken();
          if (pair.hasMoreElements())
          {
            strFieldValue = pair.nextToken();
            

          }
          else
          {
            strFieldValue = "";
          }*/
          //System.out.println("Add : " + strFieldId + "=" + strFieldValue);
      	//  m_body.put(strFieldId, strFieldValue);
          //System.out.println(strFieldId+","+strFieldValue);
          //pair = null;
        }
        //st = null;
      }
      catch (NoSuchElementException nsee)
      {
        m_body.clear();
        return false;
      }
      return true;
    }

    public boolean addField(String strField, String strValue)
    {
        if (m_body.containsKey(strField))
            return false;
        else
        {
            m_body.put(strField, (strValue==null?"":strValue));
            return true;
        }
    }
    public boolean removeField(String strField)
    {
        if (m_body.containsKey(strField))
        {
            m_body.remove(strField);
            return true;
        }
        else
            return false;
    }

    public boolean setField(String strField, String strValue)
    {
        m_body.remove(strField);
        m_body.put(strField, strValue);
        return true;
    }

    public String getField(String strFieldId)
    {
        return (String) m_body.get(strFieldId);
    }

    public void setServiceType(int iServiceType)
    {
        m_serviceType = iServiceType;
    }

    public int getServiceType()
    {
        return m_serviceType;
    }

    public void setFunctionType(int iFunctionType)
    {
        m_functionType = iFunctionType;
    }

    public int getFunctionType()
    {
        return m_functionType;
    }

    public void setType(int iServiceType, int iFunctionType)
    {
        m_serviceType = iServiceType;
        m_functionType = iFunctionType;
    }

    public void clearBody()
    {
        m_body.clear();
    }

// <19><ServiceType><FunctionType>"Body size"
//Changed from public to private!!!! Don't be called from outside since need encryption
	private static String constructHeader(int iServiceType, int iFunctionType, int bodySize)
    {
        byte byteHeader[] = new byte[3];
        if(KEY_CODE==-1)
        	byteHeader = new byte[3];
        else{
        	byteHeader = new byte[4];
        	byteHeader[3] = (byte) KEY_CODE;
        }
        String strBodySize;
        byteHeader[0] = (byte) 19;
        byteHeader[1] = (byte) iServiceType;
        byteHeader[2] = (byte) iFunctionType;
        String strHeader = "";
		try {
 			strHeader = new String(byteHeader, "ISO-8859-1");
		}
		catch(Throwable t) {
			Utility.printStackTrace(t);
		}
        strBodySize = String.valueOf(bodySize);
        while (strBodySize.length() < 7)
            strBodySize += " ";

        return strHeader + strBodySize;
    }

    public boolean convertHeaderType()
    {
        int nServiceType = -1;
        int nFunctionType = -1;
        String strNum;
        try{
            strNum = getField("srv");
            if (strNum.equals(""))
                nServiceType = -1;
            else
                nServiceType = Integer.parseInt(strNum);
            strNum = getField("fnc");
            if (strNum.equals(""))
                nFunctionType = -1;
            else
                nFunctionType = Integer.parseInt(strNum);
        }catch(NumberFormatException nfe)
        {
            nServiceType = -1;
            nFunctionType = -1;
        }

        if (nServiceType != -1 && nFunctionType != -1)
        {
            setType(nServiceType, nFunctionType);
            removeField("src");
            removeField("fnc");
            return true;
        }
        else
            return false;
    }

    public String extractBody(boolean bBodyDelimitor)
    {
        char pDelimitor, fDelimitor;
        if (bBodyDelimitor)
        {
            pDelimitor = bodyPairDelimitor;
            fDelimitor = bodyFieldDelimitor;
        }
        else
        {
            pDelimitor = pairDelimitor;
            fDelimitor = fieldDelimitor;
        }
        //String tmpMsg = "";
        StringBuffer tmpBuf = new StringBuffer();
        String strFieldId;
        String strFieldValue;
        Iterator<String> currIterator;
            
        currIterator = m_body.keySet().iterator();
        while (currIterator.hasNext())
        {
            strFieldId = currIterator.next();
            strFieldValue = m_body.get(strFieldId);

            //tmpMsg += strFieldId + fDelimitor + strFieldValue + pDelimitor;
			tmpBuf.append(strFieldId).append(fDelimitor).append(strFieldValue).append(pDelimitor);
        }
        //return tmpMsg;
		return tmpBuf.toString();
    }

    public Map<String, String> cloneBody()
    {
        return (Hashtable<String, String>)m_body.clone();
    }

    public void importBody(Map<String, String> inBody)
    {
        m_body.putAll(inBody);
    }
    
    
    public String getKey(){
    	StringBuilder sb = new StringBuilder();
    	sb.append(m_serviceType).append("|").append(m_functionType);
    	String sKey = sb.toString();
    	sb = null;
    	return sKey;
    }
    
	public static void setKey(int keyCode, String encrytionKey) {
		MessageObj.KEY_CODE = keyCode;
		MessageObj.ENCRYTION_KEY = encrytionKey;
	}
	
	private String swapChar(String messageKey) {
		/* only swap characters if the returned key id is greater than LAST_OLD_KEY */
		/*final int SWAP_POSITION_1 = 21;
		final int SWAP_POSITION_2 = 23;

		String finalMessageKey = messageKey;
		MobileTraderApplication app = (MobileTraderApplication) MobileTraderApplication.context.getApplicationContext();
		finalMessageKey = swapCharBetweenIndex(messageKey, SWAP_POSITION_1, SWAP_POSITION_2);
		return finalMessageKey;
		*/
		return messageKey;
	}

	private String swapCharBetweenIndex(String messageKey, int index1, int index2) {
		StringBuilder finalMessageKey = new StringBuilder(messageKey);
		finalMessageKey.setCharAt(index1, messageKey.substring(index2, index2+1).charAt(0));
		finalMessageKey.setCharAt(index2, messageKey.substring(index1, index1+1).charAt(0));
		return finalMessageKey.toString();
	}

    public String convertToStringWithoutHeader()
    {
        String tmpMsg = null;
        StringBuffer tmpBuf = new StringBuffer();
        String strFieldId;
        String strFieldValue;

        synchronized(m_body)
        {
            Iterator<String> currIterator = m_body.keySet().iterator();
            while (currIterator.hasNext())
            {
                strFieldId = (String) currIterator.next();
                strFieldValue = (String) m_body.get(strFieldId);

                tmpBuf.append(strFieldId).append(fieldDelimitor).append(strFieldValue).append(pairDelimitor);
            }
        }

        tmpMsg = tmpBuf.toString();

        return tmpMsg;
    }

    public static boolean isHexBodySizeMessage(int serviceType, int functionType) {
        if (serviceType == IDDictionary.TRADER_HISTORY_LIST_TYPE && functionType == IDDictionary.TRADER_TRADES_OPEN_DEAL)
            return true;
        return false;
    }

    public boolean parse(CharSequence strBody)
    {
        return parse(strBody, false, false, false);
    }

    public boolean parse(CharSequence strBody, boolean bBodyDelimitor)
    {
        return parse(strBody, bBodyDelimitor, false, false);
    }

    public boolean parse(CharSequence strBody, boolean bBodyDelimitor, boolean bNeedDecrypt)
    {
        return parse(strBody, bBodyDelimitor, bNeedDecrypt, false);
    }

    public boolean parse(CharSequence strBody, boolean bBodyDelimitor, boolean bNeedDecrypt, boolean bIsOld)
    {
        String decryptedBody;

        if (bNeedDecrypt)
        {
            if (CompanySettings.AESCrypto){
                decryptedBody = decrypt(strBody.toString());
            }else {
                cf.setKey(Utility.getMessageKey());
                decryptedBody = cf.decryptText(strBody);
                if ("".equals(decryptedBody) && !Utility.getMessageKey().equals(Utility.getMessageKey_LastVersion())) {
                    cf.setKey(Utility.getMessageKey_LastVersion());
                    decryptedBody = cf.decryptText(strBody);
                    if (!"".equals(decryptedBody)) {
                        bIsMessageKeyLastVersion = true;
                    }
                }
            }
        }
        else
        {
            decryptedBody = strBody.toString();
        }


        char pDelimitor, fDelimitor;
        if (bBodyDelimitor)
        {
            pDelimitor = bodyPairDelimitor;
            fDelimitor = bodyFieldDelimitor;
        }
        else
        {
            pDelimitor = pairDelimitor;
            fDelimitor = fieldDelimitor;
        }

        synchronized(m_body)
        {
            if (!m_body.isEmpty())
                m_body.clear();

            //David 20110225 fix null pointer exception
            if(decryptedBody == null)
                return false;

            try
            {
                //StringTokenizer st = new StringTokenizer(strBody, pDelimitor + "");
                StringTokenizer st = new StringTokenizer(decryptedBody, String.valueOf(pDelimitor));
                //David 20110225 fix null pointer exception
                while (st.hasMoreElements())
                {
                    String token = st.nextToken();
                    if(token != null)
                    {
                        String strFieldId;
                        String strFieldValue;
                        StringTokenizer pair = new StringTokenizer(token, String.valueOf(fDelimitor));
                        if(pair.hasMoreElements())
                        {
                            strFieldId = pair.nextToken();
                        }else
                        {
                            strFieldId = "";
                        }
                        if (pair.hasMoreElements())
                            strFieldValue = pair.nextToken();
                        else
                            strFieldValue = "";
                        //System.out.println("Add : " + strFieldId + "=" + strFieldValue);
                        m_body.put(strFieldId.intern(), strFieldValue);
                    }
                }
            }
            catch (NoSuchElementException nsee)
            {
                m_body.clear();
                return false;
            }
        }
        return true;
    }

    private String encrypt(String str)
    {
        if (aes == null)
        {
            aes = new AESLib();
        }
        return aes.encrypt(str);
    }

    private String decrypt(String str)
    {
        if (aes == null)
        {
            aes = new AESLib();
        }
        return aes.decrypt(str);
    }

    public String getTradeCheckSum()
    {
        StringBuilder sb = new StringBuilder();
        try
        {
            String key;
            TreeMap<String, String> tm = new TreeMap<String, String>();
            synchronized(m_body)
            {
                Iterator<String> currIterator = m_body.keySet().iterator();
                while (currIterator.hasNext())
                {
                    key = (String) currIterator.next();
                    if (!tradeCheckSumIgnoreKeysList.contains(key))
                    {
                        tm.put(key, (String) m_body.get(key));
                    }
                }
            }
            Iterator<String> currIterator = tm.keySet().iterator();
            while (currIterator.hasNext())
            {
                key = (String) currIterator.next();
                sb.append(AMP).append(key).append(EQUAL).append(tm.get(key));
            }
            sb.deleteCharAt(0);
            tm.clear();
            return sb.toString();
        }
        catch (Exception e)
        {
            return null;
        }
        finally
        {
            sb.setLength(0);
        }
    }
}
