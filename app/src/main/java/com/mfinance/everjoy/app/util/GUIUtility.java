package com.mfinance.everjoy.app.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mfinance.everjoy.app.CompanySettings;
import com.mfinance.everjoy.app.MobileTraderApplication;
import com.mfinance.everjoy.app.bo.ContractObj;
import com.mfinance.everjoy.app.pojo.ContractDefaultSetting;
import com.mfinance.everjoy.app.widget.wheel.WheelView;
import com.mfinance.everjoy.app.widget.wheel.adapter.ArrayWheelAdapter;

public class GUIUtility {
	public static final ArrayList<String> SLIPPAGE;
	
	static {
		SLIPPAGE = new ArrayList<String>();
		for(int i = 0; i < 1000; i++)
			SLIPPAGE.add(String.valueOf(i));
	}
	
	public static final ArrayList<ArrayList<String>> SECONDS = getSecondsList();
	
	private static ArrayList<ArrayList<String>> getSecondsList() {

		ArrayList<ArrayList<String>> alResult = new ArrayList<ArrayList<String>>();

		ArrayList<String> a1 = new ArrayList<String>();
		String[] s1 = new String[] { "0", "1", "2", "3", "4", "5", "6" };

		for (int i = 0; i < s1.length; i++) {
			a1.add(s1[i]);
		}
		alResult.add(a1);

		ArrayList<String> a2 = new ArrayList<String>();
		String[] s2 = new String[] { "0", "1", "2", "3", "4", "5", "6", "7",
				"8", "9" };

		for (int i = 0; i < s2.length; i++) {
			a2.add(s2[i]);
		}
		alResult.add(a2);

		return alResult;
	}
	
	public static void updateSecondsWheel(WheelView[] wvSeconds, String sValue) {

		int iWheelIndex = wvSeconds.length - 1;
		int iSecondsIndex = SECONDS.size() - 1;

		for (WheelView wv : wvSeconds) {
			wv.setCurrentItem(0);
		}

		for (int i = sValue.length() - 1; i >= 0; i--) {
			char cCur = sValue.charAt(i);
			StringBuilder sb = new StringBuilder();

			if (i != 0) {
				if (cCur == '.') {
					continue;
				}

				char cNext = sValue.charAt(i - 1);

				if (cNext == '.') {
					sb.append(cNext).append(cCur);
				} else {
					sb.append(cCur);
				}

			} else {
				sb.append(cCur);
			}

			if (cCur != '.') {
				wvSeconds[iWheelIndex].setCurrentItem(SECONDS.get(iSecondsIndex).indexOf(
						sb.toString()));
				iWheelIndex--;
				iSecondsIndex--;
			}
		}
	}
	
