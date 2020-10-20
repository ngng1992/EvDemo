package net.mfinance.chatlib.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.mfinance.chatlib.R;
import net.mfinance.chatlib.adapter.EmojiAdapter;
import net.mfinance.chatlib.impl.OnChatLayoutListener;
import net.mfinance.chatlib.impl.OnRecyclerViewItemClickListener;
import net.mfinance.chatlib.utils.EmojiUtils;
import net.mfinance.commonlib.permission.PermissionController;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * 布局view
 */
public class EMChatLayout extends RelativeLayout {

    private Context mContext;

    private LinearLayout llRootChat;
    private SwipeRefreshLayout swipeRefresh;
    private ListView listView;
    private LinearLayout llReplyMsg;
    private TextView tvReplyName;
    private TextView tvReplyContent;
    private EmojiEditText edtChatContent;
    private AudioRecorderTextView tvRecordVoice;
    private ImageView ivSendVoice;
    private ImageView ivSendEmoji;
    private ImageView ivSendFile;
    private TextView tvSendMsg;
    private LinearLayout llChoiceFile;
    private LinearLayout llChoiceCamera;
    private LinearLayout llChoiceImage;
    private LinearLayout llChoiceVideo;
    private LinearLayout llChoiceLocation;
    private LinearLayout llChoiceVoiceCall;
    private LinearLayout llChoiceEmoji;
    private ViewPager vpEmoji;
    private ImageView ivEmojiDotFirst;
    private ImageView ivEmojiDotSecond;
    private ImageView ivEmojiDotThird;

    private OnChatLayoutListener onChatLayoutListener;

