package com.mfinance.everjoy.app.service.external;

import com.mfinance.everjoy.app.bo.SystemMessage;
import com.mfinance.everjoy.app.constant.Protocol;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.service.FxMobileTraderService;
import com.mfinance.everjoy.app.util.MessageMapping;
import com.mfinance.everjoy.app.util.MessageObj;
import com.mfinance.everjoy.app.util.Utility;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SystemMessageHandler extends ServerMessageHandler {

	private final List<String> changePasswordFailMsgCode = Collections.unmodifiableList(Arrays.asList("401", "402", "409", "410", "411"));
	private final List<String> changePasswordSuccessfulMsgCode = Collections.unmodifiableList(Arrays.asList("400"));
	private final List<String> cashMovementFailMsgCode = Collections.unmodifiableList(Arrays.asList("983", "984", "303", "987", "986", "987", "1003", "1004", "1005", "1006", "1012", "933", "985", "1030", "1031"));
	private final List<String> cashMovementSuccessfulMsgCode = Collections.unmodifiableList(Arrays.asList("981", "982", "1007", "1008"));
	private final List<String> cashMovementCancelMsgCode = Collections.unmodifiableList(Arrays.asList("1025", "1026", "1027", "1028"));
	public SystemMessageHandler(FxMobileTraderService service) {
		super(service);
	}

	@Override
	public void handleMessage(MessageObj msgObj) {
		//Log.e(TAG, "SMH: "  + msgObj.convertToString());
		
		final String sCode = msgObj.getField(Protocol.SystemMessage.MSG);
		int iCode = Utility.toInteger(sCode, -1);
		String strMsgCode;
		if (iCode > 0) {
			strMsgCode = sCode;
		} else {
			strMsgCode = msgObj.getField(Protocol.SystemMessage.MSGCD + "1");
		}
		
		String sMsg = "";
		if (iCode > 0) {
			sMsg = MessageMapping.getMessageByCode(service.getRes(), sCode,service.app.locale);
		} else {
			StringBuffer strTmpBuffer = new StringBuffer();
			if (strMsgCode != null && strMsgCode.length() > 0) {
				if (strMsgCode.equals("111")) {
					// ingore
				} else if (strMsgCode.equals("986") || strMsgCode.equals("987")) {
					String strMsgLine =  MessageMapping.getMessageByCode(service.getRes(), strMsgCode,service.app.locale);
					String[] split = sCode.split(Pattern.quote("\t"));
					if (split.length >= 2) {
						strMsgLine = strMsgLine.replace(Matcher.quoteReplacement("#s1"), split[0])
								.replace(Matcher.quoteReplacement("#s2"), split[1]);
					}
					strTmpBuffer.append(strMsgLine);
				} else if (strMsgCode.equals("1031")) {
					String strMsgLine =  MessageMapping.getMessageByCode(service.getRes(), strMsgCode,service.app.locale);
					strTmpBuffer.append(strMsgLine.replace(Matcher.quoteReplacement("#s1"), sCode));
				} else {
					int iMsgNum = Utility.toInteger(msgObj.getField(Protocol.SystemMessage.MSGNO), 1);
					
					
					
					for (int i = 1; i <= iMsgNum; i++)
                	{
                		strMsgCode = msgObj.getField(Protocol.SystemMessage.MSGCD + i);

                		String strMsgLine =  MessageMapping.getMessageByCode(service.getRes(), strMsgCode,service.app.locale);
                		String strExtra = sCode;

                        String field = msgObj.getField(Protocol.SystemMessage.MSG + i);
                        if (strExtra != null && strExtra.length() > 0 && field != null)
                			strMsgLine = strMsgLine.replaceFirst("(#s)", field);
                		
                		
                		if (strTmpBuffer.length() > 0)
                			strTmpBuffer.append("\n");

                		strTmpBuffer.append(strMsgLine);
                	}
					
					String sLRef = msgObj.getField(Protocol.SystemMessage.LIMIT_REF);
					String sSRef = msgObj.getField(Protocol.SystemMessage.STOP_REF);
					String sProfitOrderRef = msgObj.getField(Protocol.SystemMessage.PROFIT_ORDER_REF);
					String sCutOrderRef = msgObj.getField(Protocol.SystemMessage.CUT_ORDER_REF);
					
					if(sLRef != null && !sLRef.equals("") && !sLRef.equals("-1")){	
						String sTmp = MessageMapping.getMessageByCode(service.getRes(), "607",service.app.locale);
						sTmp = sTmp.replace("#s", sLRef);						
						strTmpBuffer.append(", ").append(sTmp);						
					}
					
					if(sSRef != null && !sSRef.equals("") && !sSRef.equals("-1")){
						String sTmp = MessageMapping.getMessageByCode(service.getRes(), "607",service.app.locale);
						sTmp = sTmp.replace("#s", sSRef);						
						strTmpBuffer.append(", ").append(sTmp);		
					}		
					
					if(sProfitOrderRef != null && !sProfitOrderRef.equals("") && !sProfitOrderRef.equals("-1")){	
						String sTmp = MessageMapping.getMessageByCode(service.getRes(), "607",service.app.locale);
						sTmp = sTmp.replace("#s", sProfitOrderRef);						
						strTmpBuffer.append(", ").append(sTmp);						
					}
					
					if(sCutOrderRef != null && !sCutOrderRef.equals("") && !sCutOrderRef.equals("-1")){	
						String sTmp = MessageMapping.getMessageByCode(service.getRes(), "607",service.app.locale);
						sTmp = sTmp.replace("#s", sCutOrderRef);						
						strTmpBuffer.append(", ").append(sTmp);						
					}
				}
			}
			
			sMsg = strTmpBuffer.toString();
		}

		service.app.data.addSystemMessage(new SystemMessage(
				iCode,
				sMsg
		));
		
		
		/*
		if (sMsg.indexOf(ServiceFunction.FACEBOOK_MATCHER) > 0){
			Bundle data = new Bundle();
			data.putString(ServiceFunction.FACEBOOK_MESSAGE, sMsg);
			service.broadcast(ServiceFunction.ACT_POST_FB, data);
		}
		*/
		//-- Facebook service.postMessageOnWall(sMsg);
		service.broadcast(ServiceFunction.ACT_UPDATE_UI, null);
	}


	@Override
	public boolean isStatusLess() {
		return false;
	}

	@Override
	public boolean isBalanceRecalRequire() {
		return false;
	}

}
