package jp.co.tis.phr.web.controller.config.password.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = PasswordConstraintValidator.class)
@Target({ TYPE, ANNOTATION_TYPE })
@Retention(RUNTIME)
public @interface ValidPassword {
    String message() default "default error";

    Class<?>[] groups() default {};

    String userIdPropertyName();
    String oldPasswordPropertyName();
    String newPasswordPropertyName();
    String newConfirmPasswordPropertyName();

    Class<? extends Payload>[] payload() default {};
}