	public static String[] geneRateGroup(String sValue,
			int iNumOfGroup){
		int iLength = sValue.length();
		int iGroup = iNumOfGroup;
		String[] sGroup = new String[iNumOfGroup];

		int iIndex = iLength - 1;

		for (int i = 0; i < iGroup - 1; i++) {
			char cCur = sValue.charAt(iIndex);
			StringBuilder sb = new StringBuilder();

			if (iIndex != 0) {

				if ('.' == cCur) {
					iIndex--;
					cCur = sValue.charAt(iIndex);
				}

				char cNext = sValue.charAt(iIndex - 1);

				if (cNext == '.') {
					sb.append(cNext).append(cCur);
					iIndex--;
				} else {
					sb.append(cCur);
				}
			} else {
				sb.append(cCur);
			}
			sGroup[iGroup - 1 - i] = sb.toString();
			iIndex--;
		}

		sGroup[0] = (String) sValue.subSequence(0, iIndex + 1);
		return sGroup;
	}
	public static ArrayList<ArrayList<String>> generateRate(String sValue,
			int iNumOfGroup) {
		String[] sGroup = geneRateGroup(sValue,iNumOfGroup);

		ArrayList<ArrayList<String>> alWheel = new ArrayList<ArrayList<String>>();

		for (int index=0;index<sGroup.length;index++) {
			String s=sGroup[index];
			if (s.length() == 1 && index!=0) {
				ArrayList<String> alValue = new ArrayList<String>();

				for (int i = 0; i < 10; i++) {
					alValue.add(String.valueOf(i));
				}

				alWheel.add(alValue);
			} else if (s.length() == 2 && s.contains(".")) {
				ArrayList<String> alValue = new ArrayList<String>();

				for (int i = 0; i < 10; i++) {
					StringBuilder sb = new StringBuilder();
					sb.append(".").append(i);
					alValue.add(sb.toString());
				}

				alWheel.add(alValue);
			} else if (s.contains(".")) {
				double dOrig = Double.valueOf(s);
				int iDot = s.indexOf(".");
				int iRemain = s.length() - iDot - 1;
				double dPips = Math.pow(10, -iRemain);

				ArrayList<String> alValue = new ArrayList<String>();

				for (int i = CompanySettings.RateWheelRange; i > 0; i--) {
					double dValue = dOrig - dPips * i;
					if (Double.compare(Utility.roundToDouble(dValue, iRemain, iRemain), 0.00) >= 0 )
						alValue.add(Utility.round(dValue, iRemain, iRemain));
				}

				alValue.add(Utility.round(dOrig, iRemain, iRemain));

				for (int i = 1; i <= CompanySettings.RateWheelRange; i++) {
					double dValue = dOrig + dPips * i;
					alValue.add(Utility.round(dValue, iRemain, iRemain));
				}

				alWheel.add(alValue);
			} else {
				int iOrig = Integer.valueOf(s);

				ArrayList<String> alValue = new ArrayList<String>();

				for (int i = CompanySettings.RateWheelRange; i > 0; i--) {
					int iValue = iOrig - i;
					if(iValue>=0){
						alValue.add(String.valueOf(iValue));
					}
				}

				alValue.add(String.valueOf(iOrig));

				for (int i = 1; i <= CompanySettings.RateWheelRange; i++) {
					int iValue = iOrig + i;
					alValue.add(String.valueOf(iValue));
				}

				alWheel.add(alValue);
			}
		}
		return alWheel;
	}

	public static ArrayList<ArrayList<String>> generateAlertRate(String sValue,
															int iNumOfGroup) {
		String[] sGroup = geneRateGroup(sValue,iNumOfGroup);

		ArrayList<ArrayList<String>> alWheel = new ArrayList<ArrayList<String>>();

		for (int index=0;index<sGroup.length;index++) {
			String s=sGroup[index];
			if (s.length() == 1 && index!=0) {
				ArrayList<String> alValue = new ArrayList<String>();

				for (int i = 0; i < 10; i++) {
					alValue.add(String.valueOf(i));
				}

				alWheel.add(alValue);
			} else if (s.length() == 2 && s.contains(".")) {
				ArrayList<String> alValue = new ArrayList<String>();

				for (int i = 0; i < 10; i++) {
					StringBuilder sb = new StringBuilder();
					sb.append(".").append(i);
					alValue.add(sb.toString());
				}

				alWheel.add(alValue);
			} else if (s.contains(".")) {
				double dOrig = Double.valueOf(s);
				int iDot = s.indexOf(".");
				int iRemain = s.length() - iDot - 1;
				double dPips = Math.pow(10, -iRemain);

				ArrayList<String> alValue = new ArrayList<String>();

				for (int i = CompanySettings.AlertRateWheelRange; i > 0; i--) {
					double dValue = dOrig - dPips * i;
					if (Double.compare(Utility.roundToDouble(dValue, iRemain, iRemain), 0.00) >= 0 )
						alValue.add(Utility.round(dValue, iRemain, iRemain));
				}

				alValue.add(Utility.round(dOrig, iRemain, iRemain));

				for (int i = 1; i <= CompanySettings.AlertRateWheelRange; i++) {
					double dValue = dOrig + dPips * i;
					alValue.add(Utility.round(dValue, iRemain, iRemain));
				}

				alWheel.add(alValue);
			} else {
				int iOrig = Integer.valueOf(s);

				ArrayList<String> alValue = new ArrayList<String>();

				for (int i = CompanySettings.AlertRateWheelRange; i > 0; i--) {
					int iValue = iOrig - i;
					if(iValue>=0){
						alValue.add(String.valueOf(iValue));
					}
				}

				alValue.add(String.valueOf(iOrig));

				for (int i = 1; i <= CompanySettings.AlertRateWheelRange; i++) {
					int iValue = iOrig + i;
					alValue.add(String.valueOf(iValue));
				}

				alWheel.add(alValue);
			}
		}
		return alWheel;
	}
	
