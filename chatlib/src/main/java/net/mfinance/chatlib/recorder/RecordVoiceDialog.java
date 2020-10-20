package net.mfinance.chatlib.recorder;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.mfinance.chatlib.R;


/**
 * 录音dialog
 */
public class RecordVoiceDialog {

    private Dialog mDialog;

    private ImageView mIcon;
    private ImageView mVoice;

    private TextView mLable;

    private Context mContext;

    public RecordVoiceDialog(Context context) {
        mContext = context;
    }

    public void showRecordingDialog() {
        mDialog = new Dialog(mContext, R.style.RecordSoundDialogStyle);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.dialog_chat_record_sound, null);
        mDialog.setContentView(view);

        mIcon = view.findViewById(R.id.dialog_icon);
        mVoice = view.findViewById(R.id.dialog_voice);
        mLable = view.findViewById(R.id.recorder_dialogtext);

        mDialog.show();
    }

    //正在播放时的状态
    public void recording() {
        if (mDialog != null && mDialog.isShowing()) {
            mIcon.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.VISIBLE);
            mLable.setVisibility(View.VISIBLE);

            mIcon.setImageResource(R.drawable.chat_record_sound_speak);
            mLable.setText(R.string.finger_slippery_cancel_send);
        }
    }

    //想要取消
    public void wantToCancel() {
        if (mDialog != null && mDialog.isShowing()) {
            mIcon.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.GONE);
            mLable.setVisibility(View.VISIBLE);

            mIcon.setImageResource(R.drawable.chat_record_sound_cancel);
            mLable.setText(R.string.finger_slippery_cancel_send);
        }
    }

    //录音时间太短
    public void tooShort() {
        if (mDialog != null && mDialog.isShowing()) {
            mIcon.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.GONE);
            mLable.setVisibility(View.VISIBLE);

            mIcon.setImageResource(R.drawable.chat_record_sound_short);
            mLable.setText(R.string.str_record_sound_short);
        }
    }

    //关闭dialog
    public void dimissDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    /**
     * 通过level更新voice上的图片
     *
     * @param level 级别1-7
     */
    public void updateVoiceLevel(int level) {
        if (mDialog != null && mDialog.isShowing()) {
            int resId;
            switch (level) {
                case 1:
                    resId = R.drawable.chat_record_sound_v1;
                    break;
                case 2:
                    resId = R.drawable.chat_record_sound_v2;
                    break;
                case 3:
                    resId = R.drawable.chat_record_sound_v3;
                    break;
                case 4:
                    resId = R.drawable.chat_record_sound_v4;
                    break;
                case 5:
                    resId = R.drawable.chat_record_sound_v5;
                    break;
                case 6:
                    resId = R.drawable.chat_record_sound_v6;
                    break;
                case 7:
                    resId = R.drawable.chat_record_sound_v7;
                    break;
                default:
                    resId = R.drawable.chat_record_sound_v1;
                    break;
            }
            mVoice.setImageResource(resId);
        }
    }
}
