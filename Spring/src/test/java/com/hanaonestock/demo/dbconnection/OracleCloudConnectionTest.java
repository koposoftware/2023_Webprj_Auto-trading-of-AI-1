package com.hanaonestock.demo.dbconnection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(AutoAppConfig.class)
public class OracleCloudConnectionTest {

    @Autowired
    private TestMapper testMapper;

    @Test
    @DisplayName("오라클 클라우드 연결 테스트")
    void connect() {
        assertThat(testMapper).isNotNull();
        TestDto testDto = testMapper.findTest();
        System.out.println(testDto.getTest());
        assertThat(testDto.getTest()).isEqualTo("test");
    }
}
