package jp.co.tis.phr.web.controller.config.password;

import jp.co.tis.phr.web.controller.config.password.validate.ValidPassword;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ValidPassword(
        userIdPropertyName = "userId",
        oldPasswordPropertyName = "oldPassword",
        newPasswordPropertyName = "newPassword",
        newConfirmPasswordPropertyName = "newConfirmPassword")
public class PasswordForm {
    @NotNull
    private String userId;

    @NotNull
    private String oldPassword;

    @NotNull
    private String newPassword;

    @NotNull
    private String newConfirmPassword;
}
