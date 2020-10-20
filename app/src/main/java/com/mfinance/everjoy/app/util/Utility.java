package com.mfinance.everjoy.app.util;

import java.io.*;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.zip.*;
import java.util.*;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.http.conn.util.InetAddressUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.mfinance.everjoy.app.CompanySettings;
import com.mfinance.everjoy.app.MobileTraderApplication;

import com.mfinance.everjoy.R;
import com.mfinance.everjoy.app.bo.ContractObj;
import com.mfinance.everjoy.app.bo.OpenPositionRecord;
import com.mfinance.everjoy.app.constant.FXConstants;
import com.mfinance.everjoy.app.model.DataRepository;
import com.mfinance.everjoy.app.pojo.ContractDefaultSetting;
import com.mfinance.everjoy.app.pojo.ContractDefaultSettingBuilder;


public class Utility
{
    private static long lReferenceCounter = 0;
    private static final DecimalFormat m_dfAmount = new DecimalFormat("#,##0");
    private static final DecimalFormat m_dfLot0dp = new DecimalFormat("#,##0");
    private static final DecimalFormat m_dfLot1dp = new DecimalFormat("#,##0.0");
    private static final DecimalFormat m_dfLot2dp = new DecimalFormat("#,##0.00");
    private static final DecimalFormat m_dfValue = new DecimalFormat("#,##0.00");

    private static final DecimalFormat m_dfInputLot0dp = new DecimalFormat("###0");
    private static final DecimalFormat m_dfInputLot1dp = new DecimalFormat("###0.0");
    private static final DecimalFormat m_dfInputLot2dp = new DecimalFormat("###0.00");

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
    private static final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
    private static final SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private static final SimpleDateFormat sdf4 = new SimpleDateFormat("dd MMM yy", Locale.US);
    public static SimpleDateFormat sdf5 = new SimpleDateFormat("dd/MM/yy", Locale.US);
    public static SimpleDateFormat sdf5WithTime = new SimpleDateFormat("dd/MM/yy HH:MM", Locale.US);
    public static final SimpleDateFormat sdf6 = new SimpleDateFormat("yyyy-MMM-d",Locale.US);
    private static final SimpleDateFormat ddf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    public static final SimpleDateFormat tdf = new SimpleDateFormat("HH:mm:ss", Locale.US);
    private static final SimpleDateFormat tdf2 = new SimpleDateFormat("HH:mm", Locale.US);

    /*
    private static HashMap<String, String[]> hmContractMCurrency = new HashMap<String, String[]> ();
    static{
    	hmContractMCurrency.put("AUD", new String[]{"AUD", "USD"});
    	hmContractMCurrency.put("AUDJPY", new String[]{"AUD", "JPY"});
    	hmContractMCurrency.put("CAD", new String[]{"USD", "CAD"});
    	hmContractMCurrency.put("CHF", new String[]{"USD", "CHF"});
    	hmContractMCurrency.put("CHFJPY", new String[]{"CHF", "JPY"});
    	hmContractMCurrency.put("EUR", new String[]{"EUR", "USD"});
    	hmContractMCurrency.put("EURCHF", new String[]{"EUR", "CHF"});
    	hmContractMCurrency.put("EURGBP", new String[]{"EUR", "GBP"});
    	hmContractMCurrency.put("EURJPY", new String[]{"EUR", "JPY"});
    	hmContractMCurrency.put("GBP", new String[]{"GBP", "USD"});
    	hmContractMCurrency.put("GBPJPY", new String[]{"GBP", "JPY"});
    	hmContractMCurrency.put("HKG", new String[]{"GOLD", "HKD"});
    	hmContractMCurrency.put("LLG", new String[]{"GOLD", "USD"});
    	hmContractMCurrency.put("LCG(S)", new String[]{"GOLD", "CNY"});
    	hmContractMCurrency.put("LG10", new String[]{"GOLD", "CNY"});
    	hmContractMCurrency.put("LLS", new String[]{"SLIVER", "USD"});
    	hmContractMCurrency.put("LCS(S)", new String[]{"SLIVER", "CNY"});
    	hmContractMCurrency.put("LS250", new String[]{"SLIVER", "CNY"});
    	hmContractMCurrency.put("NZD", new String[]{"NZD", "USD"});
    	hmContractMCurrency.put("RKG", new String[]{"GOLD", "CNY"});
    	hmContractMCurrency.put("LCG", new String[]{"GOLD", "CNY"});
    	hmContractMCurrency.put("LCS", new String[]{"SLIVER", "CNY"});
    	hmContractMCurrency.put("USDRMB", new String[]{"USD", "CNY"});
    	hmContractMCurrency.put("OIL", new String[]{"OIL", "USD"});
    	hmContractMCurrency.put("USDCNY", new String[]{"USD", "CNY"});
    	hmContractMCurrency.put("SILR25", new String[]{"SLIVER", "USD"});
    }
    */
    private static HashMap<String, String> hmContractMChart= new HashMap<String, String> ();
    /*
    static{
    	hmContractMChart.put("AUD", "AUDUSD");

    }
    */

