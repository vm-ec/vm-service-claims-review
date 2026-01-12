package com.vm.service.claimsreview.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vm.service.claimsreview.dto.*;
import com.vm.service.claimsreview.entity.MetricsResponse;
import com.vm.service.claimsreview.repository.MetricsResponseRepository;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class MetricsService {

    private final MetricsResponseRepository repository;
    private final ObjectMapper objectMapper;

    public MetricsService(MetricsResponseRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    public MetricsResponseDTO getMetricsResponse() {
        MetricsResponse entity = repository.findTopByOrderByIdDesc()
                .orElseThrow(() -> new RuntimeException("Metrics not found"));

        MetricsResponseDTO dto = new MetricsResponseDTO();

        // META
        if (entity.getMeta() != null) {
            MetaDTO metaDTO = new MetaDTO();
            metaDTO.setTimestamp(entity.getMeta().getTimestamp());
            if (entity.getMeta().getKpis() != null) {
                metaDTO.setKpis(
                        entity.getMeta().getKpis().stream().map(kpi -> {
                            KpiDTO k = new KpiDTO();
                            k.setId(kpi.getKpiId());
                            k.setTitle(kpi.getTitle());
                            k.setValue(kpi.getValue());
                            k.setTrend(kpi.getTrend());
                            k.setTrendType(kpi.getTrendType());
                            k.setIsFinancial(kpi.getIsFinancial());
                            return k;
                        }).collect(Collectors.toList())
                );
            }
            dto.setMeta(metaDTO);
        }

        // COLUMNS
        if (entity.getColumns() != null) {
            dto.setColumns(
                    entity.getColumns().stream().map(col -> {
                        ColumnDTO c = new ColumnDTO();
                        c.setKey(col.getColumnKey());
                        c.setLabel(col.getLabel());
                        c.setType(col.getType());
                        return c;
                    }).collect(Collectors.toList())
            );
        }

        // ROWS
        if (entity.getRows() != null) {
            dto.setRows(
                    entity.getRows().stream().map(row -> {
                        RowDTO r = new RowDTO();
                        r.setId(row.getId());
                        r.setName(row.getName());
                        r.setStatus(row.getStatus());
                        r.setTech(row.getTech());
                        r.setModel(row.getModel());
                        r.setLatency(row.getLatency());
                        r.setRequests(row.getRequests());
                        r.setDrift(row.getDrift());
                        r.setCost(row.getCost());
                        r.setLogs(row.getLogs());
                        return r;
                    }).collect(Collectors.toList())
            );
        }

        return dto;
    }

    public MetricsResponseDTO getSampleData() {
        try {
            String json = "{\"meta\":{\"timestamp\":\"2024-10-24T14:30:00Z\",\"kpis\":[{\"id\":\"total_inf\",\"title\":\"Total Inferences\",\"value\":\"4.2M\",\"trend\":\"▲ 8% vs last week\",\"trendType\":\"good\"},{\"id\":\"avg_lat\",\"title\":\"Avg Latency (P95)\",\"value\":\"520ms\",\"trend\":\"▼ 12ms improvement\",\"trendType\":\"good\"},{\"id\":\"cost\",\"title\":\"Projected Cost\",\"value\":\"$18,240\",\"trend\":\"▲ 2% Over Budget\",\"trendType\":\"warn\",\"isFinancial\":true},{\"id\":\"err_rate\",\"title\":\"Error Rate\",\"value\":\"0.12%\",\"trend\":\"Stable\",\"trendType\":\"neutral\"}]},\"columns\":[{\"key\":\"name\",\"label\":\"Application Name\",\"type\":\"identity\"},{\"key\":\"status\",\"label\":\"Status\",\"type\":\"status\"},{\"key\":\"tech\",\"label\":\"Technology\",\"type\":\"text\"},{\"key\":\"latency\",\"label\":\"Latency (P95)\",\"type\":\"metric\"},{\"key\":\"requests\",\"label\":\"Requests\",\"type\":\"metric\"},{\"key\":\"drift\",\"label\":\"Drift Score\",\"type\":\"number\"},{\"key\":\"cost\",\"label\":\"Cost (24h)\",\"type\":\"financial\"}],\"rows\":[{\"id\":1,\"name\":\"Policy CoPilot\",\"status\":\"Healthy\",\"tech\":\"LLM\",\"model\":\"GPT-4o\",\"latency\":\"850ms\",\"requests\":\"1.2M\",\"drift\":0.02,\"cost\":\"$420.00\",\"logs\":[\"Query sanitized\",\"Response generated\"]},{\"id\":2,\"name\":\"Fraud Detector\",\"status\":\"Critical\",\"tech\":\"ML\",\"model\":\"XGBoost\",\"latency\":\"120ms\",\"requests\":\"8.5M\",\"drift\":0.45,\"cost\":\"$80.00\",\"logs\":[\"CRITICAL: Drift detected\"]}]}";
            return objectMapper.readValue(json, MetricsResponseDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse sample data", e);
        }
    }
}

