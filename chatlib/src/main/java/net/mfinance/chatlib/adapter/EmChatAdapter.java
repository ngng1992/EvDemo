package net.mfinance.chatlib.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMLocationMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.chat.EMVideoMessageBody;
import com.hyphenate.chat.EMVoiceMessageBody;
import net.mfinance.chatlib.R;
import net.mfinance.chatlib.constants.Constants;
import net.mfinance.chatlib.utils.ChatLibUtils;
import net.mfinance.chatlib.utils.DateUtils;
import net.mfinance.chatlib.utils.EmojiUtils;
import net.mfinance.chatlib.utils.NumberPriceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

/**
 * 使用listview，当输入框出现时，把整个view顶上去
 */
public class EmChatAdapter extends BaseChatAdapter<EMMessage> {

    private Context mContext;
    private final LayoutInflater layoutInflater;

    /**
     * 当前登录用户名称
     */
    private String currentName;

    public void setCurrentName(String currentName) {
        this.currentName = currentName;
    }

    public EmChatAdapter(Context context, List<EMMessage> datas) {
        super(datas);
        this.mContext = context;
        layoutInflater = LayoutInflater.from(context);
    }

    /**
     * 根据消息的发送与接收设置类别
     *
     * @param position 第几个消息
     * @return int
     */
    @Override
    public int getItemViewType(int position) {
        EMMessage emMessage = mDataSet.get(position);
        // 消息的方向
        EMMessage.Direct direct = emMessage.direct();
        EMMessage.Type type = emMessage.getType();
        if (direct == EMMessage.Direct.SEND) {
            switch (type) {
                case TXT:
                    boolean isVoiceCall = emMessage.getBooleanAttribute(Constants.MESSAGE_ATTR_IS_VOICE_CALL, false);
                    if (isVoiceCall) {
                        return Constants.CHAT_SEND_VOICE_CALL;
                    }
                    String carInfo = emMessage.getStringAttribute(Constants.CAR_INFO_JSON, "");
                    if (!TextUtils.isEmpty(carInfo)) {
                        return Constants.CHAT_SEND_CAR_INFO;
                    }
                    String replyName = emMessage.getStringAttribute(Constants.CHAT_SEND_REPLY_NAME, "");
                    if (TextUtils.isEmpty(replyName)) {
                        return Constants.CHAT_SEND_TXT;
                    } else {
                        return Constants.CHAT_SEND_REPLY_TXT;
                    }
                case IMAGE:
                    return Constants.CHAT_SEND_IMAGE;
                case VIDEO:
                    return Constants.CHAT_SEND_VIDEO;
                case LOCATION:
                    return Constants.CHAT_SEND_LOCATION;
                case VOICE:
                    return Constants.CHAT_SEND_VOICE;
                case FILE:
                    return Constants.CHAT_SEND_FILE;
                case CMD:
                    return Constants.CHAT_SEND_CMD;
                default:
                    return -1;
            }
        } else {
            switch (type) {
                case TXT:
                    boolean isVoiceCall = emMessage.getBooleanAttribute(Constants.MESSAGE_ATTR_IS_VOICE_CALL, false);
                    if (isVoiceCall) {
                        return Constants.CHAT_RECEIVE_VOICE_CALL;
                    }
                    String carInfo = emMessage.getStringAttribute(Constants.CAR_INFO_JSON, "");
                    if (!TextUtils.isEmpty(carInfo)) {
                        return Constants.CHAT_RECEIVE_CAR_INFO;
                    }
                    String replyName = emMessage.getStringAttribute(Constants.CHAT_SEND_REPLY_NAME, "");
                    if (TextUtils.isEmpty(replyName)) {
                        return Constants.CHAT_RECEIVE_TXT;
                    } else {
                        return Constants.CHAT_RECEIVE_REPLY_TXT;
                    }
                case IMAGE:
                    return Constants.CHAT_RECEIVE_IMAGE;
                case VIDEO:
                    return Constants.CHAT_RECEIVE_VIDEO;
                case LOCATION:
                    return Constants.CHAT_RECEIVE_LOCATION;
                case VOICE:
                    return Constants.CHAT_RECEIVE_VOICE;
                case FILE:
                    return Constants.CHAT_RECEIVE_FILE;
                case CMD:
                    return Constants.CHAT_RECEIVE_CMD;
                default:
                    return -1;
            }
        }
    }

