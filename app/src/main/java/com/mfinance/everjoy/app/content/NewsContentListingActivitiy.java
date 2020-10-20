package com.mfinance.everjoy.app.content;

import java.util.HashMap;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;


import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.hungkee.xml.Newscontent;

public class NewsContentListingActivitiy extends ListingBaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Log.e("NewsContentListingActivitiy",""+app.data.getHmNewscontents().size());

		ListView list = (ListView) findViewById(getListViewId());
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Bundle bundle = new Bundle();
				bundle.putInt(ServiceFunction.SEND_SELECTED_KEY,
						Integer.valueOf(arg1.getTag().toString()));
				goTo(ServiceFunction.SRV_MOVE_TO_NEWS_CONTENT_DETAIL, bundle);
			}
		});

		((ListView) findViewById(R.id.lvContent))
				.setOnScrollListener(new ImageViewOnScrollListener(
						(ImageView) findViewById(R.id.ivArrowUp),
						(ImageView) findViewById(R.id.ivArrowDown)));
	}

	@Override
	public BaseListAdapter<?, ?> getAdapter() {
		return new NewsContentListAdapter(this, app.data.getHmNewscontents());
	}

	/*
	 * @Override public BaseListAdapter<?, ?> getAdapter() { return new
	 * NewsContentListAdapter(this, new HashMap<Integer, Newscontent>() ); }
	 */

	@Override
	public int getHeaderId() {
		return -1;
	}

	@Override
	public int getHeaderTemplateId() {
		return -1;
	}

	@Override
	public int getListTemplateId() {
		return R.layout.l_t3;
	}

	@Override
	public int getListViewId() {
		return R.id.lvContent;
	}

	@Override
	public void bindEvent() {
	}

	@Override
	public void updateUI() {
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public int[][] functionTextMapping() {
		return null;
	}

	@Override
	public HashMap<Integer, OnClickListener> getFunctionClickListener() {
		return null;
	}

	@Override
	public int[] getTitleText() {
		return new int[] { R.id.tvTitle, R.string.db_news_content };
	}

	@Override
	public int[][] headerTextMapping() {
		return null;
	}

	@Override
	public int getServiceId() {
		return ServiceFunction.SRV_MOVE_TO_NEWS_CONTENT_LIST;
	}

	@Override
	public int getActivityServiceCode() {
		return ServiceFunction.SRV_MOVE_TO_NEWS_CONTENT_LIST;
	}

	@Override
	public boolean isLoadedData() {
		return (app.data.getNewscontents() != null
				&& app.data.getNewscontents().getNewsContentList() != null);
	}

	@Override
	public int getLoadingViewId() {
		return R.id.flLoading;
	}

	@Override
	public void loadUIData() {
		if (adp == null) {
			adp = getAdapter();
			lv.setAdapter(adp);
		} else {
			((NewsContentListAdapter) adp).assignRecord(app.data
					.getHmNewscontents());
			adp.notifyDataSetChanged();
		}
	}

	@Override
	public void handleByChild(Message msg) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean isBottonBarExist() {
		return true;
	}

	@Override
	public boolean isTopBarExist() {
		return true;
	}

	@Override
	public boolean showLogout() {
		return true;
	}

	@Override
	public boolean showTopNav() {
		return true;
	}

	@Override
	public boolean showConnected() {
		return true;
	}

	@Override
	public boolean showPlatformType() {
		return true;
	}
}
