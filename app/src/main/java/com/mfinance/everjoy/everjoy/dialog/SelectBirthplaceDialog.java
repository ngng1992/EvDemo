package com.mfinance.everjoy.everjoy.dialog;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ScreenUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.mfinance.everjoy.R;
import com.mfinance.everjoy.everjoy.dialog.base.BaseBottomDialog;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 选择出生地区
 */
public class SelectBirthplaceDialog extends BaseBottomDialog {

    private int selectIndex = 0;
    private List<String> birthplaceList;

    public SelectBirthplaceDialog(@NonNull Context context, int selectIndex, List<String> birthplaceList) {
        super(context);
        this.selectIndex = selectIndex;
        this.birthplaceList = birthplaceList;
    }

    @Override
    protected void initView() {
        // 当数据较多时，设置固定高度
        if (birthplaceList.size() > 10) {
            LinearLayout ll_content = findViewById(R.id.ll_content);
            ViewGroup.LayoutParams layoutParams = ll_content.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = (int) (ScreenUtils.getScreenHeight() * 0.4);
            ll_content.setLayoutParams(layoutParams);
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        SelectBirthplaceAdapter selectBirthplaceAdapter = new SelectBirthplaceAdapter(R.layout.item_select_sec_account, birthplaceList);
        selectBirthplaceAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                dismiss();
                if (onClickDialogOrFragmentViewListener != null) {
                    onClickDialogOrFragmentViewListener.onClickView(view, position);
                }
            }
        });
        recyclerView.setAdapter(selectBirthplaceAdapter);
    }

    @Override
    protected int setLayout() {
        return R.layout.dialog_select_birthplace;
    }


    private class SelectBirthplaceAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        private int total = 0;

        public SelectBirthplaceAdapter(int layoutResId, @Nullable List<String> data) {
            super(layoutResId, data);
            total = data.size();
        }

        @Override
        protected void convert(@NotNull BaseViewHolder baseViewHolder, String s) {
            int adapterPosition = baseViewHolder.getAdapterPosition();
            if (adapterPosition == total - 1) {
                baseViewHolder.setGone(R.id.view_line, true);
            } else {
                baseViewHolder.setGone(R.id.view_line, false);
            }
            TextView tv_select = baseViewHolder.getView(R.id.tv_select);
            tv_select.setText(s);
            tv_select.setSelected(selectIndex == adapterPosition);
        }
    }
}
