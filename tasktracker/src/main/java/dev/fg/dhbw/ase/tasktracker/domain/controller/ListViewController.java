package dev.fg.dhbw.ase.tasktracker.domain.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dev.fg.dhbw.ase.tasktracker.domain.components.ComponentEvent;
import dev.fg.dhbw.ase.tasktracker.domain.components.TaskComponent;
import dev.fg.dhbw.ase.tasktracker.domain.components.TaskListComponent;
import dev.fg.dhbw.ase.tasktracker.domain.entities.Task;
import dev.fg.dhbw.ase.tasktracker.domain.entities.TaskList;
import dev.fg.dhbw.ase.tasktracker.domain.entities.User;
import dev.fg.dhbw.ase.tasktracker.domain.vo.Title;
import dev.fg.dhbw.ase.tasktracker.observer.Observer;
import dev.fg.dhbw.ase.tasktracker.persistence.PersistenceUtil;
import dev.fg.dhbw.ase.tasktracker.persistence.TaskListRepository;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

// TODO: Controllers are meant to delegate most of the work which is not the case here. Maybe refactoring is needed?
public class ListViewController implements Observer
{
    private final Stage primaryStage;
    private final User user;
    @FXML
    private Text userInformation;
    @FXML
    private Text selectedListName;
    @FXML
    private VBox listsContainer;
    @FXML
    private VBox taskContainer;
    @FXML
    private Button addTaskButton;

    public ListViewController(final Stage primaryStage, final User user)
    {
        this.primaryStage = primaryStage;
        this.user = user;
    }

    @FXML
    private void initialize()
    {
        this.addTaskButton.setVisible(false);
        this.selectedListName.getStyleClass().add("headline");
        this.userInformation.setText(String.format("Logged in as %s", this.user.getEMail().getMailAdress()));
        List<TaskList> taskLists = PersistenceUtil.obtainTaskListRepository().getTaskListsForUser(this.user.getId());
        if (taskLists.stream().filter(tl -> tl.getTitle().getTitleString().equals("Done")).collect(Collectors.toList())
                .isEmpty())
        {
            PersistenceUtil.obtainTaskListRepository().createNewTaskListForUser(new Title("Done"), this.user);
            taskLists.add(new TaskList(new Title("Done")));
        }
        this.refreshListsContainer(taskLists);
    }

    private void selectListWithName(Title taskListTitle)
    {
        this.addTaskButton.setVisible(true);
        if (taskListTitle.getTitleString().equals("Done") || taskListTitle.getTitleString().equals("Your Lists"))
        {
            this.addTaskButton.setVisible(false);
        }
        this.selectedListName.setText(taskListTitle.getTitleString());
        this.taskContainer.getChildren().clear();
        if (taskListTitle.getTitleString().equals("Done"))
        {
            this.taskContainer.getChildren().addAll(this.getTaskComponentsForDoneTasks());
            return;
        }
        List<Task> tasksForList = PersistenceUtil.obtainTaskListRepository()
                .getTasksOfTaskListByTaskListName(taskListTitle);
        for (Task t : tasksForList)
        {
            if (!t.isDone())
            {
                TaskComponent task = new TaskComponent(t);
                task.registerObserver(this);
                this.taskContainer.getChildren().add(task);
            }
        }
    }

    private List<TaskComponent> getTaskComponentsForDoneTasks() // TODO: I should just use a database query for this...
    {
        TaskListRepository taskListRepository = PersistenceUtil.obtainTaskListRepository();
        List<TaskList> lists = taskListRepository.getTaskListsForUser(this.user.getId());
        lists = lists.stream().filter(list -> !list.getTitle().getTitleString().equals("Done"))
                .collect(Collectors.toList());
        List<Task> doneTasks = new ArrayList<>();
        for (TaskList list : lists)
        {
            List<Task> tasks = taskListRepository.getTasksOfTaskListByTaskListName(list.getTitle());
            List<Task> done = tasks.stream().filter(Task::isDone).collect(Collectors.toList());
            doneTasks.addAll(done);
        }
        List<TaskComponent> components = doneTasks.stream().map(TaskComponent::new).collect(Collectors.toList());
        components.stream().forEach(comp -> comp.registerObserver(this));
        return components;
    }

