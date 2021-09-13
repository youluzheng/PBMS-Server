package org.pbms.pbmsserver.repository.mapper;

import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class UserInfoDynamicSqlSupport {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final UserInfo userInfo = new UserInfo();

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Long> userId = userInfo.userId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> userName = userInfo.userName;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> password = userInfo.password;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> email = userInfo.email;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Byte> status = userInfo.status;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Date> createTime = userInfo.createTime;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final class UserInfo extends SqlTable {
        public final SqlColumn<Long> userId = column("USER_ID", JDBCType.BIGINT);

        public final SqlColumn<String> userName = column("USER_NAME", JDBCType.VARCHAR);

        public final SqlColumn<String> password = column("PASSWORD", JDBCType.VARCHAR);

        public final SqlColumn<String> email = column("EMAIL", JDBCType.VARCHAR);

        public final SqlColumn<Byte> status = column("STATUS", JDBCType.TINYINT);

        public final SqlColumn<Date> createTime = column("CREATE_TIME", JDBCType.TIMESTAMP);

        public UserInfo() {
            super("PUBLIC.USER_INFO");
        }
    }
}