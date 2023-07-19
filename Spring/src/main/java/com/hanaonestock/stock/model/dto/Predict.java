package com.hanaonestock.stock.model.dto;
import java.util.Date;
import lombok.Data;

@Data
public class Predict {

    private String isin;
    private String s_date;
    private int close;
    private int p_price;
    private int r_price;
    private double p_rate;
    private double r_rate;
    private int predict;
    private int correct;
    private double error;

}
