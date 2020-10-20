package net.mfinance.chatlib.login;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;

/**
 * 环信登录和退出的帮助类
 */
public class EMLogin {

    /**
     * 所有登录人的密码都是123456
     */
    private static final String PWD = "123456";

    /**
     * 退出异常时的次数
     */
    private static int logoutCount = 0;

    /**
     * 环信登出
     */
    public static void logoutEmSdk(Activity activity, OnLoginOutCallBack onLoginOutCallBack) {
        logoutEm(activity, true, onLoginOutCallBack);
    }

    /**
     * 环信登出
     * 如果集成了GCM等第三方推送，方法里第一个参数需要设为true，这样退出的时候会解绑设备token，否则可能会出现退出了，还能收到消息的现象。
     * 有时候可能会碰到网络问题而解绑失败，
     * 可以弹出提示框让用户选择，是否继续退出(弹出框提示继续退出能收到消息的风险)，
     * 如果用户选择继续退出，传false再调用logout方法退出成功；
     * <p>
     * unbindToken          是否解绑，是否强制登出
     *
     * @param onLoginOutCallBack 登出回调，sdk所有回调都在子线程中
     */
    private static void logoutEm(Activity activity, boolean unbindToken, OnLoginOutCallBack onLoginOutCallBack) {
        EMClient.getInstance().logout(unbindToken, new EMCallBack() {
            @Override
            public void onSuccess() {
                Log.e("chat", "em_logout:onSuccess");
                if (activity == null) {
                    return;
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        logoutCount = 0;
                        if (onLoginOutCallBack != null) {
                            onLoginOutCallBack.onComplete(0, "");
                        }
                    }
                });
            }

            @Override
            public void onError(int code, String error) {
                Log.e("chat", "em_logout:onError:code=" + code + ";error=" + error);
                if (activity == null) {
                    return;
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 华为、小米等设备可能会解绑失败，非强制退出，再退出一次
                        if (code == EMError.USER_UNBIND_DEVICETOKEN_FAILED) {
                            if (logoutCount < 3) {
                                logoutEm(activity, false, onLoginOutCallBack);
                                logoutCount++;
                            } else {
                                logoutCount = 0;
                                if (onLoginOutCallBack != null) {
                                    onLoginOutCallBack.onComplete(-1, error);
                                }
                            }
                        } else {
                            // 正常退出失败
                            logoutCount = 0;
                            if (onLoginOutCallBack != null) {
                                onLoginOutCallBack.onComplete(-1, error);
                            }
                        }
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }
        });
    }

    /**
     * 先登录后台接口，返回用户数据，获取环信用户名，再登录环信
     *
     * @param onLoginCallBack 登录回调，sdk所有回调都在子线程中
     */
    public static void loginEm(Activity activity, String imUsername, OnLoginCallBack onLoginCallBack) {
        if (TextUtils.isEmpty(imUsername)) {
            if (onLoginCallBack != null) {
                onLoginCallBack.onError(EMError.USER_NOT_LOGIN, "imUsername is empty");
            }
            return;
        }
        // 登录环信服务器连接监听
        EMClient.getInstance().login(imUsername, PWD, new EMCallBack() {
            @Override
            public void onSuccess() {
                Log.e("chat", "em_login:onSuccess");
                if (activity == null) {
                    return;
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 登录成功之后，加载会话
                        EMClient.getInstance().groupManager().loadAllGroups();
                        EMClient.getInstance().chatManager().loadAllConversations();
                        if (onLoginCallBack != null) {
                            onLoginCallBack.onSuccess();
                        }
                    }
                });
            }

            @Override
            public void onError(int code, String error) {
                // 200:用户已经登录过:EMError.USER_ALREADY_LOGIN
                Log.e("chat", "em_login:onError:code=" + code + ";error=" + error);
                if (activity == null) {
                    return;
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 登录成功之后，加载会话
                        if (code == EMError.USER_ALREADY_LOGIN) {
                            EMClient.getInstance().groupManager().loadAllGroups();
                            EMClient.getInstance().chatManager().loadAllConversations();
                            if (onLoginCallBack != null) {
                                onLoginCallBack.onSuccess();
                            }
                        } else {
                            if (onLoginCallBack != null) {
                                onLoginCallBack.onError(code, error);
                            }
                        }
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }
        });
    }

    /**
     * 登录回调
     */
    public interface OnLoginCallBack {
        void onSuccess();

        void onError(int code, String error);
    }

    /**
     * 登出回调
     */
    public interface OnLoginOutCallBack {
        void onComplete(int code, String msg);
    }
}
