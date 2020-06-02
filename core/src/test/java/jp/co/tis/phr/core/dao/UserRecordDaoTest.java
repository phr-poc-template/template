package jp.co.tis.phr.core.dao;

import jp.co.tis.phr.core.division.RecordType;
import jp.co.tis.phr.core.entity.Event;
import jp.co.tis.phr.core.entity.UserRecord;
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
import java.util.List;

@Slf4j
@RunWith(JUnit4.class)
public class UserRecordDaoTest {

    @Before
    public void setup() {
        new UserRecordDao().deleteAll();
    }

    @After
    public void cleanup() {
        new UserRecordDao().deleteAll();
    }

    @Test
    public void testSave() {

        // 新規登録
        UserRecord userRecord = new UserRecord();
        userRecord.setUserId("uzresk");
        Event event = new Event();
        event.setTime(DateUtils.nowUTC());
        userRecord.setEvent(event);
        userRecord.setHeight(177.7);
        userRecord.setWeight(77.7);

        UserRecordDao userRecordDao = new UserRecordDao();
        userRecordDao.save(userRecord);

        UserRecord findUserRecord = userRecordDao.findById(userRecord.getId());
        Assert.assertEquals(findUserRecord, userRecord);

        // 更新
        userRecord.setUserId("uzresk");
        event = new Event();
        event.setTime(DateUtils.nowUTC());
        userRecord.setEvent(event);
        userRecord.setHeight(188.8);
        userRecord.setWeight(88.8);
        userRecordDao.save(userRecord);

        findUserRecord = userRecordDao.findById(userRecord.getId());
        Assert.assertEquals(findUserRecord, userRecord);
    }

    @Test
    public void testFindByUserId() {

        UserRecord userRecord1 = new UserRecord();
        userRecord1.setUserId("uzresk");
        Event event1 = new Event();
        event1.setTime(DateUtils.nowUTC());
        userRecord1.setEvent(event1);
        userRecord1.setHeight(177.7);
        userRecord1.setWeight(77.7);
        UserRecordDao userRecordDao = new UserRecordDao();
        userRecordDao.save(userRecord1);

        UserRecord userRecord2 = new UserRecord();
        userRecord2.setUserId("uzresk2");
        Event event2 = new Event();
        event2.setTime(DateUtils.nowUTC());
        userRecord2.setEvent(event2);
        userRecord2.setHeight(188.8);
        userRecord2.setWeight(88.8);
        userRecordDao.save(userRecord2);

        UserRecord userRecord3 = new UserRecord();
        userRecord3.setUserId("uzresk");
        Event event3 = new Event();
        event3.setTime(DateUtils.nowUTC());
        userRecord3.setEvent(event3);
        userRecord3.setHeight(188.0);
        userRecord3.setWeight(88.0);
        userRecordDao.save(userRecord3);

        // クロスパーティションでの検索
        List<UserRecord> results = userRecordDao.findByUserId("uzresk");
        results.forEach(System.out::println);
        Assert.assertEquals(2, results.size());
        Assert.assertEquals(results.get(0), userRecord3);
        Assert.assertEquals(results.get(1), userRecord1);
    }

