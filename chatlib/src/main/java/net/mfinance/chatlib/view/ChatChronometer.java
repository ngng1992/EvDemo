package net.mfinance.chatlib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Chronometer;

/**
 * 聊天计时器
 */
public class ChatChronometer extends Chronometer{

    public ChatChronometer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ChatChronometer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChatChronometer(Context context) {
        super(context);
    }
    
    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        //continue when view is hidden
        visibility = View.VISIBLE;
        super.onWindowVisibilityChanged(visibility);
    }
}
