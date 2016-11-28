package com.kewill.queue;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;
import java.util.UUID;

/**
 * <p>Project: Queue</p>
 * <p>Description: edu.center.practice.queue.dynamic</p>
 * <p>Copyright: Copyright (c) 2014</p>
 * <p>Company: MIT Co., Ltd</p>
 *
 * @author <chao.deng@kewill.com>
 * @version 1.0
 * @date 7/8/14
 */
public class DQFileRequest {

    private QueueConnection _queueConnection = null;
    private QueueSession _queueSession = null;
    private Queue _requestQ = null;

    public DQFileRequest(QueueMeta meta) {
        try {
            Properties env = new Properties();
            env.put(
                "java.naming.provider.url", meta.getHostName() + ":" + meta.getPort() + "/" + meta.getChannelName());
            env.put("java.naming.factory.initial", "com.ibm.mq.jms.context.WMQInitialContextFactory");

            Context ctx = new InitialContext(env);
            QueueConnectionFactory queueConnectionFactory =
                QueueConnectionFactory.class.cast(ctx.lookup(meta.getQueueManagerName()));
            _queueConnection = queueConnectionFactory.createQueueConnection();
            _queueSession = _queueConnection.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
            _requestQ = Queue.class.cast(ctx.lookup(meta.getRequestQueueName()));
            _queueConnection.start();
        } catch (NamingException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (JMSException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void sendGeneralRequest(QueueMeta meta) {
        try {
            TextMessage msg = _queueSession.createTextMessage();
            UUID uuid = UUID.randomUUID();
            String uniqueId = uuid.toString();
            msg.setStringProperty("UUID", uniqueId);
            msg.setText(meta.getMessage());
            Queue responseQ = Queue.class.cast(_queueSession.createTemporaryQueue());
            msg.setJMSReplyTo(responseQ);

            QueueSender queueSender = _queueSession.createSender(_requestQ);
            queueSender.send(msg);
            Thread.sleep(meta.getInterim());
            String filter = "JMSCorrelationID = '" + uniqueId + "'";
            QueueReceiver receiver = _queueSession.createReceiver(responseQ,filter);
            Message content = Message.class.cast(receiver.receive(30000));
            if (content == null) {
                System.out.println("Query not responding!");
            } else {
                if (content instanceof TextMessage) {
                    System.out.println(TextMessage.class.cast(content).getText());
                } else {
                    throw new javax.jms.IllegalStateException("Invalid message type!");
                }
            }
        } catch (JMSException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void exit() {
        try {
            _queueConnection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    @SuppressWarnings(value = {"InfiniteLoopStatement"})
    public static void start(QueueMeta meta) {
        DQFileRequest request= new DQFileRequest(meta);
        if (meta == null || meta.getMessage().trim().length() <= 0) {
            request.exit();
        }
        request.sendGeneralRequest(meta);
    }
}
