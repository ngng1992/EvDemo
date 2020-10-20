package com.mfinance.everjoy.app.bo;

public class CashMovementRecordBuilder {
    private String sAccountNumber;
    private String sRef;
    private String sBankName;
    private String sBankAccount;
    private String sAmount;
    private String sStatus;
    private String sRequestDate;
    private String sUpdateDate;
    private String lastUpdateBy;
    public CashMovementRecordBuilder() {

    }

    public CashMovementRecordBuilder(CashMovementRecord r) {
        this.sAccountNumber = r.sAccountNumber;
        this.sRef = r.sRef;
        this.sBankName = r.sBankName;
        this.sBankAccount = r.sBankAccount;
        this.sAmount = r.sAmount;
        this.sStatus = r.sStatus;
        this.sRequestDate = r.sRequestDate;
        this.sUpdateDate = r.sUpdateDate;
        this.lastUpdateBy = r.lastUpdateBy;
    }
    public CashMovementRecordBuilder setsAccountNumber(String sAccountNumber) {
        this.sAccountNumber = sAccountNumber;
        return this;
    }

    public CashMovementRecordBuilder setsRef(String sRef) {
        this.sRef = sRef;
        return this;
    }

    public CashMovementRecordBuilder setsBankName(String sBankName) {
        this.sBankName = sBankName;
        return this;
    }

    public CashMovementRecordBuilder setsBankAccount(String sBankAccount) {
        this.sBankAccount = sBankAccount;
        return this;
    }

    public CashMovementRecordBuilder setsAmount(String sAmount) {
        this.sAmount = sAmount;
        return this;
    }

    public CashMovementRecordBuilder setsStatus(String sStatus) {
        this.sStatus = sStatus;
        return this;
    }

    public CashMovementRecordBuilder setsRequestDate(String sRequestDate) {
        this.sRequestDate = sRequestDate;
        return this;
    }

    public CashMovementRecordBuilder setsUpdateDate(String sUpdateDate) {
        this.sUpdateDate = sUpdateDate;
        return this;
    }

    public CashMovementRecordBuilder setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
        return this;
    }

    public CashMovementRecord createCashMovementRecord() {
        return new CashMovementRecord(sAccountNumber, sRef, sBankName, sBankAccount, sAmount, sStatus, sRequestDate, sUpdateDate, lastUpdateBy);
    }
}