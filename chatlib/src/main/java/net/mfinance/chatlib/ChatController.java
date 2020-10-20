package net.mfinance.chatlib;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMLocationMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.chat.EMVideoMessageBody;
import com.hyphenate.chat.EMVoiceMessageBody;

import net.mfinance.chatlib.adapter.EmChatAdapter;
import net.mfinance.chatlib.adapter.GroupMemberList;
import net.mfinance.chatlib.constants.Constants;
import net.mfinance.chatlib.impl.OnChatControllerListener;
import net.mfinance.chatlib.impl.OnChatLayoutListener;
import net.mfinance.chatlib.impl.OnListItemChildClickListener;
import net.mfinance.chatlib.impl.OnListItemChildLongClickListener;
import net.mfinance.chatlib.impl.OnSendMessageListener;
import net.mfinance.chatlib.recorder.AudioManager;
import net.mfinance.chatlib.recorder.MediaManager;
import net.mfinance.chatlib.utils.ChatToastUtils;
import net.mfinance.chatlib.utils.ConfigUtils;
import net.mfinance.chatlib.utils.ToolsUtils;
import net.mfinance.chatlib.utils.UpdateChatUserUtils;
import net.mfinance.chatlib.view.EMChatLayout;
import net.mfinance.chatlib.view.menu.FloatMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 聊天全局控制
 */
public class ChatController {

    /**
     * 5分钟过后，不能撤销
     */
    private static final long CHAT_RECALL_TIME = 5 * 60 * 1000L;

    private EMConversation mConversation;
    private EmChatAdapter emChatAdapter;

    private AppCompatActivity activity;
    private EMChatLayout emChatLayout;
    private String chatId;
    private EMConversation.EMConversationType emConversationType;
    private String currentName;
    private Point point;

    private OnChatControllerListener onChatControllerListener;

    /**
     * 是否是回复的消息
     */
    private boolean isReply;
    /**
     * 回复名称
     */
    private String replyName;
    /**
     * 回复消息内容
     */
    private String replyContent;

    private List<GroupMemberList> groupMemberLists;

    /**
     * @param chatId      聊天id
     * @param currentName 当前登录用户的名称
     */
    public ChatController(AppCompatActivity activity, List<GroupMemberList> groupMemberLists, EMChatLayout emChatLayout, String chatId, String currentName, int... resColor) {
        this.activity = activity;
        this.groupMemberLists = groupMemberLists;
        this.emChatLayout = emChatLayout;
        this.chatId = chatId;
        this.currentName = currentName;
        if (resColor != null) {
            this.emChatLayout.setSwipeRefreshLayoutColor(resColor);
        }
        initData();
    }

