package com.mfinance.everjoy.everjoy.ui.mine.securities;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.everjoy.base.BaseViewActivity;
import com.mfinance.everjoy.everjoy.dialog.RiskDisclosureDialog;
import com.mfinance.everjoy.everjoy.dialog.impl.OnClickDialogOrFragmentViewListener;
import com.mfinance.everjoy.everjoy.sp.SecuritiesSharedPUtils;
import com.mfinance.everjoy.everjoy.ui.mine.ContactActivity;
import com.ywl5320.wlmedia.WlMedia;
import com.ywl5320.wlmedia.enums.WlComplete;
import com.ywl5320.wlmedia.enums.WlPlayModel;
import com.ywl5320.wlmedia.listener.WlOnCompleteListener;
import com.ywl5320.wlmedia.listener.WlOnPreparedListener;
import com.ywl5320.wlmedia.listener.WlOnTimeInfoListener;
import com.ywl5320.wlmedia.util.WlTimeUtil;

import net.mfinance.commonlib.view.StringTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 风险披露声明
 */
public class RiskDisclosureActivity extends BaseViewActivity {

    @BindView(R.id.iv_play)
    ImageView iv_play;
    @BindView(R.id.sb_play_progress)
    SeekBar sb_play_progress;
    @BindView(R.id.tv_play_time)
    TextView tv_play_time;
    @BindView(R.id.tv_total_time)
    TextView tv_total_time;
    @BindView(R.id.tv_contact)
    TextView tvContact;

    private int position;

    @Override
    protected boolean isRemoveAppBar() {
        return true;
    }

    @Override
    protected boolean isFullStatusByView() {
        return true;
    }

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_risk_disclosure;
    }

    @Override
    protected void initView(View currentView) {
        sb_play_progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (wlMedia != null) {
                    double duration = wlMedia.getDuration();
                    position = (int) (duration * progress / 100);
                    Log.e("voice", "position = " + position
                            + ";progress = " + progress +
                            ";duration = " + duration);
                    tv_play_time.setText(WlTimeUtil.secdsToDateFormat(position));
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
//                wlMedia.seek(position);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (wlMedia != null) {
                    wlMedia.seek(position);
                }

            }
        });

        // 在线咨询
        String verifMsg = getString(R.string.sec_acc_ui_contact);
        String target = verifMsg.substring(verifMsg.length() - 4);
        new StringTextView(tvContact)
                .setStrText(verifMsg)
                .setColor(getResources().getColor(R.color.blue18))
                .setTextSize(1f)
                .setTargetText(target)
                .setUnderline(false)
                .setClick(true)
                .setOnClickSpannableStringListener(new StringTextView.OnClickSpannableStringListener() {
                    @Override
                    public void onClickSpannableString(View view) {
                        ContactActivity.startContactActivity(RiskDisclosureActivity.this);
                    }
                })
                .create();
    }

    private boolean isPlaying;
    private boolean isStart;
    private boolean isCompleted;

    @OnClick({R.id.iv_back, R.id.iv_play, R.id.tv_speed, R.id.tv_prev, R.id.tv_next, R.id.tv_appointment_witness})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
            case R.id.tv_prev:
                finish();
                break;
            case R.id.iv_play:
                if (isStart) {
                    if (isCompleted) {
                        iv_play.setImageResource(R.mipmap.btn_pause);
                        start("http://sc1.111ttt.cn/2017/1/05/09/298092035545.mp3");
                        isCompleted = false;
                    } else {
                        if (isPlaying) {
                            iv_play.setImageResource(R.mipmap.btn_play);
                            wlMedia.pause();
                        } else {
                            iv_play.setImageResource(R.mipmap.btn_pause);
                            wlMedia.resume();
                        }
                        isPlaying = !isPlaying;
                    }
                } else {
                    isStart = true;
//                    tv_play_time.setVisibility(View.VISIBLE);
//                    tv_total_time.setVisibility(View.VISIBLE);
                    // TODO 设置url
                    start("http://sc1.111ttt.cn/2017/1/05/09/298092035545.mp3");
                    iv_play.setImageResource(R.mipmap.btn_pause);
                }
                break;
            case R.id.tv_speed:
                if (wlMedia != null) {
                    wlMedia.setSpeed(2f);
                }
                break;
            case R.id.tv_next:
                String name;
                String persNameEnglish = SecuritiesSharedPUtils.getPersNameEnglish();
                String persNameChinese = SecuritiesSharedPUtils.getPersNameChinese();
                if (!TextUtils.isEmpty(persNameEnglish)) {
                    name = persNameEnglish;
                }else {
                    name = persNameChinese;
                }
                String persIdNo = SecuritiesSharedPUtils.getPersIdNo();
                RiskDisclosureDialog riskDisclosureDialog = new RiskDisclosureDialog(this, name, persIdNo);
                riskDisclosureDialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
                    @Override
                    public void onClickView(View view, Object object) {
                        startActivity(new Intent(RiskDisclosureActivity.this, SigningAgreementActivity.class));
                    }
                });
                riskDisclosureDialog.show();
                break;
            case R.id.tv_appointment_witness:
                startActivity(new Intent(this, AppointmentWitnessActivity.class));
                break;
            default:
                break;
        }
    }


    private WlMedia wlMedia;

    public void start(String voiceUrl) {
        wlMedia = new WlMedia();
        wlMedia.setPlayModel(WlPlayModel.PLAYMODEL_ONLY_AUDIO);
        wlMedia.setSource(voiceUrl);
        wlMedia.setOnPreparedListener(new WlOnPreparedListener() {
            @Override
            public void onPrepared() {
                wlMedia.start();
                isPlaying = true;
            }
        });
        wlMedia.setOnTimeInfoListener(new WlOnTimeInfoListener() {
            @Override
            public void onTimeInfo(double currentTime, double bufferTime) {
                double totalSecs = wlMedia.getDuration();
                sb_play_progress.setProgress((int) (currentTime * 100 / totalSecs));
                tv_play_time.setText(WlTimeUtil.secdsToDateFormat((int) currentTime));
                tv_total_time.setText(WlTimeUtil.secdsToDateFormat((int) totalSecs));
            }
        });
        wlMedia.setOnCompleteListener(new WlOnCompleteListener() {
            @Override
            public void onComplete(WlComplete type) {
                // 播放完毕
                isPlaying = false;
                if (iv_play != null) {
                    iv_play.setImageResource(R.mipmap.btn_play);
                }
                sb_play_progress.setProgress(0);

                wlMedia.stop();
                wlMedia.release();
                isCompleted = true;
            }
        });
        wlMedia.prepared();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (wlMedia != null) {
            wlMedia.pause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (wlMedia != null) {
            wlMedia.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (wlMedia != null) {
            wlMedia.release();
        }
    }
}
