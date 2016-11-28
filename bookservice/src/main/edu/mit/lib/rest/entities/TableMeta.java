package edu.mit.lib.rest.entities;

import org.apache.commons.lang3.StringUtils;

/**
 * <p>Title: MIT Library Practice</p>
 * <p>Description: edu.mit.lib.rest.entities.TableMeta</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: MIT Labs Co., Inc</p>
 *
 * @author <chao.deng@kewill.com>
 * @version 1.0
 * @since 11/11/2016
 */
public class TableMeta {

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

    public String getTableCatalog() {
        return tableCatalog;
    }

    public void setTableCatalog(String tableCatalog) {
        this.tableCatalog = tableCatalog;
    }

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

    public String getTableType() {
        return tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    public String getTableRemark() {
        return tableRemark;
    }

    public void setTableRemark(String tableRemark) {
        this.tableRemark = tableRemark;
    }

    public String getTypeCatalog() {
        return typeCatalog;
    }

    public void setTypeCatalog(String typeCatalog) {
        this.typeCatalog = typeCatalog;
    }

    public String getTypeSchema() {
        return typeSchema;
    }

    public void setTypeSchema(String typeSchema) {
        this.typeSchema = typeSchema;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTableSelfRefColName() {
        return tableSelfRefColName;
    }

    public void setTableSelfRefColName(String tableSelfRefColName) {
        this.tableSelfRefColName = tableSelfRefColName;
    }

    public String getTableRefGeneration() {
        return tableRefGeneration;
    }

    public void setTableRefGeneration(String tableRefGeneration) {
        this.tableRefGeneration = tableRefGeneration;
    }

    @Override
    public String toString() {
        StringBuilder item = new StringBuilder("TableMeta{");
        if (StringUtils.isNotEmpty(tableCatalog)) {
            item.append("tableCatalog='").append(tableCatalog).append('\'');
        }
        if (StringUtils.isNotEmpty(tableSchema)) {
            item.append(", tableSchema='").append(tableSchema).append('\'');
        }
        if (StringUtils.isNotEmpty(tableName)) {
            item.append(", tableName='").append(tableName).append('\'');
        }
        if (StringUtils.isNotEmpty(tableType)) {
            item.append(", tableType='").append(tableType).append('\'');
        }
        if (StringUtils.isNotEmpty(tableRemark)) {
            item.append(", tableRemark='").append(tableRemark).append('\'');
        }
        if (StringUtils.isNotEmpty(typeCatalog)) {
            item.append(", typeCatalog='").append(typeCatalog).append('\'');
        }
        if (StringUtils.isNotEmpty(typeSchema)) {
            item.append(", typeSchema='").append(typeSchema).append('\'');
        }
        if (StringUtils.isNotEmpty(typeName)) {
            item.append(", typeName='").append(typeName).append('\'');
        }
        if (StringUtils.isNotEmpty(tableSelfRefColName)) {
            item.append(", tableSelfRefColName='").append(tableSelfRefColName).append('\'');
        }
        if (StringUtils.isNotEmpty(tableRefGeneration)) {
            item.append(", tableRefGeneration='").append(tableRefGeneration).append('\'');
        }
        item.append('}');
        int position = item.indexOf("{") + 1;
        if (StringUtils.startsWith(item.substring(position), ",")) {
            item.replace(position, position + 2, "");
        }
        return item.toString();
    }
}
