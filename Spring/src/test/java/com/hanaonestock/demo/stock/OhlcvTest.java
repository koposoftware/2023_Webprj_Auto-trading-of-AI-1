package com.hanaonestock.demo.stock;

import com.hanaonestock.AutoAppConfig;
import com.hanaonestock.stock.model.dto.Ohlcv;
import com.hanaonestock.stock.service.OhlcvService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mail.javamail.JavaMailSender;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(AutoAppConfig.class)
public class OhlcvTest {
    @MockBean
    private JavaMailSender javaMailSender;
    @MockBean
    private HttpSession httpSession;
    @Autowired
    private OhlcvService ohlcvService;

    @Test
    @DisplayName("특징주 테스트")
    void findSpecialStockTest() {

        try{
            List<Ohlcv> ohlcvList1 = ohlcvService.findRisingTop5ByDate();
            List<Ohlcv> ohlcvList2 = ohlcvService.findFallingTop5ByDate();
            List<Ohlcv> ohlcvList3 = ohlcvService.findVolumeTop5ByDate();
            List<Ohlcv> ohlcvList4 = ohlcvService.findAmountTop5ByDate();

            for (Ohlcv ohlcv : ohlcvList1) {
                System.out.println(ohlcv.getUpdown());
            }

            for (Ohlcv ohlcv : ohlcvList2) {
                System.out.println(ohlcv.getUpdown());
            }

            for (Ohlcv ohlcv : ohlcvList3) {
                System.out.println(ohlcv.getUpdown());
            }

            for (Ohlcv ohlcv : ohlcvList4) {
                System.out.println(ohlcv.getAmount());
            }

        } catch (Exception e) {
        }

    }
}
