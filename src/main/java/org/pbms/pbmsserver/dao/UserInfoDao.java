package org.pbms.pbmsserver.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.util.SqlProviderAdapter;
import org.pbms.pbmsserver.common.response.user.UserListVO;
import org.pbms.pbmsserver.repository.mapper.UserInfoMapper;

import java.util.List;

@Mapper
public interface UserInfoDao extends UserInfoMapper {

    @SelectProvider(type = SqlProviderAdapter.class, method = "select")
    List<UserListVO> selectUserList(SelectStatementProvider selectStatementProvider);
}
