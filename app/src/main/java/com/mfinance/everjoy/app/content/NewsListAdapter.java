package com.mfinance.everjoy.app.content;

import java.util.Comparator;
import java.util.HashMap;

import android.content.Context;


import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.util.Utility;
import com.mfinance.everjoy.hungkee.xml.News;

public class NewsListAdapter extends BaseListAdapter<Integer, News>{
	
	int[][] mapping = {
			{R.id.lb11, R.id.lb12 }
	};	

	public NewsListAdapter(Context context,
			HashMap<Integer, News> hmRecord) {
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
		//No hightlight
		//return R.layout.r_t2;
		return R.layout.r_t11;
	}

	@Override
	public String getValue(News obj, int iIndex) {
		String sResult = "";	
		if (Utility.isSimplifiedChineses()) {
			switch(iIndex){
				case 0:
					sResult = Utility.convertHtmlSpecChar(obj.getTitleGB());
					break;
				case 1:
					sResult = obj.getDatetime();
					break;
			}
		} else if (Utility.isTraditionalChinese()){
			switch(iIndex){
				case 0:
					sResult = Utility.convertHtmlSpecChar(obj.getTitleBig5());
					break;
				case 1:
					sResult = obj.getDatetime();
					break;
			}
		} else {
			switch(iIndex){
				case 0:
					sResult =  Utility.convertHtmlSpecChar(obj.getTitleEN());
					break;
				case 1:
					sResult = obj.getDatetime();
					break;
			}
		}
		return sResult;
	}


}
