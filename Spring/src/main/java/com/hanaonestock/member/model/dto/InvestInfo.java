package com.hanaonestock.member.model.dto;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class InvestInfo {

    @Id
    private String id;
    private String type;
    private int cash;
    private int goal;
    private int period;
    private int rebalance;

}