    /*
    public static String[] getCurrency(String sContract){
    	return hmContractMCurrency.get(sContract);
    }
    */
    public static void addChartMap(String contract, String chartCode){
        hmContractMChart.put(contract, chartCode);
    }

    public static HashMap<String, String> getChartMap(){
        return hmContractMChart;
    }

    public static String getChartCode(String sContract){
        String str=hmContractMChart.get(sContract);
        //Log.e("hmContractMChart",str);
        return hmContractMChart.get(sContract);
    }

    public static int[] getConfigNumFromString(String sInputStr)
    {
        int outNum[] = null, nCount = 0;

        if(sInputStr.length()>0)
        {
            StringTokenizer st = new StringTokenizer(sInputStr, ",");
            outNum = new int[st.countTokens()];
            while(st.hasMoreElements())
            {
                try
                {
                    outNum[nCount] = Integer.parseInt(st.nextToken());
                    nCount++;
                }
                catch(NumberFormatException nfe){}
            }
        }

        return outNum;
    }



    public static String getReferenceNum()
    {
        if (lReferenceCounter == 0)
            lReferenceCounter = Calendar.YEAR * 1000000000 + Calendar.MONTH * 10000000 + Calendar.DATE * 100000;
        else
            lReferenceCounter++;

        return String.valueOf(lReferenceCounter);
    }

    public static String formatPrice(String strPrice, String strDp)
    {
        return formatPrice(strPrice, toInteger(strDp, 0));
    }

    public static String formatPrice(String strPrice, int dp)
    {
        double dPrice = 0;
        DecimalFormat df = new DecimalFormat();
        String strFillZero = "0";
        if (dp > 0)
        {
            strFillZero += ".";
            for (int j=0; j<dp; j++)
                strFillZero += "0";
        }
        df.applyPattern(strFillZero);
        try{
            strPrice = strPrice.replaceAll(",", "");
            dPrice = Double.parseDouble(strPrice);
        }catch(NumberFormatException nfe){
            return "";
        }
        //return df.format(dPrice / Math.pow(10, dp));

        return df.format(dPrice);
    }

    public static String formatAmount(double dAmount)
    {
        //return m_dfAmount.format(dAmount);
        return String.valueOf(dAmount);
    }

    public static String formatAmount(BigDecimal bdAmount)
    {
        return bdAmount.toString();
    }

    public static String formatLot(double dLot)
    {
        return getDecimalFormatLotSize().format(dLot);
    }

    public static String formatLot(BigDecimal lot) {
        return getDecimalFormatLotSize().format(lot);
    }

    public static DecimalFormat getDecimalFormatLotSize() {
        if (CompanySettings.NUM_OF_LOT_DP == 2)
            return m_dfLot2dp;
        else if (CompanySettings.NUM_OF_LOT_DP == 1)
            return m_dfLot1dp;
        else if (CompanySettings.NUM_OF_LOT_DP == 0)
            return m_dfLot0dp;
        else
            return m_dfLot1dp;
    }

    public static String formatInputLot(double dLot)
    {
        if (CompanySettings.NUM_OF_LOT_DP == 2)
            return m_dfInputLot2dp.format(dLot);
        else if (CompanySettings.NUM_OF_LOT_DP == 1)
            return m_dfInputLot1dp.format(dLot);
        else if (CompanySettings.NUM_OF_LOT_DP == 0)
            return m_dfInputLot0dp.format(dLot);
        else
            return m_dfInputLot1dp.format(dLot);
    }

    public static String formatValue(double dValue)
    {
        return m_dfValue.format(dValue);
    }

    public static String formatValue(BigDecimal d) {
        return m_dfValue.format(d);
    }

    public static String round(String strPrice, String strDp, String strDisplay)
    {
        return round(strPrice, toInteger(strDp, 0), toInteger(strDisplay, 0));
    }


