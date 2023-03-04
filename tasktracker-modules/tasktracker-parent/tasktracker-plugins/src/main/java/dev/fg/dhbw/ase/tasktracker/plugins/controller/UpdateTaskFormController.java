package dev.fg.dhbw.ase.tasktracker.plugins.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import dev.fg.dhbw.ase.tasktracker.abstraction.observer.Observable;
import dev.fg.dhbw.ase.tasktracker.application.TaskService;
import dev.fg.dhbw.ase.tasktracker.domain.task.Task;
import dev.fg.dhbw.ase.tasktracker.domain.task.TaskFactory;
import dev.fg.dhbw.ase.tasktracker.plugins.components.ComponentEvent;
import dev.fg.dhbw.ase.tasktracker.plugins.components.FinishedTaskComponent;
import dev.fg.dhbw.ase.tasktracker.plugins.components.OpenTaskComponent;
import dev.fg.dhbw.ase.tasktracker.plugins.components.TaskComponent;
import dev.fg.dhbw.ase.tasktracker.plugins.persistence.PersistenceUtil;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class UpdateTaskFormController extends Observable
{
    private Task task;
    private BorderPane root;
    private TaskService service;
    @FXML
    private TextField taskTitleTextField;
    @FXML
    private TextArea taskDescriptionTextField;
    @FXML
    private DatePicker taskDueDatePicker;
    @FXML
    private DatePicker taskReminderDatePicker;
    @FXML
    private VBox subTasksContainer;

    public UpdateTaskFormController(Task task, BorderPane root)
    {
        this.task = task;
        this.root = root;
        this.service = new TaskService(PersistenceUtil.obtainTaskListRepository(null));
    }

    @FXML
    private void initialize()
    {
        this.taskTitleTextField.setText(task.getTitle().getTitleString());
        this.taskDescriptionTextField.setText(task.getDescription());
        this.taskDueDatePicker
                .setValue(task.getDueDate().getDueDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        this.taskReminderDatePicker
                .setValue(task.getReminder().getDueDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        loadSubTasks();
    }

    private void loadSubTasks()
    {// TODO this is not working yet
        List<Task> subTasks = this.service.loadSubTasksForTask(task);
        this.subTasksContainer.getChildren().addAll(subTasks.stream().map(t -> {
            if (t.isDone())
                return new FinishedTaskComponent(t, null);
            return new OpenTaskComponent(t, null, root);
        }).map(TaskComponent::getRoot).toList());
    }

    @FXML
    private void onCancel()
    {
        this.root.rightProperty().setValue(null);
    }

    @FXML
    private void onUpdateTaskButtonClicked()
    {
        this.service.deleteTask(task);
        UUID taskListId = this.task.getTaskListId();
        String title = this.taskTitleTextField.getText();
        String description = this.taskDescriptionTextField.getText();
        LocalDate dueDate = this.taskDueDatePicker.getValue();
        LocalDate reminderDate = this.taskReminderDatePicker.getValue();
        this.service.createTask(TaskFactory.createTask(taskListId, title, description, Date.valueOf(dueDate),
                Date.valueOf(reminderDate)));
        notifyObservers(ComponentEvent.ADD_TASK_FORM_SAVE);
        this.root.rightProperty().setValue(null);
    }

    @FXML
    private void onAddSubTaskButtonClicked()
    {
        //TODO Open "Add Task Window" etc.
    }
}
