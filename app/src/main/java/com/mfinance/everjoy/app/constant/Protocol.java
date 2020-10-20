package com.mfinance.everjoy.app.constant;

public class Protocol {
	public class LoginRequest{
		public final static String JAVA_VERSION = "java.version";
		public final static String OS_ARCH = "os.arch";
		public final static String OS_NAME = "os.name";
		public final static String OS_VERSION = "os.version";
		public final static String USER_DIR = "user.dir";
		public final static String USER_HOME = "user.home";
		public final static String VERSION = "appver";
		public final static String MY_ID = "myid";
		public final static String MY_NAME = "myname";
		public final static String PASSWORD = "passwd";
		public final static String ROLE = "role";
		public final static String LOGIN_TYPE = "login_type";
		public final static String NETWORK_MAC = "macaddr";
		public final static String NETWORK_MAC_OLD = "network.mac";
		public final static String NETWORK_MAC_SUCCESS = "identifier";
		public final static String TRADE_CURR = "trade_curr";
		public final static String SERVER_IP = "serverip";
		public final static String SELF_IP = "selfip";
		public final static String RPT_GROUP = "rpgp";
		public final static String TYPE = "ptype";
		public final static String IDENTITY_PASSED = "ic_ok";
		public final static String TWO_FA_OTP = "otp";
		public final static String BY_MANUAL = "bymanual";
		public final static String PUSH_TOKEN = "push_token";
		public final static String PUSH_LOCALE = "push_locale";
	}

	public class BuySellStrengthSetting
	{
		public final static String CONTRACT = "contract";
		public final static String OPEN_SECOND = "opsec";
		public final static String LIQUIDATION_SECOND = "liqsec";
	}
	
	public class LoginResponse{
		public final static String STATUS = "stat";
		public final static String MESSAGE_CODE = "msgcd1";
		public final static String TRADE_DATE = "tradedate";
		public final static String FAIL = "fail";
		
		public final static String DISPLAY_CUR = "display_cur";
		public final static String CONV_RATE = "conv_rate";
		public final static String CONV_RATE_OP = "conv_rate_op";
		
		public final static String WEB = "web";
		public final static String CHARTLINK = "chartlink";
		
		public final static String STPORDER = "STPOrder";
		public final static String NEW_PRICE_MESSAGE = "npm";
		
		public final static String IDENTITY_CHECK = "stat";

		public final static String TWO_FA = "twofa";
		public final static String TWO_FA_PREFIX = "prefix";
		public final static String TWO_FA_EXPIRY = "expiry";
		public final static String TWO_FA_MOBILE = "mobile";
		public final static String TWO_FA_EMAIL = "email";
		public final static String TWO_FA_OTP = "otp";

		public final static String PWD_CRL_MSG = "passctrl_msg";
		public final static String PWD_CRL_AGE = "passwd_age";
		public final static String PWD_SYMBOL_LIST = "passctrl_sblist";
		public final static String FIRST_LOGIN = "first_login";

		public final static String SESS_TIMEOUT_ALERT = "sesstimeoutalert";
		public final static String SESS_TIMEOUT_DISCONN = "sesstimeoutdisconn";
		public final static String HEXADECIMAL_MESSAGE_LENGTH = "hexlen";
	}	
	
