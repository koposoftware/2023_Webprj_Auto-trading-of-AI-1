package com.hanaonestock.transaction.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanaonestock.member.model.dto.Deposit;
import com.hanaonestock.member.model.dto.InvestInfo;
import com.hanaonestock.member.service.MemberService;
import com.hanaonestock.stock.model.dto.Ohlcv;
import com.hanaonestock.stock.model.dto.Stock;
import com.hanaonestock.transaction.model.dto.BuyDto;
import com.hanaonestock.transaction.model.dto.DailyPerformance;
import com.hanaonestock.transaction.model.dto.Result;
import com.hanaonestock.transaction.model.dto.SellDto;
import com.hanaonestock.transaction.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.util.ResourceUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.List;
import javax.servlet.ServletContext;

@Controller
public class TransactionController {

    @Value("${performance.json.path}")
    private String performanceJsonPath;
    private final TransactionService transactionService;
    private final MemberService memberService;

    @Autowired
    private ServletContext servletContext;

    @Autowired
    public TransactionController(TransactionService transactionService, MemberService memberService) {
        this.transactionService = transactionService;
        this.memberService = memberService;
    }

    @RequestMapping("/trading")
    public ModelAndView trading() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("trading");
        return mav;
    }

    @RequestMapping("/dashboard2")
    public ModelAndView result(HttpSession session) {
        String id = (String) session.getAttribute("id");
        if (id == null) {
            return new ModelAndView("redirect:/index");  // 초기 페이지로 리디렉션
        }

        List<Result> resultList = transactionService.transactionsByMember(id);
        List<DailyPerformance> dailyPerformanceList = transactionService.dailyPerformanceByMember(id);
        if (resultList == null) {
            return new ModelAndView("error");  // 오류 페이지로 이동
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File(performanceJsonPath);
            objectMapper.writeValue(file, dailyPerformanceList);
        } catch (Exception e) {
            e.printStackTrace();
            return new ModelAndView("error");  // 오류 페이지로 이동
        }

        ModelAndView modelAndView = new ModelAndView("dashboard2");
        modelAndView.addObject("resultList", resultList);
        return modelAndView;
    }

    @ResponseBody
    @GetMapping(value = "/get-chart")
    public ResponseEntity<String> getStockChartData(@RequestParam("input") String input) {
        List<Ohlcv> ohlcvList = transactionService.search(input);
        ObjectMapper objectMapper = new ObjectMapper();
        String json;
        try {
            json = objectMapper.writeValueAsString(ohlcvList);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>("Error processing JSON", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "/get-user-cash")
    public ResponseEntity<Integer> getUserCash(@RequestParam("id") String id) {
        Integer cash = memberService.findUserCash(id);
        System.out.println("id = " + id);
        System.out.println("캐쉬: " + memberService.findUserCash(id));
        if (cash == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(cash);
    }

    @PostMapping("/buy-transaction")
    public ResponseEntity<?> processBuyTransaction(@RequestBody BuyDto buyDto) {
        int cash = memberService.findUserCash(buyDto.getId());
        int state = transactionService.buy(buyDto);
        if (state != 1 || buyDto.getVolume() == 0) {
            System.out.println("매수 트젝 실패");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (cash - (buyDto.getPrice() * buyDto.getVolume()) < 0) {
            System.out.println("시드보다 큰 매수");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        int state2 = memberService.updateInvestInfoCashById(buyDto.getId(), cash - (buyDto.getPrice() * buyDto.getVolume()));
        if (state2 != 1) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
//        System.out.println(state2);
//        System.out.println(buyDto.getId());
//        System.out.println(cash - (buyDto.getPrice() * buyDto.getVolume()));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/sell-transaction")
    public ResponseEntity<?> processSellTransaction(@RequestBody SellDto sellDto) {
        System.out.println("sellDto.getIsin() = " + sellDto.getIsin());
        System.out.println("sellDto.getId() = " + sellDto.getId());
        int volume = transactionService.sumHasVolume(sellDto.getId(), sellDto.getIsin());
        int state = transactionService.sell(sellDto);
        if (state < 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        int cash = memberService.findUserCash(sellDto.getId());
        int newCash = cash + (volume * sellDto.getPrice());
        int state2 = memberService.updateInvestInfoCashById(sellDto.getId(), newCash);
        if (state2 != 1) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        System.out.println(newCash);
        System.out.println(sellDto.getPrice());
        System.out.println(volume);
        // 모든 것이 정상적으로 처리되면, HTTP 상태 코드 200(OK)을 반환합니다.
        return ResponseEntity.ok().build();
    }

    @ResponseBody
    @GetMapping(value = "/get-user-goal")
    public ResponseEntity<Integer> getUserGoal(@RequestParam("id") String id) {
        Integer goal = memberService.findUserGoal(id);
        System.out.println("id = " + id);
        System.out.println("goal: " + goal);
        if (goal == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(goal);
    }

    @ResponseBody
    @GetMapping(value = "/deposit-user-cash")
    public ResponseEntity<Integer> depositUserCash(@RequestParam("id") String id, @RequestParam("amount") int amount) {
        int state = memberService.deposit(id, amount);
        System.out.println("id, amount = " + id + amount);
        if (state != 1) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(state);
    }

    @GetMapping(value = "/get-deposit-history")
    public ResponseEntity<?> getDepositHistory(@RequestParam("id") String id) {
        List<Deposit> depositList = memberService.selectDeposit(id);
        System.out.println(depositList);
        if (depositList == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(depositList);
    }


    @ResponseBody
    @GetMapping(value = "/selectDayOfTransaction")
    public ResponseEntity<Double> selectDayOfTransaction(@RequestParam("id") String id) {
        try {
            double dayProfit = transactionService.selectDayOfTransaction(id);
            return ResponseEntity.ok(dayProfit);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @ResponseBody
    @GetMapping(value = "/sumHasVolume")
    public ResponseEntity<Integer> selectDayOfTransaction(@RequestParam("id") String id, @RequestParam("isin") String isin) {
        int volume = 0;
        try {
            volume = transactionService.sumHasVolume(id, isin);
            System.out.println(id + isin);
            System.out.println(volume);
            return ResponseEntity.ok(volume);
        } catch (Exception e) {
            e.printStackTrace();
            return (ResponseEntity<Integer>) ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
