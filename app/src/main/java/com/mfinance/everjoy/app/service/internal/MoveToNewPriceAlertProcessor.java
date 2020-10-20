package com.mfinance.everjoy.app.service.internal;

import android.os.Message;

import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.pojo.PriceAlertObject;
import com.mfinance.everjoy.app.service.FxMobileTraderService;

public class MoveToNewPriceAlertProcessor implements MessageProcessor {
	private final String TAG = this.getClass().getSimpleName();
	@Override
	public boolean processMessage(Message msg, FxMobileTraderService service) {
		String sContract = msg.getData().getString(ServiceFunction.PRICE_ALERT_CONTRACT);
		String sRef = msg.getData().getString(ServiceFunction.PRICE_ALERT_REF);

		if (sContract != null){
			service.app.data.setSelectedPriceAlertRef(-1);
			service.app.setSelectedContract(sContract);
		}
		else {
			if (sRef != null) {
				PriceAlertObject item = service.app.data.getPriceAlertListByRef(Integer.parseInt(sRef));
				if (item != null) {
					service.app.data.setSelectedPriceAlertRef(Integer.parseInt(sRef));
					service.app.setSelectedContract(item.getMkt());
				} else {
					service.app.data.setSelectedPriceAlertRef(-1);
					if (service.app.getDefaultContract() != null)
						service.app.setSelectedContract(service.app.getDefaultContract().strContractCode);
					else {
						service.app.setSelectedContract((String) null);
					}
				}
			} else {
				service.app.data.setSelectedPriceAlertRef(-1);
				if (service.app.getDefaultContract() != null)
					service.app.setSelectedContract(service.app.getDefaultContract().strContractCode);
				else {
					service.app.setSelectedContract((String) null);
				}
			}
		}

		service.broadcast(ServiceFunction.ACT_GO_TO_NEW_PRICE_ALERT, msg.getData());
		return true;
	}

}
