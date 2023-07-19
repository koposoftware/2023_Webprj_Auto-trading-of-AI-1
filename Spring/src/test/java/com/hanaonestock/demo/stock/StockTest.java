package com.hanaonestock.demo.stock;

import com.hanaonestock.AutoAppConfig;
import com.hanaonestock.stock.model.dto.Stock;
import com.hanaonestock.stock.service.StockService;
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
public class StockTest {
    @MockBean
    private JavaMailSender javaMailSender;
    @MockBean
    private HttpSession httpSession;
    @Autowired
    private StockService stockService;

    @Test
    @DisplayName("최종 종목 검색 테스트")
    void searchTest() {
        Stock stock1 = stockService.search("삼성전자");
        Stock stock2 = stockService.search("005930");
        assertThat(stock1).isEqualTo(stock2);
    }

    @Test
    @DisplayName("종목 검색 중 연관 검색 결과 테스트")
    void searchingTest() {
        List<Stock> stockList1 = stockService.searching("삼성전자");
        List<Stock> stockList2 = stockService.searching("005930");
        assertThat(stockList1.get(0)).isEqualTo(stockList2.get(0));
    }
}
