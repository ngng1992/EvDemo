package com.mfinance.everjoy.everjoy.ui.mine.securities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.everjoy.base.BaseViewActivity;
import com.mfinance.everjoy.everjoy.dialog.SelectCameraDialog;
import com.mfinance.everjoy.everjoy.dialog.impl.OnClickDialogOrFragmentViewListener;
import com.mfinance.everjoy.everjoy.pics.FileSelector;
import com.mfinance.everjoy.everjoy.pics.FileUtil;
import com.mfinance.everjoy.everjoy.pics.GlideShowUtils;
import com.mfinance.everjoy.everjoy.ui.mine.ContactActivity;
import com.mfinance.everjoy.everjoy.ui.mine.securities.photo.CaraIdHKActivity;
import com.mfinance.everjoy.everjoy.ui.mine.securities.photo.CardIdActivity;
import com.mfinance.everjoy.everjoy.ui.mine.securities.photo.CardIdZHActivity;
import com.mfinance.everjoy.everjoy.utils.Contents;

import net.mfinance.commonlib.permission.PermissionController;
import net.mfinance.commonlib.view.StringTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 上传证件
 * <p>
 * 1) 香港永久身份證: 身份證(正)*、身份證(反)*、住址證明*
 * 2) 內地: 身份證(正)*、身份證(反)*、住址證明*、謢照/港澳通行證(非必填)
 * 3) 外地: 謢照/港澳通行證*、住址證明*
 */
public class UploadCardActivity extends BaseViewActivity {

    public static void startUploadCardActivity(Activity activity, int cardType) {
        Intent intent = new Intent(activity, UploadCardActivity.class);
        intent.putExtra(Contents.CARD_TYPE, cardType);
        activity.startActivity(intent);
    }

    @BindView(R.id.ll_idcard)
    LinearLayout ll_idcard;
    @BindView(R.id.ll_other_passport)
    LinearLayout ll_other_passport;
    @BindView(R.id.iv_card_front)
    ImageView iv_card_front;
    @BindView(R.id.iv_card_background)
    ImageView iv_card_background;
    @BindView(R.id.ll_ch_passport)
    LinearLayout ll_ch_passport;
    @BindView(R.id.iv_other_passport)
    ImageView iv_other_passport;
    @BindView(R.id.iv_address_desc)
    ImageView iv_address_desc;
    @BindView(R.id.iv_passport)
    ImageView iv_passport;
    @BindView(R.id.tv_contact)
    TextView tv_contact;

