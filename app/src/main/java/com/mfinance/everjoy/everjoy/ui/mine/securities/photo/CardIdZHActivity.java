package com.mfinance.everjoy.everjoy.ui.mine.securities.photo;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ImageUtils;
import com.mfinance.everjoy.R;
import com.mfinance.everjoy.everjoy.config.FileConfig;

import net.mfinance.chatlib.utils.ConfigUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 参考地址：https://github.com/xiaoowuu/CertificateCamera
 * <p>
 * Created by smartown on 2018/2/24 11:46.
 * <br>
 * Desc:
 * <br>
 * 拍照界面
 */
public class CardIdZHActivity extends Activity implements View.OnClickListener {

    /**
     * 拍摄类型-身份证正面
     */
    public final static int TYPE_IDCARD_FRONT = 1;
    /**
     * 拍摄类型-身份证反面
     */
    public final static int TYPE_IDCARD_BACK = 2;
    /**
     * 拍摄类型-竖版营业执照-证件照
     */
    public final static int TYPE_COMPANY_PORTRAIT = 3;
    /**
     * 拍摄类型-横版营业执照-证件照
     */
    public final static int TYPE_COMPANY_LANDSCAPE = 4;

    /**
     * 拍摄类型-竖版证件照
     */
    public final static int TYPE_OTHER_PORTRAIT = 5;
    /**
     * 拍摄类型-横版证件照
     */
    public final static int TYPE_OTHER_LANDSCAPE = 6;

    public final static int REQUEST_CODE = 0X13;
    public final static int RESULT_CODE = 0X14;

    /**
     * @param type {@link #TYPE_IDCARD_FRONT}
     *             {@link #TYPE_IDCARD_BACK}
     *             {@link #TYPE_COMPANY_PORTRAIT}
     *             {@link #TYPE_COMPANY_LANDSCAPE}
     */
    public static void openCertificateCamera(Activity activity, int type) {
        Intent intent = new Intent(activity, CardIdZHActivity.class);
        intent.putExtra("type", type);
        activity.startActivityForResult(intent, REQUEST_CODE);
    }

    /**
     * @return 结果文件路径
     */
    public static String getResult(Intent data) {
        if (data != null) {
            return data.getStringExtra("result");
        }
        return "";
    }

