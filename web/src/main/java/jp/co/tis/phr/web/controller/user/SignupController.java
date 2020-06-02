package jp.co.tis.phr.web.controller.user;

import jp.co.tis.phr.core.dao.UserDao;
import jp.co.tis.phr.core.entity.User;
import jp.co.tis.phr.core.utils.DateUtils;
import jp.co.tis.phr.web.controller.login.LoginUserDetailService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.context.MessageSource;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Locale;

@Controller
@AllArgsConstructor
public class SignupController {

    private final UserDao userDao;

    private final HttpSession session;

    private final MessageSource messageSource;

    private final SignupValidator signupValidator;

    private final AuthenticationManager authenticationManager;

    private final LoginUserDetailService loginUserDetailsService;

    @ModelAttribute
    SignupForm setUpForm() {
        return new SignupForm();
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.addCustomFormatter(new DateFormatter("yyyy-MM-dd"));
        binder.addValidators(signupValidator);
    }

    @GetMapping(path = "/signup")
    public String index() {
        return "signup/signup";
    }

    @PostMapping(path = "/signup/regist")
    public String regist(@Validated SignupForm signupForm,
                         BindingResult result,
                         RedirectAttributes attributes,
                         Locale locale) {

        if (result.hasErrors()) {
            return index();
        }

        User user = new User();
        BeanUtils.copyProperties(signupForm, user);
        user.setPassword(new BCryptPasswordEncoder().encode(signupForm.getPassword()));
        user.setRegistrationDate(DateUtils.nowUTC());
        userDao.save(user);

        UserDetails userDetails = loginUserDetailsService.loadUserByUsername(user.getUserId());

        Authentication newAuth
                = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), signupForm.getPassword(),userDetails.getAuthorities());

        // Authenticate the user
        Authentication authentication = authenticationManager.authenticate(newAuth);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        // Create a new session and add the security context.
        session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);

        attributes.addFlashAttribute("message",
                messageSource.getMessage("signup.complete.message",
                        new String[]{signupForm.getUserId()}, locale));

        return "redirect:/top";
    }
}
