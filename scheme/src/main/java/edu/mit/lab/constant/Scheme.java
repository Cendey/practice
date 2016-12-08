package edu.mit.lab.constant;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <p>Title: Kewill Lab Center</p>
 * <p>Description: edu.mit.lab.literal.Scheme</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Kewill Co., Ltd.</p>
 *
 * @author <chao.deng@kewill.com>
 * @version 1.0
 * @since 11/14/2016
 */
public class Scheme {

    public static final String FKTABLE_NAME = "FKTABLE_NAME";
    public static final String FKCOLUMN_NAME = "FKCOLUMN_NAME";
    public static final String PKTABLE_NAME = "PKTABLE_NAME";
    public static final String PKCOLUMN_NAME = "PKCOLUMN_NAME";
    public static final String KEY_SEQ = "KEY_SEQ";
    public static final String TABLE_CAT = "TABLE_CAT";
    public static final String TABLE_SCHEM = "TABLE_SCHEM";
    public static final String _TABLE_NAME = "TABLE_NAME";
    public static final String COLUMN_NAME = "COLUMN_NAME";
    public static final String PK_NAME = "PK_NAME";
    public static final String _TABLE_TYPE = "TABLE_TYPE";
    public static final String REMARKS = "REMARKS";
    public static final String TYPE_CAT = "TYPE_CAT";
    public static final String TYPE_SCHEM = "TYPE_SCHEM";
    public static final String _TYPE_NAME = "TYPE_NAME";
    public static final String ENTITY_TABLE = "TABLE";
    public static final String SELF_REFERENCING_COL_NAME = "SELF_REFERENCING_COL_NAME";
    public static final String REF_GENERATION = "REF_GENERATION";
    public static final String PK_TABLE_NAME = "pkTableName";
    public static final String PK_COLUMN_NAME = "pkColumnName";
    public static final String FK_TABLE_NAME = "fkTableName";
    public static final String FK_COLUMN_NAME = "fkColumnName";
    public static final String KEY_SEQUENCE = "keySequence";
    public static final String TABLE_CATALOG = "tableCatalog";
    public static final String TABLE_SCHEMA = "tableSchema";
    public static final String TABLE_NAME = "tableName";
    public static final String TABLE_TYPE = "tableType";
    public static final String TABLE_REMARK = "tableRemark";
    public static final String TYPE_CATALOG = "typeCatalog";
    public static final String TYPE_SCHEMA = "typeSchema";
    public static final String TYPE_NAME = "typeName";
    public static final String TABLE_SELF_REF_COL_NAME = "tableSelfRefColName";
    public static final String TABLE_REF_GENERATION = "tableRefGeneration";

    //TreeNode Present Status
    public static final int BOTH_NODES_ABSENT = 0;
    public static final int ONLY_TARGET_NODE_PRESENTS = 1;
    public static final int ONLY_SOURCE_NODE_PRESENTS = 2;
    public static final int BOTH_NODES_PRESENT = 3;

    //Graph Element Prefix
    public static final String NODE_PREFIX = "n";
    public static final String EDGE_PREFIX = "e";

    //Node Reference
    public static final String DATA_PARENT = "data.parent";
    public static final String DATA_CHILD = "data.child";

    public static final int MINIMUM_SIZE = 20;
    public static final int MAXIMUM_SIZE = 35;
    //UI CSS Attribute
    public static final String UI_DEFAULT_TITLE = "ui.default.title";
    public static final String UI_LABEL = "ui.label";
    public static final String UI_QUALITY = "ui.quality";
    public static final String UI_ANTIALIAS = "ui.antialias";
    public static final String UI_STYLESHEET = "ui.stylesheet";
    public static final String UI_CLASS = "ui.class";
    public static final String UI_VIEW_CLOSED = "ui.viewClosed";
    public static final String ROOT = "root";
    public static final String LEAF = "leaf";
    public static final String TEXT_MODE = "ui.text-mode";
    public static final String SHOW_TIPS = "normal";
    public static final String HIDE_TIPS = "hidden";

    //Literal for consistent reference
    public static final String HIKARI_PROPERTIES = "hikari.properties";
    public static final String SLASH_HIKARI_PROPERTIES = "/hikari.properties";
    public static final String DEPENDENCY = "Dependency";
    public static final String COMPONENT = "Component";

    //Travel Order
    public static final String PRE_ORDER_TRAVERSAL = "Pre-Order";
    public static final String POST_ORDER_TRAVERSAL = "Post-Order";
    public static final String ACTION_MARKED = "action.marked";

    //SQL Comments
    public static final String PREVENT_EDITOR_ADD_BOM_HEADER =
        "/* prevent some editor to add bom header in this sql script file */\r\n";
    public static final String STD_SQL_COMMENTS_BOILERPLATE =
        "\r\n/* deleted table group[%s], and those are related */\r\n";
    public static final String STD_SQL_COMMENTS_BOILERPLATE_FOR_INDEPENDENT_TABLES =
        "\r\n/* deleted other table group as follows */\r\n";
    public static final String STD_SQL_COMMENTS_BOILERPLATE_FOR_AUDIT_LOG_TABLES =
        "\r\n/* deleted audit log tables */\r\n";

    //SQL Statements
    public static final String STD_SQL_COMMIT = "\r\ncommit;\r\n";
    public static final String STD_SQL_DELETE_STATEMENT = "delete from  %s;\r\n";
    public static final String AUDIT_LOG_TABLE_POSTFIX = "cl";

    //Verbose Message
    public static final String TIME_DURATION_PROCESS = "This process spends [%02d min, %02d sec].";

    //System Properties
    public static final String USER_DIR = "user.dir";
    public static final String FILE_SEPARATOR = "file.separator";
    public static final String UTF_8 = "UTF-8";

    //Node Access Status
    public static final String IDENTIFIED = "identified";
    public static final String MARKED = "marked";
    public static final List<String> STATUS = Collections.unmodifiableList(Arrays.asList(IDENTIFIED, MARKED));

    //Directory to store output data or files
    public static final String WORK_DIR =
        System.getProperty(Scheme.USER_DIR) + System.getProperty(Scheme.FILE_SEPARATOR) + "work" + System
            .getProperty(Scheme.FILE_SEPARATOR);

    public static final String DB_TYPE_ORACLE = "Oracle";
    public static final String DB_TYPE_MYSQL = "MySQL";
    public static final String TABLES = "tables";
    public static final String FOREIGN_KEYS = "foreign_keys";
    public static final String EHCACHE_XML = "ehcache.xml";
    public static final String SLASH_EHCACHE_XML = "/ehcache.xml";
}