	public class AccountInfo{
		public final static String NUMBER_OF_RECORD = "NOC";
		public final static String ACTION = "_is";
		public final static String HEDGED = "hedged";
		public final static String ACCOUNT = "ac";
		public final static String ACCOUNT_NAME = "acn";
		public final static String CREDIT_LIMIT ="cl";
		public final static String GROUP_CODE = "gc";
		public final static String PARTIAL_EQUITY = "pe";
		public final static String AE_HIERARCHY = "hier";
		public final static String AE = "ae";
		public final static String SETTLEMENT_CURRENCY = "sc";
		public final static String BALANCE = "bl";
		public final static String NET = "net";
		public final static String USER_TYPE = "type";
		public final static String MARGIN_CURRENCY = "mcu";
		public final static String NEW_POSITION_MARGIN = "npm";
		public final static String HEDGE_POSITION_MARGIN = "hpm";
		public final static String MARGIN_ALERT_RATIO = "famalert";
		public final static String MARGIN_CALL_RATIO = "famcall";
		public final static String MARGIN_CUT_RATIO = "famcut";
		public final static String IS_AUTO_CUT_LOSS = "alk";
		public final static String PRIMARY_BALANCE = "pbl";
		public final static String SECONDARY_BALANCE = "sbl";
		public final static String LAST_BALANCE = "lastbl";
		public final static String PROFIT_AND_LOSS = "daypl";
		public final static String FIXED_SETTLEMENT_EXCHANGE_RATE = "fser";
		public final static String SECURITY_REF = "sref";
		public final static String MIN_LOT_LIMIT = "mll";
		public final static String MIN_INC_LOT_LIMIT = "mliu";
		public final static String SYNC_NUM = "sync";
		public final static String CREDIT_RATIO ="cr";
		public final static String DELAY = "ddelay";
		public final static String BALANCE_INTEREST = "balin";
		public final static String ACCU_BALANCE_INTEREST = "abalin";
		public final static String INIT_MARGIN = "im";
		public final static String LEVERAGE = "leverage";
		public final static String REALIZED_PNL = "daypl";
		public final static String TRADABLE_MARGIN = "tradm";
		public final static String NON_TRADABLE_MARGIN = "ntradm";
		public final static String NON_TRADABLE_CREDIT = "ntradc";
		public final static String CREDIT_TYPE = "ct";
		public final static String FREE_LOT = "fl";
		public final static String ENABLE_APP_DEPOSIT = "ead";
		public final static String ENABLE_APP_WITHDRAWAL = "eaw";
		public final static String CALL_MARGIN = "cm";
	}
	
	public class PriceUpdate{
		public final static String NUMBER_OF_RECORD = "noitem";
		public final static String ITEM_NAME = "mkt";
		public final static String ACTION = "_is";
		public final static String DEMICAL_PLACE = "dp";
		public final static String BID = "bid";
		public final static String ASK = "ask";
		public final static String BIDLOT = "bidlot";
		public final static String ASKLOT = "asklot";
		public final static String HIGH = "hi";
		public final static String LOW = "low";
		public final static String HIGH_BID = "hib";
		public final static String LOW_BID = "lowb";
		public final static String HIGH_ASK = "hia";
		public final static String HIGH_ASK_PS = "hiask";
		public final static String LOW_ASK = "lowa";
		public final static String LOW_ASK_PS = "lowask";
		public final static String TIME = "ti";
		public final static String DIRECT_QUOTE = "dq";
		public final static String BSD = "bsd";
		public final static String BASE_DIRECT_QUOTE = "bdq";
		public final static String BASE_BID = "bbid";
		public final static String BASE_ASK = "bask";
		public final static String COUNTER_DIRECT_QUOTE = "cdq";
		public final static String COUNTER_RATE = "crate";
		public final static String RATE_VARIANCE = "ratevari";
		public final static String ITEM_STATUS = "sta";
		public final static String ITEM_CATEGORY = "cat";
		public final static String PRICE_NAME = "name";		
		public final static String CONTRACT_SIZE = "cs";
		public final static String ORDER_PIPS = "op";
		public final static String BASE_CURRENCY = "bc";
		public final static String COUNTER_CURRENCY = "cc";
		public final static String BID_INTEREST_RATE = "bint";
		public final static String ASK_INTEREST_RATE = "aint";
		public final static String ALLOW_VIEW = "view";
		public final static String ALLOW_TRADE = "trade";
		public final static String COUNTER_DIRECT_QUTO = "cdq";
		public final static String MAX_TRAIL_STOP = "tsmax";
		public final static String MIN_TRAIL_STOP = "tsmin";
		public final static String EN_PRICE_NAME = "ename";
		public final static String TC_PRICE_NAME = "tcname";
		public final static String SC_PRICE_NAME = "scname";
		public final static String JP_PRICE_NAME = "japname";
		public final static String MEAN_RATE = "mrate";
		public final static String SUPPENDED = "sup";
		public final static String INTERNET_VIEW = "view";
		public final static String INTERNET_TRADE = "trade";
		public final static String MAX_LOT_LIMIT = "maxlot";
		public final static String FULL = "full";
		public final static String INTEREST_RATE_METHOD = "intmthd";
	}	
	
