package edu.mit.lib.rest.utils;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import edu.mit.lib.rest.entities.RefKeyMeta;
import edu.mit.lib.rest.entities.TableMeta;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>Title: MIT Library Practice</p>
 * <p>Description: edu.mit.lib.rest.utils.DatabaseSchemeAnalysis</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: MIT Labs Co., Inc</p>
 *
 * @author <chao.deng@kewill.com>
 * @version 1.0
 * @since 11/11/2016
 */
public class DatabaseSchemeAnalysis {

    static {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    private static Connection createConnection() {
        Connection connection = null;
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        try {
            dataSource.setDriverClass("oracle.jdbc.driver.OracleDriver");
            dataSource.setJdbcUrl("jdbc:oracle:thin:@10.104.46.195:1521:orcl12c");
            dataSource.setUser("kerry_dev_20150424");
            dataSource.setPassword("kerry_dev_20150424");
            dataSource.setMinPoolSize(8);
            dataSource.setAcquireIncrement(8);
            dataSource.setMaxPoolSize(12);
            connection = dataSource.getConnection();
            System.out.println(String.format("Database connect success! %s", connection.toString()));
        } catch (PropertyVetoException | SQLException e) {
            System.err.println(String.format("Database connect failed! %s", "Ops"));
            System.err.println(e.getMessage());
        }

        return connection;
    }

    public static void main(String[] args) {
        Connection connection = createConnection();
        if (connection != null) {
            try {
                DatabaseMetaData metaData = connection.getMetaData();
                processTableType(metaData);

                List<TableMeta> lstTable = processTable(connection, metaData);

                processPKRef(connection, metaData, lstTable);

                processFKRef(connection, metaData, lstTable);

                processQuery(connection);
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            } finally {
                try {
                    if (!connection.isClosed()) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    System.err.println(e.getMessage());
                }
            }
        }
    }

    @SuppressWarnings(value = {"StringBufferReplaceableByString"})
    private static void processQuery(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            StringBuilder dialect = new StringBuilder("select ctn.loadrefno as loadrefno,\n")
                .append("       ctn.unid,\n")
                .append("       ctn.contno,\n")
                .append("       ctn.sealno,\n")
                .append("       ctn.conttype,\n")
                .append("       to_char(sum(c.totpcs)) as totpcs,\n")
                .append("       sum(c.totwgt) as totwgt,\n")
                .append("       sum(c.totvol) as totvol,\n")
                .append("       fmctn.localdesc as conttypelocaldesc,\n")
                .append("       fmctn.description as conttypedesc\n")
                .append("  from container ctn\n")
                .append("  left join seacontitem c\n")
                .append("    on ctn.unid = c.container_unid\n")
                .append("   and ctn.job_unid = c.job_unid\n")
                .append("  left join fmctntype fmctn\n")
                .append("    on (ctn.conttype = fmctn.conttype and fmctn.schemecode = '*')\n")
                .append(" where ctn.job_unid = 245565\n")
                .append(" group by ctn.unid,\n")
                .append("          loadrefno,\n")
                .append("          ctn.contno,\n")
                .append("          ctn.sealno,\n")
                .append("          ctn.conttype,\n")
                .append("          localdesc,\n")
                .append("          description\n")
                .append(" order by conttype, contno, loadrefno");

            ResultSet result = statement.executeQuery(dialect.toString());
            while (result.next()) {
                Long unid = result.getLong("unid");
                String contNo = result.getString("contno");
                System.out.println(String.format("Container identifier is %d and number is %s", unid, contNo));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void processTableType(DatabaseMetaData metaData) throws SQLException {
        List<String> lstTableTypes = new ArrayList<>();
        ResultSet types = metaData.getTableTypes();
        while (types.next()) {
            lstTableTypes.add(types.getString("TABLE_TYPE"));
        }
        lstTableTypes.forEach(System.out::println);
    }

    private static List<TableMeta> processTable(Connection connection, DatabaseMetaData metaData) {
        List<TableMeta> lstTable = new ArrayList<>();
        try (ResultSet tables =
                 metaData.getTables(connection.getCatalog(), connection.getSchema(), null, new String[]{"TABLE"})) {
            ResultSetMetaData meta = tables.getMetaData();
            int columns = meta.getColumnCount();
            Set<String> nameSet = new HashSet<>(columns);
            for (int i = 1; i < columns; i++) {
                nameSet.add(meta.getColumnName(i));
            }


            while (tables.next()) {
                TableMeta tableMeta = new TableMeta();
                pushTableSummaryInfo(tables, lstTable, nameSet, tableMeta);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~#Tables information list#~~~~~~~~~~~~~~~~~~~~~~~");
        lstTable.forEach(System.out::println);
        return lstTable;
    }

    private static void processPKRef(Connection connection, DatabaseMetaData metaData, List<TableMeta> lstTable) {
        List<RefKeyMeta> lstPrimaryKey = new ArrayList<>();
        lstTable.forEach(
            tableMeta -> pushPrimaryKeyInfo(connection, metaData, lstPrimaryKey, tableMeta.getTableName()));
        System.out.println(
            "~~~~~~~~~~~~~~~~~~~~~~~#Tables primary keys reference information list#~~~~~~~~~~~~~~~~~~~~~~~");
        lstPrimaryKey.forEach(System.out::println);
    }

    private static void processFKRef(Connection connection, DatabaseMetaData metaData, List<TableMeta> lstTable) {
        List<RefKeyMeta> lstForeignKey = new ArrayList<>();
        lstTable
            .forEach(tableMeta -> pushForeignKeyInfo(connection, metaData, lstForeignKey, tableMeta.getTableName()));
        System.out.println(
            "~~~~~~~~~~~~~~~~~~~~~~~#Tables foreign keys reference information list#~~~~~~~~~~~~~~~~~~~~~~~");
        lstForeignKey.forEach(System.out::println);
    }

    private static void pushForeignKeyInfo(
        Connection connection, DatabaseMetaData metaData, List<RefKeyMeta> lstForeignKey, String tableName) {
        try (ResultSet foreignKeys = metaData
            .getExportedKeys(connection.getCatalog(), connection.getSchema(), tableName)) {
            handleTableRefKeys(lstForeignKey, foreignKeys);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void handleTableRefKeys(List<RefKeyMeta> lstForeignKey, ResultSet foreignKeys) throws SQLException {
        while (foreignKeys.next()) {
            RefKeyMeta foreignKeyInfo = new RefKeyMeta();
            foreignKeyInfo.setPkTableName(foreignKeys.getString("PKTABLE_NAME"));
            foreignKeyInfo.setPkColumnName(foreignKeys.getString("PKCOLUMN_NAME"));
            foreignKeyInfo.setFkTableName(foreignKeys.getString("FKTABLE_NAME"));
            foreignKeyInfo.setFkColumnName(foreignKeys.getString("FKCOLUMN_NAME"));
            foreignKeyInfo.setKeySequence(foreignKeys.getShort("KEY_SEQ"));
            lstForeignKey.add(foreignKeyInfo);
        }
    }

    private static void pushPrimaryKeyInfo(
        Connection connection, DatabaseMetaData metaData, List<RefKeyMeta> lstPrimaryKey, String tableName) {
        try (ResultSet primaryKeys = metaData
            .getImportedKeys(connection.getCatalog(), connection.getSchema(), tableName)) {
            handleTableRefKeys(lstPrimaryKey, primaryKeys);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void pushTableSummaryInfo(
        ResultSet tables, List<TableMeta> lstTable, Set<String> nameSet, TableMeta tableMeta)
        throws SQLException {

        if (nameSet.contains("TABLE_CAT")) {
            tableMeta.setTableCatalog(tables.getString("TABLE_CAT"));
        }
        if (nameSet.contains("TABLE_SCHEM")) {
            tableMeta.setTableSchema(tables.getString("TABLE_SCHEM"));
        }
        if (nameSet.contains("TABLE_NAME")) {
            tableMeta.setTableName(tables.getString("TABLE_NAME"));
        }
        if (nameSet.contains("TABLE_TYPE")) {
            tableMeta.setTableType(tables.getString("TABLE_TYPE"));
        }
        if (nameSet.contains("REMARKS")) {
            tableMeta.setTableRemark(tables.getString("REMARKS"));
        }
        if (nameSet.contains("TYPE_CAT")) {
            tableMeta.setTypeCatalog(tables.getString("TYPE_CAT"));
        }
        if (nameSet.contains("TYPE_SCHEM")) {
            tableMeta.setTypeSchema(tables.getString("TYPE_SCHEM"));
        }
        if (nameSet.contains("TYPE_NAME")) {
            tableMeta.setTypeName(tables.getString("TYPE_NAME"));
        }
        if (nameSet.contains("SELF_REFERENCING_COL_NAME")) {
            tableMeta.setTableSelfRefColName(tables.getString("SELF_REFERENCING_COL_NAME"));
        }
        if (nameSet.contains("REF_GENERATION")) {
            tableMeta.setTableRefGeneration(tables.getString("REF_GENERATION"));
        }
        lstTable.add(tableMeta);
    }
}
