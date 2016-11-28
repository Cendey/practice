package com.kewill.queue;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(final Stage primaryStage) throws Exception {
        primaryStage.setTitle("WebSphere MQ Information");
        Image icon = new Image(getClass().getResourceAsStream("ibm.jpg"));
        primaryStage.getIcons().add(icon);
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.BASELINE_CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(20);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(40);
        ColumnConstraints column3 = new ColumnConstraints();
        column1.setPercentWidth(20);
        ColumnConstraints column4 = new ColumnConstraints();
        column2.setPercentWidth(20);
        grid.getColumnConstraints().addAll(column1, column2, column3, column4);

        Scene scene = new Scene(grid, 700, 500);
        primaryStage.setScene(scene);
        Text sceneTitle = new Text("Essential Arguments For Running Dynamic Queue");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 12));
        grid.add(sceneTitle, 0, 0, 1, 1);

        Label lblHostName = new Label("Host Name:");
        grid.add(lblHostName, 0, 1);
        final TextField txtHostName = new TextField();
        grid.add(txtHostName, 1, 1, 9, 1);

        Label lblPort = new Label("Port:");
        grid.add(lblPort, 10, 1);
        final TextField txtPort = new TextField();
        grid.add(txtPort, 11, 1);

        Label lblQueueManagerName = new Label("Queue Manager Name:");
        grid.add(lblQueueManagerName, 0, 2, 1, 1);
        final TextField txtQueueManagerName = new TextField();
        grid.add(txtQueueManagerName, 1, 2, 11, 1);

        Label lblChannelName = new Label("Channel Name:");
        grid.add(lblChannelName, 0, 3, 1, 1);
        final TextField txtChannelName = new TextField();
        grid.add(txtChannelName, 1, 3, 11, 1);

        Label lblRequestQueueName = new Label("Request Queue Name:");
        grid.add(lblRequestQueueName, 0, 4, 1, 1);
        final TextField txtRequestQueueName = new TextField();
        grid.add(txtRequestQueueName, 1, 4, 11, 1);

        Label fileName = new Label("File:");
        grid.add(fileName, 0, 5);
        final TextField filePath = new TextField();
        grid.add(filePath, 1, 5, 10, 1);
        ImageView openFileImage = new ImageView(new Image(getClass().getResourceAsStream("openfile.jpg")));
        openFileImage.setFitHeight(20);
        openFileImage.setFitWidth(20);
        Button fileBtn = new Button("Open File", openFileImage);
        HBox hBox = new HBox(10);
        hBox.setAlignment(Pos.BOTTOM_RIGHT);
        hBox.getChildren().add(fileBtn);
        grid.add(hBox, 11, 5);
        fileBtn.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    filePath.setText(getFilePath());
                }
            });
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");

        final TextArea txtMsg = new TextArea("Available for a little message!");
        grid.add(txtMsg, 0, 6, 12, 5);

        Label lblInterim = new Label("Interval:");
        grid.add(lblInterim, 0, 12, 1, 1);
        final TextField txtInterim = new TextField();
        grid.add(txtInterim, 1, 12, 2, 1);
        Label lblUnit = new Label("(ms)");
        grid.add(lblUnit, 3, 12, 1, 1);

        Button btnInput = new Button();
        btnInput.setText("Input Message");
        btnInput.setFont(Font.font("Aria", FontWeight.BOLD, 12));
        HBox inputBox = new HBox(10);
        inputBox.setAlignment(Pos.BOTTOM_RIGHT);
        inputBox.getChildren().add(btnInput);
        grid.add(inputBox, 11, 12);
        btnInput.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    QueueMeta meta = new QueueMeta();
                    meta.setHostName(txtHostName.getText());
                    meta.setPort(txtPort.getText());
                    meta.setQueueManagerName(txtQueueManagerName.getText());
                    meta.setChannelName(txtChannelName.getText());
                    meta.setRequestQueueName(txtRequestQueueName.getText());
                    meta.setInterim(txtInterim.getText());
                    if (filePath.getText().trim().length() > 0) {
                        String temp = String.valueOf(filePath.getText());
                        try {
                            BufferedReader reader = new BufferedReader(
                                new FileReader(temp.replaceAll("\\\\|/", "\\" + System.getProperty("file.separator"))));
                            String line = reader.readLine();
                            StringBuilder content = new StringBuilder();
                            while (line != null) {
                                content.append(line);
                                line = reader.readLine();
                            }
                            meta.setMessage(content.toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        String message = txtMsg.getText();
                        if (message != null && message.trim().length() > 0 && !"Available for a little message!".equals(
                            message)) {
                            meta.setMessage(txtMsg.getText());
                        }
                    }
                    if (passCheck(meta)) {
                        DQFileRequest.start(meta);
                    }

                }
            });
        Button btnExit = new Button();
        btnExit.setText("Exit");
        HBox exitBox = new HBox(10);
        inputBox.setAlignment(Pos.BOTTOM_CENTER);
        inputBox.getChildren().add(btnExit);
        grid.add(exitBox, 11, 13);
        btnExit.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    System.exit(0);
                }
            });
        primaryStage.show();
    }

    private boolean passCheck(QueueMeta meta) {
        return meta.getHostName() != null && meta.getChannelName() != null && meta.getQueueManagerName() != null
            && meta.getRequestQueueName() != null && meta.getMessage() != null;
    }

    public static void main(String[] args) {
        launch(args);
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
}
