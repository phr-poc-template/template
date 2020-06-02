package jp.co.tis.phr.web.controller.login;

import jp.co.tis.phr.core.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class LoginUserDetails extends org.springframework.security.core.userdetails.User {

    @Getter
    private final User user;

    LoginUserDetails(User user, Collection<GrantedAuthority> grantedAuthorities) {

        super(user.getUserId(), user.getPassword(), grantedAuthorities);
        this.user = user;
    }
}
