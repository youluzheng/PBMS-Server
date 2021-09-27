package org.pbms.pbmsserver.dao;

import org.junit.jupiter.api.Test;
import org.pbms.pbmsserver.repository.model.TempTokenInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TempTokenInfoDaoTest {

    @Autowired
    TempTokenInfoDao tempTokenInfoDao;

    @Test
    void test_id_not_null() {
        TempTokenInfo tempTokenInfo = new TempTokenInfo();
        tempTokenInfo.setExpireTime(new Date());
        tempTokenInfo.setUploadTimes(10);
        tempTokenInfo.setNote("sdkjadl");
        tempTokenInfo.setSecretKey("dsadsa");
        tempTokenInfo.setUserId(1L);
        tempTokenInfoDao.insert(tempTokenInfo);
        assertNotNull(tempTokenInfo.getId());
    }
}