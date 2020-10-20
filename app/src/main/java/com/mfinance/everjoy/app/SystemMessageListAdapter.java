package com.mfinance.everjoy.app;

import android.content.Context;
import android.os.Messenger;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.bo.SystemMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class SystemMessageListAdapter extends BaseAdapter {
    private final String TAG = this.getClass().getSimpleName();

    Map<Integer, SystemMessage> hmSysMsg = null;
    ArrayList<Integer> alSysMsg = new ArrayList<Integer>();
    Context context;
    LayoutInflater m_inflater;

    protected Messenger mService = null;
    protected Messenger mServiceMessengerHandler = null;

    public SystemMessageListAdapter(Context context, Map<Integer, SystemMessage> hmSysMsg) {
        this.context = context;
        this.hmSysMsg = hmSysMsg;
        m_inflater = LayoutInflater.from(context);

        reload(hmSysMsg, null, null);
    }

    @Override
    public int getCount() {
        if (CompanySettings.systemMessageViewCount < 0)
            return hmSysMsg.size();
        else {
            if (hmSysMsg.size() < CompanySettings.systemMessageViewCount)
                return hmSysMsg.size();
            else
                return CompanySettings.systemMessageViewCount;
        }
    }

    @Override
    public Object getItem(int position) {
        return hmSysMsg.get(alSysMsg.get(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        synchronized (alSysMsg) {
            try {
                if (alSysMsg.size() > 0) {
                    if (convertView == null)
                        convertView = m_inflater.inflate(R.layout.list_system_message, null);

                    SystemMessage msg = hmSysMsg.get(alSysMsg.get(position));

                    TextView tvTmp = null;
                    tvTmp = (TextView) convertView.findViewById(R.id.tvMsgTime);
                    tvTmp.setText(msg.dateCreate.toLocaleString());

                    tvTmp = (TextView) convertView.findViewById(R.id.tvMsgContent);
                    if (msg.sMsg == null) {
                        tvTmp.setText(String.valueOf(msg.iCode));
                    } else {
                        tvTmp.setText(String.valueOf(msg.sMsg));
                    }

                    convertView.setTag(msg);
						
						/*
						convertView.setOnClickListener(new OnClickListener(){

							@Override
							public void onClick(View v) {
								int iCode = ((SystemMessage)v.getTag()).iCode;
								switch(iCode){
									case 607:
									case 608:
									case 609:
									case 610:
										CommonFunction.moveTo(mService, mServiceMessengerHandler, ServiceFunction.SRV_RUNNING_ORDER, null);
									break;
								case 704:
										CommonFunction.moveTo(mService, mServiceMessengerHandler, ServiceFunction.SRV_OPEN_POSITION, null);
									break;
								case 4001:
										CommonFunction.moveTo(mService, mServiceMessengerHandler, ServiceFunction.SRV_ANDROID_MARKET, null);									
									break;
								}
							}
							
						});
						 */
                    return convertView;
                } else {
                    return convertView = m_inflater.inflate(R.layout.list_item_transaction, null);
                }
            } catch (Exception e) {
                return convertView = m_inflater.inflate(R.layout.list_item_transaction, null);
            }
        }
    }

    public void reload(Map<Integer, SystemMessage> hmSysMsg, Messenger mService, Messenger mServiceMessengerHandler) {
        this.mService = mService;
        this.mServiceMessengerHandler = mServiceMessengerHandler;

        synchronized (alSysMsg) {
            this.hmSysMsg = new HashMap<>(hmSysMsg);
            alSysMsg.clear();
            alSysMsg.addAll(this.hmSysMsg.keySet());
        }

        Collections.sort(alSysMsg, new Comparator<Integer>() {

            @Override
            public int compare(Integer i1, Integer i2) {
                if (i1 > i2) {
                    return -1;
                } else if (i2 > i1) {
                    return 1;
                } else
                    return 0;
            }

        });
    }


}
