package jp.co.tis.phr.web.controller.healthdata;

import jp.co.tis.phr.web.controller.login.LoginUserDetails;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Slf4j
@Controller
@SessionAttributes(names = {"userRecord"})
@AllArgsConstructor
public class HealthDataViewController {

    @GetMapping("/health-data/list")
    public String list(@AuthenticationPrincipal LoginUserDetails loginUserDetails, Model model) {
        return "health-data/health-data";
    }


}
