package com.mfinance.everjoy.everjoy.utils;

import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;

import java.security.KeyStore;

import javax.crypto.KeyGenerator;

/**
 * 指纹登录用到的工具类
 */
public class FingerprintUtils {

    private static final String DEFAULT_KEY_NAME = "default_eveyjoy_key";

    private static KeyStore keyStore;

    /**
     * 检查指纹或面部识别
     */
    public static boolean checkFingerprint(Context context) {
        FingerprintManager fingerprintManager =
                (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);
        boolean b = fingerprintManager.isHardwareDetected();
        boolean b1 = fingerprintManager.hasEnrolledFingerprints();
        Log.e("fing", "手机是否支持指纹登录 b = " + b + ";是否已经设置了一个指纹b1 = " + b1);
        return b && b1;
    }

    public static void initKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(DEFAULT_KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7);
            keyGenerator.init(builder.build());
            keyGenerator.generateKey();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
