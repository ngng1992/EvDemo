package com.mfinance.everjoy.app.util;

public class WordController {
	protected String value;
	protected int iColor;
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public int getiColor() {
		return iColor;
	}

	public void setiColor(int iColor) {
		this.iColor = iColor;
	}

	public WordController(){}

	public WordController(String value, int iColor) {
		super();
		this.value = value;
		this.iColor = iColor;
	}

}