	public static void initWheel(Context context, WheelView wheel,
								 List<String> value, int iVisible, int iSelected, int[] colors) {
		wheel.setViewAdapter(new ArrayWheelAdapter<String>(context, value
				.toArray(new String[value.size()])));
		wheel.setVisibleItems(iVisible);
		wheel.setCurrentItem(iSelected);
		//wheel.setShadowsColors(colors);
	}

	public static void initWheel(Context context, WheelView wheel,
								 List<String> value, int iVisible, int iSelected) {
		wheel.setViewAdapter(new ArrayWheelAdapter<String>(context, value
				.toArray(new String[value.size()])));
		wheel.setVisibleItems(iVisible);
		wheel.setCurrentItem(iSelected);
	}

	public static void updateRateWheel(Context context, WheelView[] wvLot,
									   List<ArrayList<String>> alLot, String sValue) {
		for (int i = 0; i < wvLot.length; i++) {
			WheelView wv = wvLot[i];
			if (wv.getViewAdapter() == null) {
				wv.setViewAdapter(new ArrayWheelAdapter<String>(context, alLot
						.get(i).toArray(new String[alLot.get(i).size()])));
			} else {
				((ArrayWheelAdapter) wv.getViewAdapter()).updateItems(alLot
						.get(i).toArray(new String[alLot.get(i).size()]));
			}
		}

		String[] group=geneRateGroup(sValue,wvLot.length);
		
		for (int i = 0; i < wvLot.length; i++) {
			ArrayList<String> alRate = alLot.get(i);
			wvLot[i].setCurrentItem(alRate.indexOf(group[i]));
		}
	}


	public static void updateRateDepth(boolean bUpdateButton, ContractObj contract,
								  String sBuySell, int iLimitStop, View vCurRate, View vDirection,
								  View vReqRate, Resources res, String lot) {
		updateRateDepth(bUpdateButton, contract,
				sBuySell, iLimitStop, vCurRate, vDirection,
				vReqRate, null, res, lot, null);
	}

