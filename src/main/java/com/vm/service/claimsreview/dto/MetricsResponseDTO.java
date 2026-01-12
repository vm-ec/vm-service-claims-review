package com.vm.service.claimsreview.dto;

import java.util.List;

public class MetricsResponseDTO {
    private MetaDTO meta;
    private List<ColumnDTO> columns;
    private List<RowDTO> rows;
    // getters & setters
    public MetaDTO getMeta() {
        return meta;
    }
    public void setMeta(MetaDTO meta) {
        this.meta = meta;
    }
    public List<ColumnDTO> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnDTO> columns) {
        this.columns = columns;
    }
    public List<RowDTO> getRows() {
        return rows;
    }
    public void setRows(List<RowDTO> rows) {
        this.rows = rows;
    }

}