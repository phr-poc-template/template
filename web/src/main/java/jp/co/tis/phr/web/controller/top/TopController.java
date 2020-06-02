package jp.co.tis.phr.web.controller.top;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TopController {

    @GetMapping(path = "/")
    public String index() {
        return "forward:/top";
    }

    @GetMapping(path = "/top")
    public String top() {
        return "top/top";
    }
}
