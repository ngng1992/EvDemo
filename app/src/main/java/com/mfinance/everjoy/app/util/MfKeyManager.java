package com.mfinance.everjoy.app.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.UUID;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.security.auth.x500.X500Principal;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.preference.PreferenceManager;
import android.security.KeyPairGeneratorSpec;
import android.util.Base64;

public class MfKeyManager {
	private final Context context;
	private final byte[] masterKey = { (byte) 211, (byte) 133, (byte) 19,
			(byte) 128, (byte) 90, (byte) 186, (byte) 25, (byte) 65,
			(byte) 241, (byte) 181, (byte) 85, (byte) 124, (byte) 153,
			(byte) 115, (byte) 130, (byte) 243, (byte) 110, (byte) 70,
			(byte) 159, (byte) 36, (byte) 71, (byte) 216, (byte) 164,
			(byte) 191, (byte) 226, (byte) 67, (byte) 146, (byte) 157,
			(byte) 152, (byte) 67, (byte) 112, (byte) 3 };
	private final Cipher mCipher;
	private final KeyPair mPair;
	private final String sID;

	private final boolean supportKeyStore;

	public MfKeyManager(Context context) throws GeneralSecurityException,
			IOException {
		this.context = context;
		this.sID = byteToString(id().getBytes());
		if (Build.VERSION.SDK_INT >= 18) {
			supportKeyStore = true;

			String alias = context.getPackageName() + ".seed";
			mCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			final KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
			keyStore.load(null);
			if (!keyStore.containsAlias(alias)) {
				generateKeyPair(alias);
			}
			// Even if we just generated the key, always read it back to ensure
			// we
			// can read it successfully.
			final KeyStore.PrivateKeyEntry entry = (KeyStore.PrivateKeyEntry) keyStore
					.getEntry(alias, null);
			mPair = new KeyPair(entry.getCertificate().getPublicKey(),
					entry.getPrivateKey());
		} else {
			supportKeyStore = false;
			mPair = null;
			mCipher = null;
		}
	}

	public String getKey(String name) throws Exception {
		SecretKey sk = getSecretKey();
		name = byteToString(encrypt(sk.getEncoded(), name.getBytes()));
		if (!PreferenceManager.getDefaultSharedPreferences(context).contains(
				name))
			return null;

		String key = PreferenceManager.getDefaultSharedPreferences(context)
				.getString(name, "");

		return new String(decrypt(sk.getEncoded(), stringToByte(key)), "UTF-8");
	}

	public void saveKey(String name, String key) throws Exception {
		SecretKey sk = getSecretKey();
		PreferenceManager
				.getDefaultSharedPreferences(context)
				.edit()
				.putString(
						byteToString(encrypt(sk.getEncoded(), name.getBytes())),
						byteToString(encrypt(sk.getEncoded(), key.getBytes())))
				.commit();
	}

	@SuppressLint("TrulyRandom")
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	private void generateKeyPair(String alias) throws GeneralSecurityException {
		final Calendar start = new GregorianCalendar();
		final Calendar end = new GregorianCalendar();
		end.add(Calendar.YEAR, 100);
		final KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(
				context).setAlias(alias)
				.setSubject(new X500Principal("CN=" + alias))
				.setSerialNumber(BigInteger.ONE).setStartDate(start.getTime())
				.setEndDate(end.getTime()).build();
		final KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA",
				"AndroidKeyStore");
		gen.initialize(spec);
		gen.generateKeyPair();
	}

	private SecretKey getSecretKey() throws Exception {
		byte[] wsk = null;
		SecretKey sk = null;
		String skStr = null;
		if (!PreferenceManager.getDefaultSharedPreferences(context).contains(
				sID)) {
			sk = generateKey();
			if (supportKeyStore)
				wsk = wrap(sk);
			else
				wsk = wrap2(sk);
			skStr = byteToString(wsk);
			PreferenceManager.getDefaultSharedPreferences(context).edit()
					.putString(sID, skStr).commit();
			return sk;
		} else {
			skStr = PreferenceManager.getDefaultSharedPreferences(context)
					.getString(sID, "");
			wsk = stringToByte(skStr);
			if (supportKeyStore)
				sk = unwrap(wsk);
			else
				sk = unwrap2(wsk);
			return sk;
		}
	}

	private synchronized String id() {
		File installation = new File(context.getFilesDir(), "INSTALLATION");
		try {
			if (!installation.exists())
				writeInstallationFile(installation);
			return readInstallationFile(installation);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private SecretKey unwrap(byte[] blob) throws GeneralSecurityException {
		mCipher.init(Cipher.UNWRAP_MODE, mPair.getPrivate());
		return (SecretKey) mCipher.unwrap(blob, "AES", Cipher.SECRET_KEY);
	}

	private SecretKey unwrap2(byte[] blob) throws Exception {
		byte[] wsk = decrypt(masterKey, blob);
		SecretKey sk = new SecretKeySpec(wsk, 0, wsk.length, "AES");
		return sk;
	}

	private byte[] wrap(SecretKey key) throws GeneralSecurityException {
		mCipher.init(Cipher.WRAP_MODE, mPair.getPublic());
		return mCipher.wrap(key);
	}

	private byte[] wrap2(SecretKey key) throws Exception {
		byte[] wsk = encrypt(masterKey, key.getEncoded());
		return wsk;
	}

	private static String byteToString(byte[] bArray) {
		return Base64.encodeToString(bArray, Base64.DEFAULT);
	}

	private static byte[] decrypt(byte[] raw, byte[] encrypted)
			throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		byte[] decrypted = cipher.doFinal(encrypted);
		return decrypted;
	}

	private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] encrypted = cipher.doFinal(clear);
		return encrypted;
	}

	private static SecretKey generateKey() throws NoSuchAlgorithmException {
		// Generate a 256-bit key
		final int outputKeyLength = 256;

		SecureRandom secureRandom = new SecureRandom();
		// Do *not* seed secureRandom! Automatically seeded from system entropy.
		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
		keyGenerator.init(outputKeyLength, secureRandom);
		SecretKey key = keyGenerator.generateKey();
		return key;
	}

	private static String readInstallationFile(File installation)
			throws IOException {
		RandomAccessFile f = new RandomAccessFile(installation, "r");
		byte[] bytes = new byte[(int) f.length()];
		f.readFully(bytes);
		f.close();
		return new String(bytes);
	}

	private static byte[] stringToByte(String str) {
		return Base64.decode(str, Base64.DEFAULT);
	}

	private static void writeInstallationFile(File installation)
			throws IOException {
		FileOutputStream out = new FileOutputStream(installation);
		String id = UUID.randomUUID().toString();
		out.write(id.getBytes());
		out.close();
	}
}