package kea.enter.enterbe.api.auth.service;

import kea.enter.enterbe.api.auth.dto.LoginRequestDto;
import kea.enter.enterbe.api.auth.dto.TokenDao;

public interface AuthService {
    TokenDao login(LoginRequestDto loginRequestDto);

}