    private TextWatcher etChatContentTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Editable text = edtChatContent.getText();
            String content = null;
            if (text != null) {
                content = text.toString();
            }
            if (TextUtils.isEmpty(content)) {
                tvSendMsg.setVisibility(count > 0 ? View.VISIBLE : View.GONE);
                ivSendFile.setVisibility(count > 0 ? View.GONE : View.VISIBLE);
            } else {
                ivSendFile.setVisibility(View.GONE);
                tvSendMsg.setVisibility(View.VISIBLE);
            }
            // TODO @处理，单聊没有选择@某人
            if (!TextUtils.isEmpty(s) && String.valueOf(s).endsWith("@")) {
                if (null != onChatLayoutListener) {
                    onChatLayoutListener.onSelectUserToActivity();
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public void setOnChatLayoutListener(OnChatLayoutListener onChatLayoutListener) {
        this.onChatLayoutListener = onChatLayoutListener;
    }

    public EMChatLayout(Context context) {
        super(context);
        initView(context);
    }

    public EMChatLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public EMChatLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @SuppressLint({"InflateParams", "ClickableViewAccessibility"})
    private void initView(Context context) {
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.view_chat_msg, this);
        llRootChat = findViewById(R.id.rl_root_chat);

        swipeRefresh = findViewById(R.id.swipe_refresh);
        listView = findViewById(R.id.rlv_msg);
        llReplyMsg = findViewById(R.id.ll_reply_msg);
        tvReplyName = findViewById(R.id.tv_reply_name);
        tvReplyContent = findViewById(R.id.tv_reply_content);

        ivSendVoice = findViewById(R.id.iv_send_voice);
        edtChatContent = findViewById(R.id.edt_chat_content);
        tvRecordVoice = findViewById(R.id.tv_record_voice);
        ivSendEmoji = findViewById(R.id.iv_send_emoji);
        ivSendFile = findViewById(R.id.iv_send_file);
        tvSendMsg = findViewById(R.id.tv_send_msg);

        llChoiceFile = findViewById(R.id.ll_choice_file);
        llChoiceCamera = findViewById(R.id.ll_choice_camera);
        llChoiceImage = findViewById(R.id.ll_choice_image);
        llChoiceVideo = findViewById(R.id.ll_choice_video);
        llChoiceLocation = findViewById(R.id.ll_choice_location);
        llChoiceVoiceCall = findViewById(R.id.ll_voice_call);

        llChoiceEmoji = findViewById(R.id.ll_choice_emoji);
        vpEmoji = findViewById(R.id.vp_emoji);
        ivEmojiDotFirst = findViewById(R.id.iv_emoji_dot_first);
        ivEmojiDotSecond = findViewById(R.id.iv_emoji_dot_second);
        ivEmojiDotThird = findViewById(R.id.iv_emoji_dot_third);

        setListView();
        ivSendVoice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onChatLayoutListener) {
                    onChatLayoutListener.onScrollListView();
                }
                // 请求录音和读写存储卡权限
                PermissionController permissionController = new PermissionController(context);
                permissionController.setPermissions(PermissionController.AUDIO_STORAGE);
                permissionController.setOnPermissionListener(new PermissionController.OnPermissionListener() {
                    @Override
                    public void onHasPermission(boolean hasPermission) {
                        llReplyMsg.setVisibility(View.GONE);
                        llChoiceFile.setVisibility(View.GONE);
                        llChoiceEmoji.setVisibility(View.GONE);
                        ivSendEmoji.setSelected(false);
                        ivSendFile.setSelected(false);

                        // 同意
                        ivSendVoice.setSelected(!ivSendVoice.isSelected());
                        edtChatContent.setVisibility(ivSendVoice.isSelected() ? GONE : VISIBLE);
                        tvRecordVoice.setVisibility(ivSendVoice.isSelected() ? VISIBLE : GONE);
                    }
                });
                permissionController.requestPermission();
            }
        });
        // 设置语言监听
        tvRecordVoice.setAudioFinishRecorderListener(new AudioRecorderTextView.AudioFinishRecorderListener() {
            @Override
            public void onFinishedRecording(float seconds, String filePath) {
                Log.e("chat", "seconds = " + seconds + ";filePath = " + filePath);
                if (null != onChatLayoutListener) {
                    onChatLayoutListener.onFinishedRecordVoice(seconds, filePath);
                }
            }
        });
        edtChatContent.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (null != onChatLayoutListener) {
                    onChatLayoutListener.onScrollListView();
                }
                ivSendVoice.setSelected(false);
                ivSendEmoji.setSelected(false);
                ivSendFile.setSelected(false);
                llChoiceFile.setVisibility(View.GONE);
                llChoiceEmoji.setVisibility(View.GONE);
                showKeyboard();
                return false;
            }
        });
        edtChatContent.addTextChangedListener(etChatContentTextWatcher);
        ivSendEmoji.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onChatLayoutListener) {
                    onChatLayoutListener.onScrollListView();
                }
                hintKeyboard();
                ivSendEmoji.setSelected(!ivSendEmoji.isSelected());
                llChoiceEmoji.setVisibility(ivSendEmoji.isSelected() ? View.VISIBLE : View.GONE);
                edtChatContent.setVisibility(View.VISIBLE);
                tvRecordVoice.setVisibility(View.GONE);
                llChoiceFile.setVisibility(View.GONE);
                ivSendVoice.setSelected(false);
                ivSendFile.setSelected(false);
            }
        });
        tvSendMsg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ivSendVoice.setSelected(false);
                ivSendEmoji.setSelected(false);
                ivSendFile.setSelected(false);
                llReplyMsg.setVisibility(View.GONE);
                llChoiceFile.setVisibility(View.GONE);
                llChoiceEmoji.setVisibility(View.GONE);
                if (null != onChatLayoutListener) {
                    Editable text = edtChatContent.getText();
                    String msg = text == null ? "" : text.toString();
                    onChatLayoutListener.onSendTextMessage(msg);
                }
            }
        });
        // 点击图片，不显示emoji，选中，显示选择图片，未选中，显示键盘
        ivSendFile.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onChatLayoutListener) {
                    onChatLayoutListener.onScrollListView();
                }

                hintKeyboard();
                ivSendVoice.setSelected(false);
                ivSendEmoji.setSelected(false);
                edtChatContent.setVisibility(VISIBLE);
                tvRecordVoice.setVisibility(GONE);
                ivSendFile.setSelected(!ivSendFile.isSelected());
                llChoiceFile.setVisibility(ivSendFile.isSelected() ? View.VISIBLE : View.GONE);
                llChoiceEmoji.setVisibility(View.GONE);
            }
        });
        llChoiceCamera.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                llChoiceFile.setVisibility(View.GONE);
                ivSendFile.setSelected(false);
                if (null != onChatLayoutListener) {
                    onChatLayoutListener.onOpenCamera();
                }
            }
        });
        llChoiceImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                llChoiceFile.setVisibility(View.GONE);
                ivSendFile.setSelected(false);
                if (null != onChatLayoutListener) {
                    onChatLayoutListener.onOpenSelectorImage();
                }
            }
        });
        llChoiceVideo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                llChoiceFile.setVisibility(View.GONE);
                ivSendFile.setSelected(false);
                if (null != onChatLayoutListener) {
                    onChatLayoutListener.onOpenSelectorVideo();
                }
            }
        });
        llChoiceLocation.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                llChoiceFile.setVisibility(View.GONE);
                ivSendFile.setSelected(false);
                if (null != onChatLayoutListener) {
                    onChatLayoutListener.onOpenLocation(0, 0, "", "");
                }
            }
        });
        llChoiceVoiceCall.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                llChoiceFile.setVisibility(View.GONE);
                ivSendFile.setSelected(false);
                if (null != onChatLayoutListener) {
                    onChatLayoutListener.onOpenVoiceCall();
                }
            }
        });
        initEmojiVp();
    }

    public ListView getListView() {
        return listView;
    }

    /**
     * 输入框设置空值
     */
    public void setEdtChatEmpty() {
        setEdtChatContent("");
    }

    /**
     * 输入框设置值
     */
    public void setEdtChatContent(String content) {
        edtChatContent.setText(EmojiUtils.getEmojiText(mContext, content));
    }

    /**
     * 获取编辑框的值
     */
    public String getEdtChatContent() {
        Editable text = edtChatContent.getText();
        if (text != null) {
            return text.toString();
        }
        return "";
    }

    /**
     * @ 某人的结果
     */
    @SuppressLint("SetTextI18n")
    public JSONArray getMembersJSONArray(String username, String huanxinId) {
        Editable text = edtChatContent.getText();
        String content = text == null ? "" : text.toString();
        edtChatContent.setText(content + username + "  ");
        edtChatContent.setSelection(edtChatContent.getText().toString().length());
        edtChatContent.requestFocus();
        // 显示输入键盘
        showKeyboard();
        List<String> atMembers = new ArrayList<>();
        atMembers.add(huanxinId);
        return new JSONArray(atMembers);
    }

    /**
     * 视频隐藏
     */
    public void hideChoiceVideoView() {
        llChoiceVideo.setVisibility(GONE);
    }

    /**
     * 设置listview
     */
    @SuppressLint("ClickableViewAccessibility")
    private void setListView() {
        // 设置默认状态选择器为全透明，不传颜色就是没颜色
        listView.setSelector(new ColorDrawable());
        // 去掉滚动条
        listView.setVerticalScrollBarEnabled(false);
        listView.setFastScrollEnabled(false);

        // 触摸listview时，输入法键盘、选择文件、选择emoji等隐藏，选择控件恢复原状
        listView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ivSendVoice.setSelected(false);
                ivSendEmoji.setSelected(false);
                ivSendFile.setSelected(false);
                edtChatContent.setVisibility(VISIBLE);
                tvRecordVoice.setVisibility(GONE);
                llChoiceFile.setVisibility(View.GONE);
                llChoiceEmoji.setVisibility(View.GONE);
                hintKeyboard();
                return false;
            }
        });

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (null != onChatLayoutListener) {
                    onChatLayoutListener.onRefresh();
                }
            }
        });
    }

    /**
     * 设置SwipeRefreshLayout的颜色
     *
     * @param resColor 资源id
     */
    public void setSwipeRefreshLayoutColor(int... resColor) {
        swipeRefresh.setColorSchemeColors(resColor);
    }

    /**
     * 隐藏输入法键盘
     */
    private void hintKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(llRootChat.getWindowToken(), 0);
        }
    }

    /**
     * 显示输入法键盘
     */
    private void showKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.showSoftInput(llRootChat, 0);
        }
    }

    private void initEmojiVp() {
        // 第一个选中
        ivEmojiDotFirst.setSelected(true);

        List<String> reslist = new ArrayList<>();
        for (int x = 1; x < 36; x++) {
            String filename = "ee_" + x;
            reslist.add(filename);

        }
        for (int x = 60; x <= 113; x++) {
            String filename = "ee_" + x;
            reslist.add(filename);
        }

        List<View> emojiViews = new ArrayList<>();
        // 页面计算有3页，1、2、3
        for (int i = 1; i < 4; i++) {
            emojiViews.add(getGridRvView(reslist, i));
        }
        vpEmoji.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return emojiViews.size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                container.addView(emojiViews.get(position));
                return emojiViews.get(position);
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView((View) object);
            }
        });
        vpEmoji.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        ivEmojiDotFirst.setSelected(true);
                        ivEmojiDotSecond.setSelected(false);
                        ivEmojiDotThird.setSelected(false);
                        break;
                    case 1:
                        ivEmojiDotFirst.setSelected(false);
                        ivEmojiDotSecond.setSelected(true);
                        ivEmojiDotThird.setSelected(false);
                        break;
                    case 2:
                        ivEmojiDotFirst.setSelected(false);
                        ivEmojiDotSecond.setSelected(false);
                        ivEmojiDotThird.setSelected(true);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private View getGridRvView(List<String> strEmojis, int i) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(mContext).inflate(R.layout.grid_chat_emoji, null);
        GridView grvEmoji = view.findViewById(R.id.grv_emoji);
        List<String> emojiData = new ArrayList<>();
        switch (i) {
            case 1:
                emojiData.addAll(strEmojis.subList(0, 31));
                break;
            case 2:
                emojiData.addAll(strEmojis.subList(31, 62));
                break;
            case 3:
                emojiData.addAll(strEmojis.subList(62, strEmojis.size()));
                break;
            default:
                break;
        }
        emojiData.add("emoji_delete");
        EmojiAdapter emojiAdapter = new EmojiAdapter(mContext, emojiData);
        grvEmoji.setAdapter(emojiAdapter);
        emojiAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, String emoji) {
                // ee_10获取的字符串
                if (emoji.equals("emoji_delete")) {
                    // 删除文字或表情
                    if (!TextUtils.isEmpty(edtChatContent.getText())) {
                        // 光标位置
                        int selectionStart = edtChatContent.getSelectionStart();
                        if (selectionStart > 0) {
                            String content = String.valueOf(edtChatContent.getText());
                            String strBody = content.substring(0, selectionStart);
                            String strEnd = strBody.substring(selectionStart - 1, selectionStart);
                            int last = strBody.lastIndexOf("[");
                            if ("]".equals(strEnd) && selectionStart - last < 9) {
                                // 最后一个元素是表情
                                edtChatContent.getEditableText().delete(last, selectionStart);
                            } else {
                                // 最后一个元素不是表情
                                edtChatContent.getEditableText().delete(selectionStart - 1, selectionStart);
                            }
                        }
                    }
                } else {
                    Spannable spannable = EmojiUtils.getString(mContext, emoji);
                    if (spannable != null) {
                        edtChatContent.append(spannable);
                    }
                }
            }
        });
        return view;
    }

    /**
     * 完成刷新
     */
    public void onRefreshComplete() {
        swipeRefresh.setRefreshing(false);
    }

    /**
     * 设置回复的内容并显示
     */
    public void showReplyView(String replyName, String replyContent) {
        llReplyMsg.setVisibility(View.VISIBLE);
        tvReplyName.setText(replyName);
        // 如果有表情符号，转换
        Spannable emojiText = EmojiUtils.getEmojiText(mContext, replyContent);
        tvReplyContent.setText(emojiText);
    }
}
