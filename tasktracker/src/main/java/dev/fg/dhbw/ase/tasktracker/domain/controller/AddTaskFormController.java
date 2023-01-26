package dev.fg.dhbw.ase.tasktracker.domain.controller;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import dev.fg.dhbw.ase.tasktracker.domain.entities.Task;
import dev.fg.dhbw.ase.tasktracker.domain.vo.DateInFuture;
import dev.fg.dhbw.ase.tasktracker.domain.vo.TaskTitle;
import dev.fg.dhbw.ase.tasktracker.persistence.PersistenceUtil;
import dev.fg.dhbw.ase.tasktracker.persistence.TaskListRepository;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class AddTaskFormController 
{
    @FXML
    private TextField taskTitleTextField;
    @FXML
    private TextField taskDescriptionTextField;
    @FXML
    private DatePicker taskDueDatePicker;
    @FXML
    private DatePicker taskReminderDatePicker;

    @FXML
    private void onSaveTaskButtonClicked()
    {
        TaskTitle title = new TaskTitle(this.taskTitleTextField.getText());
        String description = this.taskDescriptionTextField.getText();

        LocalDate localDueDate = this.taskDueDatePicker.getValue();
        Instant dueDateInstant = localDueDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        DateInFuture dueDate = new DateInFuture(Date.from(dueDateInstant));

        LocalDate localReminderDate = this.taskReminderDatePicker.getValue();
        Instant reminderDateInstant = localReminderDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        DateInFuture reminderDate = new DateInFuture(Date.from(reminderDateInstant));

        // TODO: where do I get taskListId and name of the task list from?
        TaskListRepository repository = PersistenceUtil.obtainTaskListRepository();
        repository.addTaskToTaskList(new Task(null, null, title, description, dueDate, reminderDate, false), null);
    }
}
