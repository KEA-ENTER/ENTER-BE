package kea.enter.enterbe.api.auth.service;

import kea.enter.enterbe.api.auth.dto.LoginRequestDto;
import kea.enter.enterbe.api.auth.dto.LoginResponseDto;
import kea.enter.enterbe.api.auth.dto.MemberInfoDto;
import kea.enter.enterbe.domain.member.entity.Member;
import kea.enter.enterbe.domain.member.repository.MemberRepository;
import kea.enter.enterbe.global.common.exception.CustomException;
import kea.enter.enterbe.global.common.exception.ResponseCode;
import kea.enter.enterbe.global.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        String email = loginRequestDto.getEmail();
        String password = loginRequestDto.getPassword();

        Member member = memberRepository.findMemberByEmail(email).orElseThrow(() -> new CustomException(
            ResponseCode.MEMBER_NOT_FOUND));

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new CustomException(ResponseCode.PASSWORD_INCORRECT);
        }

        MemberInfoDto memberInfo = MemberInfoDto.toDto(member);

        return LoginResponseDto.of(
            jwtUtil.createAccessToken(memberInfo),
            jwtUtil.createRefreshToken(memberInfo),
            member.getName(),
            member.getRole()
        );
    }

}
