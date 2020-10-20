package com.mfinance.everjoy.app.presenter;

import java.util.Locale;

import android.content.res.Resources;
import android.os.Messenger;

import com.mfinance.everjoy.app.CompanySettings;
import com.mfinance.everjoy.app.BasePopupDetailListener;
import com.mfinance.everjoy.app.MobileTraderApplication;
import com.mfinance.everjoy.app.PopupLiquidationDetailListener;
import com.mfinance.everjoy.app.bo.LiquidationRecord;
import com.mfinance.everjoy.app.util.ColorController;
import com.mfinance.everjoy.app.util.Utility;

public class LiquidationDetailPresenter extends BaseDetailPresenter{
	
	double dMPrice = 0.0;
	String sTmp = null; 
	double[] dBidAsk = null;
	
	LiquidationRecord record ;
	
	public LiquidationDetailPresenter(BasePopupDetailListener view,
			Resources res, MobileTraderApplication app, Messenger mService,
			Messenger mServiceMessengerHandler) {
		super(view, res, app, mService, mServiceMessengerHandler);
	}
	
	@Override
    public void updateUI() {
		if (app.selectedLiquidation != null){
			record = app.selectedLiquidation ;
			
			String sBRef = null;
			String sBRate = null;
			String sBDate = null;
			String sSRef = null;
			String sSRate = null;
			String sSDate = null;
			
			if(!CompanySettings.LIQ_DETAIL_BS_TO_OPEN_LIQ){
				sBRef = record.sBRef;
				sBRate = record.sBRate;
				sBDate = record.sBDate;
				sSRef = record.sSRef;
				sSRate = record.sSRate;
				sSDate = record.sSDate;
			}else{
				if(record.sBorS.equals("B")){
					sBRef = record.sBRef;
					sBRate = record.sBRate;
					sBDate = record.sBDate;
					sSRef = record.sSRef;
					sSRate = record.sSRate;
					sSDate = record.sSDate;
				}else{
					sBRef = record.sSRef;
					sBRate = record.sSRate;
					sBDate = record.sSDate;
					sSRef = record.sBRef;
					sSRate = record.sBRate;
					sSDate = record.sBDate;
				}
			}

			((PopupLiquidationDetailListener)view).getBuyRefField().setText(sBRef);
			((PopupLiquidationDetailListener)view).getBuyRateField().setText( sBRate);
			((PopupLiquidationDetailListener)view).getBuyDateField().setText( Utility.displayListingDate(sBDate));
			
			((PopupLiquidationDetailListener)view).getSellRefField().setText( sSRef);
			((PopupLiquidationDetailListener)view).getSellRateField().setText( sSRate);
			((PopupLiquidationDetailListener)view).getSellDateField().setText( Utility.displayListingDate(sSDate));
			
			if(((PopupLiquidationDetailListener)view).getBSField()!=null){
				if(CompanySettings.LIQ_DETAIL_BS_TO_OPEN_LIQ)
					((PopupLiquidationDetailListener)view).getBSField().setText( record.sBorS.equals("B")?Utility.getStringById("lb_b", res):Utility.getStringById("lb_s", res));
				else
					((PopupLiquidationDetailListener)view).getBSField().setText( record.sBorS.equals("B")?Utility.getStringById("lb_buy", res):Utility.getStringById("lb_sell", res));
			}

			if(((PopupLiquidationDetailListener)view).getAmountField()!=null)
				((PopupLiquidationDetailListener)view).getAmountField().setText("("+ Utility.formatAmount(record.dAmount)+")");
			((PopupLiquidationDetailListener)view).getContractField().setText( record.contract.getContractName(app.locale));
			((PopupLiquidationDetailListener)view).getLotField().setText( Utility.formatLot(record.dAmount /record.contract.iContractSize));

			if( CompanySettings.Inverse_Commission_Sign == true )
			{
				((PopupLiquidationDetailListener)view).getCommissionField().setText( Utility.formatValue(-record.dCommission));
				ColorController.setNumberColor(res, record.dCommission== (float)0.0 ? null : record.dCommission < 0 , ((PopupLiquidationDetailListener)view).getCommissionField());
			}
			else
			{
				((PopupLiquidationDetailListener)view).getCommissionField().setText( Utility.formatValue(record.dCommission));
				ColorController.setNumberColor(res, record.dCommission== (float)0.0 ? null : record.dCommission >= 0 , ((PopupLiquidationDetailListener)view).getCommissionField());
			}
			((PopupLiquidationDetailListener)view).getPLField().setText( Utility.formatValue(record.dPL));
			ColorController.setNumberColor(res, record.dPL >= 0 , ((PopupLiquidationDetailListener)view).getPLField());
			
			if(((PopupLiquidationDetailListener)view).getAPLField()!=null){
				((PopupLiquidationDetailListener)view).getAPLField().setText( Utility.formatValue(record.getAPL()));
				ColorController.setNumberColor(res, record.getAPL() >= 0 , ((PopupLiquidationDetailListener)view).getAPLField());
			}
			
			if(((PopupLiquidationDetailListener)view).getRunningPriceField()!=null){
				((PopupLiquidationDetailListener)view).getRunningPriceField().setText(record.sRRate);		
			}
			
		    if(((PopupLiquidationDetailListener)view).getInterestField()!=null)
		    	((PopupLiquidationDetailListener)view).getInterestField().setText(Utility.formatValue(record.dFloatInterest));
		}
    }
    
}
