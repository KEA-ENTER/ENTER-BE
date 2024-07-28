package kea.enter.enterbe.domain.auth;

import jakarta.transaction.Transactional;
import kea.enter.enterbe.domain.member.entity.MemberRepository;
import kea.enter.enterbe.domain.member.entity.Member;
import kea.enter.enterbe.global.security.JwtUtil;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
@Builder
public class AuthService {

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Transactional
    public String login(LoginDto loginDto) {
        String email = loginDto.getEmail();
        String password = loginDto.getPassword();
        Member member = memberRepository.findMemberByEmail(email);
        if ( member == null ) {
            throw new UsernameNotFoundException("null");
        }

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new BadCredentialsException("Bad");
        }

        return jwtUtil.createAccessToken(member);
    }
}
