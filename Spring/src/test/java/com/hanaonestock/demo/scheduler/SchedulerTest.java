package com.hanaonestock.demo.scheduler;

import com.hanaonestock.AutoAppConfig;
import com.hanaonestock.EmailConfig;
import com.hanaonestock.scheduler.Scheduler;
import org.assertj.core.api.Assertions;
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

// @SpringBootTest
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(AutoAppConfig.class)
@Transactional
public class SchedulerTest {

    @Autowired
    private Scheduler scheduler;
    @MockBean
    private JavaMailSender javaMailSender;
    @MockBean
    private HttpSession httpSession;


    @Test
    @DisplayName("스케줄링 - restful get 요청 테스트")
    void restfulTest() {
        try {
            scheduler.runAt4PMGet();
        } catch (Exception e) {
            Assertions.fail("트랜잭션 실패");
        }
    }
}
