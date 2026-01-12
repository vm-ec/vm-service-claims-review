package com.vm.service.claimsreview.dto;


import java.util.List;

public class RowDTO {
    private Long id;
    private String name;
    private String status;
    private String tech;
    private String model;
    private String latency;
    private String requests;
    private Double drift;
    private String cost;
    private List<String> logs;
    // getters & setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;}
    public String getName() {
        return name;

    }
    public void setName(String name) {
        this.name = name;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getTech() {
        return tech;
    }
    public void setTech(String tech) {
        this.tech = tech;
    }
    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }
    public String getLatency() {
        return latency;
    }
    public void setLatency(String latency) {
        this.latency = latency;
    }
    public String getRequests() {
        return requests;
    }
    public void setRequests(String requests) {
        this.requests = requests;
    }
    public Double getDrift() {
        return drift;
    }
    public void setDrift(Double drift) {
        this.drift = drift;
    }
    public String getCost() {
        return cost;
    }
    public void setCost(String cost) {
        this.cost = cost;
    }
    public List<String> getLogs() {
        return logs;
    }
    public void setLogs(List<String> logs) {
        this.logs = logs;

    }

}
