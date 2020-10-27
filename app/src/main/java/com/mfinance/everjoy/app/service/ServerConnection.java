package com.mfinance.everjoy.app.service;

import android.util.Log;
import android.util.Pair;

import com.mfinance.everjoy.BuildConfig;
import com.mfinance.everjoy.app.CompanySettings;
import com.mfinance.everjoy.app.constant.IDDictionary;
import com.mfinance.everjoy.app.constant.Protocol;
import com.mfinance.everjoy.app.util.AESLib;
import com.mfinance.everjoy.app.util.MessageObj;
import com.mfinance.everjoy.app.util.PriceMessageObj;
import com.mfinance.everjoy.app.util.Utility;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Connection handler
 *  
 *  
 *  @author        : justin.lai
 *  @version       : v1.00
 *  
 *  
 *  @ModificationHistory  
 *  Who			When			Version			What<br>
 *  -------------------------------------------------------------------------------<br> 
 *  justin.lai	20110413		v1.00			Creation<br>
 *  
 *  
**
 */
public class ServerConnection {
	/**
	 * Socket time out interval
	 */
	protected final int TIME_OUT = 60000;
	/**
	 * Tag name for logging
	 */
	protected final String TAG = "ServerConnection";
	
	/**
	 * socket inReader
	 */
	private InputStream inReader;
	/**
	 * socket bufferedWriter
	 */
	protected BufferedWriter buffWriter;
	/**
	 * Is socket close?
	 */
	protected boolean bQuit = false;
	/**
	 * Server port
	 */
	protected int iPort;
	/**
	 * Message queue 
	 */
	protected LinkedBlockingQueue<MessageObj> lbqMessage = null;
	/**
	 * A thread for sending message to server
	 */
	protected SendMessageThread sender;
	/**
	 * A thread for handling heart beat message
	 */
	protected HeartBeatThread heartBeat;
	/**
	 * Socket for connect to server
	 */
	protected Socket socket;
	/**
	 * Server URL
	 */
	protected String strURL;
	/**
	 * Message status listener
	 */
	public MessageListener listener;
	/**
	 * Connection status listener
	 */
	public ConnectionStatusListener connectionStatusListener;
	/**
	 * Constructor
	 * @param strURL server URL
	 * @param iPort server Port
	 * @param listener Message status listener
	 * @param lbqMessage message queue
	 * @param connectionStatusListener connection status listener
	 * @throws Exception
	 */
	public ServerConnection(String strURL, int iPort, MessageListener listener, LinkedBlockingQueue<MessageObj> lbqMessage, ConnectionStatusListener connectionStatusListener) throws Exception{
		Log.e("server", "strURL = " + strURL + ";iPort = " + iPort);
		this.strURL = strURL;
		this.iPort = iPort;
		this.listener = listener;
		this.lbqMessage = lbqMessage;
		this.connectionStatusListener = connectionStatusListener;
		init();
	}
	
	/**
	 * Initial connection
	 * @throws UnknownHostException
	 * @throws IOException
	 */
    public void init() throws UnknownHostException, IOException, SocketTimeoutException {
    	if (BuildConfig.DEBUG)
    		Log.i(TAG, "Connecting .....");
    	
    	//lbqMessage = new LinkedBlockingQueue<MessageObj>();
    	
		socket = new Socket(strURL, iPort);

		if (socket != null) {
			socket.setSoTimeout(TIME_OUT);
			buffWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
			inReader = socket.getInputStream();
			if (CompanySettings.AESCrypto)
				getTodayRandomMessageKey();
		}
		
		bQuit = false;
		
		(new MessageRecevierThread()).start();
		
		sender = new SendMessageThread();
		sender.start();
		if (BuildConfig.DEBUG)
			Log.i(TAG, "Connected!");
	}
    /**
     * Kick start heart beat thread
     */
    public void startHeartBeat(){
    	heartBeat = new HeartBeatThread();
    	heartBeat.start();
    }
    /**
     * Send message to server
     * @param msg Message object
     */
    public void sendMessage(MessageObj msg){
    	sender.addToSendingQueue(msg.convertToString(true));
    }
    /**
     * Send message to server 
     * @param strMsg message string
     */
    public void sendMessage(String strMsg){
    	sender.addToSendingQueue(strMsg);
    }
    
