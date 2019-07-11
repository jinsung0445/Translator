package org.dimigo.gui.translator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.*;

public class TranslatorMain extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Translator.fxml"));

        stage.setScene(new Scene(root));
        stage.setTitle("파PA고와 함께하는 즐거운 번역기시간");

        stage.centerOnScreen();
        stage.show();

    }
}
