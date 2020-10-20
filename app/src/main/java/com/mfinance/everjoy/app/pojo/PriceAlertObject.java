package com.mfinance.everjoy.app.pojo;

public class PriceAlertObject {
    public PriceAlertObject(String strUser, int iActive, double dAlertPrice, int iAlertVolatility, String strMkt, String datecreated, int iGoodTill, int iId, int iType, String lastAlertDate, String timeInForce, String strType, int iExpired) {
        this.strUser = strUser;
        this.iActive = iActive;
        this.iGoodTill = iGoodTill;
        this.iId = iId;
        this.datecreated = datecreated;
        this.dAlertPrice = dAlertPrice;
        this.iAlertVolatility = iAlertVolatility;
        this.iType = iType;
        this.strMkt = strMkt;
        this.lastAlertDate = lastAlertDate;
        this.timeInForce = timeInForce;
        this.strType = strType;
        this.iExpired = iExpired;
    }

    public String getUser() {
        return strUser;
    }

    public Integer getActive() {
        return iActive;
    }

    public Integer getId() {
        return iId;
    }

    public String getDatecreated() {
        return datecreated;
    }

    public double getAlertPrice() {
        return dAlertPrice;
    }

    public Integer getAlertVolatility() {
        return iAlertVolatility;
    }

    public Integer getType() {
        return iType;
    }

    public String getMkt() {
        return strMkt;
    }

    public Integer getGoodTill() {
        return iGoodTill;
    }

    public String getLastAlertDate() {
        return lastAlertDate;
    }

    public String getTimeInForce() {
        return timeInForce;
    }

    public String getSType() { return strType;}

    public Integer getExpired() {
        return iExpired;
    }

    private final String strUser;
    private final int iActive;
    private final double dAlertPrice;
    private final int iAlertVolatility;
    private final String strMkt;
    private final String datecreated;
    private final int iGoodTill;
    private final int iId;
    private final int iType;
    private final String lastAlertDate;
    private final String timeInForce;
    private final String strType;
    private final int iExpired;
}
