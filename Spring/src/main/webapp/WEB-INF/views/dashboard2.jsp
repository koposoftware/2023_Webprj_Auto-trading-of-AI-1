<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/4.1.1/animate.min.css"/>
    <link rel="stylesheet" href="../../resources/style/common.css">
    <link rel="stylesheet" href="../../resources/style/dashboard2.css">
    <script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
    <script src="https://unpkg.com/lightweight-charts/dist/lightweight-charts.standalone.production.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/chart.js@3.5.1"></script>
    <script>
        var cash;
    </script>
</head>
<body>
<div class="container">
    <%@ include file="include/header.jsp" %>
    <div class="content">
        <div class="content-container">
            <div class="chart2">
                <div class="chart-menu">
                    <c:set var="results" value="${requestScope.results}"/>
                    <h3>거래내역</h3>
                    <table>
                        <tr class="results-title">
                            <th class="highlight">종목명</th>
                            <th class="highlight">구매가</th>
                            <th class="highlight">판매가</th>
                            <th class="highlight">구매수량</th>
                        </tr>
                        <c:forEach items="${resultList}" var="result">
                            <tr>
                                <td class="box">${result.name}</td>
                                <td class="box">${result.buy}</td>
                                <td class="box">${result.sell}</td>
                                <td class="box">${result.volume}</td>
                            </tr>
                        </c:forEach>
                    </table>
                </div>
                <div class="button-container">
                    <div onclick="openModal()">
                        <a href="#" class="button">자세히 보기</a>
                    </div>
                </div>
            </div>
            <div class="chart_div">
                <div class="chart">
                    <h3>${name}님의 수익률</h3>
                    <div style="width: 600px; height:450px; margin-bottom: 50px">
                        <canvas id="myLineChart"></canvas>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div id="myModal" class="modal">
        <div class="modal-content">
            <div class="modal-header">
                <br>
                <h2>${name}님의 거래내역</h2>
                <span class="close" onclick="closeModal()">&times;</span>
            </div>
            <div class="modal-body" style="max-height: 400px; overflow-y: auto;">
                <div style="display: flex; justify-content: center; height: 100%;">
                    <table style="width: 90%; table-layout: fixed; margin: 15px 0 15px 0">
                    <tr class="results-title">
                        <th class="highlight">종목명</th>
                        <th class="highlight">종목코드</th>
                        <th class="highlight">구매가</th>
                        <th class="highlight">판매가</th>
                        <th class="highlight">구매수량</th>
                        <th class="highlight">매수일자</th>
                        <th class="highlight">매도일자</th>
                    </tr>

                    <c:forEach items="${resultList}" var="result">
                        <tr>
                            <td class="box">${result.name}</td>
                            <td class="box">${result.isin}</td>
                            <td class="box">${result.buy}</td>
                            <td class="box">${result.sell}</td>
                            <td class="box">${result.volume}</td>
                            <td class="box">${result.date_buy}</td>
                            <td class="box">${result.date_sell}</td>
                        </tr>
                    </c:forEach>
                </table>
            </div>
        </div>
    </div>

</div>
<%@ include file="include/footer.jsp" %>
</div>
</body>
<script>
    $(document).ready(function () {
        $.ajax({
            url: '/dashboard',
            type: 'GET',
            success: function (json) {
                const data = JSON.parse(json);
                alert("data " + data);
            },
            error: function (xhr, status, error) {
                console.error('Error:', error);
            }
        });
    });
    // JSON 파일 가져오기
    fetch("/resources/json/performance.json")
        .then(response => response.json())
        .then(data => {

            /// 데이터 가공
            const chartData = {
                labels: data.map(item => item.dateBuy),
                datasets: [
                    {
                        label: 'Performance',
                        borderColor: 'rgba(255, 99, 132, 1)',   // Red
                        fill: false,
                        data: data.map(item => item.dailyPerformance)
                    },
                    {
                        label: 'Goal',
                        borderColor: 'rgba(54, 162, 235, 1)',    // Blue
                        fill: false,
                        data: data.map(item => item.goal)
                    }
                ]
            };

            // 차트 생성
            var ctx1 = document.getElementById("myLineChart");
            var myLineChart = new Chart(ctx1, {
                type: 'line',
                data: chartData,
                options: {
                    layout: {
                        padding: {
                            top: 0,    // 위쪽 패딩 조정
                            bottom: 0, // 아래쪽 패딩 조정
                            left: 0,   // 왼쪽 패딩 조정
                            right: 0   // 오른쪽 패딩 조정
                        }
                    },
                    maintainAspectRatio: false,
                    scales: {
                        xAxes: [{
                            display: true,
                            scaleLabel: {
                                display: true,
                                labelString: 'Date'
                            }
                        }],
                        yAxes: [{
                            display: true,
                            scaleLabel: {
                                display: true,
                                labelString: 'Amount'
                            }
                        }]
                    }
                }
            });

            // 차트 제목 업데이트
            document.getElementById("chartTitle").textContent = "수익률";
        })
        .catch(error => {
            console.error("JSON 파일을 로드하는 중 오류가 발생했습니다:", error);
        });

    // 모달 열기 함수
    function openModal() {
        document.getElementById("myModal").style.display = "block";
    }

    // 모달 닫기 함수
    function closeModal() {
        document.getElementById("myModal").style.display = "none";
    }
</script>
</html>
