package org.lin.dynamicdatasource.test.dto;

import lombok.Data;

import java.util.Date;

@Data
public abstract class BaseEntity {

    private Boolean isDel;

    private String createUser;

    private Date createTime;

}