    /**
     * Send message to server 
     * @param strMsg message string
     */
    public void sendFlushMessage(String strMsg){
    	sender.flush(strMsg);
    }
    
    /**
     * A thread for handling heart beat 
     *  
     *  
     *  @author        : justin.lai
     *  @version       : v1.00
     *  
     *  
     *  @ModificationHistory  
     *  Who			When			Version			What<br>
     *  -------------------------------------------------------------------------------<br> 
     *  justin.lai	20110413		v1.00			Creation<br>
     *  
     *  
    **
     */
    class HeartBeatThread extends Thread
    {
    	/**
    	 * Constructor
    	 */
        public HeartBeatThread()
        {
        }
        
        @Override
        public void run()
        {
        	if (BuildConfig.DEBUG)
        		Log.i(TAG, "Start heatbeat");
        	MessageObj messageObj = MessageObj.getMessageObj(IDDictionary.SERVER_HEARTBEAT_SERVICE_TYPE, 0);
        	String strHeart = messageObj.convertToString(true);
        	messageObj.release();
            while(!bQuit)
            {   
                try{
                	sender.addToSendingQueue(strHeart);
                    sleep(5000);
                }catch(Exception e){
                	Log.i(TAG, "HeartBeatThread exception occurred", e.fillInStackTrace());
                	closeConnection();
                }
            }
            if (BuildConfig.DEBUG)
            	Log.i(TAG, "Exit heatbeat");
        }
    }

    /**
     * A thread for receving message from server 
     *  
     *  
     *  @author        : justin.lai
     *  @version       : v1.00
     *  
     *  
     *  @ModificationHistory  
     *  Who			When			Version			What<br>
     *  -------------------------------------------------------------------------------<br> 
     *  justin.lai		yyyymmdd		v1.00			Creation<br>
     *  
     *  
    **
     */
    class MessageRecevierThread extends Thread {
		private Set<Pair<Integer, Integer>> hexLengthMessages;
    	/**
    	 * Constructor
    	 */
		public MessageRecevierThread() {
			if (CompanySettings.isHexMessageEncode) {
				hexLengthMessages = new HashSet<>(Arrays.asList(new Pair<>(IDDictionary.TRADER_HISTORY_LIST_TYPE, IDDictionary.TRADER_TRADES_OPEN_DEAL)));
			} else {
				hexLengthMessages = Collections.emptySet();
			}
		}
		
