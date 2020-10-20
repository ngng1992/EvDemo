package net.mfinance.chatlib.impl;

import java.util.List;

/**
 * 消息控制接口
 */
public interface OnChatControllerListener {

    void onPlayVideo(String videoUrl, String videoName, String videoThumbnailUrl);

    /**
     * @ 某人
     */
    void onSelectUserToActivity();

    /**
     * 发送录音消息
     *
     * @param seconds  录音n秒
     * @param filePath 文件路径
     */
    void onSendVoiceMessage(float seconds, String filePath);

    /**
     * 发送文字消息
     *
     * @param message 发送的内容
     */
    void onSendTxtMessage(String message);

    /**
     * 聊天室或群聊回复消息
     */
    void onSendReplyTxtMessage(String message, String replyName, String replyContent);

    /**
     * 汽车信息
     *
     * @param type  type = 1订单，type = 2寻车
     * @param carId 可能是订单id，或是寻车id
     */
    void onCarInfo(String type, String carId);

    /**
     * 打开相机
     */
    void onOpenCamera();

    /**
     * 选择图片
     */
    void onOpenSelectorImage();

    /**
     * 选择视频
     */
    void onOpenSelectorVideo();

    /**
     * 选择位置
     * @param type ios发送的扩展字段类型
     */
    void onOpenLocation(double latitude, double longitude, String address, String type);

    /**
     * 选择语音
     */
    void onOpenVoiceCall();

    /**
     * 选择语音
     */
    void onOpenSentVoiceCall(String chatId, String chatName, String chatImage, String senderHuanxinId, String senderUsername, String senderImage);

    /**
     * 选择语音
     */
    void onOpenReceiveVoiceCall(String chatId, String chatName, String chatImage, String senderHuanxinId, String senderUsername, String senderImage);

    /**
     * 查看图片
     * @param imgList 图片集合
     * @param position 当前点击的图片地址
     */
    void startImageWatcher(List<String> imgList, int position);

    /**
     * 删除缓存文件
     */
    void onDeleteCacheDirFile();


    /**
     * 单聊时显示接收的最后一条消息的用户名
     */
    void onShowLastUsernameBySingle(String username);
}
