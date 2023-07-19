package com.hanaonestock.stock.model.dao;

import com.hanaonestock.stock.model.dto.Fundamental;
import com.hanaonestock.stock.model.dto.Ohlcv;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface OhlcvMapper {

    List<Ohlcv> findAll();

    Optional<Ohlcv> findByIsinAndDate(@Param("isin")String isin, @Param("date")String date);

    List<Ohlcv> findByIsin(String isin);

    int insertData(Ohlcv ohlcv);

    List<Ohlcv> findRisingTop5ByDate(@Param("date")String date);
    List<Ohlcv> findFallingTop5ByDate(@Param("date")String date);
    List<Ohlcv> findVolumeTop5ByDate(@Param("date")String date);
    List<Ohlcv> findAmountTop5ByDate(@Param("date")String date);

}
