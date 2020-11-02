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
import com.mfinance.everjoy.everjoy.config.Constants;
import com.mfinance.everjoy.everjoy.dialog.SelectCameraDialog;
import com.mfinance.everjoy.everjoy.dialog.impl.OnClickDialogOrFragmentViewListener;
import com.mfinance.everjoy.everjoy.pics.FileSelector;
import com.mfinance.everjoy.everjoy.pics.FileUtil;
import com.mfinance.everjoy.everjoy.pics.GlideShowUtils;
import com.mfinance.everjoy.everjoy.sp.SecuritiesSharedPUtils;
import com.mfinance.everjoy.everjoy.ui.mine.ContactActivity;
import com.mfinance.everjoy.everjoy.ui.mine.securities.photo.CaraIdHKActivity;
import com.mfinance.everjoy.everjoy.ui.mine.securities.photo.CardIdActivity;
import com.mfinance.everjoy.everjoy.ui.mine.securities.photo.CardIdZHActivity;

import net.mfinance.commonlib.permission.PermissionController;
import net.mfinance.commonlib.toast.ToastUtils;
import net.mfinance.commonlib.view.StringTextView;

import java.io.File;

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
        intent.putExtra(Constants.CARD_TYPE, cardType);
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
        cardType = getIntent().getIntExtra(Constants.CARD_TYPE, Constants.CARD_HK_TYPE);
        if (cardType == Constants.CARD_HK_TYPE) {
            ll_idcard.setVisibility(View.VISIBLE);
            ll_other_passport.setVisibility(View.GONE);
            ll_ch_passport.setVisibility(View.GONE);
        } else if (cardType == Constants.CARD_CH_TYPE) {
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
                if (cardType == Constants.CARD_HK_TYPE) {
                    showIDHKCard(true);
                } else if (cardType == Constants.CARD_CH_TYPE) {
                    showIDZHCard(true);
                }
                address_passport = 1;
                break;
            case R.id.iv_card_background:
                if (cardType == Constants.CARD_HK_TYPE) {
                    showIDHKCard(false);
                } else if (cardType == Constants.CARD_CH_TYPE) {
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
                if (cardType == Constants.CARD_HK_TYPE) {
                    if (TextUtils.isEmpty(imgHkIdFront)) {
                        ToastUtils.showToast(this, R.string.toast_pls_upload_cardid_front);
                        return;
                    }
                    if (TextUtils.isEmpty(imgHkIdBack)) {
                        ToastUtils.showToast(this, R.string.toast_pls_upload_cardid_back);
                        return;
                    }
                    if (TextUtils.isEmpty(imgProofAddress)) {
                        ToastUtils.showToast(this, R.string.toast_pls_upload_imgproofaddress);
                        return;
                    }
                } else if (cardType == Constants.CARD_CH_TYPE) {
                    if (TextUtils.isEmpty(imgHkIdFront)) {
                        ToastUtils.showToast(this, R.string.toast_pls_upload_cardid_front);
                        return;
                    }
                    if (TextUtils.isEmpty(imgHkIdBack)) {
                        ToastUtils.showToast(this, R.string.toast_pls_upload_cardid_back);
                        return;
                    }
                    if (TextUtils.isEmpty(imgProofAddress)) {
                        ToastUtils.showToast(this, R.string.toast_pls_upload_imgproofaddress);
                        return;
                    }
                } else if (cardType == Constants.CARD_OTHER_TYPE) {
                    if (TextUtils.isEmpty(imgPassport) || TextUtils.isEmpty(imgHkMacauPass)) {
                        ToastUtils.showToast(this, R.string.toast_pls_upload_imghkmacaupass);
                        return;
                    }
                    if (TextUtils.isEmpty(imgProofAddress)) {
                        ToastUtils.showToast(this, R.string.toast_pls_upload_imgproofaddress);
                        return;
                    }
                }
                saveToSP();
                // 財務/投資經驗
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

    private String imgHkIdFront;
    private String imgHkIdFrontFileName;
    private String imgHkIdBack;
    private String imgHkIdBackFileName;

    private String imgMainlandIdFront;
    private String imgMainlandIdFrontFileName;
    private String imgMainlandIdBack;
    private String imgMainlandIdBackFileName;

    // 护照或港澳通行证
    private String imgPassport;
    private String imgPassportFileName;
    private String imgHkMacauPass;
    private String imgHkMacauPassFileName;

    private String imgProofAddress;
    private String imgProofAddressFileName;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CaraIdHKActivity.REQUEST_CODE && resultCode == CaraIdHKActivity.RESULT_CODE) {
            // 香港证件
            if (data != null) {
                // 身份证正面
                if (address_passport == 1) {
                    imgHkIdFront = data.getStringExtra("imgPath");
                    imgHkIdFrontFileName = data.getStringExtra("imgName");
                    GlideShowUtils.showImage(this, iv_card_front, imgHkIdFront);
                } else if (address_passport == 2) {
                    imgHkIdBack = data.getStringExtra("imgPath");
                    imgHkIdBackFileName = data.getStringExtra("imgName");
                    GlideShowUtils.showImage(this, iv_card_background, imgHkIdBack);
                }
            }
        } else if (requestCode == CardIdZHActivity.REQUEST_CODE && resultCode == CardIdZHActivity.RESULT_CODE) {
            if (data != null) {
                // 大陆身份证
                if (address_passport == 1) {
                    imgMainlandIdFront = data.getStringExtra("imgPath");
                    imgMainlandIdFrontFileName = data.getStringExtra("imgName");
                    GlideShowUtils.showImage(this, iv_card_front, imgMainlandIdFront);
                } else if (address_passport == 2) {
                    imgMainlandIdBack = data.getStringExtra("imgPath");
                    imgMainlandIdBackFileName = data.getStringExtra("imgName");
                    GlideShowUtils.showImage(this, iv_card_background, imgMainlandIdBack);
                }
            }
        } else if (requestCode == Constants.KEY_CAMERA_REQUEST_CODE && resultCode == Constants.KEY_CAMERA_RESULT_OK) {
            if (data != null) {
                String imgPath = data.getStringExtra("imgPath");
                String imgName = data.getStringExtra("imgName");
                if (imgPath != null) {
                    if (address_passport == 4) {
                        // 外地: 謢照/港澳通行證*
                        imgPassport = imgPath;
                        imgPassportFileName = imgName;
                        GlideShowUtils.showImage(this, iv_other_passport, imgPath);
                    } else if (address_passport == 3) {
                        imgProofAddress = imgPath;
                        imgProofAddressFileName = imgName;
                        GlideShowUtils.showImage(this, iv_address_desc, imgPath);
                    } else if (address_passport == 5) {
                        // 謢照/港澳通行證(非必填)
                        imgPassport = imgPath;
                        imgPassportFileName = imgName;
                        imgHkMacauPass = imgPath;
                        imgHkMacauPassFileName = imgName;
                        GlideShowUtils.showImage(this, iv_passport, imgPath);
                    }
                }
            }
        } else if (requestCode == Constants.REQUEST_CODE_SELECT_PHOTO) {
            if (data != null) {
                Uri uri = data.getData();
                if (uri != null) {
                    String path = FileUtil.getFilePathByUri(this, uri);
                    if (path != null) {
                        String name = new File(path).getName();
                        Log.e("rec", "从相册选择图片路径：" + path);
                        if (address_passport == 1) {
                            if (cardType == Constants.CARD_HK_TYPE) {
                                imgHkIdFront = path;
                                imgHkIdFrontFileName = name;
                            } else if (cardType == Constants.CARD_CH_TYPE) {
                                imgMainlandIdFront = path;
                                imgMainlandIdFrontFileName = name;
                            }
                            GlideShowUtils.showImage(this, iv_card_front, path);
                        } else if (address_passport == 2) {
                            if (cardType == Constants.CARD_HK_TYPE) {
                                imgHkIdBack = path;
                                imgHkIdBackFileName = name;
                            } else if (cardType == Constants.CARD_CH_TYPE) {
                                imgMainlandIdBack = path;
                                imgMainlandIdBackFileName = name;
                            }
                            GlideShowUtils.showImage(this, iv_card_background, path);
                        } else if (address_passport == 4) {
                            imgPassport = path;
                            imgPassportFileName = name;
                            GlideShowUtils.showImage(this, iv_other_passport, path);
                        } else if (address_passport == 3) {
                            imgProofAddress = path;
                            imgProofAddressFileName = name;
                            GlideShowUtils.showImage(this, iv_address_desc, path);
                        } else if (address_passport == 5) {
                            imgPassport = path;
                            imgPassportFileName = name;
                            imgHkMacauPass = path;
                            imgHkMacauPassFileName = name;
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
        saveToSP();
        // 如果是拍摄香港证件照，直接返回，如果不是，需要返回拍摄视频页
        if (cardType != Constants.CARD_HK_TYPE) {
            RecordVideoActivity.startRecordVideoActivity(this, cardType);
        }
        finish();
    }

    /**
     * 保存到本地
     */
    private void saveToSP() {
        if (!TextUtils.isEmpty(imgHkIdFront)) {
            SecuritiesSharedPUtils.setImgHkIdFront(imgHkIdFront);
        }
        if (!TextUtils.isEmpty(imgHkIdFrontFileName)) {
            SecuritiesSharedPUtils.setImgHkIdFrontFileName(imgHkIdFrontFileName);
        }
        if (!TextUtils.isEmpty(imgHkIdBack)) {
            SecuritiesSharedPUtils.setImgHkIdBack(imgHkIdBack);
        }
        if (!TextUtils.isEmpty(imgHkIdBackFileName)) {
            SecuritiesSharedPUtils.setImgHkIdBackFileName(imgHkIdBackFileName);
        }

        if (!TextUtils.isEmpty(imgMainlandIdFront)) {
            SecuritiesSharedPUtils.setImgMainlandIdFront(imgMainlandIdFront);
        }
        if (!TextUtils.isEmpty(imgMainlandIdFrontFileName)) {
            SecuritiesSharedPUtils.setImgMainlandIdFrontFileName(imgMainlandIdFrontFileName);
        }
        if (!TextUtils.isEmpty(imgMainlandIdBack)) {
            SecuritiesSharedPUtils.setImgMainlandIdBack(imgMainlandIdBack);
        }
        if (!TextUtils.isEmpty(imgMainlandIdBackFileName)) {
            SecuritiesSharedPUtils.setImgMainlandIdBackFileName(imgMainlandIdBackFileName);
        }

        if (!TextUtils.isEmpty(imgPassport)) {
            SecuritiesSharedPUtils.setImgPassport(imgPassport);
        }
        if (!TextUtils.isEmpty(imgPassportFileName)) {
            SecuritiesSharedPUtils.setImgPassportFileName(imgPassportFileName);
        }

        if (!TextUtils.isEmpty(imgHkMacauPass)) {
            SecuritiesSharedPUtils.setImgHkMacauPass(imgHkMacauPass);
        }
        if (!TextUtils.isEmpty(imgHkMacauPassFileName)) {
            SecuritiesSharedPUtils.setImgHkMacauPassFileName(imgHkMacauPassFileName);
        }

        if (!TextUtils.isEmpty(imgProofAddress)) {
            SecuritiesSharedPUtils.setImgProofAddress(imgProofAddress);
        }
        if (!TextUtils.isEmpty(imgProofAddressFileName)) {
            SecuritiesSharedPUtils.setImgProofAddressFileName(imgProofAddressFileName);
        }
    }
}
