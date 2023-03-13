package dev.fg.dhbw.ase.tasktracker.plugins.controller;

import java.io.IOException;
import java.util.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import dev.fg.dhbw.ase.tasktracker.abstraction.observer.Observable;
import dev.fg.dhbw.ase.tasktracker.abstraction.observer.Observer;
import dev.fg.dhbw.ase.tasktracker.application.TaskService;
import dev.fg.dhbw.ase.tasktracker.domain.task.Task;
import dev.fg.dhbw.ase.tasktracker.domain.task.TaskFactory;
import dev.fg.dhbw.ase.tasktracker.domain.user.User;
import dev.fg.dhbw.ase.tasktracker.plugins.components.ComponentEvent;
import dev.fg.dhbw.ase.tasktracker.plugins.components.FinishedTaskComponent;
import dev.fg.dhbw.ase.tasktracker.plugins.components.OpenTaskComponent;
import dev.fg.dhbw.ase.tasktracker.plugins.components.TaskComponent;
import dev.fg.dhbw.ase.tasktracker.plugins.persistence.PersistenceUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class UpdateTaskFormController extends Observable implements Observer
{
    private Task task;
    private BorderPane root;
    private User user;
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

    public UpdateTaskFormController(Task task, BorderPane root, User user)
    {
        this.task = task;
        this.root = root;
        this.user = user;
        this.service = new TaskService(PersistenceUtil.obtainTaskListRepository(user));
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
    {
        UUID userId = this.user != null ? this.user.getId() : null;
        List<Task> subTasks = this.service.loadSubTasksForTask(task, userId);
        this.subTasksContainer.getChildren().addAll(subTasks.stream().map(t ->
        {
            TaskComponent taskComponent = null;
            if (t.isDone())
                taskComponent = new FinishedTaskComponent(t, this.user);
            else
                taskComponent = new OpenTaskComponent(t, this.user, root);
            taskComponent.registerObserver(this);
            taskComponent.getRoot().prefWidthProperty().bind(this.subTasksContainer.widthProperty());
            return taskComponent.getRoot();
        }).toList());
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
        this.service.createTask(TaskFactory.createTaskWithId(task.getId(), taskListId, title, description,
                Date.from(dueDate.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                Date.from(reminderDate.atStartOfDay(ZoneId.systemDefault()).toInstant())));
        notifyObservers(ComponentEvent.ADD_TASK_FORM_SAVE);
        this.root.rightProperty().setValue(null);
    }

    @FXML
    private void onAddSubTaskButtonClicked()
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddTaskForm.fxml"));
        Stage stage = new Stage();
        stage.setTitle("TaskTracker - Add Sub Task");
        stage.initModality(Modality.APPLICATION_MODAL);
        AddTaskFormController controller = new AddTaskFormController(this.task.getTaskListId(), this.task.getId(),
                stage, this.user);
        controller.registerObserver(this);
        loader.setController(controller);
        try
        {
            Scene scene = new Scene(loader.<BorderPane>load());
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void notifyObserver(Object message)
    {
        this.subTasksContainer.getChildren().clear();
        loadSubTasks();
    }
}
