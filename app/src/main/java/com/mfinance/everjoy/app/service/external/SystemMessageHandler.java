package com.mfinance.everjoy.app.service.external;

import android.os.Bundle;
import android.util.Log;

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
		Log.e(TAG, "SMH: "  + msgObj.convertToString());
		
		final String sCode = msgObj.getField(Protocol.SystemMessage.MSG);
		int iCode = Utility.toInteger(sCode, -1);
		String strMsgCode;
		if (iCode > 0) {
			strMsgCode = sCode;
		} else {
			strMsgCode = msgObj.getField(Protocol.SystemMessage.MSGCD + "1");
		}

		if (strMsgCode.equals("8001")) {
			Bundle data = new Bundle();
			String sMsg = MessageMapping.getMessageByCode(service.getRes(), sCode,service.app.locale);
			data.putString(ServiceFunction.MESSAGE, sMsg);
			service.broadcast(ServiceFunction.ACT_SHOW_TOAST, data);
			service.broadcast(ServiceFunction.ACT_DISCONNECT_DUPLICATE, null);
		} else if (strMsgCode.equals("8003")) {
			Bundle data = new Bundle();
			String sMsg = MessageMapping.getMessageByCode(service.getRes(), sCode,service.app.locale);
			data.putString(ServiceFunction.MESSAGE, sMsg);
			service.broadcast(ServiceFunction.ACT_SHOW_TOAST, data);
			service.broadcast(ServiceFunction.ACT_DISCONNECT_DUPLICATE, null);
			service.broadcast(ServiceFunction.ACT_GO_TO_MAIN_PAGE, null);
		}
		
//		String sMsg = "";
//		if (iCode > 0) {
//			sMsg = MessageMapping.getMessageByCode(service.getRes(), sCode,service.app.locale);
//		} else {
//			StringBuffer strTmpBuffer = new StringBuffer();
//			if (strMsgCode != null && strMsgCode.length() > 0) {
//				if (strMsgCode.equals("")) {
//				}
//			}
//		}

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
