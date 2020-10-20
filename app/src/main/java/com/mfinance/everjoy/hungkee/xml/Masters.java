package com.mfinance.everjoy.hungkee.xml;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public class Masters {
	
//	Comparator SORTING = new Comparator() {
//		@Override
//		public int compare(Object object1, Object object2) {
//			Master m1 = (Master)object1;
//			Master m2 = (Master)object2;
//			Calendar cal1 = Calendar.getInstance();  
//			Calendar cal2 = Calendar.getInstance();  
//			int compareInt = 0;
//			compareInt = m1.getEmail().compareTo(m2.getEmail());
//			/*
//			if (compareInt == 0){
//				compareInt = m1.getKey().compareTo(m2.getKey());
//			}
//			*/
//			return compareInt; 
//		}
//	};	
	
	@ElementList(type=Master.class, inline=true)
	public List<Master> masters;

	public List<Master> getMasterList(){
		return masters;
	}
	
	public HashMap<Integer, Master> getMasterMap(List<Master> list){
		List<Master> lMasters = list;
		
		if(lMasters == null){
			return null;
		}else{
			HashMap<Integer, Master> hmMaster = new HashMap<Integer, Master>();
			int count=0;
			for(Master m : lMasters){
				hmMaster.put(count++, m);
			}
			return hmMaster;			
		}
		
	}
	
	public HashMap<Integer, Master> getMasterMap(){
		return  getMasterMap(getMasterList());
	}	
	
	public ArrayList<ArrayList<Master>> getGroupedAlMaster(){
		List<Master> lMasters = getMasterList();
		if (lMasters == null)
			return null;

		LinkedHashMap<String, ArrayList<Master>> hmMaster = new LinkedHashMap<String, ArrayList<Master>>();
		for (Master m : lMasters) {
			if (!hmMaster.containsKey(m.getEmail())) {
				hmMaster.put(m.getEmail(), new ArrayList<Master>());
			}
			hmMaster.get(m.getEmail()).add(m);
		}

		ArrayList<ArrayList<Master>> alList = new ArrayList<ArrayList<Master>>();
		for(ArrayList<Master> m:hmMaster.values()){
			alList.add(m);
		}
		return alList;

//		Collections.sort(lMasters,SORTING);
//		
//		if(lMasters == null){
//			return null;
//		}else{
//			
//			String preEmail = null;
//	    	ArrayList<Master> tempList = null;
//			
//			ArrayList<ArrayList<Master>> alList = new ArrayList<ArrayList<Master>>();
//			
//			for (Master m: lMasters){
//				if ( preEmail == null || (!m.getEmail().equals(preEmail))) {
//					if (preEmail != null && (!m.getEmail().equals(preEmail))){
//						alList.add(tempList);
//					}
//					preEmail = m.getEmail(); 
//					tempList = new ArrayList<Master>();
//				}
//				tempList.add(m);
//			}
//			
//			//finally
//			alList.add(tempList);
//			/*
//			System.out.println("Top size["+alList.size()+"]");
//			for (ArrayList<Master> alm : alList){
//				System.out.println("size["+alm.size()+"]["+alm.get(0).getEmail()+"]");
//				for (Master m : alm){
//					System.out.println("m:Key"+m.getKey());
//				}
//			}
//			*/
//			return alList;
//
//		}
	}
}
