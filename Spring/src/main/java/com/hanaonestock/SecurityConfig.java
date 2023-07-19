package com.hanaonestock;

import com.hanaonestock.member.service.KakaoOAuth2ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
//자동으로 생성자 주입
@RequiredArgsConstructor
public class SecurityConfig {
    private final KakaoOAuth2ServiceImpl kakaoOAuth2ServiceImpl;
    // HttpSecurity를 구성하는 SecurityFilterChain을 생성하는 메서드
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        return http.csrf().disable() // csrf 보안 설정 사용 X
                .formLogin().disable() // 폼 로그인 사용 X
                .authorizeRequests() // 사용자가 보내는 요청에 인증 절차 수행 필요
                .antMatchers("/").permitAll()
                //.anyRequest().authenticated() // 나머지 요청들은 모두 인증 절차 수행해야함
                .antMatchers("/api").authenticated()    //인증을 필요로 하지 않는 REST API의 경로
                .and()
                .oauth2Login() // OAuth2를 통한 로그인 사용
                .defaultSuccessUrl("/oauth/loginInfo", true) // 로그인 성공시 이동할 URL
                .userInfoEndpoint() // 사용자가 로그인에 성공하였을 경우,
                .userService(kakaoOAuth2ServiceImpl) // 해당 서비스 로직을 타도록 설정
                .and()
                .and().build();
    }
}
