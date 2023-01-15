package dev.fg.dhbw.ase.tasktracker.domain;

import dev.fg.dhbw.ase.tasktracker.domain.views.StartView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TaskTracker extends Application
{
    public static void main(String[] args)
    {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/StartView.fxml"));
        VBox vbox = loader.<VBox>load();
        Scene scene = new Scene(vbox);
        primaryStage.setTitle("TaskTracker - Start");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}