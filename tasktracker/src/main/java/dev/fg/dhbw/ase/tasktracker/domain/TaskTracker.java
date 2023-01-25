package dev.fg.dhbw.ase.tasktracker.domain;

import dev.fg.dhbw.ase.tasktracker.domain.controller.StartViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/StartView.fxml"));
        loader.setController(new StartViewController(primaryStage));
        Scene scene = new Scene(loader.<BorderPane>load());
        primaryStage.setMaximized(true);
        primaryStage.setTitle("TaskTracker - Start");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}