package com.ipacs.als.server;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Properties;

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
public class DQFileReply implements MessageListener {

    private QueueConnection _queueConnection = null;
    private QueueSession _queueSession = null;

    private DQFileReply(String queueManager, String requestQueue) {
        Properties env = new Properties();
        try {
            // Connect to the provider and get the JMS connection
            env.load(new FileInputStream("src/config/jndi.properties"));
            Context ctx = new InitialContext(env);
            QueueConnectionFactory qFactory = QueueConnectionFactory.class.cast(ctx.lookup(queueManager));
            _queueConnection = qFactory.createQueueConnection();

            // Create the JMS Session
            _queueSession = _queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

            // Lookup the request queue
            Queue requestQ = Queue.class.cast(ctx.lookup(requestQueue));

            // Now that setup is complete, start the Connection
            _queueConnection.start();

            // Create the message listener
            QueueReceiver qReceiver = _queueSession.createReceiver(requestQ);
            qReceiver.setMessageListener(this);
            System.out.println("Waiting for loan requests...");
        } catch (JMSException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (NamingException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void onMessage(Message message) {
        try {
            // Get the data from the message
            if (message instanceof TextMessage) {
                TextMessage msg = TextMessage.class.cast(message);
                String content = msg.getText();

                StringBuilder delimiters = new StringBuilder();
                addDelimiterToInfo(content, delimiters);
                System.out.println(delimiters);
                System.out.println(content);
                System.out.println(delimiters);

                // Send the results back to the borrower
                TextMessage textMessage = _queueSession.createTextMessage();
                textMessage.setText("Reply on " + new Date() + "!\n" + content);
                textMessage.setJMSCorrelationID(message.getStringProperty("UUID"));

                // Create the sender and send the message
                Queue responseQ = Queue.class.cast(message.getJMSReplyTo());
                if (responseQ != null) {
                    QueueSender queueSender = _queueSession.createSender(responseQ);
                    queueSender.send(textMessage);
                    System.out.println("\nWaiting for requests...");
                    Thread.sleep(1000);
                }
            } else {
                throw new IllegalArgumentException("Unsupported message type!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void addDelimiterToInfo(String detailInfo, StringBuilder delimiters) {
        for (
            int count = detailInfo.length(); count > 0; count--) {
            delimiters.append("#");
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

    public static void main(String[] args) {
        String queueManager = null;
        String requestQ = null;
        if (args.length == 2) {
            queueManager = args[0];
            requestQ = args[1];
        } else {
            System.out.println("Invalid arguments. Should be: ");
            System.out.println("Java DQFileReply Factory && Request Queue");
            System.exit(0);
        }
        DQFileReply lender = new DQFileReply(queueManager, requestQ);
        try {
            // Run until enter is pressed
            BufferedReader stdin = new BufferedReader
                (new InputStreamReader(System.in));
            System.out.println("DQFileReply application started");
            System.out.println("Press enter to quit application");
            stdin.readLine();
            lender.exit();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