    public static String round(String strPrice, int dp, int display)
    {
        return round(toDouble(strPrice,0), dp, display, 0);
    }

    public static String round(String strPrice, int dp, int display, int zeroLead)
    {
        return round(toDouble(strPrice,0), dp, display, zeroLead);
    }

    public static String round(double dPrice, int dp, int display)
    {
        return round(dPrice, dp, display, 0);
    }

    public static String round(double dPrice, int dp, int display, int zeroLead)
    {
        double dReturn;
        if (display==0)
            dReturn = (long)Math.round(roundToDouble(dPrice, dp+zeroLead, display+zeroLead));
        else
            dReturn = roundToDouble(dPrice, dp+zeroLead, display+zeroLead);

        DecimalFormat df = new DecimalFormat();
        String strFillZero = "0";
        if (display > 0)
        {
            strFillZero += ".";
            for (int j=0; j<display; j++)
                strFillZero += "0";
            for (int j=0; j<zeroLead; j++)
                strFillZero += "#";
        }
        df.applyPattern(strFillZero);

        return df.format(dReturn);
    }

    public static double roundToDouble(String strPrice, int dp, int display)
    {
        return roundToDouble(toDouble(strPrice,0), dp, display);
    }

    public static double roundToDouble(double dPrice, int dp, int display)
    {
        double d1 = Math.round(dPrice*Math.pow(10, dp)+0.0001)/Math.pow(10, dp);
        double d2 = Math.round(d1*Math.pow(10, display))/Math.pow(10, display);
        return d2;
    }

    public static double toDouble(String strValue, double defaultValue)
    {
        double dValue = 0;
        try{
            dValue = Double.parseDouble(strValue.replaceAll(",", ""));
        }catch(NumberFormatException nfe){
            dValue = defaultValue;
        }catch(NullPointerException npt){
            dValue = defaultValue;
        }
        return dValue;
    }

    public static float toFloat(String strValue, float defaultValue)
    {
        float fValue = 0;
        try{
            fValue = Float.parseFloat(strValue.replaceAll(",", ""));
        }catch(NumberFormatException nfe){
            fValue = defaultValue;
        }catch(NullPointerException npt){
            fValue = defaultValue;
        }
        return fValue;
    }

    public static BigDecimal toBigDecimal(String strValue, String defaultValue)
    {
        BigDecimal bdValue = new BigDecimal("0");
        try {
            bdValue = new BigDecimal(strValue.replaceAll(",", ""));
        } catch (NumberFormatException nfe) {
            bdValue = new BigDecimal(defaultValue);
        } catch (NullPointerException npt) {
            bdValue = new BigDecimal(defaultValue);
        }
        return bdValue;
    }

    public static long toLong(String strValue, long defaultValue)
    {
        long lValue = 0;
        try{
            lValue = (long) Double.parseDouble(strValue);
        }catch(NumberFormatException nfe){
            lValue = defaultValue;
        }catch(NullPointerException npt){
            lValue = defaultValue;
        }
        return lValue;
    }

    public static int toInteger(String strValue, int defaultValue)
    {
        int iValue = 0;
        try{
            iValue = (int) Double.parseDouble(strValue);
        }catch(NumberFormatException nfe){
            iValue = defaultValue;
        }catch(NullPointerException npt){
            iValue = defaultValue;
        }
        return iValue;
    }

    // rotate b, s (0-7) bits to the left
    public static byte rotLeft(byte b, int s)
    {
        int i = ((int)b & 0xFF) << (s & 7);
        return  (byte)((i & 0xFF) | (i >> 8));
    }
    public static byte rotRight(byte b, int s)
    {
        return rotLeft(b,8-(s & 7));
    }

    public static String compress2(String s)
    {
        String sReturn = null;
        try {
            sReturn = new String(compress3(s),"ISO-8859-1");
        }
        catch(UnsupportedEncodingException ufe) {
        }
        return sReturn;
    }
    public static String decompress2(String s)
    {
        String sReturn = null;
        try {
            sReturn = uncompress3(s.getBytes("ISO-8859-1"));
        }
        catch(UnsupportedEncodingException ufe) {
        }
        return sReturn;
    }

