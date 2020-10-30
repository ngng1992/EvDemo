package com.mfinance.everjoy.everjoy.ui.mine.securities;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.mfinance.everjoy.R;
import com.mfinance.everjoy.everjoy.base.BaseViewActivity;
import com.mfinance.everjoy.everjoy.dialog.SelectBirthdayDialog;
import com.mfinance.everjoy.everjoy.dialog.SelectBirthplaceDialog;
import com.mfinance.everjoy.everjoy.dialog.impl.OnClickDialogOrFragmentViewListener;
import com.mfinance.everjoy.everjoy.sp.SecuritiesSharedPUtils;
import com.mfinance.everjoy.everjoy.ui.mine.ContactActivity;
import com.mfinance.everjoy.everjoy.utils.CardIdUtils;
import com.mfinance.everjoy.everjoy.utils.DateFormatUtils;
import com.mfinance.everjoy.everjoy.view.AccountEditorInfoView;
import com.mfinance.everjoy.everjoy.view.AccountStepView;

import net.mfinance.commonlib.toast.ToastUtils;
import net.mfinance.commonlib.view.StringTextView;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 开通大圣证券账户 - 第一页 个人资料
 */
public class SecuritiesAccountActivity extends BaseViewActivity {

    @BindArray(R.array.persNameTitle)
    String[] persNameTitle;
    @BindArray(R.array.country_area)
    String[] country_area;
    @BindArray(R.array.persMaritalStatus)
    String[] persMaritalStatus;
    @BindArray(R.array.persGender)
    String[] persGender;
    @BindArray(R.array.persIdType)
    String[] persIdType;

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.ll_account_step)
    AccountStepView llAccountStep;
    @BindView(R.id.ll_persNameTitle)
    AccountEditorInfoView ll_persNameTitle;
    @BindView(R.id.ll_persNameChinese)
    AccountEditorInfoView ll_persNameChinese;
    @BindView(R.id.ll_persNameEnglish)
    AccountEditorInfoView ll_persNameEnglish;
    @BindView(R.id.ll_persBirthRegion)
    AccountEditorInfoView ll_persBirthRegion;
    @BindView(R.id.ll_persBirthDate)
    AccountEditorInfoView ll_persBirthDate;
    @BindView(R.id.ll_persMaritalStatus)
    AccountEditorInfoView ll_persMaritalStatus;
    @BindView(R.id.ll_persGender)
    AccountEditorInfoView ll_persGender;
    @BindView(R.id.ll_persNationality)
    AccountEditorInfoView ll_persNationality;
    @BindView(R.id.ll_persIdType)
    AccountEditorInfoView ll_persIdType;
    @BindView(R.id.ll_persIdNo)
    AccountEditorInfoView ll_persIdNo;
    @BindView(R.id.ll_persBandAccRegion)
    AccountEditorInfoView ll_persBandAccRegion;
    @BindView(R.id.tv_recognition)
    TextView tvRecognition;
    @BindView(R.id.tv_next)
    TextView tvNext;
    @BindView(R.id.tv_contact)
    TextView tvContact;

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
        return R.layout.activity_securities_account;
    }

    /**
     * 保存到本地
     */
    private void saveToSP() {
        if (persNameTitleIndex != -1) {
            SecuritiesSharedPUtils.setPersNameTitle(persNameTitleIndex);
        }

        String persNameChinese = ll_persNameChinese.getEditorContent();
        SecuritiesSharedPUtils.setPersNameChinese(persNameChinese);

        String persNameEnglish = ll_persNameEnglish.getEditorContent();
        SecuritiesSharedPUtils.setPersNameEnglish(persNameEnglish);

        if (persBirthRegionIndex != -1) {
            SecuritiesSharedPUtils.setPersBirthRegion(persBirthRegionIndex);
        }

        if (selectDate != null) {
            long timeInMillis = selectDate.getTimeInMillis();
            SecuritiesSharedPUtils.setPersBirthDate(timeInMillis);
        }

        if (persMaritalStatusIndex != -1) {
            SecuritiesSharedPUtils.setPersMaritalStatus(persMaritalStatusIndex);
        }

        if (persGenderIndex != -1) {
            SecuritiesSharedPUtils.setPersGender(persGenderIndex);
        }

        if (persNationalityIndex != -1) {
            SecuritiesSharedPUtils.setPersNationality(persNationalityIndex);
        }

        if (persIdTypeIndex != -1) {
            SecuritiesSharedPUtils.setPersIdType(persIdTypeIndex);
        }

        String persIdNoEditorContent = ll_persIdNo.getEditorContent();
        SecuritiesSharedPUtils.setPersIdNo(persIdNoEditorContent);

        if (persBandAccRegionIndex != -1) {
            SecuritiesSharedPUtils.setPersBandAccRegion(persBandAccRegionIndex);
        }
    }

    @Override
    protected void initView(View currentView) {
        persNameTitleIndex = SecuritiesSharedPUtils.getPersNameTitle();
        if (persNameTitleIndex != -1) {
            ll_persNameTitle.setEditorContent(persNameTitle[persNameTitleIndex]);
        }
        ll_persNameTitle.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showNikenameType();
            }
        });

        String persNameChinese = SecuritiesSharedPUtils.getPersNameChinese();
        if (!TextUtils.isEmpty(persNameChinese)) {
            ll_persNameChinese.setEditorContent(persNameChinese);
        }

        String persNameEnglish = SecuritiesSharedPUtils.getPersNameEnglish();
        if (!TextUtils.isEmpty(persNameEnglish)) {
            ll_persNameEnglish.setEditorContent(persNameEnglish);
        }

        // 选择出生地区，默认香港
        persBirthRegionIndex = SecuritiesSharedPUtils.getPersBirthRegion();
        ll_persBirthRegion.setEditorContent(country_area[persBirthRegionIndex]);
        ll_persBirthRegion.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showBirthplace();
            }
        });

        // 选择出生日期,yyyy-mm-dd
        long persBirthDate = SecuritiesSharedPUtils.getPersBirthDate();
        if (persBirthDate != 0) {
            selectDate.setTimeInMillis(persBirthDate);
            String millis2String = TimeUtils.millis2String(persBirthDate, DateFormatUtils.DATE_FORMAT3);
            ll_persBirthDate.setEditorContent(millis2String);
        }
        ll_persBirthDate.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showBirthday();
            }
        });

        // 婚姻
        persMaritalStatusIndex = SecuritiesSharedPUtils.getPersMaritalStatus();
        if (persMaritalStatusIndex != -1) {
            String marriageType = persMaritalStatus[persMaritalStatusIndex];
            ll_persMaritalStatus.setEditorContent(marriageType);
        }
        ll_persMaritalStatus.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showMarriage();
            }
        });

        // 性别
        persGenderIndex = SecuritiesSharedPUtils.getPersGender();
        if (persGenderIndex != -1) {
            String marriageType = persGender[persGenderIndex];
            ll_persGender.setEditorContent(marriageType);
        }
        ll_persGender.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showSex();
            }
        });

        // 国籍地区
        persNationalityIndex = SecuritiesSharedPUtils.getPersGender();
        if (persNationalityIndex != -1) {
            String marriageType = country_area[persNationalityIndex];
            ll_persNationality.setEditorContent(marriageType);
        }
        ll_persNationality.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showCountry();
            }
        });

        // 证件类型
        persIdTypeIndex = SecuritiesSharedPUtils.getPersIdType();
        if (persIdTypeIndex != -1) {
            String name = persIdType[persIdTypeIndex];
            ll_persIdType.setEditorContent(name);
        }
        ll_persIdType.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showCardtype();
            }
        });
        // 证件号码
        String persIdNo = SecuritiesSharedPUtils.getPersIdNo();
        if (!TextUtils.isEmpty(persIdNo)) {
            ll_persIdNo.setEditorContent(persIdNo);
        }

        // 银行账户所属地区
        persBandAccRegionIndex = SecuritiesSharedPUtils.getPersBandAccRegion();
        if (persBandAccRegionIndex != -1) {
            String name = country_area[persBandAccRegionIndex];
            ll_persBandAccRegion.setEditorContent(name);
        }
        ll_persBandAccRegion.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showBankAccountArea();
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
                        ContactActivity.startContactActivity(SecuritiesAccountActivity.this);
                    }
                })
                .create();
    }


    private int persBandAccRegionIndex = 0;

    /**
     * 银行账户所属地区
     */
    private void showBankAccountArea() {
        SelectBirthplaceDialog dialog = new SelectBirthplaceDialog(this, persBandAccRegionIndex, Arrays.asList(country_area));
        dialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                persBandAccRegionIndex = object == null ? 3 : (int) object;
                String type = country_area[persBandAccRegionIndex];
                ll_persBandAccRegion.setEditorContent(type);
            }
        });
        dialog.show();
    }

    /**
     * 证件类型
     */
    private int persIdTypeIndex = -1;

    /**
     * 选择证件类型
     */
    private void showCardtype() {
        // 根据选择国籍地区选择不同类型
        if (persNationalityIndex == 3) {
            persIdTypeIndex = 0;
        } else if (persNationalityIndex == 0) {
            persIdTypeIndex = 1;
        } else {
            persIdTypeIndex = 2;
        }
        SelectBirthplaceDialog dialog = new SelectBirthplaceDialog(this, persIdTypeIndex, Arrays.asList(persIdType));
        dialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                persIdTypeIndex = object == null ? 0 : (int) object;
                String type = persIdType[persIdTypeIndex];
                ll_persIdType.setEditorContent(type);
            }
        });
        dialog.show();
    }

    /**
     * 默认第3个
     */
    private int persNationalityIndex = 3;

    /**
     * 选择国籍地区
     */
    private void showCountry() {
        SelectBirthplaceDialog dialog = new SelectBirthplaceDialog(this, persNationalityIndex, Arrays.asList(country_area));
        dialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                persNationalityIndex = object == null ? 3 : (int) object;
                String type = country_area[persNationalityIndex];
                ll_persNationality.setEditorContent(type);

                // 根据选择国籍地区选择不同类型
                if (persNationalityIndex == 3) {
                    persIdTypeIndex = 0;
                } else if (persNationalityIndex == 0) {
                    persIdTypeIndex = 1;
                } else {
                    persIdTypeIndex = 2;
                }
                ll_persIdType.setEditorContent(persIdType[persIdTypeIndex]);
            }
        });
        dialog.show();
    }

    private int persGenderIndex = -1;

    /**
     * 选择性别
     */
    private void showSex() {
        SelectBirthplaceDialog dialog = new SelectBirthplaceDialog(this, persGenderIndex, Arrays.asList(persGender));
        dialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                persGenderIndex = object == null ? 0 : (int) object;
                String type = persGender[persGenderIndex];
                ll_persGender.setEditorContent(type);
            }
        });
        dialog.show();
    }

    private int persMaritalStatusIndex = -1;

    /**
     * 选择婚姻
     */
    private void showMarriage() {
        SelectBirthplaceDialog selectBirthplaceDialog = new SelectBirthplaceDialog(this,
                persMaritalStatusIndex, Arrays.asList(persMaritalStatus));
        selectBirthplaceDialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                persMaritalStatusIndex = object == null ? 0 : (int) object;
                String marriageType = persMaritalStatus[persMaritalStatusIndex];
                ll_persMaritalStatus.setEditorContent(marriageType);
            }
        });
        selectBirthplaceDialog.show();
    }

    /**
     * 选中的时间
     */
    private Calendar selectDate;

    /**
     * 选择出生日期
     */
    private void showBirthday() {
        SelectBirthdayDialog selectBirthdayDialog = new SelectBirthdayDialog(this, selectDate);
        selectBirthdayDialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                selectDate = (Calendar) object;
                // 毫秒
                long timeInMillis = selectDate.getTimeInMillis();
                // 当天时间比选中的时间小于18（567648000000）年，提示
                if (System.currentTimeMillis() - timeInMillis < 567648000000L) {
                    ToastUtils.showToast(SecuritiesAccountActivity.this, R.string.toast_sec_acc_eighteen);
                    return;
                }
                String millis2String = TimeUtils.millis2String(timeInMillis, DateFormatUtils.getDateFormat3());
                ll_persBirthDate.setEditorContent(millis2String);
            }
        });
        selectBirthdayDialog.show();
    }

    /**
     * 默认第3个
     */
    private int persBirthRegionIndex = 3;

    /**
     * 选择出生地区
     */
    private void showBirthplace() {
        SelectBirthplaceDialog selectBirthplaceDialog = new SelectBirthplaceDialog(this, persBirthRegionIndex, Arrays.asList(country_area));
        selectBirthplaceDialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                persBirthRegionIndex = object == null ? 3 : (int) object;
                String birthplace = country_area[persBirthRegionIndex];
                ll_persBirthRegion.setEditorContent(birthplace);
            }
        });
        selectBirthplaceDialog.show();
    }

    private int persNameTitleIndex = -1;

    /**
     * 选择称谓
     */
    private void showNikenameType() {
        SelectBirthplaceDialog selectBirthplaceDialog = new SelectBirthplaceDialog(this, persNameTitleIndex, Arrays.asList(persNameTitle));
        selectBirthplaceDialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                persNameTitleIndex = object == null ? 0 : (int) object;
                String persName = persNameTitle[persNameTitleIndex];
                ll_persNameTitle.setEditorContent(persName);
            }
        });
        selectBirthplaceDialog.show();

    }

    private int index = 0;

    public void onStepIndex(View view) {
        index++;
        llAccountStep.scrollStep(index);
    }

    @OnClick({R.id.iv_back, R.id.tv_recognition, R.id.tv_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_recognition:
                RecognitionActivity.startActivityResult(this);
                break;
            case R.id.tv_next:
                // 必填
                String persNameTitleEditorContent = ll_persNameTitle.getEditorContent();
                if (TextUtils.isEmpty(persNameTitleEditorContent)) {
                    ToastUtils.showToast(this, ll_persNameTitle.getLeftAndHintContentForTip());
                    return;
                }
                String editorContent = ll_persNameChinese.getEditorContent();
                String persNameEnglishEditorContent = ll_persNameEnglish.getEditorContent();
                if (TextUtils.isEmpty(persNameEnglishEditorContent)) {
                    ToastUtils.showToast(this, ll_persNameEnglish.getHintContentForTip());
                    return;
                }
                String persBirthRegionEditorContent = ll_persBirthRegion.getEditorContent();
                if (TextUtils.isEmpty(persBirthRegionEditorContent)) {
                    ToastUtils.showToast(this, ll_persBirthRegion.getLeftAndHintContentForTip());
                    return;
                }
                String persBirthDateEditorContent = ll_persBirthDate.getEditorContent();
                if (TextUtils.isEmpty(persBirthDateEditorContent)) {
                    ToastUtils.showToast(this, ll_persBirthDate.getLeftAndHintContentForTip());
                    return;
                }
                String persMaritalStatusEditorContent = ll_persMaritalStatus.getEditorContent();
                if (TextUtils.isEmpty(persMaritalStatusEditorContent)) {
                    ToastUtils.showToast(this, ll_persMaritalStatus.getLeftAndHintContentForTip());
                    return;
                }
                String persGenderEditorContent = ll_persGender.getEditorContent();
                if (TextUtils.isEmpty(persGenderEditorContent)) {
                    ToastUtils.showToast(this, ll_persGender.getLeftAndHintContentForTip());
                    return;
                }
                String persNationalityEditorContent = ll_persNationality.getEditorContent();
                if (TextUtils.isEmpty(persNationalityEditorContent)) {
                    ToastUtils.showToast(this, ll_persNationality.getLeftAndHintContentForTip());
                    return;
                }
                String persIdTypeEditorContent = ll_persIdType.getEditorContent();
                if (TextUtils.isEmpty(persIdTypeEditorContent)) {
                    ToastUtils.showToast(this, ll_persIdType.getLeftAndHintContentForTip());
                    return;
                }
                String persIdNoEditorContent = ll_persIdNo.getEditorContent();
                if (TextUtils.isEmpty(persIdNoEditorContent)) {
                    ToastUtils.showToast(this, ll_persIdNo.getHintContentForTip());
                    return;
                }
                String persBandAccRegionEditorContent = ll_persBandAccRegion.getEditorContent();
                if (TextUtils.isEmpty(persBandAccRegionEditorContent)) {
                    ToastUtils.showToast(this, ll_persBandAccRegion.getLeftAndHintContentForTip());
                    return;
                }
                // 把资料保存到本地
                saveToSP();
                startActivity(new Intent(this, SecuritiesAccountContactActivity.class));
                break;
            default:
                break;
        }
    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        saveToSP();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RecognitionActivity.REQUEST_CODE_RECOGNITION) {
                // TODO  获取证件识别的信息
                ToastUtils.showToast(this, "获取证件识别的信息");
            }
        }
    }


}
