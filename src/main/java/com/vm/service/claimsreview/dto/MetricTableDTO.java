package com.vm.service.claimsreview.dto;

import java.util.List;

public class MetricTableDTO {
    private String title;
    private List<MetricRowDTO> rows;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<MetricRowDTO> getRows() {
        return rows;
    }

    public void setRows(List<MetricRowDTO> rows) {
        this.rows = rows;
    }
}
