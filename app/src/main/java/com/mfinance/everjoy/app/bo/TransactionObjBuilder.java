package com.mfinance.everjoy.app.bo;

import java.util.Calendar;
import java.util.Date;

public class TransactionObjBuilder {
    public TransactionObjBuilder() {
        sTransactionID = null;
        sRef = null;
        sContract = null;
        sAccount = null;
        sBuySell = null;
        sMsg = null;
        dRequestRate = 0.0;
        iStatus = -1;
        sStatusMsg = null;
        iMsg = -1;
        iRemarkCode = -1;
        iType = -1;
        iStatusMsg = -1;
        sMsgCode = null;
        sOrderRef = null;
        sLiqRef = null;
        sLRef = null;
        sSRef = null;
        sReply = null;
        sLiqmethod = null;
        sMsg1 = null;
        sKey = null;
        isReplyReceived = false;
        overrideMsgCode = false;
        msgEnglish = "";
        msgTraditionalChinese = "";
        msgSimplifiedChinese = "";
        dateCreate = Calendar.getInstance().getTime();
        dateLastUpdate = dateCreate;
    }

    public TransactionObjBuilder(TransactionObj t) {
        this.sTransactionID = t.sTransactionID;
        this.sRef = t.sRef;
        this.sContract = t.sContract;
        this.sAccount = t.sAccount;
        this.sBuySell = t.sBuySell;
        this.sMsg = t.sMsg;
        this.dRequestRate = t.dRequestRate;
        this.iStatus = t.iStatus;
        this.sStatusMsg = t.sStatusMsg;
        this.iMsg = t.iMsg;
        this.iRemarkCode = t.iRemarkCode;
        this.iType = t.iType;
        this.dAmount = t.dAmount;
        this.contract = t.contract;
        this.iStatusMsg = t.iStatusMsg;
        this.sMsgCode = t.sMsgCode;
        this.sOrderRef = t.sOrderRef;
        this.sLiqRef = t.sLiqRef;
        this.sLRef = t.sLRef;
        this.sSRef = t.sSRef;
        this.sReply = t.sReply;
        this.sLiqmethod = t.sLiqmethod;
        this.sMsg1 = t.sMsg1;
        this.overrideMsgCode = t.overrideMsgCode;
        this.msgEnglish = t.msgEnglish;
        this.msgTraditionalChinese = t.msgTraditionalChinese;
        this.msgSimplifiedChinese = t.msgSimplifiedChinese;
        this.sKey = t.sKey;
        this.isReplyReceived = t.isReplyReceived;
        this.dateCreate = t.dateCreate;
        this.dateLastUpdate = t.dateLastUpdate;
    }

    private String sTransactionID;
    private String sRef;
    private String sContract;
    private String sAccount;
    private String sBuySell;
    private String sMsg;
    private double dRequestRate;
    private int iStatus;
    private String sStatusMsg;
    private int iMsg;
    private int iRemarkCode;
    private int iType;
    private double dAmount;
    private ContractObj contract;
    private int iStatusMsg;
    private String sMsgCode;
    private String sOrderRef;
    private String sLiqRef;
    private String sLRef;
    private String sSRef;
    private String sReply;
    private String sLiqmethod;
    private String sMsg1;
    public String sKey;
    public boolean isReplyReceived;
    private boolean overrideMsgCode;
    private String msgEnglish;
    private String msgTraditionalChinese;
    private String msgSimplifiedChinese;
    private Date dateCreate;
    private Date dateLastUpdate;

    public TransactionObjBuilder setsTransactionID(String sTransactionID) {
        this.sTransactionID = sTransactionID;
        return this;
    }

    public TransactionObjBuilder setsRef(String sRef) {
        this.sRef = sRef;
        return this;
    }

    public TransactionObjBuilder setsContract(String sContract) {
        this.sContract = sContract;
        return this;
    }

    public TransactionObjBuilder setsAccount(String sAccount) {
        this.sAccount = sAccount;
        return this;
    }

