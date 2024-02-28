package org.lin.dynamicdatasource.common;

import lombok.Getter;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.Map;

public class DynamicDataSource extends AbstractRoutingDataSource {

    @Getter
    private final Map<Object, Object> dataSourceMap;

    public DynamicDataSource(Map<String, DataSource> dataSourceMap) {
        this.dataSourceMap = Collections.unmodifiableMap(dataSourceMap);
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceThreadContext.getDataSourceKey();
    }
}
