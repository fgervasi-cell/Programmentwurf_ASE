package dev.fg.dhbw.ase.tasktracker.domain.components;

import java.net.URL;

import dev.fg.dhbw.ase.tasktracker.domain.entities.Task;
import dev.fg.dhbw.ase.tasktracker.exceptions.FinishedTaskComponentInitializedWithOpenTaskException;
import javafx.fxml.FXML;

public class FinishedTaskComponent extends TaskComponent
{
    private Task task;

    public FinishedTaskComponent(final Task task)
    {
        super(task);
        if (!task.isDone())
            throw new FinishedTaskComponentInitializedWithOpenTaskException();
        this.task = task;
    }

    @FXML
    @Override
    protected void onMarkTaskAsDoneOrUndone()
    {
        repository.markTaskAsUndone(this.task);
        notifyObservers(ComponentEvent.TASK_DONE);
    }

    @Override
    protected URL getFXMLLocation()
    {
        return getClass().getResource("/fxml/FinishedTaskComponent.fxml");
    }
}
