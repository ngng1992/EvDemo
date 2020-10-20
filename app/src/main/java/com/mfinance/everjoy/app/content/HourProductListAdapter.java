package com.mfinance.everjoy.app.content;

import java.util.Comparator;
import java.util.HashMap;


import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.util.Utility;
import com.mfinance.everjoy.hungkee.xml.Hourproduct;

import android.content.Context;


public class HourProductListAdapter extends BaseListAdapter<Integer, Hourproduct>{

	int[][] mapping = {
			{R.id.lb11}, //0
			{R.id.lb21} //1
	};	
	
	public HourProductListAdapter(Context context,
			HashMap<Integer, Hourproduct> hmRecord) {
		super(context, hmRecord);
	}

	@Override
	public int[][] getColumnMapping() {
		return mapping;
	}

	@Override
	public Object getValue(Hourproduct obj, int iIndex) {
		String sResult = "";	
		
		switch(iIndex){
			case 0:
				sResult = obj.getHeadline();
				break;
			case 1:
				sResult = obj.getSubTitle();
				break;
		}
		
		return sResult;
	}

	@Override
	public Comparator<Integer> getComparator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getRowLayout() {
		return R.layout.r_hour_product;
	}

}
