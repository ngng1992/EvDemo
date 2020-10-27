package com.mfinance.everjoy.everjoy.ui.mine.securities;

import android.content.Intent;
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

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 开通大圣证券账户 - 第一页 个人资料
 */
public class SecuritiesAccountActivity extends BaseViewActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.ll_account_step)
    AccountStepView llAccountStep;
    @BindView(R.id.ll_editor_nikename)
    AccountEditorInfoView ll_editor_nikename;
    @BindView(R.id.ll_editor_ch_name)
    AccountEditorInfoView ll_editor_ch_name;
    @BindView(R.id.ll_editor_eg_name)
    AccountEditorInfoView ll_editor_eg_name;
    @BindView(R.id.ll_editor_birthplace)
    AccountEditorInfoView ll_editor_birthplace;
    @BindView(R.id.ll_editor_birthday)
    AccountEditorInfoView ll_editor_birthday;
    @BindView(R.id.ll_editor_marriage)
    AccountEditorInfoView ll_editor_marriage;
    @BindView(R.id.ll_editor_sex)
    AccountEditorInfoView ll_editor_sex;
    @BindView(R.id.ll_editor_country)
    AccountEditorInfoView ll_editor_country;
    @BindView(R.id.ll_editor_cardtype)
    AccountEditorInfoView ll_editor_cardtype;
    @BindView(R.id.ll_editor_cardid)
    AccountEditorInfoView ll_editor_cardid;
    @BindView(R.id.ll_editor_bank_account_area)
    AccountEditorInfoView ll_editor_bank_account_area;
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

    @Override
    protected void initView(View currentView) {
        ll_editor_nikename.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showNikenameType();
            }
        });
        // 获取中文姓名
        String chName = ll_editor_ch_name.getEditorContent();
        String egName = ll_editor_eg_name.getEditorContent();

        // 选择出生地区，默认香港
        List<String> birthplaceList = Arrays.asList(getResources().getStringArray(R.array.country_area));
        ll_editor_birthplace.setEditorContent(birthplaceList.get(3));
        ll_editor_birthplace.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showBirthplace(birthplaceList);
            }
        });
        // 选择出生日期
        ll_editor_birthday.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showBirthday();
            }
        });
        // 婚姻
        ll_editor_marriage.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showMarriage();
            }
        });
        // 性别
        ll_editor_sex.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showSex();
            }
        });
        // 国籍地区
        ll_editor_country.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showCountry(birthplaceList);
            }
        });
        // 国籍地区
        ll_editor_cardtype.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showCardtype();
            }
        });
        // 证件号码
        String cardid = ll_editor_cardid.getEditorContent();
        // 银行账户所属地区
        ll_editor_bank_account_area.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
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


    private int selectBankAccountArea = 0;

    /**
     * 银行账户所属地区
     */
    private void showBankAccountArea() {
        List<String> list = Arrays.asList(getResources().getStringArray(R.array.bank_account_area_type));
        SelectBirthplaceDialog dialog = new SelectBirthplaceDialog(this, selectBankAccountArea, list);
        dialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                selectBankAccountArea = object == null ? 3 : (int) object;
                String type = list.get(selectBankAccountArea);
                ll_editor_bank_account_area.setEditorContent(type);
            }
        });
        dialog.show();
    }

    /**
     * 校验身份证格式
     */
    private void regexCardId() {
        // 护照不校验
        if (selectCardType == 0) {
            boolean isCard = CardIdUtils.validateHKCard("香港身份证");
        } else if (selectCardType == 1) {
            boolean isCard = RegexUtils.isIDCard18("大陆身份证");
        }
    }

    /**
     * 证件类型
     */
    private int selectCardType = 0;

    /**
     * 选择证件类型
     */
    private void showCardtype() {
        // 根据选择国籍地区选择不同类型
        if (selectMineCountry == 3) {
            selectCardType = 0;
        } else if (selectMineCountry == 0) {
            selectCardType = 1;
        } else {
            selectCardType = 2;
        }
        List<String> list = Arrays.asList(getResources().getStringArray(R.array.card_type));
        SelectBirthplaceDialog dialog = new SelectBirthplaceDialog(this, selectCardType, list);
        dialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                selectCardType = object == null ? 0 : (int) object;
                String type = list.get(selectCardType);
                ll_editor_cardtype.setEditorContent(type);
            }
        });
        dialog.show();
    }

    /**
     * 默认第3个
     */
    private int selectMineCountry = 3;

    /**
     * 选择国籍地区
     */
    private void showCountry(List<String> birthplaceList) {
        SelectBirthplaceDialog dialog = new SelectBirthplaceDialog(this, selectMineCountry, birthplaceList);
        dialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                selectMineCountry = object == null ? 3 : (int) object;
                String type = birthplaceList.get(selectMineCountry);
                ll_editor_country.setEditorContent(type);

                // 根据选择国籍地区选择不同类型
                if (selectMineCountry == 3) {
                    selectCardType = 0;
                } else if (selectMineCountry == 0) {
                    selectCardType = 1;
                } else {
                    selectCardType = 2;
                }
                ll_editor_cardtype.setEditorContent(
                        Arrays.asList(getResources().getStringArray(R.array.card_type)).get(selectCardType));
            }
        });
        dialog.show();
    }

    private int selectSex = 0;

    /**
     * 选择性别
     */
    private void showSex() {
        List<String> sexList = Arrays.asList(getResources().getStringArray(R.array.sex_type));
        SelectBirthplaceDialog dialog = new SelectBirthplaceDialog(this, selectSex, sexList);
        dialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                selectSex = object == null ? 0 : (int) object;
                String type = sexList.get(selectSex);
                ll_editor_sex.setEditorContent(type);
            }
        });
        dialog.show();
    }

    private int selectMarriage = 0;

    /**
     * 选择婚姻
     */
    private void showMarriage() {
        List<String> marriageList = Arrays.asList(getResources().getStringArray(R.array.marriage_type));
        SelectBirthplaceDialog selectBirthplaceDialog = new SelectBirthplaceDialog(this, selectMarriage, marriageList);
        selectBirthplaceDialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                selectMarriage = object == null ? 0 : (int) object;
                String marriageType = marriageList.get(selectMarriage);
                ll_editor_marriage.setEditorContent(marriageType);
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
                ll_editor_birthday.setEditorContent(millis2String);
            }
        });
        selectBirthdayDialog.show();
    }

    /**
     * 默认第3个
     */
    private int selectBirthplace = 3;

    /**
     * 选择出生地区
     */
    private void showBirthplace(List<String> birthplaceList) {
        SelectBirthplaceDialog selectBirthplaceDialog = new SelectBirthplaceDialog(this, selectBirthplace, birthplaceList);
        selectBirthplaceDialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                selectBirthplace = object == null ? 3 : (int) object;
                String birthplace = birthplaceList.get(selectBirthplace);
                ll_editor_birthplace.setEditorContent(birthplace);
            }
        });
        selectBirthplaceDialog.show();
    }

    private int nikenameType = 0;

    /**
     * 选择称谓
     */
    private void showNikenameType() {
        List<String> nikenameList = Arrays.asList(getResources().getStringArray(R.array.nikename_type));
        SelectBirthplaceDialog selectBirthplaceDialog = new SelectBirthplaceDialog(this, nikenameType, nikenameList);
        selectBirthplaceDialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                nikenameType = object == null ? 0 : (int) object;
                String nameType = nikenameList.get(nikenameType);
                ll_editor_nikename.setEditorContent(nameType);
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
                saveToSP();
                finish();
                break;
            case R.id.tv_recognition:
                RecognitionActivity.startActivityResult(this);
                break;
            case R.id.tv_next:
                // 填写联系方式
                saveToSP();
                startActivity(new Intent(this, SecuritiesAccountContactActivity.class));
                break;
            default:
                break;
        }
    }

    /**
     * 保存到本地
     */
    private void saveToSP() {

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
