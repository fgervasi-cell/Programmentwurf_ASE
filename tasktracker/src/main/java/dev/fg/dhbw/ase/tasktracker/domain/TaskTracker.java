package dev.fg.dhbw.ase.tasktracker.domain;

import org.kordamp.bootstrapfx.BootstrapFX;

import dev.fg.dhbw.ase.tasktracker.domain.views.StartView;
import javafx.application.Application;
import javafx.scene.Scene;
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
        Scene scene = new Scene(new StartView());
        scene.getStylesheets().add(getClass().getResource("/css/Main.css").toString());
        primaryStage.setMaximized(true);
        primaryStage.setTitle("TaskTracker - Start");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}