	/**
	 * @param bUpdateButton Need to update view of request rate
	 * @param contract Target contract
	 * @param sBuySell Buy or Sell
	 * @param iLimitStop Limit  = 0 or Stop = 1
	 * @param viewHolder viewHolder
	 * @param res Resource (Color, Text, etc)
	 * @return rate to display
	 */
	public static String updateRate(boolean bUpdateButton, ContractObj contract, String sBuySell, int iLimitStop, GUIUtilityUpdateRateViewHolder viewHolder, Resources res, ContractDefaultSetting defaultSetting) {

		View vCurRate = viewHolder.vCurRate;
		View vDirection = viewHolder.vDirection;
		View vReqRate = viewHolder.vReqRate;
		View vBtnRate = viewHolder.vBtnRate;
		String sDirection = "-";							//Direct
		double dOrderPips = contract.iOrderPips;			//Order Pips
		double dSpread = 0.0d;

		//Transfer pips to rate e.g. LLG 1344.9 -> 1 pip = 0.1

		if (dOrderPips > 0) {
			dSpread = Math.abs((dOrderPips - 1)				
					* Math.pow(10, -contract.iRateDecPt)); 
		} else {
			dSpread = 0;
		}

		Optional<Integer> orderPips = iLimitStop == 0 ? defaultSetting.getDefaultTakeProfitOrderPips() : defaultSetting.getDefaultStopLossOrderPips();
		double initialOrderPips = orderPips.map(o -> (double)o).orElse(dOrderPips);
		double dInitialSpread = initialOrderPips > 0 ? Math.abs(initialOrderPips * Math.pow(10, -contract.iRateDecPt)) : 0;

		//iLimitStop = 0 = Limit Order, 1 = Stop Order  
		if( contract.getBSD() == true )
		{
			if ("B".equals(sBuySell) && iLimitStop == 0 || "S".equals(sBuySell)
					&& iLimitStop == 1) {
				sDirection = ">";
			} else {
				dInitialSpread *= -1;
				dSpread *= -1;
				sDirection = "<";
			}
		}
		else
		{
			if ("B".equals(sBuySell) && iLimitStop == 0 || "S".equals(sBuySell)
					&& iLimitStop == 1) {
				dInitialSpread *= -1;
				dSpread *= -1;
				sDirection = "<";
			} else {
				sDirection = ">";
			}	
		}		

		String sLbRate = null;	
		String sBtnRate = null;

		//contract.getBidAsk()[0] = Bid Rate, contract.getBidAsk()[1] = Ask Rate
		if (vBtnRate!=null) {
			double BidAskSpread = contract.getBidAsk()[1] - contract.getBidAsk()[0];
			// this flow is for calculating oco order price, need to +/- the bid ask spread since the vBtnRate is current target rate! 
			if ( (contract.getBSD() == true && "S".equals(sBuySell)) || (contract.getBSD() == false && "B".equals(sBuySell)) )
			{
				sLbRate = Utility.round(Double.valueOf(((Button)vBtnRate).getText().toString()) + dSpread + BidAskSpread,
						contract.iRateDecPt, contract.iRateDecPt);
				sBtnRate = Utility.round(Double.valueOf(((Button)vBtnRate).getText().toString()) + dInitialSpread + BidAskSpread,
						contract.iRateDecPt, contract.iRateDecPt);				
			}
			else
			{
				sLbRate = Utility.round(Double.valueOf(((Button)vBtnRate).getText().toString()) + dSpread - BidAskSpread,
						contract.iRateDecPt, contract.iRateDecPt);
				sBtnRate = Utility.round(Double.valueOf(((Button)vBtnRate).getText().toString()) + dInitialSpread - BidAskSpread,
						contract.iRateDecPt, contract.iRateDecPt);				
			}
		}else{
			if ((contract.getBSD() == true && "S".equals(sBuySell)) || (contract.getBSD() == false && "B".equals(sBuySell))) {
				sLbRate = Utility.round(contract.getBidAsk()[1] + dSpread,
						contract.iRateDecPt, contract.iRateDecPt);
				sBtnRate = Utility.round(contract.getBidAsk()[1] + dInitialSpread,
						contract.iRateDecPt, contract.iRateDecPt);
			} else {
				sLbRate = Utility.round(contract.getBidAsk()[0] + dSpread,
						contract.iRateDecPt, contract.iRateDecPt);
				sBtnRate = Utility.round(contract.getBidAsk()[0] + dInitialSpread,
						contract.iRateDecPt, contract.iRateDecPt);
			}
		}
		
		//Update GUI
		if (vCurRate instanceof TextView) {
			((TextView) vCurRate).setText(sLbRate);
		}

		if(vBtnRate==null){
			if (contract.bChangeBidAsk) {
				ColorController.setPriceColor(res, contract.iBidUpDown, vCurRate);
			} else {
				ColorController.setPriceColor(res, 0, vCurRate);
			}
		}
		
		if (vDirection != null && vDirection instanceof TextView) {
			((TextView) vDirection).setText(sDirection);
		}

		if (bUpdateButton) {
			if (vReqRate instanceof TextView) {
				((TextView) vReqRate).setText(sBtnRate);
			} else if (vReqRate instanceof Button) {
				((Button) vReqRate).setText(sBtnRate);
			}
		}
		return sBtnRate;
	}

