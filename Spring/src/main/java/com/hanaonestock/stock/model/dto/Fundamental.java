package com.hanaonestock.stock.model.dto;

import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class Fundamental {

    private String isin;
    private String s_date;
    private double per;
    private int eps;
    private int bps;
    private double pbr;
    private int div;
    private int dps;

}
