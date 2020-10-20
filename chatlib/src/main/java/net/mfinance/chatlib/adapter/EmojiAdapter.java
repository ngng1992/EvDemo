package net.mfinance.chatlib.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import net.mfinance.chatlib.R;
import net.mfinance.chatlib.impl.OnRecyclerViewItemClickListener;

import java.util.List;

/**
 * emoji设置adapter
 */
public class EmojiAdapter extends BaseAdapter {

    private Context context;
    private List<String> list;
    private final LayoutInflater layoutInflater;

    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    public EmojiAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View currentView;
        EmojiViewHolder holder;
        if (view == null) {
            currentView = layoutInflater.inflate(R.layout.item_chat_grid_emoji, viewGroup, false);
            holder = new EmojiViewHolder();
            holder.ivEmoji = currentView.findViewById(R.id.iv_emoji);
            currentView.setTag(holder);
        } else {
            currentView = view;
            holder = (EmojiViewHolder) currentView.getTag();
        }
        String emoji = list.get(i);
        holder.ivEmoji.setImageResource(context.getResources().getIdentifier(emoji, "drawable", context.getPackageName()));
        holder.ivEmoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != onRecyclerViewItemClickListener) {
                    onRecyclerViewItemClickListener.onItemClick(holder.ivEmoji, emoji);
                }
            }
        });
        return currentView;
    }

    static class EmojiViewHolder {
        ImageView ivEmoji;
    }

}
