package org.lin.dynamicdatasource.test.service;

import org.lin.dynamicdatasource.test.dto.Table;

import java.util.List;

public interface ITableService {
    List<Table> selectAll();
}
