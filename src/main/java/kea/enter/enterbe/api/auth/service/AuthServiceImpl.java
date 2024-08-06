package kea.enter.enterbe.api.auth.service;

import java.time.Duration;
import kea.enter.enterbe.api.auth.dto.LoginRequestDto;
import kea.enter.enterbe.api.auth.dto.LoginResponseDto;
import kea.enter.enterbe.api.auth.dto.MemberInfoDto;
import kea.enter.enterbe.api.auth.dto.ReissueResponseDto;
import kea.enter.enterbe.domain.member.entity.Member;
import kea.enter.enterbe.domain.member.entity.MemberState;
import kea.enter.enterbe.domain.member.repository.MemberRepository;
import kea.enter.enterbe.global.common.exception.CustomException;
import kea.enter.enterbe.global.common.exception.ResponseCode;
import kea.enter.enterbe.global.redis.RedisUtil;
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
    private final RedisUtil redisUtil;
    private final Duration expireTime = Duration.ofSeconds(864000); // 2 weeks

    @Override
    public String logout(String accessToken) {
        // 로그아웃은 클라이언트에서 처리하도록 하자
        jwtUtil.validateToken(accessToken); // 어차피 안 되면 오류를 뿜으니까~
        redisUtil.deleteValue(jwtUtil.getMemberId(accessToken).toString()); // 로그아웃 처리
        return "Logout Success";
    }

    @Override
    public ReissueResponseDto reissue(String refreshToken) {

        MemberInfoDto memberInfo = MemberInfoDto.toDto(getMember(jwtUtil.getMemberId(refreshToken)));

        jwtUtil.validateToken(refreshToken);

        String newRefreshToken = jwtUtil.createRefreshToken(memberInfo);
        redisUtil.deleteValue(jwtUtil.getMemberId(refreshToken).toString());
        redisUtil.setValues(memberInfo.getId().toString(), newRefreshToken, expireTime);

        return ReissueResponseDto.of(
            jwtUtil.createAccessToken(memberInfo),
            jwtUtil.createRefreshToken(memberInfo));
    }

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        String email = loginRequestDto.getEmail();
        String password = loginRequestDto.getPassword();

        Member member = memberRepository.findMemberByEmailAndState(email, MemberState.ACTIVE).orElseThrow(() -> new CustomException(
            ResponseCode.MEMBER_NOT_FOUND));

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new CustomException(ResponseCode.PASSWORD_INCORRECT);
        }


        MemberInfoDto memberInfo = MemberInfoDto.toDto(member);
        String refreshToken = jwtUtil.createRefreshToken(memberInfo);
        String accessToken = jwtUtil.createAccessToken(memberInfo);
        redisUtil.setValues(memberInfo.getId().toString(), refreshToken, expireTime);

        return LoginResponseDto.of(
            accessToken,
            refreshToken,
            member.getName(),
            member.getRole()
        );
    }

    public Member getMember(Long memberId) {
        return memberRepository.findByIdAndState(memberId, MemberState.ACTIVE).orElseThrow(() -> new CustomException(ResponseCode.MEMBER_NOT_FOUND));
    }

}
