package net.mfinance.chatlib.utils;

import android.content.Context;
import android.widget.Toast;

import com.hyphenate.EMError;

import net.mfinance.chatlib.R;

/**
 * 消息发送时，根据code判断结果
 */
public class ChatToastUtils {

    public static void showSendMsgToast(Context context, int code) {
        int strId;
        if (code == EMError.USER_MUTED) {
            strId = R.string.user_mutes;
        } else if (code == EMError.GROUP_NOT_JOINED) {
            strId = R.string.not_join_group;
        } else if (code == EMError.FILE_TOO_LARGE) {
            strId = R.string.toast_file_too_large;
        } else if (code == EMError.FILE_UPLOAD_FAILED) {
            strId = R.string.toast_unable_parse;
        } else {
            strId = R.string.send_fail;
        }
        showToast(context, strId);
    }

    public static void showToast(Context context, int strId) {
        try {
            Toast.makeText(context, strId, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showToast(Context context, String strId) {
        try {
            Toast.makeText(context, strId, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
