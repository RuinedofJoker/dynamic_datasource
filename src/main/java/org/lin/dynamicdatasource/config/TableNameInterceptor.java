package org.lin.dynamicdatasource.config;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.lin.dynamicdatasource.common.DataSourceThreadContext;
import org.lin.dynamicdatasource.properties.DbRouterInfo;
import org.lin.dynamicdatasource.sql.visitor.SelectTableRenameVisitor;
import org.lin.dynamicdatasource.utils.ExecutorPluginUtils;

import java.util.Properties;


@Intercepts({
        @Signature( type = Executor.class, method = "update",args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query",args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query",args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class,BoundSql.class})
})
public class TableNameInterceptor implements Interceptor {

    private Properties properties;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        DbRouterInfo dataSourceInfo = DataSourceThreadContext.getDataSourceInfo();
        if (dataSourceInfo == null || !dataSourceInfo.getNeedDivTable()) {
            return invocation.proceed();
        }
        // 获取拦截方法的参数
        Object[] args = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) args[0];
        Configuration configuration = mappedStatement.getConfiguration();

        Object parameter = null;
        if (invocation.getArgs().length > 1) {
            parameter = invocation.getArgs()[1];
        }
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        // 获取sql语句
        String sql = boundSql.getSql();
        String oldTableName = dataSourceInfo.getTableName();
        String newTableName = dataSourceInfo.getTableName() + dataSourceInfo.getSplit() + dataSourceInfo.getTableIdx();
        String newSql = sql;
        switch (mappedStatement.getSqlCommandType()) {
            case SELECT:
                SelectTableRenameVisitor visitor = new SelectTableRenameVisitor(oldTableName, newTableName);
                newSql = visitor.changeSql(sql);
                break;
            case INSERT:
                // todo
                break;
            case UPDATE:
                // todo
                break;
            case DELETE:
                // todo
                break;
        }

        ExecutorPluginUtils.resetSql2Invocation(invocation, newSql);
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            //调用插件
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
