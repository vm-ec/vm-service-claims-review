package com.vm.service.claimsreview.service;

import java.util.Map;

public class ObservabilityService {
    public Map<String, String> getObservabilityContext() {
        // Implementation to retrieve observability context
        return Map.of("metrics", getSampledata()); // Placeholder implementation
    }
    public Map<String , String> getUserContext() {
        // Implementation to retrieve user context
        return Map.of(); // Placeholder implementation
    }
    private String getSampledata(){
        return "{\n" +
                "  \"meta\": {\n" +
                "    \"timestamp\": \"2024-10-24T14:30:00Z\",\n" +
                "    \"kpis\": [\n" +
                "      { \"id\": \"total_inf\", \"title\": \"Total Inferences\", \"value\": \"4.2M\", \"trend\": \"▲ 8% vs last week\", \"trendType\": \"good\" },\n" +
                "      { \"id\": \"avg_lat\", \"title\": \"Avg Latency (P95)\", \"value\": \"520ms\", \"trend\": \"▼ 12ms improvement\", \"trendType\": \"good\" },\n" +
                "      { \"id\": \"cost\", \"title\": \"Projected Cost\", \"value\": \"$18,240\", \"trend\": \"▲ 2% Over Budget\", \"trendType\": \"warn\", \"isFinancial\": true },\n" +
                "      { \"id\": \"err_rate\", \"title\": \"Error Rate\", \"value\": \"0.12%\", \"trend\": \"Stable\", \"trendType\": \"neutral\" }\n" +
                "    ]\n" +
                "  },\n" +
                "  \"columns\": [\n" +
                "    { \"key\": \"name\", \"label\": \"Application Name\", \"type\": \"identity\" },\n" +
                "    { \"key\": \"status\", \"label\": \"Status\", \"type\": \"status\" },\n" +
                "    { \"key\": \"tech\", \"label\": \"Technology\", \"type\": \"text\" },\n" +
                "    { \"key\": \"latency\", \"label\": \"Latency (P95)\", \"type\": \"metric\" },\n" +
                "    { \"key\": \"requests\", \"label\": \"Requests\", \"type\": \"metric\" },\n" +
                "    { \"key\": \"drift\", \"label\": \"Drift Score\", \"type\": \"number\" },\n" +
                "    { \"key\": \"cost\", \"label\": \"Cost (24h)\", \"type\": \"financial\" } \n" +
                "  ],\n" +
                "  \"rows\": [\n" +
                "    {\n" +
                "      \"id\": 1,\n" +
                "      \"name\": \"Policy CoPilot\",\n" +
                "      \"status\": \"Healthy\",\n" +
                "      \"tech\": \"LLM\",\n" +
                "      \"model\": \"GPT-4o\",\n" +
                "      \"latency\": \"850ms\",\n" +
                "      \"requests\": \"1.2M\",\n" +
                "      \"drift\": 0.02,\n" +
                "      \"cost\": \"$420.00\",\n" +
                "      \"logs\": [\"Query sanitized\", \"Response generated\"]\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 2,\n" +
                "      \"name\": \"Fraud Detector\",\n" +
                "      \"status\": \"Critical\",\n" +
                "      \"tech\": \"ML\",\n" +
                "      \"model\": \"XGBoost\",\n" +
                "      \"latency\": \"120ms\",\n" +
                "      \"requests\": \"8.5M\",\n" +
                "      \"drift\": 0.45,\n" +
                "      \"cost\": \"$80.00\",\n" +
                "      \"logs\": [\"CRITICAL: Drift detected\"]\n" +
                "    }\n" +
                "  ]\n" +
                "}";
    }
}
