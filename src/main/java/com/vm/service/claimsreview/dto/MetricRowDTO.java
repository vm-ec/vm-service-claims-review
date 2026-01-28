package com.vm.service.claimsreview.dto;

public class MetricRowDTO {
    private String category;
    private String name;
    private String labels;
    private String type;
    private String value;

    public MetricRowDTO() {
    }

    public MetricRowDTO(String category, String name, String model, String operation, String value, String unit) {
        this.category = category;
        this.name = name;
        this.labels = model;
        this.type = operation;
        this.value = value + " " + unit;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
