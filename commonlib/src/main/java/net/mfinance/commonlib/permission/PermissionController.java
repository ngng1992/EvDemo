package net.mfinance.commonlib.permission;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import net.mfinance.commonlib.R;

import java.util.List;

/**
 * 权限控制
 */
public class PermissionController {

    /**
     * 闪屏页请求的权限，先申请所有需要的权限：录音，摄像头，读取存储卡
     */
    public static final String[] AUDIO_CAMERA_STORAGE = {Permission.RECORD_AUDIO,
            Permission.CAMERA,
            Permission.READ_EXTERNAL_STORAGE,
            Permission.WRITE_EXTERNAL_STORAGE};

    /**
     * 录音、读取存储卡
     */
    public static final String[] AUDIO_STORAGE = {Permission.RECORD_AUDIO,
            Permission.READ_EXTERNAL_STORAGE,
            Permission.WRITE_EXTERNAL_STORAGE};

    /**
     * 摄像头，读取存储卡
     */
    public static final String[] CAMERA_STORAGE = {Permission.CAMERA,
            Permission.READ_EXTERNAL_STORAGE,
            Permission.WRITE_EXTERNAL_STORAGE};

    /**
     * 读取存储卡
     */
    public static final String[] STORAGE = {
            Permission.READ_EXTERNAL_STORAGE,
            Permission.WRITE_EXTERNAL_STORAGE};

    private OnPermissionListener mOnPermissionListener;
    private Context context;
    private String resStrTips;
    private String[] pers;

    public PermissionController(Context context) {
        this.context = context;
    }

    public PermissionController setOnPermissionListener(OnPermissionListener onPermissionListener) {
        mOnPermissionListener = onPermissionListener;
        return this;
    }

    /**
     * 设置提示语
     */
    public PermissionController setToastTips(int resStrTips) {
        this.resStrTips = context.getString(resStrTips);
        return this;
    }

    public PermissionController setPermissions(String[] permissions) {
        this.pers = permissions;
        if (pers == AUDIO_CAMERA_STORAGE) {
            this.resStrTips = context.getString(R.string.permission_record_camera);
        } else if (pers == AUDIO_STORAGE) {
            this.resStrTips = context.getString(R.string.permission_record_audio);
        } else if (pers == CAMERA_STORAGE) {
            this.resStrTips = context.getString(R.string.permission_camera);
        } else if (pers == STORAGE) {
            this.resStrTips = context.getString(R.string.permission_storage);
        }
        return this;
    }

    @SuppressLint("WrongConstant")
    public void requestPermission() {
        if (pers == null) {
            throw new RuntimeException("请设置对应的权限");
        }
        boolean hasPermissions = AndPermission.hasPermissions(context, pers);
        if (hasPermissions) {
            mOnPermissionListener.onHasPermission(true);
        } else {
            AndPermission.with(context)
                    .runtime()
                    .permission(pers)
                    .onGranted(new Action<List<String>>() {
                        @Override
                        public void onAction(List<String> data) {
                            // 同意
                            mOnPermissionListener.onHasPermission(true);
                        }
                    })
                    .onDenied(new Action<List<String>>() {
                        @Override
                        public void onAction(List<String> data) {
                            if (!TextUtils.isEmpty(resStrTips)) {
                                Toast.makeText(context, resStrTips, Toast.LENGTH_SHORT).show();
                            }
                            mOnPermissionListener.onHasPermission(false);
                            // 去设置界面设置对应的权限
//                            Intent localIntent = new Intent();
//                            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
//                            localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
//                            context.startActivity(localIntent);
                        }
                    })
                    .start();
        }
    }

    /**
     * 权限回调
     */
    public interface OnPermissionListener {

        /**
         * 获取到权限
         */
        void onHasPermission(boolean hasPermission);
    }

}
