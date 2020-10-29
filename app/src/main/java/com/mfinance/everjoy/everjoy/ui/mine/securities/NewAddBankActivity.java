package com.mfinance.everjoy.everjoy.ui.mine.securities;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.everjoy.base.BaseViewActivity;
import com.mfinance.everjoy.everjoy.dialog.SelectBirthplaceDialog;
import com.mfinance.everjoy.everjoy.dialog.SelectCameraDialog;
import com.mfinance.everjoy.everjoy.dialog.impl.OnClickDialogOrFragmentViewListener;
import com.mfinance.everjoy.everjoy.pics.FileSelector;
import com.mfinance.everjoy.everjoy.pics.FileUtil;
import com.mfinance.everjoy.everjoy.pics.GlideShowUtils;
import com.mfinance.everjoy.everjoy.ui.mine.securities.photo.CardIdZHActivity;
import com.mfinance.everjoy.everjoy.utils.Contents;
import com.mfinance.everjoy.everjoy.view.AccountEditorInfoView;

import net.mfinance.commonlib.permission.PermissionController;
import net.mfinance.commonlib.toast.ToastUtils;

import java.util.Arrays;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 新增银行卡
 */
public class NewAddBankActivity extends BaseViewActivity {

    @BindArray(R.array.array_bank_name)
    String[] array_bank_name;
    @BindArray(R.array.array_bank_type)
    String[] array_bank_type;

    @BindView(R.id.ll_bank_name)
    AccountEditorInfoView ll_bank_name;
    @BindView(R.id.ll_bank_code)
    AccountEditorInfoView ll_bank_code;
    @BindView(R.id.ll_bank_type)
    AccountEditorInfoView ll_bank_type;
    @BindView(R.id.iv_bank)
    ImageView iv_bank;

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
        return R.layout.activity_new_add_bank;
    }

    @Override
    protected void initView(View currentView) {
        ll_bank_name.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showBankName();
            }
        });
        String editorContent = ll_bank_code.getEditorContent();
        ll_bank_type.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showBankType();
            }
        });
    }


    private int selectBankTypeIndex = 0;

    private void showBankType() {
        SelectBirthplaceDialog selectBirthplaceDialog = new SelectBirthplaceDialog(this, selectBankTypeIndex, Arrays.asList(array_bank_type));
        selectBirthplaceDialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                selectBankTypeIndex = object == null ? 0 : (int) object;
                String nameType = array_bank_type[selectBankTypeIndex];
                ll_bank_type.setEditorContent(nameType);
            }
        });
        selectBirthplaceDialog.show();
    }

    private int selectBankNameIndex = 0;

    private void showBankName() {
        SelectBirthplaceDialog selectBirthplaceDialog = new SelectBirthplaceDialog(this, selectBankNameIndex, Arrays.asList(array_bank_name));
        selectBirthplaceDialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                selectBankNameIndex = object == null ? 0 : (int) object;
                String nameType = array_bank_name[selectBankNameIndex];
                ll_bank_name.setEditorContent(nameType);
            }
        });
        selectBirthplaceDialog.show();
    }

    @OnClick({R.id.iv_back, R.id.tv_camera, R.id.tv_start})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_camera:
                showCamera();
                break;
            case R.id.tv_start:
                startActivity(new Intent(this, DepositFundsActivity.class));
                break;
            default:
                break;
        }
    }

    private void showCamera() {
        SelectCameraDialog selectCameraDialog = new SelectCameraDialog(this);
        selectCameraDialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                int index = (int) object;
                PermissionController permissionController = new PermissionController(NewAddBankActivity.this);
                permissionController.setPermissions(PermissionController.CAMERA_STORAGE);
                permissionController.setOnPermissionListener(new PermissionController.OnPermissionListener() {
                    @Override
                    public void onHasPermission(boolean hasPermission) {
                        if (hasPermission) {
                            if (index == 1) {
                                CardIdZHActivity.openCertificateCamera(NewAddBankActivity.this, CardIdZHActivity.TYPE_OTHER_LANDSCAPE);
                            } else {
                                FileSelector.selectImage(NewAddBankActivity.this);

//                                FileSelector.selectImage(RecognitionActivity.this, new OnResultCallbackListener<LocalMedia>() {
//                                    @Override
//                                    public void onResult(List<LocalMedia> result) {
//                                        LocalMedia localMedia = result.get(0);
//                                        if (localMedia == null) {
//                                            return;
//                                        }
//                                        // TODO 取压缩的路径的
//                                        String path = localMedia.getCompressPath();
//                                    }
//
//                                    @Override
//                                    public void onCancel() {
//                                        Log.e("image", "onCancel ========= ");
//                                    }
//                                });
                            }
                        }
                    }
                });
                permissionController.requestPermission();
            }
        });
        selectCameraDialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String filePath = null;
        if (resultCode == RESULT_OK && requestCode == Contents.REQUEST_CODE_SELECT_PHOTO) {
            if (data != null) {
                Uri uri = data.getData();
                if (uri != null) {
                    filePath = FileUtil.getFilePathByUri(this, uri);
                    Log.e("rec", "从相册选择图片路径：" + filePath);
                }
            }
        }
        if (requestCode == CardIdZHActivity.REQUEST_CODE && resultCode == CardIdZHActivity.RESULT_CODE) {
            // 获取文件路径，显示图片
            filePath = CardIdZHActivity.getResult(data);
            Log.e("rec", "图片路径：" + filePath);
        }
        if (!TextUtils.isEmpty(filePath)) {
            GlideShowUtils.showImage(this, iv_bank, filePath);
        }
    }
}