	public class ContractSettingUpdate{
		public final static String NUMBER_OF_RECORD = "noitem";
		public final static String GROUP_CODE = "gc";
		public final static String ACCOUNT = "ac";
		public final static String ITEM_NAME = "mkt";
		public final static String BID_ADJUST = "bs";
		public final static String ASK_ADJUST = "as";
		public final static String BID_ADJUST_RPT = "rbs";
		public final static String ASK_ADJUST_RPT = "ras";
		public final static String BID_ADJUST_OTX = "ba";
		public final static String ASK_ADJUST_OTX = "aa";
		public final static String ORDER_PIPS = "op";
		public final static String TRADABLE = "st";
		public final static String MIN_TRADE_LOT = "mtl";
		public final static String INCREMENT_TRADE_LOT = "itl";
		public final static String MAX_LOT_LIMIT = "maxtl";
		public final static String AUTO_DEAL_AMOUT = "ada";
	}

	
	public class AddDealRequest{
		public final static String GROUP_CODE = "gc";
		public final static String ACCOUNT_CODE = "ac";
		public final static String BUY_SELL = "b/s";
		public final static String REQUEST_PRICE = "price";
		public final static String CONTRACT = "contract";
		public final static String AMOUNT = "amount";
		public final static String CONTRACT_SIZE = "cs";
		public final static String LIQUIDATION_METHOD = "liqmethod";
		public final static String COMMENT = "comment";
		public final static String AUTO_GEN = "autogen";
		public final static String PEND_ID = "pid";
		public final static String TRUE_MARKET = "bUnrestrictedMarket";

		public final static String LIMIT_PRICE = "lprice";
		public final static String LIMIT_GT = "lgoodtil";
		public final static String STOP_PRICE = "sprice";
		public final static String STOP_GT = "sgoodtil";			
		public final static String SLIPPAGE = "acceptpips";
		// Use new field name, compatibility concern since this is used for talking with fxserver
		public final static String SLIPPAGE_NEW = "mktrange";
		public final static String REQUEST_AMOUNT = "reqamount";
		public final static String CTIME = "ctime";
	}
	
	public class LiquidationRequest{
		public final static String GROUP_CODE = "gc";
		public final static String ACCOUNT_CODE = "ac";
		public final static String BUY_SELL = "b/s";
		public final static String REQUEST_PRICE = "price";
		public final static String CONTRACT = "contract";
		public final static String AMOUNT = "amount";
		public final static String CONTRACT_SIZE = "cs";
		public final static String LIQUIDATION_METHOD = "liqmethod";
		public final static String LIQUIDATION_REF = "liqref";
		public final static String COMMENT = "comment";
		public final static String AUTO_GEN = "autogen";
		public final static String PEND_ID = "pid";		
		public final static String SLIPPAGE = "acceptpips";
		// Use new field name, compatibility concern since this is used for talking with fxserver
		public final static String SLIPPAGE_NEW = "mktrange";
		public final static String REQUEST_AMOUNT = "reqamount";
		public final static String TRUE_MARKET = "bUnrestrictedMarket";
	}	
	
	public class TransactionMessage{
		public final static String PEND_ID = "pid";
		public final static String NEW_PEND_ID = "npid";
		public final static String ACTION_CODE = "code";
		public final static String REPLY = "reply";
		public final static String NUM_OF_MESSAGE = "msgno";
		public final static String MESSAGE = "msg";
		public final static String MESSAGE_CODE = "msgcd";
		public final static  String LIQUIDATION_METHOD = "liqmethod";
		public final static  String LIQUIDATION_REF = "liqref";
		public final static  String ORDER_REF = "orderno";
		public final static  String LIMIT_REF = "lref";
		public final static  String STOP_REF = "sref";
		public final static  String AMOUNT = "amount";
		public final static  String EXAMOUNT = "examount";
		public final static  String XML = "msgxml";
		
		public class MsgXml
		{
			public final static  String EXECUTION_PRICE = "Execution_Price";
			
		}
	}
	
