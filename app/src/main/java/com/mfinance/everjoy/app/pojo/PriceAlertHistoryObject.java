package com.mfinance.everjoy.app.pojo;

import java.time.LocalDateTime;
import java.util.List;

public class PriceAlertHistoryObject {
    public PriceAlertHistoryObject(Integer id, Integer iAlertId, String datecreated, double dAlertPrice, Integer iAlertVolatility, Integer iType, String strMkt){
        //System.out.println("PriceAlertHistoryObject " + id + " " + iAlertId + " "+ datecreated + " "+ dAlertPrice + " " +iAlertVolatility + " " +iType + " " + strMkt);
        this.iId = id;
        this.iAlertId = iAlertId;
        this.datecreated = datecreated;
        this.dAlertPrice = dAlertPrice;
        this.iAlertVolatility = iAlertVolatility;
        this.iType = iType;
        this.strMkt = strMkt;
    }

    public Integer getId() {
        return iId;
    }

    public Integer getAlertId() {
        return iAlertId;
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

    private final int iId;
    private final int iAlertId;
    private final String datecreated;
    private final double dAlertPrice;
    private final int iAlertVolatility;
    private final int iType;
    private final String strMkt;
}
