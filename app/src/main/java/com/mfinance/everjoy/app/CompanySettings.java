package com.mfinance.everjoy.app;

import com.mfinance.everjoy.app.MobileTraderApplication.LoginInfo;

import java.util.HashMap;

public class CompanySettings extends DefaultCompanySettings {
    public static String PROD_LICENSE_KEY = new net.java.truelicense.obfuscate.ObfuscatedString(new long[]{0xc3ce3407ddd0d157l, 0xe609e8c17ed8a93l, 0xee2c8740cbe55b0dl, 0xab187ce9e81e21bfl, 0xd4b9a53975632221l, 0xecc29a229481d787l, 0x22a76e66ab47efcdl, 0x60d073bfc4762b42l, 0x1fc3a05de44b4f39l, 0x442d89321e453d45l, 0xc286243c497c63a3l, 0xabea1ba5a6628d0cl, 0x1ed3103d7121d47bl}).toString() /* => "239147595177382836016065904824" */;
    public static String DEMO_LICENSE_KEY = new net.java.truelicense.obfuscate.ObfuscatedString(new long[]{0xf1fdbdf0b16630e0l, 0xfba87aeeefe06c3al, 0x6a18cc1122c5e0e6l, 0x4a83eefaffd4c3a9l, 0x3b065451a0ae430fl, 0x68b1ff3bbad1bf2l, 0xeb515c66d6884b95l, 0xe26fcdaef35c4e8l, 0x531d75dd595efde6l, 0xa12618c7a4f9ff6dl, 0x6a73aba68aacae4fl, 0x6e4f4b7cfc7b38l, 0x7e4c28eae0d09807l}).toString() /* => "423501713297557986692018448360" */;

    public static String HTTP_KEY = new net.java.truelicense.obfuscate.ObfuscatedString(new long[]{0x4faa17ead24b5d35l, 0x91cf22f6b85a4210l, 0x5d55c3f5f95090b8l, 0xa56d178f310e4fe9l, 0x228f8002d9f2fa61l, 0xdb847be81dbed42bl, 0x9639a1fdf7124c59l, 0x5a114372b94c8a45l, 0x8a6384db26f18ac2l, 0xa24946d75ac886ael}).toString();
    public static String PASSWORD_KEY = new net.java.truelicense.obfuscate.ObfuscatedString(new long[]{0x8ac4032f05b20401l, 0xfb693e56099f232bl, 0xa4df8bc63e006a97l, 0x406cb41c7aa54e1el, 0x1da9f10104147822l, 0x44c8d12f12b3761cl, 0x3c463103b775fe97l, 0xafcc7746fd696d5dl, 0x8c35ea7f8e6ff7ffl, 0xd1ff1f4c4a65254fl}).toString();
    public static String INIPASSWORD_KEY = new net.java.truelicense.obfuscate.ObfuscatedString(new long[]{0x29501909146989fl, 0xa910cd90d53c9d8al, 0xf65e648431a67e7l, 0x374732d50d9a613fl, 0x28cfd08011343952l, 0x85992b72c4c48c20l, 0x2487b5811c9029a8l, 0xc3a7dd180f89b5e0l, 0xaa895106d4fd0ac3l, 0x3a0bb6931fc57119l}).toString();
    public static String MESSAGE_KEY = new net.java.truelicense.obfuscate.ObfuscatedString(new long[]{0x5cae5f5e4f60e4c9l, 0xb4605b07e6693d6el, 0x2c91ad882714f617l, 0xaf2c68bea0b2642bl, 0xa5bbe75e6fdf0bd3l, 0xaafb4b35db620478l, 0xf633206cbe85a298l, 0xd54ed2aa295188d8l, 0xbac7f72d73253cd2l, 0x9a9b9146b6575043l}).toString();

//    static {
//		Log.e("p",	net.java.truelicense.obfuscate.ObfuscatedString.obfuscate("[50,51,57,49,52,55,53,57,53,49,55,55,51,56,50,56,51,54,48,49,54,48,54,53,57,48,52,56,50,52]"));//239147595177382836016065904824
//		Log.e("d",	net.java.truelicense.obfuscate.ObfuscatedString.obfuscate("[52,50,51,53,48,49,55,49,51,50,57,55,53,53,55,57,56,54,54,57,50,48,49,56,52,52,56,51,54,48]"));//423501713297557986692018448360
//
//		Log.e("h",	net.java.truelicense.obfuscate.ObfuscatedString.obfuscate("[111,89,54,118,120,51,73,52,77,104,55,99,57,53,74,115,52,117,53,57]"));//oY6vx3I4Mh7c95Js4u59
//		Log.e("pw",	net.java.truelicense.obfuscate.ObfuscatedString.obfuscate("[80,54,103,51,72,56,99,110,52,57,104,97,48,111,85,121,122,50,113,74]"));//P6g3H8cn49ha0oUyz2qJ
//
//		Log.e("ini",	net.java.truelicense.obfuscate.ObfuscatedString.obfuscate("[104,121,53,76,109,51,105,55,99,71,119,51,75,75,51,48,112,104,55,55]"));//hy5Lm3i7cGw3KK30ph77
//		Log.e("msg",	net.java.truelicense.obfuscate.ObfuscatedString.obfuscate("[121,117,52,111,81,49,50,118,77,52,56,89,99,54,118,48,111,80,113,51]"));//yu4oQ12vM48Yc6v0oPq3
//	}

