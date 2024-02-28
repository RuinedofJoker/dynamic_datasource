package org.lin.dynamicdatasource.common;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;


public class SwitchOpenDynamicDataSourceConditional implements Condition  {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return context.getEnvironment().getProperty("spring.datasource.druid.enableDynamic", Boolean.class, true);
    }
}
