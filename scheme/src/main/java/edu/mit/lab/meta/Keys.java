package edu.mit.lab.meta;

import edu.mit.lab.constant.Scheme;
import edu.mit.lab.infts.IRelevance;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * <p>Title: Kewill Lab Center</p>
 * <p>Description: edu.mit.lab.meta.Keys</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Kewill Co., Ltd.</p>
 *
 * @author <chao.deng@kewill.com>
 * @version 1.0
 * @since 11/14/2016
 */
public class Keys implements IRelevance<String, List<String>>, Comparator, Serializable {

    private String pkTableName;
    private List<String> pkColumnName;
    private String fkTableName;
    private List<String> fkColumnName;
    private Short keySequence;

    public String getPkTableName() {
        return pkTableName;
    }

    public void setPkTableName(String pkTableName) {
        this.pkTableName = pkTableName;
    }

    private List<String> getPkColumnName() {
        return pkColumnName;
    }

    public void addPkColumnName(String pkColumnName) {
        if (this.pkColumnName == null) {
            this.pkColumnName = new ArrayList<>();
        }
        this.pkColumnName.add(pkColumnName);
    }

    public String getFkTableName() {
        return fkTableName;
    }

    public void setFkTableName(String fkTableName) {
        this.fkTableName = fkTableName;
    }

    private List<String> getFkColumnName() {
        return fkColumnName;
    }

    public void addFkColumnName(String fkColumnName) {
        if (this.fkColumnName == null) {
            this.fkColumnName = new ArrayList<>();
        }
        this.fkColumnName.add(fkColumnName);
    }

    private Short getKeySequence() {
        return keySequence;
    }

    public void setKeySequence(Short keySequence) {
        this.keySequence = keySequence;
    }

    @Override
    public String toString() {
        StringBuilder item = new StringBuilder("Keys{");
        if (StringUtils.isNotEmpty(pkTableName)) {
            item.append(Scheme.PK_TABLE_NAME + "='").append(pkTableName).append('\'');
        }
        if (CollectionUtils.isNotEmpty(pkColumnName)) {
            formatColumnName(item, pkColumnName, Scheme.PK_COLUMN_NAME);
        }
        if (StringUtils.isNotEmpty(fkTableName)) {
            item.append(", ").append(Scheme.FK_TABLE_NAME).append("='").append(fkTableName).append('\'');
        }
        if (CollectionUtils.isNotEmpty(fkColumnName)) {
            formatColumnName(item, fkColumnName, Scheme.FK_COLUMN_NAME);
        }
        if (keySequence != null) {
            item.append(", ").append(Scheme.KEY_SEQUENCE).append("='").append(keySequence).append('\'');
        }

        item.append('}');
        int position = item.indexOf("{") + 1;
        if (StringUtils.startsWith(item.substring(position), ",")) {
            item.replace(position, position + 2, "");
        }
        return item.toString();
    }

    private void formatColumnName(StringBuilder item, List<String> lstColumnNames, String columnName) {
        item.append(", ").append(columnName).append("=");
        if (lstColumnNames.size() == 1) {
            item.append("'").append(lstColumnNames.get(0)).append("'");
        } else {
            item.append("{'").append(lstColumnNames.get(0));
            for (int index = 1; index < lstColumnNames.size(); index++) {
                item.append("', '").append(lstColumnNames.get(index));
            }
            item.append("'}");
        }
    }

    @Override
    public int compare(Object o1, Object o2) {
        if (o1 instanceof Keys && o2 instanceof Keys) {
            if (o1.equals(o2)) return 0;
            else {
                Keys one = Keys.class.cast(o1);
                Keys another = Keys.class.cast(o2);
                int result = one.getPkTableName().compareTo(another.getPkTableName());
                if (result != 0) return result;
                else {
                    int temp = one.getFkTableName().compareTo(another.getFkTableName());
                    if (temp != 0) return temp;
                    else {
                        return String.valueOf(one.getFkColumnName())
                            .compareTo(String.valueOf(another.getFkColumnName()));
                    }
                }
            }
        }
        throw new IncompatibleClassChangeError(
            o1.getClass().toGenericString() + " vs " + o2.getClass().toGenericString());
    }

    @Override
    public int hashCode() {
        return getPkTableName().hashCode() * 3 + getPkColumnName().hashCode() * 7 + getFkTableName().hashCode() * 11
            + getFkColumnName().hashCode() * 13 + getKeySequence() * 17;
    }


    public boolean equals(Object obj) {
        if (obj instanceof Keys) {
            Keys meta = this.getClass().cast(obj);
            return getPkTableName().equals(meta.getPkTableName()) && getPkColumnName().equals(meta.getPkColumnName())
                && getFkTableName().equals(meta.getFkTableName()) && getFkColumnName().equals(meta.getFkColumnName())
                && getKeySequence().shortValue() == meta.getKeySequence().shortValue();
        } else {
            return false;
        }
    }

    @Override
    public String from() {
        return fkTableName;
    }

    @Override
    public String to() {
        return pkTableName;
    }

    @Override
    @SuppressWarnings(value = {"unchecked"})
    public List<String> attribute(String key) {
        List<String> attribute = new ArrayList<>();
        try {
            Method reader = getClass().getMethod("get" + StringUtils.upperCase(key.substring(0, 1)) + key.substring(1));
            Object value = reader.invoke(this);
            if (value != null) {
                if (Collection.class.isAssignableFrom(value.getClass())) {
                    attribute.addAll(Collection.class.cast(value));
                } else {
                    attribute.add(String.valueOf(value));
                }
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            System.err.println(e.getMessage());
        }
        return attribute;
    }
}