    /**
     * TODO 这个方法必须设置，与getItemViewType总个数一致，否则会报viewholder异常
     *
     * @return int 个数
     */
    @Override
    public int getViewTypeCount() {
        return 20;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case Constants.CHAT_SEND_TXT:
                SendTextViewHolder sendTextViewHolder;
                if (convertView == null) {
                    convertView = layoutInflater.inflate(R.layout.item_chat_send_txt, parent, false);
                    sendTextViewHolder = new SendTextViewHolder(convertView);
                    convertView.setTag(sendTextViewHolder);
                } else {
                    sendTextViewHolder = (SendTextViewHolder) convertView.getTag();
                }
                // 设置数据
                sendTextMsg(sendTextViewHolder, position);
                return convertView;
            case Constants.CHAT_SEND_VOICE:
                SendVoiceViewHolder sendVoiceViewHolder;
                if (convertView == null) {
                    convertView = layoutInflater.inflate(R.layout.item_chat_send_voice, parent, false);
                    sendVoiceViewHolder = new SendVoiceViewHolder(convertView);
                    convertView.setTag(sendVoiceViewHolder);
                } else {
                    sendVoiceViewHolder = (SendVoiceViewHolder) convertView.getTag();
                }
                // 设置数据
                sendVoiceMsg(sendVoiceViewHolder, position);
                return convertView;
            case Constants.CHAT_SEND_IMAGE:
                SendImageViewHolder sendImageViewHolder;
                if (convertView == null) {
                    convertView = layoutInflater.inflate(R.layout.item_chat_send_image, parent, false);
                    sendImageViewHolder = new SendImageViewHolder(convertView);
                    convertView.setTag(sendImageViewHolder);
                } else {
                    sendImageViewHolder = (SendImageViewHolder) convertView.getTag();
                }
                // 设置数据
                sendImageMsg(sendImageViewHolder, position);
                return convertView;
            case Constants.CHAT_SEND_VIDEO:
                SendVideoViewHolder sendVideoViewHolder;
                if (convertView == null) {
                    convertView = layoutInflater.inflate(R.layout.item_chat_send_video, parent, false);
                    sendVideoViewHolder = new SendVideoViewHolder(convertView);
                    convertView.setTag(sendVideoViewHolder);
                } else {
                    sendVideoViewHolder = (SendVideoViewHolder) convertView.getTag();
                }
                // 设置数据
                sendVideoMsg(sendVideoViewHolder, position);
                return convertView;
            case Constants.CHAT_SEND_REPLY_TXT:
                SendReplyTextHolder sendReplyTextHolder;
                if (convertView == null) {
                    convertView = layoutInflater.inflate(R.layout.item_chat_send_reply_txt, parent, false);
                    sendReplyTextHolder = new SendReplyTextHolder(convertView);
                    convertView.setTag(sendReplyTextHolder);
                } else {
                    sendReplyTextHolder = (SendReplyTextHolder) convertView.getTag();
                }
                sendReplyTextMsg(sendReplyTextHolder, position);
                return convertView;
            case Constants.CHAT_SEND_LOCATION:
                SendLocationViewHolder sendLocationTextHolder;
                if (convertView == null) {
                    convertView = layoutInflater.inflate(R.layout.item_chat_send_location, parent, false);
                    sendLocationTextHolder = new SendLocationViewHolder(convertView);
                    convertView.setTag(sendLocationTextHolder);
                } else {
                    sendLocationTextHolder = (SendLocationViewHolder) convertView.getTag();
                }
                sendLocationMsg(sendLocationTextHolder, position);
                return convertView;
            case Constants.CHAT_SEND_VOICE_CALL:
                SendVoiceCallViewHolder sendVoiceCallViewHolder;
                if (convertView == null) {
                    convertView = layoutInflater.inflate(R.layout.item_chat_send_voice_call, parent, false);
                    sendVoiceCallViewHolder = new SendVoiceCallViewHolder(convertView);
                    convertView.setTag(sendVoiceCallViewHolder);
                } else {
                    sendVoiceCallViewHolder = (SendVoiceCallViewHolder) convertView.getTag();
                }
                sendVoiceCallMsg(sendVoiceCallViewHolder, position);
                return convertView;
            case Constants.CHAT_RECEIVE_TXT:
                ReceiveTextViewHolder receiveTextViewHolder;
                if (convertView == null) {
                    convertView = layoutInflater.inflate(R.layout.item_chat_receive_txt, parent, false);
                    receiveTextViewHolder = new ReceiveTextViewHolder(convertView);
                    convertView.setTag(receiveTextViewHolder);
                } else {
                    receiveTextViewHolder = (ReceiveTextViewHolder) convertView.getTag();
                }
                receiveTextMsg(receiveTextViewHolder, position);
                return convertView;
            case Constants.CHAT_RECEIVE_VOICE:
                ReceiveVoiceViewHolder receiveVoiceViewHolder;
                if (convertView == null) {
                    convertView = layoutInflater.inflate(R.layout.item_chat_receive_voice, parent, false);
                    receiveVoiceViewHolder = new ReceiveVoiceViewHolder(convertView);
                    convertView.setTag(receiveVoiceViewHolder);
                } else {
                    receiveVoiceViewHolder = (ReceiveVoiceViewHolder) convertView.getTag();
                }
                receiveVoiceMsg(receiveVoiceViewHolder, position);
                return convertView;
            case Constants.CHAT_RECEIVE_IMAGE:
                ReceiveImageViewHolder receiveImageViewHolder;
                if (convertView == null) {
                    convertView = layoutInflater.inflate(R.layout.item_chat_receive_image, parent, false);
                    receiveImageViewHolder = new ReceiveImageViewHolder(convertView);
                    convertView.setTag(receiveImageViewHolder);
                } else {
                    receiveImageViewHolder = (ReceiveImageViewHolder) convertView.getTag();
                }
                receiveImageMsg(receiveImageViewHolder, position);
                return convertView;
            case Constants.CHAT_RECEIVE_VIDEO:
                ReceiveVideoViewHolder receiveVideoViewHolder;
                if (convertView == null) {
                    convertView = layoutInflater.inflate(R.layout.item_chat_receive_video, parent, false);
                    receiveVideoViewHolder = new ReceiveVideoViewHolder(convertView);
                    convertView.setTag(receiveVideoViewHolder);
                } else {
                    receiveVideoViewHolder = (ReceiveVideoViewHolder) convertView.getTag();
                }
                receiveVideoMsg(receiveVideoViewHolder, position);
                return convertView;
            case Constants.CHAT_RECEIVE_REPLY_TXT:
                ReceiveReplyTextViewHolder receiveReplyTextViewHolder;
                if (convertView == null) {
                    convertView = layoutInflater.inflate(R.layout.item_chat_receive_reply_txt, parent, false);
                    receiveReplyTextViewHolder = new ReceiveReplyTextViewHolder(convertView);
                    convertView.setTag(receiveReplyTextViewHolder);
                } else {
                    receiveReplyTextViewHolder = (ReceiveReplyTextViewHolder) convertView.getTag();
                }
                receiveReplyTextMsg(receiveReplyTextViewHolder, position);
                return convertView;
            case Constants.CHAT_SEND_CAR_INFO:
            case Constants.CHAT_RECEIVE_CAR_INFO:
                CarInfoViewHolder carInfoViewHolder;
                if (convertView == null) {
                    convertView = layoutInflater.inflate(R.layout.item_chat_car_info, parent, false);
                    carInfoViewHolder = new CarInfoViewHolder(convertView);
                    convertView.setTag(carInfoViewHolder);
                } else {
                    carInfoViewHolder = (CarInfoViewHolder) convertView.getTag();
                }
                carInfoMsg(carInfoViewHolder, position);
                return convertView;
            case Constants.CHAT_RECEIVE_LOCATION:
                ReceiveLocationViewHolder receiveLocationViewHolder;
                if (convertView == null) {
                    convertView = layoutInflater.inflate(R.layout.item_chat_receive_location, parent, false);
                    receiveLocationViewHolder = new ReceiveLocationViewHolder(convertView);
                    convertView.setTag(receiveLocationViewHolder);
                } else {
                    receiveLocationViewHolder = (ReceiveLocationViewHolder) convertView.getTag();
                }
                receiveLocationMsg(receiveLocationViewHolder, position);
                return convertView;
            case Constants.CHAT_RECEIVE_VOICE_CALL:
                ReceiveVoiceCallViewHolder receiveVoiceCallViewHolder;
                if (convertView == null) {
                    convertView = layoutInflater.inflate(R.layout.item_chat_receive_voice_call, parent, false);
                    receiveVoiceCallViewHolder = new ReceiveVoiceCallViewHolder(convertView);
                    convertView.setTag(receiveVoiceCallViewHolder);
                } else {
                    receiveVoiceCallViewHolder = (ReceiveVoiceCallViewHolder) convertView.getTag();
                }
                receiveVoiceCallMsg(receiveVoiceCallViewHolder, position);
                return convertView;
            default:
                // TODO 如果聊天室发了未知的消息类型，这里就会报错
                return null;
        }
    }

    private void sendVoiceCallMsg(SendVoiceCallViewHolder holder, int position) {
        showChatTime(position, holder.tvChatTime);
        EMMessage currentEMMessage = mDataSet.get(position);
        String sendUserImg = currentEMMessage.getStringAttribute(Constants.CHAT_SEND_USER_IMG, "");
        setUserImage(mContext, sendUserImg, holder.ivUserAvatar);
        EMTextMessageBody emTextMessageBody = (EMTextMessageBody) currentEMMessage.getBody();
        String message = emTextMessageBody.getMessage();
        holder.tvSendVoiceCall.setText(message);
        setSendProgress(currentEMMessage, position, holder.sendProgressBar, holder.ivSendState);

        // 长按：删除
        holder.tvSendVoiceCall.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (null != onListViewItemChildLongClickListener) {
                    onListViewItemChildLongClickListener.onItemChildLongClick(holder.tvSendVoiceCall, position);
                }
                return false;
            }
        });
        holder.tvSendVoiceCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != onListViewItemChildClickListener) {
                    onListViewItemChildClickListener.onItemChildClick(holder.tvSendVoiceCall, position);
                }
            }
        });
    }

    private void sendLocationMsg(SendLocationViewHolder holder, int position) {
        showChatTime(position, holder.tvChatTime);
        EMMessage currentEMMessage = mDataSet.get(position);
        String sendUserImg = currentEMMessage.getStringAttribute(Constants.CHAT_SEND_USER_IMG, "");
        setUserImage(mContext, sendUserImg, holder.ivUserAvatar);
        EMLocationMessageBody emLocationMessageBody = (EMLocationMessageBody) currentEMMessage.getBody();
        String address = emLocationMessageBody.getAddress();
        holder.tvSendLocation.setText(address);
        setSendProgress(currentEMMessage, position, holder.sendProgressBar, holder.ivSendState);

        // 长按：删除，撤回
        holder.tvSendLocation.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (null != onListViewItemChildLongClickListener) {
                    onListViewItemChildLongClickListener.onItemChildLongClick(holder.tvSendLocation, position);
                }
                return false;
            }
        });
        holder.tvSendLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != onListViewItemChildClickListener) {
                    onListViewItemChildClickListener.onItemChildClick(holder.tvSendLocation, position);
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void carInfoMsg(CarInfoViewHolder holder, int position) {
        showChatTime(position, holder.tvChatTime);
        EMMessage emMessage = mDataSet.get(position);
        String carInfoJson = emMessage.getStringAttribute(Constants.CAR_INFO_JSON, "");
        try {
            JSONObject jsonObject = new JSONObject(carInfoJson);
            String carInfoId = jsonObject.optString("carInfoId");
            Log.e("chat", "carInfoMsg:carInfoId = " + carInfoId);
            String brandName = jsonObject.optString("brandName");
            String vehicleName = jsonObject.optString("vehicleName");
            String carModel = jsonObject.optString("carModel");
            int guidancePrice = jsonObject.optInt("guidePrice");
            int intentPrice = jsonObject.optInt("intentPrice");

            holder.tvCarName.setText(brandName + "  " + "  " + vehicleName + "  " + carModel);

            String guidePrice = NumberPriceUtil.toThou(guidancePrice);
            String gprice = String.format(mContext.getString(R.string.chat_guid_price), guidePrice);
            String price = "¥" + guidePrice;
            int start = gprice.indexOf(price);
            SpannableString spannableString = new SpannableString(gprice);
            // 加粗加大，与订单页粗细太大，订单用的是TTTextView，没有生效
            StyleSpan span = new StyleSpan(Typeface.NORMAL);
            spannableString.setSpan(span, start, start + price.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new RelativeSizeSpan(1.8f), start, start + price.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.tvGuidancePrice.setText(spannableString);

            String iPrice = NumberPriceUtil.toThou(intentPrice);
            String contentPrice = String.format(mContext.getString(R.string.chat_intent_price), iPrice);
            String price2 = "¥" + iPrice;
            int start2 = contentPrice.indexOf(price2);
            SpannableString spannableString2 = new SpannableString(contentPrice);
            StyleSpan span2 = new StyleSpan(Typeface.NORMAL);
            spannableString2.setSpan(span2, start2, start2 + price2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString2.setSpan(new RelativeSizeSpan(1.8f), start2, start2 + price2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.tvIntentPrice.setText(spannableString2);

            holder.llCarInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 车辆详情
                    if (onListViewItemChildClickListener != null) {
                        onListViewItemChildClickListener.onItemChildClick(holder.llCarInfo, position);
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void receiveReplyTextMsg(ReceiveReplyTextViewHolder holder, int position) {
        showChatTime(position, holder.tvChatTime);
        EMMessage emMessage = mDataSet.get(position);
        String replyName = emMessage.getStringAttribute(Constants.CHAT_SEND_REPLY_NAME, "");
        String replyContent = emMessage.getStringAttribute(Constants.CHAT_SEND_REPLY_CONTENT, "");
        // 当前登录用户的名称
        if (!TextUtils.isEmpty(currentName) && currentName.equals(replyName)) {
            holder.tvReplyReceiveName.setText(R.string.str_yours);
        } else {
            holder.tvReplyReceiveName.setText(replyName);
        }
        holder.tvReplyReceiveContent.setText(replyContent);

        EMTextMessageBody receiveTextMsgBody = (EMTextMessageBody) emMessage.getBody();
        // 接收人的头像
        String userImg = emMessage.getStringAttribute(Constants.CHAT_SEND_USER_IMG, "");
        setUserImage(mContext, userImg, holder.ivUserAvatar);
        String username = emMessage.getStringAttribute(Constants.CHAT_SEND_USER_NAME, "");
        holder.tvUsername.setText(username);
        String content = receiveTextMsgBody.getMessage();
        holder.tvReceiveMsg.setText(EmojiUtils.getEmojiText(mContext, content));

        holder.llSendReplyReceiveMsg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (null != onListViewItemChildLongClickListener) {
                    onListViewItemChildLongClickListener.onItemChildLongClick(holder.llSendReplyReceiveMsg, position);
                }
                return false;
            }
        });
    }

    private void receiveVideoMsg(ReceiveVideoViewHolder holder, int position) {
        showChatTime(position, holder.tvChatTime);
        EMMessage emMessage = mDataSet.get(position);
        String username = emMessage.getStringAttribute(Constants.CHAT_SEND_USER_NAME, "");
        holder.tvUsername.setText(username);
        String userImg = emMessage.getStringAttribute(Constants.CHAT_SEND_USER_IMG, "");
        setUserImage(mContext, userImg, holder.ivUserAvatar);
        EMVideoMessageBody videoMessageBody = (EMVideoMessageBody) emMessage.getBody();
        String thumbnailUrl = videoMessageBody.getThumbnailUrl();
        setImage(mContext, thumbnailUrl, holder.ivReceiveVideo);
        holder.rlReceiveVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onListViewItemChildClickListener) {
                    onListViewItemChildClickListener.onItemChildClick(holder.rlReceiveVideo, position);
                }
            }
        });
        holder.rlReceiveVideo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (null != onListViewItemChildLongClickListener) {
                    onListViewItemChildLongClickListener.onItemChildLongClick(holder.rlReceiveVideo, position);
                }
                return false;
            }
        });
    }

    private void receiveImageMsg(ReceiveImageViewHolder holder, int position) {
        showChatTime(position, holder.tvChatTime);
        EMMessage emMessage = mDataSet.get(position);
        EMImageMessageBody receiveImgBody = (EMImageMessageBody) emMessage.getBody();
        String userImg = emMessage.getStringAttribute(Constants.CHAT_SEND_USER_IMG, "");
        setUserImage(mContext, userImg, holder.ivUserAvatar);
        String remoteUrl = receiveImgBody.getRemoteUrl();
        setImage(mContext, remoteUrl, holder.ivReceiveImage);
        String username = emMessage.getStringAttribute(Constants.CHAT_SEND_USER_NAME, "");
        holder.tvUsername.setText(username);
        holder.ivReceiveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onListViewItemChildClickListener) {
                    onListViewItemChildClickListener.onItemChildClick(holder.ivReceiveImage, position);
                }
            }
        });
        holder.ivReceiveImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (null != onListViewItemChildLongClickListener) {
                    onListViewItemChildLongClickListener.onItemChildLongClick(holder.ivReceiveImage, position);
                }
                return false;
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void receiveVoiceMsg(ReceiveVoiceViewHolder holder, int position) {
        showChatTime(position, holder.tvChatTime);
        EMMessage emMessage = mDataSet.get(position);
        EMVoiceMessageBody receiveVoiceMsgBody = (EMVoiceMessageBody) emMessage.getBody();
        int length = receiveVoiceMsgBody.getLength();
        holder.tvVoiceLength.setText(length + "'");
        holder.ivListenState.setVisibility(emMessage.isListened() ? View.GONE : View.VISIBLE);
        String userImg = emMessage.getStringAttribute(Constants.CHAT_SEND_USER_IMG, "");
        setUserImage(mContext, userImg, holder.ivUserAvatar);
        String username = emMessage.getStringAttribute(Constants.CHAT_SEND_USER_NAME, "");
        holder.tvUsername.setText(username);

        ViewGroup.LayoutParams layoutParams = holder.rlReceiveVoice.getLayoutParams();
        if (length < 20) {
            layoutParams.width = ChatLibUtils.dp2px(80);
        } else if (length < 40) {
            layoutParams.width = ChatLibUtils.dp2px(160);
        } else {
            layoutParams.width = ChatLibUtils.dp2px(240);
        }
        holder.rlReceiveVoice.setLayoutParams(layoutParams);

        holder.rlReceiveVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onListViewItemChildClickListener) {
                    onListViewItemChildClickListener.onItemChildClick(holder.rlReceiveVoice, position);
                }
            }
        });
        holder.rlReceiveVoice.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (null != onListViewItemChildLongClickListener) {
                    onListViewItemChildLongClickListener.onItemChildLongClick(holder.rlReceiveVoice, position);
                }
                return false;
            }
        });
    }

    private void receiveVoiceCallMsg(ReceiveVoiceCallViewHolder holder, int position) {
        showChatTime(position, holder.tvChatTime);
        EMMessage emMessage = mDataSet.get(position);
        String userImg = emMessage.getStringAttribute(Constants.CHAT_SEND_USER_IMG, "");
        setUserImage(mContext, userImg, holder.ivUserAvatar);
        String username = emMessage.getStringAttribute(Constants.CHAT_SEND_USER_NAME, "");
        holder.tvUsername.setText(username);
        EMTextMessageBody receiveVoiceMsgMsgBody = (EMTextMessageBody) emMessage.getBody();
        String message = receiveVoiceMsgMsgBody.getMessage();
        holder.tvReceiveVoiceCall.setText(message);

        holder.tvReceiveVoiceCall.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (null != onListViewItemChildLongClickListener) {
                    onListViewItemChildLongClickListener.onItemChildLongClick(holder.tvReceiveVoiceCall, position);
                }
                return false;
            }
        });
        holder.tvReceiveVoiceCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO 点击可以重播
                if (null != onListViewItemChildClickListener) {
                    onListViewItemChildClickListener.onItemChildClick(holder.tvReceiveVoiceCall, position);
                }
            }
        });
    }

    private void receiveLocationMsg(ReceiveLocationViewHolder holder, int position) {
        showChatTime(position, holder.tvChatTime);
        EMMessage emMessage = mDataSet.get(position);
        String userImg = emMessage.getStringAttribute(Constants.CHAT_SEND_USER_IMG, "");
        setUserImage(mContext, userImg, holder.ivUserAvatar);
        String username = emMessage.getStringAttribute(Constants.CHAT_SEND_USER_NAME, "");
        holder.tvUsername.setText(username);
        EMLocationMessageBody receiveLocationMsgBody = (EMLocationMessageBody) emMessage.getBody();
        String address = receiveLocationMsgBody.getAddress();
        holder.tvReceiveLocation.setText(address);

        // 长按：删除
        holder.tvReceiveLocation.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (null != onListViewItemChildLongClickListener) {
                    onListViewItemChildLongClickListener.onItemChildLongClick(holder.tvReceiveLocation, position);
                }
                return false;
            }
        });
        holder.tvReceiveLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != onListViewItemChildClickListener) {
                    onListViewItemChildClickListener.onItemChildClick(holder.tvReceiveLocation, position);
                }
            }
        });
    }

    private void receiveTextMsg(ReceiveTextViewHolder holder, int position) {
        showChatTime(position, holder.tvChatTime);
        EMMessage emMessage = mDataSet.get(position);
        String userImg = emMessage.getStringAttribute(Constants.CHAT_SEND_USER_IMG, "");
        setUserImage(mContext, userImg, holder.ivUserAvatar);
        String username = emMessage.getStringAttribute(Constants.CHAT_SEND_USER_NAME, "");
        holder.tvUsername.setText(username);
        EMTextMessageBody receiveTextMsgBody = (EMTextMessageBody) emMessage.getBody();
        String content = receiveTextMsgBody.getMessage();
        holder.tvReceiveMsg.setText(EmojiUtils.getEmojiText(mContext, content));
        holder.tvReceiveMsg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (null != onListViewItemChildLongClickListener) {
                    onListViewItemChildLongClickListener.onItemChildLongClick(holder.tvReceiveMsg, position);
                }
                return false;
            }
        });
    }

    private void sendReplyTextMsg(SendReplyTextHolder holder, int position) {
        showChatTime(position, holder.tvChatTime);
        EMMessage currentEMMessage = mDataSet.get(position);
        String sendUserImg = currentEMMessage.getStringAttribute(Constants.CHAT_SEND_USER_IMG, "");
        setUserImage(mContext, sendUserImg, holder.ivUserAvatar);
        String replyName = currentEMMessage.getStringAttribute(Constants.CHAT_SEND_REPLY_NAME, "");
        String replyContent = currentEMMessage.getStringAttribute(Constants.CHAT_SEND_REPLY_CONTENT, "");
        holder.tvReplyUserName.setText(replyName);
        holder.tvReplyContent.setText(EmojiUtils.getEmojiText(mContext, replyContent));
        EMTextMessageBody emTextMessageBody = (EMTextMessageBody) currentEMMessage.getBody();
        String content = emTextMessageBody.getMessage();
        holder.tvSendMsg.setText(EmojiUtils.getEmojiText(mContext, content));
        setSendProgress(currentEMMessage, position, holder.sendProgressBar, holder.ivSendState);

        holder.llSendReplyMsg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (null != onListViewItemChildLongClickListener) {
                    onListViewItemChildLongClickListener.onItemChildLongClick(holder.tvSendMsg, position);
                }
                return false;
            }
        });
        holder.ivSendState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onListViewItemChildClickListener) {
                    onListViewItemChildClickListener.onItemChildClick(holder.ivSendState, position);
                }
            }
        });
    }

    private void sendVideoMsg(SendVideoViewHolder holder, int position) {
        showChatTime(position, holder.tvChatTime);
        EMMessage currentEMMessage = mDataSet.get(position);
        String sendUserImg = currentEMMessage.getStringAttribute(Constants.CHAT_SEND_USER_IMG, "");
        setUserImage(mContext, sendUserImg, holder.ivUserAvatar);
        EMVideoMessageBody videoMessageBody = (EMVideoMessageBody) currentEMMessage.getBody();
        // thumbnailUrl，glide可以加载视频快照，但是很慢，获取本地的视频图片地址
        String thumbnailUrl = videoMessageBody.getLocalThumb();
        setImage(mContext, thumbnailUrl, holder.ivSendVideoPic);
        setSendProgress(currentEMMessage, position, holder.sendProgressBar, holder.ivSendState);

        holder.rlSendVideoMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onListViewItemChildClickListener) {
                    onListViewItemChildClickListener.onItemChildClick(holder.rlSendVideoMsg, position);
                }
            }
        });
        holder.rlSendVideoMsg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (null != onListViewItemChildLongClickListener) {
                    onListViewItemChildLongClickListener.onItemChildLongClick(holder.rlSendVideoMsg, position);
                }
                return false;
            }
        });
        holder.ivSendState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onListViewItemChildClickListener) {
                    onListViewItemChildClickListener.onItemChildClick(holder.ivSendState, position);
                }
            }
        });
    }

    private void sendImageMsg(SendImageViewHolder holder, int position) {
        showChatTime(position, holder.tvChatTime);
        EMMessage currentEMMessage = mDataSet.get(position);
        String sendUserImg = currentEMMessage.getStringAttribute(Constants.CHAT_SEND_USER_IMG, "");
        setUserImage(mContext, sendUserImg, holder.ivUserAvatar);
        EMImageMessageBody sendImgBody = (EMImageMessageBody) currentEMMessage.getBody();
        // 本地图片地址
        String localUrl = sendImgBody.getLocalUrl();
        setImage(mContext, localUrl, holder.ivSendImage);
        setSendProgress(currentEMMessage, position, holder.sendProgressBar, holder.ivSendState);

        holder.ivSendImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (null != onListViewItemChildLongClickListener) {
                    onListViewItemChildLongClickListener.onItemChildLongClick(holder.ivSendImage, position);
                }
                return false;
            }
        });
        holder.ivSendImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onListViewItemChildClickListener) {
                    onListViewItemChildClickListener.onItemChildClick(holder.ivSendImage, position);
                }
            }
        });
        holder.ivSendState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onListViewItemChildClickListener) {
                    onListViewItemChildClickListener.onItemChildClick(holder.ivSendState, position);
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void sendVoiceMsg(SendVoiceViewHolder holder, int position) {
        showChatTime(position, holder.tvChatTime);
        EMMessage currentEMMessage = mDataSet.get(position);
        String sendUserImg = currentEMMessage.getStringAttribute(Constants.CHAT_SEND_USER_IMG, "");
        setUserImage(mContext, sendUserImg, holder.ivUserAvatar);
        EMVoiceMessageBody sendVoiceMsgBody = (EMVoiceMessageBody) currentEMMessage.getBody();
        int length = sendVoiceMsgBody.getLength();
        holder.tvVoiceLength.setText(length + "'");
        setSendProgress(currentEMMessage, position, holder.sendProgressBar, holder.ivSendState);

        ViewGroup.LayoutParams layoutParams = holder.rlSendVoice.getLayoutParams();
        if (length < 20) {
            layoutParams.width = ChatLibUtils.dp2px(80);
        } else if (length < 40) {
            layoutParams.width = ChatLibUtils.dp2px(160);
        } else {
            // 避免设置太大，超过宽度
            layoutParams.width = ChatLibUtils.dp2px(240);
        }
        holder.rlSendVoice.setLayoutParams(layoutParams);

        // 长按：删除、撤回
        holder.rlSendVoice.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (null != onListViewItemChildLongClickListener) {
                    onListViewItemChildLongClickListener.onItemChildLongClick(holder.rlSendVoice, position);
                }
                return false;
            }
        });
        holder.rlSendVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onListViewItemChildClickListener) {
                    onListViewItemChildClickListener.onItemChildClick(holder.rlSendVoice, position);
                }
            }
        });
        holder.ivSendState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onListViewItemChildClickListener) {
                    onListViewItemChildClickListener.onItemChildClick(holder.ivSendState, position);
                }
            }
        });
    }

    private void sendTextMsg(SendTextViewHolder holder, int position) {
        showChatTime(position, holder.tvChatTime);
        EMMessage currentEMMessage = mDataSet.get(position);
        String sendUserImg = currentEMMessage.getStringAttribute(Constants.CHAT_SEND_USER_IMG, "");
        setUserImage(mContext, sendUserImg, holder.ivUserAvatar);
        EMTextMessageBody emTextMessageBody = (EMTextMessageBody) currentEMMessage.getBody();
        String content = emTextMessageBody.getMessage();
        holder.tvSendMsg.setText(EmojiUtils.getEmojiText(mContext, content));
        setSendProgress(currentEMMessage, position, holder.sendProgressBar, holder.ivSendState);

        // 长按：复制，删除，撤回
        holder.tvSendMsg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (null != onListViewItemChildLongClickListener) {
                    onListViewItemChildLongClickListener.onItemChildLongClick(holder.tvSendMsg, position);
                }
                return false;
            }
        });
    }

    /**
     * 显示聊天时间
     *
     * @param position 第几个
     * @param textView view显示
     */
    private void showChatTime(int position, TextView textView) {
        EMMessage currentMessage = mDataSet.get(position);
        if (position >= 1) {
            EMMessage previousEMMessage = getItem(position - 1);
            String previousFrom = previousEMMessage.getFrom();
            String currentFrom = currentMessage.getFrom();
            // 上一条消息与当前消息是同一个人发的
            if (previousFrom.equals(currentFrom)) {
                // 超过时间，则显示时间，没有超过，则不显示
                long previousMsgTime = previousEMMessage.getMsgTime();
                long currentMsgTime = currentMessage.getMsgTime();
                if (currentMsgTime < previousMsgTime) {
                    textView.setVisibility(View.GONE);
                } else {
                    // 大于上一个消息时间，但小于30s不显示
                    if (DateUtils.isCloseEnough(currentMsgTime, previousMsgTime)) {
                        textView.setVisibility(View.GONE);
                    } else {
                        textView.setVisibility(View.VISIBLE);
                        textView.setText(DateUtils.getTimestampString(mContext, new Date(currentMessage.getMsgTime())));
                    }
                }
            } else {
                // 不是同一个人发的，显示时间
                textView.setVisibility(View.VISIBLE);
                textView.setText(DateUtils.getTimestampString(mContext, new Date(currentMessage.getMsgTime())));
            }
        } else {
            textView.setVisibility(View.VISIBLE);
            textView.setText(DateUtils.getTimestampString(mContext, new Date(currentMessage.getMsgTime())));
        }
    }

    private static class SendVoiceCallViewHolder {

        private TextView tvChatTime;
        private ImageView ivUserAvatar;
        private ImageView ivSendState;
        private ProgressBar sendProgressBar;
        private TextView tvSendVoiceCall;

        SendVoiceCallViewHolder(View itemView) {
            tvChatTime = itemView.findViewById(R.id.tv_chat_time);
            ivUserAvatar = itemView.findViewById(R.id.iv_user_avatar);
            ivSendState = itemView.findViewById(R.id.iv_send_state);
            sendProgressBar = itemView.findViewById(R.id.pb_loading);
            tvSendVoiceCall = itemView.findViewById(R.id.tv_send_voice_call);
        }
    }

    private static class SendLocationViewHolder {

        private TextView tvChatTime;
        private ImageView ivUserAvatar;
        private ImageView ivSendState;
        private ProgressBar sendProgressBar;
        private TextView tvSendLocation;

        SendLocationViewHolder(View itemView) {
            tvChatTime = itemView.findViewById(R.id.tv_chat_time);
            ivUserAvatar = itemView.findViewById(R.id.iv_user_avatar);
            ivSendState = itemView.findViewById(R.id.iv_send_state);
            sendProgressBar = itemView.findViewById(R.id.pb_loading);
            tvSendLocation = itemView.findViewById(R.id.tv_send_location);
        }
    }

    private static class CarInfoViewHolder {

        private LinearLayout llCarInfo;
        private TextView tvChatTime;
        private TextView tvCarName;
        private TextView tvGuidancePrice;
        private TextView tvIntentPrice;

        CarInfoViewHolder(View itemView) {
            llCarInfo = itemView.findViewById(R.id.ll_car_info);
            tvChatTime = itemView.findViewById(R.id.tv_chat_time);
            tvCarName = itemView.findViewById(R.id.tv_car_name);
            tvGuidancePrice = itemView.findViewById(R.id.tv_guidance_price);
            tvIntentPrice = itemView.findViewById(R.id.tv_intent_price);
        }
    }

    private static class SendTextViewHolder {

        private TextView tvChatTime;
        private ImageView ivUserAvatar;
        private ImageView ivSendState;
        private ProgressBar sendProgressBar;
        private TextView tvSendMsg;

        SendTextViewHolder(View itemView) {
            tvChatTime = itemView.findViewById(R.id.tv_chat_time);
            ivUserAvatar = itemView.findViewById(R.id.iv_user_avatar);
            ivSendState = itemView.findViewById(R.id.iv_send_state);
            sendProgressBar = itemView.findViewById(R.id.pb_loading);
            tvSendMsg = itemView.findViewById(R.id.tv_send_msg);
        }
    }

    private static class SendVoiceViewHolder {

        private TextView tvChatTime;
        private ImageView ivUserAvatar;
        private ImageView ivSendState;
        private ProgressBar sendProgressBar;
        private TextView tvVoiceLength;
        private RelativeLayout rlSendVoice;

        SendVoiceViewHolder(View itemView) {
            tvChatTime = itemView.findViewById(R.id.tv_chat_time);
            ivUserAvatar = itemView.findViewById(R.id.iv_user_avatar);
            ivSendState = itemView.findViewById(R.id.iv_send_state);
            sendProgressBar = itemView.findViewById(R.id.pb_loading);
            tvVoiceLength = itemView.findViewById(R.id.tv_voice_length);
            rlSendVoice = itemView.findViewById(R.id.rl_send_voice);
        }
    }

    private static class SendImageViewHolder {

        private TextView tvChatTime;
        private ImageView ivUserAvatar;
        private ImageView ivSendState;
        private ProgressBar sendProgressBar;
        private ImageView ivSendImage;

        SendImageViewHolder(View itemView) {
            tvChatTime = itemView.findViewById(R.id.tv_chat_time);
            ivUserAvatar = itemView.findViewById(R.id.iv_user_avatar);
            ivSendState = itemView.findViewById(R.id.iv_send_state);
            sendProgressBar = itemView.findViewById(R.id.pb_loading);
            ivSendImage = itemView.findViewById(R.id.iv_send_image);
        }
    }

    private static class SendVideoViewHolder {

        private TextView tvChatTime;
        private ImageView ivUserAvatar;
        private ImageView ivSendState;
        private ProgressBar sendProgressBar;
        private RelativeLayout rlSendVideoMsg;
        private ImageView ivSendVideoPic;

        SendVideoViewHolder(View itemView) {
            tvChatTime = itemView.findViewById(R.id.tv_chat_time);
            ivUserAvatar = itemView.findViewById(R.id.iv_user_avatar);
            ivSendState = itemView.findViewById(R.id.iv_send_state);
            sendProgressBar = itemView.findViewById(R.id.pb_loading);
            rlSendVideoMsg = itemView.findViewById(R.id.rl_send_video_msg);
            ivSendVideoPic = itemView.findViewById(R.id.iv_send_video_pic);
        }
    }

    private static class SendReplyTextHolder {

        private TextView tvChatTime;
        private ImageView ivUserAvatar;
        private ImageView ivSendState;
        private ProgressBar sendProgressBar;
        private LinearLayout llSendReplyMsg;
        private TextView tvReplyUserName;
        private TextView tvReplyContent;
        private TextView tvSendMsg;

        SendReplyTextHolder(View itemView) {
            tvChatTime = itemView.findViewById(R.id.tv_chat_time);
            ivUserAvatar = itemView.findViewById(R.id.iv_user_avatar);
            ivSendState = itemView.findViewById(R.id.iv_send_state);
            sendProgressBar = itemView.findViewById(R.id.pb_loading);
            llSendReplyMsg = itemView.findViewById(R.id.ll_send_reply_msg);
            tvReplyUserName = itemView.findViewById(R.id.tv_reply_send_name);
            tvReplyContent = itemView.findViewById(R.id.tv_reply_send_content);
            tvSendMsg = itemView.findViewById(R.id.tv_send_msg);
        }
    }

    private static class ReceiveVoiceCallViewHolder {

        private TextView tvChatTime;
        private ImageView ivUserAvatar;
        private TextView tvUsername;
        private TextView tvReceiveVoiceCall;

        ReceiveVoiceCallViewHolder(View itemView) {
            tvChatTime = itemView.findViewById(R.id.tv_chat_time);
            ivUserAvatar = itemView.findViewById(R.id.iv_user_avatar);
            tvUsername = itemView.findViewById(R.id.tv_chat_username);
            tvReceiveVoiceCall = itemView.findViewById(R.id.tv_receive_voice_call);
        }
    }

    private static class ReceiveLocationViewHolder {

        private TextView tvChatTime;
        private ImageView ivUserAvatar;
        private TextView tvUsername;
        private TextView tvReceiveLocation;

        ReceiveLocationViewHolder(View itemView) {
            tvChatTime = itemView.findViewById(R.id.tv_chat_time);
            ivUserAvatar = itemView.findViewById(R.id.iv_user_avatar);
            tvUsername = itemView.findViewById(R.id.tv_chat_username);
            tvReceiveLocation = itemView.findViewById(R.id.tv_receive_location);
        }
    }

    private static class ReceiveTextViewHolder {

        private TextView tvChatTime;
        private ImageView ivUserAvatar;
        private TextView tvUsername;
        private TextView tvReceiveMsg;

        ReceiveTextViewHolder(View itemView) {
            tvChatTime = itemView.findViewById(R.id.tv_chat_time);
            ivUserAvatar = itemView.findViewById(R.id.iv_user_avatar);
            tvUsername = itemView.findViewById(R.id.tv_chat_username);
            tvReceiveMsg = itemView.findViewById(R.id.tv_receive_msg);
        }
    }

    private static class ReceiveVoiceViewHolder {

        private TextView tvChatTime;
        private ImageView ivUserAvatar;
        private TextView tvUsername;
        private RelativeLayout rlReceiveVoice;
        private ImageView ivListenState;
        private TextView tvVoiceLength;

        ReceiveVoiceViewHolder(View itemView) {
            tvChatTime = itemView.findViewById(R.id.tv_chat_time);
            ivUserAvatar = itemView.findViewById(R.id.iv_user_avatar);
            tvUsername = itemView.findViewById(R.id.tv_chat_username);
            rlReceiveVoice = itemView.findViewById(R.id.rl_receive_voice);
            ivListenState = itemView.findViewById(R.id.iv_read_state);
            tvVoiceLength = itemView.findViewById(R.id.tv_voice_length);
        }
    }

    private static class ReceiveImageViewHolder {

        private TextView tvChatTime;
        private ImageView ivUserAvatar;
        private TextView tvUsername;
        private ImageView ivReceiveImage;

        ReceiveImageViewHolder(View itemView) {
            tvChatTime = itemView.findViewById(R.id.tv_chat_time);
            ivUserAvatar = itemView.findViewById(R.id.iv_user_avatar);
            tvUsername = itemView.findViewById(R.id.tv_chat_username);
            ivReceiveImage = itemView.findViewById(R.id.iv_receive_image);
        }
    }

    private static class ReceiveVideoViewHolder {

        private TextView tvChatTime;
        private ImageView ivUserAvatar;
        private TextView tvUsername;
        private RelativeLayout rlReceiveVideo;
        private ImageView ivReceiveVideo;

        ReceiveVideoViewHolder(View itemView) {
            tvChatTime = itemView.findViewById(R.id.tv_chat_time);
            ivUserAvatar = itemView.findViewById(R.id.iv_user_avatar);
            tvUsername = itemView.findViewById(R.id.tv_chat_username);
            rlReceiveVideo = itemView.findViewById(R.id.rl_receive_video);
            ivReceiveVideo = itemView.findViewById(R.id.iv_receive_video);
        }
    }

    private static class ReceiveReplyTextViewHolder {

        private TextView tvChatTime;
        private ImageView ivUserAvatar;
        private TextView tvUsername;
        private LinearLayout llSendReplyReceiveMsg;
        private TextView tvReplyReceiveName;
        private TextView tvReplyReceiveContent;
        private TextView tvReceiveMsg;

        ReceiveReplyTextViewHolder(View itemView) {
            tvChatTime = itemView.findViewById(R.id.tv_chat_time);
            ivUserAvatar = itemView.findViewById(R.id.iv_user_avatar);
            tvUsername = itemView.findViewById(R.id.tv_chat_username);
            llSendReplyReceiveMsg = itemView.findViewById(R.id.ll_send_reply_receive_msg);
            tvReplyReceiveName = itemView.findViewById(R.id.tv_reply_receive_name);
            tvReplyReceiveContent = itemView.findViewById(R.id.tv_reply_receive_content);
            tvReceiveMsg = itemView.findViewById(R.id.tv_receive_msg);
        }
    }

    /**
     * 设置加载进度
     *
     * @param emMessage 消息
     */
    private void setSendProgress(EMMessage emMessage, int position, ProgressBar sendProgressBar, ImageView ivSendState) {
        EMMessage.Status status = emMessage.status();
        if (status == EMMessage.Status.INPROGRESS) {
            sendProgressBar.setVisibility(View.VISIBLE);
            ivSendState.setVisibility(View.GONE);
        } else if (status == EMMessage.Status.FAIL) {
            sendProgressBar.setVisibility(View.GONE);
            ivSendState.setVisibility(View.VISIBLE);
        } else {
            sendProgressBar.setVisibility(View.GONE);
            ivSendState.setVisibility(View.GONE);
        }
        // 点击：失败时，重新发送
        ivSendState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onListViewItemChildClickListener) {
                    onListViewItemChildClickListener.onItemChildClick(ivSendState, position);
                }
            }
        });
    }
}