    public static byte[] compress3(String s)
    {
        Deflater defl = new Deflater(Deflater.BEST_SPEED);
        defl.setInput(s.getBytes());
        defl.finish();
        boolean done = false;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while (!done)
        {
            byte[] buf = new byte[256];
            int bufnum = defl.deflate(buf);
            bos.write(buf, 0, bufnum);
            if (bufnum < buf.length)
                done = true;
        }
        try
        {
            bos.flush();
            bos.close();
        }
        catch(IOException ioe) {}

        return(bos.toByteArray());
    }

    public static String uncompress3(byte[] b)
    {
        Inflater infl = new Inflater();
        infl.setInput(b);

        StringBuffer retval = new StringBuffer();
        boolean done = false;
        while (!done)
        {
            byte[] buf = new byte[256];
            try
            {
                int bufnum = infl.inflate(buf);
                retval.append(new String(buf, 0, bufnum));
                if (bufnum < buf.length)
                    done = true;
            }
            catch(DataFormatException dfe)
            {
                done = true;
            }
        }

        return(retval.toString());
    }

    public static void printStackTrace(Throwable throwMsg)
    {
        StringBuffer sb = new StringBuffer();
        if (throwMsg != null) {
            StringWriter writer = new StringWriter();
            throwMsg.printStackTrace(new PrintWriter(writer));
            writer.flush();
            sb.append(writer.toString());
            try {
                writer.close();
            }
            catch (IOException ioe) {
            }
        }
        //System.out.println(sb.toString());
        sb.setLength(0);
        sb = null;
    }

    public static void displayUnicode(String strInput)
    {
        try
        {
            byte []output = strInput.getBytes("UTF-16");
            // First 2 bytes are FF FE, so no need to display
            for (int j = 2; j < output.length; j++)
                if (output[j] < 0)
                    System.out.print(String.valueOf(256 + output[j]) + " ");
                else
                    System.out.print(String.valueOf(output[j]) + " ");
            //System.out.println("");
            output = null;
        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
            //System.out.println("Fail to show unicode");
        }
    }

    public static String toUTF8(String strInput)
    {
        try
        {
            return new String(strInput.getBytes("UTF-8"), "UTF-8");
        }
        catch(IOException ioe)
        {
            return strInput;
        }
    }

    private static String charToString(char[] in){
        String out = "";
        for (int i = 0 ; i < in.length ; i ++){
            out += Character.toString ((char) in[i]);
        }
        return out;
    }

    public static String getKey(String in){
        byte[] key;
        in = in.replace(" ", "").replace("[", "").replace("]", "");
        String[] sIn = in.split(",");
        if (sIn.length > 1){
            byte[] out = new byte[sIn.length];
            for (int i = 0 ; i < sIn.length ; i++){
                byte c = (byte)Integer.parseInt(sIn[i]);
                out[i] = c;
            }
            key = out;
        }
        else
            key = in.getBytes();

        //return charToString(key);
        return new String(key);
    }

    public static String getHttpKey()
    {
        MobileTraderApplication app = (MobileTraderApplication)MobileTraderApplication.context.getApplicationContext();
        String strUserID = app.data.getStrUser();
        if(app.isDemoPlatform == true && CompanySettings.HTTP_KEY_DEMO != null)
            return getKey(CompanySettings.HTTP_KEY_DEMO);
        else if(CompanySettings.checkProdServer() == 2 && CompanySettings.HTTP2_KEY != null)
            return getKey(CompanySettings.HTTP2_KEY);
        else if(CompanySettings.checkProdServer() == 3 && CompanySettings.HTTP3_KEY != null)
            return getKey(CompanySettings.HTTP3_KEY);
        else if(CompanySettings.checkProdServer() == 4 && CompanySettings.HTTP4_KEY != null)
            return getKey(CompanySettings.HTTP4_KEY);
        else if(CompanySettings.checkProdServer() == 5 && CompanySettings.HTTP5_KEY != null)
            return getKey(CompanySettings.HTTP5_KEY);
        else if(CompanySettings.checkProdServer() == 6 && CompanySettings.HTTP6_KEY != null)
            return getKey(CompanySettings.HTTP6_KEY);
        else if(CompanySettings.checkProdServer() == 7 && CompanySettings.HTTP7_KEY != null)
            return getKey(CompanySettings.HTTP7_KEY);
        else if(CompanySettings.checkProdServer() == 8 && CompanySettings.HTTP8_KEY != null)
            return getKey(CompanySettings.HTTP8_KEY);
        else if(CompanySettings.checkProdServer() == 9 && CompanySettings.HTTP9_KEY != null)
            return getKey(CompanySettings.HTTP9_KEY);
        else if(CompanySettings.checkProdServer() == 10 && CompanySettings.HTTP10_KEY != null)
            return getKey(CompanySettings.HTTP10_KEY);
        else
            return getKey(CompanySettings.HTTP_KEY);
    }

