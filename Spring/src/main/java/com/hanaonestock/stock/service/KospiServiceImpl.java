package com.hanaonestock.stock.service;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class KospiServiceImpl implements KospiService{
    @Value("${flask.server.url}")
    private String flaskServerUrl;
    @Value("${kospi.json.path}")
    private String kospiJsonPath;
    private LocalDate today;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    @Autowired
    public KospiServiceImpl(){}

    private String getResquestJson() {
        today = LocalDate.now();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> flaskResponse = restTemplate.getForEntity(flaskServerUrl + "/stock_info/kospi/" + today.format(formatter), String.class);
        return flaskResponse.getBody().replaceAll("^\"|\"$", "").replaceAll("\\\\", "").toLowerCase();
    }

    @Override
    public boolean writeKospiData() {
        String kospiJson = getResquestJson();
        File file = new File(kospiJsonPath);
        try {
            // JSON 데이터를 파일로 저장
            FileUtils.writeStringToFile(file, kospiJson, StandardCharsets.UTF_8);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
