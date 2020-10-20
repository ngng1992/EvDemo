package com.mfinance.everjoy.app;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.util.GUIUtility;
import com.mfinance.everjoy.app.util.Utility;
import com.mfinance.everjoy.app.widget.quickaction.CustomPopupWindow;
import com.mfinance.everjoy.app.widget.wheel.WheelView;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static com.mfinance.everjoy.app.CompanySettings.NUM_OF_LOT_DP;

public class PopupLOT {
	public CustomPopupWindow popup;
	public WheelView[] wvLot = new WheelView[3];
	public Button btnCommit;
	public Button btnClose;
	final private BigDecimal maxLot;
	final private int decimalPlace;
	final private int wvLotExponent0;
	final private int wvLotExponent1;
	final private int wvLotExponent2;
	final private List<String> firstDecimalDigits = new ArrayList<>();
	final private List<String> firstDigits = new ArrayList<>();
	final private List<String> secondDigits = new ArrayList<>();

	public PopupLOT(Context context, View vParent, BigDecimal maxLot) {
		this(context, vParent, maxLot, NUM_OF_LOT_DP);
	}
	
	public PopupLOT(Context context, View vParent, BigDecimal maxLot, int decimalPlace){
		popup = new CustomPopupWindow(vParent);	
		popup.setContentView(R.layout.popup_lot);
		this.decimalPlace = decimalPlace;
		this.maxLot = maxLot;
		
		wvLot[0] = popup.findViewById(R.id.p1);
		wvLot[1] = popup.findViewById(R.id.p2);
		wvLot[2] = popup.findViewById(R.id.p3);

		switch (decimalPlace) {
			case 0:
				wvLotExponent0 = 2;
				wvLotExponent1 = 1;
				wvLotExponent2 = 0;
				break;
			case 1:
				wvLotExponent0 = 1;
				wvLotExponent1 = 0;
				wvLotExponent2 = -1;
				break;
			case 2:
			default:
				wvLotExponent0 = 0;
				wvLotExponent1 = -1;
				wvLotExponent2 = -2;
				break;
		}

		int firstDecimalDigitMax;
		if (maxLot.compareTo(power(BigDecimal.valueOf(10), (wvLotExponent2 + 1))) > 0) {
			firstDecimalDigitMax = 10;
		} else {
			firstDecimalDigitMax = maxLot.divide(power(BigDecimal.valueOf(10), wvLotExponent2), RoundingMode.HALF_DOWN).intValue();
		}
		int firstDigitMax;
		if (maxLot.compareTo(power(BigDecimal.valueOf(10), (wvLotExponent1 + 1))) > 0) {
			firstDigitMax = 10;
		} else {
			firstDigitMax = maxLot.divide(power(BigDecimal.valueOf(10), wvLotExponent1), RoundingMode.HALF_DOWN).intValue();
		}
		int secondDigitMax = maxLot.divide(power(BigDecimal.valueOf(10), wvLotExponent0), RoundingMode.HALF_DOWN).intValue();
		if (secondDigitMax == 0 && firstDigitMax < 10 && firstDigitMax > 0) {
			firstDigitMax = firstDigitMax + 1;
		}
		if (firstDigitMax == 0 && firstDecimalDigitMax > 0) {
			firstDecimalDigitMax = firstDecimalDigitMax + 1;
		}
		for (int i = 0; i < firstDecimalDigitMax ; i++) {
			firstDecimalDigits.add(i + (wvLotExponent2 == 0 ? "." : ""));
		}
		if (firstDecimalDigits.size() == 0) {
			firstDecimalDigits.add("0" + (wvLotExponent2 == 0 ? "." : ""));
		}
		for (int i = 0; i < firstDigitMax ; i++) {
			firstDigits.add(i + (wvLotExponent1 == 0 ? "." : ""));
		}
		if (firstDigits.size() == 0) {
			firstDigits.add("0" + (wvLotExponent1 == 0 ? "." : ""));
		}
		for (int i = 0; i <= secondDigitMax ; i++) {
			secondDigits.add(i + (wvLotExponent0 == 0 ? "." : ""));
		}
		if (secondDigits.size() == 0) {
			secondDigits.add("0" + (wvLotExponent0 == 0 ? "." : ""));
		}

		GUIUtility.initWheel(context, wvLot[0], secondDigits, 5, 0);
		GUIUtility.initWheel(context, wvLot[1], firstDigits, 5, 1);
		GUIUtility.initWheel(context, wvLot[2], firstDecimalDigits, 5, 0);
		
		btnCommit = popup.findViewById(R.id.btnPopCommit);
		btnClose = popup.findViewById(R.id.btnClose);
	}
	
	//03-25 17:54:10.136: INFO/LiquidationHistoryRequestProcessor(602): logcat read: Invalid argument

	public void showLikeQuickAction(){
		popup.showLikeQuickAction();
	}
	public void dismiss(){
		popup.dismiss();
	}
	
	public String getValue(){
		return Utility.formatLot(getDecimalValue());
	}

	public BigDecimal getDecimalValue() {
		BigDecimal firstDecimalDigit = BigDecimal.valueOf(wvLot[2].getCurrentItem()).multiply(power(BigDecimal.valueOf(10), wvLotExponent2));
		BigDecimal firstDigit = BigDecimal.valueOf(wvLot[1].getCurrentItem()).multiply(power(BigDecimal.valueOf(10), wvLotExponent1));
		BigDecimal secondDigit = BigDecimal.valueOf(wvLot[0].getCurrentItem()).multiply(power(BigDecimal.valueOf(10), wvLotExponent0));
		return firstDecimalDigit.add(firstDigit).add(secondDigit).min(maxLot);
	}

	public void setValue(String value) {
		try {
			setValue(new BigDecimal(value));
		} catch (Exception ex) {

		}
	}

	public void setValue(BigDecimal value) {
		BigDecimal v = value.compareTo(maxLot) > 0 ? maxLot : value;
		BigInteger firstDecimalDigit = v.divide(power(BigDecimal.valueOf(10), wvLotExponent2), RoundingMode.HALF_DOWN).toBigInteger();
		if (firstDecimalDigits.size() <= 10) {
			firstDecimalDigit = firstDecimalDigit.mod(BigInteger.TEN);
		}
		wvLot[2].setCurrentItem(firstDecimalDigit.intValue());
		BigInteger firstDigit = v.divide(power(BigDecimal.valueOf(10), wvLotExponent1), RoundingMode.HALF_DOWN).toBigInteger();
		if (firstDigits.size() <= 10) {
			firstDigit = firstDigit.mod(BigInteger.TEN);
		}
		wvLot[1].setCurrentItem(firstDigit.intValue());
		BigInteger secondDigit = v.divide(power(BigDecimal.valueOf(10), wvLotExponent0), RoundingMode.HALF_DOWN).toBigInteger().mod(BigInteger.TEN);
		if (secondDigits.size() <= 10) {
			secondDigit = secondDigit.mod(BigInteger.TEN);
		}
		wvLot[0].setCurrentItem(secondDigit.intValue());
	}

	private static BigDecimal power(BigDecimal base, int exponent) {
		if (exponent < 0) {
			return BigDecimal.ONE.divide(base).pow(-exponent);
		} else {
			return base.pow(exponent);
		}
	}
}
