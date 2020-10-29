package com.mfinance.everjoy.everjoy.ui.mine.securities;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.RegexUtils;
import com.mfinance.everjoy.R;
import com.mfinance.everjoy.everjoy.base.BaseViewActivity;
import com.mfinance.everjoy.everjoy.dialog.SelectBirthplaceDialog;
import com.mfinance.everjoy.everjoy.dialog.impl.OnClickDialogOrFragmentViewListener;
import com.mfinance.everjoy.everjoy.ui.mine.ContactActivity;
import com.mfinance.everjoy.everjoy.utils.Contents;
import com.mfinance.everjoy.everjoy.view.AccountEditorInfoView;
import com.mfinance.everjoy.everjoy.view.AccountStepView;

import net.mfinance.commonlib.view.StringTextView;

import java.util.Arrays;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 开通大圣证券账户-填写联系方式/学历
 */
public class SecuritiesAccountContactActivity extends BaseViewActivity {

    @BindArray(R.array.array_edu)
    String[] array_edu;

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

    @Override
    protected void initView(View currentView) {
        String emailEditorContent = et_email.getText().toString();
        boolean email = RegexUtils.isEmail(emailEditorContent);
        String phoneEditorContent = ll_editor_phone.getEditorContent();
        String houseEditorContent = ll_editor_house.getEditorContent();
        String addressContent = et_address_content.getText().toString();

        ll_editor_edu.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showEdu();
            }
        });
        ll_editor_work.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showWork();
            }
        });
        String compEditorContent = ll_editor_comp.getEditorContent();
        String compAddressEditorContent = ll_editor_comp_address.getEditorContent();
        ll_editor_business.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showCompBusiness();
            }
        });
        ll_editor_profession.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showProfession();
            }
        });
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

    private int workingType = 0;

    /**
     * 选择工作年资
     */
    private void showWorking() {
        List<String> list = Arrays.asList(getResources().getStringArray(R.array.array_working));
        SelectBirthplaceDialog dialog = new SelectBirthplaceDialog(this, workingType, list);
        dialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                workingType = object == null ? 0 : (int) object;
                String content = list.get(workingType);
                ll_editor_working.setEditorContent(content);
            }
        });
        dialog.show();
    }

    private int professionType = 0;

    /**
     * 选择职业
     */
    private void showProfession() {
        List<String> list = Arrays.asList(getResources().getStringArray(R.array.array_profession));
        SelectBirthplaceDialog dialog = new SelectBirthplaceDialog(this, professionType, list);
        dialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                professionType = object == null ? 0 : (int) object;
                String content = list.get(professionType);
                ll_editor_profession.setEditorContent(content);
            }
        });
        dialog.show();
    }

    private int businessType = 0;

    /**
     * 选择商业性质
     */
    private void showCompBusiness() {
        List<String> list = Arrays.asList(getResources().getStringArray(R.array.array_business));
        SelectBirthplaceDialog dialog = new SelectBirthplaceDialog(this, businessType, list);
        dialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                businessType = object == null ? 0 : (int) object;
                String content = list.get(businessType);
                ll_editor_business.setEditorContent(content);
            }
        });
        dialog.show();
    }

    private int workType = 0;

    /**
     * 选择工作状态
     */
    private void showWork() {
        List<String> list = Arrays.asList(getResources().getStringArray(R.array.array_work));
        SelectBirthplaceDialog dialog = new SelectBirthplaceDialog(this, workType, list);
        dialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                workType = object == null ? 0 : (int) object;
                String content = list.get(workType);
                ll_editor_work.setEditorContent(content);
            }
        });
        dialog.show();
    }

    private int eduType = 0;

    /**
     * 选择教育程度
     */
    private void showEdu() {
        SelectBirthplaceDialog dialog = new SelectBirthplaceDialog(this, eduType, Arrays.asList(array_edu));
        dialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                eduType = object == null ? 0 : (int) object;
                String content = array_edu[eduType];
                ll_editor_edu.setEditorContent(content);
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
                // 非香港人一律录制2s视频，是香港人只需拍摄证件
//                RecordVideoActivity.startRecordVideoActivity(this, Contents.CARD_CH_TYPE);
                UploadCardActivity.startUploadCardActivity(this, Contents.CARD_CH_TYPE);
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
    }

}
