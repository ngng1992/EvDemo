package com.mfinance.everjoy.everjoy.base;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.hyphenate.EMChatRoomChangeListener;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMGroupChangeListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMucSharedFile;
import com.mfinance.everjoy.everjoy.dialog.EMOtherAccountLoginDialog;
import com.mfinance.everjoy.everjoy.dialog.impl.OnClickDialogOrFragmentViewListener;

import net.mfinance.chatlib.login.EMLogin;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 环信聊天activity，子类去实现具体业务
 */
public abstract class HuanxinChatActivity extends AppCompatActivity {

    /**
     * 每个页面都会检测是否连接环信
     */
    private EMConnectionListener emConnectionListener = new EMConnectionListener() {

        @Override
        public void onConnected() {
            Log.e("chat", "连接成功：onConnected------");
        }

        @Override
        public void onDisconnected(int errorCode) {
            // 206账户在另外一台设备登录
            Log.e("chat", "连接失败：onDisconnected：errorCode=" + errorCode);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // 避免出现 is your activity running?
                    if (isFinishing()) {
                        return;
                    }
                    if (errorCode == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                        EMLogin.logoutEmSdk(HuanxinChatActivity.this, new EMLogin.OnLoginOutCallBack() {
                            @Override
                            public void onComplete(int code, String msg) {
                                // 显示被踢下线
                                if (isFinishing()) {
                                    return;
                                }
                                showAccountOtherDeviceLogin();
                                // 清除用户信息


                                EMOtherAccountLoginDialog emOtherLoginPointDialog = new EMOtherAccountLoginDialog(HuanxinChatActivity.this);
                                emOtherLoginPointDialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
                                    @Override
                                    public void onClickView(View view, Object object) {
//                                        Intent intent = new Intent(HuanxinChatActivity.this, LoginActivity.class);
//                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                        startActivity(intent);
//                                        finish();
                                    }
                                });
                                emOtherLoginPointDialog.show();
                            }
                        });
                    }
                }
            });
        }
    };

    private EMMessageListener msgListener;
    private EMGroupChangeListener emGroupChangeListener;
    private EMChatRoomChangeListener emChatRoomChangeListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EMClient.getInstance().addConnectionListener(emConnectionListener);

        boolean showMsgListener = isShowMsgListener();
        if (showMsgListener) {
            initMessageListener();
            EMClient.getInstance().chatManager().addMessageListener(msgListener);
        }

        boolean showGroupChangeListener = isShowGroupChangeListener();
        if (showGroupChangeListener) {
            initGroupChangeListener();
            EMClient.getInstance().groupManager().addGroupChangeListener(emGroupChangeListener);
        }

        boolean showChatRoomChangeListener = isShowChatRoomChangeListener();
        if (showChatRoomChangeListener) {
            initChatRoomChangeListener();
            EMClient.getInstance().chatroomManager().addChatRoomChangeListener(emChatRoomChangeListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().removeConnectionListener(emConnectionListener);

        if (msgListener != null) {
            EMClient.getInstance().chatManager().removeMessageListener(msgListener);
        }
        if (emGroupChangeListener != null) {
            EMClient.getInstance().groupManager().removeGroupChangeListener(emGroupChangeListener);
        }
        if (emChatRoomChangeListener != null) {
            EMClient.getInstance().chatroomManager().removeChatRoomListener(emChatRoomChangeListener);
        }
    }

    /**
     * 消息监听
     *
     * @return 默认都需要消息监听
     */
    protected boolean isShowMsgListener() {
        return true;
    }

    /**
     * 群组改变监听
     *
     * @return 子类需要时，返回true
     */
    protected boolean isShowGroupChangeListener() {
        return false;
    }

    /**
     * 群组改变监听
     *
     * @return 子类需要时，返回true
     */
    protected boolean isShowChatRoomChangeListener() {
        return false;
    }

    /**
     * 消息接收监听
     */
    private void initMessageListener() {
        msgListener = new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                // 接收消息
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showMessageReceived(messages);
                    }
                });
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                // 接收透传消息
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showCmdMessageReceived(messages);
                    }
                });
            }

            @Override
            public void onMessageRead(List<EMMessage> messages) {
                //收到已读回执
                Log.e("chat", "onMessageRead------------");
            }

            @Override
            public void onMessageDelivered(List<EMMessage> messages) {
                //收到已送达回执
                Log.e("chat", "onMessageDelivered------------");
            }

            @Override
            public void onMessageRecalled(List<EMMessage> messages) {
                //消息被撤回
                Log.e("chat", "onMessageRecalled------------");
            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {
                //消息状态变动
                Log.e("chat", "onMessageChanged------------");
            }
        };
    }

    /**
     * 环信群组管理接口
     */
    private void initGroupChangeListener() {
        emGroupChangeListener = new EMGroupChangeListener() {
            @Override
            public void onInvitationReceived(String groupId, String groupName, String inviter, String reason) {

            }

            @Override
            public void onRequestToJoinReceived(String groupId, String groupName, String applicant, String reason) {

            }

            @Override
            public void onRequestToJoinAccepted(String groupId, String groupName, String accepter) {

            }

            @Override
            public void onRequestToJoinDeclined(String groupId, String groupName, String decliner, String reason) {

            }

            @Override
            public void onInvitationAccepted(String groupId, String invitee, String reason) {

            }

            @Override
            public void onInvitationDeclined(String groupId, String invitee, String reason) {

            }

            @Override
            public void onUserRemoved(String groupId, String groupName) {
                // TODO 当前登录用户被管理员移除出群组，自己退出群
                Log.e("chat", "onUserRemoved：groupId = " + groupId + ";groupName = " + groupName);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onShowUserRemoved();
                    }
                });
            }

            @Override
            public void onGroupDestroyed(String groupId, String groupName) {
                // 群组被解散。
                Log.e("chat", "onGroupDestroyed：groupId = " + groupId + ";groupName = " + groupName);
            }

            @Override
            public void onAutoAcceptInvitationFromGroup(String groupId, String inviter, String inviteMessage) {

            }

            @Override
            public void onMuteListAdded(String groupId, List<String> mutes, long muteExpire) {
                //成员禁言的通知
                for (String mu : mutes) {
                    Log.e("chat", "onMuteListAdded：groupId = " + groupId + ";mutes = " + mu + ";muteExpire = " + muteExpire);
                }
            }

            @Override
            public void onMuteListRemoved(String groupId, List<String> mutes) {
                //成员从禁言列表里移除通知
                for (String mu : mutes) {
                    Log.e("chat", "onMuteListRemoved：groupId = " + groupId + ";mutes = " + mu);
                }
            }

            @Override
            public void onWhiteListAdded(String groupId, List<String> whitelist) {

            }

            @Override
            public void onWhiteListRemoved(String groupId, List<String> whitelist) {

            }

            @Override
            public void onAllMemberMuteStateChanged(String groupId, boolean isMuted) {

            }

            @Override
            public void onAdminAdded(String groupId, String administrator) {

            }

            @Override
            public void onAdminRemoved(String groupId, String administrator) {
                // 管理员移除的通知
                Log.e("chat", "onAdminRemoved：groupId = " + groupId + ";administrator = " + administrator);
            }

            @Override
            public void onOwnerChanged(String groupId, String newOwner, String oldOwner) {
                Log.e("chat", "onMemberJoined：groupId = " + groupId + ";newOwner = " + newOwner + ";oldOwner = " + oldOwner);
            }

            @Override
            public void onMemberJoined(String groupId, String member) {
                Log.e("chat", "onMemberJoined：groupId = " + groupId + ";member = " + member);
            }

            @Override
            public void onMemberExited(String groupId, String member) {
                // 群成员退出通知
                Log.e("chat", "onMemberExited：groupId = " + groupId + ";member = " + member);
            }

            @Override
            public void onAnnouncementChanged(String groupId, String announcement) {

            }

            @Override
            public void onSharedFileAdded(String groupId, EMMucSharedFile sharedFile) {

            }

            @Override
            public void onSharedFileDeleted(String groupId, String fileId) {

            }
        };
    }

    /**
     * 加入和退出聊天室的监听接口
     */
    private void initChatRoomChangeListener() {
        emChatRoomChangeListener = new EMChatRoomChangeListener() {
            @Override
            public void onChatRoomDestroyed(String roomId, String roomName) {

            }

            @Override
            public void onMemberJoined(String roomId, String participant) {
                Log.e("chat", "加入聊天室：roomId=" + roomId + ";participant=" + participant);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showMemberJoined(roomId, participant);
                    }
                });
            }

            @Override
            public void onMemberExited(String roomId, String roomName, String participant) {
                Log.e("chat", "退出聊天室：roomId=" + roomId + ";roomName=" + roomName + ";participant=" + participant);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showMemberExited(roomId, roomName, participant);
                    }
                });
            }

            @Override
            public void onRemovedFromChatRoom(int reason, String roomId, String roomName, String participant) {

            }

            @Override
            public void onMuteListAdded(String chatRoomId, List<String> mutes, long expireTime) {

            }

            @Override
            public void onMuteListRemoved(String chatRoomId, List<String> mutes) {

            }

            @Override
            public void onWhiteListAdded(String chatRoomId, List<String> whitelist) {

            }

            @Override
            public void onWhiteListRemoved(String chatRoomId, List<String> whitelist) {

            }

            @Override
            public void onAllMemberMuteStateChanged(String chatRoomId, boolean isMuted) {

            }

            @Override
            public void onAdminAdded(String chatRoomId, String admin) {

            }

            @Override
            public void onAdminRemoved(String chatRoomId, String admin) {

            }

            @Override
            public void onOwnerChanged(String chatRoomId, String newOwner, String oldOwner) {

            }

            @Override
            public void onAnnouncementChanged(String chatRoomId, String announcement) {

            }
        };
    }

    /**
     * 接收消息的处理
     *
     * @param messages 消息集合
     */
    protected void showMessageReceived(List<EMMessage> messages) {
        try {
            // 打印测试
            for (EMMessage emm : messages) {
                Log.e("car", "接收到消息：" + emm.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 接收透传消息的处理，实际用于删除某条消息
     *
     * @param messages 消息集合
     */
    protected void showCmdMessageReceived(List<EMMessage> messages) {
        try {
            // 打印测试
            for (EMMessage emm : messages) {
                Log.e("car", "接收到CMD消息：" + emm.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 账号在另一个设备上登录，被踢下线，子类实现，在finish之前做一些操作
     */
    protected void showAccountOtherDeviceLogin() {

    }

    /**
     * 自己退出群聊或被群组删除
     */
    protected void onShowUserRemoved() {

    }

    /**
     * 加入聊天室
     *
     * @param roomId      聊天室id
     * @param participant 用户id
     */
    protected void showMemberJoined(String roomId, String participant) {

    }

    /**
     * 退出聊天室
     *
     * @param roomId      聊天室id
     * @param roomName    聊天室名称
     * @param participant 用户id
     */
    protected void showMemberExited(String roomId, String roomName, String participant) {

    }

    public abstract void updateUI();
}
