<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <html lang="ko">
    <head>
        <meta charset="UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="../../resources/style/common.css">
        <link rel="stylesheet" href="../../resources/style/join.css">
        <script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
    </head>
    <body>
    <div class="container">
        <nav>
            <a href="/">
                <img src="../../resources/img/logo.png" height="40">
            </a>
            <div>
                <ul>
                    <li class="nav-list">
                        <a href="" class="nav-menu">서비스소개</a>
                    </li>
                    <li class="nav-list">
                        <a href="#" class="nav-menu" onclick="openModal()">로그인</a>
                    </li>
                </ul>
            </div>
        </nav>
        <div class="member">
            <form id="signupForm">
                <input type="hidden" value="${provider}" name="provider">
                <h1>회원가입</h1>
                <br>
                <div class="field">
                    <b>아이디</b>
                    <span><input type="text" name="id"></span>
                    <span class="idChk" id="idChk" style="color: green;"></span>
                </div>
                <div class="field">
                    <b>비밀번호</b>
                    <input class="userpw" type="password" name="password">
                    <span class="pwChk" style="color: green;"></span>
                </div>
                <div class="field">
                    <b>비밀번호 재확인</b>
                    <input class="userpw-confirm" type="password" name="password_confirm">
                    <span class="pwChkRe" style="color: orange;"></span>
                </div>
                <div class="field">
                    <b>이름</b>
                    <input type="text" name="name" value="${name}">
                </div>

                <div class="field email">
                    <b>본인 확인 이메일</b>
                    <div>
                        <input type="email" name="email" value="${email}">
                        <input type="button" value="인증메일 받기" onclick="sendVerificationEmail()">
                        <input type="text" placeholder="인증번호를 입력하세요" name="ePw">
                        <input type="button" value="인증하기" onclick="verifyEmail()">
                    </div>
                </div>
                <div class="field">
                    <b>전화번호</b>
                    <input type="tel" placeholder="전화번호 입력" name="phone">
                </div>
                <div class="field">
                    <b>목표수익률</b>
                    <input type="text" placeholder="목표수익률 입력" name="goal">
                </div>
                <input type="submit" value="가입하기"></button>
            </form>
        </div>
        <br>
        <%@ include file="include/footer.jsp" %>
    </div>
    </body>
    <script>
        // 이메일 인증
        var ePw; // 서버에서 전송된 인증번호를 저장할 변수

        // 이메일 전송 버튼 클릭 시
        function sendVerificationEmail() {
            // 입력된 이메일 가져오기
            var email = $('input[name="email"]').val();

            // 이메일 유효성 검사
            var emailRegex = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
            if (!emailRegex.test(email)) {
                alert('유효한 이메일 주소를 입력해주세요.');
                return;
            }

            // 서버로 이메일 전송 요청 보내기
            $.ajax({
                url: '/sendEmail',
                type: 'POST',
                data: {email: encodeURI(email)},
                success: function (response) {
                    alert('이메일 전송 성공');
                    console.log(response);
                    ePw = response; // 서버에서 전송된 인증번호 저장
                },
                error: function () {
                    alert('이메일 전송 실패');
                }
            });
        }

        // 이메일 인증 확인 버튼 클릭 시
        function verifyEmail() {
            var enteredCode = $('input[name="ePw"]').val();
            if (enteredCode === ePw) {
                alert('인증 성공');
                // 인증 성공 시, 필요한 추가 동작 수행
            } else {
                alert('인증 실패');
            }
        }

        // 회원 가입
        $('#signupForm').submit(function (event) {
            event.preventDefault(); // 기본 동작 방지 (페이지 새로고침)

            // 입력된 정보 가져오기
            const id = $('#signupForm input[name="id"]').val();
            const password = $('#signupForm input[name="password_confirm"]').val();
            const name = $('#signupForm input[name="name"]').val();
            const email = $('#signupForm input[name="email"]').val();
            const phoneNumber = $('#signupForm input[name="phone"]').val();
            const provider = $('[name="provider"]').val();
            const goal = $('[name="goal"]').val();

            // Check if email verification is completed
            if (ePw === undefined) {
                alert("이메일 인증을 완료해야 합니다.");
                return;
            }

            const data = {
                id: id,
                password: password,
                name: name,
                email: email,
                phoneNumber: phoneNumber,
                provider: provider,
                goal: goal
            };

            // Ajax 요청 보내기
            $.ajax({
                url: '/insertMember',
                type: 'POST',
                data: JSON.stringify(data),
                contentType: 'application/json',
                success: function (response) {
                    if (response === "회원 등록 성공") {
                        alert("회원 등록 성공");
                        var link = document.createElement("a");
                        link.href = "/";
                        link.click();
                    } else {
                        console.error("회원 등록 실패");
                    }
                }
            });
        });

        // 유효성 검사
        var timer; // 타이머 변수

        $('[name=id]').on('input', function () {
            clearTimeout(timer);
            var id = $(this).val();
            timer = setTimeout(function () {
                $.ajax({
                    url: "/idCheck",
                    data: {id: id},
                    type: "post",
                    success: function (response) {
                        if (response.exists) {
                            $("#idChk").html("이미 사용중인 아이디 입니다.");
                        } else {
                            $("#idChk").html("사용가능한 아이디 입니다.");
                        }
                    }
                });
            }, 300);
        })

        $('[name=password]').on('keyup', function (event) {
            if (/(?=.*\d{1,50})(?=.*[~`!@#$%\^&*()-+=]{1,50})(?=.*[a-zA-Z]{2,50}).{8,50}$/g.test($('[name=password]').val())) {
                $('.pwChk').html("<i class='bi bi-exclamation-circle'></i>");
            } else {
                $('.pwChk').html("<i class='bi bi-exclamation-circle'></i> 숫자, 특문 각 1회 이상, 영문은 2개 이상 사용하여 8자리 이상 입력");
            }
        });
        $('[name=password_confirm]').focusout(function () {
            var pwd1 = $("[name=password]").val();
            var pwd2 = $("[name=password_confirm]").val();

            if (pwd1 != '' && pwd2 == '') {
                null;
            } else if (pwd1 != "" || pwd2 != "") {
                if (pwd1 == pwd2) {
                    $('.pwChkRe').html("비밀번호가 일치합니다.");
                } else {
                    $('.pwChkRe').html("비밀번호를 다시 입력해주세요.");
                }
            }
        });
    </script>
    </html>