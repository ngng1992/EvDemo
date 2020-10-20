package com.mfinance.everjoy.app.service.internal;

import com.mfinance.everjoy.app.CompanySettings;
import com.mfinance.everjoy.app.MobileTraderApplication;
import com.mfinance.everjoy.app.bo.OrderRecord;
import com.mfinance.everjoy.app.bo.TransactionObj;
import com.mfinance.everjoy.app.bo.TransactionObjBuilder;
import com.mfinance.everjoy.app.constant.IDDictionary;
import com.mfinance.everjoy.app.constant.Protocol;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.service.FxMobileTraderService;
import com.mfinance.everjoy.app.util.CommonFunction;
import com.mfinance.everjoy.app.util.MessageMapping;
import com.mfinance.everjoy.app.util.MessageObj;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;

public class CancelOrderRequestProcessor implements MessageProcessor{
	public enum RequestType {
		CANCEL(0), CANCEL_MULTIPLE(1);
		private int value;
		RequestType(int value) {
			this.value = value;
		}
		public int getValue() {
			return value;
		}
		public static RequestType fromInt(int i) {
			for (RequestType r : RequestType.values()) {
				if (r.value == i) {
					return r;
				}
			}
			return CANCEL;
		}
	}
	private final String TAG = this.getClass().getSimpleName();
	@Override
	public boolean processMessage(Message msg, FxMobileTraderService service) {
		RequestType requestType = RequestType.fromInt(msg.arg1);
		Bundle data = msg.getData();
		switch (requestType) {
			case CANCEL:
				int iRef = data.getInt(ServiceFunction.SEND_CANCEL_ORDER_REQUEST_ORDER_REF);
				return cancelOrder(service, iRef);
			case CANCEL_MULTIPLE:
				ArrayList<Integer> ordernos = data.getIntegerArrayList("ordernos");
				for (int i : ordernos) {
					if (!cancelOrder(service, i)) {
						return false;
					}
				}
				return true;
			default:
				return true;
		}
	}

