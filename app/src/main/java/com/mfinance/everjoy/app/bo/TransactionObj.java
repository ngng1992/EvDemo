package com.mfinance.everjoy.app.bo;

import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;

import com.mfinance.everjoy.app.MobileTraderApplication;
import com.mfinance.everjoy.app.util.MessageMapping;
import com.mfinance.everjoy.app.util.Utility;

import java.util.Date;
import java.util.Locale;

public class TransactionObj implements Parcelable {
	public final String sTransactionID;
	public String sRef;
	public final String sContract;
	public final String sAccount;
	public final String sBuySell;
	public String sMsg;
	public double dRequestRate;
	public int iStatus;
	public String sStatusMsg;
	public int iMsg;
	public int iRemarkCode;
	public int iType;
	public Date dateCreate = null;
	public Date dateLastUpdate = null;
	public double dAmount;
	public ContractObj contract;
	
	public int iStatusMsg;
	public String sMsgCode;
	public String sOrderRef;
	public String sLiqRef;
	public String sLRef;
	public String sSRef;
	public String sReply;
	public String sLiqmethod;
	public String sMsg1;
	
	public String sKey;

	public boolean isReplyReceived;
	public boolean overrideMsgCode;
	public String msgEnglish;
	public String msgTraditionalChinese;
	public String msgSimplifiedChinese;
	
	public TransactionObj(String sTransactionID, String sRef, String sContract, String sAccount,
			String sBuySell, double dRequestRate, int iStatus,
			String sStatusMsg, int iMsg, int iRemarkCode, int iType, double dAmount, ContractObj contract,
			int iStatusMsg, String sMsgCode, String sOrderRef, String sLiqRef, String sLRef, String sSRef, String sReply, 
			String sLiqmethod, String sMsg1, boolean overrideMsgCode, String msgEnglish, String msgTraditionalChinese, String msgSimplifiedChinese,
		    String sKey, boolean isReplyReceived, Date dateCreate, Date dateLastUpdate, String sMsg) {
		this.sTransactionID = sTransactionID;
		this.sRef = sRef;
		this.sContract = sContract;
		this.sAccount = sAccount;
		this.sBuySell = sBuySell;
		this.dRequestRate = dRequestRate;
		this.iStatus = iStatus;
		this.sStatusMsg = sStatusMsg;
		this.iMsg = iMsg;
		this.iRemarkCode = iRemarkCode;
		this.iType = iType;
		this.dAmount = dAmount;
		this.contract = contract;
		this.iStatusMsg = iStatusMsg;
		this.sMsgCode = sMsgCode;
		this.sOrderRef = sOrderRef;
		this.sLiqRef = sLiqRef;
		this.sLRef = sLRef; 
		this.sSRef  =sSRef;
		this.sReply = sReply;
		this.sLiqmethod = sLiqmethod;
		this.sMsg1 = sMsg1;
		this.overrideMsgCode = overrideMsgCode;
		this.msgEnglish = msgEnglish;
		this.msgTraditionalChinese = msgTraditionalChinese;
		this.msgSimplifiedChinese = msgSimplifiedChinese;
		this.sKey = sKey;
		this.isReplyReceived = isReplyReceived;
		this.dateCreate = dateCreate;
		this.dateLastUpdate = dateLastUpdate;
		this.sMsg = sMsg;
	}

	protected TransactionObj(Parcel in) {
		sTransactionID = in.readString();
		sRef = in.readString();
		sContract = in.readString();
		sAccount = in.readString();
		sBuySell = in.readString();
		sMsg = in.readString();
		dRequestRate = in.readDouble();
		iStatus = in.readInt();
		sStatusMsg = in.readString();
		iMsg = in.readInt();
		iRemarkCode = in.readInt();
		iType = in.readInt();
		dAmount = in.readDouble();
		iStatusMsg = in.readInt();
		sMsgCode = in.readString();
		sOrderRef = in.readString();
		sLiqRef = in.readString();
		sLRef = in.readString();
		sSRef = in.readString();
		sReply = in.readString();
		sLiqmethod = in.readString();
		sMsg1 = in.readString();
		sKey = in.readString();
		isReplyReceived = in.readByte() != 0;
		overrideMsgCode = in.readByte() != 0;
		msgEnglish = in.readString();
		msgTraditionalChinese = in.readString();
		msgSimplifiedChinese = in.readString();
	}


