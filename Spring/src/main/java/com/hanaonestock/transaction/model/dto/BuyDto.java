package com.hanaonestock.transaction.model.dto;

import lombok.Data;

@Data
public class BuyDto {

    private String isin; // 종목명
    private String id; // 사용자 아이디
    private int price; // 매수가
    private int volume; // 수량

}
