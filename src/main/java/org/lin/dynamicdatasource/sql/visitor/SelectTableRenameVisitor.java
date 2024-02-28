package org.lin.dynamicdatasource.sql.visitor;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitorAdapter;
import com.alibaba.druid.util.JdbcConstants;
import com.sun.istack.internal.NotNull;

import java.util.List;

public class SelectTableRenameVisitor extends MySqlASTVisitorAdapter {

    private final String oldTableName;

    private final String newTableName;

    private static final String dbType = JdbcConstants.MYSQL;

    public SelectTableRenameVisitor(@NotNull String oldTableName, @NotNull String newTableName) {
        this.oldTableName = oldTableName;
        this.newTableName = newTableName;
    }

    @Override
    public boolean visit(MySqlSelectQueryBlock x) {
        SQLTableSource tableSource = x.getFrom();
        if (tableSource instanceof SQLExprTableSource) {
            SQLExpr expr = ((SQLExprTableSource) tableSource).getExpr();
            if (expr instanceof SQLIdentifierExpr && oldTableName.equalsIgnoreCase(((SQLIdentifierExpr) expr).getName())) {
                ((SQLIdentifierExpr) expr).setName(newTableName);
                return true;
            }
        }
        return false;
    }

    public String changeSql(String orgSql) {
        List<SQLStatement> stmtList = SQLUtils.parseStatements(orgSql, dbType);
        for (SQLStatement stmt : stmtList) {
            stmt.accept(this);
        }
        return SQLUtils.toSQLString(stmtList, dbType);
    }

}
