package org.pbms.pbmsserver.repository;


import org.pbms.pbmsserver.repository.mapper.TempTokenInfoDynamicSqlSupport;
import org.pbms.pbmsserver.repository.mapper.UserInfoDynamicSqlSupport;
import org.pbms.pbmsserver.repository.mapper.UserSettingsDynamicSqlSupport;

public class Tables {
    public static final TempTokenInfoDynamicSqlSupport.TempTokenInfo tempTokenInfoTable = TempTokenInfoDynamicSqlSupport.tempTokenInfo;
    public static final UserInfoDynamicSqlSupport.UserInfo userInfoTable = UserInfoDynamicSqlSupport.userInfo;
    public static final UserSettingsDynamicSqlSupport.UserSettings userSettingsTable = UserSettingsDynamicSqlSupport.userSettings;
}
