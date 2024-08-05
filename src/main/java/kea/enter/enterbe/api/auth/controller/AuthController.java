package kea.enter.enterbe.api.auth.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import kea.enter.enterbe.api.auth.dto.LoginRequestDto;
import kea.enter.enterbe.api.auth.dto.LoginResponseDto;
import kea.enter.enterbe.api.auth.service.AuthService;
import kea.enter.enterbe.global.common.exception.CustomException;
import kea.enter.enterbe.global.common.exception.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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
    public ResponseEntity<LoginResponseDto> login(LoginRequestDto loginRequestDto, Authorization authorization) {
        if (authorization != null) {
            // 이미 로그인한 상태
            throw new CustomException(ResponseCode.ALREADY_LOGGED_IN);
        }
        return ResponseEntity.ok(authService.login(loginRequestDto));
    }

}