    private int cardType;

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
        return R.layout.activity_upload_card;
    }

    @Override
    protected void initView(View currentView) {
        cardType = getIntent().getIntExtra(Contents.CARD_TYPE, Contents.CARD_HK_TYPE);
        if (cardType == Contents.CARD_HK_TYPE) {
            ll_idcard.setVisibility(View.VISIBLE);
            ll_other_passport.setVisibility(View.GONE);
            ll_ch_passport.setVisibility(View.GONE);
        } else if (cardType == Contents.CARD_CH_TYPE) {
            ll_idcard.setVisibility(View.VISIBLE);
            ll_other_passport.setVisibility(View.GONE);
            ll_ch_passport.setVisibility(View.VISIBLE);
        } else {
            ll_idcard.setVisibility(View.GONE);
            ll_other_passport.setVisibility(View.VISIBLE);
            ll_ch_passport.setVisibility(View.GONE);
        }

        // 在线咨询
        String verifMsg = getString(R.string.sec_acc_ui_contact);
        String target = verifMsg.substring(verifMsg.length() - 4);
        new StringTextView(tv_contact)
                .setStrText(verifMsg)
                .setColor(getResources().getColor(R.color.blue18))
                .setTextSize(1f)
                .setTargetText(target)
                .setUnderline(false)
                .setClick(true)
                .setOnClickSpannableStringListener(new StringTextView.OnClickSpannableStringListener() {
                    @Override
                    public void onClickSpannableString(View view) {
                        startActivity(new Intent(UploadCardActivity.this, ContactActivity.class));
                    }
                })
                .create();
    }

    /**
     * 身份证正面1
     * 身份证反面2
     * 地址3
     * 外地护照4
     * 护照5
     */
    private int address_passport = 0;

    @OnClick({R.id.iv_back, R.id.iv_card_front, R.id.iv_card_background, R.id.iv_other_passport, R.id.iv_address_desc, R.id.iv_passport,
            R.id.tv_prev, R.id.tv_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.iv_card_front:
                if (cardType == Contents.CARD_HK_TYPE) {
                    showIDHKCard(true);
                } else if (cardType == Contents.CARD_CH_TYPE) {
                    showIDZHCard(true);
                }
                address_passport = 1;
                break;
            case R.id.iv_card_background:
                if (cardType == Contents.CARD_HK_TYPE) {
                    showIDHKCard(false);
                } else if (cardType == Contents.CARD_CH_TYPE) {
                    showIDZHCard(false);
                }
                address_passport = 2;
                break;
            case R.id.iv_address_desc:
                address_passport = 3;
                showAddressPassport();
                break;
            case R.id.iv_other_passport:
                address_passport = 4;
                showAddressPassport();
                break;
            case R.id.iv_passport:
                address_passport = 5;
                showAddressPassport();
                break;
            case R.id.tv_prev:
                onBackPressed();
                break;
            case R.id.tv_next:
                // 財務/投資經驗
                saveToSP();
                startActivity(new Intent(this, FinanceActivity.class));
                break;
            default:
                break;
        }
    }

    private void showAddressPassport() {
        SelectCameraDialog selectCameraDialog = new SelectCameraDialog(this);
        selectCameraDialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                int index = (int) object;
                PermissionController permissionController = new PermissionController(UploadCardActivity.this);
                permissionController.setPermissions(PermissionController.CAMERA_STORAGE);
                permissionController.setOnPermissionListener(new PermissionController.OnPermissionListener() {
                    @Override
                    public void onHasPermission(boolean hasPermission) {
                        if (hasPermission) {
                            if (index == 1) {
                                CardIdActivity.openCardIdActivity(UploadCardActivity.this);
                            } else {
                                FileSelector.selectImage(UploadCardActivity.this);

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

    private void showIDZHCard(boolean isFront) {
        SelectCameraDialog selectCameraDialog = new SelectCameraDialog(this);
        selectCameraDialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                int index = (int) object;
                PermissionController permissionController = new PermissionController(UploadCardActivity.this);
                permissionController.setPermissions(PermissionController.CAMERA_STORAGE);
                permissionController.setOnPermissionListener(new PermissionController.OnPermissionListener() {
                    @Override
                    public void onHasPermission(boolean hasPermission) {
                        if (hasPermission) {
                            if (index == 1) {
                                // ZH UI
                                CardIdZHActivity.openCertificateCamera(UploadCardActivity.this,
                                        isFront ? CardIdZHActivity.TYPE_IDCARD_FRONT : CardIdZHActivity.TYPE_IDCARD_BACK);
                            } else {
                                FileSelector.selectImage(UploadCardActivity.this);

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

    private void showIDHKCard(boolean isFront) {
        SelectCameraDialog selectCameraDialog = new SelectCameraDialog(this);
        selectCameraDialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                int index = (int) object;
                PermissionController permissionController = new PermissionController(UploadCardActivity.this);
                permissionController.setPermissions(PermissionController.CAMERA_STORAGE);
                permissionController.setOnPermissionListener(new PermissionController.OnPermissionListener() {
                    @Override
                    public void onHasPermission(boolean hasPermission) {
                        if (hasPermission) {
                            if (index == 1) {
                                // HK UI
                                CaraIdHKActivity.openCertificateCamera(UploadCardActivity.this,
                                        isFront ? CaraIdHKActivity.TYPE_IDCARD_FRONT : CardIdZHActivity.TYPE_IDCARD_BACK);
                            } else {
                                FileSelector.selectImage(UploadCardActivity.this);
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
        if (requestCode == CardIdZHActivity.REQUEST_CODE && resultCode == CardIdZHActivity.RESULT_CODE) {
            //获取文件路径，显示图片
            if (data != null) {
                String path = "";
                if (cardType == Contents.CARD_HK_TYPE) {
                    path = CaraIdHKActivity.getResult(data);
                } else if (cardType == Contents.CARD_CH_TYPE) {
                    path = CardIdZHActivity.getResult(data);
                }
                if (!TextUtils.isEmpty(path)) {
                    if (address_passport == 1) {
                        GlideShowUtils.showImage(this, iv_card_front, path);
                    } else if (address_passport == 2) {
                        GlideShowUtils.showImage(this, iv_card_background, path);
                    }
                }
            }
        } else if (requestCode == Contents.KEY_CAMERA_REQUEST_CODE && resultCode == Contents.KEY_CAMERA_RESULT_OK) {
            if (data != null) {
                String path = data.getStringExtra(Contents.KEY_FILE_PATH);
                if (!TextUtils.isEmpty(path)) {
                    if (address_passport == 4) {
                        GlideShowUtils.showImage(this, iv_other_passport, path);
                    } else if (address_passport == 3) {
                        GlideShowUtils.showImage(this, iv_address_desc, path);
                    } else if (address_passport == 5) {
                        GlideShowUtils.showImage(this, iv_passport, path);
                    }
                }
            }
        } else if (requestCode == Contents.REQUEST_CODE_SELECT_PHOTO) {
            if (data != null) {
                Uri uri = data.getData();
                if (uri != null) {
                    String path = FileUtil.getFilePathByUri(this, uri);
                    Log.e("rec", "从相册选择图片路径：" + path);
                    if (!TextUtils.isEmpty(path)) {
                        if (address_passport == 1) {
                            GlideShowUtils.showImage(this, iv_card_front, path);
                        } else if (address_passport == 2) {
                            GlideShowUtils.showImage(this, iv_card_background, path);
                        } else if (address_passport == 4) {
                            GlideShowUtils.showImage(this, iv_other_passport, path);
                        } else if (address_passport == 3) {
                            GlideShowUtils.showImage(this, iv_address_desc, path);
                        } else if (address_passport == 5) {
                            GlideShowUtils.showImage(this, iv_passport, path);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        goBack();
    }

    private void goBack() {
        if (cardType == Contents.CARD_HK_TYPE) {
            saveToSP();
            finish();
        } else {
            RecordVideoActivity.startRecordVideoActivity(this, cardType);
        }
    }

    /**
     * 保存到本地
     */
    private void saveToSP() {

    }
}