    public void setOnChatControllerListener(OnChatControllerListener onChatControllerListener) {
        this.onChatControllerListener = onChatControllerListener;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public void setEmConversationType(EMConversation.EMConversationType emConversationType) {
        this.emConversationType = emConversationType;
    }

    public EMChatLayout getEmChatLayout() {
        return emChatLayout;
    }

    /**
     * 进入页面，填充数据
     */
    private void initData() {
        if (emConversationType == EMConversation.EMConversationType.ChatRoom) {
            emChatLayout.hideChoiceVideoView();
        }
        // 点击view
        emChatLayout.setOnChatLayoutListener(new OnChatLayoutListener() {
            @Override
            public void onSelectUserToActivity() {
                if (emConversationType != EMConversation.EMConversationType.Chat) {
                    if (null != onChatControllerListener) {
                        onChatControllerListener.onSelectUserToActivity();
                    }
                }
            }

            @Override
            public void onRefresh() {
                // 获取top第0个位置的数据
                int count = emChatAdapter.getCount();
                // 大于1个时，刷新
                if (count >= 1) {
                    EMMessage item = emChatAdapter.getItem(0);
                    if (item != null) {
                        List<EMMessage> pageData = mConversation.loadMoreMsgFromDB(item.getMsgId(), 10);
                        // 标记所有消息已读
                        mConversation.markAllMessagesAsRead();
                        if (pageData != null && pageData.size() != 0) {
                            pageData = UpdateChatUserUtils.updateEMMessageChatUserInfo(groupMemberLists, pageData);
                            emChatAdapter.addItemsToHead(pageData);
                        }
                    }
                }
                emChatLayout.onRefreshComplete();
            }

            @Override
            public void onFinishedRecordVoice(float seconds, String filePath) {
                if (seconds < 1f) {
                    ChatToastUtils.showToast(activity, R.string.toast_voice_time);
                    return;
                }
                if (null != onChatControllerListener) {
                    onChatControllerListener.onSendVoiceMessage(seconds, filePath);
                }
            }

            @Override
            public void onSendTextMessage(String message) {
                if (message == null || message.length() == 0) {
                    ChatToastUtils.showToast(activity, R.string.toast_no_cant_send_empty);
                    return;
                }
                // 是否是回复
                if (isReply) {
                    if (null != onChatControllerListener) {
                        onChatControllerListener.onSendReplyTxtMessage(message, replyName, replyContent);
                    }
                } else {
                    if (null != onChatControllerListener) {
                        onChatControllerListener.onSendTxtMessage(message);
                    }
                }
            }

            @Override
            public void onOpenCamera() {
                if (null != onChatControllerListener) {
                    onChatControllerListener.onOpenCamera();
                }
            }

            @Override
            public void onOpenSelectorImage() {
                if (null != onChatControllerListener) {
                    onChatControllerListener.onOpenSelectorImage();
                }
            }

            @Override
            public void onOpenSelectorVideo() {
                if (null != onChatControllerListener) {
                    onChatControllerListener.onOpenSelectorVideo();
                }
            }

            @Override
            public void onOpenLocation(double latitude, double longitude, String address, String type) {
                if (null != onChatControllerListener) {
                    onChatControllerListener.onOpenLocation(latitude, longitude, address, type);
                }
            }

            @Override
            public void onOpenVoiceCall() {
                if (null != onChatControllerListener) {
                    onChatControllerListener.onOpenVoiceCall();
                }
            }

            @Override
            public void onScrollListView() {
                scrollBottom();
            }
        });

        // 创建聊天会话
        if (mConversation == null) {
            mConversation = EMClient.getInstance().chatManager().getConversation(chatId, emConversationType, true);
        }
        List<EMMessage> pageData = getPageData();
        emChatAdapter = new EmChatAdapter(activity, pageData);
        emChatAdapter.setCurrentName(currentName);
        emChatLayout.getListView().setAdapter(emChatAdapter);
        // 点击item
        emChatAdapter.setOnListViewItemChildClickListener(new OnListItemChildClickListener() {
            @Override
            public void onItemChildClick(View view, int position) {
                EMMessage emMessage = emChatAdapter.getItem(position);
                if (emMessage == null) {
                    return;
                }
                // lib不能使用switch
                int viewId = view.getId();
                if (viewId == R.id.rl_receive_voice) {
                    // 点击接收到的声音
                    ImageView imageView = view.findViewById(R.id.iv_receive_voice);
                    imageView.setImageResource(R.drawable.bg_chat_receive_voice_paly);
                    AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getDrawable();
                    animationDrawable.start();
                    emMessage.setListened(true);
                    emChatAdapter.notifyDataSetChanged();
                    EMVoiceMessageBody receiveVoiceBody = (EMVoiceMessageBody) emMessage.getBody();
                    MediaManager.playSound(receiveVoiceBody.getRemoteUrl(), new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (activity.isFinishing()) {
                                        return;
                                    }
                                    MediaManager.release();
                                    // 完成之后，停止动画
                                    imageView.setImageResource(R.drawable.ease_chatto_voice_playing);
                                    animationDrawable.stop();
                                }
                            });
                        }
                    });
                } else if (viewId == R.id.rl_send_voice) {
                    ImageView imageView = view.findViewById(R.id.iv_send_voice);
                    imageView.setImageResource(R.drawable.bg_chat_send_voice_paly);
                    AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getDrawable();
                    animationDrawable.start();
                    emMessage.setListened(true);
                    emChatAdapter.notifyDataSetChanged();
                    EMVoiceMessageBody receiveVoiceBody = (EMVoiceMessageBody) emMessage.getBody();
                    MediaManager.playSound(receiveVoiceBody.getRemoteUrl(), new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (activity.isFinishing()) {
                                        return;
                                    }
                                    MediaManager.release();
                                    // 完成之后，停止动画
                                    imageView.setImageResource(R.drawable.ease_chatfrom_voice_playing);
                                    animationDrawable.stop();
                                }
                            });
                        }
                    });
                } else if (viewId == R.id.iv_receive_image || viewId == R.id.iv_send_image) {
                    // 没有网络时，打开图片查看器会报一串英文错误
                    boolean connected = ToolsUtils.isConnected(activity);
                    int index = 0;
                    if (connected) {
                        List<String> imgList = new ArrayList<>();
                        List<EMMessage> items = emChatAdapter.getItems();
                        int size = items.size();
                        for (int i = 0; i < size; i++) {
                            EMMessage emMessageChild = items.get(i);
                            if (emMessage == emMessageChild) {
                                index = i;
                            }
                            EMMessage.Type type = emMessageChild.getType();
                            if (type == EMMessage.Type.IMAGE) {
                                EMImageMessageBody messageBody = (EMImageMessageBody) emMessageChild.getBody();
                                String remoteUrl = messageBody.getRemoteUrl();
                                imgList.add(remoteUrl);
                            }
                        }
                        if (null != onChatControllerListener) {
                            onChatControllerListener.startImageWatcher(imgList, index);
                        }
                    } else {
                        ChatToastUtils.showToast(activity, R.string.toast_net_unusual);
                    }
                } else if (viewId == R.id.rl_receive_video) {
                    EMMessage.Status status = emMessage.status();
                    if (status == EMMessage.Status.INPROGRESS) {
                        ChatToastUtils.showToast(activity, R.string.receiving);
                        return;
                    }
                    EMVideoMessageBody videoMessageBody = (EMVideoMessageBody) emMessage.getBody();
                    String fileName = videoMessageBody.getFileName();
                    // 隐藏框架名称，外部使用picture_library选择文件库
                    if (fileName.contains("PictureSelector_")) {
                        fileName = fileName.replace("PictureSelector_", "");
                    }
                    // 优先播放远程地址
                    String videoUrl;
                    String localUrl = videoMessageBody.getLocalUrl();
                    String remoteUrl = videoMessageBody.getRemoteUrl();
                    File file = new File(localUrl);
                    if (file.isFile() && file.exists()) {
                        videoUrl = localUrl;
                    } else {
                        videoUrl = remoteUrl;
                    }
                    if (null != onChatControllerListener) {
                        onChatControllerListener.onPlayVideo(videoUrl, fileName, videoMessageBody.getThumbnailUrl());
                    }
                } else if (viewId == R.id.rl_send_video_msg) {
                    EMMessage.Status status = emMessage.status();
                    if (status == EMMessage.Status.INPROGRESS) {
                        ChatToastUtils.showToast(activity, R.string.sending);
                        return;
                    }
                    // 发送播放本地视频：播放远程视频有时候播放不了
                    EMVideoMessageBody videoMessageBody = (EMVideoMessageBody) emMessage.getBody();
                    String fileName = videoMessageBody.getFileName();
                    // 优先播放本地地址
                    String videoUrl;
                    String localUrl = videoMessageBody.getLocalUrl();
                    String remoteUrl = videoMessageBody.getRemoteUrl();
                    File file = new File(localUrl);
                    if (file.isFile() && file.exists()) {
                        videoUrl = localUrl;
                    } else {
                        videoUrl = remoteUrl;
                    }
                    if (null != onChatControllerListener) {
                        onChatControllerListener.onPlayVideo(videoUrl, fileName, videoMessageBody.getThumbnailUrl());
                    }
                } else if (viewId == R.id.iv_send_state) {
                    // 发送失败，重新发送
                    reSendMsg(emMessage);
                } else if (viewId == R.id.ll_car_info) {
                    String content = emMessage.getStringAttribute(Constants.CAR_INFO_JSON, "");
                    Log.e("chat", "content = " + content);
                    try {
                        JSONObject jsonObject = new JSONObject(content);
                        String type = jsonObject.optString("type");
                        String carInfoId = jsonObject.optString("carInfoId");
                        if (null != onChatControllerListener) {
                            onChatControllerListener.onCarInfo(type, carInfoId);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (viewId == R.id.tv_send_location || viewId == R.id.tv_receive_location) {
                    EMLocationMessageBody emLocationMessageBody = (EMLocationMessageBody) emMessage.getBody();
                    double latitude = emLocationMessageBody.getLatitude();
                    double longitude = emLocationMessageBody.getLongitude();
                    String address = emLocationMessageBody.getAddress();
                    String type = emMessage.getStringAttribute(Constants.MESSAGE_ATTR_IOS, "");
                    if (null != onChatControllerListener) {
                        onChatControllerListener.onOpenLocation(latitude, longitude, address, type);
                    }
                } else if (viewId == R.id.tv_send_voice_call) {
                    // 拨打语音通话
                    String from = emMessage.getFrom();
                    String to = emMessage.getTo();
                    String senderUsername = emMessage.getStringAttribute(Constants.CHAT_SEND_USER_NAME, "");
                    String senderImage = emMessage.getStringAttribute(Constants.CHAT_SEND_USER_IMG, "");
                    String senderHuanxinId = emMessage.getStringAttribute(Constants.CHAT_SEND_HUAN_XIN_ID, "");
                    String chatName = emMessage.getStringAttribute(Constants.CHAT_RECEIVE_USER_NAME, "");
                    String chatImage = emMessage.getStringAttribute(Constants.CHAT_RECEIVE_USER_AVTAR, "");
                    Log.e("chat", "from = " + from + ";to = " + to +
                            ";senderHuanxinId = " + senderHuanxinId + ";senderUsername = " + senderUsername + "; chatName = " + chatName);
                    if (onChatControllerListener != null) {
                        // 我发给对方：from = senderHuanxinId
                        // ios-android: from = cardog20200229110056824;to = cardog202001091604461;
                        // senderHuanxinId = cardog20200229110056824;senderUsername = ; chatName = Wang66
                        onChatControllerListener.onOpenSentVoiceCall(to, chatName, chatImage, senderHuanxinId, senderUsername, senderImage);
                    }
                } else if (viewId == R.id.tv_receive_voice_call) {
                    // 接收到的语音通话，不做处理，显示会比较乱
//                    String from = emMessage.getFrom();
//                    String to = emMessage.getTo();
//                    String senderUsername = emMessage.getStringAttribute(Constants.CHAT_SEND_USER_NAME, "");
//                    String senderImage = emMessage.getStringAttribute(Constants.CHAT_SEND_USER_IMG, "");
//                    String senderHuanxinId = emMessage.getStringAttribute(Constants.CHAT_SEND_HUAN_XIN_ID, "");
//                    String chatName = emMessage.getStringAttribute(Constants.CHAT_RECEIVE_USER_NAME, "");
//                    String chatImage = emMessage.getStringAttribute(Constants.CHAT_RECEIVE_USER_AVTAR, "");
//                    Log.e("chat", "from = " + from + ";to = " + to + ";senderHuanxinId = " + senderHuanxinId + ";senderUsername = " + senderUsername + "; chatName = " + chatName);
//                    if (onChatControllerListener != null) {
//                       // 回拨过去，我发给对方，
//                        // from = cardog20200229110056824;to = cardog202001091043233;senderHuanxinId = cardog20200229110056824;senderUsername = 天天酷跑; chatName = 亮仔33
//                        // 对调信息
//                        onChatControllerListener.onOpenReceiveVoiceCall(senderHuanxinId, senderUsername, senderImage, to, chatName, chatImage);
//                    }
                }
            }
        });
        emChatAdapter.setOnListViewItemChildLongClickListener(new OnListItemChildLongClickListener() {
            @Override
            public void onItemChildLongClick(View view, int position) {
                EMMessage emMessage = emChatAdapter.getItem(position);
                if (emMessage == null) {
                    Log.e("chat", "emMessage == null");
                    return;
                }
                // 失败或正在发送，不显示
                EMMessage.Status status = emMessage.status();
                if (status == EMMessage.Status.INPROGRESS || status == EMMessage.Status.FAIL) {
                    Log.e("chat", "status == inprogress || status == fail");
                    return;
                }

                int viewId = view.getId();
                if (viewId == R.id.iv_receive_image) {
                    showPopReceiveReplyVoiceImageDelete(emMessage, position);
                } else if (viewId == R.id.iv_send_image) {
                    showPopSendImageVideoDeleteRecall(emMessage, position);
                } else if (viewId == R.id.tv_receive_msg) {
                    showPopReceiveTxtDeleteCopyReply(emMessage, position);
                } else if (viewId == R.id.tv_send_msg) {
                    showPopSendTxtDeleteCopyRecall(emMessage, position);
                } else if (viewId == R.id.rl_receive_voice) {
                    showPopReceiveReplyVoiceImageDelete(emMessage, position);
                } else if (viewId == R.id.rl_send_voice) {
                    showPopSendImageVideoDeleteRecall(emMessage, position);
                } else if (viewId == R.id.ll_send_reply_msg) {
                    showPopSendImageVideoDeleteRecall(emMessage, position);
                } else if (viewId == R.id.ll_send_reply_receive_msg) {
                    showPopReceiveReplyVoiceImageDelete(emMessage, position);
                } else if (viewId == R.id.rl_send_video_msg) {
                    showPopSendImageVideoDeleteRecall(emMessage, position);
                } else if (viewId == R.id.rl_receive_video) {
                    showPopReceiveReplyVoiceImageDelete(emMessage, position);
                } else if (viewId == R.id.tv_send_voice_call) {
                    showPopSendImageVideoDeleteRecall(emMessage, position);
                } else if (viewId == R.id.tv_receive_voice_call) {
                    showPopReceiveReplyVoiceImageDelete(emMessage, position);
                }
            }
        });
        // 滑动到最后一个显示，计算所有item的高度
        scrollBottom();
    }

    /**
     * 获取聊天数据，聊天室没有保存数据，单聊和群聊有数据
     * startMsgId为空时，获取最新消息的20条
     * startMsgId有值时，获取记录20条
     */
    private List<EMMessage> getPageData() {
        // 传入错误的参数，会话是null
        if (mConversation == null) {
            return new ArrayList<>();
        }
        List<EMMessage> pageData = mConversation.loadMoreMsgFromDB("", 10);
        pageData = UpdateChatUserUtils.updateEMMessageChatUserInfo(groupMemberLists, pageData);
        // 标记所有消息已读
        mConversation.markAllMessagesAsRead();
        return pageData;
    }

    /**
     * 滑动到底部
     */
    private void scrollBottom() {
        // 延迟0.5秒，显示到最底部，避免遮盖，如果最后一个是图片，因为图片显示加载耗时，并不能马上显示，并上移，尽量设置大一点
        emChatLayout.getListView().postDelayed(new Runnable() {
            @Override
            public void run() {
                int count = emChatAdapter.getCount();
                if (count <= 0) {
                    return;
                }
                int itemHeight = 0;
                for (int i = 0; i < emChatAdapter.getCount(); i++) {
                    View item = emChatAdapter.getView(i, null, emChatLayout.getListView());
                    item.measure(0, 0);
                    int measuredHeight = item.getMeasuredHeight();
                    itemHeight += measuredHeight;
                }
                emChatLayout.getListView().setSelection(itemHeight);
            }
        }, 680);
    }

    /**
     * 发送文字消息：删除、复制、撤回
     */
    private void showPopSendTxtDeleteCopyRecall(EMMessage emMessage, int adapterPosition) {
        String[] item;
        long msgTime = emMessage.getMsgTime();
        // 本地时间
        long nowMills = System.currentTimeMillis();
        // 大于5分钟没有撤销按钮
        if (nowMills - msgTime > CHAT_RECALL_TIME) {
            item = new String[]{activity.getString(R.string.str_chat_copy), activity.getString(R.string.str_chat_delete)};
        } else {
            item = new String[]{activity.getString(R.string.str_chat_copy), activity.getString(R.string.str_chat_delete),
                    activity.getString(R.string.str_chat_recall)};
        }
        FloatMenu floatMenu = new FloatMenu(activity);
        floatMenu.items(item);
        floatMenu.setOnItemClickListener(new FloatMenu.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                if (position == 0) {
                    copyMsg(emMessage);
                } else if (position == 1) {
                    deleteMsg(emMessage, adapterPosition);
                } else if (position == 2) {
                    sendCMDMsg(emMessage, adapterPosition);
                }
            }
        });
        floatMenu.show(point);
    }

    /**
     * 发送图片消息、视频消息、语音消息、回复消息：删除、撤回
     */
    private void showPopSendImageVideoDeleteRecall(EMMessage emMessage, int adapterPosition) {
        String[] item;
        long msgTime = emMessage.getMsgTime();
        // 本地时间
        long nowMills = System.currentTimeMillis();
        // 大于5分钟没有撤销按钮
        if (nowMills - msgTime > CHAT_RECALL_TIME) {
            item = new String[]{activity.getString(R.string.str_chat_delete)};
        } else {
            item = new String[]{activity.getString(R.string.str_chat_delete), activity.getString(R.string.str_chat_recall)};
        }
        FloatMenu floatMenu = new FloatMenu(activity);
        floatMenu.items(item);
        floatMenu.setOnItemClickListener(new FloatMenu.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                if (position == 0) {
                    deleteMsg(emMessage, adapterPosition);
                } else if (position == 1) {
                    sendCMDMsg(emMessage, adapterPosition);
                }
            }
        });
        floatMenu.show(point);
    }

    /**
     * 接收文字消息：删除、复制、回复
     */
    private void showPopReceiveTxtDeleteCopyReply(EMMessage emMessage, int adapterPosition) {
        FloatMenu floatMenu = new FloatMenu(activity);
        String[] item;
        // 私聊无回复
        EMMessage.ChatType chatType = emMessage.getChatType();
        if (chatType == EMMessage.ChatType.Chat) {
            item = new String[]{activity.getString(R.string.str_chat_copy), activity.getString(R.string.str_chat_delete)};
        } else {
            item = new String[]{activity.getString(R.string.str_chat_copy), activity.getString(R.string.str_chat_delete),
                    activity.getString(R.string.str_chat_reply)};
        }
        floatMenu.items(item);
        floatMenu.setOnItemClickListener(new FloatMenu.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                Log.e("chat", "position=" + position);
                if (position == 0) {
                    copyMsg(emMessage);
                } else if (position == 1) {
                    deleteMsg(emMessage, adapterPosition);
                } else if (position == 2) {
                    // 发送回复消息
                    isReply = true;
                    EMTextMessageBody textMsgBody = (EMTextMessageBody) emMessage.getBody();
                    replyName = emMessage.getStringAttribute(Constants.CHAT_SEND_USER_NAME, "");
                    replyContent = textMsgBody.getMessage();
                    // 显示回复的view
                    emChatLayout.showReplyView(replyName, replyContent);
                }
            }
        });
        floatMenu.show(point);
    }

    /**
     * 接收回复消息、声音消息、图片消息、视频消息：删除
     */
    private void showPopReceiveReplyVoiceImageDelete(EMMessage emMessage, int adapterPosition) {
        String[] item = {activity.getString(R.string.str_chat_delete)};
        FloatMenu floatMenu = new FloatMenu(activity);
        floatMenu.items(item);
        floatMenu.setOnItemClickListener(new FloatMenu.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                if (position == 0) {
                    deleteMsg(emMessage, adapterPosition);
                }
            }
        });
        floatMenu.show(point);
    }


    /**
     * 删除当前会话的某个消息
     *
     * @param emMessage 消息
     */
    private void deleteMsg(EMMessage emMessage, int adapterPosition) {
        mConversation.removeMessage(emMessage.getMsgId());
        emChatAdapter.remove(adapterPosition);
    }

    /**
     * 删除当前会话消息
     */
    public void clearConversation() {
        mConversation.clearAllMessages();
        emChatAdapter.clearAllItem();
    }

    /**
     * @ 某人的结果
     */
    public JSONArray getMembersJSONArray(String username, String huanxinId) {
        return emChatLayout.getMembersJSONArray(username, huanxinId);
    }

    public ListView getListView() {
        return emChatLayout.getListView();
    }

    /**
     * 接收消息时是否滑动到底部
     * listview如果向上滑动过，接收消息时，就不要自动滑动到底部了
     */
    public void messageReceived(List<EMMessage> messages, String chatId) {
        // 去掉秘书提醒
        // chatId = cardog20200217073531275;chatName = 李看
        // msg{from:cardog202001091043233, to:cardog20200229110056824 body:txt:"w"
        // msg{from:cardog20200217073531275, to:cardog20200229110056824 body:txt:"q"
        List<EMMessage> temMsg = new ArrayList<>();
        for (EMMessage emm : messages) {
            String conversationId = emm.conversationId();
            if (conversationId.equals(Constants.MI_SHU_TI_XING)) {
                emm.setUnread(true);
            } else {
                // 当前的聊天id必须相等，不是会话id，因为不同人发送给同一个人，会话id可能是相等的
                if (emm.getChatType() == EMMessage.ChatType.Chat) {
                    if (chatId.equals(emm.getFrom())) {
                        mConversation.markMessageAsRead(emm.getMsgId());
                        temMsg.add(emm);
                    }
                } else if (emm.getChatType() == EMMessage.ChatType.GroupChat) {
                    if (mConversation != null && mConversation.conversationId().equals(conversationId)) {
                        mConversation.markMessageAsRead(emm.getMsgId());
                        temMsg.add(emm);
                    }
                }
            }
        }
        if (temMsg.size() == 0) {
            return;
        }
        if (emConversationType == EMConversation.EMConversationType.Chat) {
            groupMemberLists = UpdateChatUserUtils.receivedUpdateGroupMemberUserInfo(groupMemberLists, temMsg);
            temMsg = UpdateChatUserUtils.receivedUpdateUserInfo(emChatAdapter.getItems(), temMsg);
            // 最后一个消息
            EMMessage emMessage = messages.get(temMsg.size() - 1);
            // 单聊才会改变标题，是同一个才会改变标题
            if (chatId.equals(emMessage.getFrom())) {
                String username = emMessage.getStringAttribute(Constants.CHAT_SEND_USER_NAME, "");
                if (onChatControllerListener != null) {
                    onChatControllerListener.onShowLastUsernameBySingle(username);
                }
            }
        } else {
            temMsg = UpdateChatUserUtils.receivedUpdateUserInfo(emChatAdapter.getItems(), temMsg);
        }

        boolean lastItemVisible = isShowing(emChatAdapter.getCount() - 1);
        emChatAdapter.addItems(temMsg);
        if (lastItemVisible) {
            scrollBottom();
            for (EMMessage emm : messages) {
                emm.setUnread(false);
                mConversation.markMessageAsRead(emm.getMsgId());
                mConversation.updateMessage(emm);
            }
        }
    }

    /**
     * 判断某个item是否正在显示
     */
    public boolean isShowing(int position) {
        int showViewCount = getListView().getChildCount();
        int lastPosition = getListView().getLastVisiblePosition();
        boolean isShowing = position <= lastPosition && position > lastPosition - showViewCount;
        return isShowing;
    }

    /**
     * 最后一个消息是否全部显示
     */
    private boolean isLastItemVisible() {
        // 少于1个，表示没有数据，默认显示
        if (emChatAdapter.getCount() < 1) {
            return true;
        }
        ListView listView = emChatLayout.getListView();
        int lastItemPosition = emChatAdapter.getCount() - 1;
        int lastVisiblePosition = listView.getLastVisiblePosition();
        if (lastVisiblePosition >= lastItemPosition - 1) {
            int childIndex = lastVisiblePosition - listView.getFirstVisiblePosition();
            int childCount = listView.getChildCount();
            int index = Math.min(childIndex, childCount - 1);
            View lastVisibleChild = listView.getChildAt(index);
            if (lastVisibleChild != null) {
                return lastVisibleChild.getBottom() <= listView.getBottom();
            }
        }
        return false;
    }

    /**
     * 接收到cmd消息，实际是删除
     */
    public void cmdMessageReceived(List<EMMessage> messages) {
        List<EMMessage> temList = new ArrayList<>();
        // 本地是否有该条消息
        int count = emChatAdapter.getCount();
        for (int i = 0; i < count; i++) {
            EMMessage emMessage = emChatAdapter.getItem(i);
            for (EMMessage message : messages) {
                String cmdMsgId = (String) message.ext().get(Constants.MSG_ID);
                mConversation.removeMessage(cmdMsgId);
                String localMsgId = emMessage.getMsgId();
                if (cmdMsgId != null && cmdMsgId.equals(localMsgId)) {
                    temList.add(emMessage);
                }
            }
        }
        if (temList.size() != 0) {
            groupMemberLists = UpdateChatUserUtils.cmdMessageUpdateGroupMember(groupMemberLists, temList);
            emChatAdapter.removeAll(temList);
        }
    }

    /**
     * 复制
     */
    private void copyMsg(EMMessage emMessage) {
        EMTextMessageBody textMsgBody = (EMTextMessageBody) emMessage.getBody();
        String content = textMsgBody.getMessage();
        // 如果有emoji：[):][):][):][):][):][):][):][):][):][):][):][):][):][):][):][):][):][):][):][):][):][):][):][):][):]
        Log.e("chat", "复制的内容：content=" + content);
        // 获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", content);
        // 将ClipData内容放到系统剪贴板里。
        if (cm != null) {
            cm.setPrimaryClip(mClipData);
        }
    }

    /**
     * 撤回失败
     */
    private void recallFail() {
        ChatToastUtils.showToast(activity, R.string.toast_recall_fail);
    }

    /**
     * 发送透传消息：实际是撤回；
     * 处理方式：发送成功之后，本地删除、接收方删除该条消息
     */
    private void sendCMDMsg(EMMessage emMessage, int adapterPosition) {
        EMMessage cmdMsg = EMMessage.createSendMessage(EMMessage.Type.CMD);
        // 支持单聊和群聊，默认单聊，如果是群聊添加下面这行
        EMMessage.ChatType chatType = emMessage.getChatType();
        if (chatType == EMMessage.ChatType.GroupChat) {
            cmdMsg.setChatType(EMMessage.ChatType.GroupChat);
            // 这个属性关联cms，单聊和群聊都设置GROUP_CHAT_TYPE_GROUP
            cmdMsg.setAttribute(Constants.GROUP_CHAT_TYPE, Constants.GROUP_CHAT_TYPE_GROUP);
        } else if (chatType == EMMessage.ChatType.ChatRoom) {
            cmdMsg.setChatType(EMMessage.ChatType.ChatRoom);
            cmdMsg.setAttribute(Constants.GROUP_CHAT_TYPE, Constants.GROUP_CHAT_TYPE_ROOM);
        } else {
            cmdMsg.setChatType(EMMessage.ChatType.Chat);
            cmdMsg.setAttribute(Constants.GROUP_CHAT_TYPE, Constants.GROUP_CHAT_TYPE_GROUP);
        }
        String action = "sendCMDMsg";
        EMCmdMessageBody cmdBody = new EMCmdMessageBody(action);
        String toUsername = emMessage.conversationId();
        cmdMsg.setTo(toUsername);
        cmdMsg.addBody(cmdBody);
        cmdMsg.setAttribute(Constants.MSG_ID, emMessage.getMsgId());
        cmdMsg.setAttribute(Constants.SEND_TRANSMISSION, true);
        EMClient.getInstance().chatManager().sendMessage(cmdMsg);
        cmdMsg.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("chat", "发送透传成功：--------------");
                        deleteMsg(emMessage, adapterPosition);
                    }
                });
            }

            @Override
            public void onError(int code, String error) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("chat", "发送透传失败：code" + code + "error=" + error);
                        recallFail();
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }
        });
    }

    /**
     * 重新发送消息
     */
    private void reSendMsg(EMMessage emMessage) {
        emMessage.setStatus(EMMessage.Status.INPROGRESS);
        emChatAdapter.notifyDataSetChanged();
        emMessage.setMessageStatusCallback(new SendMessageCallBack(emMessage));
        EMClient.getInstance().chatManager().sendMessage(emMessage);
    }

    /**
     * 群组
     * <p>
     * 1. 没有视频的缩略图，不能发送，环信会发送失败
     * 2. 视频时时长不能超过5分钟
     *
     * @param receiverName      群聊的名称/单聊对方的名称
     * @param receiverImage     群聊的头像/单聊对方的头像
     * @param receiverHuanxinId 群聊环信id/单聊对方的环信id
     * @param groupId           服务器的群组id，单聊groupId = null
     */
    private void sendVideoMsg(String srcVideoPath, String videoThumbnail, int videoTime,
                              String senderUsername, String senderImage, String senderHuanxinId,
                              String groupId,
                              String receiverName, String receiverImage, String receiverHuanxinId) {
        // videoPath为视频本地路径，thumbPath为视频缩略图路径，videoLength为视频时间长度
        EMMessage videoMessage = EMMessage.createVideoSendMessage(srcVideoPath, videoThumbnail, videoTime, receiverHuanxinId);
        // 设置发送人昵称
        videoMessage.setAttribute(Constants.CHAT_SEND_USER_NAME, senderUsername);
        // 设置发送人的环信ID
        videoMessage.setAttribute(Constants.CHAT_SEND_HUAN_XIN_ID, senderHuanxinId);
        // 设置发送人的头像
        videoMessage.setAttribute(Constants.CHAT_SEND_USER_IMG, senderImage);
        if (emConversationType == EMConversation.EMConversationType.GroupChat) {
            videoMessage.setAttribute(Constants.CHAT_RECEIVE_ROOM_NAME, receiverName);
            videoMessage.setAttribute(Constants.CHAT_GROUP_AVATAR_IMG, receiverImage);
            videoMessage.setAttribute(Constants.GROUP_ID, groupId);
            // 这个属性关联cms，单聊和群聊都设置GROUP_CHAT_TYPE_GROUP
            videoMessage.setAttribute(Constants.GROUP_CHAT_TYPE, Constants.GROUP_CHAT_TYPE_GROUP);
            // 如果是群聊，设置chattype
            videoMessage.setChatType(EMMessage.ChatType.GroupChat);
        } else if (emConversationType == EMConversation.EMConversationType.ChatRoom) {
            videoMessage.setAttribute(Constants.CHAT_RECEIVE_ROOM_NAME, receiverName);
            videoMessage.setAttribute(Constants.CHAT_GROUP_AVATAR_IMG, receiverImage);
            videoMessage.setAttribute(Constants.GROUP_ID, groupId);
            // 这个属性关联cms，单聊和群聊都设置GROUP_CHAT_TYPE_GROUP
            videoMessage.setAttribute(Constants.GROUP_CHAT_TYPE, Constants.GROUP_CHAT_TYPE_ROOM);
            // 设置chattype
            videoMessage.setChatType(EMMessage.ChatType.ChatRoom);
        } else {
            // 设置接收人的昵称和头像
            videoMessage.setAttribute(Constants.CHAT_RECEIVE_USER_NAME, receiverName);
            videoMessage.setAttribute(Constants.CHAT_RECEIVE_USER_AVTAR, receiverImage);
            // 设置chattype，默认是单聊
            videoMessage.setChatType(EMMessage.ChatType.Chat);
        }
        videoMessage.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        videoMessage.setStatus(EMMessage.Status.SUCCESS);
                        emChatAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onError(int code, String error) {
                Log.e("chat", "onError：code=" + code + ";error=" + error);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        videoMessage.setStatus(EMMessage.Status.FAIL);
                        emChatAdapter.notifyDataSetChanged();
                        ChatToastUtils.showSendMsgToast(activity, code);
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {
                Log.e("chat", "onProgress：progress=" + progress + ";status=" + status);
            }
        });
        // 发送消息
        EMClient.getInstance().chatManager().sendMessage(videoMessage);

        // 刷新UI
        emChatAdapter.addItem(videoMessage);
        boolean lastItemVisible = isLastItemVisible();
        if (lastItemVisible) {
            scrollBottom();
        }
    }

    /**
     * 群组或聊天室发送图片
     */
    public void sendImageMsg(String filePath,
                             String senderUsername, String senderImage, String senderHuanxinId,
                             String groupId,
                             String receiverName, String receiverImage, String receiverHuanxinId) {
        // imagePath为图片本地路径，false为不发送原图（默认超过100k的图片会压缩后发给对方），需要发送原图传true
        EMMessage imageSendMessage = EMMessage.createImageSendMessage(filePath, false, receiverHuanxinId);
        // 设置发送人昵称
        imageSendMessage.setAttribute(Constants.CHAT_SEND_USER_NAME, senderUsername);
        // 设置发送人的环信ID
        imageSendMessage.setAttribute(Constants.CHAT_SEND_HUAN_XIN_ID, senderHuanxinId);
        // 设置发送人的头像
        imageSendMessage.setAttribute(Constants.CHAT_SEND_USER_IMG, senderImage);

        Log.e("img", "emConversationType = " + emConversationType.name());
        if (emConversationType == EMConversation.EMConversationType.GroupChat) {
            imageSendMessage.setAttribute(Constants.CHAT_RECEIVE_ROOM_NAME, receiverName);
            imageSendMessage.setAttribute(Constants.CHAT_GROUP_AVATAR_IMG, receiverImage);
            imageSendMessage.setAttribute(Constants.GROUP_ID, groupId);
            // 这个属性关联cms，单聊和群聊都设置GROUP_CHAT_TYPE_GROUP
            imageSendMessage.setAttribute(Constants.GROUP_CHAT_TYPE, Constants.GROUP_CHAT_TYPE_GROUP);
            // 如果是群聊，设置chattype
            imageSendMessage.setChatType(EMMessage.ChatType.GroupChat);
        } else if (emConversationType == EMConversation.EMConversationType.ChatRoom) {
            imageSendMessage.setAttribute(Constants.CHAT_RECEIVE_ROOM_NAME, receiverName);
            imageSendMessage.setAttribute(Constants.CHAT_GROUP_AVATAR_IMG, receiverImage);
            imageSendMessage.setAttribute(Constants.GROUP_ID, groupId);
            // 这个属性关联cms，单聊和群聊都设置GROUP_CHAT_TYPE_GROUP
            imageSendMessage.setAttribute(Constants.GROUP_CHAT_TYPE, Constants.GROUP_CHAT_TYPE_ROOM);
            // 设置chattype
            imageSendMessage.setChatType(EMMessage.ChatType.ChatRoom);
        } else {
            // 设置接收人的昵称和头像
            imageSendMessage.setAttribute(Constants.CHAT_RECEIVE_USER_NAME, receiverName);
            imageSendMessage.setAttribute(Constants.CHAT_RECEIVE_USER_AVTAR, receiverImage);
            // 设置chattype，默认是单聊
            imageSendMessage.setChatType(EMMessage.ChatType.Chat);
        }
        imageSendMessage.setMessageStatusCallback(new SendMessageCallBack(imageSendMessage));
        // 发送消息
        EMClient.getInstance().chatManager().sendMessage(imageSendMessage);
        // 刷新UI
        emChatAdapter.addItem(imageSendMessage);
        boolean lastItemVisible = isLastItemVisible();
        if (lastItemVisible) {
            scrollBottom();
        }
    }

    /**
     * 群组或聊天室，发送回复某人的消息
     * 单聊没有回复某人
     */
    public void sendReplyMsg(String msgContent,
                             String senderUsername, String senderImage, String senderHuanxinId,
                             String replyerName, String replyerContent,
                             String groupId,
                             String receiverName, String receiverImage, String receiverHuanxinId) {
        EMMessage txtSendMessage = EMMessage.createTxtSendMessage(msgContent, receiverHuanxinId);
        // 设置回复人
        txtSendMessage.setAttribute(Constants.CHAT_SEND_REPLY_NAME, replyerName);
        txtSendMessage.setAttribute(Constants.CHAT_SEND_REPLY_CONTENT, replyerContent);

        // 设置发送人
        txtSendMessage.setAttribute(Constants.CHAT_SEND_USER_NAME, senderUsername);
        // 设置发送人的环信ID
        txtSendMessage.setAttribute(Constants.CHAT_SEND_HUAN_XIN_ID, senderHuanxinId);
        // 设置发送人的头像
        txtSendMessage.setAttribute(Constants.CHAT_SEND_USER_IMG, senderImage);
        // 群聊才有群信息
        if (emConversationType == EMConversation.EMConversationType.GroupChat) {
            txtSendMessage.setAttribute(Constants.CHAT_RECEIVE_ROOM_NAME, receiverName);
            txtSendMessage.setAttribute(Constants.CHAT_GROUP_AVATAR_IMG, receiverImage);
            txtSendMessage.setAttribute(Constants.GROUP_ID, groupId);
            txtSendMessage.setAttribute(Constants.GROUP_CHAT_TYPE, Constants.GROUP_CHAT_TYPE_GROUP);
            // 设置聊天的 群聊或单聊
            txtSendMessage.setChatType(EMMessage.ChatType.GroupChat);
        } else {
            txtSendMessage.setAttribute(Constants.CHAT_RECEIVE_ROOM_NAME, receiverName);
            txtSendMessage.setAttribute(Constants.CHAT_GROUP_AVATAR_IMG, receiverImage);
            txtSendMessage.setAttribute(Constants.GROUP_ID, groupId);
            txtSendMessage.setAttribute(Constants.GROUP_CHAT_TYPE, Constants.GROUP_CHAT_TYPE_ROOM);
            txtSendMessage.setChatType(EMMessage.ChatType.ChatRoom);
        }
        txtSendMessage.setMessageStatusCallback(new SendMessageCallBack(txtSendMessage));
        EMClient.getInstance().chatManager().sendMessage(txtSendMessage);
        // 刷新UI
        emChatLayout.setEdtChatEmpty();
        emChatAdapter.addItem(txtSendMessage);
        // 发送成功或失败，下次再次发送都不是回复消息了
        isReply = false;
        boolean lastItemVisible = isLastItemVisible();
        if (lastItemVisible) {
            scrollBottom();
        }
    }

    /**
     * 单聊、群组或聊天室，发送声音
     */
    public void sendVoiceMsg(float seconds, String voicePath,
                             String senderUsername, String senderImage, String senderHuanxinId,
                             String groupId,
                             String receiverName, String receiverImage, String receiverHuanxinId) {
        // filePath为语音文件路径，length为录音时间(秒)
        EMMessage voiceMessage = EMMessage.createVoiceSendMessage(voicePath, (int) seconds, receiverHuanxinId);
        // 设置发送人昵称
        voiceMessage.setAttribute(Constants.CHAT_SEND_USER_NAME, senderUsername);
        // 设置发送人的环信ID
        voiceMessage.setAttribute(Constants.CHAT_SEND_HUAN_XIN_ID, senderHuanxinId);
        // 设置发送人的头像
        voiceMessage.setAttribute(Constants.CHAT_SEND_USER_IMG, senderImage);
        // 如果是群聊那么需要设置接收消息的群的昵称
        if (emConversationType == EMConversation.EMConversationType.GroupChat) {
            voiceMessage.setAttribute(Constants.CHAT_RECEIVE_ROOM_NAME, receiverName);
            voiceMessage.setAttribute(Constants.CHAT_GROUP_AVATAR_IMG, receiverImage);
            voiceMessage.setAttribute(Constants.GROUP_ID, groupId);
            voiceMessage.setAttribute(Constants.GROUP_CHAT_TYPE, Constants.GROUP_CHAT_TYPE_GROUP);
            voiceMessage.setChatType(EMMessage.ChatType.GroupChat);
        } else if (emConversationType == EMConversation.EMConversationType.ChatRoom) {
            voiceMessage.setAttribute(Constants.CHAT_RECEIVE_ROOM_NAME, receiverName);
            voiceMessage.setAttribute(Constants.CHAT_GROUP_AVATAR_IMG, receiverImage);
            voiceMessage.setAttribute(Constants.GROUP_ID, groupId);
            voiceMessage.setAttribute(Constants.GROUP_CHAT_TYPE, Constants.GROUP_CHAT_TYPE_ROOM);
            voiceMessage.setChatType(EMMessage.ChatType.ChatRoom);
        } else {
            // 设置接收人的昵称和头像
            voiceMessage.setAttribute(Constants.CHAT_RECEIVE_USER_NAME, receiverName);
            voiceMessage.setAttribute(Constants.CHAT_RECEIVE_USER_AVTAR, receiverImage);
            // 设置聊天的 群聊或单聊
            voiceMessage.setChatType(EMMessage.ChatType.Chat);
        }
        voiceMessage.setMessageStatusCallback(new SendMessageCallBack(voiceMessage));
        EMClient.getInstance().chatManager().sendMessage(voiceMessage);
        // 刷新UI
        emChatAdapter.addItem(voiceMessage);
        boolean lastItemVisible = isLastItemVisible();
        if (lastItemVisible) {
            scrollBottom();
        }
    }

    /**
     * 单聊、群组或聊天室，发送消息
     * 单聊没有@某人，remindUserJson = null，groupId = null
     *
     * @param remindUserJson @的联系人
     */
    public void sendTxtMsg(String msgContent,
                           String senderUsername, String senderImage, String senderHuanxinId,
                           JSONArray remindUserJson,
                           String groupId,
                           String receiverName, String receiverImage, String receiverHuanxinId, OnSendMessageListener onSendMessageListener) {
        EMMessage txtSendMessage = EMMessage.createTxtSendMessage(msgContent, receiverHuanxinId);
        // 设置发送人昵称
        txtSendMessage.setAttribute(Constants.CHAT_SEND_USER_NAME, senderUsername);
        // 设置发送人的环信ID
        txtSendMessage.setAttribute(Constants.CHAT_SEND_HUAN_XIN_ID, senderHuanxinId);
        // 设置发送人的头像
        txtSendMessage.setAttribute(Constants.CHAT_SEND_USER_IMG, senderImage);
        // 如果是群聊那么需要设置接收消息的群的昵称
        if (emConversationType == EMConversation.EMConversationType.GroupChat) {
            txtSendMessage.setAttribute(Constants.CHAT_RECEIVE_ROOM_NAME, receiverName);
            txtSendMessage.setAttribute(Constants.CHAT_GROUP_AVATAR_IMG, receiverImage);
            txtSendMessage.setAttribute(Constants.GROUP_ID, groupId);
            txtSendMessage.setAttribute(Constants.GROUP_CHAT_TYPE, Constants.GROUP_CHAT_TYPE_GROUP);
            txtSendMessage.setChatType(EMMessage.ChatType.GroupChat);
        } else if (emConversationType == EMConversation.EMConversationType.ChatRoom) {
            txtSendMessage.setAttribute(Constants.CHAT_RECEIVE_ROOM_NAME, receiverName);
            txtSendMessage.setAttribute(Constants.CHAT_GROUP_AVATAR_IMG, receiverImage);
            txtSendMessage.setAttribute(Constants.GROUP_ID, groupId);
            txtSendMessage.setAttribute(Constants.GROUP_CHAT_TYPE, Constants.GROUP_CHAT_TYPE_ROOM);
            txtSendMessage.setChatType(EMMessage.ChatType.ChatRoom);
        } else {
            // 设置接收人的昵称和头像
            txtSendMessage.setAttribute(Constants.CHAT_RECEIVE_USER_NAME, receiverName);
            txtSendMessage.setAttribute(Constants.CHAT_RECEIVE_USER_AVTAR, receiverImage);
            // 设置聊天的 群聊或单聊
            txtSendMessage.setChatType(EMMessage.ChatType.Chat);
        }
        // 判断是否有@人的信息
        if (remindUserJson != null) {
            txtSendMessage.setAttribute(Constants.CHAT_AT_USER, remindUserJson);
        }
        txtSendMessage.setMessageStatusCallback(new SendMessageCallBack(txtSendMessage, onSendMessageListener));
        // 发送消息
        EMClient.getInstance().chatManager().sendMessage(txtSendMessage);
        // 刷新UI
        emChatLayout.setEdtChatEmpty();
        emChatAdapter.addItem(txtSendMessage);
        boolean lastItemVisible = isLastItemVisible();
        if (lastItemVisible) {
            scrollBottom();
        }
    }

    /**
     * 发送汽车数据
     * 单聊没有@某人，remindUserJson = null，groupId = null
     *
     * @param remindUserJson @的联系人
     */
    public void sendTxtMsgByCarInfo(String carInfo,
                                    String senderUsername, String senderImage, String senderHuanxinId,
                                    JSONArray remindUserJson,
                                    String groupId,
                                    String receiverName, String receiverImage, String receiverHuanxinId, OnSendMessageListener onSendMessageListener) {
        // TODO 发送汽车详情之前，检查消息中是否已经有发过了，避免重复发送，显示了多条

        EMMessage txtSendMessage = EMMessage.createTxtSendMessage(carInfo, receiverHuanxinId);
        // 设置汽车数据
        txtSendMessage.setAttribute(Constants.CAR_INFO_JSON, carInfo);
        // 设置发送人昵称
        txtSendMessage.setAttribute(Constants.CHAT_SEND_USER_NAME, senderUsername);
        // 设置发送人的环信ID
        txtSendMessage.setAttribute(Constants.CHAT_SEND_HUAN_XIN_ID, senderHuanxinId);
        // 设置发送人的头像
        txtSendMessage.setAttribute(Constants.CHAT_SEND_USER_IMG, senderImage);
        // 如果是群聊那么需要设置接收消息的群的昵称
        if (emConversationType == EMConversation.EMConversationType.GroupChat) {
            txtSendMessage.setAttribute(Constants.CHAT_RECEIVE_ROOM_NAME, receiverName);
            txtSendMessage.setAttribute(Constants.CHAT_GROUP_AVATAR_IMG, receiverImage);
            txtSendMessage.setAttribute(Constants.GROUP_ID, groupId);
            txtSendMessage.setAttribute(Constants.GROUP_CHAT_TYPE, Constants.GROUP_CHAT_TYPE_GROUP);
            txtSendMessage.setChatType(EMMessage.ChatType.GroupChat);
        } else if (emConversationType == EMConversation.EMConversationType.ChatRoom) {
            txtSendMessage.setAttribute(Constants.CHAT_RECEIVE_ROOM_NAME, receiverName);
            txtSendMessage.setAttribute(Constants.CHAT_GROUP_AVATAR_IMG, receiverImage);
            txtSendMessage.setAttribute(Constants.GROUP_ID, groupId);
            txtSendMessage.setAttribute(Constants.GROUP_CHAT_TYPE, Constants.GROUP_CHAT_TYPE_ROOM);
            txtSendMessage.setChatType(EMMessage.ChatType.ChatRoom);
        } else {
            // 设置接收人的昵称和头像
            txtSendMessage.setAttribute(Constants.CHAT_RECEIVE_USER_NAME, receiverName);
            txtSendMessage.setAttribute(Constants.CHAT_RECEIVE_USER_AVTAR, receiverImage);
            // 设置聊天的 群聊或单聊
            txtSendMessage.setChatType(EMMessage.ChatType.Chat);
        }
        // 判断是否有@人的信息
        if (remindUserJson != null) {
            txtSendMessage.setAttribute(Constants.CHAT_AT_USER, remindUserJson);
        }
        txtSendMessage.setMessageStatusCallback(new SendMessageCallBack(txtSendMessage, onSendMessageListener));
        // 发送消息
        EMClient.getInstance().chatManager().sendMessage(txtSendMessage);
        // 刷新UI
        emChatLayout.setEdtChatEmpty();
        emChatAdapter.addItem(txtSendMessage);
        boolean lastItemVisible = isLastItemVisible();
        if (lastItemVisible) {
            scrollBottom();
        }
    }

    /**
     * 单聊、群组或聊天室，发送位置信息
     * 单聊没有@某人，remindUserJson = null，groupId = null
     *
     * @param remindUserJson @的联系人
     */
    public void sendLocationMsg(double latitude, double longitude, String locationAddress,
                                String senderUsername, String senderImage, String senderHuanxinId,
                                JSONArray remindUserJson,
                                String groupId,
                                String receiverName, String receiverImage, String receiverHuanxinId, OnSendMessageListener onSendMessageListener) {
        //latitude为纬度，longitude为经度，locationAddress为具体位置内容
        EMMessage message = EMMessage.createLocationSendMessage(latitude, longitude, locationAddress, receiverHuanxinId);
        // 设置发送人昵称
        message.setAttribute(Constants.CHAT_SEND_USER_NAME, senderUsername);
        // 设置发送人的环信ID
        message.setAttribute(Constants.CHAT_SEND_HUAN_XIN_ID, senderHuanxinId);
        // 设置发送人的头像
        message.setAttribute(Constants.CHAT_SEND_USER_IMG, senderImage);
        // 如果是群聊那么需要设置接收消息的群的昵称
        if (emConversationType == EMConversation.EMConversationType.GroupChat) {
            message.setAttribute(Constants.CHAT_RECEIVE_ROOM_NAME, receiverName);
            message.setAttribute(Constants.CHAT_GROUP_AVATAR_IMG, receiverImage);
            message.setAttribute(Constants.GROUP_ID, groupId);
            message.setAttribute(Constants.GROUP_CHAT_TYPE, Constants.GROUP_CHAT_TYPE_GROUP);
            message.setChatType(EMMessage.ChatType.GroupChat);
        } else if (emConversationType == EMConversation.EMConversationType.ChatRoom) {
            message.setAttribute(Constants.CHAT_RECEIVE_ROOM_NAME, receiverName);
            message.setAttribute(Constants.CHAT_GROUP_AVATAR_IMG, receiverImage);
            message.setAttribute(Constants.GROUP_ID, groupId);
            message.setAttribute(Constants.GROUP_CHAT_TYPE, Constants.GROUP_CHAT_TYPE_ROOM);
            message.setChatType(EMMessage.ChatType.ChatRoom);
        } else {
            // 设置接收人的昵称和头像
            message.setAttribute(Constants.CHAT_RECEIVE_USER_NAME, receiverName);
            message.setAttribute(Constants.CHAT_RECEIVE_USER_AVTAR, receiverImage);
            // 设置聊天的 群聊或单聊
            message.setChatType(EMMessage.ChatType.Chat);
        }
        // 判断是否有@人的信息
        if (remindUserJson != null) {
            message.setAttribute(Constants.CHAT_AT_USER, remindUserJson);
        }
        message.setMessageStatusCallback(new SendMessageCallBack(message, onSendMessageListener));
        // 发送消息
        EMClient.getInstance().chatManager().sendMessage(message);
        // 刷新UI
        emChatLayout.setEdtChatEmpty();
        emChatAdapter.addItem(message);
        boolean lastItemVisible = isLastItemVisible();
        if (lastItemVisible) {
            scrollBottom();
        }
    }


    /**
     * 发送消息状态回调
     */
    private class SendMessageCallBack implements EMCallBack {

        private EMMessage emMessage;
        private OnSendMessageListener onSendMessageListener;

        /**
         * 文字
         */
        SendMessageCallBack(EMMessage emMessage, OnSendMessageListener onSendMessageListener) {
            this.emMessage = emMessage;
            this.onSendMessageListener = onSendMessageListener;
        }

        /**
         * 其他
         */
        SendMessageCallBack(EMMessage emMessage) {
            this.emMessage = emMessage;
        }

        @Override
        public void onSuccess() {
            Log.e("chat", "发送成功---------------------");
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    emMessage.setStatus(EMMessage.Status.SUCCESS);
                    emChatAdapter.notifyDataSetChanged();
                    // TODO 如果是图片和视频，删除缓存
                    if (emMessage.getType() == EMMessage.Type.IMAGE) {
                        if (null != onChatControllerListener) {
                            onChatControllerListener.onDeleteCacheDirFile();
                        }
                    }
                    if (onSendMessageListener != null) {
                        onSendMessageListener.onFinished();
                    }
                }
            });
        }

        @Override
        public void onError(int code, String error) {
            Log.e("chat", "发送失败：code=" + code + ";error=" + error);
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    emMessage.setStatus(EMMessage.Status.FAIL);
                    emChatAdapter.notifyDataSetChanged();
                    if (onSendMessageListener != null) {
                        onSendMessageListener.onFinished();
                    }
                    ChatToastUtils.showSendMsgToast(activity, code);
                }
            });
        }

        @Override
        public void onProgress(int progress, String status) {
            Log.e("chat", "发送进度：progress=" + progress + ";status=" + status);
        }
    }

    /**
     * 暂停播放
     */
    public void onPause() {
        MediaManager.pause();
    }

    /**
     * 恢复播放
     */
    public void onResume(String chatId) {
        // 更新消息，可能有语音消息
        if (mConversation != null) {
            if (!TextUtils.isEmpty(Constants.VOICE_MSG_ID)) {
                EMMessage message = mConversation.getMessage(Constants.VOICE_MSG_ID, true);
                if (message != null) {
                    String conversationId = message.conversationId();
                    if (chatId.equals(conversationId)) {
                        boolean lastItemVisible = isShowing(emChatAdapter.getCount() - 1);
                        emChatAdapter.addItem(message);
                        if (lastItemVisible) {
                            scrollBottom();
                        }
                    }
                }
            }
        }
        MediaManager.resume();
    }

    /**
     * 释放资源
     */
    public void onRelease() {
        Constants.VOICE_MSG_ID = "";
        AudioManager.getInstance(ConfigUtils.getFileDir(activity)).cancel();
    }
}
