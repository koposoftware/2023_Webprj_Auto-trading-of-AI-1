<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
    <title></title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/4.1.1/animate.min.css"/>
    <link rel="stylesheet" href="../../../resources/style/common.css">
    <script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
</head>
<body>
<header>
    <nav>
        <a href="/">
            <img src="../../resources/img/logo.png" height="40">
        </a>
        <div>
            <ul>
                <%-- provider 데이터가 "kakao"일 경우 카카오 로그인 버튼을 표시 --%>
                <%
                    String id = (String) session.getAttribute("id");
                    if (id != null) {
                %>
                <li class="nav-list">
                    <a href="recommend" class="nav-menu">추천종목</a>
                <li class="nav-list">
                    <a href="mypage" class="nav-menu">마이페이지</a>
                    <c:if test="${empty provider}">
                <li class="nav-list">
                    <a href="/logoutMember" class="nav-menu">로그아웃</a>
                </li>
                </c:if>
                <c:if test="${provider eq 'kakao'}">
                    <li class="nav-list">
                        <a href="/oauth/logout" class="nav-menu">로그아웃</a>
                    </li>
                </c:if>
                <c:if test="${provider eq 'general'}">
                    <li class="nav-list">
                        <a href="/logoutMember" class="nav-menu">로그아웃</a>
                    </li>
                </c:if>

                <%-- provider 데이터가 "kakao"가 아닐 경우 일반 로그아웃 버튼을 표시 --%>
                <c:if test="${provider eq 'null'}">
                    <li class="nav-list">
                        <a href="/logoutMember" class="nav-menu">로그아웃</a>
                    </li>
                </c:if>
                <%} else{%>
                <li class="nav-list">
                    <a class="nav-menu" onclick="openModalLogin()">로그인</a>
                </li>
                <%}%>
            </ul>
        </div>
    </nav>
</header>
</body>
</html>