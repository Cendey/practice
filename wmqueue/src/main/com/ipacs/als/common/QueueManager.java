package com.ipacs.als.common;

import com.ibm.mq.MQException;
import com.ibm.mq.constants.MQConstants;
import com.ibm.mq.pcf.PCFMessage;
import com.ibm.mq.pcf.PCFMessageAgent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>Title: DQueue</p>
 * <p>Description: com.ipacs.als.common.QueueManager</p>
 * <p>Copyright: Copyright (c) 2014</p>
 * <p>Company: Kewill Co., Ltd</p>
 *
 * @author <chao.deng@kewill.com>
 * @version 1.0
 * @date 7/17/2014
 */
public class QueueManager {

    private List<String> lstManagerName;
    private List<String> lstQueueName;
    private List<String> lstChannelName;

    private List<String> getLstManagerName() {
        return lstManagerName;
    }

    private void setLstManagerName(List<String> lstManagerName) {
        if (lstManagerName != null && lstManagerName.size() > 0) {
            if (this.lstManagerName == null) {
                this.lstManagerName = new ArrayList<String>();
            } else {
                this.lstManagerName.clear();
            }
            this.lstManagerName.addAll(lstManagerName);
        }
    }

    private List<String> getLstQueueName() {
        return lstQueueName;
    }

    private void setLstQueueName(List<String> lstQueueName) {
        if (lstQueueName != null && lstQueueName.size() > 0) {
            if (this.lstQueueName == null) {
                this.lstQueueName = new ArrayList<String>();
            } else {
                this.lstQueueName.clear();
            }
            this.lstQueueName.addAll(lstQueueName);
        }
    }

    private List<String> getLstChannelName() {
        return lstChannelName;
    }

    private void setLstChannelName(List<String> lstChannelName) {
        if (lstChannelName != null && lstChannelName.size() > 0) {
            if (this.lstChannelName == null) {
                this.lstChannelName = new ArrayList<String>();
            } else {
                this.lstChannelName.clear();
            }
            this.lstChannelName.addAll(lstChannelName);
        }
    }

/*
    public List<String> retrieveQueueManagerInfo(QueueMeta meta) {
        PCFMessageAgent agent = null;
        List<String> lstManagerName = null;
        try {
            int[] pcfParamAttributes = {MQConstants.MQIACF_ALL};
            PCFParameter[] pcfParameters = {new MQCFIL(MQConstants.MQIACF_Q_MGR_ATTRS, pcfParamAttributes)};

            // Connect to qmgr
            agent = new PCFMessageAgent();
            agent.connect(meta.getHostName(), Integer.valueOf(meta.getPort()), meta.getChannelName());

            // Send the INQUIRE_Q_STATUS PCF request
            MQMessage[] mqResponse = agent.send(MQConstants.MQCMD_INQUIRE_Q_MGR, pcfParameters);

            // Print the INQUIRE_Q_STATUS PCF response(s)
            lstManagerName = new ArrayList<String>();
            PCFParameter pcfParam;

            for (MQMessage item : mqResponse) {
                MQCFH mqCFH = new MQCFH(item);
                for (int index = 0; index < mqCFH.getParameterCount(); index++) {
                    pcfParam = PCFParameter.nextParameter(item);
                    if ("MQCA_Q_MGR_NAME".equals(pcfParam.getParameterName())) {
                        String managerNames = pcfParam.getStringValue();
                        if (managerNames != null && managerNames.length() > 0) {
                            lstManagerName.add(managerNames);
                        }
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (agent != null) {
                try {
                    agent.disconnect();
                } catch (MQException ignored) {
                    ignored.printStackTrace();
                }
            }
        }
        return lstManagerName;
    }
*/

