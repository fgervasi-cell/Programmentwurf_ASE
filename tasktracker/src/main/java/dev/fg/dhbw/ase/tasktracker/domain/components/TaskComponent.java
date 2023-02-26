package dev.fg.dhbw.ase.tasktracker.domain.components;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import dev.fg.dhbw.ase.tasktracker.domain.entities.Task;
import dev.fg.dhbw.ase.tasktracker.domain.vo.DateInFuture;
import dev.fg.dhbw.ase.tasktracker.observer.Observable;
import dev.fg.dhbw.ase.tasktracker.persistence.PersistenceUtil;
import dev.fg.dhbw.ase.tasktracker.persistence.TaskListRepository;
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
    protected TaskListRepository repository;
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

    protected TaskComponent(final Task task)
    {
        this.task = task;
        this.repository = PersistenceUtil.obtainTaskListRepository();
        FXMLLoader loader = new FXMLLoader(this.getFXMLLocation());
        loader.setController(this);
        try
        {
            this.root = loader.<HBox>load();
            prepareUI();
        }
        catch (IOException e)
        {
            // TODO
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
        String dueDateString = formatDate(dueDate);
        String reminderString = formatDate(reminder);
        this.taskDueDate.setText(dueDateString != null ? dueDateString : "There is no due date set");
        this.taskReminderDate.setText(reminderString != null ? reminderString : "There is no reminder set");
        if (this.dueDateIsReached(dueDate.getDueDate()))
        {
            this.taskDueDate.setFill(Color.RED);
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

    // TODO: same like above. This should maybe be moved to the DateInFuture class
    // like "dateReached" or smth
    private boolean dueDateIsReached(Date dueDate)
    {
        Calendar currentDateCalendar = Calendar.getInstance(TimeZone.getDefault());
        Calendar dueDateCalendar = Calendar.getInstance();
        dueDateCalendar.setTime(dueDate);

        if (currentDateCalendar.get(Calendar.YEAR) > dueDateCalendar.get(Calendar.YEAR))
        {
            return false;
        }

        if (currentDateCalendar.get(Calendar.MONTH) > dueDateCalendar.get(Calendar.MONTH))
        {
            return false;
        }

        return currentDateCalendar.get(Calendar.DAY_OF_MONTH) >= dueDateCalendar.get(Calendar.DAY_OF_MONTH);
    }

    public HBox getRoot()
    {
        return this.root;
    }

    @FXML
    private void onTaskDelete()
    {
        this.repository.removeTask(this.task);
        notifyObservers(ComponentEvent.TASK_DELETE);
    }

    protected abstract URL getFXMLLocation();

    protected abstract void onMarkTaskAsDoneOrUndone();
}
