package jp.co.tis.phr.web.controller.config.password;

import jp.co.tis.phr.core.dao.UserDao;
import jp.co.tis.phr.core.entity.User;
import jp.co.tis.phr.core.utils.DateUtils;
import jp.co.tis.phr.web.controller.login.LoginUserDetails;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Slf4j
@Controller
@SessionAttributes(names = {"userRecord"})
@AllArgsConstructor
public class PasswordController {

    private final UserDao userDao;

    @GetMapping("/password")
    public String index(@AuthenticationPrincipal LoginUserDetails loginUserDetails,
                        PasswordForm form,
                        Model model) {
        form.setUserId(loginUserDetails.getUser().getUserId());
        model.addAttribute("passwordForm", form);
        return "password/index";
    }

    @PostMapping("/password/edit")
    public String edit(@AuthenticationPrincipal LoginUserDetails loginUserDetails,
                       @Validated PasswordForm form,
                       BindingResult result,
                       Model model) {
        if (result.hasErrors()) {
            return index(loginUserDetails, form, model);
        }

        User user = userDao.findById(loginUserDetails.getUser().getUserId());
        user.setPassword(new BCryptPasswordEncoder().encode(form.getNewPassword()));
        user.setRegistrationDate(DateUtils.nowUTC());
        userDao.save(user);

        return "redirect:/top";
    }
}
