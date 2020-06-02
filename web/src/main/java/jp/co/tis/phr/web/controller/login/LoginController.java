package jp.co.tis.phr.web.controller.login;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller
public class LoginController {

    @Autowired
    private HttpServletRequest req;

    @GetMapping(path = "/loginForm")
    public String loginForm(Model model) {
        return "login/login";
    }
}
