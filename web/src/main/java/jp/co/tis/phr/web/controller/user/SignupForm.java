package jp.co.tis.phr.web.controller.user;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Date;

@Data
public class SignupForm {

    @NotEmpty
    @Size(min = 4, max = 40)
    private String userId;

    @NotEmpty
    private String email;

    @NotEmpty
    @Size(min = 1, max = 20)
    private String password;

    private String confirmPassword;
}
