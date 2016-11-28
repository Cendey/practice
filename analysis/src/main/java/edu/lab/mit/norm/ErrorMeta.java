package edu.lab.mit.norm;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.Date;

/**
 * <p>Project: KEWILL FORWARD ENTERPRISE</p>
 * <p>File: edu.lab.mit.norm.ErrorMeta</p>
 * <p>Copyright: Copyright � 2015 Kewill Co., Ltd. All Rights Reserved.</p>
 * <p>Company: Kewill Co., Ltd</p>
 *
 * @author <chao.deng@kewill.com>
 * @version 1.0
 * @since 8/7/2015
 */
public class ErrorMeta {

    private SimpleIntegerProperty sNo;
    private SimpleStringProperty currDate;
    private SimpleStringProperty md5;
    private SimpleStringProperty detail;

    public ErrorMeta(Integer sNo, String currDate, String md5, String detail) {
        this.sNo = new SimpleIntegerProperty(sNo);
        this.currDate = new SimpleStringProperty(currDate);
        this.md5 = new SimpleStringProperty(md5);
        this.detail = new SimpleStringProperty(detail);
    }

    public int getsNo() {
        return sNo.get();
    }

    public SimpleIntegerProperty sNoProperty() {
        return sNo;
    }

    public void setsNo(int sNo) {
        this.sNo.set(sNo);
    }

    public String getCurrDate() {
        return currDate.get();
    }

    public SimpleStringProperty currDateProperty() {
        return currDate;
    }

    public void setCurrDate(String currDate) {
        this.currDate.set(currDate);
    }

    public String getMd5() {
        return md5.get();
    }

    public SimpleStringProperty md5Property() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5.set(md5);
    }

    public String getDetail() {
        return detail.get();
    }

    public SimpleStringProperty detailProperty() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail.set(detail);
    }
}
