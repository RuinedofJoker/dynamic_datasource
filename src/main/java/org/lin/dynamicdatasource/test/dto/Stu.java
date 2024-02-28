package org.lin.dynamicdatasource.test.dto;

import lombok.Data;

@Data
public class Stu extends BaseEntity {

    private Long id;

    private String name;

    private Integer age;
}