    private List<String> retrieveQueueManagerInfo(QueueMeta meta) {
        PCFMessageAgent agent = null;
        List<String> lstManagerName = null;
        try {
            PCFMessage pcfCmd = new PCFMessage(MQConstants.MQCMD_INQUIRE_Q_MGR_STATUS);
            pcfCmd.addParameter(MQConstants.MQIACF_Q_MGR_STATUS_ATTRS, new int[]{MQConstants.MQIACF_ALL});

            // Connect to queue manager
            agent = new PCFMessageAgent();
            agent.connect(meta.getHostName(), Integer.valueOf(meta.getPort()), meta.getChannelName());

            // Send the INQUIRE_Q_STATUS PCF request
            PCFMessage[] mqResponse = agent.send(pcfCmd);

            // Print the INQUIRE_Q_STATUS PCF response(s)
            lstManagerName = new ArrayList<String>();
            for (PCFMessage item : mqResponse) {
                String managerNames = (String) item.getParameterValue(MQConstants.MQCA_Q_MGR_NAME);
                if (managerNames != null && managerNames.trim().length() > 0) {
                    lstManagerName.add(managerNames.trim());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (agent != null) {
                try {
                    agent.disconnect();
                } catch (MQException ignored) {
                    ignored.printStackTrace();
                }
            }
        }
        return lstManagerName;
    }

//    public List<String> retrieveQueueInfo(QueueMeta meta) {
//        PCFMessageAgent agent = null;
//        List<String> lstQueueName = null;
//        try {
//            // Create INQUIRE_Q_STATUS PCF message
//            PCFMessage pcfCmd = new PCFMessage(MQConstants.MQCMD_INQUIRE_Q_NAMES);
//
//            // Fixed & working code
//            pcfCmd.addParameter(MQConstants.MQCA_Q_NAME, "*");
//            pcfCmd.addParameter(MQConstants.MQIA_Q_TYPE, MQConstants.MQQT_LOCAL);
////            inquireQueueStatus.addParameter(MQConstants.MQIACF_CHANNEL_ATTRS, new int[]{MQConstants.MQIACF_ALL});
//
//            // Connect to qmgr
//            agent = new PCFMessageAgent();
//            agent.connect(meta.getHostName(), Integer.valueOf(meta.getPort()), meta.getChannelName());
//
//            // Send the INQUIRE_Q_STATUS PCF request
//            PCFMessage[] pcfResponse = agent.send(pcfCmd);
//
//            // Print the INQUIRE_Q_STATUS PCF response(s)
//
//            lstQueueName = new ArrayList<String>();
//            for (PCFMessage item : pcfResponse) {
//                String[] queueNames = (String[]) item.getParameterValue(MQConstants.MQCACF_Q_NAMES);
//                if (queueNames != null && queueNames.length > 0) {
//                    lstQueueName.addAll(Arrays.asList(queueNames));
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (agent != null) {
//                try {
//                    agent.disconnect();
//                } catch (MQException ignored) {
//                    ignored.printStackTrace();
//                }
//            }
//        }
//        return lstQueueName;
//    }

    private List<String> retrieveQueueInfo(QueueMeta meta) {
        PCFMessageAgent agent = null;
        List<String> lstQueueName = null;
        try {
            // Create INQUIRE_Q_STATUS PCF message
            PCFMessage pcfCmd = new PCFMessage(MQConstants.MQCMD_INQUIRE_Q_STATUS);

            // Fixed & working code
            pcfCmd.addParameter(MQConstants.MQCA_Q_NAME, "*");
            pcfCmd.addParameter(MQConstants.MQIA_Q_TYPE, MQConstants.MQQT_LOCAL);
            pcfCmd.addParameter(MQConstants.MQIA_SCOPE, MQConstants.MQSCOPE_QMGR);
            pcfCmd.addParameter(MQConstants.MQIA_QSG_DISP, MQConstants.MQQSGD_PRIVATE);
            pcfCmd.addParameter(MQConstants.MQIACF_Q_ATTRS, new int[]{MQConstants.MQCA_CUSTOM});

            // Connect to queue manager
            agent = new PCFMessageAgent();
            agent.connect(meta.getHostName(), Integer.valueOf(meta.getPort()), meta.getChannelName());

            // Send the INQUIRE_Q_STATUS PCF request
            PCFMessage[] pcfResponse = agent.send(pcfCmd);

            // Print the INQUIRE_Q_STATUS PCF response(s)

            lstQueueName = new ArrayList<String>();
            for (PCFMessage item : pcfResponse) {
                String queueName = (String) item.getParameterValue(MQConstants.MQCA_Q_NAME);
                if (queueName != null && !queueName.startsWith("SYSTEM") && queueName.trim().length() > 0) {
                    lstQueueName.add(queueName.trim());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (agent != null) {
                try {
                    agent.disconnect();
                } catch (MQException ignored) {
                    ignored.printStackTrace();
                }
            }
        }
        return lstQueueName;
    }

//    public List<String> retrieveChannelInfo(QueueMeta meta) {
//        PCFMessageAgent agent = null;
//        List<String> lstChannelName = null;
//        try {
//            // Create MQCMD_INQUIRE_CHANNEL_NAMES PCF message
//            PCFMessage pcfCmd = new PCFMessage(MQConstants.MQCMD_INQUIRE_CHANNEL_NAMES);
//
//            // Fixed & working code
//
//            pcfCmd.addParameter(MQConstants.MQCACH_CHANNEL_NAME, "*");
//            pcfCmd.addParameter(MQConstants.MQIACH_CHANNEL_TYPE, MQConstants.MQCHT_SVRCONN);
//
//            // Connect to qmgr
//            agent = new PCFMessageAgent();
//            agent.connect(meta.getHostName(), Integer.valueOf(meta.getPort()), meta.getChannelName());
//
//            // Send the MQCMD_INQUIRE_CHANNEL_NAMES PCF request
//            PCFMessage[] pcfResponse = agent.send(pcfCmd);
//
//            // Print the MQCMD_INQUIRE_CHANNEL_NAMES PCF response(s)
//            lstChannelName = new ArrayList<String>();
//            for (PCFMessage item : pcfResponse) {
//                String[] channelNames = (String[]) item.getParameterValue(MQConstants.MQCACH_CHANNEL_NAMES);
//                if (channelNames != null && channelNames.length > 0) {
//                    lstChannelName.addAll(Arrays.asList(channelNames));
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (agent != null) {
//                try {
//                    agent.disconnect();
//                } catch (MQException ignored) {
//                    ignored.printStackTrace();
//                }
//            }
//        }
//        return lstChannelName;
//    }

    private List<String> retrieveChannelInfo(QueueMeta meta) {
        PCFMessageAgent agent = null;
        List<String> lstChannelName = null;
        try {
            // Create MQCMD_INQUIRE_CHANNEL_NAMES PCF message
            PCFMessage pcfCmd = new PCFMessage(MQConstants.MQCMD_INQUIRE_CHANNEL_STATUS);

            // Fixed & working code
            pcfCmd.addParameter(MQConstants.MQCACH_CHANNEL_NAME, "*");

            // Connect to queue manager
            agent = new PCFMessageAgent();
            agent.connect(meta.getHostName(), Integer.valueOf(meta.getPort()), meta.getChannelName());

            // Send the MQCMD_INQUIRE_CHANNEL_NAMES PCF request
            PCFMessage[] pcfResponse = agent.send(pcfCmd);

            // Print the MQCMD_INQUIRE_CHANNEL_NAMES PCF response(s)
            lstChannelName = new ArrayList<String>();
            for (PCFMessage item : pcfResponse) {
                String channelName = (String) item.getParameterValue(MQConstants.MQCACH_CHANNEL_NAME);
                if (channelName != null && channelName.trim().length() > 0) {
                    lstChannelName.addAll(Collections.singletonList(channelName.trim()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (agent != null) {
                try {
                    agent.disconnect();
                } catch (MQException ignored) {
                    ignored.printStackTrace();
                }
            }
        }
        return lstChannelName;
    }

    public static void main(String[] args) {
        QueueMeta meta = new QueueMeta();
        meta.setHostName("10.104.46.83");
        meta.setQueueManagerName("WMQ502");
        meta.setPort("1414");
        meta.setChannelName("F5SERVER.TCP");

        QueueManager queueManager = new QueueManager();
        queueManager.setLstManagerName(queueManager.retrieveQueueManagerInfo(meta));
        System.out.println("Queue Manager Name(s):");
        System.out.println(queueManager.getLstManagerName());

        queueManager.setLstQueueName(queueManager.retrieveQueueInfo(meta));
        System.out.println("Queue Name(s):");
        System.out.println(queueManager.getLstQueueName());

        queueManager.setLstChannelName(queueManager.retrieveChannelInfo(meta));
        System.out.println("Channel Name(s):");
        System.out.println(queueManager.getLstChannelName());
    }
}