	public static final Creator<TransactionObj> CREATOR = new Creator<TransactionObj>() {
		@Override
		public TransactionObj createFromParcel(Parcel in) {
			return new TransactionObj(in);
		}

		@Override
		public TransactionObj[] newArray(int size) {
			return new TransactionObj[size];
		}
	};

	public void destory() {
		// 
		
	}
	
	public static String getMessage(TransactionObj trans, Resources res, MobileTraderApplication app){
		//System.out.println("sMsgCode:" + sMsgCode);
		if (trans.overrideMsgCode) {
			if (app.locale.equals(Locale.TRADITIONAL_CHINESE)) {
				return trans.msgTraditionalChinese;
			} else if (app.locale.equals(Locale.SIMPLIFIED_CHINESE)) {
				return trans.msgSimplifiedChinese;
			} else {
				return trans.msgEnglish;
			}
		}
		if(trans.sMsgCode != null){
			int iMsgCode = Utility.toInteger(trans.sMsgCode, -1);
			String sTMsg = MessageMapping.getMessageByCode(res, trans.sMsgCode,app.locale);
			String sTmp = null;
			
			switch(iMsgCode){
				case 607:
				case 608:
				case 609:
				case 610:
					return sTMsg.replace("#s", trans.sOrderRef);
				case 620:
				case 621:
				case 622:
					return sTMsg.replace("#s", trans.sMsg1!=null?trans.sMsg1:"");
			case 704:					
					if("3".equals(trans.sLiqmethod)){
						try{ 
							String msg704a = MessageMapping.getMessageByCode(res, "704a",app.locale);;
		                    if(!msg704a.equals("704a")){
		                    	sTMsg = msg704a;
		                    }
							sTMsg = sTMsg.replace("#s", trans.sRef);
							return sTMsg; 
						}catch(Exception e){							
							e.printStackTrace();
							return sTMsg.replace("#s", "-");	
						}
					}else{
						//sTmp = msgObj.getField(Protocol.TransactionMessage.MESSAGE + 1);
						sTMsg = sTMsg.replace("#s", trans.sMsg1);
						
						String sOrderMsg = "";
						
						if(trans.sLRef != null && !trans.sLRef.equals("") && !trans.sLRef.equals("-1")){
							sOrderMsg = sOrderMsg +", "+MessageMapping.getMessageByCode(res, "607",app.locale);
							sOrderMsg = sOrderMsg.replace("#s", trans.sLRef);
						}
						
						if(trans.sSRef != null && !trans.sSRef.equals("") && !trans.sSRef.equals("-1")){
							sOrderMsg = sOrderMsg +", "+MessageMapping.getMessageByCode(res, "607",app.locale);
							sOrderMsg = sOrderMsg.replace("#s", trans.sSRef);
						}		
						
						return sTMsg + sOrderMsg;
										
					}				
			default:					
					return sTMsg;
			}
		}else{
			if("reject".equals(trans.sReply)){
				return trans.sMsg;

			}else if("accept".equals(trans.sReply)){
				return trans.sMsg;
			}	
		}
		return trans.sMsg;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(sTransactionID);
		dest.writeString(sRef);
		dest.writeString(sContract);
		dest.writeString(sAccount);
		dest.writeString(sBuySell);
		dest.writeString(sMsg);
		dest.writeDouble(dRequestRate);
		dest.writeInt(iStatus);
		dest.writeString(sStatusMsg);
		dest.writeInt(iMsg);
		dest.writeInt(iRemarkCode);
		dest.writeInt(iType);
		dest.writeDouble(dAmount);
		dest.writeInt(iStatusMsg);
		dest.writeString(sMsgCode);
		dest.writeString(sOrderRef);
		dest.writeString(sLiqRef);
		dest.writeString(sLRef);
		dest.writeString(sSRef);
		dest.writeString(sReply);
		dest.writeString(sLiqmethod);
		dest.writeString(sMsg1);
		dest.writeString(sKey);
		dest.writeByte((byte) (isReplyReceived ? 1 : 0));
		dest.writeByte((byte) (overrideMsgCode ? 1 : 0));
		dest.writeString(msgEnglish);
		dest.writeString(msgTraditionalChinese);
		dest.writeString(msgSimplifiedChinese);
	}
}
