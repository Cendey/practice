package edu.lab.mit.norm;

import javafx.beans.property.SimpleStringProperty;

/**
 * <p>Project: KEWILL FORWARD ENTERPRISE</p>
 * <p>File: edu.lab.mit.norm.Criterion</p>
 * <p>Copyright: Copyright � 2015 Kewill Co., Ltd. All Rights Reserved.</p>
 * <p>Company: Kewill Co., Ltd</p>
 *
 * @author <chao.deng@kewill.com>
 * @version 1.0
 * @since 7/29/2015
 */
public class Criterion {

    private SimpleStringProperty errorStartID;
    private SimpleStringProperty errorEndID;

    private SimpleStringProperty userID;

    private SimpleStringProperty sourceFilePath;
    private SimpleStringProperty targetFilePath;

    public Criterion() {
        this("ERROR", "yyyy-MM-dd HH:mm:ss", "", "", "ROY");
    }

    public Criterion(
        String errorStartID, String errorEndID, String sourceFilePath, String targetFilePath, String userID) {
        this.errorStartID = new SimpleStringProperty(errorStartID);
        this.errorEndID = new SimpleStringProperty(errorEndID);
        this.sourceFilePath = new SimpleStringProperty(sourceFilePath);
        this.targetFilePath = new SimpleStringProperty(targetFilePath);
        this.userID = new SimpleStringProperty(userID);
    }

    public String getErrorStartID() {
        return errorStartID.get();
    }

    public SimpleStringProperty errorStartIDProperty() {
        return errorStartID;
    }

    public void setErrorStartID(String errorStartID) {
        this.errorStartID.set(errorStartID);
    }

    public String getErrorEndID() {
        return errorEndID.get();
    }

    public SimpleStringProperty errorEndIDProperty() {
        return errorEndID;
    }

    public void setErrorEndID(String errorEndID) {
        this.errorEndID.set(errorEndID);
    }

    public String getUserID() {
        return userID.get();
    }

    public SimpleStringProperty userIDProperty() {
        return userID;
    }

    void setUserID(String userID) {
        this.userID.set(userID);
    }

    public String getSourceFilePath() {
        return sourceFilePath.get();
    }

    public SimpleStringProperty sourceFilePathProperty() {
        return sourceFilePath;
    }

    public void setSourceFilePath(String sourceFilePath) {
        this.sourceFilePath.set(sourceFilePath);
    }

    public String getTargetFilePath() {
        return targetFilePath.get();
    }

    public SimpleStringProperty targetFilePathProperty() {
        return targetFilePath;
    }

    public void setTargetFilePath(String targetFilePath) {
        this.targetFilePath.set(targetFilePath);
    }
}
