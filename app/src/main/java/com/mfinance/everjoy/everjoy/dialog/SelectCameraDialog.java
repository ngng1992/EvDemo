package com.mfinance.everjoy.everjoy.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.everjoy.dialog.base.BaseBottomDialog;

/**
 * 选择相机
 */
public class SelectCameraDialog extends BaseBottomDialog {

    public SelectCameraDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void initView() {
        TextView tv_camera = findViewById(R.id.tv_camera);
        TextView tv_photo = findViewById(R.id.tv_photo);
        tv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onClickDialogOrFragmentViewListener != null) {
                    onClickDialogOrFragmentViewListener.onClickView(tv_photo, 1);
                }
            }
        });

        tv_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onClickDialogOrFragmentViewListener != null) {
                    onClickDialogOrFragmentViewListener.onClickView(tv_photo, 2);
                }
            }
        });
    }

    @Override
    protected int setLayout() {
        return R.layout.dialog_select_camera;
    }
}
