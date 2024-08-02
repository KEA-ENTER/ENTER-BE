package kea.enter.enterbe.api.auth.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginRequestDto {
    @NotBlank
    @Email
    private final String email;

    @NotBlank
    private final String password;

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
