package kea.enter.enterbe.api.auth.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginRequestDto {
    @NotNull
    @Email
    private String email;

    @NotNull
    private String password;

    @Builder
    public LoginRequestDto(
        String email, String password
    ) {
        this.email = email;
        this.password = password;
    }

    public static LoginRequestDto of(String email, String password) {
        return LoginRequestDto.builder().email(email).password(password).build();
    }
}
