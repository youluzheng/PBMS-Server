package org.pbms.pbmsserver.service;

import org.pbms.pbmsserver.common.exception.BusinessException;
import org.pbms.pbmsserver.common.exception.BusinessStatus;
import org.pbms.pbmsserver.dao.TempTokenInfoDao;
import org.pbms.pbmsserver.repository.model.TempTokenInfo;
import org.pbms.pbmsserver.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.mybatis.dynamic.sql.SqlBuilder.*;
import static org.pbms.pbmsserver.repository.mapper.TempTokenInfoDynamicSqlSupport.*;

/**
 * 新增临时token服务
 *
 * @author zqs
 */
@Service
public class TempTokenService {

    @Autowired
    TempTokenInfoDao tempTokenInfoDao;

    /**
     * 用户产生新的临时token，存入数据库
     *
     * @param expireTime  过期时间
     * @param uploadTimes 可上传次数
     * @param note        备注
     * @return 临时token的实体类，其中secretKey是：Id + "-" + 随机数
     */
    public TempTokenInfo addTempToken(Date expireTime, int uploadTimes, String note) {
        String randomString = UUID.randomUUID().toString().trim().replaceAll("-", "");
        TempTokenInfo tokenInfo = new TempTokenInfo();
        tokenInfo.setSecretKey(randomString);
        tokenInfo.setUploadTimes(uploadTimes);
        tokenInfo.setUserId(TokenUtil.getUserId());
        tokenInfo.setExpireTime(expireTime);
        tokenInfo.setNote(note);
        tempTokenInfoDao.insert(tokenInfo);
        return tokenInfo;
    }

    /**
     * 获取用户创建的所有临时token
     *
     * @return 临时token列表
     */
    public List<TempTokenInfo> myTokenList() {
        long myId = TokenUtil.getUserId();
        return tempTokenInfoDao.select(c -> c
                .where(userId, isEqualTo(myId))
                .orderBy(expireTime, uploadTimes.descending())
        );
    }

    /**
     * 删除用户产生的临时token
     *
     * @param tokenId tokenId
     */
    public void deleteTempToken(long tokenId) {
        tempTokenInfoDao.delete(c -> c
                .where(userId, isEqualTo(TokenUtil.getUserId()))
                .and(id, isEqualTo(tokenId))
        );
    }

    /**
     * 检查临时token是否合法
     *
     * @param tempToken tempToken
     */
    public TempTokenInfo checkTempToken(String tempToken) {
        TempTokenInfo tempTokenInfo = tempTokenInfoDao.selectOne(c -> c
                .where(secretKey, isEqualTo(tempToken))
        ).orElseThrow(() -> new BusinessException(BusinessStatus.TEMP_TOKEN_ERROR));
        Date now = new Date();
        if (now.after(tempTokenInfo.getExpireTime())) {
            throw new BusinessException(BusinessStatus.TEMP_TOKEN_EXPIRE);
        }
        return tempTokenInfo;
    }

    /**
     * 修改剩余上传图片的次数
     *
     * @param tempToken tempToken
     * @param imageSize 上传图片数量
     */
    public void updateTimes(String tempToken, int imageSize) {
        int result = tempTokenInfoDao.update(c -> c
                .set(uploadTimes).equalTo(subtract(uploadTimes, constant(String.valueOf(imageSize))))
                .where(secretKey, isEqualTo(tempToken))
                .and(uploadTimes, isGreaterThanOrEqualTo(imageSize))
        );
        if (result == 0) {
            throw new BusinessException(BusinessStatus.TEMP_TOKEN_UPLOAD_TIMES_EXCEED);
        }
    }

}