	public static void updateRateDepth(boolean bUpdateButton, ContractObj contract,
									   String sBuySell, int iLimitStop, View vCurRate, View vDirection,
									   View vReqRate, View vBtnRate, Resources res, String lot) {
		updateRateDepth(bUpdateButton, contract,
				sBuySell, iLimitStop, vCurRate, vDirection,
				vReqRate, null, res, lot, null);
	}

	public static void updateRateDepth(boolean bUpdateButton, ContractObj contract,
								  String sBuySell, int iLimitStop, View vCurRate, View vDirection,
								  View vReqRate, View vBtnRate, Resources res, String lot, String currRate) {
		//bUpdateButton: Need to update view of request rate
		//contract: Target contract
		//sBuySell: Buy or Sell
		//iLimitStop: Limit or Stop
		//vCurRate: View for display current rate
		//vDirection: View for display arrow direction
		//vReqRate: View for display request rate
		//res: Resource (Color, Text, etc)
		System.out.println(vCurRate);

		String sDirection = "-";							//Direct
		double dOrderPips = contract.iOrderPips;			//Order Pips
		double dInitialSpread = 0.0d;						//Initial Spread
		double dSpread = 0.0d;

		//Transfer pips to rate e.g. LLG 1344.9 -> 1 pip = 0.1
		if (dOrderPips > 0) {
			dInitialSpread = Math.abs(dOrderPips
					* Math.pow(10, -contract.iRateDecPt)); //contract.iRateDecPt > Decimal place
			dSpread = Math.abs((dOrderPips - 1)
					* Math.pow(10, -contract.iRateDecPt));
		} else {
			dInitialSpread = 0;
			dSpread = 0;
		}

		//iLimitStop = 0 = Limit Order, 1 = Stop Order
		if( contract.getBSD() == true )
		{
			if ("B".equals(sBuySell) && iLimitStop == 0 || "S".equals(sBuySell)
					&& iLimitStop == 1) {
				sDirection = ">";
			} else {
				dInitialSpread *= -1;
				dSpread *= -1;
				sDirection = "<";
			}
		}
		else
		{
			if ("B".equals(sBuySell) && iLimitStop == 0 || "S".equals(sBuySell)
					&& iLimitStop == 1) {
				dInitialSpread *= -1;
				dSpread *= -1;
				sDirection = "<";
			} else {
				sDirection = ">";
			}
		}

		String sLbRate = null;
		String sBtnRate = null;

		String sTmp;

		//contract.getBidAsk()[0] = Bid Rate, contract.getBidAsk()[1] = Ask Rate
		if ((contract.getBSD() == true && "S".equals(sBuySell)) || (contract.getBSD() == false && "B".equals(sBuySell))) {
			double[] askLot = contract.askLotDepth;
			sTmp = Utility.round(getMax(contract.askPriceDepth), contract.iRateDecPt, contract.iRateDecPt);
			double accLot = 0;
			for (int i = 0; i < askLot.length; i++) {
				accLot += askLot[i];
				if (Double.parseDouble(lot) <= accLot) {
					sTmp = Utility.round(contract.askPriceDepth[i], contract.iRateDecPt, contract.iRateDecPt);
					break;
				}
			}
			sTmp = String.valueOf(Utility.roundToDouble(Double.parseDouble(sTmp) + (contract.dOffset + contract.dAskAdjust) * Math.pow(10.0, -contract.iRateDecPt), contract.iRateDecPt, contract.iRateDecPt));
		}
		else {
			double[] bidLot = contract.askLotDepth;
			sTmp = Utility.round(getMin(contract.bidPriceDepth), contract.iRateDecPt, contract.iRateDecPt);
			double accLot = 0;
			for (int i = 0; i < bidLot.length; i++) {
				accLot += bidLot[i];
				if (Double.parseDouble(lot) <= accLot) {
					sTmp = Utility.round(contract.bidPriceDepth[i], contract.iRateDecPt, contract.iRateDecPt);
					break;
				}
			}
			sTmp = String.valueOf(Utility.roundToDouble(Double.parseDouble(sTmp) + (contract.dOffset + contract.dBidAdjust) * Math.pow(10.0, -contract.iRateDecPt), contract.iRateDecPt, contract.iRateDecPt));
		}

		if(vBtnRate!=null){
			double BidAskSpread = contract.getBidAsk()[1]- contract.getBidAsk()[0];
			// this flow is for calculating oco order price, need to +/- the bid ask spread since the vBtnRate is current target rate!
			if ( (contract.getBSD() == true && "S".equals(sBuySell)) || (contract.getBSD() == false && "B".equals(sBuySell)) )
			{
				sLbRate = Utility.round(Double.valueOf(((Button)vBtnRate).getText().toString()) + dSpread + BidAskSpread,
						contract.iRateDecPt, contract.iRateDecPt);
				sBtnRate = Utility.round(Double.valueOf(((Button)vBtnRate).getText().toString()) + dInitialSpread + BidAskSpread,
						contract.iRateDecPt, contract.iRateDecPt);
			}
			else
			{
				sLbRate = Utility.round(Double.valueOf(((Button)vBtnRate).getText().toString()) + dSpread - BidAskSpread,
						contract.iRateDecPt, contract.iRateDecPt);
				sBtnRate = Utility.round(Double.valueOf(((Button)vBtnRate).getText().toString()) + dInitialSpread - BidAskSpread,
						contract.iRateDecPt, contract.iRateDecPt);
			}
		}
		else {
			if ((contract.getBSD() == true && "S".equals(sBuySell)) || (contract.getBSD() == false && "B".equals(sBuySell))) {
				sLbRate = Utility.round(Double.parseDouble(sTmp) + dSpread,
						contract.iRateDecPt, contract.iRateDecPt);
				sBtnRate = Utility.round(Double.parseDouble(sTmp) + dInitialSpread,
						contract.iRateDecPt, contract.iRateDecPt);
			} else {
				sLbRate = Utility.round(Double.parseDouble(sTmp) + dSpread,
						contract.iRateDecPt, contract.iRateDecPt);
				sBtnRate = Utility.round(Double.parseDouble(sTmp) + dInitialSpread,
						contract.iRateDecPt, contract.iRateDecPt);
			}
		}


		//Update GUI
		if (vCurRate instanceof TextView) {
			((TextView) vCurRate).setText(sLbRate);
		}

		if(vBtnRate==null){
			if (contract.bChangeBidAsk) {
				ColorController.setPriceColor(res, contract.iBidUpDown, vCurRate);
			} else {
				ColorController.setPriceColor(res, 0, vCurRate);
			}
		}

		if (vDirection != null && vDirection instanceof TextView) {
			((TextView) vDirection).setText(sDirection);
		}

		if (bUpdateButton) {
			if (vReqRate instanceof TextView) {
				((TextView) vReqRate).setText(sBtnRate);
			} else if (vReqRate instanceof Button) {
				((Button) vReqRate).setText(sBtnRate);
			}
		}
	}

