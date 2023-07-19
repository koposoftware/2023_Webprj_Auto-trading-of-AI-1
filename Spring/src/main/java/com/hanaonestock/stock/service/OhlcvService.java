package com.hanaonestock.stock.service;

import com.hanaonestock.stock.model.dto.Ohlcv;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

public interface OhlcvService {

    public List<Ohlcv> findAll();
    List<Ohlcv> findRisingTop5ByDate();
    List<Ohlcv> findFallingTop5ByDate();
    List<Ohlcv> findVolumeTop5ByDate();
    List<Ohlcv> findAmountTop5ByDate();
}