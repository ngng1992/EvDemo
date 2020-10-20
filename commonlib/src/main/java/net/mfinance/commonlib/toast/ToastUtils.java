package net.mfinance.commonlib.toast;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

public class ToastUtils {

    public static void showToast(Context context, String msg) {
        if (context == null) return;
        if (TextUtils.isEmpty(msg)) return;
        try {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showToast(Context context, int res) {
        if (context == null) return;
        if (res == 0) return;
        try {
            // 如果改变了语言，提示还是之前的语言类型，不应该获取全局的context，但是context被销毁，可能会报错
            Toast.makeText(context, res, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 有的手机toast会含有app的名称，去掉
     */
    public static void showToastNotAppName(Context context, String msg) {
        if (context == null) return;
        if (TextUtils.isEmpty(msg)) return;
        try {
            Toast toast = Toast.makeText(context, null, Toast.LENGTH_SHORT);
            toast.setText(msg);
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
