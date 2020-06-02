package jp.co.tis.phr.core.dao;

import com.microsoft.azure.documentdb.*;
import jp.co.tis.phr.core.cosmosdb.dao.BaseDao;
import jp.co.tis.phr.core.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class UserDao extends BaseDao<User> {
    public void save(User user) {
        user.setId(user.getUserId());
        PartitionKey partitionKey = new PartitionKey(user.getUserId());
        super.save(user, partitionKey);
    }

    public User findById(String userId) {
        return super.findById(userId);
    }

    /**
     * Emailアドレスで検索します。
     * PartitionKeyを指定
     *
     * @param userId UserId
     * @param email  Email
     * @return List<User>
     */
    public List<User> findByEmail(String userId, String email) {

        String query = "SELECT * FROM User WHERE User.email = @email";
        SqlQuerySpec sqlQuerySpec = new SqlQuerySpec();
        sqlQuerySpec.setQueryText(query);
        sqlQuerySpec.setParameters(new SqlParameterCollection(new SqlParameter("@email", email)));

        FeedOptions options = new FeedOptions();
        options.setPartitionKey(new PartitionKey(userId));

        return find(sqlQuerySpec, options);
    }

    /**
     * Emailアドレスで検索します。
     * Cross Partition Search
     *
     * @param email Email
     * @return List<User>
     */
    public List<User> findByEmail(String email) {

        String query = "SELECT * FROM User WHERE User.email = @email";
        SqlQuerySpec sqlQuerySpec = new SqlQuerySpec();
        sqlQuerySpec.setQueryText(query);
        sqlQuerySpec.setParameters(new SqlParameterCollection(new SqlParameter("@email", email)));

        FeedOptions options = new FeedOptions();
        options.setEnableCrossPartitionQuery(true);

        return find(sqlQuerySpec, options);
    }

    public void delete(String userId) {
        super.delete(userId);
    }

    public void deleteAll() {
        findAll().forEach(u -> delete(u.getUserId()));
    }
}
