package net.mfinance.chatlib.constants;

public class Constants {

    /**
     * 聊天消息类型：接收文本
     */
    public static final int CHAT_RECEIVE_TXT = 0;
    /**
     * 聊天消息类型：接收图片
     */
    public static final int CHAT_RECEIVE_IMAGE = 1;
    /**
     * 聊天消息类型：接收视频
     */
    public static final int CHAT_RECEIVE_VIDEO = 2;
    /**
     * 聊天消息类型：接收位置
     */
    public static final int CHAT_RECEIVE_LOCATION = 3;
    /**
     * 聊天消息类型：接收语音
     */
    public static final int CHAT_RECEIVE_VOICE = 4;
    /**
     * 聊天消息类型：接收文件
     */
    public static final int CHAT_RECEIVE_FILE = 5;
    /**
     * 聊天消息类型：接收透传消息
     */
    public static final int CHAT_RECEIVE_CMD = 6;

    /**
     * 聊天消息类型：发送文本
     */
    public static final int CHAT_SEND_TXT = 7;
    /**
     * 聊天消息类型：发送图片
     */
    public static final int CHAT_SEND_IMAGE = 8;
    /**
     * 聊天消息类型：发送视频
     */
    public static final int CHAT_SEND_VIDEO = 9;
    /**
     * 聊天消息类型：发送位置
     */
    public static final int CHAT_SEND_LOCATION = 10;
    /**
     * 聊天消息类型：发送语音
     */
    public static final int CHAT_SEND_VOICE = 11;
    /**
     * 聊天消息类型：发送文件
     */
    public static final int CHAT_SEND_FILE = 12;
    /**
     * 聊天消息类型：发送透传消息
     */
    public static final int CHAT_SEND_CMD = 13;
    /**
     * 回复某人：发送
     */
    public static final int CHAT_SEND_REPLY_TXT = 14;
    /**
     * 回复某人：接收
     */
    public static final int CHAT_RECEIVE_REPLY_TXT = 15;

    /**
     * 发送汽车信息
     */
    public static final int CHAT_SEND_CAR_INFO = 16;
    /**
     * 接收汽车信息
     */
    public static final int CHAT_RECEIVE_CAR_INFO = 17;

    /**
     * 接收的语音通话
     */
    public static final int CHAT_RECEIVE_VOICE_CALL = 18;

    /**
     * 发送的语音通话
     */
    public static final int CHAT_SEND_VOICE_CALL = 19;

    /**
     * 回复人的名称
     */
    public static final String CHAT_SEND_REPLY_NAME = "reply_name";

    /**
     * 回复人的内容
     */
    public static final String CHAT_SEND_REPLY_CONTENT = "reply_content";

    /**
     * 环信消息扩展内容中的当前用户头像
     */
    public static final String CHAT_SEND_USER_IMG = "img";

    /**
     * 环信消息扩展内容中的发送消息的接收的昵称
     */
    public static final String CHAT_RECEIVE_USER_NAME = "other_name";
    /**
     * 环信消息扩展内容中的发送消息的接收的头像
     */
    public static final String CHAT_RECEIVE_USER_AVTAR = "other_img";

    /**
     * 环信消息扩展内容中的群头像
     */
    public static final String CHAT_GROUP_AVATAR_IMG = "group_imgUrl";

    /**
     * 环信消息扩展内容中的发送消息的用户的名称
     */
    public static final String CHAT_SEND_USER_NAME = "name";

    /**
     * 环信消息扩展内容中的发送消息的用户的环信id
     */
    public static final String CHAT_SEND_HUAN_XIN_ID = "unick";

    /**
     * 环信消息扩展内容中的群名称
     */
    public static final String CHAT_RECEIVE_ROOM_NAME = "room_name";

    /**
     * 发送透传消息
     * 环信某条消息id
     */
    public static final String MSG_ID = "msgId";
    public static final String SEND_TRANSMISSION = "send_transmission";

    /**
     * CMS平台：聊天室
     */
    public static final int GROUP_CHAT_TYPE_ROOM = 1;
    /**
     * CMS平台：群聊
     */
    public static final int GROUP_CHAT_TYPE_GROUP = 0;

    /**
     * CMS平台：聊天类型：0群聊，1聊天室
     */
    public static final String GROUP_CHAT_TYPE = "group_chat_type";

    /**
     * 秘书提醒·相当于环信的id
     */
    public static final String MI_SHU_TI_XING = "mstx";

    /**
     * 操作管理员的环信id，用于订单状态的修改
     */
    public static final String CAR_DOG_ADMIN = "cardogadmin";

    /**
     * 环信消息扩展内容中@用户
     */
    public static final String CHAT_AT_USER = "em_at_list";

    /**
     * 群组id，不是环信群组id
     */
    public static final String GROUP_ID = "groupId";

    /**
     * 汽车信息，扩展字段(品牌名+车系+车型 + 指导价+意向价)
     */
    public static final String CAR_INFO_JSON = "car_info_json";

    /**
     * 语音通话扩展字段
     */
    public static final String MESSAGE_ATTR_IS_VOICE_CALL = "is_voice_call";

    /**
     * 视频通话
     */
    public static final String MESSAGE_ATTR_IS_VIDEO_CALL = "is_video_call";

    /**
     * ios发送android的地图位置的扩展字段
     */
    public static final String MESSAGE_ATTR_IOS = "type";

    /**
     * 如果有语音通话，记录一个语音
     */
    public static String VOICE_MSG_ID = "";
}
