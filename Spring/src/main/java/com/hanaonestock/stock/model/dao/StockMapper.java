package com.hanaonestock.stock.model.dao;

import com.hanaonestock.stock.model.dto.RecommendedStock;
import com.hanaonestock.stock.model.dto.Stock;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Mapper
public interface StockMapper {
    Optional<Stock> findByName(String name);
    Optional<Stock> findByIsin(String isin);
    List<Stock> stockListFindByName(String name);
    List<Stock> stockListFindByIsin(String isin);
    List<RecommendedStock> recommendedStock();
    void updateGoalOfInvestInfoById(HashMap<String,String> stockData);
    List<Stock> selectAssetsById(String id);
}