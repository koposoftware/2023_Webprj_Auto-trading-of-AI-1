package com.hanaonestock.transaction.model.dto;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
public class Transaction {
//    @Id
//    private Long tId; // DB 시퀸스 사용
    @Id
    private String id; // 사용자 아이디
    private int buy;
    private int sell; // nullable
    private int volume;
    private String isin;
    private String dateBuy;
    private String dateSell; // nullable
    private String duration; // "S" or "L"

    // Default constructor
    public Transaction() {
    }
}
