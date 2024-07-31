package kea.enter.enterbe.api.auth.service;

import kea.enter.enterbe.api.auth.dto.LoginRequestDto;

public interface AuthService {
    public String login(LoginRequestDto loginRequestDto);

}
