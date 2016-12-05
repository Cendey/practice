package edu.mit.lab.repos.mysql;

/**
 * <p>Title: MIT Lib Project</p>
 * <p>Description: edu.mit.lab.repos.mysql.DAOForMeta</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: MIT Lib Co., Ltd</p>
 *
 * @author <chao.deng@mit.edu>
 * @version 1.0
 * @since 12/5/2016
 */
public class DAOForMeta extends edu.mit.lab.repos.common.DAOForMeta {

    public String exportedKeys() {
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
            "         order by fktable_cat, fktable_schem, fktable_name, key_seq) refer\n" +
            " limit ?, ?";
    }

    @Override
    public String importedKeys() {
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
            "         order by pktable_cat, pktable_schem, pktable_name, key_seq) refer\n" +
            " limit ?, ?";
    }

    @Override
    public String primaryKeys() {

        return "select *\n" +
            "  from (select a.table_schema table_cat,\n" +
            "               null           table_schem,\n" +
            "               a.table_name,\n" +
            "               a.column_name,\n" +
            "               b.seq_in_index key_seq,\n" +
            "               null           pk_name\n" +
            "          from information_schema.columns a, information_schema.statistics b\n" +
            "         where a.column_key = 'pri'\n" +
            "           and b.index_name = 'PRIMARY'\n" +
            "           and (a.table_schema = ?)\n" +
            "           and (b.table_schema = ?)\n" +
            "           and a.table_schema = b.table_schema\n" +
            "           and a.table_name = b.table_name\n" +
            "           and a.column_name = b.column_name\n" +
            "         order by a.column_name) refer\n" +
            " limit ?, ?";
    }
}
