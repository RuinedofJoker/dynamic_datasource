package org.lin.dynamicdatasource.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface DbRouter {

    @AliasFor("name")
    String[] value() default "";

    @AliasFor("value")
    String[] name() default "";

    String identifyName() default "";

    boolean useDefaultDB() default false;

    boolean needDivTable() default false;

    String tableName() default "";

    int tableCount() default 1;

    String split() default "_";
}
