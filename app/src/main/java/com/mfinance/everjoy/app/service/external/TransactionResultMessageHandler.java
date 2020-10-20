package com.mfinance.everjoy.app.service.external;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.os.Bundle;
import android.util.Log;

import com.mfinance.everjoy.BuildConfig;
import com.mfinance.everjoy.app.bo.SystemMessage;
import com.mfinance.everjoy.app.bo.TransactionObj;
import com.mfinance.everjoy.app.constant.Protocol;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.service.FxMobileTraderService;
import com.mfinance.everjoy.app.util.MessageMapping;
import com.mfinance.everjoy.app.util.MessageObj;
import com.mfinance.everjoy.app.util.Utility;

public class TransactionResultMessageHandler extends ServerMessageHandler {
	ArrayList<SystemMessage> alMessage = new ArrayList<SystemMessage>(); 
	
	public TransactionResultMessageHandler(FxMobileTraderService service) {
		super(service);
	}

	@Override
	public void handleMessage(MessageObj msgObj) {
		//Log.i(TAG, msgObj.convertToString());
		
		if(msgObj == null){
			if (BuildConfig.DEBUG)
				Log.d(TAG, "msgObj is empty");
			return;
		}
		
		try
		{
			//System.out.println("TRM : " + msgObj.convertToString());
			
			String sID = msgObj.getField(Protocol.TransactionMessage.PEND_ID);
			String sReply = msgObj.getField(Protocol.TransactionMessage.REPLY);
			//int iNumOfMessage = Utility.toInteger(msgObj.geField(Protocol.TransactionMessage.NUM_OF_MESSAGE), 0);
			String sMsg = msgObj.getField(Protocol.TransactionMessage.MESSAGE);
			String sLiqmethod = msgObj.getField(Protocol.TransactionMessage.LIQUIDATION_METHOD);
			String sMsgCode = msgObj.getField(Protocol.TransactionMessage.MESSAGE_CODE + 1);
			String sLRef = msgObj.getField(Protocol.TransactionMessage.LIMIT_REF);
			String sSRef = msgObj.getField(Protocol.TransactionMessage.STOP_REF);
			
			//Log.d("QUEUE", "[RESULT ID]["+sID+"]");
			
			TransactionObj t = service.app.data.getTransaction(sID);
			
			if( (sMsgCode == null || sMsgCode.equals("704") == false) && t != null && t.sLiqRef != null && t.sLiqRef.equals("") == false ) 
		    {
				Bundle data = new Bundle();
		    	data.putString("liqref", t.sLiqRef);
		        service.broadcast(ServiceFunction.ACT_TRADER_LIQUIDATE_RETURN, data);
		    }
			
			
			if( t != null )
			{
				t.sMsgCode = sMsgCode;
				if (!"".equals(sLRef)) t.sLRef = sLRef;
				if (!"".equals(sSRef)) t.sSRef = sSRef;
				if (!"".equals(sReply)) t.sReply = sReply;
				if (!"".equals(sLiqmethod)) t.sLiqmethod = sLiqmethod;
			}
			
			
			if(sMsgCode != null){
				int iMsgCode = Utility.toInteger(sMsgCode, -1);
				String sTMsg = MessageMapping.getMessageByCode(service.getRes(), sMsgCode,service.app.locale);
				String sTmp = null;
				
				switch(iMsgCode){
					case 607:
					case 608:
					case 609:
					case 610:
						if( t!= null) {
							sTmp = msgObj.getField(Protocol.TransactionMessage.ORDER_REF);
							if (sTmp==null){
								sTmp = msgObj.getField(Protocol.TransactionMessage.MESSAGE + 1);
							}
						}
						else
							sTmp = msgObj.getField(Protocol.TransactionMessage.MESSAGE + 1);
							
						sTMsg = sTMsg.replace("#s", sTmp);
						
						if( t != null )
						{
							t.sOrderRef = sTmp;
							t.iStatus = 1;
							t.sStatusMsg = MessageMapping.getMessageByCode(service.getRes(), "916",service.app.locale);
							t.sMsg = sTMsg;
							t.sRef = sTmp;	
							t.iStatusMsg = 916;
						}
												
						break;
					case 704:		
						if("3".equals(sLiqmethod)){
							try{ 
								String sLiqRef = msgObj.getField(Protocol.TransactionMessage.LIQUIDATION_REF);
								double dOriginalAmount = Utility.toDouble(msgObj.getField( Protocol.TransactionMessage.AMOUNT ), -1);
			                    double dExecutedAmount = Utility.toDouble(msgObj.getField( Protocol.TransactionMessage.EXAMOUNT ), -1);
			                    sTmp = msgObj.getField(Protocol.TransactionMessage.MESSAGE + 1);
			                    String msg704a = MessageMapping.getMessageByCode(service.getRes(), "704a",service.app.locale);;
			                    if(!msg704a.equals("704a")){
			                    	sTMsg = msg704a;
			                    }

		                    	sTMsg = sTMsg.replace("#s", sTmp);
			                    if(t.isReplyReceived == false)
			                    {
			                    	t.isReplyReceived = true;
			                    	if ((dOriginalAmount >= 0 && dExecutedAmount >= 0) && (dOriginalAmount > dExecutedAmount)) {
				                    	t.sMsg1 = "937";
				                    	sTMsg += "\n" + MessageMapping.getMessageByCode(service.getRes(), t.sMsg1,service.app.locale);
				                    }
									t.sMsg = sTMsg;
									t.sRef = sTmp;
			                    }
			                    else
			                    {
			                    	t.sRef = t.sRef + "," + sTmp;
			                    	int pos = t.sMsg.indexOf(")");
			                    	t.sMsg = t.sMsg.substring(0, pos) + "," + sTmp + t.sMsg.substring(pos); 
			                    }
			                    
								t.iStatus = 1;
								t.sStatusMsg = MessageMapping.getMessageByCode(service.getRes(), "916",service.app.locale);
								t.sLiqRef = sLiqRef;
								t.iStatusMsg = 916;
								if( msgObj.getField( Protocol.TransactionMessage.XML ) != null )
								{
									Document msgxml = getDomElement(msgObj.getField( Protocol.TransactionMessage.XML ));
									if(msgxml != null)
									{
										NodeList list = msgxml.getElementsByTagName(Protocol.TransactionMessage.MsgXml.EXECUTION_PRICE);
										if( list.getLength() > 0 )
											t.dRequestRate = Utility.toDouble(list.item(0).getTextContent(),t.dRequestRate);
									}
								}
								
			                }catch(Exception e){							
								e.printStackTrace();
								//Log.i(TAG, msgObj.convertToString());
								
								t.iStatus = 1;
								t.sStatusMsg = MessageMapping.getMessageByCode(service.getRes(), "916",service.app.locale);
								t.sMsg = sTMsg.replace("#s", "-");;	
								t.sRef = sTmp;
								t.iStatusMsg = 916;
								
							}
						}else{
							sTmp = msgObj.getField(Protocol.TransactionMessage.MESSAGE + 1);
							sTMsg = sTMsg.replace("#s", sTmp);
	
		                    // if null, then this is cut loss message
		                    if( t != null )
		                    {
								t.iStatus = 1;
								t.sStatusMsg = MessageMapping.getMessageByCode(service.getRes(), "916",service.app.locale);
								t.iStatusMsg = 916;
								t.sMsg1 = sTmp;
								if( msgObj.getField( Protocol.TransactionMessage.XML ) != null )
								{
									Document msgxml = getDomElement(msgObj.getField( Protocol.TransactionMessage.XML ));
									if(msgxml != null)
									{
										NodeList list = msgxml.getElementsByTagName(Protocol.TransactionMessage.MsgXml.EXECUTION_PRICE);
										if( list.getLength() > 0 )
											t.dRequestRate = Utility.toDouble(list.item(0).getTextContent(),t.dRequestRate);
									}
								}
		                    }
		                    
							String sOrderMsg = "";
							
							//System.out.println("sLRef:" + sLRef);
							if(sLRef != null && !sLRef.equals("") && !sLRef.equals("-1")){
								sOrderMsg = sOrderMsg +", "+MessageMapping.getMessageByCode(service.getRes(), "607",service.app.locale);
								sOrderMsg = sOrderMsg.replace("#s", sLRef);
							}
							
							//System.out.println("sSRef:" + sSRef);
							if(sSRef != null && !sSRef.equals("") && !sSRef.equals("-1")){
								sOrderMsg = sOrderMsg +", "+MessageMapping.getMessageByCode(service.getRes(), "607",service.app.locale);
								sOrderMsg = sOrderMsg.replace("#s", sSRef);
							}						
							sTMsg = sTMsg + sOrderMsg;
							// if null, then this is cut loss message
		                    if( t != null )
		                    {
		                    	t.sMsg = sTMsg;
		                    	t.sRef = sTmp;
		                    }
						}				
						break;		
					default:					
						t.iStatus = -1;
						t.sStatusMsg = MessageMapping.getMessageByCode(service.getRes(), "918",service.app.locale);
						t.sMsg = sTMsg;
						t.sRef = sTmp;	
						t.iStatusMsg = 918;
						break;
				}
				
				service.app.data.addSystemMessage(new SystemMessage(iMsgCode, sTMsg));				
			}else{
				//Log.i(TAG, msgObj.convertToString());
				
				if("reject".equals(sReply)){
					t.iStatus = -1;
					t.sStatusMsg = MessageMapping.getMessageByCode(service.getRes(), "918",service.app.locale);
					int id;
					if (service.app.locale.equals(Locale.TRADITIONAL_CHINESE)) {
						id = 1;
					} else if (service.app.locale.equals(Locale.SIMPLIFIED_CHINESE)) {
						id = 2;
					} else {
						id = 0;
					}
					String rejectMsg = msgObj.getField("lang_" + id + "_msg");
					t.sMsg = rejectMsg == null ? sMsg : rejectMsg;
					sMsg = rejectMsg == null ? sMsg : rejectMsg;
					t.iStatusMsg = 918;
					t.overrideMsgCode = true;
					t.msgEnglish = msgObj.getField("lang_0_msg");
					t.msgTraditionalChinese = msgObj.getField("lang_1_msg");
					t.msgSimplifiedChinese = msgObj.getField("lang_2_msg");
				}else if("accept".equals(sReply)){
					t.iStatus = 1;
					t.sStatusMsg = MessageMapping.getMessageByCode(service.getRes(), "916",service.app.locale);
					t.sMsg = sMsg;
					t.iStatusMsg = 916;
				}	
				service.app.data.addSystemMessage(new SystemMessage(				
						-1,
						sMsg
				));
	
			}
			if (t != null) {
				service.app.data.updateTransaction(t);
				service.updateTransactionToDB(t);
			}
			//-- Facebook service.postMessageOnWall(t.sMsg);
			service.broadcast(ServiceFunction.ACT_UPDATE_UI, null);
		}catch(Exception e){
			e.printStackTrace();
			//System.out.println(msgObj.convertToString());
		}
	}
	/*
	new deal
	--------
	gc004
	autodeal1
	role2autogenhouse
	msgcd1704
	price1421.0
	hier004001comment
	srv5examount10.00
	hasDealer1clientcom2u3mydealer
	type1userType2
	fridSYS
	amount10b/sBliqmethod-1
	contractLLGcode300
	msgDeal(1752) accepted
	cs100chk1frnamecom2u3msg11752toidcom2u3
	alk1
	pidcom2u31301473047191
	msgno1ip202.155.229.100accom2u3
	*/
	/*
	Liq
	-----
	hasDealer1clientcom2u3mydealergc004srv5
	type1msg11753userType2fridSYS
	amount10.0autodeal1role2b/sS
	liqmethod3toidcom2u3alk1
	liqref1746|10.0contract
	LLGexamount10.00pidcom2u31301475150080
	autogenhousemsgno1code300
	msgDeal(1753) acceptedmsgcd1704ip202.155.229.100
	cs100price1420.2chk1
	frnamecom2u3accom2u3
	hier004001comment
	*/	
	/*
	new order
	---------
	gc004
	role2clientcom2u3mydealer
	userType2fridSYSliqmethod-1code300cs100
	frnamecom2u3alk1accom2u3refrate1421.4
	commentsrv4type1trail-1contractLLG
	msg185toidcom2u3pidcom2u31301474603666
	ip202.155.229.100gttimeautodeal1goodtil0
	autogenhouseprice1419.4hier004001ocoref-1
	otype0hasDealer1amount10gtdate
	msgOrder (85) Added.liqrate-1msgno1orderno85
	msgcd1607b/sBchk2ordernew
	 */
/*	
	gc004role2clientcom2u3mydealeruserType2
	fridSYSliqmethod-1code300cs100frname
	com2u3alk1accom2u3refrate1419.8comment
	srv4type1trail-1
	contractLLGmsg186toidcom2u3pidcom2u31301477252650
	ip202.155.229.100gttimeautodeal1goodtil0
	autogenhouseprice1417.2hier004001ocoref-1otype0hasDealer1amount20
	gtdatemsgOrder (86) Amended.liqrate-1msgno1
	orderno86
	msgcd1608b/sBchk2orderamend
/*
 reject
 ------
code403
srv5toidcom3u6
pidcom3u61301539361957
msgCan't add deal! Cause: Not Available to trade this contract
msgcd1934
 */

	@Override
	public boolean isStatusLess() {
		return false;
	}

	@Override
	public boolean isBalanceRecalRequire() {
		return false;
	}

	public Document getDomElement(String xml){
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
 
            DocumentBuilder db = dbf.newDocumentBuilder();
 
            InputSource is = new InputSource();
                is.setCharacterStream(new StringReader(xml));
                doc = db.parse(is); 
 
            } catch (ParserConfigurationException e) {
                return null;
            } catch (SAXException e) {
                return null;
            } catch (IOException e) {
                return null;
            }

        	// return DOM
            return doc;
    }
}


