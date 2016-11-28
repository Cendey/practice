package com.ipacs.als.client;

import com.ipacs.als.server.DQFileRequest;
import com.ipacs.als.common.QueueMeta;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * <p>Project: JavaFX</p>
 * <p>Description: edu.center.als</p>
 * <p>Copyright: Copyright (c) 2014</p>
 * <p>Company: MIT Co., Ltd</p>
 *
 * @author <chao.deng@kewill.com>
 * @version 1.0
 * @date 7/12/14
 */
public class MQController {

    private QueueMeta meta;
    public TextField fileName;
    public TextField hostName;
    public TextField port;
    public TextField managerName;
    public TextField channelName;
    public TextField requestQueueName;
    public TextField interval;
    public Button chooseFile;
    public TextArea content;
    public Button inputMessage;
    public Button exit;

    private boolean passCheck(QueueMeta meta) {
        return meta.getHostName() != null && meta.getChannelName() != null && meta.getQueueManagerName() != null
                && meta.getRequestQueueName() != null && meta.getMessage() != null;
    }

    private String getFilePath() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML Files", "*.xml"),
                new FileChooser.ExtensionFilter("CSV Files", "*.csv"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));

        String userDirectoryString = System.getProperty("user.home");
        File userDirectory = new File(userDirectoryString);
        if (!userDirectory.canRead()) {
            userDirectory = new File("c:/");
        }
        fileChooser.setInitialDirectory(userDirectory);
        File chosenFile = fileChooser.showOpenDialog(null);
        String path = null;
        if (chosenFile != null) {
            path = chosenFile.getPath();
        }
        return path;
    }

    public void handleChooseFileButtonAction() {
        fileName.setText(getFilePath());
    }

    public void handlePutMessageButtonAction() {
        collectMQInfo();
        if (fileName.getText().trim().length() > 0) {
            String temp = String.valueOf(fileName.getText());
            try {
                BufferedReader reader = new BufferedReader(
                        new FileReader(temp.replaceAll("\\\\|/", "\\" + System.getProperty("file.separator"))));
                String line = reader.readLine();
                StringBuilder content = new StringBuilder();
                while (line != null) {
                    content.append(line).append("\n");
                    line = reader.readLine();
                }
                meta.setMessage(content.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            String message = content.getText();
            if (message != null && message.trim().length() > 0 && !"Available for a little message!".equals(
                    message)) {
                meta.setMessage(content.getText());
            }
        }
        if (passCheck(meta)) {
            DQFileRequest.start(meta);
        }
    }

    private void collectMQInfo() {
        meta = new QueueMeta();
        meta.setHostName(hostName.getText());
        meta.setPort(port.getText());
        meta.setQueueManagerName(managerName.getText());
        meta.setChannelName(channelName.getText());
        meta.setRequestQueueName(requestQueueName.getText());
        meta.setInterim(interval.getText());
    }

    public void handleExitButtonAction() {
        System.exit(0);
    }
}
