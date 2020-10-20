package com.mfinance.everjoy.app.util;

import com.google.common.collect.Iterators;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;

public class ConnectionSelector {

    public static class ConnectionEntry {
        public static ConnectionEntry create(InetSocketAddress socketAddress, String displayName) {
            return new ConnectionEntry(socketAddress, displayName);
        }

        private ConnectionEntry(InetSocketAddress socketAddress, String displayName) {
            this.socketAddress = socketAddress;
            this.displayName = displayName;
        }
        final private InetSocketAddress socketAddress;
        final private String displayName;

        public InetSocketAddress getSocketAddress() {
            return socketAddress;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    private static Iterable<ConnectionEntry> priceAgentDefaultAddress;

    public static void initPriceAgentDefaultSetting(Iterable<ConnectionEntry> priceAgentDefaultAddress) {
        ConnectionSelector.priceAgentDefaultAddress = priceAgentDefaultAddress;
    }

    public static Iterator<ConnectionEntry> getPriceAgent(String strUrl, String selfIP) {
        return Iterators.cycle(priceAgentDefaultAddress);
    }

    public static int get(String strUrl, String selfIP)
    {
        if (strUrl.toLowerCase().startsWith("https"))
            return httpsGet(strUrl, selfIP);
        else
            return httpGet(strUrl, selfIP);
    }

    private static int httpGet(String strUrl, String selfIP)
    {
        int result = 0;
        try
        {
            CommonFunction cf = new CommonFunction();
            cf.setKey(Utility.getHttpKey());
            String strEncryptedQueryString = cf.encryptText("ip="+selfIP);
            StringBuilder sb = new StringBuilder();
            URL url;
            url = new URL(strUrl+"/terminalconn.asp?key="+strEncryptedQueryString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);
            conn.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null)
            {
                sb.append(line);
            }
            rd.close();
            result = Utility.toInteger(sb.toString().trim(), 0);
        }
        catch(Exception e)
        {
        }
        return result;
    }

    private static int httpsGet(String strUrl, String selfIP)
    {
        int result = 0;
        try
        {
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(new KeyManager[0], new TrustManager[] {new MyX509TrustManager(null)}, new SecureRandom());
            SSLContext.setDefault(ctx);

            CommonFunction cf = new CommonFunction();
            cf.setKey(Utility.getHttpKey());
            String strEncryptedQueryString = cf.encryptText("ip="+selfIP);
            StringBuilder sb = new StringBuilder();
            URL url;
            url = new URL(strUrl+"/terminalconn.asp?key="+strEncryptedQueryString);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);
            conn.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null)
            {
                sb.append(line);
            }
            rd.close();
            result = Utility.toInteger(sb.toString().trim(), 0);
        }
        catch(Exception e)
        {
        }
        return result;
    }

    /**
     *
     * @param addressString a CSV of list of address. Expecting a hostname and port
     * separated by ":" e.g. testing:1234
     * @return list of address
     */
    public static ArrayList<ConnectionEntry> parsePriceAgent(String addressString) {
        ArrayList<ConnectionEntry> returnInstance = new ArrayList<ConnectionEntry>();
        Integer connectionNumber = 1;
        for (String temp : addressString.split(",")) {
            String[] splitAddressPart = temp.split(":");
            if (splitAddressPart.length >= 2) {
                try {
                    InetSocketAddress tempAddress = new InetSocketAddress(splitAddressPart[0], Integer.parseInt(splitAddressPart[1]));
                    returnInstance.add(new ConnectionEntry(tempAddress, connectionNumber.toString()));
                    connectionNumber = connectionNumber + 1;
                } catch (NumberFormatException ex) {
                } catch (IllegalArgumentException ex) {
                }
            }
        }
        return returnInstance;
    }

    /**
     *
     * @param addressString a CSV of list of address. Expecting a hostname and port
     * separated by ":" e.g. testing:1234
     * @param displayNameString Comma separated value. list of display Name.
     * @return list of address
     */
    public static ArrayList<ConnectionEntry> parsePriceAgent(String addressString, String displayNameString) {
        ArrayList<ConnectionEntry> returnInstance = new ArrayList<ConnectionEntry>();
        Iterator<String> currentDisplayNameIterator = Arrays.asList(displayNameString.split(",")).iterator();
        for (String temp : addressString.split(",")) {
            String[] splitAddressPart = temp.split(":");
            if (splitAddressPart.length >= 2) {
                try {
                    InetSocketAddress tempAddress = new InetSocketAddress(splitAddressPart[0], Integer.parseInt(splitAddressPart[1]));
                    if(currentDisplayNameIterator.hasNext()) {
                        returnInstance.add(new ConnectionEntry(tempAddress, currentDisplayNameIterator.next()));
                    } else {
                        returnInstance.add(new ConnectionEntry(tempAddress, ""));
                    }
                } catch (NumberFormatException ex) {
                } catch (IllegalArgumentException ex) {
                }
            }
        }
        return returnInstance;
    }

}
