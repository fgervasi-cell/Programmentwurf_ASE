package dev.fg.dhbw.ase.tasktracker.plugins.components;

import java.io.IOException;
import java.net.URL;

import dev.fg.dhbw.ase.tasktracker.abstraction.observer.Observer;
import dev.fg.dhbw.ase.tasktracker.domain.task.Task;
import dev.fg.dhbw.ase.tasktracker.domain.user.User;
import dev.fg.dhbw.ase.tasktracker.plugins.controller.UpdateTaskFormController;
import dev.fg.dhbw.ase.tasktracker.plugins.exceptions.OpenTaskInitializedWithClosedTaskException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

public class OpenTaskComponent extends TaskComponent implements Observer
{
    private Task task;
    private BorderPane root;
    private User user;

    public OpenTaskComponent(final Task task, User user, BorderPane root)
    {
        super(task, user);
        if (task.isDone())
            throw new OpenTaskInitializedWithClosedTaskException();
        this.task = task;
        this.getRoot().addEventFilter(MouseEvent.MOUSE_PRESSED, this::handleTaskComponentClicked);
        this.root = root;
        this.user = user;
    }

    private void handleTaskComponentClicked(MouseEvent e)
    {
        if (e.isPrimaryButtonDown() && !this.task.isSubTask())
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/UpdateTaskForm.fxml"));
            UpdateTaskFormController controller = new UpdateTaskFormController(this.task, this.root, this.user);
            controller.registerObserver(this);
            loader.setController(controller);
            try
            {
                this.root.rightProperty().set(loader.<BorderPane>load());
            }
            catch (IOException e1)
            {
                e1.printStackTrace();
            }
        }
    }

    @FXML
    @Override
    protected void onMarkTaskAsDoneOrUndone()
    {
        service.markTaskAsDone(this.task);
        notifyObservers(ComponentEvent.TASK_DONE_OR_UNDONE);
    }

    @Override
    protected URL getFXMLLocation()
    {
        return getClass().getResource("/fxml/OpenTaskComponent.fxml");
    }

    @Override
    public void notifyObserver(Object message)
    {
        notifyObservers(message);
    }
}
