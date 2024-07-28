package kea.enter.enterbe.domain.auth;

import kea.enter.enterbe.domain.member.entity.Member;
import kea.enter.enterbe.domain.member.entity.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailsService implements UserDetailsService {
    private MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        Member member = memberRepository.findById(Long.parseLong(id))
            .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저가 없습니다."));

        LoginUser dto = LoginUser.of(member.getId(), member.getEmail(), member.getName(), member.getPassword(), member.getRole());
        return new CustomUserDetails(dto);
    }
}