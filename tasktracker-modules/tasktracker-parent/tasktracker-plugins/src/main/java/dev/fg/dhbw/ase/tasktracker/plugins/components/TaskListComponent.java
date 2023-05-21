package dev.fg.dhbw.ase.tasktracker.plugins.components;

import java.io.IOException;

import dev.fg.dhbw.ase.tasktracker.domain.task.TaskList;
import dev.fg.dhbw.ase.tasktracker.domain.user.User;
import dev.fg.dhbw.ase.tasktracker.abstraction.observer.Observable;
import dev.fg.dhbw.ase.tasktracker.application.TaskListService;
import dev.fg.dhbw.ase.tasktracker.plugins.persistence.PersistenceUtil;
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
    private HBox root;
    private TaskListService service;

    public TaskListComponent(TaskList list, User user)
    {
        this.service = new TaskListService(PersistenceUtil.obtainTaskListRepository(user));
        this.list = list;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TaskListComponent.fxml"));
        loader.setController(this);
        try
        {
            this.root = loader.<HBox>load();
            this.listName.setText(list.getTitle().getTitleString());
            this.listName.addEventFilter(MouseEvent.MOUSE_CLICKED, this::handleListNameClicked);
            if (this.listName.getText().equals("Done"))
            {
                this.deleteButton.setVisible(false);
            }
        }
        catch (IOException e)
        {
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

    public HBox getRoot()
    {
        return this.root;
    }

    @FXML
    private void onListDelete()
    {
        if (this.listName.getText().equals("Done")) 
        {
            return;
        }

        this.service.deleteTaskList(list);
        ComponentEvent event = ComponentEvent.TASK_LIST_DELETE;
        event.setData(this.listName.getText());
        notifyObservers(event);
    }
}
