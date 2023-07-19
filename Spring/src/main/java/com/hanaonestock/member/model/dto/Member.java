package com.hanaonestock.member.model.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class Member {
    private int m_id;
    private String id;
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private String provider;
    private int goal;
    public Member() {
    }
}