	private boolean cancelOrder(FxMobileTraderService service, int iRef) {
		OrderRecord order = service.app.data.getRunningOrder(iRef);


		String sTransactionID = CommonFunction.getTransactionID(service.app.data.getBalanceRecord().strAccount);

		boolean newProtocol = false;
		MobileTraderApplication app = service.app;
		if(app.isDemoPlatform == true && CompanySettings.USE_NEW_DEAL_PROTOCOL_DEMO == true){
			newProtocol = true;
		}
		else if( CompanySettings.checkProdServer() == 1 && CompanySettings.USE_NEW_DEAL_PROTOCOL == true )
		{
			newProtocol = true;
		}
		else if( CompanySettings.checkProdServer() == 2 && CompanySettings.USE_NEW_DEAL_PROTOCOL_PROD2 == true )
		{
			newProtocol = true;
		}
		else if( CompanySettings.checkProdServer() == 3 && CompanySettings.USE_NEW_DEAL_PROTOCOL_PROD3 == true )
		{
			newProtocol = true;
		}
		else if( CompanySettings.checkProdServer() == 4 && CompanySettings.USE_NEW_DEAL_PROTOCOL_PROD4 == true )
		{
			newProtocol = true;
		}
		else if( CompanySettings.checkProdServer() == 5 && CompanySettings.USE_NEW_DEAL_PROTOCOL_PROD5 == true )
		{
			newProtocol = true;
		}
		else if( CompanySettings.checkProdServer() == 6 && CompanySettings.USE_NEW_DEAL_PROTOCOL_PROD6 == true )
		{
			newProtocol = true;
		}
		else if( CompanySettings.checkProdServer() == 7 && CompanySettings.USE_NEW_DEAL_PROTOCOL_PROD7 == true )
		{
			newProtocol = true;
		}
		else if( CompanySettings.checkProdServer() == 8 && CompanySettings.USE_NEW_DEAL_PROTOCOL_PROD8 == true )
		{
			newProtocol = true;
		}
		else if( CompanySettings.checkProdServer() == 9 && CompanySettings.USE_NEW_DEAL_PROTOCOL_PROD9 == true )
		{
			newProtocol = true;
		}
		else if( CompanySettings.checkProdServer() == 10 && CompanySettings.USE_NEW_DEAL_PROTOCOL_PROD10 == true )
		{
			newProtocol = true;
		}

		try {
			MessageObj orderRequestMsg = MessageObj.getMessageObj(IDDictionary.SERVER_IO_SERVICE_TYPE, newProtocol ? IDDictionary.SERVER_IO_REQUEST_CANCEL_ORDER : IDDictionary.SERVER_IO_REQUEST_CANCEL_ORDER_OLD);

			orderRequestMsg.addField(Protocol.CancelOrderRequest.GROUP_CODE, service.app.data.getBalanceRecord().strGroup);
			orderRequestMsg.addField(Protocol.CancelOrderRequest.ACCOUNT_CODE, service.app.data.getBalanceRecord().strAccount);
			orderRequestMsg.addField("accountID", service.app.data.getBalanceRecord().strAccount);
			orderRequestMsg.addField(Protocol.CancelOrderRequest.ACTION, "cancel");
			orderRequestMsg.addField(Protocol.CancelOrderRequest.ORDER_REF, String.valueOf(iRef));
			orderRequestMsg.addField(Protocol.CancelOrderRequest.REPLY, "accept");
			orderRequestMsg.addField(Protocol.CancelOrderRequest.TYPE, "1");
			orderRequestMsg.addField(Protocol.CancelOrderRequest.ORDER_TYPE, "0");
			orderRequestMsg.addField(Protocol.CancelOrderRequest.OVERRIDE, "1");
			orderRequestMsg.addField(Protocol.CancelOrderRequest.USER_ID, service.app.data.getStrUser());
			orderRequestMsg.addField(Protocol.CancelOrderRequest.USER_PASSWORD, service.app.data.getStrPassword());
			orderRequestMsg.addField(Protocol.CancelOrderRequest.DIRECT, "n");
			orderRequestMsg.addField(Protocol.CancelOrderRequest.IS_AUTO_DEAL, "1");
			orderRequestMsg.addField(Protocol.CancelOrderRequest.ROLE, "2");
			orderRequestMsg.addField(Protocol.CancelOrderRequest.LIQUIDATION_METHOD, "-1");
			orderRequestMsg.addField(Protocol.CancelOrderRequest.LIQUIDATION_REF, "");
			orderRequestMsg.addField(Protocol.CancelOrderRequest.COMMENT, "");
			orderRequestMsg.addField(Protocol.CancelOrderRequest.OCO, "-1");
			orderRequestMsg.addField(Protocol.CancelOrderRequest.LIQUIDATION_RATE, "");
			orderRequestMsg.addField(Protocol.CancelOrderRequest.PEND_ID, sTransactionID);

			TransactionObj transaction = new TransactionObjBuilder().setsTransactionID(sTransactionID).setsRef("").setsContract(order.strContract).setsAccount(service.app.data.getBalanceRecord().strAccount).setsBuySell(order.strBuySell).setdRequestRate(Double.valueOf(order.dRequestRate)).setiStatus(0).setsStatusMsg(MessageMapping.getMessageByCode(service.getRes(), "917", service.app.locale)).setiMsg(923).setiRemarkCode(923).setiType(1).setdAmount(-1).setContract(order.contract).createTransactionObj();
			transaction.sMsgCode = "917";
			service.addTransaction(transaction);

			service.connection.sendMessage(orderRequestMsg.convertToString(true));
			//Log.i(TAG, "[Sent order canceling request.]");

			return true;
		} catch (Exception e) {
			Log.e(TAG, "Exception in processMessage",e.fillInStackTrace());
			return false;
		}
	}
	
}


