package edu.mit.lib.rest.entities;

import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;

/**
 * <p>Title: MIT Library Practice</p>
 * <p>Description: edu.mit.lib.rest.entities.RefKeyMeta</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: MIT Labs Co., Inc</p>
 *
 * @author <chao.deng@kewill.com>
 * @version 1.0
 * @since 11/11/2016
 */
public class RefKeyMeta implements Comparator {

    private String pkTableName;
    private String pkColumnName;
    private String fkTableName;
    private String fkColumnName;
    private Short keySequence;

    public String getPkTableName() {
        return pkTableName;
    }

    public void setPkTableName(String pkTableName) {
        this.pkTableName = pkTableName;
    }

    public String getPkColumnName() {
        return pkColumnName;
    }

    public void setPkColumnName(String pkColumnName) {
        this.pkColumnName = pkColumnName;
    }

    public String getFkTableName() {
        return fkTableName;
    }

    public void setFkTableName(String fkTableName) {
        this.fkTableName = fkTableName;
    }

    public String getFkColumnName() {
        return fkColumnName;
    }

    public void setFkColumnName(String fkColumnName) {
        this.fkColumnName = fkColumnName;
    }

    public Short getKeySequence() {
        return keySequence;
    }

    public void setKeySequence(Short keySequence) {
        this.keySequence = keySequence;
    }

    @Override
    public String toString() {
        StringBuilder item = new StringBuilder("RefKeyMeta{");
        if (StringUtils.isNotEmpty(pkTableName)) {
            item.append("pkTableName='").append(pkTableName).append('\'');
        }
        if (StringUtils.isNotEmpty(pkColumnName)) {
            item.append(", pkColumnName='").append(pkColumnName).append('\'');
        }
        if (StringUtils.isNotEmpty(fkTableName)) {
            item.append(", fkTableName='").append(fkTableName).append('\'');
        }
        if (StringUtils.isNotEmpty(fkColumnName)) {
            item.append(", fkColumnName='").append(fkColumnName).append('\'');
        }
        if (keySequence != null) {
            item.append(", keySequence=").append(keySequence).append('\'');
        }

        item.append('}');
        int position = item.indexOf("{") + 1;
        if (StringUtils.startsWith(item.substring(position), ",")) {
            item.replace(position, position + 2, "");
        }
        return item.toString();
    }

    @Override
    public int compare(Object o1, Object o2) {
        if (o1 instanceof RefKeyMeta && o2 instanceof RefKeyMeta) {
            if (o1.equals(o2)) return 0;
            else {
                RefKeyMeta one = RefKeyMeta.class.cast(o1);
                RefKeyMeta another = RefKeyMeta.class.cast(o2);
                int result = one.getPkTableName().compareTo(another.getPkTableName());
                if (result != 0) return result;
                else {
                    int temp = one.getFkTableName().compareTo(another.getFkTableName());
                    if (temp != 0) return temp;
                    else {
                        return one.getFkColumnName().compareTo(another.getFkColumnName());
                    }
                }
            }
        }
        throw new IncompatibleClassChangeError(
            o1.getClass().toGenericString() + " vs " + o2.getClass().toGenericString());
    }

    public boolean equals(Object obj) {
        if (obj instanceof RefKeyMeta) {
            RefKeyMeta meta = this.getClass().cast(obj);
            return getPkTableName().equals(meta.getPkTableName()) && getPkColumnName().equals(meta.getPkColumnName())
                && getFkTableName().equals(meta.getFkTableName()) && getFkColumnName().equals(meta.getFkColumnName())
                && getKeySequence().shortValue() == meta.getKeySequence().shortValue();
        } else {
            return false;
        }
    }
}
