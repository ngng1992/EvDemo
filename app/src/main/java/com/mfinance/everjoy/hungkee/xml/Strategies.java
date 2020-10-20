package com.mfinance.everjoy.hungkee.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Strategies {

	public String schemaLocation;

	public List<Strategy> strategy;

	public List<Strategy> getStrategyList() {
		return strategy;
	}

	public void sort() {

	}

	public HashMap<Integer, Strategy> getStrategyMap(List<Strategy> lStrategys) {
		if (lStrategys == null) {
			return null;
		} else {
			HashMap<Integer, Strategy> hmlStrategy = new HashMap<Integer, Strategy>();
			int count = 0;
			for (Strategy d : lStrategys) {
				hmlStrategy.put(count++, d);
			}
			return hmlStrategy;
		}
	}

	public HashMap<String, Strategy> getStrategyMapWithKeyStr(
			List<Strategy> lStrategys) {
		if (lStrategys == null) {
			return null;
		} else {
			HashMap<String, Strategy> hmlStrategy = new HashMap<String, Strategy>();

			for (Strategy d : lStrategys) {
				hmlStrategy.put(d.getKey(), d);
			}
			return hmlStrategy;
		}
	}

	public HashMap<Integer, Strategy> getStrategyMap() {
		return getStrategyMap(getStrategyList());
	}

	public Strategy getStrategyByKey(String key) {
		for (Strategy d : strategy) {
			if (d.getKey().equals(key))
				return d;
		}
		return null;
	}

	public ArrayList<ArrayList<Strategy>> getGroupedAlStrategy() {
		List<Strategy> lStrategy = getStrategyList();
		if (lStrategy == null) {
			return null;
		} else {

			String preDate = null;
			String tempDateStr = null;
			ArrayList<Strategy> tempList = null;

			ArrayList<ArrayList<Strategy>> alList = new ArrayList<ArrayList<Strategy>>();

			for (Strategy d : lStrategy) {
				tempDateStr = d.issueDateOnly;
				if (preDate == null || (!tempDateStr.equals(preDate))) {
					if (preDate != null && (!tempDateStr.equals(preDate))) {
						alList.add(tempList);
					}
					preDate = tempDateStr;
					tempList = new ArrayList<Strategy>();
				}
				tempList.add(d);
			}
			alList.add(tempList);
			return alList;
		}
	}
}
