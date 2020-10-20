package net.mfinance.chatlib.init;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import net.mfinance.chatlib.login.EMLogin;

import java.util.Iterator;
import java.util.List;

/**
 * 在application中初始化环信配置
 */
public class ChatInit {

    public static void init(Application application, boolean isDebug) {
        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        // 是否自动将消息附件上传到环信服务器，默认为True是使用环信服务器上传下载，如果设为 false，需要开发者自己处理附件消息的上传和下载
        options.setAutoTransferMessageAttachments(true);
        // 是否自动下载附件类消息的缩略图等，默认为 true 这里和上边这个参数相关联
        options.setAutoDownloadThumbnail(true);

        int pid = android.os.Process.myPid();
        String processAppName = getAppName(application, pid);
        // 如果APP启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回
        if (processAppName == null || !processAppName.equalsIgnoreCase(application.getPackageName())) {
            Log.e(EMClient.TAG, "enter the service process!");
            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }

        //初始化
        EMClient.getInstance().init(application, options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源,debug是true
        EMClient.getInstance().setDebugMode(isDebug);

        if (EMClient.getInstance().isLoggedInBefore()) {
            //如果用户没有手动退出环信,则初始化后强行退出一次
            EMLogin.logoutEmSdk(null, null);
        }
    }

    /**
     * 三星SM-G9200在application中初始化环信SDK失败，emaObject.getChatManager()报null，
     * 在这里在初始化一次{@link EMClient#chatManager()}
     *
     * @param application 全局上下文
     */
    public static void initBySM(Application application, boolean isDebug) {
        String model = getModel();
        if (model.equals("SM-G9200")) {
            init(application, isDebug);
        }
    }

    /**
     * 手机型号
     */
    private static String getModel() {
        String model = Build.MODEL;
        if (model != null) {
            model = model.trim().replaceAll("\\s*", "");
        } else {
            model = "";
        }
        return model;
    }

    private static String getAppName(Application application, int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) application.getSystemService(Context.ACTIVITY_SERVICE);
        if (am != null) {
            List l = am.getRunningAppProcesses();
            Iterator i = l.iterator();
            while (i.hasNext()) {
                ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
                try {
                    if (info.pid == pID) {
                        processName = info.processName;
                        return processName;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return processName;
    }
}
