package com.mfinance.everjoy.app.content;

import java.util.Comparator;
import java.util.HashMap;

import android.content.Context;


import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.util.Utility;
import com.mfinance.everjoy.hungkee.xml.Newscontent;

public class NewsContentListAdapter extends BaseListAdapter<Integer, Newscontent>{
	
	int[][] mapping = {
			{R.id.lb11}, //0
			{R.id.lb12}, //1
	};	

	public NewsContentListAdapter(Context context,
			HashMap<Integer, Newscontent> hmRecord) {
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
		return R.layout.r_t4;
	}

	@Override
	public String getValue(Newscontent obj, int iIndex) {
		switch(iIndex){
			case 0:
				if (Utility.isSimplifiedChineses()) 
					return Utility.convertHtmlSpecChar(obj.getTitleGB());
					else if (Utility.isTraditionalChinese()) 
						return Utility.convertHtmlSpecChar(obj.getTitleBig5());
						else
							return Utility.convertHtmlSpecChar(obj.getTitleEN());
				
			case 1:
				return  obj.getDate();
				
		}
		return null;
	}


}
