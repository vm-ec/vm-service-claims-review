package com.vm.service.claimsreview.service;

import com.vm.service.claimsreview.dto.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MetricsParsingServiceHelper {

    public static MetricsResponseDTO getHardCodedData() {
        MetaDTO meta = new MetaDTO(buildKpis(), null);
        
        MetricsResponseDTO dto = new MetricsResponseDTO();
        dto.setMeta(meta);
        dto.setColumns(buildColumns());
        dto.setRows(buildRows());
        dto.setMetricTables(buildMetricTables());
        
        return dto;
    }

    private static List<KpiDTO> buildKpis() {
        List<KpiDTO> kpis = new ArrayList<>();
        kpis.add(new KpiDTO("total_requests", "Total Requests", "85", null, null, null));
        kpis.add(new KpiDTO("avg_latency", "Average Latency", "4.32", null, null, null));
        kpis.add(new KpiDTO("error_count", "Errors", "0", null, null, null));
        kpis.add(new KpiDTO("benchmark_accuracy", "Accuracy", "100", null, null, null));
        return kpis;
    }

    private static List<ColumnDTO> buildColumns() {
        return List.of(
            new ColumnDTO("metric", "Metric", "string"),
            new ColumnDTO("model", "Model", "string"),
            new ColumnDTO("operation", "Operation", "string"),
            new ColumnDTO("value", "Value", "number"),
            new ColumnDTO("unit", "Unit", "string")
        );
    }

    private static List<RowDTO> buildRows() {
        List<RowDTO> rows = new ArrayList<>();
        rows.add(createRow(1, "Total Requests", "gemini", "classification", "85", "count"));
        rows.add(createRow(2, "Average Latency", "gemini", "classification", "4.32", "seconds"));
        rows.add(createRow(3, "P95 Latency", "gemini", "classification", "7.5", "seconds"));
        rows.add(createRow(4, "P99 Latency", "gemini", "classification", "10", "seconds"));
        rows.add(createRow(5, "Error Count", "gemini", "classification", "0", "count"));
        rows.add(createRow(6, "Total Input Tokens", "gemini", "-", "118762", "tokens"));
        rows.add(createRow(7, "Total Output Tokens", "gemini", "-", "4736", "tokens"));
        return rows;
    }

    private static List<MetricTableDTO> buildMetricTables() {
        List<MetricTableDTO> tables = new ArrayList<>();
        tables.add(buildRequestTable());
        tables.add(buildLatencyTable());
        tables.add(buildTokensTable());
        tables.add(buildBenchmarkTable());
        tables.add(buildSystemTable());
        return tables;
    }

    private static MetricTableDTO buildRequestTable() {
        MetricTableDTO table = new MetricTableDTO("AI Request Metrics", new ArrayList<>());
        table.getRows().add(new MetricRowDTO("AI Request Metrics", "Total Requests", "gemini", "classification", "125", "count"));
        table.getRows().add(new MetricRowDTO("AI Request Metrics", "Error Count", "gemini", "classification", "0", "count"));
        table.getRows().add(new MetricRowDTO("AI Request Metrics", "Error Rate", "gemini", "classification", "0", "%"));
        return table;
    }

    private static MetricTableDTO buildLatencyTable() {
        MetricTableDTO table = new MetricTableDTO("AI Latency Metrics", new ArrayList<>());
        table.getRows().add(new MetricRowDTO("AI Latency Metrics", "Average Latency", "gemini", "classification", "4.35", "seconds"));
        table.getRows().add(new MetricRowDTO("AI Latency Metrics", "P95 Latency", "gemini", "classification", "7.5", "seconds"));
        table.getRows().add(new MetricRowDTO("AI Latency Metrics", "P99 Latency", "gemini", "classification", "10", "seconds"));
        table.getRows().add(new MetricRowDTO("AI Latency Metrics", "Latency Distribution (0-5s)", "gemini", "classification", "101", "requests"));
        table.getRows().add(new MetricRowDTO("AI Latency Metrics", "Latency Distribution (5-7.5s)", "gemini", "classification", "17", "requests"));
        table.getRows().add(new MetricRowDTO("AI Latency Metrics", "Latency Distribution (7.5-10s)", "gemini", "classification", "7", "requests"));
        return table;
    }

    private static MetricTableDTO buildTokensTable() {
        MetricTableDTO table = new MetricTableDTO("AI Token Usage Metrics", new ArrayList<>());
        table.getRows().add(new MetricRowDTO("AI Token Usage Metrics", "Total Input Tokens", "gemini", "total_input", "174650", "tokens"));
        table.getRows().add(new MetricRowDTO("AI Token Usage Metrics", "Total Output Tokens", "gemini", "total_output", "6961", "tokens"));
        table.getRows().add(new MetricRowDTO("AI Token Usage Metrics", "Prediction Input Tokens", "gemini", "prediction_input", "165575", "tokens"));
        table.getRows().add(new MetricRowDTO("AI Token Usage Metrics", "Prediction Output Tokens", "gemini", "prediction_output", "575", "tokens"));
        table.getRows().add(new MetricRowDTO("AI Token Usage Metrics", "Summary Input Tokens", "gemini", "summary_input", "9075", "tokens"));
        table.getRows().add(new MetricRowDTO("AI Token Usage Metrics", "Summary Output Tokens", "gemini", "summary_output", "6386", "tokens"));
        return table;
    }

    private static MetricTableDTO buildBenchmarkTable() {
        MetricTableDTO table = new MetricTableDTO("AI Benchmark Metrics", new ArrayList<>());
        table.getRows().add(new MetricRowDTO("AI Benchmark Metrics", "Total Benchmark Runs", "-", "-", "25", "count"));
        table.getRows().add(new MetricRowDTO("AI Benchmark Metrics", "Benchmark Accuracy", "-", "-", "100", "%"));
        table.getRows().add(new MetricRowDTO("AI Benchmark Metrics", "Correct Results Count", "-", "-", "125", "count"));
        table.getRows().add(new MetricRowDTO("AI Benchmark Metrics", "Average Benchmark Duration", "-", "-", "146.07", "seconds"));
        return table;
    }

    private static MetricTableDTO buildSystemTable() {
        MetricTableDTO table = new MetricTableDTO("Python / System Metrics", new ArrayList<>());
        table.getRows().add(new MetricRowDTO("Python / System Metrics", "GC Objects Collected", "generation=0", "-", "18918", "count"));
        table.getRows().add(new MetricRowDTO("Python / System Metrics", "GC Objects Collected", "generation=1", "-", "1624", "count"));
        table.getRows().add(new MetricRowDTO("Python / System Metrics", "GC Objects Collected", "generation=2", "-", "96", "count"));
        table.getRows().add(new MetricRowDTO("Python / System Metrics", "GC Collections", "generation=0", "-", "398", "count"));
        table.getRows().add(new MetricRowDTO("Python / System Metrics", "GC Collections", "generation=1", "-", "36", "count"));
        table.getRows().add(new MetricRowDTO("Python / System Metrics", "GC Collections", "generation=2", "-", "3", "count"));
        table.getRows().add(new MetricRowDTO("Python / System Metrics", "Uncollectable Objects", "generation=0", "-", "0", "count"));
        table.getRows().add(new MetricRowDTO("Python / System Metrics", "Uncollectable Objects", "generation=1", "-", "0", "count"));
        table.getRows().add(new MetricRowDTO("Python / System Metrics", "Uncollectable Objects", "generation=2", "-", "0", "count"));
        return table;
    }

    private static RowDTO createRow(long id, String metric, String model, String operation, String value, String unit) {
        return new RowDTO(id, metric, "Healthy", operation, model, value, unit, null, null, Collections.emptyList());
    }
}
