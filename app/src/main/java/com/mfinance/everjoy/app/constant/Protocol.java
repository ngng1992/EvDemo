package com.mfinance.everjoy.app.constant;

public class Protocol {
	public class LoginRequest{
		public final static String EMAIL = "email";
		public final static String PASSWORD = "passwd";
		public final static String PASSWORD_TOKEN = "pwdToken";
		public final static String SELF_IP = "selfip";
		public final static String SERVER_IP = "serverip";
		public final static String TYPE = "ptype";
		public final static String OTYPE = "otype";
		public final static String OPENID = "open_id";
		public final static String USERNAME = "username";
		public final static String OS_VERSION = "os.version";
		public final static String ACC = "acc";
		public final static String OTP = "otp";
	}
	
	public class LoginResponse{
		public final static String STATUS = "stat";
		public final static String MSG = "msgcd1";
		public final static String PASSWORD_TOKEN = "pwdToken";
		public final static String USERNAME = "username";
		public final static String ID = "id";
		public final static String PREFIX = "prefix";
		public final static String FIRST_LOGIN_CHANGE_PWD = "chgpwd";
		public final static String FAIL_COUNT = "failcount";
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

	public class Logout{
		public final static String REDIRECT = "redirect";
	}

	public class ChangePassword {
		public final static String USER_ID = "uid";
		public final static String OLD_PASSWORD = "opwd";
		public final static String NEW_PASSWORD = "npwd";
	}

	public class ResetPasswordRequest {
		public final static String ACC = "acc";
		public final static String EMAIL = "email";
		public final static String OLD_PASSWORD = "oldpwd";
		public final static String NEW_PASSWORD = "newpwd";
	}
	public class ResetPasswordResponse {
		public final static String STATUS = "stat";
	}
}


