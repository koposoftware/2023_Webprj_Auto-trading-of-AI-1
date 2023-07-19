package com.hanaonestock.demo.transaction;

import com.hanaonestock.AutoAppConfig;
import com.hanaonestock.EmailConfig;
import com.hanaonestock.stock.model.dto.Ohlcv;
import com.hanaonestock.transaction.model.dto.BuyDto;
import com.hanaonestock.transaction.model.dto.SellDto;
import com.hanaonestock.transaction.service.TransactionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(AutoAppConfig.class)
@Transactional
public class TransactionTest {

    @Autowired
    private TransactionService transactionService;
    @MockBean
    private JavaMailSender javaMailSender;
    @MockBean
    private HttpSession httpSession;


    @Test
    @DisplayName("매수, 매도 테스트")
    void buyAndSellTest(){
        int state = 0;
        BuyDto buyDto = new BuyDto();
        buyDto.setId("test");
        buyDto.setIsin("testIsin");
        buyDto.setPrice(10000);
        buyDto.setVolume(5);
        state = transactionService.buy(buyDto);
        assertThat(state).isEqualTo(1);

        SellDto sellDto = new SellDto();
        sellDto.setId("test");
        sellDto.setIsin("testIsin");
        sellDto.setPrice(2000);
        state = transactionService.sell(sellDto);
        assertThat(state).isEqualTo(1);
    }

    @Test
    @DisplayName("종목 검색 테스트 (종목번호, 종목명)")
    void searchTest(){
        List<Ohlcv> ohlcvList = transactionService.search("005930");
        List<Ohlcv> ohlcvList2 = transactionService.search("삼성전자");
        assertThat(ohlcvList).isEqualTo(ohlcvList2);
    }
}
