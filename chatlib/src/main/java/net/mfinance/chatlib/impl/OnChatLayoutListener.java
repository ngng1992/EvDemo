package net.mfinance.chatlib.impl;

public interface OnChatLayoutListener {

    /**
     * 输入框输入@后的处理
     */
    void onSelectUserToActivity();

    /**
     * 翻页获取消息
     */
    void onRefresh();

    /**
     * 录音完成
     *
     * @param seconds  录音n秒
     * @param filePath 文件路径
     */
    void onFinishedRecordVoice(float seconds, String filePath);

    /**
     * 发送消息
     *
     * @param message 内容
     */
    void onSendTextMessage(String message);

    /**
     * 打开相机
     * <p>
     * 先获取读写存储卡，拍照权限
     */
    void onOpenCamera();

    /**
     * 打开图片选择器
     * <p>
     * 先获取读写存储卡权限
     */
    void onOpenSelectorImage();

    /**
     * 打开视频选择器
     * <p>
     * 先获取读写存储卡权限
     */
    void onOpenSelectorVideo();

    /**
     * 选择位置
     */
    void onOpenLocation(double latitude, double longitude, String address, String type);

    /**
     * 打开语音通话
     */
    void onOpenVoiceCall();

    /**
     * 当点击语音、输入框获取焦点、表情符号、选择文件发送时，listview滑动到最低端
     */
    void onScrollListView();
}
