package org.lin.dynamicdatasource.common;

import org.lin.dynamicdatasource.annotation.DbRouter;
import org.lin.dynamicdatasource.properties.DbRouterInfo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DbRouterInfoTransformer {

    DbRouterInfoTransformer INSTANCE = Mappers.getMapper(DbRouterInfoTransformer.class);

    default DbRouterInfo annotationTransform(DbRouter dbRouter) {
        DbRouterInfo dbRouterInfo = new DbRouterInfo();
        dbRouterInfo.setValue(dbRouter.value());
        dbRouterInfo.setIdentifyName(dbRouter.identifyName());
        dbRouterInfo.setUseDefaultDB(dbRouter.useDefaultDB());
        dbRouterInfo.setNeedDivTable(dbRouter.needDivTable());
        dbRouterInfo.setTableName(dbRouter.tableName());
        dbRouterInfo.setTableCount(dbRouter.tableCount());
        dbRouterInfo.setSplit(dbRouter.split());
        return dbRouterInfo;
    }
}
