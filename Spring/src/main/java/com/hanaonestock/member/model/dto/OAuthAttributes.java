package com.hanaonestock.member.model.dto;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

public enum OAuthAttributes {
    KAKAO("kakao", (attribute) -> {
        Map<String, Object> account = (Map)attribute.get("kakao_account");
        Map<String, String> profile = (Map)account.get("profile");
        Member member = new Member();
        member.setName(profile.get("nickname"));
        member.setEmail((String)account.get("email"));
        return member;
    });
    // registrationId : 로그인한 서비스를 식별하는 문자열을 저장하고
    // of : 사용자 속성 정보를 UserProfile 객체로 변환하는 함수를 저장
    private final String registrationId;
    private final Function<Map<String, Object>, Member> of;

    OAuthAttributes(String registrationId, Function<Map<String, Object>, Member> of) {
        this.registrationId = registrationId;
        this.of = of;
    }
    //extract : registrationId와 attributes를 입력으로 받아 해당 registrationId에 대한 OAuthAttributes를 찾고, of 함수를 호출하여 attributes를 기반으로 UserProfile 객체를 생성하여 반환
    public static Member extract(String registrationId, Map<String, Object> attributes) {
        return Arrays.stream(values())
                .filter(value -> registrationId.equals(value.registrationId))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .of.apply(attributes);
    }
}