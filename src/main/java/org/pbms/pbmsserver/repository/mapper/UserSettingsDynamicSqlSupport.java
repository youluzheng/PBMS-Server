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
        public final SqlColumn<Long> userId = column("user_id", JDBCType.BIGINT);

        public final SqlColumn<Boolean> watermarkLogoEnable = column("watermark_logo_enable", JDBCType.BIT);

        public final SqlColumn<Boolean> watermarkLogoRepeat = column("watermark_logo_repeat", JDBCType.BIT);

        public final SqlColumn<Byte> watermarkLogoGradient = column("watermark_logo_gradient", JDBCType.TINYINT);

        public final SqlColumn<Byte> watermarkLogoAlpha = column("watermark_logo_alpha", JDBCType.TINYINT);

        public final SqlColumn<Boolean> watermarkTextEnable = column("watermark_text_enable", JDBCType.BIT);

        public final SqlColumn<String> watermarkTextContent = column("watermark_text_content", JDBCType.VARCHAR);

        public final SqlColumn<Byte> watermarkTextAlpha = column("watermark_text_alpha", JDBCType.TINYINT);

        public final SqlColumn<Byte> compressScale = column("compress_scale", JDBCType.TINYINT);

        public final SqlColumn<String> responseReturnType = column("response_return_type", JDBCType.VARCHAR);

        public UserSettings() {
            super("user_settings");
        }
    }
}