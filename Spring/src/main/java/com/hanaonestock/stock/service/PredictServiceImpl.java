package com.hanaonestock.stock.service;

import com.hanaonestock.stock.model.dao.PredictMapper;
import com.hanaonestock.stock.model.dto.Predict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PredictServiceImpl implements PredictService{

    private final PredictMapper predictMapper;

    @Autowired
    public PredictServiceImpl(PredictMapper predictMapper) {
        this.predictMapper = predictMapper;
    }

    @Override
    public List<Predict> findAll(){
        return predictMapper.findAll();
    }

    @Override
    public int insertData(Predict predict){
        return predictMapper.insertData(predict);
    }

    @Override
    public int updateData(Predict predict){
        return predictMapper.updateData(predict);
    }
}
