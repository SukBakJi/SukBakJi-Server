package umc.SukBakJi.global.security.oauth2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import umc.SukBakJi.domain.repository.MemberRepository;
import umc.SukBakJi.global.security.jwt.JwtTokenProvider;

import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // OAuth 서비스 이름(kakao, apple)
        String registrationId = userRequest.getClientRegistration()
                .getRegistrationId();

        // OAuth 로그인 시 키(pk)가 되는 값, (kakao: "id", apple: "sub")
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        AppleService.OAuthAttributes attributes = AppleService.OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

//        Member member = authService.processOAuth2User(attributes);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }

//    public Member findOrCreateMember(Provider provider, String providerId, String email) {
//        // 이미 존재하는 회원인지 찾음
//        Member member = memberRepository.findByProviderAndProviderId(provider, providerId)
//                .orElseGet(() -> {
//                    // 회원가입 처리
//                    Member newMember = Member.builder()
//                            .email(email)
//                            .password("default_password")
//                            .provider(provider)
//                            .providerId(providerId)
//                            .build();
//                    return memberRepository.save(newMember);
//                });
//
//        return member;
//    }
//
//    public JwtToken generateJwtToken(Member member) {
//        return jwtTokenProvider.generateToken(new UsernamePasswordAuthenticationToken(
//                member.getEmail(), member.getPassword()
//        ));
//    }
}
