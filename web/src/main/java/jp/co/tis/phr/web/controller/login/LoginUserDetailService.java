package jp.co.tis.phr.web.controller.login;

import jp.co.tis.phr.core.dao.UserDao;
import jp.co.tis.phr.core.entity.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Slf4j
@Service
@AllArgsConstructor
public class LoginUserDetailService implements UserDetailsService {

    private final UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

        log.info("userId:" + userId);

        User user = userDao.findById(userId);
        if (user == null) {
            throw new UsernameNotFoundException(userId + " is not found.");
        }

        return new LoginUserDetails(user, getAuthorities(userId));
    }

    private Collection<GrantedAuthority> getAuthorities(String userId) {
        return AuthorityUtils.createAuthorityList("ROLE_READ", "ROLE_CREATE","ROLE_UPDATE","ROLE_DELETE");
    }
}
