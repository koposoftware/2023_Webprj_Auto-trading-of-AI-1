package com.hanaonestock.scheduler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanaonestock.stock.model.dao.FundamentalMapper;
import com.hanaonestock.stock.model.dao.OhlcvMapper;
import com.hanaonestock.stock.model.dao.PredictMapper;
import com.hanaonestock.stock.model.dto.Fundamental;
import com.hanaonestock.stock.model.dto.Ohlcv;
import com.hanaonestock.stock.model.dto.Predict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 *  flask Restful API에 주가 정보 요청 (매일 16시)
 */
@Component
public class Scheduler {

    @Autowired
    OhlcvMapper ohlcvMapper;
    @Autowired
    FundamentalMapper fundamentalMapper;
    @Autowired
    PredictMapper predictMapper;

    @Value("${flask.server.url}")
    private String flaskServerUrl;
    private LocalDate today;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final ObjectMapper mapper = new ObjectMapper();
    public static final String ohlcvStr = "ohlcv";
    public static final String fundamentalStr = "fundamental";
    public static final String predictStr = "predict";
    public static final String scoreStr = "score";

    /**
     * flask api에 주가 정보 데이터를 요청하고 json 형식의 String을 리턴
     */
    private String getResquestJson(String str) {
        today = LocalDate.now();
        LocalDate chooseDay = null;
        RestTemplate restTemplate = new RestTemplate();

        switch (str) {
            case ohlcvStr:
            case fundamentalStr:
                chooseDay = today;
                break;
            case predictStr:
            case scoreStr:
                chooseDay = today.plusDays(1);
                break;
            default:
                return null;
        }

        ResponseEntity<String> flaskResponse = restTemplate.getForEntity(flaskServerUrl + "/stock_info/" + str + "/" + chooseDay.format(formatter), String.class);
        return flaskResponse.getBody().replaceAll("^\"|\"$", "").replaceAll("\\\\", "").toLowerCase();
    }

    /**
     * fundamental json 데이터를 Fundamental List로 변환하여 리턴
     */
    private List<Fundamental> createFundamentalsFromJson(String json) {

        try {
            // Remove the starting and ending double quotes from the JSON string
            json = json.substring(1, json.length() - 1);
            List<Fundamental> fundamentals = mapper.readValue(json, new TypeReference<List<Fundamental>>() {
            });
            return fundamentals;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Ohlcv json 데이터를 Ohlcv List로 변환하여 리턴
     */
    public List<Ohlcv> createOhlcvsFromJson(String json) {

        try {
            // Remove the starting and ending double quotes from the JSON string
            json = json.substring(1, json.length() - 1);
            List<Ohlcv> Ohlcvs = mapper.readValue(json, new TypeReference<List<Ohlcv>>() {});
            return Ohlcvs;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Predict json 데이터를 Predict List로 변환하여 리턴
     */
    public List<Predict> createPredictsFromJson(String json) {

        try {
//            // Remove the starting and ending double quotes from the JSON string
//            json = json.substring(1, json.length() - 1);
            List<Predict> Predict = mapper.readValue(json, new TypeReference<List<Predict>>() {});
            return Predict;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 16시에 실행되는 스프링 스케줄러
     */
    @Scheduled(cron = "0 0 16 * * ?")
    public void runAt4PMGet() {

        String fundamentalJson = getResquestJson(fundamentalStr);
        String ohlcvJson = getResquestJson(ohlcvStr);
        String predictJson = getResquestJson(predictStr);
        String scoreJson = getResquestJson(scoreStr);

        List<Fundamental> fundamentals = createFundamentalsFromJson(fundamentalJson);
        List<Ohlcv> ohlcvs = createOhlcvsFromJson(ohlcvJson);
        List<Predict> predict = createPredictsFromJson(predictJson);
        List<Predict> score = createPredictsFromJson(scoreJson);

        saveOhlcv(ohlcvs);
        saveFundamental(fundamentals);
        savePredict(predict);
        saveScore(score);
    }

    /**
     * Fundamental List를 각각 DB에 저장 (mybatis 활용)
     */
    private int saveFundamental(List<Fundamental> fundamentals) {
        Fundamental tmp = null;
        int state = 0;
        try {
            for (Fundamental fundamental : fundamentals) {
                fundamental.setIsin(fundamental.getIsin().toUpperCase());
                tmp = fundamental;
                state = fundamentalMapper.insertData(fundamental);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(tmp.toString());
        }
        return state;
    }

    /**
     * Ohlcv List를 각각 DB에 저장 (mybatis 활용)
     */
    private int saveOhlcv(List<Ohlcv> ohlcvs) {
        Ohlcv tmp = null;
        int state = 0;
        try {
            for (Ohlcv ohlcv : ohlcvs) {
                ohlcv.setIsin(ohlcv.getIsin().toUpperCase());
                tmp = ohlcv;
                state = ohlcvMapper.insertData(ohlcv);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(tmp.toString());
        }
        return state;
    }

    /**
     * Predict List를 각각 DB에 저장 (mybatis 활용)
     */
    private int savePredict(List<Predict> predicts) {
        Predict tmp = null;
        int state = 0;
        try {
            for (Predict predict : predicts) {
                predict.setIsin(predict.getIsin().toUpperCase());
                tmp = predict;
                state = predictMapper.insertData(predict);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(tmp.toString());
        }
        return state;
    }

    /**
     * Score List를 각각 DB에 저장 (mybatis 활용)
     */
    private int saveScore(List<Predict> scores) {
        Predict tmp = null;
        int state = 0;
        try {
            for (Predict score : scores) {
                score.setIsin(score.getIsin().toUpperCase());
                tmp = score;
                state = predictMapper.updateData(score);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(tmp.toString());
        }
        return state;
    }
}
