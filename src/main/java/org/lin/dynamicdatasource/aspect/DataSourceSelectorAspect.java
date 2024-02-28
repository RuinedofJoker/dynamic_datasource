package org.lin.dynamicdatasource.aspect;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.lin.dynamicdatasource.annotation.DbRouter;
import org.lin.dynamicdatasource.common.DataSourceThreadContext;
import org.lin.dynamicdatasource.common.DbRouterInfoTransformer;
import org.lin.dynamicdatasource.properties.DbRouterInfo;
import org.lin.dynamicdatasource.properties.DbRouterMarkContext;
import org.lin.dynamicdatasource.properties.DynamicDruidDataSourceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Aspect
@EnableAspectJAutoProxy
public class DataSourceSelectorAspect {

    @Autowired
    private DynamicDruidDataSourceProperties dataSourceProperties;

    @Pointcut("@annotation(org.lin.dynamicdatasource.annotation.DbRouter)")
    public void dbRouterPointcut() {
    }

    @Pointcut("@within(org.lin.dynamicdatasource.annotation.DbRouter)")
    public void dbRouterPointcutClass() {
    }

    @Around("@annotation(org.lin.dynamicdatasource.annotation.IgnoreDbRouter) || @within(org.lin.dynamicdatasource.annotation.IgnoreDbRouter)")
    public Object aroundIgnoreDataSource(ProceedingJoinPoint pjp) throws Throwable {
        String parentDataSource = null;
        DbRouterInfo parentDbRouterInfo = null;
        try {
            parentDataSource = DataSourceThreadContext.getDataSourceKey();
            parentDbRouterInfo = DataSourceThreadContext.getDataSourceInfo();

            DataSourceThreadContext.clearDataSourceInfo();
            DataSourceThreadContext.clearDataSourceInfo();

            return pjp.proceed();
        } finally {
            if (parentDataSource == null) {
                DataSourceThreadContext.clearDataSourceKey();
            } else {
                DataSourceThreadContext.setDataSourceKey(parentDataSource);
            }
            if (parentDbRouterInfo == null) {
                DataSourceThreadContext.clearDataSourceInfo();
            } else {
                DataSourceThreadContext.setDataSourceInfo(parentDbRouterInfo);
            }
        }
    }

    @Around("(dbRouterPointcut() && @annotation(dbRouter)) || (dbRouterPointcutClass() && @within(dbRouter))")
    public Object dbRouterSelector(ProceedingJoinPoint pjp, DbRouter dbRouter) throws Throwable {
        System.out.println("111");
        String parentDataSource = null;
        DbRouterInfo parentDbRouterInfo = null;
        try {
            parentDataSource = DataSourceThreadContext.getDataSourceKey();
            parentDbRouterInfo = DataSourceThreadContext.getDataSourceInfo();

            DbRouterInfo dbRouterInfo = DbRouterInfoTransformer.INSTANCE.annotationTransform(dbRouter);
            DataSourceThreadContext.setDataSourceInfo(dbRouterInfo);

            String selectDataSource = "";
            String[] useDBs = null;
            if (!dbRouterInfo.getUseDefaultDB()) {
                useDBs = dbRouterInfo.getValue();
                if (useDBs == null || useDBs.length != 0) {
                    useDBs = dataSourceProperties.getDruid().keySet().toArray(new String[0]);
                }
            } else {
                useDBs = new String[] {""};
            }
            if (StringUtils.isNotBlank(dbRouterInfo.getIdentifyName())) {
                dbRouterInfo.setDbRouterMark(getAttrValue(dbRouterInfo.getIdentifyName(), pjp.getArgs()));
            }
            if (dbRouterInfo.getDbRouterMark() == null) {
                dbRouterInfo.setDbRouterMark(DbRouterMarkContext.get());
                DbRouterMarkContext.clear();
            }
            if (parentDbRouterInfo != null) {
                org.lin.dynamicdatasource.utils.BeanUtils.copyPropertiesOnlyNull(parentDbRouterInfo, dbRouterInfo);
            }
            if (dbRouterInfo.getDbRouterMark() == null) {
                dbRouterInfo.setDbRouterMark("");
            }

            // 选择数据源和分表后缀
            int dbSize = useDBs.length;
            int tbSize;
            if (!dbRouterInfo.getNeedDivTable()) {
                tbSize = dbSize;
            } else {
                tbSize = dbRouterInfo.getTableCount();
            }
            int size = dbSize * tbSize;
            int idx = (size - 1) & (dbRouterInfo.getDbRouterMark().hashCode() ^ (dbRouterInfo.getDbRouterMark().hashCode() >>> 16));
            int dbIdx = idx / tbSize + 1;
            int tbIdx = idx - tbSize * (dbIdx - 1);
            dbRouterInfo.setTableIdx(tbIdx);
            selectDataSource = useDBs[dbIdx];

            DataSourceThreadContext.setDataSourceKey(selectDataSource);

            return pjp.proceed();
        } finally {
            if (parentDataSource == null) {
                DataSourceThreadContext.clearDataSourceKey();
            } else {
                DataSourceThreadContext.setDataSourceKey(parentDataSource);
            }
            if (parentDbRouterInfo == null) {
                DataSourceThreadContext.clearDataSourceInfo();
            } else {
                DataSourceThreadContext.setDataSourceInfo(parentDbRouterInfo);
            }
        }
    }

    public String getAttrValue(String attr, Object[] args) {
        String filedValue = null;
        for (Object arg : args) {
            try {
                if (StringUtils.isNotBlank(filedValue)) break;
                filedValue = BeanUtils.getProperty(arg, attr);
            } catch (Exception e) {
            }
        }
        return filedValue;
    }
}
