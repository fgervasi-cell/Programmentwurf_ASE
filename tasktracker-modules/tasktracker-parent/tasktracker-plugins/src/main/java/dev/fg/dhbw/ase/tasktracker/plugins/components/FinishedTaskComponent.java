package dev.fg.dhbw.ase.tasktracker.plugins.components;

import java.net.URL;

import dev.fg.dhbw.ase.tasktracker.domain.task.Task;
import dev.fg.dhbw.ase.tasktracker.domain.user.User;
import dev.fg.dhbw.ase.tasktracker.plugins.exceptions.FinishedTaskComponentInitializedWithOpenTaskException;
import javafx.fxml.FXML;

public class FinishedTaskComponent extends TaskComponent
{
    private Task task;

    public FinishedTaskComponent(final Task task, User user)
    {
        super(task, user);
        if (!task.isDone())
            throw new FinishedTaskComponentInitializedWithOpenTaskException();
        this.task = task;
    }

    @FXML
    @Override
    protected void onMarkTaskAsDoneOrUndone()
    {
        service.markTaskAsUndone(this.task);
        notifyObservers(ComponentEvent.TASK_DONE_OR_UNDONE);
    }

    @Override
    protected URL getFXMLLocation()
    {
        return getClass().getResource("/fxml/FinishedTaskComponent.fxml");
    }
}
