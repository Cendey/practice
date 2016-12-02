package edu.mit.lab.repos;

/**
 * <p>Title: MIT Lib Project</p>
 * <p>Description: edu.mit.lab.repos.DAOForScheme</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: MIT Lib Co., Ltd</p>
 *
 * @author <chao.deng@mit.edu>
 * @version 1.0
 * @since 12/2/2016
 */
public class DAOForScheme {

    public static String foreignKeyConstraintSQL() {
        return "select *" +
            "  from (select null as pktable_cat," +
            "               p.owner as pktable_schem," +
            "               p.table_name as pktable_name," +
            "               pc.column_name as pkcolumn_name," +
            "               null as fktable_cat," +
            "               f.owner as fktable_schem," +
            "               f.table_name as fktable_name," +
            "               fc.column_name as fkcolumn_name," +
            "               fc.position as key_seq," +
            "               null as update_rule," +
            "               decode(f.delete_rule, 'CASCADE', 0, 'SET NULL', 2, 1) as delete_rule," +
            "               f.constraint_name as fk_name," +
            "               p.constraint_name as pk_name," +
            "               decode(f.deferrable," +
            "                      'DEFERRABLE'," +
            "                      5," +
            "                      'NOT DEFERRABLE'," +
            "                      7," +
            "                      'DEFERRED'," +
            "                      6) deferrability," +
            "               row_number() over(order by f.owner, f.table_name, fc.position) rownumber"
            +
            "          from all_cons_columns pc," +
            "               all_constraints  p," +
            "               all_cons_columns fc," +
            "               all_constraints  f" +
            "         where p.owner = ?" +
            "           and f.constraint_type = 'R'" +
            "           and p.owner = f.r_owner" +
            "           and p.constraint_name = f.r_constraint_name" +
            "           and p.constraint_type = 'P'" +
            "           and pc.owner = p.owner" +
            "           and pc.constraint_name = p.constraint_name" +
            "           and pc.table_name = p.table_name" +
            "           and fc.owner = f.owner" +
            "           and fc.constraint_name = f.constraint_name" +
            "           and fc.table_name = f.table_name" +
            "           and fc.position = pc.position" +
            "         order by fktable_schem, fktable_name, key_seq)" +
            " where rownumber > ?" +
            "   and rownumber <= ?" +
            " order by fktable_schem, fktable_name, key_seq";
    }

    public static String primaryKeyConstraintSQL() {
        return "select *" +
            "  from (select null as pktable_cat," +
            "               p.owner as pktable_schem," +
            "               p.table_name as pktable_name," +
            "               pc.column_name as pkcolumn_name," +
            "               null as fktable_cat," +
            "               f.owner as fktable_schem," +
            "               f.table_name as fktable_name," +
            "               fc.column_name as fkcolumn_name," +
            "               fc.position as key_seq," +
            "               null as update_rule," +
            "               decode(f.delete_rule, 'CASCADE', 0, 'SET NULL', 2, 1) as delete_rule," +
            "               f.constraint_name as fk_name," +
            "               p.constraint_name as pk_name," +
            "               decode(f.deferrable," +
            "                      'DEFERRABLE'," +
            "                      5," +
            "                      'NOT DEFERRABLE'," +
            "                      7," +
            "                      'DEFERRED'," +
            "                      6) deferrability," +
            "               row_number() over(order by p.owner, p.table_name, fc.position) rownumber"
            +
            "          from all_cons_columns pc," +
            "               all_constraints  p," +
            "               all_cons_columns fc," +
            "               all_constraints  f" +
            "         where f.owner = ?" +
            "           and f.constraint_type = 'R'" +
            "           and p.owner = f.r_owner" +
            "           and p.constraint_name = f.r_constraint_name" +
            "           and p.constraint_type = 'P'" +
            "           and pc.owner = p.owner" +
            "           and pc.constraint_name = p.constraint_name" +
            "           and pc.table_name = p.table_name" +
            "           and fc.owner = f.owner" +
            "           and fc.constraint_name = f.constraint_name" +
            "           and fc.table_name = f.table_name" +
            "           and fc.position = pc.position" +
            "         order by pktable_schem, pktable_name, key_seq)" +
            " where rownumber >= ?" +
            "   and rownumber < ?";
    }
}
