package kea.enter.enterbe.api.auth.service;

import kea.enter.enterbe.api.auth.dto.MemberInfoDto;
import kea.enter.enterbe.domain.member.entity.Member;
import kea.enter.enterbe.domain.member.entity.MemberState;
import kea.enter.enterbe.domain.member.repository.MemberRepository;
import kea.enter.enterbe.global.common.exception.CustomException;
import kea.enter.enterbe.global.common.exception.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        Member member = memberRepository.findByIdAndState(Long.valueOf(id), MemberState.ACTIVE)
            .orElseThrow(() -> new CustomException(ResponseCode.MEMBER_NOT_FOUND));
        MemberInfoDto memberInfoDto = MemberInfoDto.toDto(member);

        return new CustomUserDetails(memberInfoDto);
    }
}
