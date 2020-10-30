package com.mfinance.everjoy.everjoy.ui.mine.securities;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.RegexUtils;
import com.mfinance.everjoy.R;
import com.mfinance.everjoy.everjoy.base.BaseViewActivity;
import com.mfinance.everjoy.everjoy.config.Constants;
import com.mfinance.everjoy.everjoy.dialog.SelectBirthplaceDialog;
import com.mfinance.everjoy.everjoy.dialog.impl.OnClickDialogOrFragmentViewListener;
import com.mfinance.everjoy.everjoy.sp.SecuritiesSharedPUtils;
import com.mfinance.everjoy.everjoy.ui.mine.ContactActivity;
import com.mfinance.everjoy.everjoy.view.AccountEditorInfoView;
import com.mfinance.everjoy.everjoy.view.AccountStepView;

import net.mfinance.commonlib.toast.ToastUtils;
import net.mfinance.commonlib.view.StringTextView;

import java.util.Arrays;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 开通大圣证券账户-填写联系方式/学历
 */
public class SecuritiesAccountContactActivity extends BaseViewActivity {

    @BindArray(R.array.array_edu)
    String[] array_edu;
    @BindArray(R.array.array_work)
    String[] array_work;
    @BindArray(R.array.array_work_type)
    String[] array_work_type;
    @BindArray(R.array.array_work_pos)
    String[] array_work_pos;
    @BindArray(R.array.array_work_year)
    String[] array_work_year;

