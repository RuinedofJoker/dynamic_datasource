package org.lin.dynamicdatasource.common;

import org.lin.dynamicdatasource.properties.DbRouterInfo;

public class DataSourceThreadContext {

    private static final ThreadLocal<String> dataSourceKeyThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<DbRouterInfo> dataSourceInfoThreadLocal = new ThreadLocal<>();

    public static void setDataSourceKey(String dataSourceKey) {
        dataSourceKeyThreadLocal.set(dataSourceKey);
    }

    public static String getDataSourceKey() {
        return dataSourceKeyThreadLocal.get();
    }

    public static void clearDataSourceKey() {
        dataSourceKeyThreadLocal.remove();
    }

    public static void setDataSourceInfo(DbRouterInfo dbRouterInfo) {
        dataSourceInfoThreadLocal.set(dbRouterInfo);
    }

    public static DbRouterInfo getDataSourceInfo() {
        return dataSourceInfoThreadLocal.get();
    }

    public static void clearDataSourceInfo() {
        dataSourceInfoThreadLocal.remove();
    }
}
