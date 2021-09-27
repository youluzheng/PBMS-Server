package org.pbms.pbmsserver.repository.mapper;

import static org.mybatis.dynamic.sql.SqlBuilder.*;
import static org.pbms.pbmsserver.repository.mapper.UserSettingsDynamicSqlSupport.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import javax.annotation.Generated;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.dynamic.sql.BasicColumn;
import org.mybatis.dynamic.sql.delete.DeleteDSLCompleter;
import org.mybatis.dynamic.sql.delete.render.DeleteStatementProvider;
import org.mybatis.dynamic.sql.insert.render.InsertStatementProvider;
import org.mybatis.dynamic.sql.insert.render.MultiRowInsertStatementProvider;
import org.mybatis.dynamic.sql.select.CountDSLCompleter;
import org.mybatis.dynamic.sql.select.SelectDSLCompleter;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.update.UpdateDSL;
import org.mybatis.dynamic.sql.update.UpdateDSLCompleter;
import org.mybatis.dynamic.sql.update.UpdateModel;
import org.mybatis.dynamic.sql.update.render.UpdateStatementProvider;
import org.mybatis.dynamic.sql.util.SqlProviderAdapter;
import org.mybatis.dynamic.sql.util.mybatis3.MyBatis3Utils;
import org.pbms.pbmsserver.repository.model.UserSettings;

