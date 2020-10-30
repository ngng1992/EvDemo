package com.mfinance.everjoy.everjoy.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.everjoy.dialog.impl.OnClickDialogOrFragmentViewListener;

/**
 * 开通证券账户填写资料通用view
 */
public class AccountEditorInfoView extends RelativeLayout {

    private TextView tv_left_title;
    private EditText et_right_content;
    private ImageView iv_goto;

    private OnClickDialogOrFragmentViewListener onClickDialogOrFragmentViewListener;

    public void setOnClickDialogOrFragmentViewListener(OnClickDialogOrFragmentViewListener onClickDialogOrFragmentViewListener) {
        this.onClickDialogOrFragmentViewListener = onClickDialogOrFragmentViewListener;
    }

    public AccountEditorInfoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AccountEditorInfoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public AccountEditorInfoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AccountEditorInfoView);
        String txtLeft = a.getString(R.styleable.AccountEditorInfoView_txtLeft);
        String txtRight = a.getString(R.styleable.AccountEditorInfoView_txtRight);
        String txtRightHint = a.getString(R.styleable.AccountEditorInfoView_txtRightHint);
        boolean isVisibilityGoto = a.getBoolean(R.styleable.AccountEditorInfoView_isVisibilityGoto, false);
        boolean isEditor = a.getBoolean(R.styleable.AccountEditorInfoView_isEditor, true);
        int editorInputType = a.getInt(R.styleable.AccountEditorInfoView_editorInputType, 1);
        a.recycle();

        LayoutInflater.from(context).inflate(R.layout.view_account_editor_info, this);
        tv_left_title = findViewById(R.id.tv_left_title);
        et_right_content = findViewById(R.id.et_right_content);
        iv_goto = findViewById(R.id.iv_goto);
        RelativeLayout rl_right_content = findViewById(R.id.rl_right_content);

        tv_left_title.setText(txtLeft);
        et_right_content.setText(txtRight);
        et_right_content.setHint(txtRightHint);
        iv_goto.setVisibility(isVisibilityGoto ? VISIBLE : GONE);

        // 不可编辑但可点击
        if (!isEditor) {
            et_right_content.setFocusable(false);
            et_right_content.setFocusableInTouchMode(false);
            et_right_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickDialogOrFragmentViewListener != null) {
                        onClickDialogOrFragmentViewListener.onClickView(rl_right_content, "");
                    }
                }
            });
        } else {
            // 可换行
            if (editorInputType == 1) {
                et_right_content.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            } else if (editorInputType == 2) {
                // TODO 不起作用？
                et_right_content.setInputType(
                        InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS | InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            } else if (editorInputType == 3) {
                et_right_content.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            }
        }
    }

    public String getEditorContent() {
        return et_right_content.getText().toString();
    }

    public void setEditorContent(String content) {
        et_right_content.setText(content);
    }

    public String getLeftAndHintContentForTip() {
        return et_right_content.getHint().toString() + tv_left_title.getText().toString();
    }

    public String getHintContentForTip() {
        return et_right_content.getHint().toString();
    }
}
