package com.hanaonestock.stock.service;

import com.hanaonestock.stock.model.dto.Predict;
import java.util.List;

public interface PredictService {

    public List<Predict> findAll();

    public int insertData(Predict predict);

    public int updateData(Predict predict);

}
