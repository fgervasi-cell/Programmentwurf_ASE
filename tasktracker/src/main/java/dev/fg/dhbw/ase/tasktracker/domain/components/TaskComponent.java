package dev.fg.dhbw.ase.tasktracker.domain.components;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import dev.fg.dhbw.ase.tasktracker.domain.entities.Task;
import dev.fg.dhbw.ase.tasktracker.domain.vo.DateInFuture;
import dev.fg.dhbw.ase.tasktracker.observer.Observable;
import dev.fg.dhbw.ase.tasktracker.observer.Observer;
import dev.fg.dhbw.ase.tasktracker.persistence.PersistenceUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class TaskComponent extends HBox implements Observable // NOSONAR: just using the JavaFX library
{
    @FXML
    private Text taskTitle;
    @FXML
    private Text taskDescription;
    @FXML
    private Text taskDueDate;
    @FXML
    private Text taskReminderDate;
    private Task task;
    private List<Observer> observers = new ArrayList<>();

    public enum Events
    {
        TASK_DELETE, TASK_DONE;
    }

    public TaskComponent(final Task task)
    {
        this.task = task;
        this.getStyleClass().add("task");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TaskComponent.fxml"));
        loader.setController(this);
        try
        {
            this.getChildren().add(loader.<HBox>load());
            this.taskTitle.setText(task.getTitle().getTitleString());
            String description = task.getDescription();
            // TODO: For some reason this is not working
            this.taskDescription.setText(description != null ? description : "There is no description");
            DateInFuture dueDate = task.getDueDate();
            DateInFuture reminder = task.getReminder();
            String dueDateString = formatDate(dueDate);
            String reminderString = formatDate(reminder);
            this.taskDueDate.setText(dueDateString != null ? dueDateString : "There is no due date set");
            this.taskReminderDate.setText(reminderString != null ? reminderString : "There is no reminder set");
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private String formatDate(DateInFuture futureDate)
    {
        String dateString = null;
        if (futureDate != null)
        {
            Date date = futureDate.getDueDate();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            dateString = String.format("%s-%s-%s", calendar.get(Calendar.DAY_OF_MONTH),
                    calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
        }
        return dateString;
    }

    @FXML
    private void onMarkTaskAsDone()
    {
        // TODO: move the task to the "Done" list (maybe I need to implement a new
        // method like "move(Task t, TaskList newList)" in the repository for that)
        notifyObservers(TaskComponent.Events.TASK_DONE);
    }

    @FXML
    private void onTaskDelete()
    {
        PersistenceUtil.obtainTaskListRepository().removeTask(this.task);
        notifyObservers(TaskComponent.Events.TASK_DELETE);
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
            observer.notifyObserver(getClass().getName() + ":" + event);
        }
    }
}