    public static String getContentHttpKey()
    {
        return getKey(CompanySettings.CONTENT_KEY);
    }

    public static String getCustomContentHttpKey()
    {
        if(CompanySettings.USE_HTTPKEY_AS_CONTENTKEY)
            return getKey(CompanySettings.HTTP_KEY);
        else
            return getKey("3KCakxUt4begfkgTl1gb");
    }

    public static String getPasswdKey(String strKey)
    {
        StringBuffer strTmpKey = new StringBuffer();

        strTmpKey.append(getKey(CompanySettings.PASSWORD_KEY));

        strTmpKey.append(strKey);

        String strReturnKey = strTmpKey.toString();
        strTmpKey = null;

        return strReturnKey;
    }

    public static String getINIPasswdKey()
    {
        StringBuffer strTmpKey = new StringBuffer();

        strTmpKey.append(getKey(CompanySettings.INIPASSWORD_KEY));

        String strReturnKey = strTmpKey.toString();
        strTmpKey = null;

        return strReturnKey;
    }

    public static String getMessageKey()
    {
        if( CompanySettings.hmFxServerMessageKeyMap != null )
        {
            MobileTraderApplication app = (MobileTraderApplication)MobileTraderApplication.context.getApplicationContext();
            String strUserID = app.data.getStrUser();
            int key = -1;
            if(app.isDemoPlatform == true){
                key = app.AppLauncherMessageKeyIndexForDemo;
            }
            else if( CompanySettings.checkProdServer() == 1 )
            {
                key = app.AppLauncherMessageKeyIndexForProd1;
            }
            else if( CompanySettings.checkProdServer() == 2 )
            {
                key = app.AppLauncherMessageKeyIndexForProd2;
            }
            else if( CompanySettings.checkProdServer() == 3 )
            {
                key = app.AppLauncherMessageKeyIndexForProd3;
            }
            else if( CompanySettings.checkProdServer() == 4 )
            {
                key = app.AppLauncherMessageKeyIndexForProd4;
            }
            else if( CompanySettings.checkProdServer() == 5 )
            {
                key = app.AppLauncherMessageKeyIndexForProd5;
            }

            if( CompanySettings.hmFxServerMessageKeyMap.containsKey(key) == true )
                return getKey( CompanySettings.hmFxServerMessageKeyMap.get(key));
            else
                return getKey(CompanySettings.MESSAGE_KEY);
        }
        else
            return getKey(CompanySettings.MESSAGE_KEY);
    }

    public static Locale getLanguageLocale(int iLanguage)
    {
        Locale returnLocale;
        switch (iLanguage)
        {
            case FXConstants.TRAD_CHINESE:
                returnLocale = Locale.TRADITIONAL_CHINESE;
                break;
            case FXConstants.SIMP_CHINESE:
                returnLocale = Locale.SIMPLIFIED_CHINESE;
                break;
            case FXConstants.JAPANESE:
                returnLocale = Locale.JAPANESE;
                break;
            default:
                returnLocale = Locale.ENGLISH;
                break;
        }

        return returnLocale;
    }

    public static String getResourceLabel(ResourceBundle resourceBundle, String name)
    {
        String strValue;
        try
        {
            strValue = resourceBundle.getString(name);

            if (resourceBundle.getLocale() != Locale.ENGLISH)
            {
                try
                {
                    String s = new String(strValue.getBytes("iso-8859-1"),"UTF-8");
                    strValue = s;
                }
                catch(Throwable t){}
            }
        }
        catch(MissingResourceException mre)
        {
            strValue = name;
        }

        return strValue;
    }

    public static String getDate(String sDate){
        //System.out.println(sDate);
        try {
            return ddf.format(sdf.parse(sDate));
        } catch (ParseException e) {
            try{
                return ddf.format(sdf2.parse(sDate));
            }catch(ParseException e2) {
                try{
                    return ddf.format(sdf3.parse(sDate));
                }catch(ParseException e3) {
                    return sDate;
                }
            }
        }
    }

    public static String getTime(Date date){
        return tdf.format(date);
    }

    public static String getShortTime(Date date){
        return tdf2.format(date);
    }


