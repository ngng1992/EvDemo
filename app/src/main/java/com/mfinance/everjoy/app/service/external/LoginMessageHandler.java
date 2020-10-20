package com.mfinance.everjoy.app.service.external;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.mfinance.everjoy.BuildConfig;
import com.mfinance.everjoy.app.CompanySettings;
import com.mfinance.everjoy.app.MobileTraderApplication;
import com.mfinance.everjoy.app.bo.SystemMessage;
import com.mfinance.everjoy.app.constant.IDDictionary;
import com.mfinance.everjoy.app.constant.Protocol;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.pojo.PasswordControlBuilder;
import com.mfinance.everjoy.app.service.FxMobileTraderService;
import com.mfinance.everjoy.app.util.MessageMapping;
import com.mfinance.everjoy.app.util.MessageObj;
import com.mfinance.everjoy.app.util.Utility;

public class LoginMessageHandler extends ServerMessageHandler {

	public LoginMessageHandler(FxMobileTraderService service) {
		super(service);
	}

	@Override
	public void handleMessage(MessageObj msgObj) {
		//Log.i(TAG, msgObj.convertToString());
		Log.e("login", "login:" + msgObj.convertToString());

		try {
			if(msgObj.getField(Protocol.LoginResponse.IDENTITY_CHECK) != null && msgObj.getField(Protocol.LoginResponse.IDENTITY_CHECK).equals("identity"))
			{
				service.app.isLoading = false;
				service.broadcast(ServiceFunction.ACT_INVISIBLE_POP_UP, null);
				Message msg;
				msg = Message.obtain(null, 
						ServiceFunction.SRV_MOVE_TO_IDENTITY_CHECK);
				service.handler.handleMessage(msg);
				return;
			}

			String field1 = msgObj.getField(Protocol.LoginResponse.STATUS);
			// success表示成功
			Log.e("login", "登录成功 ============================ field1 = " + field1);

			if("success".equals(msgObj.getField(Protocol.LoginResponse.STATUS))){


				//Password control
				String passctrl_msg = msgObj.getField(Protocol.LoginResponse.PWD_CRL_MSG);
				PasswordControlBuilder passwordControlBuilder = new PasswordControlBuilder();

				if (passctrl_msg != null) {
					passwordControlBuilder.setSymbolList(msgObj.getField(Protocol.LoginResponse.PWD_SYMBOL_LIST));
					passwordControlBuilder.setFirstLogin("1".equals(msgObj.getField(Protocol.LoginResponse.FIRST_LOGIN)));
					try {
						passwordControlBuilder.setAge(Integer.parseInt(msgObj.getField(Protocol.LoginResponse.PWD_CRL_AGE)));
						for (String field : passctrl_msg.split(",")) {
							String[] t = field.split(":", 2);
							String fieldName = t[0];
							String fieldValue = t[1];
							if ("aro".equals(fieldName)) {
								passwordControlBuilder.setAllowReuse("1".equals(fieldValue));
							} else if ("mnl".equals(fieldName)) {
								passwordControlBuilder.setMinLength(Integer.parseInt(fieldValue));
							} else if ("mxl".equals(fieldName)) {
								passwordControlBuilder.setMaxLength(Integer.parseInt(fieldValue));
							} else if ("nol".equals(fieldName)) {
								passwordControlBuilder.setMinLowercaseLetter(Integer.parseInt(fieldValue));
							} else if ("nou".equals(fieldName)) {
								passwordControlBuilder.setMinUppercaseLetter(Integer.parseInt(fieldValue));
							} else if ("nod".equals(fieldName)) {
								passwordControlBuilder.setMinDigitLetter(Integer.parseInt(fieldValue));
							} else if ("nos".equals(fieldName)) {
								passwordControlBuilder.setMinSymbolLetter(Integer.parseInt(fieldValue));
							} else if ("mad".equals(fieldName)) {
								passwordControlBuilder.setMaxPasswordAge(Integer.parseInt(fieldValue));
							}
						}
					} catch (NumberFormatException ex) {

					} catch (IndexOutOfBoundsException ex) {

					}
				}
				service.app.data.setPasswordControl(passwordControlBuilder.createPasswordControl());

				if (msgObj.getField(Protocol.LoginResponse.SESS_TIMEOUT_ALERT) != null){
					service.app.data.sessTimeoutAlert = Integer.parseInt(msgObj.getField(Protocol.LoginResponse.SESS_TIMEOUT_ALERT));
				}
				if (msgObj.getField(Protocol.LoginResponse.SESS_TIMEOUT_DISCONN) != null){
					service.app.data.sessTimeoutDisconn = Integer.parseInt(msgObj.getField(Protocol.LoginResponse.SESS_TIMEOUT_DISCONN));
				}

				Bundle data = new Bundle();    	    				
				data.putInt(ServiceFunction.P_RESULT, ServiceFunction.V_SUCCESS);			
				service.app.dtTradeDate = Utility.toDate(msgObj.getField(Protocol.LoginResponse.TRADE_DATE));
				if (msgObj.getField(Protocol.LoginResponse.DISPLAY_CUR) != null)
				{
					MobileTraderApplication.DEFAULT_CURR = msgObj.getField(Protocol.LoginResponse.DISPLAY_CUR);
				}
				if (msgObj.getField(Protocol.LoginResponse.CONV_RATE) != null) {
					MobileTraderApplication.CONV_RATE = Double.valueOf(msgObj.getField(Protocol.LoginResponse.CONV_RATE));
					if (BuildConfig.DEBUG)
						Log.d("CONV_RATE", ""+ MobileTraderApplication.CONV_RATE);
				}
				if (msgObj.getField(Protocol.LoginResponse.CONV_RATE_OP) != null) {
					MobileTraderApplication.CONV_RATE_OP = msgObj.getField(Protocol.LoginResponse.CONV_RATE_OP);
				}
				if(msgObj.getField(Protocol.LoginResponse.WEB)!=null){
					MobileTraderApplication.WEB = msgObj.getField(Protocol.LoginResponse.WEB);
					if (BuildConfig.DEBUG)
						Log.d("WEB", MobileTraderApplication.WEB);
				}
				if(msgObj.getField(Protocol.LoginResponse.CHARTLINK)!=null&&!CompanySettings.FORCE_HARD_CODE_CHART_URL){
					String chartLink = msgObj.getField(Protocol.LoginResponse.CHARTLINK);
					if(chartLink.split(":").length==1)
						service.app.chartDomain = chartLink;
					else if(chartLink.split(":").length>1){
						String [] splitChartLink = chartLink.split(":");
						String [] splitChartPort = splitChartLink[1].split("\\|");
						service.app.chartDomain = splitChartLink[0];
						service.app.CHART_PORT = Integer.valueOf(splitChartPort[0]);
						service.app.REALTIME_CHART_PORT = Integer.valueOf(splitChartPort[1]);
					}
					if (BuildConfig.DEBUG)
						Log.d("chartDomain", chartLink);
				}
				if(msgObj.getField(Protocol.LoginResponse.STPORDER) != null)
				{
					if( msgObj.getField(Protocol.LoginResponse.STPORDER).equals("1") )
						CompanySettings.ALLOW_STP_ORDER = true;
					else
						CompanySettings.ALLOW_STP_ORDER = false;					
				}
				
				service.connection.startHeartBeat();
				service.app.bLogon = true;
				service.onLogin();
				if (CompanySettings.SHOW_DASHBOARD_BEFORE_LOGIN)
					service.app.bPriceReload = true;
				service.deleteExpireTransactionInDB(service.app.dtTradeDate);
				// System.out.println(msgObj.convertToString());
				service.app.reloadDefaultPageInMemory();
				service.updateProcessingMsgToTransactionFail();
				service.app.data.setTradableContract(msgObj.getField("tc"));
				
				service.app.data.clearSystemMessage();
				
				/*if( CompanySettings.FORCE_NEW_PRICE_STREAMING_PROTOCOL == true )
					service.app.data.setNewPriceMessage(true);
				else if (msgObj.getField(Protocol.LoginResponse.NEW_PRICE_MESSAGE) != null)
				{
					Log.e("Disconnect", "npm" + msgObj.getField(Protocol.LoginResponse.NEW_PRICE_MESSAGE) );
					if( msgObj.getField(Protocol.LoginResponse.NEW_PRICE_MESSAGE).equals("1") )
						service.app.data.setNewPriceMessage(true);
					else
						service.app.data.setNewPriceMessage(false);						
				}
				else
					service.app.data.setNewPriceMessage(false);*/

				if (CompanySettings.ENABLE_TRADER_PRICE_AGENT_CONNECTION) {
					MessageObj messageObj = MessageObj.getMessageObj(IDDictionary.TRADER_LIVE_PRICE_TYPE, IDDictionary.TRADER_UPDATE_LIVE_PRICE_WITH_PRICE_AGENT_CONNECTION);
					service.postMessage(messageObj);
				}
				else{
					service.app.setPriceAgentConnectionStatus(true);
				}

//				if (CompanySettings.ENABLE_PRICE_ALERT){
//					MessageObj messageObj = MessageObj.getMessageObj(IDDictionary.TRADER_PRICE_ALERT_TYPE, IDDictionary.TRADER_PUSH_PRICE_ALERTS);
//					service.postMessage(messageObj);
//				}
			}
			else if("otp".equals(msgObj.getField(Protocol.LoginResponse.STATUS))) {

				String sCode = msgObj.getField(Protocol.LoginResponse.MESSAGE_CODE);
				if( sCode != null && sCode.equals("316") == true )
				{
					String sMsg = MessageMapping.getMessageByCode(service.getRes(), sCode , service.app.locale);
					service.app.data.addSystemMessage(new SystemMessage(
							Utility.toInteger(sCode, -1),
							sMsg
					));
					service.app.isLoading = false;
					service.app.data.twoFARefreshTimer = false;

					service.broadcast(ServiceFunction.ACT_INVISIBLE_POP_UP, null);
					service.broadcast(ServiceFunction.ACT_UPDATE_UI, null);
				}
				else {
					service.app.data.setTwoFAMessage(msgObj);
					service.app.isLoading = false;

					service.broadcast(ServiceFunction.ACT_INVISIBLE_POP_UP, null);
					Message msg;
					msg = Message.obtain(null,
							ServiceFunction.SRV_TWO_FA);
					service.handler.handleMessage(msg);
					return;
				}
			}
			else{
				if("Invalid Application".equals(msgObj.getField(Protocol.LoginResponse.FAIL))){
					if (!service.app.isDemoPlatform) 
						service.app.data.addSystemMessage(new SystemMessage(				
								-1,
								 MessageMapping.getMessageByCode(service.getRes(), "4004" , service.app.locale)
						));
					else 
						service.app.data.addSystemMessage(new SystemMessage(				
								-1,
								 MessageMapping.getMessageByCode(service.getRes(), "4005" , service.app.locale)
						));
				}
				else {
					String sCode = msgObj.getField(Protocol.LoginResponse.MESSAGE_CODE);
					String sMsg = MessageMapping.getMessageByCode(service.getRes(), sCode , service.app.locale);
					if( sCode.equals("315") == true )
					{
						sMsg = sMsg.replaceAll("@@1", msgObj.getField("rt"));
					}
					service.app.data.addSystemMessage(new SystemMessage(				
							Utility.toInteger(sCode, -1),
							sMsg
					));
				}
				service.app.isLoading = false;
				
				service.broadcast(ServiceFunction.ACT_INVISIBLE_POP_UP, null);
				service.broadcast(ServiceFunction.ACT_UPDATE_UI, null);
				
			}
			service.app.isLoading = false;
			
		} catch (Exception e) {
			Log.e(TAG, "Unable to send login message", e);
		}	
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

