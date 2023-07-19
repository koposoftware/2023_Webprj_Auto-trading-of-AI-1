package com.hanaonestock.transaction.model.dto;

import lombok.Data;

@Data
public class SellDto {

    private String isin; // 종목명
    private String id; // 사용자 아이디
    private int price; // 매도가
}