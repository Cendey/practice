package edu.mit.lab.repos.mysql;

/**
 * <p>Title: MIT Lib Project</p>
 * <p>Description: edu.mit.lab.repos.mysql.DAOForDBScheme</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: MIT Lib Co., Ltd</p>
 *
 * @author <chao.deng@mit.edu>
 * @version 1.0
 * @since 12/5/2016
 */
public class DAOForDBScheme extends edu.mit.lab.repos.common.DAOForDBScheme {

    public String fkConstraint() {
        return "select *\n" +
            "  from (select kcu.referenced_table_schema pktable_cat,\n" +
            "               null pktable_schem,\n" +
            "               kcu.referenced_table_name pktable_name,\n" +
            "               kcu.referenced_column_name pkcolumn_name,\n" +
            "               kcu.table_schema fktable_cat,\n" +
            "               null fktable_schem,\n" +
            "               kcu.table_name fktable_name,\n" +
            "               kcu.column_name fkcolumn_name,\n" +
            "               kcu.position_in_unique_constraint key_seq,\n" +
            "               case update_rule\n" +
            "                 when 'RESTRICT' then\n" +
            "                  1\n" +
            "                 when 'NO ACTION' then\n" +
            "                  3\n" +
            "                 when 'CASCADE' then\n" +
            "                  0\n" +
            "                 when 'SET NULL' then\n" +
            "                  2\n" +
            "                 when 'SET DEFAULT' then\n" +
            "                  4\n" +
            "               end update_rule,\n" +
            "               case delete_rule\n" +
            "                 when 'RESTRICT' then\n" +
            "                  1\n" +
            "                 when 'NO ACTION' then\n" +
            "                  3\n" +
            "                 when 'CASCADE' then\n" +
            "                  0\n" +
            "                 when 'SET NULL' then\n" +
            "                  2\n" +
            "                 when 'SET DEFAULT' then\n" +
            "                  4\n" +
            "               end delete_rule,\n" +
            "               rc.constraint_name fk_name,\n" +
            "               null pk_name,\n" +
            "               6 deferrability\n" +
            "          from information_schema.key_column_usage kcu\n" +
            "         inner join information_schema.referential_constraints rc\n" +
            "            on kcu.constraint_schema = rc.constraint_schema\n" +
            "           and kcu.constraint_name = rc.constraint_name\n" +
            "         where (kcu.referenced_table_schema = ?)\n" +
            "         order by fktable_cat, fktable_schem, fktable_name, key_seq) as refernce\n" +
            " limit ?, ?\n";
    }


}