    private CameraPreview cameraPreview;
    private TextView tvIdcardDesc;
    private View containerView;
    private ImageView cropView;
    private ImageView flashImageView;
    private View optionView;
    private View resultView;

    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getIntent().getIntExtra("type", 0);
        if (type == TYPE_COMPANY_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        setContentView(R.layout.activity_camera);
        cameraPreview = (CameraPreview) findViewById(R.id.camera_surface);
        tvIdcardDesc = (TextView) findViewById(R.id.tv_idcard_desc);
        //获取屏幕最小边，设置为cameraPreview较窄的一边
        float screenMinSize = Math.min(getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels);
        //根据screenMinSize，计算出cameraPreview的较宽的一边，长宽比为标准的16:9
        float maxSize = screenMinSize / 9.0f * 16.0f;
        RelativeLayout.LayoutParams layoutParams;
        if (type == TYPE_COMPANY_PORTRAIT) {
            layoutParams = new RelativeLayout.LayoutParams((int) screenMinSize, (int) maxSize);
        } else {
            layoutParams = new RelativeLayout.LayoutParams((int) maxSize, (int) screenMinSize);
        }
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        cameraPreview.setLayoutParams(layoutParams);

        containerView = findViewById(R.id.camera_crop_container);
        cropView = (ImageView) findViewById(R.id.camera_crop);
        if (type == TYPE_COMPANY_PORTRAIT || type == TYPE_OTHER_PORTRAIT) {
            float width = (int) (screenMinSize * 0.8);
            float height = (int) (width * 43.0f / 30.0f);
            LinearLayout.LayoutParams containerParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) height);
            LinearLayout.LayoutParams cropParams = new LinearLayout.LayoutParams((int) width, (int) height);
            containerView.setLayoutParams(containerParams);
            cropView.setLayoutParams(cropParams);
        } else if (type == TYPE_COMPANY_LANDSCAPE || type == TYPE_OTHER_LANDSCAPE) {
            float height = (int) (screenMinSize * 0.8);
            float width = (int) (height * 43.0f / 30.0f);
            LinearLayout.LayoutParams containerParams = new LinearLayout.LayoutParams((int) width, ViewGroup.LayoutParams.MATCH_PARENT);
            LinearLayout.LayoutParams cropParams = new LinearLayout.LayoutParams((int) width, (int) height);
            containerView.setLayoutParams(containerParams);
            cropView.setLayoutParams(cropParams);
        } else {
            float height = (int) (screenMinSize * 0.75);
            float width = (int) (height * 75.0f / 47.0f);
            LinearLayout.LayoutParams containerParams = new LinearLayout.LayoutParams((int) width, ViewGroup.LayoutParams.MATCH_PARENT);
            LinearLayout.LayoutParams cropParams = new LinearLayout.LayoutParams((int) width, (int) height);
            containerView.setLayoutParams(containerParams);
            cropView.setLayoutParams(cropParams);
        }
        switch (type) {
            case TYPE_IDCARD_FRONT:
                cropView.setImageResource(R.mipmap.cn_idcard_front);
                tvIdcardDesc.setText(R.string.cn_idcard_front);
                break;
            case TYPE_IDCARD_BACK:
                cropView.setImageResource(R.mipmap.cn_idcard_back);
                tvIdcardDesc.setText(R.string.cn_idcard_back);
                break;
            case TYPE_COMPANY_PORTRAIT:
                cropView.setImageResource(R.mipmap.camera_company);
                tvIdcardDesc.setVisibility(View.GONE);
                break;
            case TYPE_COMPANY_LANDSCAPE:
                cropView.setImageResource(R.mipmap.camera_company_landscape);
                tvIdcardDesc.setVisibility(View.GONE);
                break;
            case TYPE_OTHER_PORTRAIT:
                // TODO 其他证件照设置普通的界面
//                cropView.setImageResource(R.mipmap.camera_company_landscape);
                tvIdcardDesc.setVisibility(View.GONE);
                break;
            case TYPE_OTHER_LANDSCAPE:
//                cropView.setImageResource(R.mipmap.camera_company_landscape);
                tvIdcardDesc.setVisibility(View.GONE);
                break;
        }

        flashImageView = (ImageView) findViewById(R.id.camera_flash);
        optionView = findViewById(R.id.camera_option);
        resultView = findViewById(R.id.camera_result);
        cameraPreview.setOnClickListener(this);
        findViewById(R.id.camera_close).setOnClickListener(this);
        findViewById(R.id.camera_take).setOnClickListener(this);
        flashImageView.setOnClickListener(this);
        findViewById(R.id.camera_result_ok).setOnClickListener(this);
        findViewById(R.id.camera_result_cancel).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.camera_surface) {
            cameraPreview.focus();
        } else if (id == R.id.camera_close) {
            finish();
        } else if (id == R.id.camera_take) {
            takePhoto();
        } else if (id == R.id.camera_flash) {
            boolean isFlashOn = cameraPreview.switchFlashLight();
            flashImageView.setImageResource(isFlashOn ? R.mipmap.camera_flash_on : R.mipmap.camera_flash_off);
        } else if (id == R.id.camera_result_ok) {
            goBack();
        } else if (id == R.id.camera_result_cancel) {
            optionView.setVisibility(View.VISIBLE);
            cameraPreview.setEnabled(true);
            resultView.setVisibility(View.GONE);
            cameraPreview.startPreview();
        }
    }

    private void takePhoto() {
        optionView.setVisibility(View.GONE);
        cameraPreview.setEnabled(false);
        cameraPreview.takePhoto(new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(final byte[] data, Camera camera) {
                camera.stopPreview();
                //子线程处理图片，防止ANR
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            File originalFile = getOriginalFile();
                            FileOutputStream originalFileOutputStream = new FileOutputStream(originalFile);
                            originalFileOutputStream.write(data);
                            originalFileOutputStream.close();

                            Bitmap bitmap = BitmapFactory.decodeFile(originalFile.getPath());

                            //计算裁剪位置
                            float left, top, right, bottom;
                            if (type == TYPE_COMPANY_PORTRAIT) {
                                left = (float) cropView.getLeft() / (float) cameraPreview.getWidth();
                                top = ((float) containerView.getTop() - (float) cameraPreview.getTop()) / (float) cameraPreview.getHeight();
                                right = (float) cropView.getRight() / (float) cameraPreview.getWidth();
                                bottom = (float) containerView.getBottom() / (float) cameraPreview.getHeight();
                            } else {
                                left = ((float) containerView.getLeft() - (float) cameraPreview.getLeft()) / (float) cameraPreview.getWidth();
                                top = (float) cropView.getTop() / (float) cameraPreview.getHeight();
                                right = (float) containerView.getRight() / (float) cameraPreview.getWidth();
                                bottom = (float) cropView.getBottom() / (float) cameraPreview.getHeight();
                            }
                            //裁剪及保存到文件
                            Bitmap cropBitmap = Bitmap.createBitmap(bitmap,
                                    (int) (left * (float) bitmap.getWidth()),
                                    (int) (top * (float) bitmap.getHeight()),
                                    (int) ((right - left) * (float) bitmap.getWidth()),
                                    (int) ((bottom - top) * (float) bitmap.getHeight()));

                            String path = getCropFile();
                            ImageUtils.save(cropBitmap, path, Bitmap.CompressFormat.JPEG);

//                            final File cropFile = getCropFile();
//                            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(cropFile));
//                            cropBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
//                            bos.flush();
//                            bos.close();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    resultView.setVisibility(View.VISIBLE);
                                }
                            });
                            return;
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                optionView.setVisibility(View.VISIBLE);
                                cameraPreview.setEnabled(true);
                            }
                        });
                    }
                }).start();

            }
        });
    }

    /**
     * @return 拍摄图片原始文件
     */
    private File getOriginalFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date());
        File file = new File(FileConfig.FILE_IDCARDS);
        switch (type) {
            case TYPE_IDCARD_FRONT:
                return new File(file, "idcard_front_" + timeStamp + ".jpg");
            case TYPE_IDCARD_BACK:
                return new File(file, "idcard_back_" + timeStamp + ".jpg");
            case TYPE_COMPANY_PORTRAIT:
            case TYPE_COMPANY_LANDSCAPE:
                return new File(file, "comp_" + timeStamp + ".jpg");
            case TYPE_OTHER_PORTRAIT:
            case TYPE_OTHER_LANDSCAPE:
                return new File(file, "card_" + timeStamp + ".jpg");
        }
        return new File(file, "picture_" + timeStamp + ".jpg");
    }

    private String cropFilePath;

    /**
     * @return 拍摄图片裁剪文件
     */
    private String getCropFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date());
        switch (type) {
            case TYPE_IDCARD_FRONT:
                cropFilePath = FileConfig.FILE_IDCARDS + "/idcard_crop_front_" + timeStamp + ".jpg";
                break;
            case TYPE_IDCARD_BACK:
                cropFilePath = FileConfig.FILE_IDCARDS + "/idcard_crop_back_" + timeStamp + ".jpg";
                break;
            case TYPE_COMPANY_PORTRAIT:
            case TYPE_COMPANY_LANDSCAPE:
                cropFilePath = FileConfig.FILE_IDCARDS + "/comp_crop_" + timeStamp + ".jpg";
                break;
            case TYPE_OTHER_PORTRAIT:
            case TYPE_OTHER_LANDSCAPE:
                cropFilePath = FileConfig.FILE_IDCARDS + "/card_crop_" + timeStamp + ".jpg";
                break;
            default:
                break;
        }
        return cropFilePath;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraPreview != null) {
            cameraPreview.release();
        }
    }

    /**
     * 点击对勾，使用拍照结果，返回对应图片路径
     */
    private void goBack() {
        Intent intent = new Intent();
        intent.putExtra("result", cropFilePath);
        setResult(RESULT_CODE, intent);
        finish();
    }

}
