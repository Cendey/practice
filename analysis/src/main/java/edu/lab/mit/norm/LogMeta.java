package edu.lab.mit.norm;

import javafx.beans.property.SimpleStringProperty;

/**
 * <p>Project: KEWILL FORWARD ENTERPRISE</p>
 * <p>File: edu.lab.mit.norm.LogMeta</p>
 * <p>Copyright: Copyright � 2015 Kewill Co., Ltd. All Rights Reserved.</p>
 * <p>Company: Kewill Co., Ltd</p>
 *
 * @author <chao.deng@kewill.com>
 * @version 1.0
 * @since 7/30/2015
 */
public class LogMeta {
    private static LogMeta instance;

    private SimpleStringProperty logInfo;

    private StringBuilder buffer;

    private LogMeta() {
        logInfo = new SimpleStringProperty("");
        buffer = new StringBuilder();
    }

    public String getLogInfo() {
        return logInfo.get();
    }

    public SimpleStringProperty logInfoProperty() {
        return logInfo;
    }

    public void appendLog(String logInfo) {
        buffer.append(logInfo).append("\r\n");
        this.logInfo.set(buffer.toString());
    }

    public void clearLog() {
        buffer.delete(0, buffer.length());
        logInfo.set(null);
    }
    public static LogMeta getInstance() {
        if (instance == null) {
            instance = new LogMeta();
        }
        return instance;
    }
}
