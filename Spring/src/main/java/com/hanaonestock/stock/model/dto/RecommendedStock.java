package com.hanaonestock.stock.model.dto;

import lombok.Data;

import java.util.Date;

@Data
public class RecommendedStock {

    private String isin;
    private Date s_date;
    private String name;
    private double updown;
    private int close;
    private int gap;

}
