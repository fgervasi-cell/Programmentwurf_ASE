package dev.fg.dhbw.ase.tasktracker.domain.util;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Modal
{
    private Modal()
    {

    }

    public enum Type
    {
        INFO(MaterialDesign.MDI_INFORMATION.getDescription()), ERROR(MaterialDesign.MDI_ALARM.getDescription());

        private String literal;

        private Type(String literal)
        {
            this.literal = literal;
        }
    }

    public static void show(String title, String description, Modal.Type type)
    {
        FontIcon icon = new FontIcon(type.literal);
        Text descriptionText = new Text((type == Modal.Type.INFO ? "Information: " : "Error: ") + description);
        HBox text = new HBox(icon, descriptionText);
        Button okButton = new Button("Okay");
        Button cancelButton = new Button("Cancel");
        HBox buttons = new HBox(okButton, cancelButton);
        VBox root = new VBox(text, buttons);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("TaskTracker - " + title);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
