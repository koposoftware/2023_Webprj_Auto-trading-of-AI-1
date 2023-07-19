package com.hanaonestock.stock.model.dto;

import lombok.Data;

@Data
public class Stock {
    private String isin;
    private String name;
    private String totalPrice;
}