    public TransactionObjBuilder setsBuySell(String sBuySell) {
        this.sBuySell = sBuySell;
        return this;
    }

    public TransactionObjBuilder setdRequestRate(double dRequestRate) {
        this.dRequestRate = dRequestRate;
        return this;
    }

    public TransactionObjBuilder setiStatus(int iStatus) {
        this.iStatus = iStatus;
        return this;
    }

    public TransactionObjBuilder setsStatusMsg(String sStatusMsg) {
        this.sStatusMsg = sStatusMsg;
        return this;
    }

    public TransactionObjBuilder setiMsg(int iMsg) {
        this.iMsg = iMsg;
        return this;
    }

    public TransactionObjBuilder setiRemarkCode(int iRemarkCode) {
        this.iRemarkCode = iRemarkCode;
        return this;
    }

    public TransactionObjBuilder setiType(int iType) {
        this.iType = iType;
        return this;
    }

    public TransactionObjBuilder setdAmount(double dAmount) {
        this.dAmount = dAmount;
        return this;
    }

    public TransactionObjBuilder setContract(ContractObj contract) {
        this.contract = contract;
        return this;
    }

    public TransactionObjBuilder setiStatusMsg(int iStatusMsg) {
        this.iStatusMsg = iStatusMsg;
        return this;
    }

    public TransactionObjBuilder setsMsgCode(String sMsgCode) {
        this.sMsgCode = sMsgCode;
        return this;
    }

    public TransactionObjBuilder setsOrderRef(String sOrderRef) {
        this.sOrderRef = sOrderRef;
        return this;
    }

    public TransactionObjBuilder setsLiqRef(String sLiqRef) {
        this.sLiqRef = sLiqRef;
        return this;
    }

    public TransactionObjBuilder setsLRef(String sLRef) {
        this.sLRef = sLRef;
        return this;
    }

    public TransactionObjBuilder setsSRef(String sSRef) {
        this.sSRef = sSRef;
        return this;
    }

    public TransactionObjBuilder setsReply(String sReply) {
        this.sReply = sReply;
        return this;
    }

    public TransactionObjBuilder setsLiqmethod(String sLiqmethod) {
        this.sLiqmethod = sLiqmethod;
        return this;
    }

    public TransactionObjBuilder setsMsg1(String sMsg1) {
        this.sMsg1 = sMsg1;
        return this;
    }

    public TransactionObjBuilder setOverrideMsgCode(boolean overrideMsgCode) {
        this.overrideMsgCode = overrideMsgCode;
        return this;
    }

    public TransactionObjBuilder setMsgEnglish(String msgEnglish) {
        this.msgEnglish = msgEnglish;
        return this;
    }

    public TransactionObjBuilder setMsgTraditionalChinese(String msgTraditionalChinese) {
        this.msgTraditionalChinese = msgTraditionalChinese;
        return this;
    }

    public TransactionObjBuilder setMsgSimplifiedChinese(String msgSimplifiedChinese) {
        this.msgSimplifiedChinese = msgSimplifiedChinese;
        return this;
    }

    public TransactionObjBuilder setsMsg(String sMsg) {
        this.sMsg = sMsg;
        return this;
    }

    public TransactionObjBuilder setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
        return this;
    }

    public TransactionObjBuilder setDateLastUpdate(Date dateLastUpdate) {
        this.dateLastUpdate = dateLastUpdate;
        return this;
    }

    public TransactionObj createTransactionObj() {
        if (sMsg == null) {
            sMsg = sStatusMsg;
        }
        return new TransactionObj(sTransactionID, sRef, sContract, sAccount, sBuySell, dRequestRate, iStatus, sStatusMsg, iMsg, iRemarkCode, iType, dAmount, contract, iStatusMsg, sMsgCode, sOrderRef, sLiqRef, sLRef, sSRef, sReply, sLiqmethod, sMsg1, overrideMsgCode, msgEnglish, msgTraditionalChinese, msgSimplifiedChinese, sKey, isReplyReceived, dateCreate, dateLastUpdate, sMsg);
    }
}