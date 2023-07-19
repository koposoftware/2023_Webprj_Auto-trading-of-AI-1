<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/4.1.1/animate.min.css"/>
    <link rel="stylesheet" href="../../resources/style/common.css">
    <link rel="stylesheet" href="../../resources/style/main.css">
    <script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
    <script src="https://unpkg.com/lightweight-charts/dist/lightweight-charts.standalone.production.js"></script>

</head>
<body>
<div class="container">
    <%@ include file="include/header.jsp" %>
    <div class="content">
        <div class="memberinfo_div">
            <div class="chart_div">
                <div class="chart">
                    <div class="stock_title">
                        <span>증시 현황</span>
                    </div>
                    <div id="chart-container"></div>
                    <div style="text-align: center; margin-top: 10px">
                        <span class="small_text" id="latest-value"></span>
                    </div>
                    <script>
                        <%-- JSON 데이터 가져오기 --%>
                        <%@ page import="org.springframework.core.io.ClassPathResource" %>
                        <%@ page import="org.springframework.core.io.Resource" %>
                        <%@ page import="org.apache.commons.io.IOUtils" %>
                        <%@ page import="java.nio.charset.StandardCharsets" %>
                        <%@ page import="java.io.InputStream" %>

                        <%-- JSON 파일 경로 --%>
                        <% String filePath = "/resources/json/kospi.json"; %>

                        <%-- JSON 데이터 읽기 --%>
                        <% Resource resource = new ClassPathResource(filePath); %>
                        <% InputStream inputStream = resource.getInputStream(); %>
                        <% String fileContent = IOUtils.toString(inputStream, StandardCharsets.UTF_8); %>

                        <%-- JSON 데이터 파싱 --%>
                        <%@ page import="org.json.JSONArray" %>
                        <%@ page import="org.json.JSONObject" %>
                        <% JSONArray jsonArray = new JSONArray(fileContent); %>
                    </script>
                </div>
                <div class="chart">
                    <div class="stock_title">
                        <span class="stock_a">특징 종목</span>
                    </div>
                    <div class="stock_type">
                        <button class="stock_button" id="increase_button" data-target="increase_list">상승률</button>
                        <button class="stock_button" id="decrease_button" data-target="decrease_list">하락률</button>
                        <button class="stock_button" id="volume_button" data-target="volume_list">거래량</button>
                        <button class="stock_button" id="trading_button" data-target="trading_list">거래대금</button>
                    </div>

                    <!-- 각 리스트 추가 -->
                    <div class="stock_lists" id="increase_list">
                        <ul id="increase-resultList">
                        </ul>
                    </div>
                    <div class="stock_lists" id="decrease_list">
                        <ul id="decrease-resultList">
                        </ul>
                    </div>

                    <div class="stock_lists" id="volume_list">
                        <ul id="volume-resultList">
                        </ul>
                    </div>

                    <div class="stock_lists" id="trading_list">
                        <ul id="trading-resultList">
                        </ul>
                    </div>
                </div>
            </div>
            <div class="list_div">
                <div class="stock_title">
                    <span>추천 주식</span>
                </div>
                <div style="display: flex; flex-direction: row">
                <c:forEach var="stock" items="${stockList}" varStatus="status">
                    <div class="stock_list">
                        <fmt:formatNumber value="${stock.close}" pattern="#,##0" var="formattedClose"/>
                        <h3><c:out value="${stock.name}"/></h3>
                        <c:choose>
                            <c:when test="${stock.updown >= 0}">
                            <span class="small_text red_text">
                                <c:out value="${stock.updown}"/>%
                                ▲<c:out value="${stock.gap}"/>
                            </span>
                            </c:when>
                            <c:otherwise>
                            <span class="small_text blue_text">
                                <c:out value="${stock.updown}"/>%
                                ▼<c:out value="${stock.gap}"/>
                            </span>
                            </c:otherwise>
                        </c:choose>
                        <h3><c:out value="${formattedClose}"/></h3>
                    </div>
                </c:forEach>
                </div>
            </div>
        </div>
    </div>
    <div class="button-container">
        <input type="button" class="button" value="거래소 둘러보기" onclick="goToDashboard();">
        <input type="button" class="button" value="수익률 확인하기" onclick="goToDashboard2();">
    </div>
    <%@ include file="include/footer.jsp" %>
