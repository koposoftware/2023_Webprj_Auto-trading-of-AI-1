package com.hanaonestock.transaction.model.dao;

import com.hanaonestock.transaction.model.dto.DailyPerformance;
import com.hanaonestock.transaction.model.dto.Transaction;
import org.apache.ibatis.annotations.Mapper;
import com.hanaonestock.transaction.model.dto.Result;
import java.util.Map;
import java.util.List;
import java.util.Optional;

@Mapper
public interface TransactionMapper {
    // 매수
    int insertBuyTransaction(Transaction transaction);

    // 매도
    int updateSellTransaction(Transaction transaction);

    // 보유 해당 주식 수
    Optional<Integer> sumHasVolumeByIdIsin(Map<String, Object> idAndIsin);

    List<Result> findTransactionsById(String id);
    double selectDayOfTransaction(String id);
    List<DailyPerformance> dailyPerfomanceById(String id);
}
