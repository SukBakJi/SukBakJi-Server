package umc.SukBakJi.global.security.oauth2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import umc.SukBakJi.domain.model.entity.Member;
import umc.SukBakJi.domain.repository.MemberRepository;
import umc.SukBakJi.global.apiPayload.code.status.ErrorStatus;
import umc.SukBakJi.global.apiPayload.exception.handler.MemberHandler;
import umc.SukBakJi.global.security.PrincipalDetails;

@Service
@RequiredArgsConstructor
public class PrincipalService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        Member member = memberRepository.findByName(name)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        return new PrincipalDetails(member);
    }
}