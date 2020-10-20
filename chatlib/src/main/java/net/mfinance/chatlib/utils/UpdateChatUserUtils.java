package net.mfinance.chatlib.utils;

import com.hyphenate.chat.EMMessage;
import net.mfinance.chatlib.adapter.GroupMemberList;
import net.mfinance.chatlib.constants.Constants;

import java.util.ArrayList;
import java.util.List;

public class UpdateChatUserUtils {

    /**
     * 更新所有群聊或单聊里面聊过天的用户信息
     */
    public static List<EMMessage> updateEMMessageChatUserInfo(List<GroupMemberList> groupMemberLists, List<EMMessage> emMessageList) {
        if (groupMemberLists == null || groupMemberLists.size() == 0) {
            return emMessageList;
        }
        if (emMessageList == null || emMessageList.size() == 0) {
            return emMessageList;
        }

        // groupMemberLists是最新用户数据
        for (GroupMemberList groupMember : groupMemberLists) {
            String huanxinId = groupMember.getHuanxinId();
            String huanxinImg = groupMember.getHuanxinImg();
            String huanxinName = groupMember.getHuanxinName();
            for (EMMessage emMessage : emMessageList) {
                String from = emMessage.getFrom();
//                Log.e("chat", "接收：huanxinId = " + huanxinId + "; from = " + from);
                if (huanxinId.equals(from)) {
                    String sendUserImg = emMessage.getStringAttribute(Constants.CHAT_SEND_USER_IMG, "");
                    String sendUsername = emMessage.getStringAttribute(Constants.CHAT_SEND_USER_NAME, "");
//                    Log.e("chat", "接收：sendUserImg = " + sendUserImg + "; sendUsername = " + sendUsername);
                    if (!sendUsername.equals(huanxinName)) {
                        emMessage.setAttribute(Constants.CHAT_SEND_USER_NAME, huanxinName);
                    }
                    if (!sendUserImg.equals(huanxinImg)) {
                        emMessage.setAttribute(Constants.CHAT_SEND_USER_IMG, huanxinImg);
                    }
                }
            }
        }
        return emMessageList;
    }

    /**
     * 更新所有群聊或单聊里面聊过天的用户信息
     *
     * @param showMessageList     已经在显示的消息
     * @param receivedMessageList 接收到的消息，最新的用户信息
     */
    public static List<EMMessage> receivedUpdateUserInfo(List<EMMessage> showMessageList, List<EMMessage> receivedMessageList) {
        // 没有正在显示的数据，返回接收到的消息
        if (showMessageList == null || showMessageList.size() == 0) {
            return receivedMessageList;
        }

        for (EMMessage itemMessage : showMessageList) {
            String huanxinId = itemMessage.getFrom();
            String sendUserImg = itemMessage.getStringAttribute(Constants.CHAT_SEND_USER_IMG, "");
            String sendUsername = itemMessage.getStringAttribute(Constants.CHAT_SEND_USER_NAME, "");
            for (EMMessage emMessage : receivedMessageList) {
                String from = emMessage.getFrom();
                if (huanxinId.equals(from)) {
                    String sendUserImg1 = emMessage.getStringAttribute(Constants.CHAT_SEND_USER_IMG, "");
                    String sendUsername1 = emMessage.getStringAttribute(Constants.CHAT_SEND_USER_NAME, "");
                    if (!sendUsername.equals(sendUsername1)) {
                        itemMessage.setAttribute(Constants.CHAT_SEND_USER_NAME, sendUsername1);
                    }
                    if (!sendUserImg.equals(sendUserImg1)) {
                        itemMessage.setAttribute(Constants.CHAT_SEND_USER_NAME, sendUserImg1);
                    }
                }
            }
        }
        return receivedMessageList;
    }

    /**
     * 更新接口中的成员信息
     *
     * @param groupMemberLists    接口中的成员信息
     * @param receivedMessageList 接收到的成员信息
     */
    public static List<GroupMemberList> receivedUpdateGroupMemberUserInfo(List<GroupMemberList> groupMemberLists, List<EMMessage> receivedMessageList) {
        if (groupMemberLists == null || groupMemberLists.size() == 0) {
            return groupMemberLists;
        }
        if (receivedMessageList == null || receivedMessageList.size() == 0) {
            return groupMemberLists;
        }

        // groupMemberLists最新用户数据
        for (GroupMemberList groupMember : groupMemberLists) {
            String huanxinId = groupMember.getHuanxinId();
            String huanxinImg = groupMember.getHuanxinImg();
            String huanxinName = groupMember.getHuanxinName();
            for (EMMessage emMessage : receivedMessageList) {
                String from = emMessage.getFrom();
//                Log.e("chat", "接收：huanxinId = " + huanxinId + "; from = " + from);
                if (huanxinId.equals(from)) {
                    String sendUserImg = emMessage.getStringAttribute(Constants.CHAT_SEND_USER_IMG, "");
                    String sendUsername = emMessage.getStringAttribute(Constants.CHAT_SEND_USER_NAME, "");
//                    Log.e("chat", "接收：sendUserImg = " + sendUserImg + "; sendUsername = " + sendUsername);
                    if (!sendUsername.equals(huanxinName)) {
                        groupMember.setHuanxinName(sendUsername);
                    }
                    if (!sendUserImg.equals(huanxinImg)) {
                        groupMember.setHuanxinImg(sendUserImg);
                    }
                }
            }
        }
        return groupMemberLists;
    }

    /**
     * 接收透传消息时的处理，删除该成员信息，加快下次for循环的速度
     */
    public static List<GroupMemberList> cmdMessageUpdateGroupMember(List<GroupMemberList> groupMemberLists, List<EMMessage> emMessageList) {
        if (groupMemberLists == null || groupMemberLists.size() == 0) {
            return groupMemberLists;
        }
        if (emMessageList == null || emMessageList.size() == 0) {
            return groupMemberLists;
        }

        List<GroupMemberList> temList = new ArrayList<>();
        for (GroupMemberList groupMember : groupMemberLists) {
            String huanxinId = groupMember.getHuanxinId();
            for (EMMessage emMessage : emMessageList) {
                String from = emMessage.getFrom();
//                Log.e("chat", "接收cmd：huanxinId = " + huanxinId + "; from = " + from);
                if (huanxinId.equals(from)) {
                    temList.add(groupMember);
                }
            }
        }
        if (temList.size() != 0) {
            groupMemberLists.removeAll(temList);
        }
        return groupMemberLists;
    }
}
