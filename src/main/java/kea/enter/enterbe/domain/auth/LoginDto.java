package kea.enter.enterbe.domain.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {

    @NotNull(message = "not null")
    @Email
    private String email;

    @NotNull(message = "not null")
    private String password;
}
