package com.ipacs.als.common;

/**
 * <p>Project: JavaFX</p>
 * <p>Description: sample</p>
 * <p>Copyright: Copyright (c) 2014</p>
 * <p>Company: MIT Co., Ltd</p>
 *
 * @author <chao.deng@kewill.com>
 * @version 1.0
 * @date 7/8/14
 */
public class QueueMeta {

    private String hostName;
    private String port;
    private String queueManagerName;
    private String channelName;
    private String requestQueueName;
    private String message;
    private Long interim;

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        if (hostName != null && hostName.trim().length() > 0) {
            hostName = hostName.trim();
        }
        this.hostName = hostName;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        String temp = "1414";
        if (port != null && port.trim().length() > 0) {
            port = port.trim();
            temp = port;
        }
        this.port = temp;
    }

    public String getQueueManagerName() {
        return queueManagerName;
    }

    public void setQueueManagerName(String queueManagerName) {
        if (queueManagerName != null && queueManagerName.trim().length() > 0) {
            queueManagerName = queueManagerName.trim();
        }
        this.queueManagerName = queueManagerName;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        if (channelName != null && channelName.trim().length() > 0) {
            channelName = channelName.trim();
        }
        this.channelName = channelName;
    }

    public String getRequestQueueName() {
        return requestQueueName;
    }

    public void setRequestQueueName(String requestQueueName) {
        if (requestQueueName != null && requestQueueName.trim().length() > 0) {
            requestQueueName = requestQueueName.trim();
        }
        this.requestQueueName = requestQueueName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        if (message != null && message.trim().length() > 0) {
            message = message.trim();
        }
        this.message = message;
    }

    public Long getInterim() {
        return interim;
    }

    public void setInterim(String interim) {
        Long temp = 10000L;
        if (interim != null && interim.trim().length() > 0) {
            interim = interim.trim();
            temp = Long.valueOf(interim);
        }
        this.interim = temp;
    }
}
