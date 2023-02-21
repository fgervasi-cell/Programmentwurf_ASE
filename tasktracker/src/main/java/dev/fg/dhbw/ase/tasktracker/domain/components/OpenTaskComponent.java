package dev.fg.dhbw.ase.tasktracker.domain.components;

import java.net.URL;

import dev.fg.dhbw.ase.tasktracker.domain.entities.Task;
import dev.fg.dhbw.ase.tasktracker.exceptions.OpenTaskInitializedWithClosedTaskException;
import javafx.fxml.FXML;

public class OpenTaskComponent extends TaskComponent
{
    private Task task;

    public OpenTaskComponent(final Task task)
    {
        super(task);
        if (task.isDone())
            throw new OpenTaskInitializedWithClosedTaskException();
        this.task = task;
    }

    @FXML
    @Override
    protected void onMarkTaskAsDoneOrUndone()
    {
        // TODO: Interface Segregation principle. Make an Interface TaskComponent and
        // seperate the implementations of done and open tasks to reduce the use of if
        // statements!!!

        repository.markTaskAsDone(this.task);
        notifyObservers(ComponentEvent.TASK_DONE); // TODO: rename this event
    }

    @Override
    protected URL getFXMLLocation()
    {
        return getClass().getResource("/fxml/OpenTaskComponent.fxml");
    }
}
