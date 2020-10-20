package com.mfinance.everjoy.app.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.util.Arrays;

import android.util.Log;

import com.mfinance.everjoy.app.CompanySettings;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.X509Certificate;

public class IPRetriever {

	public static String get(URL url) {
		return get(url.getHost(), url.getPort());
	}

	private static final int TIMEOUT = 5000;
	public static final byte ZERO = 0;

	public static class MyTrustManager implements X509TrustManager {

		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException
		{
		}

		public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException
		{
			boolean isTrust = false;
			byte[] sn = CompanySettings.getEchoServerCertsSN();
			byte[] md5 = CompanySettings.getEchoServerCertsMD5();
			for (int j=0; j<chain.length; j++)
			{
				if (Arrays.equals(sn, chain[j].getSerialNumber().toByteArray()))
				{
					try
					{
						System.out.println("chain[j].getEncoded() " + Arrays.toString(chain[j].getEncoded()));
						System.out.println("chain[j].getEncoded()  MessageDigest.getInstance(\"MD5\").digest(chain[j].getEncoded()) " + Arrays.toString( MessageDigest.getInstance("MD5").digest(chain[j].getEncoded())));
						if (Arrays.equals(md5, MessageDigest.getInstance("MD5").digest(chain[j].getEncoded())))
						{
							isTrust = true;
						}
					}
					catch (Exception e)
					{
					}
					break;
				}
			}
			Arrays.fill(sn, ZERO);
			Arrays.fill(md5, ZERO);
			if (!isTrust)
			{
				throw new CertificateException();
			}
		}
	}

	public static String httpsGet(String strUrl)
	{
		StringBuilder sb = new StringBuilder();
		try
		{
			SSLContext sc = SSLContext.getInstance("TLSv1.2");
			sc.init(null, new TrustManager[] { new MyTrustManager() }, new SecureRandom());

			URL url;
			url = new URL(strUrl);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setSSLSocketFactory(sc.getSocketFactory());
			conn.setHostnameVerifier(new HostnameVerifier() {
				@Override
				public boolean verify(String arg0, SSLSession arg1) {
					return true;
				}
			});
			conn.setConnectTimeout(TIMEOUT);
			conn.setReadTimeout(TIMEOUT);
			conn.setRequestMethod("GET");
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null)
			{
				sb.append(line);
			}
			rd.close();
			return sb.toString().trim();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			sb.setLength(0);
		}
		return null;
	}

	public static String get(String host, String port) {
		return get(host, Integer.valueOf(port));
	}

	public static String get(String host, int port) {
		Socket echoSocket = null;
		BufferedReader in = null;
		String ip = null;

		try {
			echoSocket = new Socket(host, port);
		} catch (UnknownHostException e) {
			Log.e("IPRetriever", "Unknown host:" + host);
		} catch (IOException e) {
			Log.e("IPRetriever", "Cannot connect to host:" + host + " port:"
					+ port);
		}

		try {
			if (echoSocket != null) {
				in = new BufferedReader(new InputStreamReader(
						echoSocket.getInputStream()));
				ip = in.readLine();
			}
		} catch (IOException e) {
			Log.e("IPRetriever", "Cannot get IP from host:" + host + " port:"
					+ port);
		}

		try {
			// cleanup
			if (in != null) {
				in.close();
			}
			if (echoSocket != null) {
				echoSocket.close();
			}
		} catch (IOException e) {
			// ignored
		}

		return ip;
	}
}