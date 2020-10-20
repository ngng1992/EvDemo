package net.mfinance.chatlib.view;

import android.content.ClipboardManager;
import android.content.Context;
import android.text.Editable;
import android.text.Spannable;
import android.util.AttributeSet;
import android.util.Log;

import androidx.appcompat.widget.AppCompatEditText;

import net.mfinance.chatlib.utils.EmojiUtils;


/**
 * 自定义EditText，解决emoji的问题
 */
public class EmojiEditText extends AppCompatEditText {

    private Context context;

    public EmojiEditText(Context context) {
        super(context);
        this.context = context;
    }

    public EmojiEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public EmojiEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    public boolean onTextContextMenuItem(int id) {
//        boolean consumed = super.onTextContextMenuItem(id);
        // 监听
        if (id == android.R.id.paste) {
            onTextPaste();
            return true;
        }
        return super.onTextContextMenuItem(id);
    }

    /**
     * 粘贴操作
     */
    private void onTextPaste() {
        ClipboardManager clipboardManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        //改变剪贴板中Content
        if (clipboardManager != null && clipboardManager.getText() != null) {
            // 粘贴一次后重置剪贴板的内容，避免第二次复制时携带重复数据
            clipboardManager.setText(clipboardManager.getText());
            // [):][):][):][):][):][):][):][):]，把String转成Spannable
            String emoji = clipboardManager.getText().toString();
            Spannable spannable = EmojiUtils.getEmojiText(context, emoji);
            Log.e("text", "复制的内容 = " + emoji);
            //改变文本内容，可以多次复制
            this.append(spannable);
            //光标置到文本末尾
            Editable text = getText();
            if (text != null) {
                this.setSelection(text.toString().length());
            }
        }
    }
}
