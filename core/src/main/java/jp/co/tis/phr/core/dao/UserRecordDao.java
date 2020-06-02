package jp.co.tis.phr.core.dao;

import com.microsoft.azure.documentdb.*;
import jp.co.tis.phr.core.cosmosdb.dao.BaseDao;
import jp.co.tis.phr.core.division.RecordType;
import jp.co.tis.phr.core.entity.UserRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Component
public class UserRecordDao extends BaseDao<UserRecord> {

    public void save(UserRecord userRecord) {
        userRecord.setId(userRecord.getId());
        PartitionKey partitionKey = new PartitionKey(userRecord.getId());
        super.save(userRecord, partitionKey);
    }

    public UserRecord findById(String id) {
        return super.findById(id);
    }

    public List<UserRecord> findByUserId(String userId) {

        // event_timeの降順でレコードを取得する
        String query = "SELECT * FROM user_record as r "
                + "WHERE r.user_id = @user_id AND r.deleted = false "
                + "ORDER BY r.event.time DESC";
        SqlQuerySpec sqlQuerySpec = new SqlQuerySpec();
        sqlQuerySpec.setQueryText(query);
        sqlQuerySpec.setParameters(new SqlParameterCollection(new SqlParameter("@user_id", userId)));

        FeedOptions options = new FeedOptions();
        options.setEnableCrossPartitionQuery(true);

        return find(sqlQuerySpec, options);
    }

    /**
     * 健康診断データを取得する
     *
     * @param userId ユーザID
     * @return ユーザIDに紐づく健康診断データ
     */
    public List<UserRecord> findMedicalExam(String userId) {

        // timeの降順でレコードを取得する
        String query = "SELECT * FROM user_record as r "
                + "WHERE r.user_id = @user_id AND r.event.record_type = @record_type AND r.deleted = false "
                + "ORDER BY r.event.time DESC";
        SqlQuerySpec sqlQuerySpec = new SqlQuerySpec();
        sqlQuerySpec.setQueryText(query);
        SqlParameterCollection params = new SqlParameterCollection();
        params.add(new SqlParameter("@user_id", userId));
        params.add(new SqlParameter("@record_type", RecordType.MEDICAL_EXAM.getRecordType()));
        sqlQuerySpec.setParameters(params);

        FeedOptions options = new FeedOptions();
        options.setEnableCrossPartitionQuery(true);

        return find(sqlQuerySpec, options);
    }

    public List<UserRecord> findByEventTime(String userId, ZonedDateTime from, ZonedDateTime to) {
        String query = "SELECT * FROM user_record as r "
                + "WHERE r.user_id = @user_id AND "
                + "r.deleted = false AND r.event.time >= @from AND r.event.time <= @to";
        SqlQuerySpec sqlQuerySpec = new SqlQuerySpec();
        sqlQuerySpec.setQueryText(query);
        SqlParameterCollection params = new SqlParameterCollection();
        params.add(new SqlParameter("@user_id", userId));
        params.add(new SqlParameter("@from", ZonedDateTime.ofInstant(from.toInstant(), ZoneId.of("UTC")).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)));
        params.add(new SqlParameter("@to", ZonedDateTime.ofInstant(to.toInstant(), ZoneId.of("UTC")).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)));
        sqlQuerySpec.setParameters(params);

        FeedOptions options = new FeedOptions();
        options.setEnableCrossPartitionQuery(true);

        return find(sqlQuerySpec, options);
    }

    public void deleteAfterTTL(UserRecord userRecord, int ttl) {
        userRecord.setDeleted(true);
        userRecord.setTtl(ttl);
        PartitionKey partitionKey = new PartitionKey(userRecord.getId());
        super.save(userRecord, partitionKey);
    }

    public void delete(String id) {
        super.delete(id);
    }

    public void deleteAll() {
        findAll().forEach(u -> delete(u.getId()));
    }
}
