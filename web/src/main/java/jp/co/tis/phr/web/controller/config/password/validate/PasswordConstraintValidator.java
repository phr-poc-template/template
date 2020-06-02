package jp.co.tis.phr.web.controller.config.password.validate;

import jp.co.tis.phr.core.dao.UserDao;
import org.apache.commons.lang3.StringUtils;
import org.passay.*;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, Object> {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDao userDao;

    @Autowired
    private MessageSource messageSource;

    private String userIdPropertyName;
    private String oldPasswordPropertyName;
    private String newPasswordPropertyName;
    private String newConfirmPasswordPropertyName;

    @Override
    public void initialize(ValidPassword annotation) {
        userIdPropertyName = annotation.userIdPropertyName();
        oldPasswordPropertyName = annotation.oldPasswordPropertyName();
        newConfirmPasswordPropertyName = annotation.newConfirmPasswordPropertyName();
        newPasswordPropertyName = annotation.newPasswordPropertyName();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        CharacterCharacteristicsRule characterCharacteristicsRule = new CharacterCharacteristicsRule();
        characterCharacteristicsRule.setNumberOfCharacteristics(3);
        characterCharacteristicsRule.getRules().add(new CharacterRule(EnglishCharacterData.UpperCase, 1));
        characterCharacteristicsRule.getRules().add(new CharacterRule(EnglishCharacterData.LowerCase, 1));
        characterCharacteristicsRule.getRules().add(new CharacterRule(EnglishCharacterData.Digit, 1));
        characterCharacteristicsRule.getRules().add(new CharacterRule(EnglishCharacterData.Special, 1));

//        PasswordValidator validator = new PasswordValidator(Arrays.asList(
//                new LengthRule(8, 100),
//                characterCharacteristicsRule,
//                new WhitespaceRule()));
        PasswordValidator validator = new PasswordValidator(Arrays.asList(new LengthRule(1, 20)));

        BeanWrapper beanWrapper = new BeanWrapperImpl(value);
        String newPassword = (String) beanWrapper.getPropertyValue(newPasswordPropertyName);
        String newConfirmPassword = (String) beanWrapper.getPropertyValue(newConfirmPasswordPropertyName);
        String oldPassword = (String) beanWrapper.getPropertyValue(oldPasswordPropertyName);
        String userId = (String) beanWrapper.getPropertyValue(userIdPropertyName);

        RuleResult result = validator.validate(new PasswordData(newPassword));


        // パスワードポリシーに則っている、かつ新しいパスワードと確認用パスワードが一致している、かつ現在のパスワードが一致している
        String encodedPassword = userDao.findById(userId).getPassword();
        if (result.isValid() &&
                StringUtils.equals(newPassword, newConfirmPassword) &&
                passwordEncoder.matches(oldPassword, encodedPassword)) {
            return true;
        }

        if (!result.isValid()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    messageSource.getMessage("valid-password.invalid-policy.message", null, LocaleContextHolder.getLocale()))
                    .addPropertyNode(newPasswordPropertyName)
                    .addConstraintViolation();
        }
        if (!StringUtils.equals(newPassword, newConfirmPassword)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    messageSource.getMessage("valid-password.invalid-confirmation.message", null, LocaleContextHolder.getLocale()))
                    .addPropertyNode(newConfirmPasswordPropertyName)
                    .addConstraintViolation();
        }
        if (!passwordEncoder.matches(oldPassword, encodedPassword)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    messageSource.getMessage("valid-password.invalid-old.message", null, LocaleContextHolder.getLocale()))
                    .addPropertyNode(oldPasswordPropertyName)
                    .addConstraintViolation();
        }
        return false;
    }
}
