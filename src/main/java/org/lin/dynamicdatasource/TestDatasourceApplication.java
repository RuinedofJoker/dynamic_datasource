package org.lin.dynamicdatasource;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

//@SpringBootApplication
public class TestDatasourceApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(TestDatasourceApplication.class, args);
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            System.out.println(beanDefinitionName);
        }

//        TableDao dao = applicationContext.getBean(TableDao.class);
//        ITableService tableService = applicationContext.getBean(ITableService.class);
//        dao.insert(new Table().setTestCol("123"));
        //dao.insertTestCol(new Table().setTestCol("123"));
//        DbRouterMarkContext.set("1");
        //List<Table> tables = dao.selectList(null);
//        List<Table> tables = tableService.selectAll();
//        System.out.println(tables);


//        String sql = "SELECT id,test_col FROM table1 where id = 1 and name = 'nihao' or age > 18";
//        List<SQLStatement> sqlStatements = SQLUtils.parseStatements(sql, JdbcConstants.MYSQL);
//        System.out.println(sqlStatements);

//        String sql = "select * from t where id=1 or name='test' and age=14";
//        List<SQLStatement> sqlStatements = SQLUtils.parseStatements(sql, JdbcConstants.MYSQL);
//        SQLExpr sqlExpr = SQLUtils.toSQLExpr(sql, JdbcConstants.MYSQL);
//        System.out.println(sqlStatements);
    }



}