		@Override
		public void run() {
			String strRecvLine = null;
			byte[] byRecvLine = null;
			byte chStartSymbol[] = new byte[1];
			byte chTmpSymbol[] = new byte[1];
			char strHeader[] = new char[9];
			byte[] tmpByte = null;
			byte chRecvLine[];
			
			int iServiceType = -1;
			int iFunctionType = -1;
			int iBodySize = -1;
			
			try
			{
				while (!bQuit) {
					boolean bInvalid = false;
					if (inReader == null)
	                {
	                    break;
	                }

					try {
						if (inReader.read(chStartSymbol, 0, 1) < 0)
	                    {
	                        break;
	                    }

	                    if (chStartSymbol[0] == 19)
	                    {
	                        int nCount = 0;
	                        while (nCount<9 && !bQuit) {
	                            int nByteRead = inReader.read(chTmpSymbol, 0, 1);
	                            if (nByteRead < 0) {
	                                bInvalid = true;
	                                break;
	                            }
	                            else if (nByteRead == 1) {
	                                if (chTmpSymbol[0] != 19) {
	                                    strHeader[nCount] = (char)chTmpSymbol[0];

	                                    nCount++;
	                                    if (nCount == 2)
	                                    {
	                                    	if ( CompanySettings.FORCE_NEW_PRICE_STREAMING_PROTOCOL == true &&
	                                    			(int)strHeader[0] == IDDictionary.TRADER_LIVE_PRICE_TYPE && (int)strHeader[1] == IDDictionary.TRADER_UPDATE_STREAM_PRICE )
	                                    	{
	                                    		tmpByte = new byte[2];
	                                    		nByteRead = inReader.read(tmpByte);
	                                    		if (nByteRead == 2)
	                                    		{
	                                    			iBodySize = ByteBuffer.wrap(tmpByte).getShort() & 0xffff;
	                                        		break;
	                                    		}
	                                    		else
	                                    		{
	                                                bInvalid = true;
	                                                break;
	                                            }
	                                    	}
	                                    	if ((int)strHeader[0] == IDDictionary.TRADER_LIVE_PRICE_TYPE && (int)strHeader[1] == IDDictionary.TRADER_UPDATE_LIVE_PRICE_WITH_DEPTH){
												tmpByte = new byte[2];
												nByteRead = inReader.read(tmpByte);
												if (nByteRead == 2)
												{
													iBodySize = ByteBuffer.wrap(tmpByte).getShort() & 0xffff;
													break;
												}
												else
												{
													bInvalid = true;
													break;
												}
											}
	                                    }
	                                }
	                            }
	                        }
	                    }
	                    else
	                    {
	                        Log.i(TAG, "Msg Corrupted : " + chStartSymbol[0]);
	                        continue;
	                    }
	
	                    iServiceType = (int) strHeader[0];
						iFunctionType = (int) strHeader[1];
						
	                    if (bInvalid) {
							break;
						}
	
						
						if (CompanySettings.FORCE_NEW_PRICE_STREAMING_PROTOCOL == false ||
								(!(iServiceType == IDDictionary.TRADER_LIVE_PRICE_TYPE && iFunctionType == IDDictionary.TRADER_UPDATE_STREAM_PRICE))) {

							if (!(iServiceType == IDDictionary.TRADER_LIVE_PRICE_TYPE && (iFunctionType == IDDictionary.TRADER_UPDATE_LIVE_PRICE_WITH_DEPTH))) {
								if (hexLengthMessages.contains(new Pair<>(iServiceType, iFunctionType))) {
									iBodySize = Integer.parseInt(String.valueOf(strHeader, 2, 7).trim(), 16);
								} else {
									iBodySize = Integer.parseInt(String.valueOf(strHeader, 2, 7).trim());
								}
							}
						}

	                    chRecvLine = new byte[iBodySize];
	                    int iByteRead = inReader.read(chRecvLine, 0, iBodySize);
	                    
	                    if (iByteRead < 0)
	                    {
	                        break;
	                    }
	
	                    if (CompanySettings.FORCE_NEW_PRICE_STREAMING_PROTOCOL == true && 
	                    		iServiceType == IDDictionary.TRADER_LIVE_PRICE_TYPE && iFunctionType == IDDictionary.TRADER_UPDATE_STREAM_PRICE)
	                    {
	                    	if (iByteRead != iBodySize)
		                    {
		                    	byRecvLine = new byte[iByteRead];
		                        System.arraycopy(chRecvLine, 0, byRecvLine, 0 , iByteRead);
		                        int iByteLeft = iBodySize-iByteRead;
		                        while (iByteLeft > 0 && !bQuit)
		                        {
		                            int iTmpByteRead = inReader.read(chRecvLine, 0, iByteLeft);
		                            if (iTmpByteRead < 0) {
		                                bInvalid = true;
		                                break;
		                            }
		                            iByteLeft = iByteLeft-iTmpByteRead;
		                            tmpByte = new byte[iTmpByteRead];
		                            System.arraycopy(chRecvLine, 0, tmpByte, 0 , iTmpByteRead);
		                            byRecvLine = byteConcat(byRecvLine, tmpByte);
		                        }
		                    }
		                    else
		                    {
		                    	byRecvLine = new byte[iByteRead];
		                    	System.arraycopy(chRecvLine, 0, byRecvLine, 0 , iByteRead);
		                    }
	                    }
	                    else if (iServiceType == IDDictionary.TRADER_LIVE_PRICE_TYPE && (iFunctionType == IDDictionary.TRADER_UPDATE_LIVE_PRICE_WITH_DEPTH))
						{
							if (iByteRead != iBodySize)
							{
								byRecvLine = new byte[iByteRead];
								System.arraycopy(chRecvLine, 0, byRecvLine, 0 , iByteRead);
								int iByteLeft = iBodySize-iByteRead;
								while (iByteLeft > 0 && !bQuit)
								{
									int iTmpByteRead = inReader.read(chRecvLine, 0, iByteLeft);
									if (iTmpByteRead < 0) {
										bInvalid = true;
										break;
									}
									iByteLeft = iByteLeft-iTmpByteRead;
									tmpByte = new byte[iTmpByteRead];
									System.arraycopy(chRecvLine, 0, tmpByte, 0 , iTmpByteRead);
									strRecvLine += new String(chRecvLine, 0, iTmpByteRead);
								}
							}
							else
							{
								byRecvLine = new byte[iByteRead];
								System.arraycopy(chRecvLine, 0, byRecvLine, 0 , iByteRead);
							}
						}

	                    else
	                    {
		                    if (iByteRead != iBodySize)
		                    {
		                        strRecvLine = new String(chRecvLine, 0, iByteRead);
		                        int iByteLeft = iBodySize-iByteRead;
		                        while (iByteLeft > 0 && !bQuit)
		                        {
		                            int iTmpByteRead = inReader.read(chRecvLine, 0, iByteLeft);
		                            if (iTmpByteRead < 0) {
		                                bInvalid = true;
		                                break;
		                            }
		                            iByteLeft = iByteLeft-iTmpByteRead;
		                            strRecvLine += new String(chRecvLine, 0, iTmpByteRead);
		                        }
		                    }
		                    else
		                    {
		                        strRecvLine = new String(chRecvLine);
		                    }
	                    }
	                    
	                    MessageObj msgIncoming;
	                    boolean bDecodable = false;

	                    if (CompanySettings.FORCE_NEW_PRICE_STREAMING_PROTOCOL == true &&
	                    		iServiceType == IDDictionary.TRADER_LIVE_PRICE_TYPE && iFunctionType == IDDictionary.TRADER_UPDATE_STREAM_PRICE)
	                    {
		                    msgIncoming = MessageObj.getMessageObj(iServiceType, iFunctionType);
		                    bDecodable = ((PriceMessageObj)msgIncoming).parse(byRecvLine);
	                    }
	                    else if (iServiceType == IDDictionary.TRADER_LIVE_PRICE_TYPE && (iFunctionType == IDDictionary.TRADER_UPDATE_LIVE_PRICE_WITH_DEPTH))
						{
							bDecodable = false;
							msgIncoming = new PriceMessageObj(iServiceType, iFunctionType);
							bDecodable = ((PriceMessageObj)msgIncoming).parse(byRecvLine);
						}
	                    else
	                    {
	                    	msgIncoming = MessageObj.getMessageObj(iServiceType, iFunctionType);
	                    	bDecodable = msgIncoming.parse(strRecvLine, false, true);
	                    }
	                    
	                    if (bDecodable) {
	                    	if (iServiceType == IDDictionary.TRADER_LOGIN_SERVICE_TYPE && iFunctionType == IDDictionary.TRADER_REQUEST_LOGIN_RETURN) {
//								String hexMessageLength = msgIncoming.getField(Protocol.LoginResponse.HEXADECIMAL_MESSAGE_LENGTH);
//								if (hexMessageLength != null) {
//									Set<Pair<Integer, Integer>> temp = new HashSet<>();
//									for (String serviceFunctionCode : hexMessageLength.split(";")) {
//										String[] s = serviceFunctionCode.split(":");
//										if (s.length > 1) {
//											try {
//												temp.add(new Pair<>(Integer.parseInt(s[0]), Integer.parseInt(s[1])));
//											} catch (NumberFormatException ex) {
//											}
//										}
//									}
//									hexLengthMessages = temp;
//								}
							}

							System.out.println("msgIncoming " + msgIncoming.getServiceType() + " " + msgIncoming.getFunctionType());
							lbqMessage.add(msgIncoming);
	                    }
	                    
	                    //listener.notifyMessageReceived();
					} catch (Exception e) {
						if (BuildConfig.DEBUG)
							Log.i(TAG, "MessageRecevierThread lost connection 1", e.fillInStackTrace());
						closeConnection();
					}
	
				}
			}
			catch (Exception e) {
				Log.i(TAG, "MessageRecevierThread throw exception", e.fillInStackTrace());
			}
		}
	}
    