    @Test
    public void testFindMedicalExam() {

        UserRecord userRecord1 = new UserRecord();
        userRecord1.setUserId("uzresk");
        Event event1 = new Event();
        event1.setTime(DateUtils.nowUTC());
        event1.setRecordType(RecordType.MEDICAL_EXAM.getRecordType());
        userRecord1.setEvent(event1);
        userRecord1.setHeight(177.7);
        userRecord1.setWeight(77.7);
        UserRecordDao userRecordDao = new UserRecordDao();
        userRecordDao.save(userRecord1);

        UserRecord userRecord2 = new UserRecord();
        userRecord2.setUserId("uzresk");
        Event event2 = new Event();
        event2.setTime(DateUtils.nowUTC());
        event2.setRecordType(RecordType.MEDICAL_EXAM.getRecordType());
        userRecord2.setEvent(event2);
        userRecord2.setHeight(188.8);
        userRecord2.setWeight(88.8);
        userRecordDao.save(userRecord2);

        UserRecord userRecord3 = new UserRecord();
        userRecord3.setUserId("uzresk");
        Event event3 = new Event();
        event3.setTime(DateUtils.nowUTC());
        event3.setRecordType(RecordType.DEVICE.getRecordType());
        userRecord3.setEvent(event3);
        userRecord3.setHeight(188.0);
        userRecord3.setWeight(88.0);
        userRecordDao.save(userRecord3);

        // クロスパーティションでの検索
        List<UserRecord> results = userRecordDao.findMedicalExam("uzresk");
        results.forEach(System.out::println);
        Assert.assertEquals(2, results.size());
        Assert.assertEquals(results.get(0), userRecord2);
        Assert.assertEquals(results.get(1), userRecord1);
    }

    @Test
    public void testFindByTime() {

        UserRecord userRecord1 = new UserRecord();
        userRecord1.setUserId("uzresk");
        Event event1 = new Event();
        event1.setTime(LocalDateTime.of(2019, 6, 15, 12, 15).atZone(ZoneId.of("UTC")));
        userRecord1.setEvent(event1);
        userRecord1.setHeight(177.7);
        userRecord1.setWeight(77.7);
        UserRecordDao userRecordDao = new UserRecordDao();
        userRecordDao.save(userRecord1);

        UserRecord userRecord2 = new UserRecord();
        userRecord2.setUserId("uzresk");
        Event event2 = new Event();
        event2.setTime(LocalDateTime.of(2019, 7, 18, 1, 2).atZone(ZoneId.of("UTC")));
        userRecord2.setEvent(event2);
        userRecord2.setHeight(188.8);
        userRecord2.setWeight(88.8);
        userRecordDao.save(userRecord2);

        UserRecord userRecord3 = new UserRecord();
        userRecord3.setUserId("uzresk");
        Event event3 = new Event();
        event3.setTime(LocalDateTime.of(2019, 9, 3, 15, 12).atZone(ZoneId.of("UTC")));
        userRecord3.setEvent(event3);
        userRecord3.setHeight(188.0);
        userRecord3.setWeight(88.0);
        userRecordDao.save(userRecord3);

        // クロスパーティションでの検索
        List<UserRecord> results = userRecordDao.findByEventTime("uzresk",
                LocalDateTime.of(2019, 7, 1, 0, 0).atZone(ZoneId.of("UTC")),
                DateUtils.nowUTC());

        results.forEach(System.out::println);
    }


    @Test
    public void testDelete() {

        UserRecordDao userRecordDao = new UserRecordDao();

        UserRecord userRecord1 = new UserRecord();
        userRecord1.setUserId("uzresk-test");
        Event event = new Event();
        event.setTime(DateUtils.nowUTC());
        userRecord1.setEvent(event);
        userRecord1.setHeight(177.7);
        userRecord1.setWeight(77.7);
        userRecordDao.save(userRecord1);

        userRecordDao.delete(userRecord1.getId());

        Assert.assertEquals(0, userRecordDao.findByUserId("uzresk-test").size());
    }

    @Test
    public void testDeleteAfterTTL() {
        UserRecordDao userRecordDao = new UserRecordDao();

        UserRecord userRecord = new UserRecord();
        userRecord.setUserId("uzresk-test");
        Event event = new Event();
        event.setTime(DateUtils.nowUTC());
        userRecord.setEvent(event);
        userRecord.setHeight(177.7);
        userRecord.setWeight(77.7);
        userRecordDao.save(userRecord);

        userRecordDao.deleteAfterTTL(userRecord, 60);

        Assert.assertEquals(0, userRecordDao.findByUserId("uzresk-test").size());

    }
}