	public static void updateRateDepth(boolean bUpdateButton,
								  MobileTraderApplication app, View vCurRate, View vDirection,
								  View vReqRate, Resources res, String lot) {
		updateRateDepth(bUpdateButton, app.getSelectedContract(), app
						.getSelectedBuySell(), app.getSelectedLimitStop(), vCurRate,
				vDirection, vReqRate, res, lot);
	}

	public static ArrayList<ArrayList<String>> LOT = getLOTList();

	private static ArrayList<ArrayList<String>> getLOTList() {

		ArrayList<ArrayList<String>> alResult = new ArrayList<ArrayList<String>>();

		ArrayList<String> a1 = new ArrayList<String>();
		String[] s1 = CompanySettings.s1;

		for (int i = 0; i < s1.length; i++) {
			a1.add(s1[i]);
		}
		alResult.add(a1);

		ArrayList<String> a2 = new ArrayList<String>();
		String[] s2 = CompanySettings.s2;

		for (int i = 0; i < s2.length; i++) {
			a2.add(s2[i]);
		}
		alResult.add(a2);

		ArrayList<String> a3 = new ArrayList<String>();
		String[] s3 = CompanySettings.s3;

		for (int i = 0; i < s3.length; i++) {
			a3.add(s3[i]);
		}
		alResult.add(a3);
		return alResult;
	}
	
