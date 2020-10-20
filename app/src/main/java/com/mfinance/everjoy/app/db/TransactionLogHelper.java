package com.mfinance.everjoy.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TransactionLogHelper extends SQLiteOpenHelper {
	private static final String DB_FILE = "mobile_trader.db";
	private static final int DB_VERSION = 4;
    public static final String TABLE_NAME = "transaction_log";
    
	public TransactionLogHelper(Context context) {
		super(context, TransactionLogHelper.DB_FILE, null, TransactionLogHelper.DB_VERSION);
	}
    
	@Override
	public void onCreate(SQLiteDatabase db) {
		StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE " + TransactionLogHelper.TABLE_NAME + " (");
        sql.append("ID TEXT PRIMARY KEY");
        sql.append(", REF TEXT");
        sql.append(", CONTRACT TEXT ");
        sql.append(", ACCOUNT TEXT");
        sql.append(", BUYSELL TEXT");
        sql.append(", REQ_RATE REAL");
        sql.append(", STATUS INTEGER");
        sql.append(", STATUS_MSG TEXT");
        sql.append(", MSG_CODE INTEGER");
        sql.append(", MSG TEXT");
        sql.append(", REMARK_CODE INTEGER");
        sql.append(", TYPE INTEGER");
        sql.append(", AMOUNT INTEGER");
        sql.append(", CREATE_DATE TEXT");
        sql.append(", LAST_UPDATE TEXT");
        sql.append(", TRADE_DATE TEXT"); 
        
        sql.append(", I_STATUS_MSG INTEGER");
        sql.append(", S_MSG_CODE TEXT");
        sql.append(", ORDER_REF TEXT");
        sql.append(", LIQ_REF TEXT");
        sql.append(", L_REF TEXT");
        sql.append(", S_REF TEXT");
        sql.append(", REPLY TEXT");
        sql.append(", LIQ_METHOD TEXT");
        sql.append(", MSG_1 TEXT");

        sql.append(", IS_DEMO INT");
        sql.append(", OVERRIDE_MSG_CODE INT");
        sql.append(", MSG_ENGLISH TEXT");
        sql.append(", MSG_TRADITIONAL_CHINESE TEXT");
        sql.append(", MSG_SIMPLIFIED_CHINESE TEXT");
        sql.append(");");
        db.execSQL(sql.toString());
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		//System.out.println("onUpgrade");
		
		db.execSQL("DROP TABLE "+TransactionLogHelper.TABLE_NAME+";");
		StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE " + TransactionLogHelper.TABLE_NAME + " (");
        sql.append("ID TEXT PRIMARY KEY");
        sql.append(", REF TEXT");
        sql.append(", CONTRACT TEXT ");
        sql.append(", ACCOUNT TEXT");
        sql.append(", BUYSELL TEXT");
        sql.append(", REQ_RATE REAL");
        sql.append(", STATUS INTEGER");
        sql.append(", STATUS_MSG TEXT");
        sql.append(", MSG_CODE INTEGER");
        sql.append(", MSG TEXT");
        sql.append(", REMARK_CODE INTEGER");
        sql.append(", TYPE INTEGER");
        sql.append(", AMOUNT INTEGER");
        sql.append(", CREATE_DATE TEXT");
        sql.append(", LAST_UPDATE TEXT");  
        sql.append(", TRADE_DATE TEXT");
        
        sql.append(", I_STATUS_MSG INTEGER");
        sql.append(", S_MSG_CODE TEXT");
        sql.append(", ORDER_REF TEXT");
        sql.append(", LIQ_REF TEXT");
        sql.append(", L_REF TEXT");
        sql.append(", S_REF TEXT");
        sql.append(", REPLY TEXT");
        sql.append(", LIQ_METHOD TEXT");
        sql.append(", MSG_1 TEXT");
        
        sql.append(", IS_DEMO INT");
        sql.append(", OVERRIDE_MSG_CODE INT");
        sql.append(", MSG_ENGLISH TEXT");
        sql.append(", MSG_TRADITIONAL_CHINESE TEXT");
        sql.append(", MSG_SIMPLIFIED_CHINESE TEXT");
        sql.append(");");
        db.execSQL(sql.toString());
	}

}
