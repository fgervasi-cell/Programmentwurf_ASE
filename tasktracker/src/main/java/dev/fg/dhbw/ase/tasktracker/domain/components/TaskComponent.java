package dev.fg.dhbw.ase.tasktracker.domain.components;

import dev.fg.dhbw.ase.tasktracker.domain.vo.DateInFuture;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class TaskComponent extends HBox // NOSONAR: just using the JavaFX library
{
    public TaskComponent(String title, DateInFuture dueDate)
    {
        this.getChildren().add(new Button("Done"));
        this.getChildren().add(new Separator(Orientation.VERTICAL));
        this.getChildren().add(new Text(title));
        this.getChildren().add(new Separator(Orientation.VERTICAL));
        this.getChildren().add(new Text(dueDate != null ? dueDate.getDueDate().toString() : "There is no due date set"));
    }
}
