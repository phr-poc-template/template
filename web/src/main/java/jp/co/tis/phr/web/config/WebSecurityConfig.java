package jp.co.tis.phr.web.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/error", "/favicon.ico", "/css/**", "/js/**", "/images/**", "/fonts/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/loginForm").permitAll()
                .antMatchers("/signup/**").permitAll()
                .antMatchers("/admin/health").access(getIpAddressesFilter())
                .anyRequest().authenticated();  // それ以外は全て認証無しの場合アクセス不許可
        http.formLogin()
                .loginProcessingUrl("/login")
                .loginPage("/loginForm")
                .failureUrl("/loginForm?error")
                .defaultSuccessUrl("/top", false)
                .usernameParameter("userId")
                .passwordParameter("password");
        http.logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/loginForm");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager customAuthenticationManager() throws Exception {
        // AuthenticationManagerをDIして利用するのに必要
        return authenticationManager();
    }

    private String getIpAddressesFilter() {
        String ipAddresses = StringUtils.trimToNull(System.getenv().get("IP_ADDRESSES_FILTER"));
        log.info("IP ADDRESSES FOR FILTERING: " + ipAddresses);
        if (StringUtils.isEmpty(ipAddresses)) {
            return "hasIpAddress('127.0.0.1')";
        }
        return Arrays.asList(ipAddresses.split(","))
                .stream()
                .map(s -> "hasIpAddress('" + s + "')")
                .collect(Collectors.joining(" or "));
    }

}
