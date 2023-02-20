package dev.fg.dhbw.ase.tasktracker.domain.components;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dev.fg.dhbw.ase.tasktracker.domain.entities.TaskList;
import dev.fg.dhbw.ase.tasktracker.observer.Observable;
import dev.fg.dhbw.ase.tasktracker.observer.Observer;
import dev.fg.dhbw.ase.tasktracker.persistence.PersistenceUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class TaskListComponent extends Observable
{
    @FXML
    private Text listName;
    @FXML
    private Button deleteButton;
    private TaskList list;

    public TaskListComponent(TaskList list)
    {
        this.list = list;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TaskListComponent.fxml"));
        loader.setController(this);
        try
        {
            this.getChildren().add(loader.<HBox>load());
            this.listName.setText(list.getTitle().getTitleString());
            this.listName.addEventFilter(MouseEvent.MOUSE_CLICKED, this::handleListNameClicked);
            if (this.listName.getText().equals("Done"))
            {
                this.deleteButton.setVisible(false);
            }
        }
        catch (IOException e)
        {
            // TODO
            e.printStackTrace();
        }
    }

    private void handleListNameClicked(MouseEvent e)
    {
        if (e.getButton() == MouseButton.PRIMARY)
        {
            ComponentEvent event = ComponentEvent.TASK_LIST_NAME_CLICKED;
            event.setData(this.listName.getText());
            notifyObservers(event);
        }
    }

    @FXML
    private void onListDelete()
    {
        // TODO: Would it not be better to create a whole other class for this so I do not need all those checks (like "ImmutableListComponent" or so)?
        // TOOD: Maybe I could also design an Interface for that like "ListComponent" or so
        if (this.listName.getText().equals("Done")) 
        {
            return;
        }

        PersistenceUtil.obtainTaskListRepository().deleteTaskList(list);
        ComponentEvent event = ComponentEvent.TASK_LIST_DELETE;
        event.setData(this.listName.getText());
        notifyObservers(event);
    }
}
