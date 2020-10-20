package com.mfinance.everjoy.app.util;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.view.ViewGroup;

import com.mfinance.everjoy.app.CompanySettings;
import com.mfinance.everjoy.R;


public class ColorController {
	public static void setNumberColor(Resources res, Boolean bPositive, TextView tv){
		if (bPositive==null)
		{
			if( CompanySettings.IsUseCustomPriceColor == true )
				tv.setTextColor(CompanySettings.CustomPriceColor);
			else
				tv.setTextColor(res.getColor(R.color.no_chanage));
		}
		else {
			if(bPositive){
				tv.setTextColor(res.getColor(R.color.positive));
			}else{
				tv.setTextColor(res.getColor(R.color.negative));
			}		
		}
	}
	
	public static void setPriceColor(Resources res, int bUpDown, TextView tv){
		if(bUpDown == 2){
			tv.setTextColor(res.getColor(R.color.up));
		}else if(bUpDown == 1){
			tv.setTextColor(res.getColor(R.color.down));
		}else{
			if( CompanySettings.IsUseCustomPriceColor == true )
				tv.setTextColor(CompanySettings.CustomPriceColor);
			else
				tv.setTextColor(res.getColor(R.color.no_chanage));
		}
	}
	
	public static void setPriceColor(Resources res, int bUpDown, TextView tv, int iNoChangeColor){
		if(bUpDown == 2){
			tv.setTextColor(res.getColor(R.color.up));
		}else if(bUpDown == 1){
			tv.setTextColor(res.getColor(R.color.down));
		}else{
			tv.setTextColor(res.getColor(iNoChangeColor));
		}
	}
	
	public static void setPriceColor(Resources res, int bUpDown, View v){
		if(v instanceof TextView)
			setPriceColor(res, bUpDown, (TextView)v);
	}
	
	public static void setPriceColor(Resources res, int bUpDown, TextView tv, ColorStateList csl){
		if(bUpDown == 2){
			tv.setTextColor(res.getColor(R.color.up));
		}else if(bUpDown == 1){
			tv.setTextColor(res.getColor(R.color.down));
		}else{
			tv.setTextColor(csl.getDefaultColor());
		}
	}
	
	public static void setVisible(int bCondition, ToggleButton tb){
		if(bCondition == 2){
			tb.setChecked(true);
			tb.setVisibility(View.VISIBLE);
		}else if(bCondition == 1){
			tb.setChecked(false);
			tb.setVisibility(View.VISIBLE);
		}else{
			tb.setVisibility(View.INVISIBLE);
		}
	}
	
	public static void setPriceColor(Resources res, int bUpDown, Button btn){
		if(bUpDown == 2){
			btn.setTextColor(res.getColor(R.color.up));			
		}else if(bUpDown == 1){
			btn.setTextColor(res.getColor(R.color.down));
		}else{
			if( CompanySettings.IsUseCustomPriceColor == true )
				btn.setTextColor(CompanySettings.CustomPriceColor);
			else
				btn.setTextColor(res.getColor(R.color.no_chanage));
		}
	}
	
	
	public static void setPriceColor(Resources res, int bUpDown, ViewGroup view){
		int color = -1;
		
		if(bUpDown == 2){
			color = res.getColor(R.color.pd_up);			
		}else if(bUpDown == 1){
			color = res.getColor(R.color.pd_down);
		}else{
			color = res.getColor(R.color.no_chanage);
		}
		
		for(int i = 0; i < view.getChildCount(); i ++){
			View v = view.getChildAt(i);
			
			if(v instanceof Button){
				((Button)v).setTextColor(res.getColor(R.color.no_chanage));
			}else if(v instanceof ViewGroup){
				setPriceColor(res,bUpDown, (ViewGroup)v);
			}else if(v instanceof TextView){
				((TextView)v).setTextColor(color);			
			}
		}	
	}	
	
	public static void updateBackground(Resources res, int bUpDown, View view, int iNormal, int iUp, int iDown){
		int iBackground = -1;
		
		if(bUpDown == 2){
			iBackground = iUp;			
		}else if(bUpDown == 1){
			iBackground = iDown;
		}else{
			iBackground = iNormal;
		}
		
		view.setBackgroundResource(iBackground);
	}
	
	
	public static void updateRate(TextView tv1, TextView tv2, String sValue){	      
	      int iLength = 2;
	      if(sValue.length() >= iLength ){
	    	  StringBuilder sb = new StringBuilder();
	    	  
		      for(int i = 0; i < iLength ; i++){
		    	  char c = sValue.charAt(sValue.length() - 1 - i);
		    	  
		    	  if('.' == c){
		    		  iLength ++;
		    	  }
		    	  
		    	  sb.append(c);
		      }
		      	      
		      StringBuilder sb2 = new StringBuilder();
		      for(int i = 0; i < (sValue.length() - iLength); i++){
		    	  char c = sValue.charAt(i);
		    	  sb2.append(c);
		      }
		      tv1.setText(sb2.toString());		      
		      tv2.setText(sb.reverse().toString());
		      
		      sb2 = null;
		      sb = null;
	      }
	}	
}
