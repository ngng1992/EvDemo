package com.mfinance.everjoy.app.util;

import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

public class ListViewAdapterReloader {
    private final AdapterView<ListAdapter> listView;
    private final BaseAdapter adapter;
    int lastSize = 0;
    public ListViewAdapterReloader(AdapterView<ListAdapter> listView, BaseAdapter adapter) {
        this.listView = listView;
        this.adapter = adapter;
    }

    public void reload() {
        int size = adapter.getCount();
        if (lastSize != size) {
            adapter.notifyDataSetChanged();
        } else {
            int start = listView.getFirstVisiblePosition();
            int end = listView.getLastVisiblePosition();
            for (int i = start; i <= end; i++) {

                View view;
                if (adapter.hasStableIds()) {
                    view = listView.getChildAt(i - start);
                } else {
                    view = listView.getChildAt(i);
                }
                if (view != null) {
                    adapter.getView(i, view, listView);
                } else {
                    adapter.notifyDataSetChanged();
                    break;
                }
            }

        }
        lastSize = size;
    }
}
