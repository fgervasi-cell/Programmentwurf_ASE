package dev.fg.dhbw.ase.tasktracker.domain.controller;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

import dev.fg.dhbw.ase.tasktracker.domain.entities.Task;
import dev.fg.dhbw.ase.tasktracker.domain.vo.DateInFuture;
import dev.fg.dhbw.ase.tasktracker.domain.vo.Title;
import dev.fg.dhbw.ase.tasktracker.persistence.PersistenceUtil;
import dev.fg.dhbw.ase.tasktracker.persistence.TaskListRepository;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddTaskFormController
{
    private final UUID taskListId;
    private final Stage stage;
    @FXML
    private TextField taskTitleTextField;
    @FXML
    private TextArea taskDescriptionTextField;
    @FXML
    private DatePicker taskDueDatePicker;
    @FXML
    private DatePicker taskReminderDatePicker;

    public AddTaskFormController(final UUID taskListId, final Stage stage)
    {
        this.taskListId = taskListId;
        this.stage = stage;
    }

    @FXML
    private void onSaveTaskButtonClicked()
    {
        Title title = new Title(this.taskTitleTextField.getText());
        String description = this.taskDescriptionTextField.getText();

        LocalDate localDueDate = this.taskDueDatePicker.getValue();
        DateInFuture dueDate = null;
        if (localDueDate != null)
        {
            Instant dueDateInstant = localDueDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
            dueDate = new DateInFuture(Date.from(dueDateInstant));
        }

        LocalDate localReminderDate = this.taskReminderDatePicker.getValue();
        DateInFuture reminderDate = null;
        if (localReminderDate != null)
        {
            Instant reminderDateInstant = localReminderDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
            reminderDate = new DateInFuture(Date.from(reminderDateInstant));
        }

        TaskListRepository repository = PersistenceUtil.obtainTaskListRepository();
        repository.addTaskToTaskList(new Task(this.taskListId, null, title, description, dueDate, reminderDate, false));

        this.stage.close();
    }

    @FXML
    private void onCancel()
    {
        this.stage.close();
    }
}
