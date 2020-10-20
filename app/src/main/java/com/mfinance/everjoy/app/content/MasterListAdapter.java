package com.mfinance.everjoy.app.content;

import java.util.Comparator;
import java.util.HashMap;

import android.content.Context;


import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.util.Utility;
import com.mfinance.everjoy.hungkee.xml.Master;

public class MasterListAdapter extends BaseListAdapter<Integer, Master>{
	
	int[][] mapping = {
			{R.id.lb11} //0
	};	

	public MasterListAdapter(Context context,
			HashMap<Integer, Master> hmRecord) {
		super(context, hmRecord);
	}

	@Override
	public int[][] getColumnMapping() {
		return mapping;
	}

	@Override
	public Comparator<Integer> getComparator() {
		/*
		Comparator<String> sorting = new Comparator() {
			
			@Override
			public int compare(Object object1, Object object2) {
				
				String s1 = ((String)object1).substring(0,10);
				String s2 = ((String)object2).substring(0,10);
				return s1.compareTo(s2) * -1;
			}
		};
		return sorting;
		*/
		return null;
	}

	@Override
	public int getRowLayout() {
		return R.layout.r_t10;
	}

	@Override
	public String getValue(Master obj, int iIndex) {
		String sResult = "";	
		if (Utility.isSimplifiedChineses()) {
			switch(iIndex){
				case 0:
					sResult = obj.getDate() + "  " + obj.getTitleGB();
					break;
			}
		} else if (Utility.isTraditionalChinese()) {
			switch(iIndex){
				case 0:
					sResult = obj.getDate() + "  " + obj.getTitleBig5();
					break;
			}
		} else {
			switch(iIndex){
				case 0:
					sResult = obj.getDate() + "  " + obj.getTitleEN();
					break;
			}
		}
		return sResult;
	}


}
