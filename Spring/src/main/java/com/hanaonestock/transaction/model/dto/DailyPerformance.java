package com.hanaonestock.transaction.model.dto;

import lombok.Data;
@Data
public class DailyPerformance {
    private String dateBuy;
    private String dailyPerformance;
    private String goal;

    // Default constructor
    public DailyPerformance() {
    }
}
