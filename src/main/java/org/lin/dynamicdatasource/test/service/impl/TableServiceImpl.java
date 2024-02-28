package org.lin.dynamicdatasource.test.service.impl;

import org.lin.dynamicdatasource.annotation.DbRouter;
import org.lin.dynamicdatasource.test.dao.TableDao;
import org.lin.dynamicdatasource.test.dto.Table;
import org.lin.dynamicdatasource.test.service.ITableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@DbRouter
@Service
public class TableServiceImpl implements ITableService {

    @Autowired
    private TableDao tableDao;

    @Override
    public List<Table> selectAll() {
        List<Table> tables = tableDao.selectList(null);
        return tables;
    }
}
