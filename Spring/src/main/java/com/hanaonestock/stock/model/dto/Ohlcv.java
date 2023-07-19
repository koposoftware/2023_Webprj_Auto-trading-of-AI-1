package com.hanaonestock.stock.model.dto;

import lombok.Data;
import java.util.Date;

@Data
public class Ohlcv {
    private String name;
    private String isin;
    private String s_date;
    private int open;
    private int high;
    private int low;
    private int close;
    private long volume;
    private long amount;
    private double updown;


}
