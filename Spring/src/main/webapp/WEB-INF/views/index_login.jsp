<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/4.1.1/animate.min.css"/>
    <link rel="stylesheet" href="../../resources/style/common.css">
    <link rel="stylesheet" href="../../resources/style/index_login.css">
    <script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
</head>
<body>
<div class="container">
<%@ include file="include/header.jsp" %>
<div class="content">
    <div class="main">
        <div class="animate__animated animate__slideInLeft slow 4s">
            <h1><span class="highlight">HANA - One Stock</span><br>SMART TRADING</h1>
        </div>
        <h3>당연하지 않았던 투자환경, 이제는 당연하게</h3>
        <div onclick="openModal()">
            <a href="#" class="btn">시작하기</a>
        </div>
    </div>
</div>

<div id="myModalMain" class="modalMain">
    <div class="modal-content-main">
        <div class="modal-header-main">
            <br>
            <h2>${name} 환영합니다.</h2>
            <span class="close" onclick="closeModal()">&times;</span>
        </div>
        <div class="modal-body-main">
            <form>
                <div class="form-body-main">
                    <h4>목표 수익률을 입력하고,<br>
                        추천 주식을 확인하세요 !</h4>
                    <input type="text" id="goal" name="goal"><br><br>
                    <%--                    main.jsp로 이동--%>
                    <input type="button" class="button" value="추천 주식 확인하기" onclick="goToMain()">
                </div>
            </form>
        </div>
    </div>
</div>
    <%@ include file="include/footer.jsp" %>
</div>
</body>
<script>
    // 모달 열기 함수
    function openModal() {
        document.getElementById("myModalMain").style.display = "block";
    }

    // 모달 닫기 함수
    function closeModal() {
        document.getElementById("myModalMain").style.display = "none";
    }

    function goToMain() {
        const goal = $('[name="goal"]').val();
        var link = document.createElement("a");
        link.href = "/main?goal=" + goal;
        link.click();
    }
</script>
</html>