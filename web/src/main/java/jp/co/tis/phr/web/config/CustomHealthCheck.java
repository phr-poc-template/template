package jp.co.tis.phr.web.config;

import jp.co.tis.phr.core.dao.UserDao;
import jp.co.tis.phr.core.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CustomHealthCheck implements HealthIndicator {

    private UserDao userDao;

    /**
     * CosmosDBにhealthcheckユーザがいる場合、HealthcheckをUPにする
     *
     * @return Health
     */
    public Health health() {

        User user = userDao.findById("healthcheck");
        if (user != null) {
            return new Health.Builder(Status.UP).build();
        } else {
            return new Health.Builder(Status.OUT_OF_SERVICE).build();
        }

    }
}
