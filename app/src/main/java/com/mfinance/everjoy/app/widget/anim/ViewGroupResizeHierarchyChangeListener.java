package com.mfinance.everjoy.app.widget.anim;

import android.view.View;
import android.view.ViewGroup;


public class ViewGroupResizeHierarchyChangeListener extends ViewGroupResizeAnimation implements ViewGroup.OnHierarchyChangeListener
{
    public ViewGroupResizeHierarchyChangeListener(View parent, int duration)
    {
        super(parent, duration);
    }

    public void onChildViewAdded(View parent, View child)
    {
        final int addingHeight = findViewHeight(child);
        runAnimation(getToHeight() + addingHeight);
    }

    public void onChildViewRemoved(View parent, View child)
    {
        final int addingHeight = findViewHeight(child);
        runAnimation(getToHeight() - addingHeight);
    }

    private int findViewHeight(View child)
    {
        if(child.getMeasuredHeight() == 0)
        {
            child.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        }

        return child.getMeasuredHeight();
    }
}
