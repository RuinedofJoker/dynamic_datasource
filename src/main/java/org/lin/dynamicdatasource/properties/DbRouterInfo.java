package org.lin.dynamicdatasource.properties;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DbRouterInfo {

    private String[] value;

    private String identifyName;

    private Boolean useDefaultDB;

    private Boolean needDivTable;

    private String tableName;

    private Integer tableCount;

    private Integer tableIdx;

    private String split;

    private String dbRouterMark;
}
