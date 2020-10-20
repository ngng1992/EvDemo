package com.mfinance.everjoy.app.util;

import android.util.Log;

import com.mfinance.everjoy.app.CompanySettings;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.compress.compressors.deflate.DeflateCompressorOutputStream;

/**
 * Created by johnny.ng on 10/7/2020.
 */

public class AESLib
{
    public static final String CHARSETFORMAT="UTF8";
    public static final byte ZERO = 0;
    public static byte[] randomKey = new byte[0];
    private SecretKeySpec secretKey;
    private Cipher encryptCipher;
    private Cipher decryptCipher;

    static
    {
        try
        {
            Field field = Class.forName("javax.crypto.JceSecurity").getDeclaredField("isRestricted");
            field.setAccessible(true);
            field.set(null, java.lang.Boolean.FALSE);
        }
        catch (Exception ex)
        {
        }
    }

    private static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    public AESLib()
    {
        try
        {
            byte[] specName = getSpecName();
            byte[] cipherName = getCipherName();
            byte[] staticKey = CompanySettings.getSecretKey();
            byte[] staticRandom = Utility.byteConcat(staticKey, randomKey);
            byte[] messageKey = sha256(staticRandom);
            byte[] iv = CompanySettings.getIV();
            secretKey = new SecretKeySpec(messageKey, new String(specName));
            encryptCipher = Cipher.getInstance(new String(cipherName));
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            encryptCipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
            decryptCipher = Cipher.getInstance(new String(cipherName));
            decryptCipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
            Arrays.fill(staticKey, ZERO);
            Arrays.fill(staticRandom, ZERO);
            Arrays.fill(messageKey, ZERO);
            Arrays.fill(iv, ZERO);
            Arrays.fill(specName, ZERO);
            Arrays.fill(cipherName, ZERO);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public AESLib(byte[] inKey)
    {
        try
        {
            byte[] specName = getSpecName();
            byte[] cipherName = getCipherName();
            byte[] iv = CompanySettings.getIV();
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            secretKey = new SecretKeySpec(inKey, new String(specName));
            ivspec = new IvParameterSpec(Arrays.copyOfRange(inKey, 0, 16));
            encryptCipher = Cipher.getInstance(new String(cipherName));
            encryptCipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
            decryptCipher = Cipher.getInstance(new String(cipherName));
            decryptCipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
            Arrays.fill(iv, ZERO);
            Arrays.fill(specName, ZERO);
            Arrays.fill(cipherName, ZERO);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public String encrypt(String inputStr)
    {
        try
        {
            byte[] outputBytes = encryptCipher.doFinal(compress3(inputStr));
            return DatatypeConverter.printBase64Binary(outputBytes);
        }
        catch (Exception e)
        {
            Log.i("AESLib", "AESLib encrypt", e.fillInStackTrace());
            return null;
        }
    }
    public String decrypt(String inputStr)
    {
        try
        {
            byte[] outputBytes = DatatypeConverter.parseBase64Binary(inputStr);
            outputBytes = decryptCipher.doFinal(outputBytes);
            return uncompress3(outputBytes);
        }
        catch (Exception e)
        {
            Log.i("AESLib", "AESLib decrypt", e.fillInStackTrace());
            return null;
        }
    }

    public static byte[] compress3(String s)
    {
        try {
            byte[] bytes = s.getBytes(CHARSETFORMAT);
            ByteArrayOutputStream baos = new ByteArrayOutputStream(bytes.length);
            OutputStream outputStream = new DeflateCompressorOutputStream(baos);
            outputStream.write(bytes);
            outputStream.close();
            baos.close();

            return baos.toByteArray();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return new byte[0];
        }
    }
    public static String uncompress3(byte[] b)
    {
        Inflater infl = new Inflater();
        infl.setInput(b);

        String szRet = null;
        boolean done = false;

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while (!done)
        {
            byte[] buf = new byte[256];
            try
            {
                int bufnum = infl.inflate(buf);
                bos.write(buf, 0, bufnum);

                if (bufnum < buf.length)
                    done = true;
            }
            catch(DataFormatException dfe)
            {
                done = true;
            }
        }

        try
        {
            szRet = new String(bos.toByteArray(), CHARSETFORMAT);
        }
        catch (UnsupportedEncodingException uee)
        {
        }

        bos = null;

        return szRet;
    }

    private static byte[] getSpecName()
    {
        //AES
        return (new Object() {int t;public byte[] process() {byte[] buf = new byte[3];t = -1602224128; buf[0] = (byte) (t >>> 23);t = 128090112; buf[1] = (byte) (t >>> 15);t = 240000; buf[2] = (byte) (t >>> 7);return buf;}}.process());
    }

    private static byte[] getCipherName()
    {
        //AES/CBC/PKCS5Padding
        return (new Object() {int t;public byte[] process() {byte[] buf = new byte[20];t = 6664; buf[0] = (byte) (t >>> 3);t = 1157627904; buf[1] = (byte) (t >>> 24);t = 240000; buf[2] = (byte) (t >>> 7);t = -1512046592; buf[3] = (byte) (t >>> 21);t = 272826368; buf[4] = (byte) (t >>> 16);t = -1710227456; buf[5] = (byte) (t >>> 19);t = 2690048; buf[6] = (byte) (t >>> 10);t = 606; buf[7] = (byte) (t >>> 1);t = 239616; buf[8] = (byte) (t >>> 7);t = 12890112; buf[9] = (byte) (t >>> 12);t = 27811840; buf[10] = (byte) (t >>> 13);t = 17712; buf[11] = (byte) (t >>> 4);t = 537856; buf[12] = (byte) (t >>> 8);t = 12910592; buf[13] = (byte) (t >>> 12);t = -1693974528; buf[14] = (byte) (t >>> 19);t = 6944; buf[15] = (byte) (t >>> 3);t = 60358656; buf[16] = (byte) (t >>> 14);t = 551168; buf[17] = (byte) (t >>> 8);t = 275644416; buf[18] = (byte) (t >>> 16);t = 104896; buf[19] = (byte) (t >>> 6);return buf;}}.process());
    }

    private static byte[] getSHA256Name()
    {
        //SHA-256
        return (new Object() {int t;public byte[] process() {byte[] buf = new byte[7];t = 1392508928; buf[0] = (byte) (t >>> 24);t = 542720; buf[1] = (byte) (t >>> 8);t = 578945024; buf[2] = (byte) (t >>> 17);t = -1721237504; buf[3] = (byte) (t >>> 19);t = 42560; buf[4] = (byte) (t >>> 5);t = 12800000; buf[5] = (byte) (t >>> 12);t = 127598592; buf[6] = (byte) (t >>> 15);return buf;}}.process());
    }

    private static byte[] sha256(byte[] str)
    {
        try
        {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(str);
            return md.digest();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}

