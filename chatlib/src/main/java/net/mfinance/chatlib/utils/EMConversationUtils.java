package net.mfinance.chatlib.utils;

import android.util.Log;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import net.mfinance.chatlib.constants.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class EMConversationUtils {

    /**
     * 获取所有的会话列表
     * <p>
     * 必须有登录才获取环信消息会话列表
     */
    public static List<EMConversation> getLocalAllConversation() {
        List<EMConversation> list = new ArrayList<>();

        // 获取当前所有的会话
        Map<String, EMConversation> allConversations = EMClient.getInstance().chatManager().getAllConversations();
        Log.e("chat", "聊天会话:allConversations.size()=" + allConversations.size());
        if (allConversations.size() == 0) {
            return list;
        }
        Set<String> stringSet = allConversations.keySet();
        for (String next : stringSet) {
            EMConversation emConversation = allConversations.get(next);
            // 去掉秘书提醒和聊天室，只加载单聊和群聊
            if (emConversation != null && !next.equals(Constants.MI_SHU_TI_XING) && !next.equals(Constants.CAR_DOG_ADMIN)
                    && emConversation.getType() != EMConversation.EMConversationType.ChatRoom) {
                list.add(emConversation);
            }
            // TODO 打印聊天信息
            if (emConversation != null) {
                List<EMMessage> allMessages = emConversation.getAllMessages();
                for (EMMessage message : allMessages) {
                    Log.e("chat", "msg = " + message.toString());
                }
            }
        }

        // 大于等于2个以上，排序，减少排序时间
        int size = list.size();
        Log.e("chat", "size = " + size);
        if (size <= 1) {
            return list;
        }

        // 排序，按照会话层的最新消息排序
        try {
            //  根据会话的最后消息时间对会话列表排序
            Collections.sort(list, new Comparator<EMConversation>() {
                @Override
                public int compare(EMConversation con1, EMConversation con2) {
                    // 如果是null，排在最后，一般来说，一定有值
                    EMMessage lastMessage1 = con1.getLastMessage();
                    EMMessage lastMessage2 = con2.getLastMessage();
                    if (lastMessage1 == null && lastMessage2 == null) {
                        Log.e("chat", "获取会话日期比较：lastMessage1 == null && lastMessage2 == null");
                        return 0;
                    }
                    if (lastMessage1 == null) {
                        return 1;
                    }
                    if (lastMessage2 == null) {
                        return -1;
                    }
                    // date1 = 2020-03-30 00:00:00;date2 = 2020-03-25 20:05:34;
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    String date1 = simpleDateFormat.format(new Date(lastMessage1.getMsgTime()));
                    String date2 = simpleDateFormat.format(new Date(lastMessage2.getMsgTime()));
                    // 返回正值是代表左侧日期大于参数日期，时间从最新的时间排列
                    int i = date2.compareTo(date1);
                    Log.e("chat", "获取会话日期比较：date1 = " + date1 + ";date2 = " + date2 + ";i = " + i);
                    return i;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}
