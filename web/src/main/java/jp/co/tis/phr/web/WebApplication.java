package jp.co.tis.phr.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "jp.co.tis.phr")
public class WebApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

    @Bean
    public ServletContextInitializer servletContextInitializer(@Value("${secure.cookie}") boolean secure) {
        return servletContext -> {
            servletContext.getSessionCookieConfig().setSecure(secure);
        };
    }

}
