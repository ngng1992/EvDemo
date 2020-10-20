package com.mfinance.everjoy.app.content;

import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.AbsListView.OnScrollListener;

public class ImageViewOnScrollListener implements OnScrollListener {
	
	ImageView ivUp,ivDown;
	
	public ImageViewOnScrollListener(){}
	
	public ImageViewOnScrollListener(ImageView ivUp,ImageView ivDown){
		this.ivUp= ivUp;
		this.ivDown= ivDown;
	}
	
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		ivUp.setVisibility(firstVisibleItem==0?View.INVISIBLE:View.VISIBLE);
		ivDown.setVisibility(visibleItemCount==totalItemCount - firstVisibleItem ?View.INVISIBLE:View.VISIBLE);
	}



	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// 
		
	}

}