    public static String getTime(String sDate){
        //System.out.println(sDate);
        try {
            return tdf.format(sdf.parse(sDate));
        } catch (ParseException e) {
            try{
                return tdf.format(sdf2.parse(sDate));
            }catch(ParseException e2) {
                try{
                    return tdf.format(sdf3.parse(sDate));
                }catch(ParseException e3) {
                    return sDate;
                }
            }
        }
    }



    public static Date toDate(String sDate) {
        try {
            return sdf4.parse(sDate);
        } catch (ParseException e) {
            try{
                return sdf.parse(sDate);
            }catch(ParseException e2) {
                try{
                    return sdf2.parse(sDate);
                }catch(ParseException e3) {
                    try{
                        return sdf3.parse(sDate);
                    }catch(ParseException e4){
                        return null;
                    }
                }
            }
        }
    }

    public static String dateToString(Date date) {
        return sdf3.format(date);
    }

    public static String dateToNormalString(Date date) {
        return sdf5.format(date);
    }

    public static String dateToStatementString(Date date) {
        return sdf6.format(date);
    }

    public static String dateLiquidationHistoryFormat(String data) {
        try {
            return sdf5WithTime.format(sdf.parse(data));
        } catch (ParseException e) {
            try {
                return sdf5WithTime.format(sdf2.parse(data));
            } catch (ParseException e1) {
                try {
                    return sdf5WithTime.format(sdf3.parse(data));
                } catch (ParseException e2) {
                    return data;
                }
            }
        }
    }

