package com.hanaonestock.demo.dbconnection;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TestMapper {
    TestDto findTest();
}
