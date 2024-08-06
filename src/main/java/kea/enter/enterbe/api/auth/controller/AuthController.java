package kea.enter.enterbe.api.auth.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import kea.enter.enterbe.api.auth.dto.LoginRequestDto;
import kea.enter.enterbe.api.auth.dto.LoginResponseDto;
import kea.enter.enterbe.api.auth.dto.ReissueResponseDto;
import kea.enter.enterbe.api.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "인증 및 인가 컨트롤러", description = "인증 및 인가 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
        @RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(authService.login(loginRequestDto));
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(
        @RequestHeader("Authorization") String accessToken
    ) {
        return ResponseEntity.ok(authService.logout(accessToken.substring(7)));
    }

    @GetMapping("/reissue")
    public ResponseEntity<ReissueResponseDto> reissue(
        @RequestHeader("Authorization") String refreshToken
    ) {
        /*
        authentication.getAuthorities() : 권한
        authentication.getName() : memberId를 반환함

        getName()이 기분이 나쁘다면...
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getId();
        String token = redisUtil.getValue(userId.toString());
        return ResponseEntity.ok(authService.reissue(token));

        식으로 사용하면 됨

        */
        return ResponseEntity.ok(authService.reissue(refreshToken.substring(7)));
    }

}
