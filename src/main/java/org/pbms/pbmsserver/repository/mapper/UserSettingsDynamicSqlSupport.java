package org.pbms.pbmsserver.repository.mapper;

import java.sql.JDBCType;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class UserSettingsDynamicSqlSupport {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final UserSettings userSettings = new UserSettings();

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Long> userId = userSettings.userId;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Boolean> watermarkLogoEnable = userSettings.watermarkLogoEnable;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Boolean> watermarkLogoRepeat = userSettings.watermarkLogoRepeat;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Byte> watermarkLogoGradient = userSettings.watermarkLogoGradient;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Byte> watermarkLogoAlpha = userSettings.watermarkLogoAlpha;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Boolean> watermarkTextEnable = userSettings.watermarkTextEnable;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> watermarkTextContent = userSettings.watermarkTextContent;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Byte> watermarkTextAlpha = userSettings.watermarkTextAlpha;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<Byte> compressScale = userSettings.compressScale;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final SqlColumn<String> responseReturnType = userSettings.responseReturnType;

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    public static final class UserSettings extends SqlTable {
        public final SqlColumn<Long> userId = column("USER_ID", JDBCType.BIGINT);

        public final SqlColumn<Boolean> watermarkLogoEnable = column("WATERMARK_LOGO_ENABLE", JDBCType.BOOLEAN);

        public final SqlColumn<Boolean> watermarkLogoRepeat = column("WATERMARK_LOGO_REPEAT", JDBCType.BOOLEAN);

        public final SqlColumn<Byte> watermarkLogoGradient = column("WATERMARK_LOGO_GRADIENT", JDBCType.TINYINT);

        public final SqlColumn<Byte> watermarkLogoAlpha = column("WATERMARK_LOGO_ALPHA", JDBCType.TINYINT);

        public final SqlColumn<Boolean> watermarkTextEnable = column("WATERMARK_TEXT_ENABLE", JDBCType.BOOLEAN);

        public final SqlColumn<String> watermarkTextContent = column("WATERMARK_TEXT_CONTENT", JDBCType.VARCHAR);

        public final SqlColumn<Byte> watermarkTextAlpha = column("WATERMARK_TEXT_ALPHA", JDBCType.TINYINT);

        public final SqlColumn<Byte> compressScale = column("COMPRESS_SCALE", JDBCType.TINYINT);

        public final SqlColumn<String> responseReturnType = column("RESPONSE_RETURN_TYPE", JDBCType.VARCHAR);

        public UserSettings() {
            super("PUBLIC.USER_SETTINGS");
        }
    }
}