package net.mfinance.chatlib.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import net.mfinance.chatlib.R;
import net.mfinance.chatlib.impl.OnListItemChildClickListener;
import net.mfinance.chatlib.impl.OnListItemChildLongClickListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * listview点击事件
 */
public abstract class BaseChatAdapter<D> extends BaseAdapter {

    /**
     * 数据集
     */
    final List<D> mDataSet = new ArrayList<>();

    OnListItemChildClickListener onListViewItemChildClickListener;
    OnListItemChildLongClickListener onListViewItemChildLongClickListener;

    BaseChatAdapter(List<D> datas) {
        mDataSet.addAll(datas);
    }

    public void setOnListViewItemChildClickListener(OnListItemChildClickListener onListViewItemChildClickListener) {
        this.onListViewItemChildClickListener = onListViewItemChildClickListener;
    }

    public void setOnListViewItemChildLongClickListener(OnListItemChildLongClickListener onListViewItemChildLongClickListener) {
        this.onListViewItemChildLongClickListener = onListViewItemChildLongClickListener;
    }

    public void addItem(D item) {
        mDataSet.add(item);
        notifyDataSetChanged();
    }

    public void addItems(List<D> items) {
        mDataSet.addAll(items);
        notifyDataSetChanged();
    }

    /**
     * 得到所有数据
     *
     * @return mDataSet
     */
    public List<D> getItems() {
        return mDataSet;
    }

    public void setItems(List<D> items) {
        mDataSet.clear();
        mDataSet.addAll(items);
        notifyDataSetChanged();
    }

    public void addItemToHead(D item) {
        mDataSet.add(0, item);
        notifyDataSetChanged();
    }

    public void addItemsToHead(List<D> items) {
        mDataSet.addAll(0, items);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        mDataSet.remove(position);
        notifyDataSetChanged();
    }

    /**
     * 删除所有数据
     */
    public void clearAllItem() {
        mDataSet.clear();
        notifyDataSetChanged();
    }

    public void remove(D item) {
        mDataSet.remove(item);
        notifyDataSetChanged();
    }

    /**
     * @param lists 需要删除的集合
     */
    public void removeAll(Collection<D> lists) {
        mDataSet.removeAll(lists);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDataSet.size();
    }

    @Override
    public D getItem(int position) {
        return mDataSet.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * RequestOptions 头像显示模式，黑边框、圆形、缓存、默认头像
     */
    private RequestOptions getUserImageRequestOptions() {
        return RequestOptions.circleCropTransform()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.def_userimg)
                .error(R.mipmap.def_userimg);
    }

    public void setUserImage(Context context, String urlImg, ImageView imageView) {
        try {
            Glide.with(context)
                    .load(urlImg)
                    .apply(getUserImageRequestOptions())
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setImage(Context context, String urlImg, ImageView imageView) {
        try {
            RequestOptions requestOptions = new RequestOptions()
                    .override(dp2px(120f))
                    .diskCacheStrategy(DiskCacheStrategy.ALL);
            Glide.with(context)
                    .load(urlImg)
                    .apply(requestOptions)
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int dp2px(float dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
