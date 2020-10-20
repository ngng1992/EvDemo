package com.mfinance.everjoy.app.service.external;

import android.util.Log;

import com.mfinance.everjoy.app.constant.FXConstants;
import com.mfinance.everjoy.app.constant.ServiceFunction;
import com.mfinance.everjoy.app.model.DataRepository;
import com.mfinance.everjoy.app.pojo.NewsSummary;
import com.mfinance.everjoy.app.service.FxMobileTraderService;
import com.mfinance.everjoy.app.util.MessageObj;
import com.mfinance.everjoy.app.util.Utility;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class NewsMessageHandler extends ServerMessageHandler {
    private String TAG = this.getClass().getSimpleName();

    public enum UpdateType {
        ADD(FXConstants.COMMON_ADD), UPDATE(FXConstants.COMMON_UPDATE), DELETE(FXConstants.COMMON_DELETE), NEW(FXConstants.COMMON_NEW);
        private String value;

        UpdateType(String value) {
            this.value = value;
        }

        public static UpdateType fromString(String v) {
            for (UpdateType u : values()) {
                if (u.value.equals(v)) {
                    return u;
                }
            }
            return NEW;
        }
    }

    public class NewsResponse {
        public static final String STATUS = "status";
        public static final String NUMBER_OF_ITEM = "noitem";
        public static final String ITEM_NEWS_ID = "nid";
        public static final String ITEM_NEWS_EN = "news";
        public static final String ITEM_NEWS_TIME = "ntime";
        public static final String ITEM_NEWS_TYPE = "newstype";
        public static final String ITEM_NEWS_TC = "news_tc";
        public static final String ITEM_NEWS_SC = "news_sc";
        public static final String ITEM_REPORT_GROUP = "rptgroup";
    }

    /**
     * Constructor
     *
     * @param service background service
     */
    public NewsMessageHandler(FxMobileTraderService service) {
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
        ArrayList<NewsSummary> newsSummaries = new ArrayList<>(service.app.data.getNewsSummaries());
        UpdateType updateType = UpdateType.fromString(msgObj.getField(NewsResponse.STATUS));
        try {
            int num = Integer.parseInt(msgObj.getField(NewsResponse.NUMBER_OF_ITEM));
            for (int i = 0; i < num; i++) {

                int index = i + 1; // array set at 1? WTF?
                List<String> list = Optional.ofNullable(msgObj.getField(NewsResponse.ITEM_REPORT_GROUP + index))
                        .map(s -> Arrays.asList(s.split(",")))
                        .orElse(Collections.EMPTY_LIST);
                NewsSummary newsSummary = new NewsSummary(
                        Integer.parseInt(msgObj.getField(NewsResponse.ITEM_NEWS_ID + index)),
                        NewsSummary.NewsType.fromString(msgObj.getField(NewsResponse.ITEM_NEWS_TYPE + index)),
                        msgObj.getField(NewsResponse.ITEM_NEWS_EN + index),
                        msgObj.getField(NewsResponse.ITEM_NEWS_TC + index),
                        msgObj.getField(NewsResponse.ITEM_NEWS_SC + index),
                        LocalDateTime.parse(msgObj.getField(NewsResponse.ITEM_NEWS_TIME + index), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                        list.stream().filter(s -> !s.equals("")).collect(Collectors.toList())
                );
                switch (updateType) {
                    case ADD:
                    case NEW:
                    case UPDATE:
                        newsSummaries.add(newsSummary);
                        break;
                }
            }
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage(), ex);
        }
        newsSummaries.sort((v1, v2) -> v2.getId().compareTo(v1.getId()));
        service.app.data.setNewsSummaries(newsSummaries);
        service.broadcast(ServiceFunction.ACT_UPDATE_UI, null);
    }
}