    @BindView(R.id.ll_account_step)
    AccountStepView llAccountStep;
    @BindView(R.id.et_email)
    EditText et_email;
    @BindView(R.id.ll_editor_phone)
    AccountEditorInfoView ll_editor_phone;
    @BindView(R.id.ll_editor_house)
    AccountEditorInfoView ll_editor_house;
    @BindView(R.id.et_address_content)
    EditText et_address_content;
    @BindView(R.id.ll_editor_edu)
    AccountEditorInfoView ll_editor_edu;
    @BindView(R.id.ll_editor_work)
    AccountEditorInfoView ll_editor_work;
    @BindView(R.id.ll_editor_comp)
    AccountEditorInfoView ll_editor_comp;
    @BindView(R.id.ll_editor_comp_address)
    AccountEditorInfoView ll_editor_comp_address;
    @BindView(R.id.ll_editor_comp_phone)
    AccountEditorInfoView ll_editor_comp_phone;
    @BindView(R.id.ll_editor_business)
    AccountEditorInfoView ll_editor_business;
    @BindView(R.id.ll_editor_profession)
    AccountEditorInfoView ll_editor_profession;
    @BindView(R.id.ll_editor_working)
    AccountEditorInfoView ll_editor_working;
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
        return R.layout.activity_securities_account_contact;
    }

    /**
     * 保存到本地
     */
    private void saveToSP() {
        String emailEditorContent = et_email.getText().toString();
        SecuritiesSharedPUtils.setPersEmailn(emailEditorContent);

        String phoneEditorContent = ll_editor_phone.getEditorContent();
        SecuritiesSharedPUtils.setPersTelNo(phoneEditorContent);

        String houseEditorContent = ll_editor_house.getEditorContent();
        SecuritiesSharedPUtils.setPersAddress(houseEditorContent);

        String persMailAddress = et_address_content.getText().toString();
        SecuritiesSharedPUtils.setPersMailAddress(persMailAddress);

        if (persEducationLevelIndex != -1) {
            SecuritiesSharedPUtils.setPersEducationLevel(persEducationLevelIndex);
        }
        String eduEditorContent = ll_editor_edu.getEditorContent();
        SecuritiesSharedPUtils.setPersEducationLevelOther(eduEditorContent);

        SecuritiesSharedPUtils.setWorkState(workStateIndex);

        String compEditorContent = ll_editor_comp.getEditorContent();
        if (!TextUtils.isEmpty(compEditorContent)) {
            SecuritiesSharedPUtils.setWorkEmployerName(compEditorContent);
        }

        String compAddressEditorContent = ll_editor_comp_address.getEditorContent();
        if (!TextUtils.isEmpty(compAddressEditorContent)) {
            SecuritiesSharedPUtils.setWorkAddress(compAddressEditorContent);
        }

        String compPhoneEditorContent = ll_editor_comp_phone.getEditorContent();
        if (!TextUtils.isEmpty(compPhoneEditorContent)) {
            SecuritiesSharedPUtils.setWorkTel(compPhoneEditorContent);
        }

        if (workTypeIndex != -1) {
            SecuritiesSharedPUtils.setWorkType(workTypeIndex);
        }

        if (workPosIndex != -1) {
            SecuritiesSharedPUtils.setWorkPos(workPosIndex);
        }

        if (workYearIndex != -1) {
            SecuritiesSharedPUtils.setWorkYeas(workYearIndex);
        }

    }

    @Override
    protected void initView(View currentView) {
        String persEmail = SecuritiesSharedPUtils.getPersEmail();
        if (!TextUtils.isEmpty(persEmail)) {
            et_email.setText(persEmail);
        }
        String persTelNo = SecuritiesSharedPUtils.getPersTelNo();
        if (!TextUtils.isEmpty(persTelNo)) {
            ll_editor_phone.setEditorContent(persTelNo);
        }
        String persAddress = SecuritiesSharedPUtils.getPersAddress();
        if (!TextUtils.isEmpty(persAddress)) {
            ll_editor_house.setEditorContent(persAddress);
        }
        String persMailAddress = SecuritiesSharedPUtils.getPersMailAddress();
        if (!TextUtils.isEmpty(persMailAddress)) {
            et_address_content.setText(persMailAddress);
        }

        // 教育程度
        persEducationLevelIndex = SecuritiesSharedPUtils.getPersEducationLevel();
        if (persEducationLevelIndex == 4) {
            String persEducationLevelOther = SecuritiesSharedPUtils.getPersEducationLevelOther();
            ll_editor_edu.setEditorContent(persEducationLevelOther);
        } else {
            if (persEducationLevelIndex != -1) {
                ll_editor_edu.setEditorContent(array_edu[persEducationLevelIndex]);
            }
        }
        ll_editor_edu.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showEdu();
            }
        });

        workStateIndex = SecuritiesSharedPUtils.getWorkState();
        ll_editor_work.setEditorContent(array_work[workStateIndex]);
        ll_editor_work.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showWork();
            }
        });

        String workEmployerName = SecuritiesSharedPUtils.getWorkEmployerName();
        if (!TextUtils.isEmpty(workEmployerName)) {
            ll_editor_comp.setEditorContent(workEmployerName);
        }

        String workAddress = SecuritiesSharedPUtils.getWorkAddress();
        if (!TextUtils.isEmpty(workAddress)) {
            ll_editor_comp_address.setEditorContent(workAddress);
        }

        String workTel = SecuritiesSharedPUtils.getWorkTel();
        if (!TextUtils.isEmpty(workTel)) {
            ll_editor_comp_phone.setEditorContent(workTel);
        }

        workTypeIndex = SecuritiesSharedPUtils.getWorkType();
        if (workTypeIndex != -1) {
            ll_editor_business.setEditorContent(array_work_type[workTypeIndex]);
        }
        ll_editor_business.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showCompBusiness();
            }
        });

        workPosIndex = SecuritiesSharedPUtils.getWorkPos();
        if (workPosIndex != -1) {
            ll_editor_profession.setEditorContent(array_work_pos[workPosIndex]);
        }
        ll_editor_profession.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showProfession();
            }
        });

        workYearIndex = SecuritiesSharedPUtils.getWorkYear();
        if (workYearIndex != -1) {
            ll_editor_working.setEditorContent(array_work_year[workYearIndex]);
        }
        ll_editor_working.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showWorking();
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
                        startActivity(new Intent(SecuritiesAccountContactActivity.this, ContactActivity.class));
                    }
                })
                .create();
    }

    private int workYearIndex = -1;

    /**
     * 选择工作年资
     */
    private void showWorking() {
        SelectBirthplaceDialog dialog = new SelectBirthplaceDialog(this, workYearIndex, Arrays.asList(array_work_year));
        dialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                workYearIndex = object == null ? 0 : (int) object;
                String content = array_work_year[workYearIndex];
                ll_editor_working.setEditorContent(content);
            }
        });
        dialog.show();
    }

    private int workPosIndex = -1;

    /**
     * 选择职业
     */
    private void showProfession() {
        SelectBirthplaceDialog dialog = new SelectBirthplaceDialog(this, workPosIndex, Arrays.asList(array_work_pos));
        dialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                workPosIndex = object == null ? 0 : (int) object;
                String content = array_work_pos[workPosIndex];
                ll_editor_profession.setEditorContent(content);
            }
        });
        dialog.show();
    }

    private int workTypeIndex = -1;

    /**
     * 选择业务性质
     */
    private void showCompBusiness() {
        SelectBirthplaceDialog dialog = new SelectBirthplaceDialog(this, workTypeIndex, Arrays.asList(array_work_type));
        dialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                workTypeIndex = object == null ? 0 : (int) object;
                String content = array_work_type[workTypeIndex];
                ll_editor_business.setEditorContent(content);
            }
        });
        dialog.show();
    }

    // 默认第一个
    private int workStateIndex = 0;

    /**
     * 选择工作状态
     */
    private void showWork() {
        SelectBirthplaceDialog dialog = new SelectBirthplaceDialog(this, workStateIndex, Arrays.asList(array_work));
        dialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                workStateIndex = object == null ? 0 : (int) object;
                String content = array_work[workStateIndex];
                ll_editor_work.setEditorContent(content);
            }
        });
        dialog.show();
    }

    private int persEducationLevelIndex = -1;

    /**
     * 选择教育程度
     */
    private void showEdu() {
        SelectBirthplaceDialog dialog = new SelectBirthplaceDialog(this, persEducationLevelIndex, Arrays.asList(array_edu));
        dialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                persEducationLevelIndex = object == null ? 0 : (int) object;
                if (persEducationLevelIndex == 4) {
                    ll_editor_edu.setEditor(true);
                } else {
                    ll_editor_edu.setEditor(false);
                    String content = array_edu[persEducationLevelIndex];
                    ll_editor_edu.setEditorContent(content);
                }
            }
        });
        dialog.show();
    }

    @OnClick({R.id.iv_back, R.id.tv_address_compare, R.id.tv_prev, R.id.tv_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_address_compare:
                String houseEditorContent = ll_editor_house.getEditorContent();
                et_address_content.setText(houseEditorContent);
                break;
            case R.id.tv_prev:
                onBackPressed();
                break;
            case R.id.tv_next:
                String emailEditorContent = et_email.getText().toString();
                if (TextUtils.isEmpty(emailEditorContent)) {
                    ToastUtils.showToast(this, et_email.getHint().toString());
                    return;
                }
                if (!RegexUtils.isEmail(emailEditorContent)) {
                    ToastUtils.showToast(this, R.string.str_email_rule_error);
                    return;
                }
                String phoneEditorContent = ll_editor_phone.getEditorContent();
                if (TextUtils.isEmpty(phoneEditorContent)) {
                    ToastUtils.showToast(this, ll_editor_phone.getHintContentForTip());
                    return;
                }
                houseEditorContent = ll_editor_house.getEditorContent();
                if (TextUtils.isEmpty(houseEditorContent)) {
                    ToastUtils.showToast(this, ll_editor_house.getHintContentForTip());
                    return;
                }
                String persMailAddress = et_address_content.getText().toString();
                if (TextUtils.isEmpty(persMailAddress)) {
                    ToastUtils.showToast(this, et_address_content.getHint().toString());
                    return;
                }
                String persEducationLevel = ll_editor_edu.getEditorContent();
                if (TextUtils.isEmpty(persEducationLevel)) {
                    ToastUtils.showToast(this, ll_editor_edu.getLeftAndHintContentForTip());
                    return;
                }
                String workEditorContent = ll_editor_work.getEditorContent();
                if (TextUtils.isEmpty(workEditorContent)) {
                    ToastUtils.showToast(this, ll_editor_work.getLeftAndHintContentForTip());
                    return;
                }

                saveToSP();
                // 非香港人一律录制2s视频，拍摄证件照，香港人只需拍摄证件
                int persIdTypeIndex = SecuritiesSharedPUtils.getPersIdType();
                if (persIdTypeIndex == 0) {
                    UploadCardActivity.startUploadCardActivity(this, Constants.CARD_HK_TYPE);
                } else if (persIdTypeIndex == 1) {
                    RecordVideoActivity.startRecordVideoActivity(this, Constants.CARD_CH_TYPE);
                } else if (persIdTypeIndex == 2) {
                    RecordVideoActivity.startRecordVideoActivity(this, Constants.CARD_OTHER_TYPE);
                }
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
    }

}
