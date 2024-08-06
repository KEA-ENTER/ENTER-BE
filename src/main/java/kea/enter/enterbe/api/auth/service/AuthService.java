package kea.enter.enterbe.api.auth.service;

import kea.enter.enterbe.api.auth.dto.LoginRequestDto;
import kea.enter.enterbe.api.auth.dto.LoginResponseDto;
import kea.enter.enterbe.api.auth.dto.ReissueResponseDto;

public interface AuthService {
    LoginResponseDto login(LoginRequestDto loginRequestDto);
    ReissueResponseDto reissue(String refreshToken);

}
