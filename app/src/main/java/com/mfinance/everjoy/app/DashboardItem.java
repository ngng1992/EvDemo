package com.mfinance.everjoy.app;

public class DashboardItem{
	public int iBackgroud;
	public int iDesc;
	public int iService;
	public int iNavIcon;
	public int iNavDesc;
	public String iActivityName;
	
	DashboardItem(int iBackgroud, int iDesc, int iService, int iNavIcon, int iNavDesc, String iActivityName){
		this.iBackgroud = iBackgroud;
		this.iDesc = iDesc;
		this.iService = iService;
		this.iNavIcon = iNavIcon;
		this.iNavDesc = iNavDesc;
		this.iActivityName = iActivityName;
	}
}