package com.vm.service.claimsreview.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
}
