package com.hanaonestock.transaction.service;

import com.hanaonestock.stock.model.dao.OhlcvMapper;
import com.hanaonestock.stock.model.dao.StockMapper;
import com.hanaonestock.stock.model.dto.Ohlcv;
import com.hanaonestock.stock.model.dto.Stock;
import com.hanaonestock.transaction.model.dao.TransactionMapper;
import com.hanaonestock.transaction.model.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionMapper transactionMapper;
    private final OhlcvMapper ohlcvMapper;
    private final StockMapper stockMapper;

    private LocalDate currentDate = LocalDate.now();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    private String now = currentDate.format(formatter);

    @Autowired
    public TransactionServiceImpl(TransactionMapper transactionMapper, OhlcvMapper ohlcvMapper, StockMapper stockMapper) {
        this.transactionMapper = transactionMapper;
        this.ohlcvMapper = ohlcvMapper;
        this.stockMapper = stockMapper;
    }

    /**
     *  매수 기능 : buyDto 활용 transaction 테이블에 매수 내역 기록
     */
    @Override
    public int buy(BuyDto buyDto) {
        int state = 0;
        Transaction buyTransaction = new Transaction();
        buyTransaction.setId(buyDto.getId());
        buyTransaction.setIsin(buyDto.getIsin());
        buyTransaction.setBuy(buyDto.getPrice());
        buyTransaction.setVolume(buyDto.getVolume());
        buyTransaction.setDateBuy(now);
        buyTransaction.setDuration("S");

        try {
            state = transactionMapper.insertBuyTransaction(buyTransaction);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return state;
    }

    /**
     *  매도 기능 : sellDto 활용 transaction 테이블에 매도 내용 업데이트
     */
    @Override
    public int sell(SellDto sellDto) {
        int state = 0;
        Transaction sellTransaction = new Transaction();
        sellTransaction.setId(sellDto.getId());
        sellTransaction.setIsin(sellDto.getIsin());
        sellTransaction.setSell(sellDto.getPrice());
        sellTransaction.setDateSell(now);
        sellTransaction.setDuration("S");

        try {
            state = transactionMapper.updateSellTransaction(sellTransaction);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return state;
    }

    /**
     * 종목 검색 기능 : 종목코드, 종목명 입력시 해당 주식 종목의 모든(10년치) ohlcv 데이터를 반환.
     */
    @Override
    public List<Ohlcv> search(String input) {
        List<Ohlcv> ohlcvList = null;
        Stock stock;
        Optional<Stock> stockOptional;

        try {
            stockOptional = stockMapper.findByName(input);

            // 이름 검색 결과 확인
            if (stockOptional.isPresent()) {
                stock = stockOptional.get();
                ohlcvList = ohlcvMapper.findByIsin(stock.getIsin());
            } else {
                // 이름 검색 결과 없음
                stockOptional = stockMapper.findByIsin(input);
                // isin 검색 결과 확인
                if (stockOptional.isPresent()) {
                    ohlcvList = ohlcvMapper.findByIsin(stockOptional.get().getIsin());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ohlcvList;
    }

    @Override
    public int sumHasVolume(String id, String isin) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("isin", isin);
        try{
            Optional<Integer> optionalInteger = transactionMapper.sumHasVolumeByIdIsin(map);
            if(optionalInteger.isPresent()){
                return optionalInteger.get().intValue();
            } else {
                System.out.println("null임");
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public List<Result> transactionsByMember(String id) {
        List<Result> results = null;
        Stock stock;

        try {
            results = transactionMapper.findTransactionsById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    @Override
    public double selectDayOfTransaction(String id) {
        return transactionMapper.selectDayOfTransaction(id);
    }

    @Override
    public List<DailyPerformance> dailyPerformanceByMember(String id) {
        List<DailyPerformance> results = null;
        try {
            results = transactionMapper.dailyPerfomanceById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }
}
