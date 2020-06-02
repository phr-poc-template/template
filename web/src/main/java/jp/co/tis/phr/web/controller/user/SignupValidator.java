package jp.co.tis.phr.web.controller.user;

import jp.co.tis.phr.core.dao.UserDao;
import jp.co.tis.phr.core.entity.User;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component
@AllArgsConstructor
public class SignupValidator implements Validator {

    private final UserDao userDao;

    @Override
    public boolean supports(Class<?> clazz) {
        return SignupForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SignupForm signupForm = (SignupForm) target;

        // パスワードと確認用パスワードのチェック
        String password = signupForm.getPassword();
        String confirmPassword = signupForm.getConfirmPassword();

        if (password == null || confirmPassword == null) {
            return;
        }
        if (!password.equals(confirmPassword)) {
            errors.rejectValue("password", "signup.password-mismatch.message");
        }

        // ユーザIDの存在チェック
        String userId = signupForm.getUserId();
        if (StringUtils.isNotEmpty(userId)) {
            User member = userDao.findById(signupForm.getUserId());
            if (member != null) {
                errors.rejectValue("userId", "signup.duplicate-userid.message");
            }
        }
    }

}
