package com.hanaonestock.member.model.dto;

import lombok.Data;

@Data
public class Deposit {

    private int d_id; // default d_id_seq.nextval
    private String id;
    private int amount;
    private String s_date; // default sysdate + 3/8
}