	public class RunningOrderMessage{
		public final static String STATUS = "status";
		public final static String UNKNOWN = "end";
		public final static String NUMBER_OF_RECORD = "noitem";
		public final static String ACTION = "_is";		
		public final static String ORDER_REF = "ono";
		public final static String ACCOUNT = "ac";
		public final static String GROUP_CODE = "ac";
		public final static String ITEM_NAME = "mkt";
		public final static String BUY_SELL = "b/s";
		public final static String TRADE_DATE = "amended";
		public final static String PRICE = "price";
		public final static String AMOUNT = "amount";
		public final static String LIMIT_OR_STOP = "s/l";
		public final static String GOOD_TILL_TYPE = "gt";
		public final static String GOOD_TILL_TIME = "gttime";
		public final static String DEALER = "dealer";
		public final static String DEMICAL_PLACE = "dp";
		public final static String CONTRACT_SIZE = "cs";
		public final static String LIQUIDATION_METHOD = "liqmethod";
		public final static String LIQUIDATION_RATE = "liqrate";
		public final static String LIQUIDATION_REF = "liqref";
		public final static String OCO_REF = "ocoref";
		public final static String TRAILING_STOP = "trail";
		public final static String PROFIT_RATE = "profitrate";
		public final static String CUT_RATE = "cutrate";
	}	

	public class AddOrderRequest{
		public final static String GROUP_CODE = "gc";
		public final static String ACCOUNT_CODE = "ac";
		public final static String ACTION = "order";
		public final static String BUY_SELL = "b/s";
		public final static String REQUEST_PRICE = "price";
		public final static String CONTRACT = "contract";
		public final static String AMOUNT = "amount";
		public final static String CONTRACT_SIZE = "cs";
		public final static String LIMIT_STOP = "otype";
		public final static String GOOD_TILL = "goodtil";
		public final static String GOOD_TILL_DATE = "gtdate";		
		public final static String GOOD_TILL_TIME = "gttime";
		public final static String PROFIT_GOOD_TILL = "profitgood";
		public final static String CUT_GOOD_TILL = "cutgood";
		public final static String LIQUIDATION_METHOD = "liqmethod";
		public final static String LIQUIDATION_REF = "liqref";
		public final static String COMMENT = "comment";
		public final static String OCO_REF = "ocoref";
		public final static String LIQUIDATION_RATE = "liqrate";
		public final static String TRAILING_STOP = "trail";
		public final static String CURRENT_PRICE = "refrate";
		public final static String PEND_ID = "pid";		
		public final static String PROFIT_RATE = "profitrate";
		public final static String CUT_RATE = "cutrate";		
	}	
	
	public class CancelOrderRequest{
		public final static String ORDER_REF = "orderno";		
		public final static String GROUP_CODE = "gc";
		public final static String ACCOUNT_CODE = "ac";
		public final static String ACTION = "order";	
		public final static String REPLY = "reply";
		public final static String TYPE = "type";
		public final static String ORDER_TYPE = "otype";
		public final static String OVERRIDE = "override";
		public final static String USER_ID = "orid";
		public final static String USER_PASSWORD = "orpwd";	
		public final static String DIRECT = "direct";	
		public final static String IS_AUTO_DEAL = "autodeal";	
		public final static String ROLE = "role";
		public final static String LIQUIDATION_METHOD = "liqmethod";	
		public final static String LIQUIDATION_REF = "liqref";
		public final static String COMMENT = "comment";	
		public final static String OCO = "ocoref";
		public final static String LIQUIDATION_RATE = "liqrate";		
		public final static String PEND_ID = "pid";		
	}	
	
	public class EditOrderRequest{		
		public final static String GROUP_CODE = "gc";
		public final static String ACCOUNT_CODE = "ac";
		public final static String ACTION = "order";
		public final static String ORDER_REF = "orderno";
		public final static String BUY_SELL = "b/s";
		public final static String REQUEST_PRICE = "price";
		public final static String CONTRACT = "contract";
		public final static String AMOUNT = "amount";
		public final static String CONTRACT_SIZE = "cs";
		public final static String LIMIT_STOP = "otype";
		public final static String GOOD_TILL = "goodtil";
		public final static String GOOD_TILL_DATE = "gtdate";		
		public final static String GOOD_TILL_TIME = "gttime";
		public final static String PROFIT_GOOD_TILL = "profitgood";
		public final static String CUT_GOOD_TILL = "cutgood";
		public final static String LIQUIDATION_METHOD = "liqmethod";
		public final static String LIQUIDATION_REF = "liqref";
		public final static String COMMENT = "comment";
		public final static String OCO_REF = "ocoref";
		public final static String LIQUIDATION_RATE = "liqrate";
		public final static String TRAILING_STOP = "trail";
		public final static String CURRENT_PRICE = "refrate";
		public final static String PEND_ID = "pid";		
		public final static String PROFIT_RATE = "profitrate";
		public final static String CUT_RATE = "cutrate";	
	}	
	
