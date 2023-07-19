package com.hanaonestock.stock.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanaonestock.stock.model.dto.Ohlcv;
import com.hanaonestock.stock.service.OhlcvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;

import java.util.List;

@Controller
public class OhlcvController {

    private final OhlcvService ohlcvService;

    @Autowired
    public OhlcvController(OhlcvService ohlcvService) {
        this.ohlcvService = ohlcvService;
    }

    @ResponseBody
    @GetMapping(value = "/stock")
    public ResponseEntity<String> index(Model model) {
        List<Ohlcv> ohlcvList = ohlcvService.findAll();
        ObjectMapper objectMapper = new ObjectMapper();
        String json;
        try {
            json = objectMapper.writeValueAsString(ohlcvList);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>("Error processing JSON", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        model.addAttribute("ohlcvList",ohlcvList);
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

}
