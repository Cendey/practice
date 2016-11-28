package com.ipacs.als.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

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
public class MQStart extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        String BASE_DIR = "../../../..";
        Parent root = FXMLLoader.load(getClass().getResource(BASE_DIR + "/layout/layout.fxml"));
        Scene scene = new Scene(root, 850, 600);

        stage.setTitle("WebSphere MQ Information");
        Image icon = new Image(getClass().getResourceAsStream(BASE_DIR + "/resources/ibm.png"));
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
