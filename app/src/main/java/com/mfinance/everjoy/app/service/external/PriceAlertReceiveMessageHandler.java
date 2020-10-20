package com.mfinance.everjoy.app.service.external;

import android.util.Log;

import com.mfinance.everjoy.app.CompanySettings;
import com.mfinance.everjoy.app.bo.ContractObj;
import com.mfinance.everjoy.app.bo.SystemMessage;
import com.mfinance.everjoy.app.constant.FXConstants;
import com.mfinance.everjoy.app.constant.IDDictionary;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.pojo.NewsSummary;
import com.mfinance.everjoy.app.pojo.PriceAlertHistoryObject;
import com.mfinance.everjoy.app.pojo.PriceAlertObject;
import com.mfinance.everjoy.app.service.FxMobileTraderService;
import com.mfinance.everjoy.app.util.MessageMapping;
import com.mfinance.everjoy.app.util.MessageObj;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PriceAlertReceiveMessageHandler extends ServerMessageHandler {
    private String TAG = this.getClass().getSimpleName();

    public class PriceAlertResponse {
        public static final String NUMBER_OF_ITEM = "noc";
        public static final String ITEM_ACC = "acc";
        public static final String ITEM_ACTIVE = "active";
        public static final String ITEM_ALERT_PRICE = "alertprice";
        public static final String ITEM_ALERT_VOLATILITY = "alertvolatility";
        public static final String ITEM_CONTRACT = "contract";
        public static final String ITEM_CREATE_TIME = "createdtime";
        public static final String ITEM_GOODTILL = "goodtill";
        public static final String ITEM_ID = "id";
        public static final String ITEM_ITYPE = "itype";
        public static final String ITEM_LAST_ALERT = "lastalerted";
        public static final String ITEM_TIME_IN_FORCE = "timeinforce";
        public static final String ITEM_TYPE = "type";
        public static final String ITEM_MSGCD = "msgcd1";

        public static final String NUMBER_OF_HISTORY = "historynoc";
        public static final String HISTORY_ID = "hid";
        public static final String HISTORY_ALERT_ID = "halertid";
        public static final String HISTORY_CREATE_TIME = "hcreatedtime";
        public static final String HISTORY_ALERT_PRICE = "halertprice";
        public static final String HISTORY_ALERT_VOLATILITY = "halertvolatility";
        public static final String HISTORY_ALERT_TYPE = "halerttype";
        public static final String HISTORY_ALERT_CONTRACT = "hcontract";
    }

    /**
     * Constructor
     *
     * @param service background service
     */
    public PriceAlertReceiveMessageHandler(FxMobileTraderService service) {
        super(service);
    }

    @Override
    public boolean isStatusLess() {
        return false;
    }

    @Override
    public boolean isBalanceRecalRequire() {
        return false;
    }

    @Override
    public void handleMessage(MessageObj msgObj) {
        if (!CompanySettings.ENABLE_PRICE_ALERT)
            return;

        if (msgObj.getFunctionType() == IDDictionary.TRADER_RECEIVE_PRICE_ALERTS) {
            ArrayList<PriceAlertObject> priceAlertArray = new ArrayList<PriceAlertObject>();
            ArrayList<PriceAlertHistoryObject> priceAlertHistoryArray = new ArrayList<PriceAlertHistoryObject>();

            try {
                int num = Integer.parseInt(msgObj.getField(PriceAlertResponse.NUMBER_OF_ITEM));
                for (int index = 0; index < num; index++) {
                    String strUser = "";
                    int iActive = -1;
                    int iGoodTill = -1;
                    int iId = -1;
                    String dateCreated = "";
                    double dAlertPrice = -1;
                    int iAlertVolatility = -1;
                    int iType = -1;
                    String strMkt = "";
                    String lastAlertDate = "";
                    String timeInForce = "";
                    String strType = "";
                    int iExpired = -1;

                    if (msgObj.getField(PriceAlertResponse.ITEM_ACC + index) != null)
                        strUser = msgObj.getField(PriceAlertResponse.ITEM_ACC + index);
                    if (msgObj.getField(PriceAlertResponse.ITEM_ACTIVE + index) != null)
                        iActive = Integer.parseInt(msgObj.getField(PriceAlertResponse.ITEM_ACTIVE + index));
                    if (msgObj.getField(PriceAlertResponse.ITEM_ALERT_PRICE + index) != null)
                        dAlertPrice = Double.parseDouble(msgObj.getField(PriceAlertResponse.ITEM_ALERT_PRICE + index));
                    if (msgObj.getField(PriceAlertResponse.ITEM_ALERT_VOLATILITY + index) != null)
                        iAlertVolatility = Integer.parseInt(msgObj.getField(PriceAlertResponse.ITEM_ALERT_VOLATILITY + index));
                    if (msgObj.getField(PriceAlertResponse.ITEM_CONTRACT + index) != null)
                        strMkt = msgObj.getField(PriceAlertResponse.ITEM_CONTRACT + index);
                    if (msgObj.getField(PriceAlertResponse.ITEM_CREATE_TIME + index) != null)
                        dateCreated = msgObj.getField(PriceAlertResponse.ITEM_CREATE_TIME + index);
                    if (msgObj.getField(PriceAlertResponse.ITEM_GOODTILL + index) != null)
                        iGoodTill = Integer.parseInt(msgObj.getField(PriceAlertResponse.ITEM_GOODTILL + index));
                    if (msgObj.getField(PriceAlertResponse.ITEM_ID + index) != null)
                        iId = Integer.parseInt(msgObj.getField(PriceAlertResponse.ITEM_ID + index));
                    if (msgObj.getField(PriceAlertResponse.ITEM_ITYPE + index) != null)
                        iType = Integer.parseInt(msgObj.getField(PriceAlertResponse.ITEM_ITYPE + index));
                    if (msgObj.getField(PriceAlertResponse.ITEM_LAST_ALERT + index) != null)
                        lastAlertDate = msgObj.getField(PriceAlertResponse.ITEM_LAST_ALERT + index);
                    if (msgObj.getField(PriceAlertResponse.ITEM_TIME_IN_FORCE + index) != null)
                        timeInForce = msgObj.getField(PriceAlertResponse.ITEM_TIME_IN_FORCE + index);
                    if (msgObj.getField(PriceAlertResponse.ITEM_TYPE + index) != null)
                        strType = msgObj.getField(PriceAlertResponse.ITEM_TYPE + index);

                    if (!timeInForce.isEmpty()) {
                        Date dateTimeInForce = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(timeInForce);
                        Date dateCurrent = new Date();
                        if (service.app.dServerDateTime != null)
                            dateCurrent = service.app.dServerDateTime;

                        if (dateTimeInForce.after(dateCurrent))
                            iExpired = 0;
                        else
                            iExpired = 1;
                    } else
                        iExpired = 1;

                    PriceAlertObject price = new PriceAlertObject(strUser, iActive, dAlertPrice, iAlertVolatility, strMkt, dateCreated, iGoodTill, iId, iType, lastAlertDate, timeInForce, strType, iExpired);
                    priceAlertArray.add(0, price);
                }

                num = Integer.parseInt(msgObj.getField(PriceAlertResponse.NUMBER_OF_HISTORY));
                for (int index = 0; index < num; index++) {
                    int iId = -1;
                    int iAlertId = -1;
                    String dateCreated = "";
                    double dAlertPrice = -1;
                    int iAlertVolatility = -1;
                    int iType = -1;
                    String strMkt = "";

                    if (msgObj.getField(PriceAlertResponse.HISTORY_ID + index) != null)
                        iId = Integer.parseInt(msgObj.getField(PriceAlertResponse.HISTORY_ID + index));
                    if (msgObj.getField(PriceAlertResponse.HISTORY_ALERT_ID + index) != null)
                        iAlertId = Integer.parseInt(msgObj.getField(PriceAlertResponse.HISTORY_ALERT_ID + index));
                    if (msgObj.getField(PriceAlertResponse.HISTORY_CREATE_TIME + index) != null)
                        dateCreated = msgObj.getField(PriceAlertResponse.HISTORY_CREATE_TIME + index);
                    if (msgObj.getField(PriceAlertResponse.HISTORY_ALERT_PRICE + index) != null)
                        dAlertPrice = Double.parseDouble(msgObj.getField(PriceAlertResponse.HISTORY_ALERT_PRICE + index));
                    if (msgObj.getField(PriceAlertResponse.HISTORY_ALERT_VOLATILITY + index) != null)
                        iAlertVolatility = Integer.parseInt(msgObj.getField(PriceAlertResponse.HISTORY_ALERT_VOLATILITY + index));
                    if (msgObj.getField(PriceAlertResponse.HISTORY_ALERT_TYPE + index) != null)
                        iType = Integer.parseInt(msgObj.getField(PriceAlertResponse.HISTORY_ALERT_TYPE + index));
                    if (msgObj.getField(PriceAlertResponse.HISTORY_ALERT_CONTRACT + index) != null)
                        strMkt = msgObj.getField(PriceAlertResponse.HISTORY_ALERT_CONTRACT + index);

                    PriceAlertHistoryObject priceHistory = new PriceAlertHistoryObject(iId, iAlertId, dateCreated, dAlertPrice, iAlertVolatility, iType, strMkt);
                    priceAlertHistoryArray.add(0, priceHistory);
                }
            } catch (Exception e) {
                System.out.println("PriceAlertReceiveMessageHandler " + e);
            }
            //service.app.data.setNewsSummaries(newsSummaries);
            service.app.data.setPriceAlertList(priceAlertArray);
            service.app.data.setPriceAlertHistoryList(priceAlertHistoryArray);
        }
        else if (msgObj.getFunctionType() == IDDictionary.TRADER_UPDATE_PRICE_ALERTS){
            String strUser = "";
            int iActive = -1;
            int iGoodTill = -1;
            int iId = -1;
            String dateCreated = "";
            double dAlertPrice = -1;
            int iAlertVolatility = -1;
            int iType = -1;
            String strMkt = "";
            String lastAlertDate = "";
            String timeInForce = "";
            String strType = "";
            int iExpired = -1;

            try {
                String msgcd = msgObj.getField(PriceAlertResponse.ITEM_MSGCD);
                if (msgcd != null && msgcd.equals("1023")){
                    String message = MessageMapping.getStringById(service.getRes(), "lb_pricealert_message_1023", service.app.locale);
                    service.app.data.addSystemMessage(new SystemMessage(-1, message));
                }
                else {
                    if (msgObj.getField(PriceAlertResponse.ITEM_ACC) != null)
                        strUser = msgObj.getField(PriceAlertResponse.ITEM_ACC);
                    if (msgObj.getField(PriceAlertResponse.ITEM_ACTIVE) != null)
                        iActive = Integer.parseInt(msgObj.getField(PriceAlertResponse.ITEM_ACTIVE));
                    if (msgObj.getField(PriceAlertResponse.ITEM_ALERT_PRICE) != null)
                        dAlertPrice = Double.parseDouble(msgObj.getField(PriceAlertResponse.ITEM_ALERT_PRICE));
                    if (msgObj.getField(PriceAlertResponse.ITEM_ALERT_VOLATILITY) != null)
                        iAlertVolatility = Integer.parseInt(msgObj.getField(PriceAlertResponse.ITEM_ALERT_VOLATILITY));
                    if (msgObj.getField(PriceAlertResponse.ITEM_CONTRACT) != null)
                        strMkt = msgObj.getField(PriceAlertResponse.ITEM_CONTRACT);
                    if (msgObj.getField(PriceAlertResponse.ITEM_CREATE_TIME) != null)
                        dateCreated = msgObj.getField(PriceAlertResponse.ITEM_CREATE_TIME);
                    if (msgObj.getField(PriceAlertResponse.ITEM_GOODTILL) != null)
                        iGoodTill = Integer.parseInt(msgObj.getField(PriceAlertResponse.ITEM_GOODTILL));
                    if (msgObj.getField(PriceAlertResponse.ITEM_ID) != null)
                        iId = Integer.parseInt(msgObj.getField(PriceAlertResponse.ITEM_ID));
                    if (msgObj.getField(PriceAlertResponse.ITEM_ITYPE) != null)
                        iType = Integer.parseInt(msgObj.getField(PriceAlertResponse.ITEM_ITYPE));
                    if (msgObj.getField(PriceAlertResponse.ITEM_LAST_ALERT) != null)
                        lastAlertDate = msgObj.getField(PriceAlertResponse.ITEM_LAST_ALERT);
                    if (msgObj.getField(PriceAlertResponse.ITEM_TIME_IN_FORCE) != null)
                        timeInForce = msgObj.getField(PriceAlertResponse.ITEM_TIME_IN_FORCE);
                    if (msgObj.getField(PriceAlertResponse.ITEM_TYPE) != null)
                        strType = msgObj.getField(PriceAlertResponse.ITEM_TYPE);

                    if (!timeInForce.isEmpty()) {
                        Date dateTimeInForce = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(timeInForce);
                        Date dateCurrent = new Date();
                        if (service.app.dServerDateTime != null)
                            dateCurrent = service.app.dServerDateTime;

                        if (dateTimeInForce.after(dateCurrent))
                            iExpired = 0;
                        else
                            iExpired = 1;
                    } else
                        iExpired = 1;

                    PriceAlertObject price = new PriceAlertObject(strUser, iActive, dAlertPrice, iAlertVolatility, strMkt, dateCreated, iGoodTill, iId, iType, lastAlertDate, timeInForce, strType, iExpired);

                    service.app.data.updatePriceAlertListByRef(iId, price);

                    service.broadcast(ServiceFunction.ACT_GO_TO_PRICE_ALERT, null);

                    String message = MessageMapping.getStringById(service.getRes(), "lb_pricealert_add_added", service.app.locale);
                    if (msgcd.equals("1018"))
                        message = MessageMapping.getStringById(service.getRes(), "lb_pricealert_edit_edited", service.app.locale);
                    if (service.app.data.getPriceAlertUpdateEnable())
                        service.app.data.setPriceAlertUpdateEnable(false);
                    else
                        service.app.data.addSystemMessage(new SystemMessage(-1, message));
                }

            } catch (Exception e) {
                System.out.println("PriceAlertReceiveMessageHandler " + e);
            }
        }
        else if (msgObj.getFunctionType() == IDDictionary.TRADER_REMOVE_PRICE_ALERTS){
            int iId = -1;
            if (msgObj.getField(PriceAlertResponse.ITEM_ID) != null)
                iId = Integer.parseInt(msgObj.getField(PriceAlertResponse.ITEM_ID));
            service.app.data.removePriceAlertListByRef(iId);
        }
        else if (msgObj.getFunctionType() == IDDictionary.TRADER_ALERT_PRICE_ALERTS){
            //Update History
            String strUser = "";
            int iActive = -1;
            int iGoodTill = -1;
            int iId = -1;
            String dateCreated = "";
            double dAlertPrice = -1;
            int iAlertVolatility = -1;
            int iType = -1;
            String strMkt = "";
            String lastAlertDate = "";
            String timeInForce = "";
            String strType = "";
            int iExpired = -1;
            int iAlertId = -1;

            if (msgObj.getField(PriceAlertResponse.ITEM_ID) != null)
                iId = Integer.parseInt(msgObj.getField(PriceAlertResponse.ITEM_ID));
            if (msgObj.getField(PriceAlertResponse.ITEM_LAST_ALERT) != null)
                dateCreated = msgObj.getField(PriceAlertResponse.ITEM_LAST_ALERT);
            if (msgObj.getField(PriceAlertResponse.ITEM_ALERT_PRICE) != null)
                dAlertPrice = Double.parseDouble(msgObj.getField(PriceAlertResponse.ITEM_ALERT_PRICE));
            if (msgObj.getField(PriceAlertResponse.ITEM_ALERT_VOLATILITY) != null)
                iAlertVolatility = Integer.parseInt(msgObj.getField(PriceAlertResponse.ITEM_ALERT_VOLATILITY));
            if (msgObj.getField(PriceAlertResponse.ITEM_ITYPE + "1") != null)
                iType = Integer.parseInt(msgObj.getField(PriceAlertResponse.ITEM_ITYPE + "1"));
            if (msgObj.getField(PriceAlertResponse.ITEM_CONTRACT) != null)
                strMkt = msgObj.getField(PriceAlertResponse.ITEM_CONTRACT);

            PriceAlertHistoryObject priceHistory = new PriceAlertHistoryObject(iId, iAlertId, dateCreated, dAlertPrice, iAlertVolatility, iType, strMkt);
            service.app.data.addPriceAlertHistory(priceHistory);

            //Update Price Alert Object
            try {
                if (msgObj.getField(PriceAlertResponse.ITEM_ACC) != null)
                    strUser = msgObj.getField(PriceAlertResponse.ITEM_ACC);
                if (msgObj.getField(PriceAlertResponse.ITEM_ACTIVE) != null)
                    iActive = Integer.parseInt(msgObj.getField(PriceAlertResponse.ITEM_ACTIVE));
                if (msgObj.getField(PriceAlertResponse.ITEM_ALERT_PRICE) != null)
                    dAlertPrice = Double.parseDouble(msgObj.getField(PriceAlertResponse.ITEM_ALERT_PRICE));
                if (msgObj.getField(PriceAlertResponse.ITEM_ALERT_VOLATILITY) != null)
                    iAlertVolatility = Integer.parseInt(msgObj.getField(PriceAlertResponse.ITEM_ALERT_VOLATILITY));
                if (msgObj.getField(PriceAlertResponse.ITEM_CONTRACT) != null)
                    strMkt = msgObj.getField(PriceAlertResponse.ITEM_CONTRACT);
                if (msgObj.getField(PriceAlertResponse.ITEM_CREATE_TIME) != null)
                    dateCreated = msgObj.getField(PriceAlertResponse.ITEM_CREATE_TIME);
                if (msgObj.getField(PriceAlertResponse.ITEM_GOODTILL) != null)
                    iGoodTill = Integer.parseInt(msgObj.getField(PriceAlertResponse.ITEM_GOODTILL));
                if (msgObj.getField(PriceAlertResponse.ITEM_ID) != null)
                    iId = Integer.parseInt(msgObj.getField(PriceAlertResponse.ITEM_ID));
                if (msgObj.getField(PriceAlertResponse.ITEM_ITYPE) != null)
                    iType = Integer.parseInt(msgObj.getField(PriceAlertResponse.ITEM_ITYPE));
                if (msgObj.getField(PriceAlertResponse.ITEM_LAST_ALERT) != null)
                    lastAlertDate = msgObj.getField(PriceAlertResponse.ITEM_LAST_ALERT);
                if (msgObj.getField(PriceAlertResponse.ITEM_TIME_IN_FORCE) != null)
                    timeInForce = msgObj.getField(PriceAlertResponse.ITEM_TIME_IN_FORCE);
                if (msgObj.getField(PriceAlertResponse.ITEM_TYPE) != null)
                    strType = msgObj.getField(PriceAlertResponse.ITEM_TYPE);

                if (!timeInForce.isEmpty()) {
                    Date dateTimeInForce = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(timeInForce);
                    Date dateCurrent = new Date();
                    if (service.app.dServerDateTime != null)
                        dateCurrent = service.app.dServerDateTime;

                    if (dateTimeInForce.after(dateCurrent))
                        iExpired = 0;
                    else
                        iExpired = 1;
                } else
                    iExpired = 1;

                PriceAlertObject price = new PriceAlertObject(strUser, iActive, dAlertPrice, iAlertVolatility, strMkt, dateCreated, iGoodTill, iId, iType, lastAlertDate, timeInForce, strType, iExpired);
                service.app.data.updatePriceAlertListByRef(iId, price);
            } catch (Exception e) {
                System.out.println("PriceAlertReceiveMessageHandler " + e);
            }

            //System Message
            ContractObj contractObj = service.app.data.getContract(strMkt);
            if (contractObj != null) {
                String message = MessageMapping.getStringById(service.getRes(), "lb_pricealert_message_alert", service.app.locale) + " ";
                message += lastAlertDate + " ";
                message += contractObj.getContractName(service.app.locale) + " ";
                message += MessageMapping.getStringById(service.getRes(), "lb_pricealert_type" + iType, service.app.locale) + " ";
                message += dAlertPrice + " ";
                message += (iType == 0 || iType == 2) ? MessageMapping.getStringById(service.getRes(), "lb_pricealert_message_alert_bid", service.app.locale) + " " : MessageMapping.getStringById(service.getRes(), "lb_pricealert_message_alert_ask", service.app.locale) + " " ;
                message += (iType == 0 || iType == 2) ? contractObj.getBidAsk()[0]: contractObj.getBidAsk()[1];
                service.app.data.addSystemMessage(new SystemMessage(-1, message));
            }
        }
        service.broadcast(ServiceFunction.ACT_UPDATE_UI, null);
    }
}