	public static final String[] chartPeriods = new String[]{
			"OneMinForexData", "FiveMinForexData", "HourlyForexData","DailyForexData","WeeklyForexData","MonthlyForexData"
	};
	
	public static final String[] chartTypes = new String[]{
			"1","2","3"
	};
	
	public static String getURL(String sContract, int iPeriod, int iType){		
		String strCur = sContract;	
		if(strCur != null){
			if(strCur.equals("LLG")){
				strCur = "LGD";
			}else if(strCur.equals("LLS")){
				strCur = "LSI";
			}
			
			StringBuilder sb = new StringBuilder();			
			String sChartURL = "http://202.76.10.175:8080/chart/";
			
			sb.append(sChartURL).append(strCur).append("_")
				.append(GUIUtility.chartPeriods[iPeriod]).append("_")
				.append(GUIUtility.chartTypes[iType]).append(".png");
			 		
			return sb.toString();
		}else{
			return null;
		}
	}
	
	public void showConfirmDialog(Context owner, String sMessage){
		AlertDialog dialog = new AlertDialog.Builder(owner, CompanySettings.alertDialogTheme).create();
		dialog.setMessage(sMessage);
		dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Do you want to exit?",
		new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
	
		}
		});

		dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Save Settings?",
		new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
	
		}
		});
		dialog.show();
	}

	// Method for getting the maximum value
	public static String getMax(String[] inputArray){
		double maxValue = Double.parseDouble(inputArray[0]);
		for(int i=1;i < inputArray.length;i++){
			try {
				if (Double.parseDouble(inputArray[i]) > maxValue) {
					maxValue = Double.parseDouble(inputArray[i]);
				}
			}
			catch(Exception e){
				return String.valueOf(maxValue);
			}
		}
		return String.valueOf(maxValue);
	}

	// Method for getting the minimum value
	public static String getMin(String[] inputArray){
		double minValue = Double.parseDouble(inputArray[0]);
		for(int i=1;i<inputArray.length;i++){
			try {
				if (Double.parseDouble(inputArray[i]) < minValue && Double.parseDouble(inputArray[i])>0) {
					minValue = Double.parseDouble(inputArray[i]);
				}
			}
			catch(Exception e){
				return String.valueOf(minValue);
			}
		}
		return String.valueOf(minValue);
	}

	public static View.OnTouchListener getForceClickOnTouchListener() {
		return new View.OnTouchListener() {
			private boolean moving = false;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						return true;
					case MotionEvent.ACTION_UP:
						v.performClick();
						return true;
					case MotionEvent.ACTION_CANCEL:
						if (moving) {
							moving = false;
						} else {
							v.performClick();
							return true;
						}
						return false;
					case MotionEvent.ACTION_MOVE:
						moving = true;
					default:
						return false;
				}
			}
		};
	}

	public static boolean isMotionEventInsideView(View v, MotionEvent e) {
		return !(e.getX() < 0 || e.getY() < 0
				|| e.getX() > v.getMeasuredWidth()
				|| e.getY() > v.getMeasuredHeight());
	}
}
