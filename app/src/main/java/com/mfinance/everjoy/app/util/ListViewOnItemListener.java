package com.mfinance.everjoy.app.util;

import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;

public class ListViewOnItemListener implements View.OnTouchListener, AdapterView.OnItemClickListener {
    private boolean moving = false;
    private Runnable actionCancelledFallback = null;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                moving = false;
                actionCancelledFallback = v::performClick;
                return true;
            case MotionEvent.ACTION_UP:
                v.performClick();
                moving = false;
                actionCancelledFallback = null;
                return true;
            case MotionEvent.ACTION_CANCEL:
                if (moving) {
                    moving = false;
                    actionCancelledFallback = null;
                } else {
                    actionCancelledFallback = v::performClick;
                    return true;
                }
                return false;
            case MotionEvent.ACTION_MOVE:
                moving = true;
                actionCancelledFallback = null;
            default:
                return false;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (actionCancelledFallback != null) {
            actionCancelledFallback.run();
        }
        actionCancelledFallback = null;
    }
}
