package com.hanaonestock.stock.service;

import com.hanaonestock.stock.model.dao.OhlcvMapper;
import com.hanaonestock.stock.model.dto.Ohlcv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class OhlcvServiceImpl implements OhlcvService{

    private OhlcvMapper ohlcvMapper;
    private LocalDate currentDate = LocalDate.now();
    private LocalDate previousDate = currentDate.minusDays(1);
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    private String previousDay = previousDate.format(formatter);


    @Autowired
    public OhlcvServiceImpl(OhlcvMapper ohlcvMapper){
        this.ohlcvMapper = ohlcvMapper;
    }

    /**
     *  테스트용 모든 데이터 검색
     */
    @Override
    public List<Ohlcv> findAll(){
        return ohlcvMapper.findAll();
    }
    /**
     *  상승률 탑 5 종목
     */
    @Override
    public List<Ohlcv> findRisingTop5ByDate() {
        return ohlcvMapper.findRisingTop5ByDate(previousDay);
    }

    /**
     *  하락률 탑 5 종목
     */
    @Override
    public List<Ohlcv> findFallingTop5ByDate() {
        return ohlcvMapper.findFallingTop5ByDate(previousDay);
    }

    /**
     *  거래랑 상위 5 종목
     */
    @Override
    public List<Ohlcv> findVolumeTop5ByDate() {
        return ohlcvMapper.findVolumeTop5ByDate(previousDay);
    }

    /**
     *  거래대금 상위 5 종목
     */
    @Override
    public List<Ohlcv> findAmountTop5ByDate() {
        return ohlcvMapper.findAmountTop5ByDate(previousDay);
    }

}
