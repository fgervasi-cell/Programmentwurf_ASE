package dev.fg.dhbw.ase.tasktracker.domain.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dev.fg.dhbw.ase.tasktracker.domain.components.ComponentEvent;
import dev.fg.dhbw.ase.tasktracker.domain.components.FinishedTaskComponent;
import dev.fg.dhbw.ase.tasktracker.domain.components.OpenTaskComponent;
import dev.fg.dhbw.ase.tasktracker.domain.components.TaskListComponent;
import dev.fg.dhbw.ase.tasktracker.domain.task.Task;
import dev.fg.dhbw.ase.tasktracker.domain.task.TaskList;
import dev.fg.dhbw.ase.tasktracker.domain.user.User;
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
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

// TODO: Controllers are meant to delegate most of the work which is not the case here. Maybe refactoring is needed?
public class ListViewController implements Observer
{
    private final Stage primaryStage;
    private final User user;
    private final TaskListRepository repository;
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
    // TODO: should also store the TaskList object itself that is currently selected
    // because I need its ID

    public ListViewController(final Stage primaryStage, final User user)
    {
        this.primaryStage = primaryStage;
        this.user = user;
        this.repository = PersistenceUtil.obtainTaskListRepository();
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
        this.userInformation.setText(String.format("Logged in as %s", this.user.getEMail().getMailAdress()));
    }

    private List<TaskList> getTaskListsForUser()
    {
        List<TaskList> taskLists = this.repository.getTaskListsForUser(this.user.getId());
        return this.addTaskListForDoneTasksIfNotPresent(taskLists);
    }

    private List<TaskList> addTaskListForDoneTasksIfNotPresent(List<TaskList> taskLists)
    {
        if (taskLists.stream().filter(tl -> tl.getTitle().getTitleString().equals("Done")).count() == 0)
        {
            this.repository.createNewTaskListForUser(new Title("Done"), this.user);
            taskLists.add(new TaskList(new Title("Done")));
        }
        return taskLists;
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
                OpenTaskComponent task = new OpenTaskComponent(t);
                task.registerObserver(this);
                this.taskContainer.getChildren().add(task.getRoot());
            }
        }
    }

    private List<HBox> getTaskComponentsForDoneTasks() // TODO: I should just use a database query for this...
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
        List<FinishedTaskComponent> components = doneTasks.stream().map(FinishedTaskComponent::new).collect(Collectors.toList());
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
                || this.selectedListName.getText().equals("Done")
                || this.selectedListName.getText().equals("Your Lists");
    }

    private void openAddTaskWindow()
    {
        TaskList list = this.repository.getTaskListByName(new Title(this.selectedListName.getText()));
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
            this.primaryStage.setTitle("TaskTracker - Statistics");
            this.primaryStage.setScene(statisticsScene);
        }
        catch (IOException e1)
        {
            // TODO Auto-generated catch block
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
        PersistenceUtil.obtainTaskListRepository().createNewTaskListForUser(new Title(input), user);
        TaskList list = PersistenceUtil.obtainTaskListRepository().getTaskListByName(new Title(input));
        TaskListComponent taskListComponent = new TaskListComponent(list);
        taskListComponent.registerObserver(this);
        this.listsContainer.getChildren().add(taskListComponent.getRoot());
        this.listsContainer.getChildren().remove(newList);
    }

    private void refreshListsContainer(List<TaskList> lists)
    {
        this.listsContainer.getChildren().clear();
        for (TaskList list : lists)
        {
            TaskListComponent taskListComponent = new TaskListComponent(list);
            taskListComponent.registerObserver(this);
            this.listsContainer.getChildren().add(taskListComponent.getRoot());
        }
        this.taskContainer.getChildren().clear();
        this.selectedListName.setText("Your Lists");
    }

    @Override
    public void notifyObserver(Object message)
    {
        // TODO: maybe I could use inheritance to prevent this method from getting very
        // long?
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
