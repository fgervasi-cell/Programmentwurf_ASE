package dev.fg.dhbw.ase.tasktracker.plugins.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dev.fg.dhbw.ase.tasktracker.plugins.components.ComponentEvent;
import dev.fg.dhbw.ase.tasktracker.plugins.components.FinishedTaskComponent;
import dev.fg.dhbw.ase.tasktracker.plugins.components.OpenTaskComponent;
import dev.fg.dhbw.ase.tasktracker.plugins.components.TaskListComponent;
import dev.fg.dhbw.ase.tasktracker.domain.task.Task;
import dev.fg.dhbw.ase.tasktracker.domain.task.TaskList;
import dev.fg.dhbw.ase.tasktracker.domain.user.User;
import dev.fg.dhbw.ase.tasktracker.domain.vo.Title;
import dev.fg.dhbw.ase.tasktracker.abstraction.observer.Observer;
import dev.fg.dhbw.ase.tasktracker.application.TaskListService;
import dev.fg.dhbw.ase.tasktracker.plugins.persistence.PersistenceUtil;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ListViewController implements Observer
{
    private static final String DEFAULT_HEADING = "Your Lists";
    private static final String NAME_OF_DONE_TASKS_LIST = "Done";
    private final User user;
    private final TaskListService service;
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
    @FXML
    private BorderPane root;

    public ListViewController(final User user)
    {
        this.user = user;
        this.service = new TaskListService(PersistenceUtil.obtainTaskListRepository(user));
    }

    @FXML
    private void initialize()
    {
        this.prepareUI();
        this.refreshListsContainer(this.getTaskListsForUser());
    }

    private void prepareUI()
    {
        this.addTaskButton.setVisible(false);
        this.selectedListName.getStyleClass().add("headline");
        String loggedInAs = this.user == null ? "anonymous user" : this.user.getEMail().getMailAdress();
        this.userInformation.setText(String.format("Logged in as %s", loggedInAs));
    }

    private List<TaskList> getTaskListsForUser()
    {
        return this.addTaskListForDoneTasksIfNotPresent(this.service.getTaskLists(user));
    }

    private List<TaskList> addTaskListForDoneTasksIfNotPresent(List<TaskList> taskLists)
    {
        if (taskLists.stream().filter(tl -> tl.getTitle().getTitleString().equals(NAME_OF_DONE_TASKS_LIST)).count() == 0)
        {
            this.service.createTaskList(new Title(NAME_OF_DONE_TASKS_LIST), this.user);
            taskLists.add(new TaskList(new Title(NAME_OF_DONE_TASKS_LIST)));
        }
        return taskLists;
    }

    private void selectListWithName(Title taskListTitle)
    {
        this.addTaskButton.setVisible(true);
        if (taskListTitle.getTitleString().equals(NAME_OF_DONE_TASKS_LIST) || taskListTitle.getTitleString().equals(DEFAULT_HEADING))
        {
            this.addTaskButton.setVisible(false);
        }
        this.selectedListName.setText(taskListTitle.getTitleString());
        this.taskContainer.getChildren().clear();
        if (taskListTitle.getTitleString().equals(NAME_OF_DONE_TASKS_LIST))
        {
            this.taskContainer.getChildren().addAll(this.getTaskComponentsForDoneTasks());
            return;
        }
        List<Task> tasksForList = this.service.getTasksFromList(taskListTitle);
        for (Task t : tasksForList)
        {
            if (!t.isDone() && !t.isSubTask())
            {
                OpenTaskComponent task = new OpenTaskComponent(t, user, this.root);
                task.registerObserver(this);
                this.taskContainer.getChildren().add(task.getRoot());
            }
        }
    }

    private List<HBox> getTaskComponentsForDoneTasks()
    {
        List<TaskList> lists = this.service.getTaskLists(this.user);
        lists = lists.stream().filter(list -> !list.getTitle().getTitleString().equals(NAME_OF_DONE_TASKS_LIST))
                .collect(Collectors.toList());
        List<Task> doneTasks = new ArrayList<>();
        for (TaskList list : lists)
        {
            List<Task> tasks = this.service.getTasksFromList(list.getTitle());
            List<Task> done = tasks.stream().filter(Task::isDone).collect(Collectors.toList());
            doneTasks.addAll(done);
        }
        List<FinishedTaskComponent> components = doneTasks.stream().map(task -> new FinishedTaskComponent(task, user)).collect(Collectors.toList());
        components.stream().forEach(comp -> comp.registerObserver(this));
        return components.stream().map(FinishedTaskComponent::getRoot).collect(Collectors.toList());
    }

    @FXML
    private void onAddTaskButtonClicked(Event e)
    {
        if (this.selectedListNameIsInvalid())
        {
            return;
        }
        this.openAddTaskWindow();
    }

    private boolean selectedListNameIsInvalid()
    {
        return this.selectedListName == null || this.selectedListName.getText() == null
                || this.selectedListName.getText().equals(NAME_OF_DONE_TASKS_LIST)
                || this.selectedListName.getText().equals(DEFAULT_HEADING);
    }

    private void openAddTaskWindow()
    {
        TaskList list = this.service.getTaskList(new Title(this.selectedListName.getText()));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddTaskForm.fxml"));
        Stage stage = new Stage();
        AddTaskFormController addTaskFormController = new AddTaskFormController(list.getId(), stage, user);
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
            e1.printStackTrace();
        }
    }

    @FXML
    private void onAddListButtonClicked(Event e)
    {
        final TextField newList = new TextField();
        newList.addEventFilter(KeyEvent.KEY_PRESSED, event -> this.handleSubmitNewListName(event, newList));
        this.listsContainer.getChildren().add(newList);
    }

    @FXML
    private void showStatistics(Event e)
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/StatisticsView.fxml"));
        StatisticsViewController controller = new StatisticsViewController(this.user);
        loader.setController(controller);
        try
        {
            Scene statisticsScene = new Scene(loader.<FlowPane>load());
            controller.addWidgetsToContainer();
            Stage statisticsStage = new Stage();
            statisticsStage.setTitle("TaskTracker - Statistics");
            statisticsStage.setScene(statisticsScene);
            statisticsStage.show();
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
        }
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
        this.service.createTaskList(new Title(input), user);
        TaskList list = this.service.getTaskList(new Title(input));
        TaskListComponent taskListComponent = new TaskListComponent(list, user);
        taskListComponent.registerObserver(this);
        this.listsContainer.getChildren().add(taskListComponent.getRoot());
        this.listsContainer.getChildren().remove(newList);
    }

    private void refreshListsContainer(List<TaskList> lists)
    {
        this.listsContainer.getChildren().clear();
        for (TaskList list : lists)
        {
            TaskListComponent taskListComponent = new TaskListComponent(list, user);
            taskListComponent.registerObserver(this);
            this.listsContainer.getChildren().add(taskListComponent.getRoot());
        }
        this.taskContainer.getChildren().clear();
        this.selectedListName.setText(DEFAULT_HEADING);
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

            if (event.name().equals(ComponentEvent.TASK_DONE_OR_UNDONE.name()))
            {
                this.selectListWithName(new Title(this.selectedListName.getText()));
                return;
            }

            if (event.name().equals(ComponentEvent.TASK_LIST_DELETE.name()))
            {
                List<TaskList> lists = this.service.getTaskLists(user);
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
