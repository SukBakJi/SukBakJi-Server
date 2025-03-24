package umc.SukBakJi.global.security.oauth2.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import umc.SukBakJi.domain.auth.converter.AuthConverter;
import umc.SukBakJi.domain.auth.model.dto.KakaoUserInfo;
import umc.SukBakJi.domain.auth.model.dto.OAuth2UserInfo;
import umc.SukBakJi.domain.common.entity.enums.Role;
import umc.SukBakJi.domain.member.model.dto.MemberResponseDTO;
import umc.SukBakJi.domain.member.model.entity.Member;
import umc.SukBakJi.domain.common.entity.enums.Provider;
import umc.SukBakJi.domain.member.repository.MemberRepository;
import umc.SukBakJi.global.security.jwt.JwtToken;
import umc.SukBakJi.global.security.jwt.JwtTokenProvider;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class KakaoService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final MemberRepository memberRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String kakaoApiUri;

    public MemberResponseDTO.LoginResponseDto kakaoLogin(String accessToken) {
        // 액세스 토큰 검증 및 사용자 정보 조회
        OAuth2UserInfo userInfo = getUserInfo(accessToken);
        if (userInfo == null) {
            throw new IllegalStateException("카카오 사용자 정보를 가져올 수 없습니다.");
        }

        // 회원 가입 또는 로그인 처리
        Member member = saveOrUpdate(userInfo);
        JwtToken jwtToken = jwtTokenProvider.generateJwtToken(member);

        return AuthConverter.toLoginDto(Provider.KAKAO, member, jwtToken);
    }

    // 카카오 사용자 정보 요청
    private OAuth2UserInfo getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(kakaoApiUri, HttpMethod.GET, request, String.class);

        try {
            Map<String, Object> attributes = objectMapper.readValue(response.getBody(), Map.class);
            return new KakaoUserInfo(attributes);
        } catch (Exception e) {
            throw new IllegalStateException("카카오 사용자 정보 파싱 실패", e);
        }
    }

    // 사용자 정보 저장 및 업데이트
    private Member saveOrUpdate(OAuth2UserInfo kakaoUserInfo) {
        return memberRepository.findByEmailAndProvider(kakaoUserInfo.getEmail(), kakaoUserInfo.getProvider())
                .orElseGet(() -> memberRepository.save(Member.builder()
                        .email(kakaoUserInfo.getEmail())
                        .provider(kakaoUserInfo.getProvider())
                        .role(Role.ROLE_USER)
                        .build()));
    }
}