    public static final boolean ENABLE_ORDER_OCO = true;
    public static final boolean ENABLE_DEAL_OCO = true;
    public static final boolean ENABLE_SLIPPAGE = true;

    public static final boolean ENABLE_WEBVIEW_DEMO_REGISTRATION = true;
    public static final boolean ENABLE_WEBVIEW_LOST_PWD = true;

    public final static String CONTRACTLIST_URL = ":24206/mobile_service/getContractList?";
    public static String DEMO_REPORT_GROUP_NAME = "DEMOPLATFM";
    public static String DEMO_REPORT_GROUP = "2";
    public static String PRODUCTION_REPORT_GROUP = "1";
    public static final boolean USE_NEW_DEAL_PROTOCOL = true;

    public static final int NUM_OF_LOT_DP = 1;

    public static final String DEFAULT_LOT_SIZE = "1.0";
    public static int MINIMUM_PASSWORD_SIZE = 8;
    public static int DefaultSlippageValue = 0;


    public static boolean newinterface = true;

    public static final boolean FOR_UAT = false; //USE DEMO PLATFORM ONLY ANYTIME
    public static final boolean FOR_TEST = true; //OVERRIDE ALL CONNECTION TO loginInfoTest

    /**
     * 设置服务器地址
     */
    public static final LoginInfo loginInfoTest = new LoginInfo("192.168.123.68", 15100);

    public static final String APP_UPDATE_URL = "https://dl.hg778899.com:18443/download/CROWNBULLIONLIMITED.apk";
    public static final String APP_UPDATE_URL_TYPE = null;

    public static final boolean isHexMessageEncode = true;

    public static boolean ENABLE_TRADER_PRICE_AGENT_CONNECTION = true;
    public static String PRICEAGENT_LOGIN_SECRET = "hm%(c03R8%*2bT^!xm9hf%pW4";
    public static String m_strWebLink = "https://crownpfsec.mfcloud.net:21206/web";
    public static String PriceAgentIP = "crownpfsec.mfcloud.net:25206,crownpfsec.mfcloud.net:25206,crownpfsec.mfcloud.net:25206,crownpfsec.mfcloud.net:25206,crownpfsec.mfcloud.net:25206";

    public static boolean getSelfIPBySSL = true;
    public static boolean AESCrypto = true;
    public static final boolean getMaxlotFromServer = false;

    public static void initValues(MobileTraderApplication app) {
        DEMO_REGISTRATION_KEYS = new HashMap<String, String>();
        DEMO_REGISTRATION_KEYS.put("EN", "905315488947943622710630852761");
        DEMO_REGISTRATION_KEYS.put("SC", "141926385034710467582269597830");
        DEMO_REGISTRATION_KEYS.put("TC", "126640601525079397934587834128");

        LOST_PWD_KEYS = new HashMap<String, String>();
        LOST_PWD_KEYS.put("PROD_EN", "073528516042134791696480598273");
        LOST_PWD_KEYS.put("PROD_SC", "249517866039483561705238402971");
        LOST_PWD_KEYS.put("PROD_TC", "048186329659738925304117542706");
        LOST_PWD_KEYS.put("DEMO_EN", "831747743601904681620953558292");
        LOST_PWD_KEYS.put("DEMO_SC", "910274655183923812509307476846");
        LOST_PWD_KEYS.put("DEMO_TC", "335643067980581996022771285441");

        String json = "[]";
        parseJson(app, json);
    }

