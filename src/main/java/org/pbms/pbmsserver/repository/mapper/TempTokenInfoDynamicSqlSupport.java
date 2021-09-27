package org.pbms.pbmsserver.repository.mapper;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class TempTokenInfoDynamicSqlSupport {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final TempTokenInfo tempTokenInfo = new TempTokenInfo();

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Long> id = tempTokenInfo.id;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> secretKey = tempTokenInfo.secretKey;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Integer> uploadTimes = tempTokenInfo.uploadTimes;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Date> expireTime = tempTokenInfo.expireTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Long> userId = tempTokenInfo.userId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> note = tempTokenInfo.note;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final class TempTokenInfo extends SqlTable {
        public final SqlColumn<Long> id = column("id", JDBCType.BIGINT);

        public final SqlColumn<String> secretKey = column("secret_key", JDBCType.VARCHAR);

        public final SqlColumn<Integer> uploadTimes = column("upload_times", JDBCType.INTEGER);

        public final SqlColumn<Date> expireTime = column("expire_time", JDBCType.TIMESTAMP);

        public final SqlColumn<Long> userId = column("user_id", JDBCType.BIGINT);

        public final SqlColumn<String> note = column("note", JDBCType.VARCHAR);

        public TempTokenInfo() {
            super("temp_token_info");
        }
    }
}