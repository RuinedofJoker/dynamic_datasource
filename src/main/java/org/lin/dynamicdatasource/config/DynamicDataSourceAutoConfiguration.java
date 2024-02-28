package org.lin.dynamicdatasource.config;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.lin.dynamicdatasource.aspect.DataSourceSelectorAspect;
import org.lin.dynamicdatasource.common.DynamicDataSource;
import org.lin.dynamicdatasource.common.SwitchOpenDynamicDataSourceConditional;
import org.lin.dynamicdatasource.properties.DynamicDruidDataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Optional;

@Configuration
@EnableConfigurationProperties(DynamicDruidDataSourceProperties.class)
@Conditional(SwitchOpenDynamicDataSourceConditional.class)
public class DynamicDataSourceAutoConfiguration {

    @Bean
    public DynamicDataSource dataSource(DynamicDruidDataSourceProperties dynamicDruidDataSourceProperties) {
        if (dynamicDruidDataSourceProperties.getDruid() == null || dynamicDruidDataSourceProperties.getDruid().isEmpty()) {
            throw new RuntimeException("没有配置数据源");
        }
        DynamicDataSource dynamicDataSource = new DynamicDataSource(new LinkedHashMap<>(dynamicDruidDataSourceProperties.getDruid()));
        dynamicDataSource.setTargetDataSources(dynamicDataSource.getDataSourceMap());
        Optional<DynamicDruidDataSourceProperties.DynamicDruidDataSource> dynamicDruidDataSourceOptional = dynamicDruidDataSourceProperties.getDruid().values().stream().
                filter(DynamicDruidDataSourceProperties.DynamicDruidDataSource::getPrimary)
                .findFirst();
        dynamicDruidDataSourceOptional.ifPresent(dynamicDataSource::setDefaultTargetDataSource);
        dynamicDruidDataSourceOptional.orElseGet(() -> {
            dynamicDataSource.setDefaultTargetDataSource(dynamicDruidDataSourceProperties.getDruid().get(0));
            return null;
        });
        return dynamicDataSource;
    }

    @Bean
    public DataSourceSelectorAspect dataSourceSelectorAspect() {
        return new DataSourceSelectorAspect();
    }

    @Bean
    public ConfigurationCustomizer configurationCustomizer(){
        return new ConfigurationCustomizer(){
            @Override
            public void customize(MybatisConfiguration configuration) {
                configuration.addInterceptor(new TableNameInterceptor());
            }
        };
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(DynamicDataSource dataSource, MybatisPlusAutoConfiguration configuration) throws Exception {
        return configuration.sqlSessionFactory(dataSource);
    }
}