    public static byte[] getSecretKey() {
        return (new Object() {
            int t;

            public byte[] process() {
                byte[] buf = new byte[32];
                t = 537344;
                buf[0] = (byte) (t >>> 8);
                t = 1238368256;
                buf[1] = (byte) (t >>> 18);
                t = -1694498816;
                buf[2] = (byte) (t >>> 23);
                t = 244608;
                buf[3] = (byte) (t >>> 7);
                t = -1908408320;
                buf[4] = (byte) (t >>> 22);
                t = 756;
                buf[5] = (byte) (t >>> 1);
                t = 100608;
                buf[6] = (byte) (t >>> 6);
                t = -1470103552;
                buf[7] = (byte) (t >>> 21);
                t = 16992;
                buf[8] = (byte) (t >>> 4);
                t = 1226309632;
                buf[9] = (byte) (t >>> 18);
                t = -1524629504;
                buf[10] = (byte) (t >>> 21);
                t = -1837105152;
                buf[11] = (byte) (t >>> 22);
                t = 43008;
                buf[12] = (byte) (t >>> 5);
                t = 27901952;
                buf[13] = (byte) (t >>> 13);
                t = 60342272;
                buf[14] = (byte) (t >>> 14);
                t = 6800;
                buf[15] = (byte) (t >>> 3);
                t = 1234698240;
                buf[16] = (byte) (t >>> 18);
                t = 60112896;
                buf[17] = (byte) (t >>> 14);
                t = 2729984;
                buf[18] = (byte) (t >>> 10);
                t = 17792;
                buf[19] = (byte) (t >>> 4);
                t = 243456;
                buf[20] = (byte) (t >>> 7);
                t = 2248;
                buf[21] = (byte) (t >>> 2);
                t = 13049856;
                buf[22] = (byte) (t >>> 12);
                t = 2675712;
                buf[23] = (byte) (t >>> 10);
                t = 1239552;
                buf[24] = (byte) (t >>> 9);
                t = 2268;
                buf[25] = (byte) (t >>> 2);
                t = 60686336;
                buf[26] = (byte) (t >>> 14);
                t = 5834752;
                buf[27] = (byte) (t >>> 11);
                t = 59785216;
                buf[28] = (byte) (t >>> 14);
                t = 586;
                buf[29] = (byte) (t >>> 1);
                t = 1145044992;
                buf[30] = (byte) (t >>> 20);
                t = 12754944;
                buf[31] = (byte) (t >>> 12);
                return buf;
            }
        }.process());
    }


    public static byte[] getTradeKey() {
        return (new Object() {
            int t;

            public byte[] process() {
                byte[] buf = new byte[32];
                t = 60325888;
                buf[0] = (byte) (t >>> 14);
                t = -1440743424;
                buf[1] = (byte) (t >>> 21);
                t = 550144;
                buf[2] = (byte) (t >>> 8);
                t = -1795162112;
                buf[3] = (byte) (t >>> 22);
                t = -1392508928;
                buf[4] = (byte) (t >>> 21);
                t = 274137088;
                buf[5] = (byte) (t >>> 16);
                t = 1828716544;
                buf[6] = (byte) (t >>> 24);
                t = 240896;
                buf[7] = (byte) (t >>> 7);
                t = 1237504;
                buf[8] = (byte) (t >>> 9);
                t = 127533056;
                buf[9] = (byte) (t >>> 15);
                t = -1684013056;
                buf[10] = (byte) (t >>> 19);
                t = 236416;
                buf[11] = (byte) (t >>> 7);
                t = 28237824;
                buf[12] = (byte) (t >>> 13);
                t = 1108344832;
                buf[13] = (byte) (t >>> 20);
                t = 1242112;
                buf[14] = (byte) (t >>> 9);
                t = 1217658880;
                buf[15] = (byte) (t >>> 18);
                t = 59817984;
                buf[16] = (byte) (t >>> 14);
                t = -1722810368;
                buf[17] = (byte) (t >>> 19);
                t = 102784;
                buf[18] = (byte) (t >>> 6);
                t = 2228;
                buf[19] = (byte) (t >>> 2);
                t = 5918720;
                buf[20] = (byte) (t >>> 11);
                t = 1175453696;
                buf[21] = (byte) (t >>> 20);
                t = -1703936000;
                buf[22] = (byte) (t >>> 19);
                t = 712;
                buf[23] = (byte) (t >>> 1);
                t = 273809408;
                buf[24] = (byte) (t >>> 16);
                t = 716;
                buf[25] = (byte) (t >>> 1);
                t = 581566464;
                buf[26] = (byte) (t >>> 17);
                t = -1689255936;
                buf[27] = (byte) (t >>> 19);
                t = 6848;
                buf[28] = (byte) (t >>> 3);
                t = 275644416;
                buf[29] = (byte) (t >>> 16);
                t = -1728053248;
                buf[30] = (byte) (t >>> 23);
                t = -1371537408;
                buf[31] = (byte) (t >>> 21);
                return buf;
            }
        }.process());
    }
}