    /**
     * A thread for sending message 
     *  
     *  
     *  @author        : justin.lai
     *  @version       : v1.00
     *  
     *  
     *  @ModificationHistory  
     *  Who			When			Version			What<br>
     *  -------------------------------------------------------------------------------<br> 
     *  justin.lai	20110413		v1.00			Creation<br>
     *  
     *  
    **
     */
	class SendMessageThread extends Thread
    {
		/**
		 * Message queue
		 */
        private Vector<String> msgWaitToSend = new Vector<String>();
        /**
         * Constructor
         */
        public SendMessageThread()
        {
        }
        /**
         * Add message to sending queue
         * @param strMsg message
         */
        public synchronized void addToSendingQueue(String strMsg)
        {
            if (strMsg != null)
            {
                msgWaitToSend.add(strMsg);
                notify();
            }
        }
        
        public void flush(String strMsg)
        {
            if (strMsg != null)
            {
            	try {
	                buffWriter.write(strMsg);
	                buffWriter.flush();	                
            	}
                catch(Exception e) {
                	if (BuildConfig.DEBUG)
                		Log.i(TAG, "SendMessageThread lost connection 2", e.fillInStackTrace());                	
                	closeConnection();
                }
            }
        }        
        /**
         * Destory
         */
        public void destroy()
        {
            bQuit = true;
            if (msgWaitToSend != null)
            {
                msgWaitToSend.clear();
                msgWaitToSend = null;
            }
        }
        
