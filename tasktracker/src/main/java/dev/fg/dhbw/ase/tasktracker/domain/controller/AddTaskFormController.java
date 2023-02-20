package dev.fg.dhbw.ase.tasktracker.domain.controller;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

import dev.fg.dhbw.ase.tasktracker.domain.components.ComponentEvent;
import dev.fg.dhbw.ase.tasktracker.domain.factories.TaskFactory;
import dev.fg.dhbw.ase.tasktracker.observer.Observable;
import dev.fg.dhbw.ase.tasktracker.persistence.PersistenceUtil;
import dev.fg.dhbw.ase.tasktracker.persistence.TaskListRepository;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddTaskFormController extends Observable
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
        String title = this.taskTitleTextField.getText();
        String description = this.taskDescriptionTextField.getText();

        LocalDate localDueDate = this.taskDueDatePicker.getValue();
        Date dueDate = null;
        if (localDueDate != null)
        {
            Instant dueDateInstant = localDueDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
            dueDate = Date.from(dueDateInstant);
        }

        LocalDate localReminderDate = this.taskReminderDatePicker.getValue();
        Date reminderDate = null;
        if (localReminderDate != null)
        {
            Instant reminderDateInstant = localReminderDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
            reminderDate = Date.from(reminderDateInstant);
        }

        TaskListRepository repository = PersistenceUtil.obtainTaskListRepository();
        repository.addTaskToTaskList(TaskFactory.createTask(this.taskListId, title, description, dueDate, reminderDate));

        notifyObservers(ComponentEvent.ADD_TASK_FORM_SAVE);
        this.stage.close();
    }

    @FXML
    private void onCancel()
    {
        this.stage.close();
    }
}
