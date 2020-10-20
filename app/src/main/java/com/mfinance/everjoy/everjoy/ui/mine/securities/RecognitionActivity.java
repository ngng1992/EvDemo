package com.mfinance.everjoy.everjoy.ui.mine.securities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.everjoy.base.BaseViewActivity;
import com.mfinance.everjoy.everjoy.dialog.SelectBirthplaceDialog;
import com.mfinance.everjoy.everjoy.dialog.SelectCameraDialog;
import com.mfinance.everjoy.everjoy.dialog.impl.OnClickDialogOrFragmentViewListener;
import com.mfinance.everjoy.everjoy.pics.FileSelector;
import com.mfinance.everjoy.everjoy.pics.FileUtil;
import com.mfinance.everjoy.everjoy.pics.GlideShowUtils;
import com.mfinance.everjoy.everjoy.ui.mine.certificate.CaraIdHKActivity;
import com.mfinance.everjoy.everjoy.ui.mine.certificate.CardIdZHActivity;
import com.mfinance.everjoy.everjoy.view.AccountEditorInfoView;

import net.mfinance.chatlib.utils.ConfigUtils;
import net.mfinance.commonlib.permission.PermissionController;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 自动识别
 */
public class RecognitionActivity extends BaseViewActivity {

    public static final int REQUEST_CODE_RECOGNITION = 100;

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.ll_editor_cardtype)
    AccountEditorInfoView llEditorCardtype;
    @BindView(R.id.iv_card_front)
    ImageView ivCardFront;
    @BindView(R.id.iv_card_background)
    ImageView ivCardBackground;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;

    public static void startActivityResult(Activity context) {
        Intent intent = new Intent(context, RecognitionActivity.class);
        context.startActivityForResult(intent, REQUEST_CODE_RECOGNITION);
    }

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
        return R.layout.activity_recognition;
    }

    @Override
    protected void initView(View currentView) {
        // 测试初始化
        ConfigUtils.initFileConfig();


        List<String> list = Arrays.asList(getResources().getStringArray(R.array.card_type2));
        llEditorCardtype.setEditorContent(list.get(0));
        llEditorCardtype.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                showCardtype(list);
            }
        });
    }

    /**
     * 证件类型
     */
    private int selectCardType = 0;

    /**
     * 选择证件类型
     */
    private void showCardtype(List<String> list) {
        SelectBirthplaceDialog dialog = new SelectBirthplaceDialog(this, selectCardType, list);
        dialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                selectCardType = object == null ? 0 : (int) object;
                if (selectCardType == 2) {
                    finish();
                    return;
                }
                String type = list.get(selectCardType);
                llEditorCardtype.setEditorContent(type);
            }
        });
        dialog.show();
    }

    /**
     * 是否是正反面，默认反面
     */
    private boolean isFront = false;

    @OnClick({R.id.iv_back, R.id.iv_card_front, R.id.iv_card_background, R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_card_front:
                if (selectCardType == 0) {
                    showIDHKCard(true);
                } else if (selectCardType == 1) {
                    showIDZHCard(true);
                }
                break;
            case R.id.iv_card_background:
                if (selectCardType == 0) {
                    showIDHKCard(false);
                } else if (selectCardType == 1) {
                    showIDZHCard(false);
                }
                break;
            case R.id.tv_submit:
                // 把值传回
                Intent intent = new Intent();
                intent.putExtra("three", "啊啊啊啊啊啊");
                setResult(RESULT_OK, intent);
                finish();
                break;
            default:
                break;
        }
    }

    private void showIDZHCard(boolean isFront) {
        this.isFront = isFront;
        SelectCameraDialog selectCameraDialog = new SelectCameraDialog(this);
        selectCameraDialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                int index = (int) object;
                PermissionController permissionController = new PermissionController(RecognitionActivity.this);
                permissionController.setPermissions(PermissionController.CAMERA_STORAGE);
                permissionController.setOnPermissionListener(new PermissionController.OnPermissionListener() {
                    @Override
                    public void onHasPermission(boolean hasPermission) {
                        if (hasPermission) {
                            if (index == 1) {
                                // ZH UI
                                CardIdZHActivity.openCertificateCamera(RecognitionActivity.this,
                                        isFront ? CardIdZHActivity.TYPE_IDCARD_FRONT : CardIdZHActivity.TYPE_IDCARD_BACK);
                            } else {
                                FileSelector.selectImage(RecognitionActivity.this);

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
        this.isFront = isFront;
        SelectCameraDialog selectCameraDialog = new SelectCameraDialog(this);
        selectCameraDialog.setOnClickDialogOrFragmentViewListener(new OnClickDialogOrFragmentViewListener() {
            @Override
            public void onClickView(View view, Object object) {
                int index = (int) object;
                PermissionController permissionController = new PermissionController(RecognitionActivity.this);
                permissionController.setPermissions(PermissionController.CAMERA_STORAGE);
                permissionController.setOnPermissionListener(new PermissionController.OnPermissionListener() {
                    @Override
                    public void onHasPermission(boolean hasPermission) {
                        if (hasPermission) {
                            if (index == 1) {
                                // HK UI
                                CaraIdHKActivity.openCertificateCamera(RecognitionActivity.this,
                                        isFront ? CaraIdHKActivity.TYPE_IDCARD_FRONT : CardIdZHActivity.TYPE_IDCARD_BACK);
                            } else {
                                FileSelector.selectImage(RecognitionActivity.this);
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
            final String path = CardIdZHActivity.getResult(data);
            Log.e("rec", "图片路径：" + path);
            if (!TextUtils.isEmpty(path)) {
                GlideShowUtils.showImage(this, isFront ? ivCardFront : ivCardBackground, path);
            }
        }
        if (requestCode == FileSelector.REQUEST_CODE_SELECT_PHOTO) {
            if (data != null) {
                Uri uri = data.getData();
                if (uri != null) {
                    String path = FileUtil.getFilePathByUri(this, uri);
                    Log.e("rec", "从相册选择图片路径：" + path);
                    if (!TextUtils.isEmpty(path)) {
                        GlideShowUtils.showImage(this, isFront ? ivCardFront : ivCardBackground, path);
                    }
                }
            }
        }
    }
}
