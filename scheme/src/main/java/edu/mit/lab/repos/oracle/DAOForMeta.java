package edu.mit.lab.repos.oracle;

/**
 * <p>Title: MIT Lib Project</p>
 * <p>Description: edu.mit.lab.repos.oracle.DAOForMeta</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: MIT Lib Co., Ltd</p>
 *
 * @author <chao.deng@mit.edu>
 * @version 1.0
 * @since 12/5/2016
 */
public class DAOForMeta extends edu.mit.lab.repos.common.DAOForMeta {

    public String exportedKeys() {
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
            "               row_number() over(order by f.owner, f.table_name, fc.position) rownumber" +
            "          from all_cons_columns pc," +
            "               all_constraints  p," +
            "               all_cons_columns fc," +
            "               all_constraints  f" +
            "         where p.owner = ?" +
            "           and f.owner = ?" +
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
            " where rownumber >= ?" +
            "   and rownumber <= ?" +
            " order by fktable_schem, fktable_name, key_seq";
    }

    public String importedKeys() {
        return "select *\n" +
            "  from (select null as pktable_cat,\n" +
            "               p.owner as pktable_schem,\n" +
            "               p.table_name as pktable_name,\n" +
            "               pc.column_name as pkcolumn_name,\n" +
            "               null as fktable_cat,\n" +
            "               f.owner as fktable_schem,\n" +
            "               f.table_name as fktable_name,\n" +
            "               fc.column_name as fkcolumn_name,\n" +
            "               fc.position as key_seq,\n" +
            "               null as update_rule,\n" +
            "               decode(f.delete_rule, 'CASCADE', 0, 'SET NULL', 2, 1) as delete_rule,\n" +
            "               f.constraint_name as fk_name,\n" +
            "               p.constraint_name as pk_name,\n" +
            "               decode(f.deferrable,\n" +
            "                      'DEFERRABLE',\n" +
            "                      5,\n" +
            "                      'NOT DEFERRABLE',\n" +
            "                      7,\n" +
            "                      'DEFERRED',\n" +
            "                      6) deferrability,\n" +
            "               row_number() over(order by p.owner, p.table_name, fc.position) rownumber\n" +
            "          from all_cons_columns pc,\n" +
            "               all_constraints  p,\n" +
            "               all_cons_columns fc,\n" +
            "               all_constraints  f\n" +
            "         where p.owner = ?\n" +
            "           and f.owner = ?\n" +
            "           and f.constraint_type = 'R'\n" +
            "           and p.owner = f.r_owner\n" +
            "           and p.constraint_name = f.r_constraint_name\n" +
            "           and p.constraint_type = 'P'\n" +
            "           and pc.owner = p.owner\n" +
            "           and pc.constraint_name = p.constraint_name\n" +
            "           and pc.table_name = p.table_name\n" +
            "           and fc.owner = f.owner\n" +
            "           and fc.constraint_name = f.constraint_name\n" +
            "           and fc.table_name = f.table_name\n" +
            "           and fc.position = pc.position\n" +
            "         order by pktable_schem, pktable_name, key_seq)\n" +
            " where rownumber >= ?\n" +
            "   and rownumber <= ?";
    }

    @Override
    public String primaryKeys() {

        return "select *\n" +
            "  from (select null as table_cat,\n" +
            "               c.owner as table_schem,\n" +
            "               c.table_name,\n" +
            "               c.column_name,\n" +
            "               c.position as key_seq,\n" +
            "               c.constraint_name as pk_name,\n" +
            "               row_number() over(order by c.table_name, c.column_name) rownumber\n" +
            "          from all_cons_columns c, all_constraints k\n" +
            "         where k.constraint_type = 'P'\n" +
            "           and k.owner like ? escape\n" +
            "         '/'\n" +
            "           and k.constraint_name = c.constraint_name\n" +
            "           and k.table_name = c.table_name\n" +
            "           and k.owner = c.owner\n" +
            "         order by c.table_name, column_name)\n" +
            " where rownumber >= ?\n" +
            "   and rownumber <= ?";
    }
}
