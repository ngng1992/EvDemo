package net.mfinance.chatlib.utils;

import android.app.Service;
import android.content.Context;
import android.content.res.Resources;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import net.mfinance.chatlib.R;
import net.mfinance.chatlib.constants.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Set;

/**
 * 工具类
 */
public class ChatLibUtils {

    /**
     * 根据消息内容和类型获取消息的内容提示
     * <p>
     * 参考聊天列表
     */
    public static String getMessageDigest(Context context, EMMessage lastMessage) {
        String digest = "";
        switch (lastMessage.getType()) {
            // 位置消息
            case LOCATION:
                if (lastMessage.direct() == EMMessage.Direct.RECEIVE) {
                    String chatName = lastMessage.getStringAttribute(Constants.CHAT_SEND_USER_NAME, "");
                    digest = String.format(context.getString(R.string.loaction_brackets), chatName);
                } else {
                    digest = context.getString(R.string.mine_location_brackets);
                }
                break;
            // 图片消息
            case IMAGE:
                digest = context.getString(R.string.pic_brackets);
                break;
            // 语音消息
            case VOICE:
                digest = context.getString(R.string.voice_brackets);
                break;
            // 视频消息
            case VIDEO:
                digest = context.getString(R.string.video_brackets);
                break;
            // 文本消息
            case TXT:
                String carInfoJson = lastMessage.getStringAttribute(Constants.CAR_INFO_JSON, "");
                String content = context.getString(R.string.chat_car_detail);
                if (!TextUtils.isEmpty(carInfoJson)) {
                    try {
                        JSONObject jsonObject = new JSONObject(carInfoJson);
//                    String carInfoId = jsonObject.optString("carInfoId");
                        String brandName = jsonObject.optString("brandName");
                        String vehicleName = jsonObject.optString("vehicleName");
                        String carModel = jsonObject.optString("carModel");
                        int guidancePrice = jsonObject.optInt("guidePrice");
                        int intentPrice = jsonObject.optInt("intentPrice");

                        String guidePrice = NumberPriceUtil.toThou(guidancePrice);
                        String gprice = String.format(context.getString(R.string.chat_guid_price), guidePrice);

                        String iPrice = NumberPriceUtil.toThou(intentPrice);
                        String contentPrice = String.format(context.getString(R.string.chat_intent_price), iPrice);

                        content = brandName + "  " + vehicleName + "  " + carModel + "  " + gprice + "  " + contentPrice;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return content;
                }

                if (lastMessage.getBooleanAttribute(Constants.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
                    EMTextMessageBody txtBody = (EMTextMessageBody) lastMessage.getBody();
                    digest = context.getString(R.string.voice_call_brackets) + txtBody.getMessage();
                } else {
                    EMTextMessageBody txtBody = (EMTextMessageBody) lastMessage.getBody();
                    digest = txtBody.getMessage();
                }
                break;
            // 普通文件消息
            case FILE:
                digest = context.getString(R.string.file_brackets);
                break;
            default:
                break;
        }

        return digest;
    }

    /**
     * 播放系统默认提示音，与通知的声音一样
     */
    public static Ringtone playNotificationRingtone(Context context) {
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone rt = RingtoneManager.getRingtone(context, uri);
        rt.play();
        return rt;
    }

    /**
     * 跟随系统设置的铃音
     */
    public static Ringtone playRingtone(Context context) {
        Uri ringUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        Ringtone ringtone = RingtoneManager.getRingtone(context, ringUri);
        ringtone.play();
        return ringtone;
    }

    public static void stopRingtone(Ringtone ringtone) {
        try {
            if (ringtone != null) {
                ringtone.stop();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 手机震动一下
     */
    public static void playVibrate(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        if (vibrator != null) {
            boolean hasVibrator = vibrator.hasVibrator();
            if (hasVibrator) {
                vibrator.vibrate(500); // 振动0.5秒
                // 暂停500毫秒，振动1秒，暂停500毫秒，振动2秒
                // 第二个参数为-1则振动一遍，第二个参数为0则重复振动
//                vibrator.vibrate(new long[]{500, 1000, 500, 2000}, -1);
                // 暂停震动
//                vibrator.cancel();
            }
        }
    }

    /**
     * 秘书提醒和消息的未读消息个数
     * unreadMsgCount[0] = 秘书提醒的个数
     * unreadMsgCount[1] = 消息未读个数
     *
     * @return count数组
     */
    public static int[] getMSTXAndMsgUnreadMsgCount() {
        int[] unreadMsgCount = new int[2];
        // 获取所有会话层，没有登录没有值
        Map<String, EMConversation> allConversations = EMClient.getInstance().chatManager().getAllConversations();
        // 秘书提醒未读个数
        int mstxConversationsUnreadMsgCount = 0;
        // 消息未读个数
        int msgConversationsUnreadMsgCount = 0;

        Set<String> keySet = allConversations.keySet();
        for (String key : keySet) {
            // key值是环信的聊天id
            if (key.equals(Constants.MI_SHU_TI_XING)) {
                EMConversation emConversation = allConversations.get(Constants.MI_SHU_TI_XING);
                if (emConversation != null) {
                    mstxConversationsUnreadMsgCount += emConversation.getUnreadMsgCount();
                }
            } else {
                // 不是聊天室
                EMConversation emConversation = allConversations.get(key);
                if (emConversation != null && emConversation.getType() != EMConversation.EMConversationType.ChatRoom) {
                    msgConversationsUnreadMsgCount += emConversation.getUnreadMsgCount();
                }
            }
        }

        unreadMsgCount[0] = mstxConversationsUnreadMsgCount;
        unreadMsgCount[1] = msgConversationsUnreadMsgCount;
        Log.e("chat", "mstxConversationsUnreadMsgCount=" + mstxConversationsUnreadMsgCount +
                ";msgConversationsUnreadMsgCount=" + msgConversationsUnreadMsgCount);
        return unreadMsgCount;
    }

    /**
     * px转成dp
     */
    public static int px2dp(final float pxValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        int px2dp = (int) (pxValue / scale + 0.5f);
        Log.e("chat", "px2dp = " + px2dp);
        return px2dp;
    }

    /**
     * dp转px
     */
    public static int dp2px(final float dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        int dp2px = (int) (dpValue * scale + 0.5f);
        Log.e("chat", "dp2px = " + dp2px);
        return dp2px;
    }
}
