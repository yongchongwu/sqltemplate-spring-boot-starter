package com.ycwu.sqltemplate.util;

public class SqlUtil {

    public final static String ORACLE_DIALECT = "oracle";

    public final static String DB2_DIALECT = "db2";

    public final static String MYSQL_DIALECT = "mysql";

    public final static String POSTGRESQL_DIALECT = "postgresql";

    public final static String H2_DIALECT = "h2";

    public final static String MSSQL_DIALECT = "mssql";

    public static String makeCountSql(String sql, String dialect) {
        StringBuffer sqlb = new StringBuffer(sql.length() + 100);
        if (dialect.equalsIgnoreCase(ORACLE_DIALECT)) {
            sqlb.append("select count(*) from (");
            sqlb.append(sql);
            sqlb.append(")");
            return sqlb.toString();
        } else {
            sqlb.append("select count(*) from (");
            sqlb.append(sql);
            sqlb.append(") my_alias");
            return sqlb.toString();
        }
    }

    public static String makeScrollSql(String sql, int start, int len,String dialect) {

        StringBuffer sqlb = new StringBuffer(sql.length() + 100);

        if (dialect.equalsIgnoreCase(ORACLE_DIALECT)) {
            //oracle
            sqlb=getOracleScrollSql(sql, start, len);

        } else if (dialect.equalsIgnoreCase(MYSQL_DIALECT)) {
            //mysql
            sqlb.append(sql).append(" limit ");
            sqlb.append(start);
            sqlb.append(",");
            sqlb.append(len);

        } else if (dialect.equalsIgnoreCase(POSTGRESQL_DIALECT)) {
            //postgresql
            sqlb.append(sql).append(" limit ");
            sqlb.append(len);
            sqlb.append(" offset ");
            sqlb.append(start);

        } else if (dialect.equalsIgnoreCase(H2_DIALECT)) {
            //h2
            sqlb.append(sql).append(" limit ");
            sqlb.append(len);
            sqlb.append(" offset ");
            sqlb.append(start);
        } else if (dialect.equalsIgnoreCase(DB2_DIALECT)) {
            //db2
            sqlb=getDb2ScrollSql(sql, start, len);

        } else if (dialect.equalsIgnoreCase(MSSQL_DIALECT)) {
            //mssql

        }
        return sqlb.toString();
    }

    private static boolean hasDistinct(String sql) {
        return sql.toLowerCase().indexOf("select distinct") >= 0;
    }

    private static String getRowNumber(String sql) {
        StringBuffer rownumber = new StringBuffer(50)
            .append("rownumber() over(");

        int orderByIndex = sql.toLowerCase().indexOf("order by");

        if (orderByIndex > 0 && !hasDistinct(sql)) {
            rownumber.append(sql.substring(orderByIndex));
        }

        rownumber.append(") as rownumber_,");

        return rownumber.toString();
    }

    private static StringBuffer getOracleScrollSql(String sql, int start, int len) {
        StringBuffer sqlb = new StringBuffer(sql.length() + 100);
        sqlb.append("select * from ( select row_.*, rownum rownum_ from ( ");
        sqlb.append(sql);
        sqlb.append(" ) row_ where rownum <= ");
        sqlb.append(start + len);
        sqlb.append(") where rownum_ > ");
        sqlb.append(start);
        return sqlb;
    }

    private static StringBuffer getDb2ScrollSql(String sql, int start, int len) {
        int startOfSelect = sql.toLowerCase().indexOf("select");
        StringBuffer pagingSelect = new StringBuffer(sql.length() + 100)
            .append(sql.substring(0, startOfSelect))
            .append("select * from ( select ")
            .append(getRowNumber(sql));
        if (hasDistinct(sql)) {
            pagingSelect.append(" row_.* from ( ")
                .append(sql.substring(startOfSelect))
                .append(" ) as row_");
        } else {
            pagingSelect.append(sql.substring(startOfSelect + 6));
        }
        pagingSelect.append(" ) as temp_ where rownumber_ ");
        pagingSelect.append("between " + (start + 1) + " and "
            + (start + len));
        return pagingSelect;
    }

}