	public class ExecutedOrderMessage{
		public final static String QUERY_TYPE = "rettype";
		public final static String STATUS = "status";
		public final static String UNKNOWN = "end";
		public final static String NUMBER_OF_RECORD = "noitem";
		public final static String ACTION = "_is";		
		public final static String ORDER_REF = "ono";
		public final static String ACCOUNT = "ac";
		public final static String GROUP_CODE = "ac";
		public final static String ITEM_NAME = "mkt";
		public final static String BUY_SELL = "b/s";
		public final static String EXECUTED_DATE = "executed"; 
		public final static String CREATED_DATE = "created";
		public final static String PRICE = "price";
		public final static String AMOUNT = "amount";
		public final static String LIMIT_OR_STOP = "s/l";
		public final static String GOOD_TILL_TYPE = "gt";
		public final static String GOOD_TILL_TIME = "gttime";
		public final static String DEALER = "dealer";
		public final static String DEMICAL_PLACE = "dp";
		public final static String CONTRACT_SIZE = "cs";
		public final static String LIQUIDATION_METHOD = "liqmethod";
		public final static String LIQUIDATION_RATE = "liqrate";
		public final static String LIQUIDATION_REF = "liqref";
		public final static String OCO_REF = "ocoref";
		public final static String EXECUTED_AMOUNT = "examount";
	}
	
	public class ExecutedOrderHistoryRequestMessage{	
		public final static String USER_TYPE = "type";
		public final static String GET_ALL = "ga";		
		public final static String FROM = "fd";
		public final static String TO = "td";		
		public final static String CONTRACT = "contract";
		public final static String ACCOUNT = "ac";
		public final static String ROLE = "role";
		public final static String GROUP_CODE = "gc";
		public final static String BUY_SELL = "b/s";		
	}	
	
	public class CancelledOrderMessage{
		public final static String QUERY_TYPE = "rettype";
		public final static String STATUS = "status";
		public final static String UNKNOWN = "end";
		public final static String NUMBER_OF_RECORD = "noitem";
		public final static String ACTION = "_is";		
		public final static String ORDER_REF = "ono";
		public final static String ACCOUNT = "ac";
		public final static String GROUP_CODE = "ac";
		public final static String ITEM_NAME = "mkt";
		public final static String BUY_SELL = "b/s";
		public final static String EXECUTED_DATE = "executed"; 
		public final static String CREATED_DATE = "created";
		public final static String PRICE = "price";
		public final static String AMOUNT = "amount";
		public final static String LIMIT_OR_STOP = "s/l";
		public final static String GOOD_TILL_TYPE = "gt";
		public final static String GOOD_TILL_TIME = "gttime";
		public final static String DEALER = "dealer";
		public final static String DEMICAL_PLACE = "dp";
		public final static String CONTRACT_SIZE = "cs";
		public final static String LIQUIDATION_METHOD = "liqmethod";
		public final static String LIQUIDATION_RATE = "liqrate";
		public final static String LIQUIDATION_REF = "liqref";
		public final static String OCO_REF = "ocoref";
	}	
	
	public class LiquidationHistoryMessage{
		public final static String QUERY_TYPE = "rettype";
		public final static String STATUS = "status";
		public final static String NUMBER_OF_RECORD = "noitem";
		public final static String AMOUNT = "qty";	
		public final static String ACTION = "_is";		
		public final static String EXEC_DATE = "ed";
		public final static String BUY_REF = "bno";
		public final static String BUY_DATE = "bed";
		public final static String BUY_RATE = "bep";
		public final static String SELL_REF = "sno";
		public final static String SELL_DATE = "sed";
		public final static String SELL_RATE = "sep";
		public final static String CONTRACT = "ic";
		public final static String COMMISSION = "com";
		public final static String PL = "pl";
		public final static String APL = "apl";
		public final static String WHICH_IS_LIQ = "wil";
		public final static String RUNNING_RATE = "rrate";
		public final static String INTEREST = "in";
		public final static String FLOATINTEREST = "fin";
	}	
	
