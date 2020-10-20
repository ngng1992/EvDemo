package com.mfinance.everjoy.everjoy.sp;

import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.SPUtils;

/**
 * 用户的SP
 */
public class UserSharedPUtils {

    /**
     * 服务器默认的RefreshToken时长是30天，这里取29天
     * token的有效期不固定
     * <p>
     * 29*24*60*60*1000
     */
    public static final long REFRESH_TOKEN_TIME = 250560000;

    /**
     * 全局sp对象，app的若干设置
     */
    private static SPUtils SPUTILS = null;

    /**
     * 调用保存方法时，必须先调用init
     */
    public static void init() {
        SPUTILS = SPUtils.getInstance("sp_user");
    }

    /**
     * 判断用户是否登录
     *
     * @return true登录
     */
    public static boolean isLogin() {
        String token = getToken();
        boolean empty = TextUtils.isEmpty(token);
        if (empty) {
            return false;
        } else {
            long currentTimeMillis = System.currentTimeMillis();
            long refreshTokenTime = getRefreshTokenTime();
            // 判断刷新token，当前时间比保存的时间大于29天，把token删除，去登陆
            if (currentTimeMillis - refreshTokenTime > REFRESH_TOKEN_TIME) {
                SPUTILS.remove("token");
                SPUTILS.remove("refresh_token");
                return false;
            } else {
                return true;
            }
        }
    }

    /**
     * 判断token是否过期
     *
     * @return true有效期
     */
    public static boolean isTokenValid() {
        long tokenTime = getTokenTime();
        long currentTimeMillis = System.currentTimeMillis();
        //  当前时间 - 有效期，小于等于0，未过期，此时刷新token，会提示未过期
        long time = currentTimeMillis - tokenTime;
        Log.e("http", "time = " + time);
        return time <= 0;
    }

    /**
     * 2. 刷新的token
     */
    public static String getRefreshToken() {
        return SPUTILS.getString("refresh_token", "");
    }

    public static void setRefreshToken(String refreshToken) {
        SPUTILS.put("refresh_token", refreshToken);
    }

    /**
     * 2. 刷新的token时间
     */
    public static long getRefreshTokenTime() {
        return SPUTILS.getLong("refresh_token_time", 0L);
    }

    public static void setRefreshTokenTime(long refreshTokenTime) {
        SPUTILS.put("refresh_token_time", refreshTokenTime);
    }

    /**
     * 3. 登录的token
     */
    public static String getToken() {
        return SPUTILS.getString("token", "");
    }

    public static void setToken(String token) {
        SPUTILS.put("token", token);
    }

    /**
     * token时间
     */
    public static long getTokenTime() {
        return SPUTILS.getLong("token_time", 0L);
    }

    public static void setTokenTime(long tokenTime) {
        SPUTILS.put("token_time", tokenTime);
    }


    /**
     * 当前账号是否是在当前手机上首次登录
     */
    public static boolean isLoginFirstByEmail(String email) {
        return SPUTILS.getBoolean(email + "_is_login_first", true);
    }

    public static void setIsLoginFirst(String email, boolean isLoginFirst) {
        SPUTILS.put(email + "_is_login_first", isLoginFirst);
    }
}