        @Override
        public void run()
        {
            String strMsg = null;
            while (!bQuit)
            {
                try
                {
                    synchronized(this)
                    {
                        while (!bQuit && msgWaitToSend.size() == 0) {
                            try{
                                this.wait(1000);
                            }catch(InterruptedException ie){}
                        }
                        if (!bQuit)
                        {
                            strMsg = (String) msgWaitToSend.remove(0);
                        }
                    }

                    if (!bQuit && buffWriter != null && strMsg!=null)
                    {
                        buffWriter.write(strMsg);
                        buffWriter.flush();
                    }
                    else
                    {
                        if (msgWaitToSend != null)
                            msgWaitToSend.clear();
                    }
                }
                catch(Exception e) {
                	if (BuildConfig.DEBUG)
                		Log.i(TAG, "SendMessageThread lost connection", e.fillInStackTrace());                	
                	closeConnection();
                }
            }
        }
    }
	/**
	 * Close server connection
	 */
	public void closeConnection(){
		if(!bQuit){
			bQuit = true;
			sender.destroy();
			
			try {
				//buffReader.close();
				buffWriter.close();		
				socket.close();
			} catch (IOException e) {e.printStackTrace();}
			socket = null;		
			inReader = null;
			buffWriter = null;
			heartBeat = null;
			lbqMessage = null;
			listener = null;
			
			if(connectionStatusListener != null)
				connectionStatusListener.disconnected();
		}
	}
	
	protected static byte[] byteConcat(byte[] arg0, byte[] arg1)
	{
		byte[] b = new byte[arg0.length + arg1.length];
		System.arraycopy(arg0, 0, b, 0, arg0.length);
		System.arraycopy(arg1, 0, b, arg0.length, arg1.length);
		return b;
	}

	public void getTodayRandomMessageKey()
	{
		byte[] byRecvLine = null;
		byte[] chStartSymbol = new byte[1];

		try
		{
			byRecvLine = new byte[0];
			while (!bQuit)
			{
				int iTmpByteRead = inReader.read(chStartSymbol, 0, 1);
				if (iTmpByteRead < 0)
				{
					break;
				}
				if (chStartSymbol[0] == 19)
				{
					AESLib.randomKey = new byte[byRecvLine.length];
					System.arraycopy(byRecvLine, 0 , AESLib.randomKey, 0, byRecvLine.length);
					break;
				}
				else
				{
					byRecvLine = Utility.byteConcat(byRecvLine, chStartSymbol);
				}
			}
		}
		catch(Exception t)
		{
			Log.e("Exception", t.toString());
		}
	}
/*	
	public void resetConnection(){
		bQuit = true;
		Log.i(TAG, "Waiting end of heatbeat, sender and receiver");
		while(bEndHeadBeat && bEndRecevier && bEndSender){
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
        try {
        	buffReader.close();
        	buffWriter.close();        	
        	socket.close();
        	
        	socket = null;
        	buffReader = null;
        	buffWriter = null;
        		        	
			init();
			login();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
*/		
}
