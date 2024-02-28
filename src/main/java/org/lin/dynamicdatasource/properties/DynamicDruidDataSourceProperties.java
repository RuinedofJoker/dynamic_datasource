package org.lin.dynamicdatasource.properties;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;

@ConfigurationProperties(prefix = "spring.datasource")
@Data
public class DynamicDruidDataSourceProperties {

    private LinkedHashMap<String, DynamicDruidDataSource> druid;

    /**
     * 是否开启动态数据源
     */
    private Boolean enableDynamic;

    @Data
    public static class DynamicDruidDataSource extends DruidDataSource {

        /**
         * 是否设置为主数据源
         */
        private Boolean primary = false;

    }
}
