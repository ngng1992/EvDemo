package com.mfinance.everjoy.app.content;

import java.util.Comparator;
import java.util.HashMap;

import android.content.Context;


import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.util.Utility;
import com.mfinance.everjoy.hungkee.xml.Economicdata;

public class EconomicDataListAdapter extends BaseListAdapter<Integer, Economicdata>{
	
	int[][] mapping = {
			{R.id.lb11}, //0
			{R.id.lb21,R.id.lb22,R.id.lb23, R.id.lb24} //1,2,3,4
	};	

	public EconomicDataListAdapter(Context context,
			HashMap<Integer, Economicdata> hmRecord) {
		super(context, hmRecord);
	}

	@Override
	public int[][] getColumnMapping() {
		return mapping;
	}

	@Override
	public Comparator<Integer> getComparator() {
		// 
		return null;
	}

	@Override
	public int getRowLayout() {
		return R.layout.r_t5;
	}

	@Override
	public String getValue(Economicdata obj, int iIndex) {
		String sResult = "";	
		if (Utility.isSimplifiedChineses()) {
			switch(iIndex){
				case 0:
					sResult = obj.getCountryGB()+" "+obj.getNameGB();
					sResult = sResult.replaceAll("&amp;", "&");
					break;
				case 1:
					sResult = obj.getTime();
					break;
				case 2:
					sResult = obj.getCountryGB();
					break;
				case 3:
					sResult = "".equals(obj.getPrevValue()) ? "" : obj.getPrevValue() + obj.getUnitGB();
					break;
				case 4:
					sResult = "".equals(obj.getForecastValue()) ? "" :obj.getForecastValue() + obj.getUnitGB();
					break;
			}
		} else if (Utility.isTraditionalChinese()) {
			switch(iIndex){
				case 0:
					sResult = obj.getCountryBig5()+" "+obj.getNameBig5();
					sResult = sResult.replaceAll("&amp;", "&");
					break;
				case 1:
					sResult = obj.getTime();
					break;
				case 2:
					sResult = obj.getCountryBig5();
					break;
				case 3:
					sResult = "".equals(obj.getPrevValue()) ? "" : obj.getPrevValue() + obj.getUnitBig5();
					break;
				case 4:
					sResult = "".equals(obj.getForecastValue()) ? "" : obj.getForecastValue() + obj.getUnitBig5();
					break;
			}
		} else {
			switch(iIndex){
				case 0:
					sResult = obj.getCountryEN()+" "+obj.getNameEN();
					sResult = sResult.replaceAll("&amp;", "&");
					break;
				case 1:
					sResult = obj.getTime();
					break;
				case 2:
					sResult = obj.getCountryEN();
					break;					
				case 3:
					sResult = "".equals(obj.getPrevValue()) ? "" : obj.getPrevValue() + obj.getUnitEN();
					break;
				case 4:
					sResult = "".equals(obj.getForecastValue()) ? "" : obj.getForecastValue() + obj.getUnitEN();
					break;
			}
		}
		return sResult;
	}


}