    @FXML
    private void onAddTaskButtonClicked(Event e)
    {
        if (this.selectedListName == null || this.selectedListName.getText() == null
                || this.selectedListName.getText().equals("Done")
                || this.selectedListName.getText().equals("Your Lists"))
        {
            return;
        }
        TaskList list = PersistenceUtil.obtainTaskListRepository()
                .getTaskListByName(new Title(this.selectedListName.getText()));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddTaskForm.fxml"));
        Stage stage = new Stage();
        AddTaskFormController addTaskFormController = new AddTaskFormController(list.getId(), stage);
        addTaskFormController.registerObserver(this);
        loader.setController(addTaskFormController);
        try
        {
            BorderPane form = loader.<BorderPane>load();
            Scene scene = new Scene(form);
            stage.setTitle("TaskTracker - Add Task");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        }
        catch (IOException e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    @FXML
    private void onAddListButtonClicked(Event e)
    {
        // TODO: Refactor this. Extract it to another method to reuse it (e.g. in the
        // initialization phase).
        final TextField newList = new TextField();
        newList.addEventFilter(KeyEvent.KEY_PRESSED, event -> this.handleSubmitNewListName(event, newList));
        this.listsContainer.getChildren().add(newList);
    }

    private void handleSubmitNewListName(KeyEvent event, TextField newList)
    {
        if (event.getCode() != KeyCode.ENTER)
        {
            return;
        }

        String input = newList.getText();
        if (input == null || input.isEmpty())
        {
            this.listsContainer.getChildren().remove(newList);
            return;
        }
        PersistenceUtil.obtainTaskListRepository().createNewTaskListForUser(new Title(input), user);
        TaskList list = PersistenceUtil.obtainTaskListRepository().getTaskListByName(new Title(input));
        TaskListComponent taskListComponent = new TaskListComponent(list);
        taskListComponent.registerObserver(this);
        this.listsContainer.getChildren().add(taskListComponent);
        this.listsContainer.getChildren().remove(newList);
    }

    private void refreshListsContainer(List<TaskList> lists)
    {
        this.listsContainer.getChildren().clear();
        for (TaskList list : lists)
        {
            TaskListComponent taskListComponent = new TaskListComponent(list);
            taskListComponent.registerObserver(this);
            this.listsContainer.getChildren().add(taskListComponent);
        }
        this.taskContainer.getChildren().clear();
        this.selectedListName.setText("Your Lists");
    }

    @Override
    public void notifyObserver(Object message)
    {
        if (message instanceof ComponentEvent)
        {
            ComponentEvent event = (ComponentEvent) message;
            if (event.name().equals(ComponentEvent.TASK_DELETE.name()))
            {
                selectListWithName(new Title(this.selectedListName.getText()));
                return;
            }

            if (event.name().equals(ComponentEvent.TASK_DONE.name()))
            {
                this.selectListWithName(new Title(this.selectedListName.getText()));
                return;
            }

            if (event.name().equals(ComponentEvent.TASK_LIST_DELETE.name()))
            {
                List<TaskList> lists = PersistenceUtil.obtainTaskListRepository()
                        .getTaskListsForUser(this.user.getId());
                this.refreshListsContainer(lists);
                return;
            }

            if (event.name().equals(ComponentEvent.ADD_TASK_FORM_SAVE.name()))
            {
                this.selectListWithName(new Title(this.selectedListName.getText()));
                return;
            }

            if (event.name().equals(ComponentEvent.TASK_LIST_NAME_CLICKED.name()))
            {
                this.selectListWithName(new Title(event.getData()));
            }
        }
    }
}
