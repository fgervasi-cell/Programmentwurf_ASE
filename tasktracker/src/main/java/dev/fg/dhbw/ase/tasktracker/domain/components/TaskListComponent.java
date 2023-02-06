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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class TaskListComponent extends HBox implements Observable // NOSONAR: just using the JavaFX library
{
    @FXML
    private Text listName;
    private TaskList list;
    private List<Observer> observers = new ArrayList<>();

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
        PersistenceUtil.obtainTaskListRepository().deleteTaskList(list);
        ComponentEvent event = ComponentEvent.TASK_LIST_NAME_CLICKED;
        event.setData(this.listName.getText());
        notifyObservers(event);
    }

    @Override
    public void registerObserver(Observer observer)
    {
        this.observers.add(observer);
    }

    @Override
    public void notifyObservers(Object event)
    {
        for (Observer observer : this.observers)
        {
            observer.notifyObserver(event);
        }
    }
}
