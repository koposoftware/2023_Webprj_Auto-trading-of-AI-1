package com.hanaonestock.stock.model.dao;

import com.hanaonestock.stock.model.dto.Fundamental;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface FundamentalMapper {

    List<Fundamental> findAll();

    Optional<Fundamental> findByIsinAndDate(String isin, String Date);

    int insertData(Fundamental fundamental);

}