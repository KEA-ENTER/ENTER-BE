package kea.enter.enterbe.api.auth.service;

import kea.enter.enterbe.api.auth.dto.LoginRequestDto;
import kea.enter.enterbe.api.auth.dto.LoginResponseDto;
import kea.enter.enterbe.api.auth.dto.ReissueResponseDto;
import kea.enter.enterbe.global.common.api.CustomResponseCode;

public interface AuthService {
    LoginResponseDto login(LoginRequestDto loginRequestDto);
    ReissueResponseDto reissue(String refreshToken);
    CustomResponseCode logout(String accessToken);

}
