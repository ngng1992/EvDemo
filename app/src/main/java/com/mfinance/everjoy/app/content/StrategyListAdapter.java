package com.mfinance.everjoy.app.content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.util.Utility;
import com.mfinance.everjoy.hungkee.xml.Strategy;

public class StrategyListAdapter extends SuccessBaseExpandableListAdapter<Integer,Strategy>{

	public StrategyListAdapter(Context context, HashMap<Integer, Strategy> hmRecord) {
		super(context, hmRecord);
		
	}

	@Override
	protected void reloadGroup(HashMap<Integer, Strategy> hmRecord,ArrayList<Group> alGroup) {
		try {
		HashMap<Integer, Strategy> hmClone = (HashMap<Integer, Strategy>)hmRecord.clone();
		Iterator<Integer> itKey = hmClone.keySet().iterator();	
		HashMap<String, Group> hmTmpGroup = new HashMap<String, Group>();
		while(itKey.hasNext()){
			Integer iKey = itKey.next();
			Strategy plan = hmClone.get(iKey);
			
			String sGroup = plan.type_zh;
			
			if(hmTmpGroup.containsKey(sGroup)){
				hmTmpGroup.get(sGroup).alRecord.add(iKey);
			}else{				
				Group group = new Group();
				group.sTTile = sGroup;
				group.sETitle = plan.type_en;
				group.sSTitle = plan.type_cn;
				group.alRecord.add(iKey);
				alGroup.add(group);
				hmTmpGroup.put(sGroup, group);
			}
		}
		hmTmpGroup.clear();
		hmTmpGroup = null;
		hmClone.clear();
		hmClone = null;
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		
		synchronized(alGroup){
			if(alGroup.size() > 0){
				if (convertView == null) {
					convertView = m_inflater.inflate(R.layout.r_t6, null);
				}
				
				Strategy strategy = (Strategy)this.getChild(groupPosition, childPosition);
				
				if (Utility.isSimplifiedChineses()) 
					((TextView)convertView.findViewById(R.id.lb11)).setText(
							(strategy.gold_cn == null ? "" : (strategy.gold_cn + " ")) +strategy.caption_cn
					);
				else if (Utility.isTraditionalChinese())
					((TextView)convertView.findViewById(R.id.lb11)).setText(
							(strategy.gold_zh == null ? "" : (strategy.gold_zh + " ") )+strategy.caption_zh
					);
				else
					((TextView)convertView.findViewById(R.id.lb11)).setText(
							(strategy.gold_en == null ? "" : (strategy.gold_en + " ") )+strategy.caption_en
					);
			}
		}
		
		return convertView;
	}
	
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = m_inflater.inflate(R.layout.h_t6, null);
		}
		if (Utility.isTraditionalChinese())
			((TextView)convertView.findViewById(R.id.lbH11)).setText(((Group)getGroup(groupPosition)).sTTile);
		else if (Utility.isSimplifiedChineses())
			((TextView)convertView.findViewById(R.id.lbH11)).setText(((Group)getGroup(groupPosition)).sSTitle);
		else
			((TextView)convertView.findViewById(R.id.lbH11)).setText(((Group)getGroup(groupPosition)).sETitle);
		
		convertView.setClickable(false);
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

}

