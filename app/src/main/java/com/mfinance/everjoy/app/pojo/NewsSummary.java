package com.mfinance.everjoy.app.pojo;

import java.time.LocalDateTime;
import java.util.List;

public class NewsSummary {
    public NewsSummary(Integer id, NewsType newsType, String titleEnglish, String titleTraditionalChinese, String titleSimplifiedChinese, LocalDateTime dateTime, List<String> reportGroups) {
        this.id = id;
        this.newsType = newsType;
        this.titleEnglish = titleEnglish;
        this.titleTraditionalChinese = titleTraditionalChinese;
        this.titleSimplifiedChinese = titleSimplifiedChinese;
        this.dateTime = dateTime;
        this.reportGroups = reportGroups;
    }

    public Integer getId() {
        return id;
    }

    public NewsType getNewsType() {
        return newsType;
    }

    public String getTitleEnglish() {
        return titleEnglish;
    }

    public String getTitleTraditionalChinese() {
        return titleTraditionalChinese;
    }

    public String getTitleSimplifiedChinese() {
        return titleSimplifiedChinese;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public List<String> getReportGroups() {
        return reportGroups;
    }

    public enum NewsType {
        NEWS("0"), SYSTEM_MESSAGE("1");
        private String value;
        NewsType(String value){
            this.value = value;
        }
        public static NewsType fromString(String t) {
            for (NewsType i : values()) {
                if (i.value.equals(t)) {
                    return i;
                }
            }
            return NewsType.NEWS;
        }
    }
    private final int id;
    private final NewsType newsType;
    private final String titleEnglish;
    private final String titleTraditionalChinese;
    private final String titleSimplifiedChinese;
    private final LocalDateTime dateTime;
    private final List<String> reportGroups;
}
