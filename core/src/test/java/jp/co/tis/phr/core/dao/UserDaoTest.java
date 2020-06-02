package jp.co.tis.phr.core.dao;

import jp.co.tis.phr.core.entity.User;
import jp.co.tis.phr.core.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
@RunWith(JUnit4.class)
public class UserDaoTest {

    @Before
    public void setup() {
        new UserDao().deleteAll();
    }

    @After
    public void cleanup() {
        new UserDao().deleteAll();
    }

    @Test
    public void testSave() {

        // 新規登録
        User user = new User();
        user.setUserId("uzresk");
        user.setEmail("uzresk@uzresk.com");
        user.setRegistrationDate(DateUtils.nowUTC());

        UserDao userDao = new UserDao();
        userDao.save(user);

        User findUser = userDao.findById("uzresk");
        Assert.assertEquals(findUser, user);

        // 更新
        user.setUserId("uzresk");
        user.setEmail("uzresk-change@uzresk.com");
        user.setRegistrationDate(DateUtils.nowUTC());
        userDao.save(user);

        findUser = userDao.findById("uzresk");
        Assert.assertEquals(findUser, user);
    }

    @Test
    public void testFindByEmail() {

        UserDao userDao = new UserDao();

        User user1 = new User();
        user1.setUserId("uzresk");
        user1.setEmail("uzresk@uzresk.com");
        user1.setRegistrationDate(DateUtils.nowUTC());
        userDao.save(user1);

        User user2 = new User();
        user2.setUserId("uzresk2");
        user2.setEmail("uzresk2@uzresk.com");
        user2.setRegistrationDate(DateUtils.nowUTC());
        userDao.save(user2);

        // パーティションキーを指定した検索
        List<User> results = userDao.findByEmail("uzresk", "uzresk@uzresk.com");
        results.forEach(System.out::println);

        Assert.assertEquals(1, results.size());
        Assert.assertEquals(results.get(0), user1);

        // クロスパーティションでの検索
        results = userDao.findByEmail("uzresk2@uzresk.com");
        results.forEach(System.out::println);
        Assert.assertEquals(1, results.size());
        Assert.assertEquals(results.get(0), user2);
    }

    @Test
    public void testDelete() {

        UserDao userDao = new UserDao();

        User user = new User();
        user.setUserId("uzresk");
        user.setEmail("uzresk@uzresk.com");
        userDao.save(user);

        userDao.delete("uzresk");

        Assert.assertEquals(0, userDao.findAll().size());
    }
}

