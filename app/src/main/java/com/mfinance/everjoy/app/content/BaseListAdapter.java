package com.mfinance.everjoy.app.content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.mfinance.everjoy.app.util.ArrowController;
import com.mfinance.everjoy.app.util.Utility;
import com.mfinance.everjoy.app.util.WordController;

public abstract class BaseListAdapter<K, T> extends BaseAdapter {
	/**
	 * Context View
	 */
	protected Context context = null;
	/**
	 * This class is used to instantiate layout XML file into its corresponding View objects
	 */
	protected LayoutInflater m_inflater;

	protected HashMap<K, T> hmRecord = null;
	protected ArrayList<K> alRecord = new ArrayList<K>();
	int iSortIndex = -1;

	public BaseListAdapter(Context context, HashMap<K, T> hmRecord) {
		this.context = context;
		m_inflater = LayoutInflater.from(context);
		assignRecord(hmRecord);
	}

	
	public void assignRecord(HashMap<K, T> hmRecord){
		synchronized(alRecord){
			try{
				this.hmRecord = hmRecord;	
				alRecord.clear();
				alRecord.addAll(hmRecord.keySet());			
				Collections.sort(alRecord, getComparator());				
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public void sort(int iSortIndex) {
		this.iSortIndex = iSortIndex;
	}

	@Override
	public int getCount() {
		return alRecord.size();
	}

	@Override
	public T getItem(int position) {
		return hmRecord.get(alRecord.get(position));
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		synchronized (alRecord) {
			if (alRecord.size() > 0) {
				if (convertView == null)
					convertView = m_inflater.inflate(getRowLayout(), null);

				int[][] mapping = getColumnMapping();

				T obj = hmRecord.get(alRecord.get(position));

				convertView.setTag(alRecord.get(position));
				int iIndex = 0;

				for (int i = 0; i < mapping.length; i++) {
					for (int j = 0; j < mapping[i].length; j++) {
						try{						
							Object objValue = getValue(obj, iIndex);
							if (objValue instanceof String) {
								String sTmp = (String) objValue;
								int iView = mapping[i][j];
								if (iView != -1) {
									TextView view = (TextView) convertView
											.findViewById(iView);
									view.setText(sTmp);
								}
							}

							if (objValue instanceof Drawable) {
								Drawable dTmp = (Drawable) objValue;
								int iView = mapping[i][j];
								if (iView != -1 && dTmp != null) {
									ImageView view = (ImageView) convertView
											.findViewById(iView);
									view.setImageDrawable(dTmp);
								}
							}

							if (objValue instanceof ArrowController) {
								ArrowController arrow = (ArrowController) objValue;
								int iView = mapping[i][j];
								if (iView != -1) {
									ToggleButton view = (ToggleButton) convertView
											.findViewById(iView);

									if (arrow.iUpDown == 1) {
										view.setVisibility(View.VISIBLE);
										view.setChecked(true);
									} else if (arrow.iUpDown == 0) {
										view.setVisibility(View.INVISIBLE);
									} else {
										view.setVisibility(View.VISIBLE);
										view.setChecked(false);
									}

								}
							}

							if (objValue instanceof WordController) {
								WordController word = (WordController) objValue;
								int iView = mapping[i][j];
								if (iView != -1) {
									TextView view = (TextView) convertView
											.findViewById(iView);
									view.setText(word.getValue());
									view.setTextColor(context.getResources()
											.getColor(word.getiColor()));
								}
							}

							if (objValue instanceof Boolean) {
								int iView = mapping[i][j];
								if (iView != -1)
									convertView.findViewById(iView).setVisibility(
											(Boolean) objValue ? View.VISIBLE
													: View.INVISIBLE);
							}

							if (objValue instanceof Integer) {
								Integer dTmp = (Integer) objValue;
								int iView = mapping[i][j];
								if (iView != -1 && dTmp != null) {
									ImageView view = (ImageView) convertView
											.findViewById(iView);
									view.setImageBitmap(Utility.decodeImage(
											context.getResources(), dTmp, 100));
									// view.setImageResource(dTmp);
								}
							}
							
							leafHandle(mapping[i][j],  convertView
									.findViewById(mapping[i][j]), objValue, obj);
						}catch(Exception e){
							e.printStackTrace();
							//System.out.println("tranfer object to view error in BaseListAdapter");
						}
	

						iIndex++;
					}
				}

			} else if (convertView == null) {
				convertView = m_inflater.inflate(getRowLayout(), null);
			}
		}
		return convertView;
	}

	public void leafHandle(int iView, View view, Object value, Object viewObj){}

	public abstract int[][] getColumnMapping();
	public abstract Object getValue(T obj, int iIndex);
	public abstract Comparator<K> getComparator();
	public abstract int getRowLayout();

	
}
