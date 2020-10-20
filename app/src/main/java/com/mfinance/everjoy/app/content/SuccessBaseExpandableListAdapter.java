package com.mfinance.everjoy.app.content;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseExpandableListAdapter;

public abstract class SuccessBaseExpandableListAdapter<K, T> extends BaseExpandableListAdapter{
	
	public class Group{
		String sETitle;
		String sTTile;
		String sSTitle;
		ArrayList<K> alRecord = new ArrayList<K>();	
	}	
	ArrayList<Group> alGroup = new ArrayList<Group>(); 
	public HashMap<K, T> hmRecord = null;
	
	/**
	 * Context View
	 */
	protected Context context = null;
	/**
	 * This class is used to instantiate layout XML file into its corresponding View objects
	 */
	protected LayoutInflater m_inflater;
	
	public SuccessBaseExpandableListAdapter(Context context,HashMap<K, T> hmRecord){
		this.hmRecord = hmRecord;
		m_inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		assignRecord(hmRecord);
	}
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return hmRecord.get(alGroup.get(groupPosition).alRecord.get(childPosition));  
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return groupPosition * 1000 + childPosition;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if(groupPosition < getGroupCount())
			return alGroup.get(groupPosition).alRecord.size();
		return 0;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return alGroup.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return alGroup.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}
	
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
	public void assignRecord(HashMap<K, T> hmRecord){		
		synchronized(alGroup){					
			this.hmRecord = hmRecord;
			alGroup.clear();
			reloadGroup(hmRecord, alGroup);
		}
	}	
	
	protected abstract void reloadGroup(HashMap<K, T> hmRecord, ArrayList<Group> alGroup);  
}
