package edu.mit.lab.meta;

import edu.mit.lab.constant.Scheme;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * <p>Title: Kewill Lab Center</p>
 * <p>Description: edu.mit.lab.meta.Tables</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Kewill Co., Ltd.</p>
 *
 * @author <chao.deng@kewill.com>
 * @version 1.0
 * @since 11/14/2016
 */
public class Tables implements Serializable {

    private String tableCatalog;
    private String tableSchema;
    private String tableName;
    private String tableType;
    private String tableRemark;
    private String typeCatalog;
    private String typeSchema;
    private String typeName;
    private String tableSelfRefColName;
    private String tableRefGeneration;

    @SuppressWarnings(value = {"unused"})
    public String getTableCatalog() {
        return tableCatalog;
    }

    public void setTableCatalog(String tableCatalog) {
        this.tableCatalog = tableCatalog;
    }

    @SuppressWarnings(value = {"unused"})
    public String getTableSchema() {
        return tableSchema;
    }

    public void setTableSchema(String tableSchema) {
        this.tableSchema = tableSchema;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @SuppressWarnings(value = {"unused"})
    public String getTableType() {
        return tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    @SuppressWarnings(value = {"unused"})
    public String getTableRemark() {
        return tableRemark;
    }

    public void setTableRemark(String tableRemark) {
        this.tableRemark = tableRemark;
    }

    @SuppressWarnings(value = {"unused"})
    public String getTypeCatalog() {
        return typeCatalog;
    }

    public void setTypeCatalog(String typeCatalog) {
        this.typeCatalog = typeCatalog;
    }

    @SuppressWarnings(value = {"unused"})
    public String getTypeSchema() {
        return typeSchema;
    }

    public void setTypeSchema(String typeSchema) {
        this.typeSchema = typeSchema;
    }

    @SuppressWarnings(value = {"unused"})
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @SuppressWarnings(value = {"unused"})
    public String getTableSelfRefColName() {
        return tableSelfRefColName;
    }

    public void setTableSelfRefColName(String tableSelfRefColName) {
        this.tableSelfRefColName = tableSelfRefColName;
    }

    @SuppressWarnings(value = {"unused"})
    public String getTableRefGeneration() {
        return tableRefGeneration;
    }

    public void setTableRefGeneration(String tableRefGeneration) {
        this.tableRefGeneration = tableRefGeneration;
    }

    @Override
    public int hashCode() {
        return (
            !StringUtils.isEmpty(getTableSchema()) ? getTableSchema().hashCode() * 3 : 0)
            + (!StringUtils.isEmpty(getTableCatalog()) ? getTableCatalog().hashCode() * 7 : 0)
            + (!StringUtils.isEmpty(getTableName()) ? getTableName().hashCode() * 11 : 0)
            + (!StringUtils.isEmpty(getTableType()) ? getTableType().hashCode() * 13 : 0)
            + (!StringUtils.isEmpty(getTableSelfRefColName()) ? getTableSelfRefColName().hashCode() * 17 : 0)
            + (!StringUtils.isEmpty(getTableRefGeneration()) ? getTableRefGeneration().hashCode() * 19 : 0)
            + (!StringUtils.isEmpty(getTableRemark()) ? getTableRemark().hashCode() * 23 : 0)
            + (!StringUtils.isEmpty(getTypeSchema()) ? getTypeSchema().hashCode() * 29 : 0)
            + (!StringUtils.isEmpty(getTypeCatalog()) ? getTypeCatalog().hashCode() * 31 : 0)
            + (!StringUtils.isEmpty(getTypeName()) ? getTypeName().hashCode() * 41 : 0);
    }

    @Override
    public String toString() {
        StringBuilder item = new StringBuilder("Tables{");
        if (StringUtils.isNotEmpty(tableCatalog)) {
            item.append(Scheme.TABLE_CATALOG).append("='").append(tableCatalog).append('\'');
        }
        if (StringUtils.isNotEmpty(tableSchema)) {
            item.append(", ").append(Scheme.TABLE_SCHEMA).append("='").append(tableSchema).append('\'');
        }
        if (StringUtils.isNotEmpty(tableName)) {
            item.append(", ").append(Scheme.TABLE_NAME).append("='").append(tableName).append('\'');
        }
        if (StringUtils.isNotEmpty(tableType)) {
            item.append(", ").append(Scheme.TABLE_TYPE).append("='").append(tableType).append('\'');
        }
        if (StringUtils.isNotEmpty(tableRemark)) {
            item.append(", ").append(Scheme.TABLE_REMARK).append("='").append(tableRemark).append('\'');
        }
        if (StringUtils.isNotEmpty(typeCatalog)) {
            item.append(", ").append(Scheme.TYPE_CATALOG).append("='").append(typeCatalog).append('\'');
        }
        if (StringUtils.isNotEmpty(typeSchema)) {
            item.append(", ").append(Scheme.TYPE_SCHEMA).append("='").append(typeSchema).append('\'');
        }
        if (StringUtils.isNotEmpty(typeName)) {
            item.append(", ").append(Scheme.TYPE_NAME).append("='").append(typeName).append('\'');
        }
        if (StringUtils.isNotEmpty(tableSelfRefColName)) {
            item.append(", ").append(Scheme.TABLE_SELF_REF_COL_NAME).append("='").append(tableSelfRefColName)
                .append('\'');
        }
        if (StringUtils.isNotEmpty(tableRefGeneration)) {
            item.append(", ").append(Scheme.TABLE_REF_GENERATION).append("='").append(tableRefGeneration).append('\'');
        }
        item.append('}');
        int position = item.indexOf("{") + 1;
        if (StringUtils.startsWith(item.substring(position), ",")) {
            item.replace(position, position + 2, "");
        }
        return item.toString();
    }
}