@Mapper
public interface UserSettingsMapper {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    BasicColumn[] selectList = BasicColumn.columnList(userId, watermarkLogoEnable, watermarkLogoRepeat, watermarkLogoGradient, watermarkLogoAlpha, watermarkTextEnable, watermarkTextContent, watermarkTextAlpha, compressScale, responseReturnType);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    int insert(InsertStatementProvider<UserSettings> insertStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @InsertProvider(type=SqlProviderAdapter.class, method="insertMultiple")
    int insertMultiple(MultiRowInsertStatementProvider<UserSettings> multipleInsertStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("UserSettingsResult")
    Optional<UserSettings> selectOne(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="UserSettingsResult", value = {
        @Result(column="user_id", property="userId", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="watermark_logo_enable", property="watermarkLogoEnable", jdbcType=JdbcType.BIT),
        @Result(column="watermark_logo_repeat", property="watermarkLogoRepeat", jdbcType=JdbcType.BIT),
        @Result(column="watermark_logo_gradient", property="watermarkLogoGradient", jdbcType=JdbcType.TINYINT),
        @Result(column="watermark_logo_alpha", property="watermarkLogoAlpha", jdbcType=JdbcType.TINYINT),
        @Result(column="watermark_text_enable", property="watermarkTextEnable", jdbcType=JdbcType.BIT),
        @Result(column="watermark_text_content", property="watermarkTextContent", jdbcType=JdbcType.VARCHAR),
        @Result(column="watermark_text_alpha", property="watermarkTextAlpha", jdbcType=JdbcType.TINYINT),
        @Result(column="compress_scale", property="compressScale", jdbcType=JdbcType.TINYINT),
        @Result(column="response_return_type", property="responseReturnType", jdbcType=JdbcType.VARCHAR)
    })
    List<UserSettings> selectMany(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, userSettings, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, userSettings, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int deleteByPrimaryKey(Long userId_) {
        return delete(c -> 
            c.where(userId, isEqualTo(userId_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insert(UserSettings record) {
        return MyBatis3Utils.insert(this::insert, record, userSettings, c ->
            c.map(userId).toProperty("userId")
            .map(watermarkLogoEnable).toProperty("watermarkLogoEnable")
            .map(watermarkLogoRepeat).toProperty("watermarkLogoRepeat")
            .map(watermarkLogoGradient).toProperty("watermarkLogoGradient")
            .map(watermarkLogoAlpha).toProperty("watermarkLogoAlpha")
            .map(watermarkTextEnable).toProperty("watermarkTextEnable")
            .map(watermarkTextContent).toProperty("watermarkTextContent")
            .map(watermarkTextAlpha).toProperty("watermarkTextAlpha")
            .map(compressScale).toProperty("compressScale")
            .map(responseReturnType).toProperty("responseReturnType")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertMultiple(Collection<UserSettings> records) {
        return MyBatis3Utils.insertMultiple(this::insertMultiple, records, userSettings, c ->
            c.map(userId).toProperty("userId")
            .map(watermarkLogoEnable).toProperty("watermarkLogoEnable")
            .map(watermarkLogoRepeat).toProperty("watermarkLogoRepeat")
            .map(watermarkLogoGradient).toProperty("watermarkLogoGradient")
            .map(watermarkLogoAlpha).toProperty("watermarkLogoAlpha")
            .map(watermarkTextEnable).toProperty("watermarkTextEnable")
            .map(watermarkTextContent).toProperty("watermarkTextContent")
            .map(watermarkTextAlpha).toProperty("watermarkTextAlpha")
            .map(compressScale).toProperty("compressScale")
            .map(responseReturnType).toProperty("responseReturnType")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertSelective(UserSettings record) {
        return MyBatis3Utils.insert(this::insert, record, userSettings, c ->
            c.map(userId).toPropertyWhenPresent("userId", record::getUserId)
            .map(watermarkLogoEnable).toPropertyWhenPresent("watermarkLogoEnable", record::getWatermarkLogoEnable)
            .map(watermarkLogoRepeat).toPropertyWhenPresent("watermarkLogoRepeat", record::getWatermarkLogoRepeat)
            .map(watermarkLogoGradient).toPropertyWhenPresent("watermarkLogoGradient", record::getWatermarkLogoGradient)
            .map(watermarkLogoAlpha).toPropertyWhenPresent("watermarkLogoAlpha", record::getWatermarkLogoAlpha)
            .map(watermarkTextEnable).toPropertyWhenPresent("watermarkTextEnable", record::getWatermarkTextEnable)
            .map(watermarkTextContent).toPropertyWhenPresent("watermarkTextContent", record::getWatermarkTextContent)
            .map(watermarkTextAlpha).toPropertyWhenPresent("watermarkTextAlpha", record::getWatermarkTextAlpha)
            .map(compressScale).toPropertyWhenPresent("compressScale", record::getCompressScale)
            .map(responseReturnType).toPropertyWhenPresent("responseReturnType", record::getResponseReturnType)
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<UserSettings> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, userSettings, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<UserSettings> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, userSettings, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<UserSettings> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, userSettings, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<UserSettings> selectByPrimaryKey(Long userId_) {
        return selectOne(c ->
            c.where(userId, isEqualTo(userId_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, userSettings, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateAllColumns(UserSettings record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(userId).equalTo(record::getUserId)
                .set(watermarkLogoEnable).equalTo(record::getWatermarkLogoEnable)
                .set(watermarkLogoRepeat).equalTo(record::getWatermarkLogoRepeat)
                .set(watermarkLogoGradient).equalTo(record::getWatermarkLogoGradient)
                .set(watermarkLogoAlpha).equalTo(record::getWatermarkLogoAlpha)
                .set(watermarkTextEnable).equalTo(record::getWatermarkTextEnable)
                .set(watermarkTextContent).equalTo(record::getWatermarkTextContent)
                .set(watermarkTextAlpha).equalTo(record::getWatermarkTextAlpha)
                .set(compressScale).equalTo(record::getCompressScale)
                .set(responseReturnType).equalTo(record::getResponseReturnType);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(UserSettings record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(userId).equalToWhenPresent(record::getUserId)
                .set(watermarkLogoEnable).equalToWhenPresent(record::getWatermarkLogoEnable)
                .set(watermarkLogoRepeat).equalToWhenPresent(record::getWatermarkLogoRepeat)
                .set(watermarkLogoGradient).equalToWhenPresent(record::getWatermarkLogoGradient)
                .set(watermarkLogoAlpha).equalToWhenPresent(record::getWatermarkLogoAlpha)
                .set(watermarkTextEnable).equalToWhenPresent(record::getWatermarkTextEnable)
                .set(watermarkTextContent).equalToWhenPresent(record::getWatermarkTextContent)
                .set(watermarkTextAlpha).equalToWhenPresent(record::getWatermarkTextAlpha)
                .set(compressScale).equalToWhenPresent(record::getCompressScale)
                .set(responseReturnType).equalToWhenPresent(record::getResponseReturnType);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKey(UserSettings record) {
        return update(c ->
            c.set(watermarkLogoEnable).equalTo(record::getWatermarkLogoEnable)
            .set(watermarkLogoRepeat).equalTo(record::getWatermarkLogoRepeat)
            .set(watermarkLogoGradient).equalTo(record::getWatermarkLogoGradient)
            .set(watermarkLogoAlpha).equalTo(record::getWatermarkLogoAlpha)
            .set(watermarkTextEnable).equalTo(record::getWatermarkTextEnable)
            .set(watermarkTextContent).equalTo(record::getWatermarkTextContent)
            .set(watermarkTextAlpha).equalTo(record::getWatermarkTextAlpha)
            .set(compressScale).equalTo(record::getCompressScale)
            .set(responseReturnType).equalTo(record::getResponseReturnType)
            .where(userId, isEqualTo(record::getUserId))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKeySelective(UserSettings record) {
        return update(c ->
            c.set(watermarkLogoEnable).equalToWhenPresent(record::getWatermarkLogoEnable)
            .set(watermarkLogoRepeat).equalToWhenPresent(record::getWatermarkLogoRepeat)
            .set(watermarkLogoGradient).equalToWhenPresent(record::getWatermarkLogoGradient)
            .set(watermarkLogoAlpha).equalToWhenPresent(record::getWatermarkLogoAlpha)
            .set(watermarkTextEnable).equalToWhenPresent(record::getWatermarkTextEnable)
            .set(watermarkTextContent).equalToWhenPresent(record::getWatermarkTextContent)
            .set(watermarkTextAlpha).equalToWhenPresent(record::getWatermarkTextAlpha)
            .set(compressScale).equalToWhenPresent(record::getCompressScale)
            .set(responseReturnType).equalToWhenPresent(record::getResponseReturnType)
            .where(userId, isEqualTo(record::getUserId))
        );
    }
}