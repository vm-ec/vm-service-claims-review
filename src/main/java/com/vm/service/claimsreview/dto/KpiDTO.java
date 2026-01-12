package com.vm.service.claimsreview.dto;

public class KpiDTO {
    private String id;
    private String title;
    private String value;
    private String trend;
    private String trendType;
    private Boolean isFinancial;
    // getters & setters
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public String getTrend() {
        return trend;
    }
    public void setTrend(String trend) {
        this.trend = trend;
    }
    public String getTrendType() {
        return trendType;
    }
    public void setTrendType(String trendType) {
        this.trendType = trendType;
    }
    public Boolean getIsFinancial() {
        return isFinancial;
    }
    public void setIsFinancial(Boolean isFinancial) {
        this.isFinancial = isFinancial;
    }

}