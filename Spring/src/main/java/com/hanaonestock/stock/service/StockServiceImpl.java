package com.hanaonestock.stock.service;

import com.hanaonestock.stock.model.dao.StockMapper;
import com.hanaonestock.stock.model.dto.RecommendedStock;
import com.hanaonestock.stock.model.dto.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class StockServiceImpl implements StockService {

    private final StockMapper stockMapper;

    @Autowired
    public StockServiceImpl(StockMapper stockMapper) {
        this.stockMapper = stockMapper;
    }

    /**
     * 종목 검색 : 검색 결과 Stock 객체 반환
     */
    @Override
    public Stock search(String input) {
        Stock stock = null;
        Optional<Stock> stockOptional = null;
        try {
            stockOptional = stockMapper.findByName(input);

            // 이름 검색 결과 확인
            if (stockOptional.isPresent()) {
                stock = stockOptional.get();
            } else {
                // 이름 검색 결과 없음
                stockOptional = stockMapper.findByIsin(input);
                // isin 검색 결과 확인
                if (stockOptional.isPresent()) {
                    stock = stockOptional.get();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stock;
    }

    /**
     *  검색 중 : 종목 검색 중 실시간 비동기 Stock 객체 반환
     */
    @Override
    public List<Stock> searching(String input) {
        List<Stock> stockList = null;
        try {
            stockList = stockMapper.stockListFindByName(input);
            // 이름 검색 결과 확인
            if (stockList.isEmpty()) {
                stockList = stockMapper.stockListFindByIsin(input);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return stockList;
    }

    //오늘의 추천 주식 받아오기
    @Override
    public List<RecommendedStock> recommendedStock() {
        List<RecommendedStock> stockList = null;
        try {
            stockList = stockMapper.recommendedStock();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stockList;
    }

    @Override
    public void updateGoalOfInvestInfoById(HashMap<String,String> stockData) {
        stockMapper.updateGoalOfInvestInfoById(stockData);
    }

    @Override
    public List<Stock> selectAssetsById(String id) {
        return stockMapper.selectAssetsById(id);
    }

}
