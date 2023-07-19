//package com.hanaonestock.demo.stock;
//
//import com.hanaonestock.stock.controller.StockController;
//import com.hanaonestock.stock.model.dto.Ohlcv;
//import com.hanaonestock.stock.service.OhlcvService;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.ResponseEntity;
//import org.springframework.mail.javamail.JavaMailSender;
//
//import javax.servlet.http.HttpSession;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.when;
//
//public class GetSpecialStockTest {
//    @MockBean
//    private JavaMailSender javaMailSender;
//    @MockBean
//    private HttpSession httpSession;
//    @Mock
//    private OhlcvService ohlcvService;
//
//    @InjectMocks
//    private StockController stockController;
//
//    @Test
//    public void testFindRisingTop5() throws Exception {
//        // Arrange
//        List<Ohlcv> ohlcvList = Arrays.asList(new Ohlcv(), new Ohlcv(), new Ohlcv(), new Ohlcv(), new Ohlcv());
//        List<String> stockNames = Arrays.asList("Stock1", "Stock2", "Stock3", "Stock4", "Stock5");
//        when(ohlcvService.findRisingTop5ByDate()).thenReturn(ohlcvList);
//        ResponseEntity<Map<List<Ohlcv>, List<String>>> expectedResponse = ResponseEntity.ok().body(Map.of(ohlcvList, stockNames));
//
//        // Act
//        ResponseEntity<Map<List<Ohlcv>, List<String>>> actualResponse = stockController.findRisingTop5();
//
//        // Assert
//        assertEquals(expectedResponse.getStatusCode(), actualResponse.getStatusCode());
//        assertEquals(expectedResponse.getBody(), actualResponse.getBody());
//
//    }
//}
