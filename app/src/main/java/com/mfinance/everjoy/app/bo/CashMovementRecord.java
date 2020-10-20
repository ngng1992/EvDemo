package com.mfinance.everjoy.app.bo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

public class CashMovementRecord implements Parcelable {
	public static String userAccountNumber="";
	public static String userBankName="";

    public final String sAccountNumber;
    public final String sRef;
    public final String sBankName;
    public final String sBankAccount;
    public final String sAmount;
    public final String sStatus;
    public final String sRequestDate;
    public final String sUpdateDate;
    public final String lastUpdateBy;
    
	public CashMovementRecord(String sAccountNumber,
			String sRef, String sBankName, String sBankAccount, String sAmount,
			String sStatus, String sRequestDate, String sUpdateDate, String lastUpdateBy) {
		this.sAccountNumber = sAccountNumber;
		this.sRef = sRef;
		this.sBankName = sBankName;
		this.sBankAccount = sBankAccount;
		this.sAmount = String.format("%.2f", Double.valueOf(sAmount));
		this.sStatus = sStatus;
		this.sRequestDate = sRequestDate;
		this.sUpdateDate = sUpdateDate;
		this.lastUpdateBy = lastUpdateBy;
	}

	protected CashMovementRecord(Parcel in) {
		sAccountNumber = in.readString();
		sRef = in.readString();
		sBankName = in.readString();
		sBankAccount = in.readString();
		sAmount = in.readString();
		sStatus = in.readString();
		sRequestDate = in.readString();
		sUpdateDate = in.readString();
		lastUpdateBy = in.readString();
	}

	public static final Creator<CashMovementRecord> CREATOR = new Creator<CashMovementRecord>() {
		@Override
		public CashMovementRecord createFromParcel(Parcel in) {
			return new CashMovementRecord(in);
		}

		@Override
		public CashMovementRecord[] newArray(int size) {
			return new CashMovementRecord[size];
		}
	};

	public String getKey(){
		return sRef;
	}
	
	@Override
	public String toString() {
		return "CashMovementRecord [sAccountNumber=" + sAccountNumber
				+ ", sRef=" + sRef + ", sBankName=" + sBankName
				+ ", sBankAccount=" + sBankAccount + ", sAmount=" + sAmount
				+ ", sStatus=" + sStatus + ", sRequestDate=" + sRequestDate
				+ ", sUpdateDate=" + sUpdateDate + "]";
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(sAccountNumber);
		dest.writeString(sRef);
		dest.writeString(sBankName);
		dest.writeString(sBankAccount);
		dest.writeString(sAmount);
		dest.writeString(sStatus);
		dest.writeString(sRequestDate);
		dest.writeString(sUpdateDate);
		dest.writeString(lastUpdateBy);
	}
}
