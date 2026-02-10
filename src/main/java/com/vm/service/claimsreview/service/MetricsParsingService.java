package com.vm.service.claimsreview.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.vm.service.claimsreview.dto.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class MetricsParsingService {

    private final AiMetricsService aiMetricsService;

    public MetricsResponseDTO fetchAndParseDashboard() {
        try {
            return parseMetrics(aiMetricsService.fetchRawMetrics());
        } catch (Exception e) {
            return MetricsParsingServiceHelper.getHardCodedData();
        }
    }

    private MetricsResponseDTO parseMetrics(JsonNode root) {

        JsonNode metrics = root.path("metrics");

        if (!metrics.isArray()) {
            return new MetricsResponseDTO(
                new MetaDTO(Collections.emptyList(), null),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList()
            );
        }
        
        MetricsResponseDTO dto = new MetricsResponseDTO();

        /* ===============================
           1. Dynamic aggregation bucket
           =============================== */
        Map<String, Aggregation> group = new LinkedHashMap<>();

        for (JsonNode metric : metrics) {

            String metricType = metric.path("type").asText();
            JsonNode samples = metric.path("samples");

            if (!samples.isArray()) continue;

            for (JsonNode sample : samples) {

                String sampleName = sample.path("name").asText();
                if (sampleName.endsWith("_created")) continue;

                JsonNode labels = sample.path("labels");
                
                // Extract identifier from labels or metric name
                String identifier;
                if (labels.has("model") && labels.has("operation")) {
                    identifier = labels.get("model").asText() + "|" + labels.get("operation").asText();
                } else if (labels.has("model")) {
                    identifier = labels.get("model").asText() + "|general";
                } else {
                    // Use metric name as identifier for metrics without model/operation
                    String metricName = metric.path("name").asText();
                    identifier = metricName + "|system";
                }

                Aggregation agg = group.computeIfAbsent(identifier, k -> new Aggregation());
                agg.metricName = metric.path("name").asText();

                double value = sample.path("value").asDouble(0);

                /* -------- COUNTERS -------- */
                if (metricType.equals("counter") && sampleName.endsWith("_total")) {
                    agg.counters.merge(sampleName, value, Double::sum);
                }

                /* -------- HISTOGRAM -------- */
                if (metricType.equals("histogram")) {
                    if (sampleName.endsWith("_sum")) agg.histogramSum += value;
                    if (sampleName.endsWith("_count")) agg.histogramCount += value;
                }

                /* -------- GAUGE -------- */
                if (metricType.equals("gauge")) {
                    agg.gauges.put(sampleName, value);
                }
            }
        }

        /* ===============================
           2. KPIs (derived dynamically)
           =============================== */
        double totalRequests = group.values().stream()
                .mapToDouble(a -> a.counters.values().stream().mapToDouble(Double::doubleValue).sum())
                .sum();

        double totalLatencySum = group.values().stream().mapToDouble(a -> a.histogramSum).sum();
        double totalLatencyCount = group.values().stream().mapToDouble(a -> a.histogramCount).sum();

        List<KpiDTO> kpis = new ArrayList<>();
        kpis.add(kpi("total_requests", "Total Requests", (long) totalRequests));
        if (totalLatencyCount > 0) {
            kpis.add(kpi("avg_latency", "Average Latency", round(totalLatencySum / totalLatencyCount)));
        }
        kpis.add(kpi("error_count", "Errors", 0));
        
        double benchmarkAccuracy = group.values().stream()
                .flatMap(a -> a.gauges.entrySet().stream())
                .filter(e -> e.getKey().contains("benchmark_accuracy"))
                .mapToDouble(Map.Entry::getValue)
                .findFirst().orElse(0);
        kpis.add(kpi("benchmark_accuracy", "Accuracy", (long) benchmarkAccuracy));

        MetaDTO meta = new MetaDTO(kpis, null);
        dto.setMeta(meta);
        dto.setMetricTables(buildMetricTables(metrics));

        /* ===============================
           3. Columns
           =============================== */
        dto.setColumns(List.of(
                column("metric", "Metric", "string"),
                column("model", "Model", "string"),
                column("operation", "Operation", "string"),
                column("value", "Value", "number"),
                column("unit", "Unit", "string")
        ));

        /* ===============================
           4. Rows (fully dynamic)
           =============================== */
        List<RowDTO> rows = new ArrayList<>();
        long id = 1;

        for (Map.Entry<String, Aggregation> e : group.entrySet()) {
            String[] parts = e.getKey().split("\\|");
            String model = parts[0];
            String operation = parts.length > 1 && !parts[1].equals("system") && !parts[1].equals("general") ? parts[1] : "-";

            Aggregation a = e.getValue();
            double avgLatency = a.histogramCount > 0 ? a.histogramSum / a.histogramCount : 0;
            double rowRequests = a.counters.values().stream().mapToDouble(Double::doubleValue).sum();
            double totalInputTokens = a.counters.entrySet().stream()
                    .filter(entry -> entry.getKey().contains("input"))
                    .mapToDouble(Map.Entry::getValue).sum();
            double totalOutputTokens = a.counters.entrySet().stream()
                    .filter(entry -> entry.getKey().contains("output"))
                    .mapToDouble(Map.Entry::getValue).sum();

            if (rowRequests > 0) {
                rows.add(new RowDTO(id++, "Total Requests", "Healthy", operation, model, String.valueOf((long)rowRequests), "count", null, null, Collections.emptyList()));
            }
            if (avgLatency > 0) {
                rows.add(new RowDTO(id++, "Average Latency", "Healthy", operation, model, String.valueOf(round(avgLatency)), "seconds", null, null, Collections.emptyList()));
            }
            if (totalInputTokens > 0) {
                rows.add(new RowDTO(id++, "Total Input Tokens", "Healthy", "-", model, String.valueOf((long)totalInputTokens), "tokens", null, null, Collections.emptyList()));
            }
            if (totalOutputTokens > 0) {
                rows.add(new RowDTO(id++, "Total Output Tokens", "Healthy", "-", model, String.valueOf((long)totalOutputTokens), "tokens", null, null, Collections.emptyList()));
            }
        }

        dto.setRows(rows);
        return dto;
    }

    /* ===============================
       Helpers
       =============================== */

    private KpiDTO kpi(String id, String title, Object value) {
        return new KpiDTO(id, title, String.valueOf(value), "N/A", "neutral", false);
    }

    private ColumnDTO column(String key, String label, String type) {
        return new ColumnDTO(key, label, type);
    }

    private double round(double v) {
        return Math.round(v * 100.0) / 100.0;
    }

    private List<MetricTableDTO> buildMetricTables(JsonNode metrics) {

        Map<String, MetricTableDTO> tablesMap = new LinkedHashMap<>();
        
        tablesMap.put("ai_requests", table("AI Request Metrics"));
        tablesMap.put("ai_request_duration", table("AI Latency Metrics"));
        tablesMap.put("ai_tokens", table("AI Token Usage Metrics"));
        tablesMap.put("benchmark", table("AI Benchmark Metrics"));
        tablesMap.put("python", table("Python / System Metrics"));

        for (JsonNode metric : metrics) {

            String metricName = metric.path("name").asText();
            JsonNode samples = metric.path("samples");
            if (!samples.isArray() || samples.size() == 0) continue;

            MetricTableDTO target = null;
            for (Map.Entry<String, MetricTableDTO> entry : tablesMap.entrySet()) {
                if (metricName.startsWith(entry.getKey())) {
                    target = entry.getValue();
                    break;
                }
            }

            if (target == null) continue;

            for (JsonNode sample : samples) {

                String sampleName = sample.path("name").asText();
                if (sampleName.endsWith("_created")) continue;

                JsonNode labels = sample.path("labels");
                String model = labels.has("model") ? labels.get("model").asText() : "-";
                String operation = labels.has("operation") ? labels.get("operation").asText() : "-";
                String generation = labels.has("generation") ? "generation=" + labels.get("generation").asText() : "-";
                
                double value = sample.path("value").asDouble();
                String valueStr = value == (long)value ? String.valueOf((long)value) : String.valueOf(value);
                String unit = "";

                target.getRows().add(
                    new MetricRowDTO(
                        target.getTitle(),
                        sampleName,
                        model.equals("-") ? generation : model,
                        operation,
                        valueStr,
                        unit
                    )
                );
            }
        }

        return new ArrayList<>(tablesMap.values());
    }

    private MetricTableDTO table(String title) {
        return new MetricTableDTO(title, new ArrayList<>());
    }

    /* ===============================
       Internal aggregation bucket
       =============================== */
    private static class Aggregation {
        String metricName;
        Map<String, Double> counters = new HashMap<>();
        Map<String, Double> gauges = new HashMap<>();
        double histogramSum = 0;
        double histogramCount = 0;
    }
}
