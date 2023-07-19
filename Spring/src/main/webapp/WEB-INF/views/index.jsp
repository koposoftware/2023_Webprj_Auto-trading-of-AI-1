<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/4.1.1/animate.min.css"/>
    <link rel="stylesheet" href="../../resources/style/common.css">
    <link rel="stylesheet" href="../../resources/style/index.css">
    <script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
</head>
<body>
<div class="container">
    <%@ include file="include/header.jsp" %>
    <div class="content">
        <div class="main">
            <div class="animate__animated animate__slideInLeft slow 4s">
                <h1><span class="highlight">HANA - One Stock<br></span>SMART TRADING</h1>
            </div>
            <h3>당연하지 않았던 투자환경, 이제는 당연하게</h3>
            <c:choose>
                <c:when test="${not empty sessionScope.id}">
                    <div onclick="openModalMain()">
                        <a href="#" class="btn">시작하기</a>
                    </div>
                </c:when>
                <c:otherwise>
                    <div onclick="openModalLogin()">
                        <a href="#" class="btn">시작하기</a>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
    <div id="myModalLogin" class="modalLogin">
        <div class="modal-content">
            <div class="modal-header">
                <img src="../../resources/img/logo2.png" width="205">
            </div>
            <div class="modal-body">
                <div class="form-body">
                    <h2>로그인</h2>
                    <form id="loginForm" method="post">
                        <div class="form-group">
                            <label for="username">아이디</label>
                            <input type="text" id="username" name="id">
                        </div>
                        <div class="form-group">
                            <label for="password">비밀번호</label>
                            <input type="password" id="password" name="pw">
                        </div>
                        <input type="button" class="button" value="로그인" onclick="loginFormFunc(); return false;">
                        <a href="join" class="button">회원가입</a>
                    </form>
                    <br>
                    <a href="/oauth2/authorization/kakao" class="kakao_btn"><img src="../../resources/img/kakao.png"
                                                                                 width="20"> 카카오로 시작하기</a>
                </div>
                <br>
                <div class="text">
                    <p>소셜 계정으로 로그인 또는 가입 시 <br>개인정보 처리 방침 및 서비스 이용약관에 동의한 것으로 간주합니다.</p>
                </div>
            </div>
            <span class="close" onclick="closeModalLogin()">&times;</span>
        </div>
    </div>
    <div id="myModalMain" class="modalMain">
        <div class="modal-content-main">
            <div class="modal-header-main">
                <br>
                <span style="font-size: 30px;"><h2>${name} 환영합니다.</h2></span>
                <span class="close-main" onclick="closeModalMain()">&times;</span>
            </div>
            <div class="modal-body-main">
                <form style="text-align: center">
                    <div class="form-body-main">
                        <h4>목표 수익률을 입력하고,<br>
                            추천 주식을 확인하세요 !</h4>
                        <input type="text" id="goal" name="goal"><br><br>
                        <%--                    main.jsp로 이동--%>
                        <input type="button" class="button-main" value="추천 주식 확인하기" onclick="goToMain()">
                    </div>
                </form>
            </div>
        </div>
    </div>
    <%@ include file="include/footer.jsp" %>
</div>
</body>
<script type="text/javascript">
    // 모달 열기 함수
    function openModalLogin() {
        document.getElementById("myModalLogin").style.display = "block";
    }
    // 모달 닫기 함수
    function closeModalLogin() {
        document.getElementById("myModalLogin").style.display = "none";
    }
    function openModalMain(){
        document.getElementById("myModalMain").style.display = "block";
    }
    // 모달 닫기 함수
    function closeModalMain() {
        document.getElementById("myModalMain").style.display = "none";
    }
    // main 이동
    function goToMain() {
        const goal = $('[name="goal"]').val();
        var link = document.createElement("a");
        link.href = "/main?goal=" + goal;
        link.click();
    }
    // 로그인
    function loginFormFunc() {
        var formData = $("#loginForm").serialize();
        var id = $("#username").val();
        var password = $("#password").val();

        $.ajax({
            type: "POST",
            url: "/loginMember",
            data: JSON.stringify({
                id: id,
                password: password
            }),
            contentType: 'application/json',
            error: function (xhr, status, error) {
                alert(error + "error");
            },
            success: function (response) {
                if (response === "로그인 성공") {
                    // index_login.jsp로 이동
                    alert("로그인 성공");
                    var link = document.createElement("a");
                    link.href = "/";
                    link.click();
                } else {
                    console.error("로그인 실패");
                }
            }
        });
    }
</script>
</html>