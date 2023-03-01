package dev.fg.dhbw.ase.tasktracker;

import dev.fg.dhbw.ase.tasktracker.plugins.controller.StartViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
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
        Font.loadFont(getClass().getResource("/fonts/DM_Sans/DMSans-Bold.ttf").toExternalForm(), 0);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/StartView.fxml"));
        loader.setController(new StartViewController(primaryStage));
        Scene scene = new Scene(loader.<BorderPane>load());
        primaryStage.setMaximized(true);
        primaryStage.setTitle("TaskTracker - Start");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}