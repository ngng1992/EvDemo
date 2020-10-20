package com.mfinance.everjoy.app;

import android.content.res.Resources;
import android.view.Gravity;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

import com.mfinance.everjoy.R;

public class IPhoneCheckbox {
	ToggleButton tb;
	Resources res;
	public IPhoneCheckbox(ToggleButton tb, Resources res, int iOnID, int iOffID){
		this.res = res;
		this.tb = tb;

		tb.setTextOn(res.getText(iOnID));
		tb.setTextOff(res.getText(iOffID));
		tb.setBackgroundResource(R.drawable.tb_checkbox_btn);
        tb.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				setChecked(false, isChecked);
			}

        });
	}

	public IPhoneCheckbox(ToggleButton tb, Resources res){
		this.res = res;
		this.tb = tb;
		
		tb.setBackgroundResource(R.drawable.tb_checkbox_btn);
        tb.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {				
				setChecked(false, isChecked);
			}
        	
        });
	}
	
	public void setChecked(boolean bUpdateWidget, boolean isChecked){		
		if(isChecked){
			tb.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
			tb.setTextColor(res.getColor(R.color.tb_on));
		}else{
			tb.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
			tb.setTextColor(res.getColor(R.color.tb_off));
		}		
		if(bUpdateWidget){
			 tb.setChecked(isChecked);
		}
	}
	
	public boolean isChecked(){
		return tb.isChecked();
	}

	public void setVisiblity(int invisible) {
		tb.setVisibility(invisible);
	}
}
