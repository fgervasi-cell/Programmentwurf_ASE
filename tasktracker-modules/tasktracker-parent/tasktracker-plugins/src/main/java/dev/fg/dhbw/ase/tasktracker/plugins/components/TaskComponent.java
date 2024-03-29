package dev.fg.dhbw.ase.tasktracker.plugins.components;

import java.io.IOException;
import java.net.URL;

import dev.fg.dhbw.ase.tasktracker.domain.task.Task;
import dev.fg.dhbw.ase.tasktracker.domain.user.User;
import dev.fg.dhbw.ase.tasktracker.domain.vo.DateInFuture;
import dev.fg.dhbw.ase.tasktracker.abstraction.observer.Observable;
import dev.fg.dhbw.ase.tasktracker.application.TaskService;
import dev.fg.dhbw.ase.tasktracker.plugins.persistence.PersistenceUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public abstract class TaskComponent extends Observable
{
    private HBox root;
    private Task task;
    protected TaskService service;
    @FXML
    protected Text taskTitle;
    @FXML
    private Text taskDescription;
    @FXML
    private Text taskDueDate;
    @FXML
    private Text taskReminderDate;
    @FXML
    private Button button;

    protected TaskComponent(final Task task, User user)
    {
        this.task = task;
        this.service = new TaskService(PersistenceUtil.obtainTaskListRepository(user));
        FXMLLoader loader = new FXMLLoader(this.getFXMLLocation());
        loader.setController(this);
        try
        {
            this.root = loader.<HBox>load();
            prepareUI();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void prepareUI()
    {
        this.taskTitle.setText(this.task.getTitle().getTitleString());
        String description = this.task.getDescription();
        this.taskDescription
                .setText((description != null && !description.isBlank()) ? description : "There is no description");
        DateInFuture dueDate = this.task.getDueDate();
        DateInFuture reminder = this.task.getReminder();
        String dueDateString = dueDate.formatDate();
        String reminderString = reminder.formatDate();
        this.taskDueDate.setText(dueDateString != null ? dueDateString : "There is no due date set");
        this.taskReminderDate.setText(reminderString != null ? reminderString : "There is no reminder set");
        if (dueDate.dateIsReached())
        {
            this.taskDueDate.setFill(Color.RED);
        }
    }

    public HBox getRoot()
    {
        return this.root;
    }

    @FXML
    protected void onTaskDelete()
    {
        this.service.deleteTask(this.task);
        notifyObservers(ComponentEvent.TASK_DELETE);
    }

    protected abstract URL getFXMLLocation();

    protected abstract void onMarkTaskAsDoneOrUndone();
}
