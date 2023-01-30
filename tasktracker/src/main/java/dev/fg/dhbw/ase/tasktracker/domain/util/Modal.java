package dev.fg.dhbw.ase.tasktracker.domain.util;

import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Modal
{
    public enum Type
    {
        INFO,
        ERROR,
    }

    public static void show(String title, String description, Modal.Type type)
    {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("TaskTracker - " + title);
        Scene scene = new Scene(null);
        stage.setScene(scene);
        stage.show();
    }
}