    public static String dateToEngString(String sDate){
        //System.out.println(sDate);
        try {
            return sdf4.format(sdf3.parse(sDate));
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String timeToEngString(String sTime){
        //System.out.println(sDate);
        try {
            return tdf2.format(tdf.parse(sTime));
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static Date timeToDate(String sTime){
        try {
            return tdf.parse(sTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String dateToEngString(Date date){
        return sdf4.format(date);
    }

    public static ArrayList<String> toArrayList(CharSequence[] values){
        ArrayList<String> alResult = new ArrayList<String>();
        for(CharSequence cs : values){
            alResult.add((String) cs);
        }
        return alResult;
    }

    public static String getDateToDB(Date date){
        return sdf2.format(date);
    }

    public static Date getDateFromDB(String sDate){
        try {
            return sdf2.parse(sDate);
        } catch (ParseException e) {
            return null;
        }
    }

    public static int getLastDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        return cal.getActualMaximum(Calendar.DATE);
    }

    public static String displayListingDate(String sDate){
        try {
            return sdf5.format(ddf.parse(sDate));
        } catch (ParseException e) {
            try {
                return sdf5.format(sdf4.parse(sDate.trim()));
            } catch (ParseException e2) {
                return sDate;
            }
        }

    }

    public static String dateWithHourMToString(Date date) {
        return sdf2.format(date);
    }

    public static Boolean isSimplifiedChineses(){
        return Locale.getDefault().equals(Locale.SIMPLIFIED_CHINESE) || Locale.getDefault().equals(Locale.PRC);
    }

    public static Boolean isTraditionalChinese(){
        return Locale.getDefault().equals(Locale.TRADITIONAL_CHINESE) || Locale.getDefault().equals(Locale.TAIWAN) ||
                Locale.getDefault().getCountry().equals("HK");
    }

    /**
     * decode the Image
     * @param res - resource
     * @param id  - image id
     * @param requiredSize  - the requireSize of output bitmap
     * @return image
     */
    public static Bitmap decodeImage(Resources res, int id ,int requiredSize){
        try {
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(res, id, o);

            //Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE=requiredSize;
            int width_tmp=o.outWidth, height_tmp=o.outHeight;
            int scale=1;
            while(true){
                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
                    break;
                width_tmp/=2;
                height_tmp/=2;
                scale*=2;
            }

            //decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            //o2.inPreferredConfig = Bitmap.Config.ARGB_4444;
            o2.inSampleSize=scale;
            return BitmapFactory.decodeResource(res, id, o2);
        } catch (Exception e) {

        }
        return null;
    }

    public static String convertHtmlSpecChar(String str){
        String sTemp =  str.replace("&lt;", "<");
        sTemp = sTemp.replace("&gt;", ">");
        sTemp = sTemp.replace("&amp;lt;", "<");
        sTemp = sTemp.replace("&amp;gt;", ">");
        sTemp = sTemp.replace("&amp;apos;", "'");
        sTemp = sTemp.replace("&amp;nbsp;", " ");
        sTemp = sTemp.replace("&amp;amp;", "&");
        sTemp = sTemp.replace("&amp;quot;", "\"");
        sTemp = sTemp.replace("&amp;rdquo;", "\"");
        sTemp = sTemp.replace("&amp;ldquo;", "\"");
        sTemp = sTemp.replace("&quot;", "\"");
        sTemp = sTemp.replace("&rdquo;", "\"");
        sTemp = sTemp.replace("&ldquo;", "\"");
        return sTemp;
    }

    /**
     * Get IP address from first non-localhost interface
     * @param ipv4  true=return ipv4, false=return ipv6
     * @return  address or empty string
     */
    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress().toUpperCase();
                        boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 port suffix
                                return delim<0 ? sAddr : sAddr.substring(0, delim);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) { } // for now eat exceptions
        return "127.0.0.1";
    }

    public static int getLayoutIdById(String sCode){
        try{
            return R.layout.class.getField(sCode).getInt(null);
        }catch(Exception e){
            return -1;
        }
    }

    public static String getStringById(String sCode, Resources res){
        try{
            return res.getString(R.string.class.getField(sCode).getInt(null));
        }catch(Exception e){
            return sCode;
        }
    }

    public static int getIdById(String sCode){
        try{
            return R.id.class.getField(sCode).getInt(null);
        }catch(Exception e){
            return -1;
        }
    }

    public static int getArrayIdById(String sCode){
        try{
            return R.array.class.getField(sCode).getInt(null);
        }catch(Exception e){
            return -1;
        }
    }

    public static int getStringIdById(String sCode){
        try{
            return R.string.class.getField(sCode).getInt(null);
        }catch(Exception e){
            return -1;
        }
    }

    public static int getDrawableIdById(String sCode){
        try{
            return R.drawable.class.getField(sCode).getInt(null);
        }catch(Exception e){
            return -1;
        }
    }

    public static int getColorIdById(String sCode){
        try{
            return R.color.class.getField(sCode).getInt(null);
        }catch(Exception e){
            return -1;
        }
    }

    public static String decToHex(int decimalInt) {
        return Integer.toHexString(decimalInt);
    }

    public static int hexToDec(String hexString) {
        return Integer.parseInt(hexString, 16 );
    }

    public static String getMessageKey_LastVersion()
    {
        return new String(FXConstants.MESSAGE_KEY_LAST_VERSION);
    }

    public static BigDecimal getBigDecimal(JSONObject jsonObject, String key) throws NumberFormatException, JSONException {
        return new BigDecimal(jsonObject.getString(key));
    }

    public static JSONObject putBigDecimal(JSONObject jsonObject, String key, BigDecimal value) throws JSONException {
        return jsonObject.put(key, value.toPlainString());
    }

    public static ContractDefaultSetting getContractDefaultSetting(JSONObject jsonObject, String key) throws NumberFormatException, JSONException {
        JSONObject member = jsonObject.getJSONObject(key);
        if (!member.has("defaultTakeProfitOrderPips")) {
            throw new JSONException("defaultTakeProfitOrderPips missing in " + jsonObject.toString());
        }
        if (!member.has("defaultStopLossOrderPips")) {
            throw new JSONException("defaultTakeProfitOrderPips missing in " + jsonObject.toString());
        }
        ContractDefaultSettingBuilder builder = new ContractDefaultSettingBuilder();
        builder.setDefaultLotSize(new BigDecimal(member.getString("defaultLotSize")));
        builder.setDefaultSlippage(member.getInt("defaultSlippage"));
        builder.setDefaultTakeProfitOrderPips(member.isNull("defaultTakeProfitOrderPips") ? Optional.empty() : Optional.of(member.getInt("defaultTakeProfitOrderPips")));
        builder.setDefaultStopLossOrderPips(member.isNull("defaultStopLossOrderPips") ? Optional.empty() : Optional.of(member.getInt("defaultStopLossOrderPips")));
        return builder.createContractDefaultSetting();
    }

    public static JSONObject putContractDefaultSetting(JSONObject jsonObject, String key, ContractDefaultSetting contractDefaultSetting) throws JSONException {
        JSONObject member = new JSONObject();
        member.put("defaultLotSize", contractDefaultSetting.getDefaultLotSize().toPlainString());
        member.put("defaultSlippage", contractDefaultSetting.getDefaultSlippage());
        Optional<Integer> defaultTakeProfitOrderPips = contractDefaultSetting.getDefaultTakeProfitOrderPips();
        if (defaultTakeProfitOrderPips.isPresent()) {
            member.put("defaultTakeProfitOrderPips", defaultTakeProfitOrderPips.get());
        } else {
            member.put("defaultTakeProfitOrderPips", JSONObject.NULL);
        }
        Optional<Integer> defaultStopLossOrderPips = contractDefaultSetting.getDefaultStopLossOrderPips();
        if (defaultStopLossOrderPips.isPresent()) {
            member.put("defaultStopLossOrderPips", defaultStopLossOrderPips.get());
        } else {
            member.put("defaultStopLossOrderPips", JSONObject.NULL);
        }
        return jsonObject.put(key, member);
    }

    public static final ContractDefaultSetting EMPTY_CONTRACT_DEFAULT_SETTING = new ContractDefaultSettingBuilder()
            .setDefaultLotSize(new BigDecimal(CompanySettings.DEFAULT_LOT_SIZE))
            .setDefaultSlippage(CompanySettings.DefaultSlippageValue)
            .setDefaultTakeProfitOrderPips(Optional.empty())
            .setDefaultStopLossOrderPips(Optional.empty())
            .createContractDefaultSetting();

    public static final Comparator<OpenPositionRecord> getFIFOComparator() {
        return (o1, o2) -> {
            Integer i1 = o1.iRef;
            Integer i2 = o2.iRef;
            if (i1 > i2) {
                return -1;
            } else if (i2 > i1) {
                return 1;
            } else {
                return 0;
            }
        };
    }

    public static final Stream<ContractObj> getViewableContract(DataRepository dataRepository) {
        return getViewableContract(dataRepository, CompanySettings.SHOW_NON_TRADEABLE_ITEM);
    }

    public static final Stream<ContractObj> getViewableContract(DataRepository dataRepository, boolean showNonTradable) {
        List<String> contractSequenceSort = dataRepository.getContractSequenceSort();
        List<String> sequenceSort = contractSequenceSort.size() > 0 ? contractSequenceSort : dataRepository.getContractSequence();
        List<String> defaultSequence = dataRepository.getContractSequenceSortDefault();
        List<String> tradableContract = dataRepository.getTradableContract();
        Predicate<ContractObj> filter = contractObj -> contractObj.isViewable() && (showNonTradable || tradableContract.contains(contractObj.strContractCode));
        Comparator<ContractObj> comparator = (contractObj, contractObj1) -> {
            if (sequenceSort.contains(contractObj.strContractCode) && sequenceSort.contains(contractObj1.strContractCode)) {
                return sequenceSort.indexOf(contractObj.strContractCode) - sequenceSort.indexOf(contractObj1.strContractCode);
            }
            if (sequenceSort.contains(contractObj.strContractCode) && defaultSequence.contains(contractObj1.strContractCode)) {
                return -1;
            }
            if (defaultSequence.contains(contractObj.strContractCode) && sequenceSort.contains(contractObj1.strContractCode)) {
                return 1;
            }
            if (defaultSequence.contains(contractObj.strContractCode) && defaultSequence.contains(contractObj1.strContractCode)) {
                return defaultSequence.indexOf(contractObj.strContractCode) - defaultSequence.indexOf(contractObj1.strContractCode);
            }
            if (defaultSequence.contains(contractObj.strContractCode) && !defaultSequence.contains(contractObj1.strContractCode)) {
                return -1;
            }
            if (!defaultSequence.contains(contractObj.strContractCode) && defaultSequence.contains(contractObj1.strContractCode)) {
                return 1;
            }
            return contractObj.strContractCode.compareTo(contractObj1.strContractCode);
        };
        return dataRepository.getContractList().stream()
                .filter(filter)
                .sorted(comparator);
    }

    public static byte[] byteConcat(byte[] arg0, byte[] arg1)
    {
        byte[] b = new byte[arg0.length + arg1.length];
        System.arraycopy(arg0, 0, b, 0, arg0.length);
        System.arraycopy(arg1, 0, b, arg0.length, arg1.length);
        return b;
    }

    public static String getTradeKey()
    {
        return new String(CompanySettings.getTradeKey());
    }

    public static String MD5(String str) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte[] bs = md.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < bs.length; i++) {
                int v = bs[i] & 0xff;
                if (v < 16) {
                    sb.append(0);
                }
                sb.append(Integer.toHexString(v));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
    }
}