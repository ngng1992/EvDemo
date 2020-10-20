package com.mfinance.everjoy.app.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.LinkedBlockingQueue;

import android.util.Log;

import com.mfinance.everjoy.BuildConfig;
import com.mfinance.everjoy.app.util.MessageObj;

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
public class OTXServerConnection extends ServerConnection{
	
	/**
	 * socket bufferedReader
	 */
	protected BufferedReader buffReader;
	
	/**
	 * Constructor
	 * @param strURL server URL
	 * @param iPort server Port
	 * @param listener Message status listener
	 * @param lbqMessage message queue
	 * @param connectionStatusListener connection status listener
	 * @throws Exception
	 */
	public OTXServerConnection(String strURL, int iPort, MessageListener listener, LinkedBlockingQueue<MessageObj> lbqMessage, ConnectionStatusListener connectionStatusListener) throws Exception{
		super(strURL,iPort,listener,lbqMessage,connectionStatusListener);
	}
	
	/**
	 * Initial connection
	 * @throws UnknownHostException
	 * @throws IOException
	 */
    @Override
	public void init() throws UnknownHostException, IOException, SocketTimeoutException {
    	if (BuildConfig.DEBUG)
    		Log.i(TAG, "Connecting .....");
    	
    	//lbqMessage = new LinkedBlockingQueue<MessageObj>();
    	
		socket = new Socket(strURL, iPort);

		if (socket != null) {
			socket.setSoTimeout(TIME_OUT);
			buffWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
			buffReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
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
    @Override
	public void startHeartBeat(){
    	heartBeat = new HeartBeatThread();
    	heartBeat.start();
    }
    /**
     * Send message to server
     * @param msg Message object
     */
    @Override
	public void sendMessage(MessageObj msg){
    	sender.addToSendingQueue(msg.convertToString(true));
    }
    /**
     * Send message to server 
     * @param strMsg message string
     */
    @Override
	public void sendMessage(String strMsg){
    	sender.addToSendingQueue(strMsg);
    }
    
    /**
     * Send message to server 
     * @param strMsg message string
     */
    @Override
	public void sendFlushMessage(String strMsg){
    	sender.flush(strMsg);
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
    	/**
    	 * Constructor
    	 */
		public MessageRecevierThread() {
		}
		
		@Override
		public void run() {
			String strRecvLine;
			
			char chTmpSymbol[] = new char[1];
			char strHeader[] = new char[10];
			char chRecvLine[];
			char chStartSymbol[] = new char[1];
			int iServiceType = -1;
			int iFunctionType = -1;			
			int iBodySize = -1;
			
			while (!bQuit) {	
				boolean bInvalid = false;
				boolean encryptedMessage = true;

				if (buffReader == null)
				{
					break;
				}

				try {
					if (buffReader.read(chStartSymbol, 0, 1) < 0)
					{
						break;
					}
					
					//System.out.println("start==>"+(int)chStartSymbol[0]);

					if (chStartSymbol[0] == MessageObj.ENCRYPTED_MESSAGE || chStartSymbol[0] == MessageObj.PLAIN_MESSAGE)
	                    {
	                    	if(chStartSymbol[0] == MessageObj.PLAIN_MESSAGE)
		                	{
		                		encryptedMessage = false;
		                	}
	                    	
	                        int nCount = 0;
	                        while (nCount<10 && !bQuit) {
	                            int nByteRead = buffReader.read(chTmpSymbol, 0, 1);
	                            if (nByteRead < 0) {
	                                bInvalid = true;
	                                break;
	                            }
	                            else if (nByteRead == 1) {
	                                if (chTmpSymbol[0] != 19) {
	                                    strHeader[nCount] = chTmpSymbol[0];
	                                    nCount++;
	                                }
	                            }
	                        }
	                    }
	                    else
	                    {
	                        //System.out.println("Msg Corrupted : " + chStartSymbol[0]);
	                        continue;
	                    }

	                    if (bInvalid)
	                    {
	                        break;
	                    }

	                    iServiceType = strHeader[0];
	                    iFunctionType = strHeader[1];
	                    int keyCode = strHeader[2];
	                    
	                    try {
	                        iBodySize = Integer.parseInt(String.valueOf(strHeader, 3, 7).trim());
	                    }
	                    catch(NumberFormatException e) {
	                       //System.out.println("NumberFormatException -> strHeader : " + String.valueOf(strHeader));
	                        continue;
	                    }

	                    chRecvLine = new char[iBodySize];
	                    int iByteRead = buffReader.read(chRecvLine, 0, iBodySize);
	                    if (iByteRead < 0)
	                    {
	                        break;
	                    }

	                    if (iByteRead != iBodySize)
	                    {
	                        strRecvLine = new String(chRecvLine, 0, iByteRead);
	                        int iByteLeft = iBodySize-iByteRead;
	                        while (iByteLeft > 0 && !bQuit)
	                        {
	                            int iTmpByteRead = buffReader.read(chRecvLine, 0, iByteLeft);
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

	                    if (bInvalid)
	                    {
	                        break;
	                    }

	                    MessageObj msgIncoming = MessageObj.getMessageObj(iServiceType, iFunctionType);
	                    //TODO Set Key
	                    //msgIncoming.setKey(keyCode, clientMessageKey);

	                    boolean bDecodable = false;

	                    if (encryptedMessage && strRecvLine.length() > 0)
	                        bDecodable = msgIncoming.parse(strRecvLine, false, true);
	                    else
	                        bDecodable = msgIncoming.parse(strRecvLine);

	                    if (bDecodable)
	                    {
	                    	lbqMessage.add(msgIncoming);
	                    }
				} catch (Exception e) {
					if (BuildConfig.DEBUG)
						Log.i(TAG, "MessageRecevierThread lost connection 2", e.fillInStackTrace());
					closeConnection();
				}

			}
		}
	}
    
	/**
	 * Close server connection
	 */
	@Override
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
			buffReader = null;
			buffWriter = null;
			heartBeat = null;
			lbqMessage = null;
			listener = null;
			
			if(connectionStatusListener != null)
				connectionStatusListener.disconnected();
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

