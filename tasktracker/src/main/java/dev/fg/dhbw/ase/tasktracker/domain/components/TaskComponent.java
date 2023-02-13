package dev.fg.dhbw.ase.tasktracker.domain.components;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.kordamp.ikonli.javafx.FontIcon;

import dev.fg.dhbw.ase.tasktracker.domain.entities.Task;
import dev.fg.dhbw.ase.tasktracker.domain.vo.DateInFuture;
import dev.fg.dhbw.ase.tasktracker.observer.Observable;
import dev.fg.dhbw.ase.tasktracker.observer.Observer;
import dev.fg.dhbw.ase.tasktracker.persistence.PersistenceUtil;
import dev.fg.dhbw.ase.tasktracker.persistence.TaskListRepository;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
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
    @FXML
    private Button button;
    private Task task;
    private List<Observer> observers = new ArrayList<>();

    public TaskComponent(final Task task)
    {
        this.task = task;
        this.getStyleClass().add("task");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TaskComponent.fxml"));
        loader.setController(this);
        try
        {
            this.getChildren().add(loader.<HBox>load());
            if (task.isDone())
            {
                button.getTooltip().setText("Undo this task. It will be returned to its originating list.");
                button.setGraphic(new FontIcon("mdi-window-close:34"));
                taskTitle.getStyleClass().add("strikethrough");
            }
            this.taskTitle.setText(task.getTitle().getTitleString());
            String description = task.getDescription();
            this.taskDescription
                    .setText((description != null && !description.isBlank()) ? description : "There is no description");
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

    // TODO: I think this validates the High Cohesion and Information Expert
    // principles because this task does not fit in here.
    // TODO: I think it would be better to make this a static utility method in the
    // DateInFuture class or something like that to increase reusablity.
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
        TaskListRepository listRepository = PersistenceUtil.obtainTaskListRepository();
        // TODO: Interface Segregation principle. Make an Interface TaskComponent and
        // seperate the implementations of done and open tasks to reduce the use of if
        // statements!!!
        if (this.task.isDone())
        {
            listRepository.markTaskAsUndone(this.task);
            notifyObservers(ComponentEvent.TASK_DONE);
            return;
        }

        listRepository.markTaskAsDone(this.task);
        notifyObservers(ComponentEvent.TASK_DONE); // TODO: rename this event
    }

    @FXML
    private void onTaskDelete()
    {
        PersistenceUtil.obtainTaskListRepository().removeTask(this.task);
        notifyObservers(ComponentEvent.TASK_DELETE);
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