</div>
</body>
<script>
    function goToDashboard() {
        alert("페이지 이동");
        var link = document.createElement("a");
        link.href = "/dashboard";
        link.click();
    }

    function goToDashboard2() {
        alert("페이지 이동");
        var link = document.createElement("a");
        link.href = "/dashboard2";
        link.click();
    }

    // 증시현황
    var jsonData = <%= jsonArray.toString() %>;

    // JSON 배열에서 마지막 요소를 가져옴
    var latestData = jsonData[jsonData.length - 1];
    document.getElementById("latest-value").textContent = "[" + latestData.time.year + "년 " + latestData.time.month + "월 " + latestData.time.day + "일 " + "의 증시 : " + latestData.value + "]";
    var chart = LightweightCharts.createChart(document.getElementById('chart-container'), {
        width: 500,
        height: 300,
        rightPriceScale: {
            visible: true,
            borderColor: 'rgba(197, 203, 206, 1)',
        },
        leftPriceScale: {
            visible: true,
            borderColor: 'rgba(197, 203, 206, 1)',
        },
        layout: {
            backgroundColor: '#ffffff',
            textColor: 'rgba(33, 56, 77, 1)',
        },
        grid: {
            horzLines: {
                color: '#F0F3FA',
            },
            vertLines: {
                color: '#F0F3FA',
            },
        },
        crosshair: {
            mode: LightweightCharts.CrosshairMode.Normal,
        },
        timeScale: {
            borderColor: 'rgba(197, 203, 206, 1)',
        },
        handleScroll: {
            vertTouchDrag: false,
        },
    });

    var series = chart.addLineSeries({
        color: 'rgba(4, 111, 232, 1)',
        lineWidth: 2,
    });

    var chartData = jsonData.map(function (data) {
        var year = data.time.year;
        var month = data.time.month;
        var day = data.time.day;
        var dateString = year + '/' + month + '/' + day;
        var value = parseFloat(data.value);
        return {time: dateString, value: value};
    });


    series.setData(chartData);


    // 특징 종목
    document.addEventListener('DOMContentLoaded', function () {
        var buttons = document.querySelectorAll('.stock_button');
        buttons.forEach(function (button) {
            button.addEventListener('click', function () {
                // 모든 버튼의 active 클래스 제거
                buttons.forEach(function (btn) {
                    btn.classList.remove('active');
                });

                // 클릭한 버튼에 active 클래스 추가
                this.classList.add('active');

                // 모든 리스트 숨기기
                var lists = document.querySelectorAll('.stock_lists');
                lists.forEach(function (list) {
                    list.style.display = 'none';
                });

                // 클릭한 버튼에 해당하는 리스트 보이기
                var target = this.getAttribute('data-target');
                var list = document.getElementById(target);
                list.style.display = 'block';
            });
        });

        // 초기 데이터 로드
        fetchData("/special-stock/rising-top5", "increase-resultList");
        fetchData("/special-stock/falling-top5", "decrease-resultList");
        fetchData("/special-stock/volume-top5", "volume-resultList");
        fetchData("/special-stock/amount-top5", "trading-resultList");

        // 상승률 버튼 클릭
        var increaseButton = document.getElementById('increase_button');
        increaseButton.click();
    });

    // Ajax 요청
    function fetchData(url, resultListId) {
        $.ajax({
            type: "GET",
            url: url,
            error: function (xhr, status, error) {
                alert(error + "error");
            },
            success: function (response) {
                var resultList = document.getElementById(resultListId);
                resultList.innerHTML = ''; // 기존 리스트 초기화

                // 응답 데이터를 리스트로 추가
                response.forEach(function (item, index) {
                    var listItem = document.createElement('li');

                    var numberSpan = document.createElement('span');
                    numberSpan.textContent = (index + 1);
                    listItem.appendChild(numberSpan);

                    var nameSpan = document.createElement('span');
                    nameSpan.textContent = item.name;
                    listItem.appendChild(nameSpan);

                    var isinSpan = document.createElement('span');
                    isinSpan.textContent = "(" + item.isin + ")";
                    listItem.appendChild(isinSpan);

                    resultList.appendChild(listItem);
                });
            }
        });
    }
</script>
</html>
