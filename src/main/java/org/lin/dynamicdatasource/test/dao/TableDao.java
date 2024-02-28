package org.lin.dynamicdatasource.test.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.lin.dynamicdatasource.annotation.DbRouter;
import org.lin.dynamicdatasource.test.dto.Table;

@Mapper
public interface TableDao extends BaseMapper<Table> {

    @DbRouter(split = "")
    @Insert("insert into table1(test_col) value (#{testCol}) ")
    boolean insertTestCol(Table table);
}