	public class CashMovementHistoryMessage{
		public final static String UPDATE = "update";
		public final static String NOTIEM = "noitem";
		public final static String TOID = "toid";
		public final static String ACCOUNT = "ac";	
		public final static String REF = "ref";		
		public final static String BANKNAME = "bn";
		public final static String BANKNUMBER = "ba";
		public final static String AMOUNT = "amt";
		public final static String STATUS = "st";
		public final static String DISPLAY_STATUS = "dst";
		public final static String CREATEDATE = "rd";
		public final static String SYSTEMTIME = "up";
		public final static String GROUP_CODE = "gc";
		public final static String LAST_UPDATE_BY = "lu";
	}
	public class CashMovementRequest{
		public final static String AMOUNT = "amount";
		public final static String TYPE = "type";
		public final static String BANKACCOUNT = "bankacc";
		public final static String REF = "ref";
	}
	
	public class SystemMessage{
		public final static String MSG = "msg";
		public final static String MSGCD = "msgcd";
		public final static String MSGNO = "msgno";
		public final static String LIMIT_REF = "lref";
		public final static String STOP_REF = "sref";
		public final static String PROFIT_ORDER_REF = "profitorderref";
		public final static String CUT_ORDER_REF = "cutorderref";
		
	}
	
	public class ReportGroupContractSettingUpdate{
		public final static String NUMBER_OF_RECORD = "NOC";
		public final static String REPORT_GROUP = "rg";
		public final static String ITEM_NAME = "mkt";
		public final static String SPREAD = "sp";
		public final static String OFFSET = "of";
		public final static String ORDER_PIPS = "op";
		public final static String STOP_TRADE = "st";
		public final static String VIEWABLE = "va";
	}
	
	public class ContractHighLowUpdate{
		public final static String NUMBER_OF_RECORD = "NOC";
		public final static String REPORT_GROUP = "rg";
		public final static String ITEM_NAME = "mkt";
		public final static String HIGH_BID = "hb";
		public final static String HIGH_ASK = "ha";
		public final static String LOW_BID = "lb";
		public final static String LOW_ASK = "la";
	}
	
	public class ReportError{
		public final static String MESSAGE = "msg";
	}
	
	public class Logout{
		public final static String REDIRECT = "redirect";
	}
	
	public class DealFramOpenClose{
		public final static String SEND_DEAL_FRAME_OPEN_CLOSE_ACTION = "act";
		public final static String SEND_DEAL_FRAME_OPEN_CLOSE_ACCOUNT = "ac";
		public final static String SEND_DEAL_FRAME_OPEN_CLOSE_CONTRACT = "contract";
		public final static String SEND_DEAL_FRAME_OPEN_CLOSE_BUY_SELL = "b/s";
		public final static String SEND_DEAL_FRAME_OPEN_CLOSE_TIME = "time";
		public final static String SEND_DEAL_FRAME_OPEN_CLOSE_NUM_OF_ITEM = "noitem";	
	}

	public class ChangePassword {
		public final static String USER_ID = "uid";
		public final static String OLD_PASSWORD = "opwd";
		public final static String NEW_PASSWORD = "npwd";
	}

	public class LiquidateAllRequest {
		public final static String PID = "pid";
		public final static String TYPE = "type";
		public final static String MARKET_RANGE = "mktrange";
		public final static String NUMBER_OF_CONTRACT = "noc";
		public final static String ITEM_CONTRACT = "contract";
		public final static String ITEM_BID = "bid";
		public final static String ITEM_ASK = "ask";
		public final static String ITEM_TAG = "tag";
		public final static String NUMBER_OF_POSITION = "nop";
		public final static String ITEM_POSITION_REF = "ref";
		public final static String ITEM_POSITION_AMOUNT = "amount";
		public final static String ITEM_POSITION_PPID = "ppid";
	}

	public class LiquidateAllResponse {
		public final static String PID = "pid";
		public final static String STATUS = "status";
	}
}


