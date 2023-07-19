package com.hanaonestock.stock.model.dao;

import com.hanaonestock.stock.model.dto.Predict;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface PredictMapper {
    List<Predict> findAll();

    Optional<Predict> findByIsinAndDate(@Param("isin")String isin, @Param("date")String date);

    List<Predict> findByDate(String date);

    int insertData(Predict predict);

    int updateData(Predict predict);

}
