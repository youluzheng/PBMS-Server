package org.pbms.pbmsserver.repository.mapper;

import static org.mybatis.dynamic.sql.SqlBuilder.*;
import static org.pbms.pbmsserver.repository.mapper.TempTokenInfoDynamicSqlSupport.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import javax.annotation.Generated;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
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
import org.pbms.pbmsserver.repository.model.TempTokenInfo;

@Mapper
public interface TempTokenInfoMapper {
    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    BasicColumn[] selectList = BasicColumn.columnList(id, secretKey, uploadTimes, expireTime, userId, note);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    @Options(useGeneratedKeys=true,keyProperty="record.id")
    int insert(InsertStatementProvider<TempTokenInfo> insertStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @Insert({
        "${insertStatement}"
    })
    @Options(useGeneratedKeys=true,keyProperty="records.id")
    int insertMultiple(@Param("insertStatement") String insertStatement, @Param("records") List<TempTokenInfo> records);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertMultiple(MultiRowInsertStatementProvider<TempTokenInfo> multipleInsertStatement) {
        return insertMultiple(multipleInsertStatement.getInsertStatement(), multipleInsertStatement.getRecords());
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("TempTokenInfoResult")
    Optional<TempTokenInfo> selectOne(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="TempTokenInfoResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="secret_key", property="secretKey", jdbcType=JdbcType.VARCHAR),
        @Result(column="upload_times", property="uploadTimes", jdbcType=JdbcType.INTEGER),
        @Result(column="expire_time", property="expireTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="user_id", property="userId", jdbcType=JdbcType.BIGINT),
        @Result(column="note", property="note", jdbcType=JdbcType.VARCHAR)
    })
    List<TempTokenInfo> selectMany(SelectStatementProvider selectStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, tempTokenInfo, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, tempTokenInfo, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int deleteByPrimaryKey(Long id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insert(TempTokenInfo record) {
        return MyBatis3Utils.insert(this::insert, record, tempTokenInfo, c ->
            c.map(secretKey).toProperty("secretKey")
            .map(uploadTimes).toProperty("uploadTimes")
            .map(expireTime).toProperty("expireTime")
            .map(userId).toProperty("userId")
            .map(note).toProperty("note")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertMultiple(Collection<TempTokenInfo> records) {
        return MyBatis3Utils.insertMultiple(this::insertMultiple, records, tempTokenInfo, c ->
            c.map(secretKey).toProperty("secretKey")
            .map(uploadTimes).toProperty("uploadTimes")
            .map(expireTime).toProperty("expireTime")
            .map(userId).toProperty("userId")
            .map(note).toProperty("note")
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int insertSelective(TempTokenInfo record) {
        return MyBatis3Utils.insert(this::insert, record, tempTokenInfo, c ->
            c.map(secretKey).toPropertyWhenPresent("secretKey", record::getSecretKey)
            .map(uploadTimes).toPropertyWhenPresent("uploadTimes", record::getUploadTimes)
            .map(expireTime).toPropertyWhenPresent("expireTime", record::getExpireTime)
            .map(userId).toPropertyWhenPresent("userId", record::getUserId)
            .map(note).toPropertyWhenPresent("note", record::getNote)
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<TempTokenInfo> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, tempTokenInfo, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<TempTokenInfo> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, tempTokenInfo, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default List<TempTokenInfo> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, tempTokenInfo, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default Optional<TempTokenInfo> selectByPrimaryKey(Long id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, tempTokenInfo, completer);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateAllColumns(TempTokenInfo record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(secretKey).equalTo(record::getSecretKey)
                .set(uploadTimes).equalTo(record::getUploadTimes)
                .set(expireTime).equalTo(record::getExpireTime)
                .set(userId).equalTo(record::getUserId)
                .set(note).equalTo(record::getNote);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    static UpdateDSL<UpdateModel> updateSelectiveColumns(TempTokenInfo record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(secretKey).equalToWhenPresent(record::getSecretKey)
                .set(uploadTimes).equalToWhenPresent(record::getUploadTimes)
                .set(expireTime).equalToWhenPresent(record::getExpireTime)
                .set(userId).equalToWhenPresent(record::getUserId)
                .set(note).equalToWhenPresent(record::getNote);
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKey(TempTokenInfo record) {
        return update(c ->
            c.set(secretKey).equalTo(record::getSecretKey)
            .set(uploadTimes).equalTo(record::getUploadTimes)
            .set(expireTime).equalTo(record::getExpireTime)
            .set(userId).equalTo(record::getUserId)
            .set(note).equalTo(record::getNote)
            .where(id, isEqualTo(record::getId))
        );
    }

    @Generated("org.mybatis.generator.api.MyBatisGenerator")
    default int updateByPrimaryKeySelective(TempTokenInfo record) {
        return update(c ->
            c.set(secretKey).equalToWhenPresent(record::getSecretKey)
            .set(uploadTimes).equalToWhenPresent(record::getUploadTimes)
            .set(expireTime).equalToWhenPresent(record::getExpireTime)
            .set(userId).equalToWhenPresent(record::getUserId)
            .set(note).equalToWhenPresent(record::getNote)
            .where(id, isEqualTo(record::getId))
        );
    }
}