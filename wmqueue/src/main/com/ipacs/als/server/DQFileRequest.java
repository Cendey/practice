package com.ipacs.als.server;

import com.ipacs.als.common.QueueMeta;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
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

    private DQFileRequest(QueueMeta meta) {
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
            Message content = Message.class.cast(receiver.receive(3000));
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
        DQFileRequest borrower = new DQFileRequest(meta);
        if (meta == null || meta.getMessage().trim().length() <= 0) {
            borrower.exit();
        }
        borrower.sendGeneralRequest(meta);
    }
}
