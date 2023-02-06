package dev.fg.dhbw.ase.tasktracker.domain.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.kordamp.ikonli.javafx.FontIcon;

import dev.fg.dhbw.ase.tasktracker.domain.components.TaskComponent;
import dev.fg.dhbw.ase.tasktracker.domain.entities.Task;
import dev.fg.dhbw.ase.tasktracker.domain.entities.TaskList;
import dev.fg.dhbw.ase.tasktracker.domain.entities.User;
import dev.fg.dhbw.ase.tasktracker.domain.vo.Title;
import dev.fg.dhbw.ase.tasktracker.observer.Observer;
import dev.fg.dhbw.ase.tasktracker.persistence.PersistenceUtil;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

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

    public ListViewController(final Stage primaryStage, final User user)
    {
        this.primaryStage = primaryStage;
        this.user = user;
    }

    @FXML
    private void initialize()
    {
        this.selectedListName.getStyleClass().add("headline");
        this.userInformation.setText(String.format("Logged in as %s", this.user.getEMail().getMailAdress()));
        List<TaskList> taskLists = PersistenceUtil.obtainTaskListRepository().getTaskListsForUser(this.user.getId());
        // TODO: I need a list to store the task that are done (should not be editable by the user!)
        if (taskLists.stream().filter(tl -> !tl.getTitle().getTitleString().equals("Done")).collect(Collectors.toList()).isEmpty())
        {
            PersistenceUtil.obtainTaskListRepository().createNewTaskListForUser(new Title("Done"), this.user);
            taskLists.add(new TaskList(new Title("Done")));
        }
        for (TaskList taskList : taskLists)
        {
            // TODO: maybe now it would be better to extract this to a TaskListComponent.fxml file?
            HBox box = new HBox();
            FontIcon icon = new FontIcon();
            icon.setIconLiteral("mdi-delete-forever:24:RED");
            final Title taskListTitle = taskList.getTitle();
            Text listName = new Text(taskListTitle.getTitleString());
            listName.getStyleClass().add("link");
            listName.addEventFilter(MouseEvent.MOUSE_CLICKED,
                    event -> this.handleListNameClicked(event, taskListTitle));
            box.getChildren().addAll(icon, listName);
            this.listsContainer.getChildren().add(box);
        }
    }

    private void handleListNameClicked(MouseEvent event, Title taskListTitle)
    {
        if (event.getButton() == MouseButton.PRIMARY)
        {
            this.selectedListName.setText(taskListTitle.getTitleString());
            List<Task> tasksForList = PersistenceUtil.obtainTaskListRepository()
                    .getTasksOfTaskListByTaskListName(taskListTitle);
            this.taskContainer.getChildren().clear();
            for (Task t : tasksForList)
            {
                if (!t.isDone())
                {
                    this.taskContainer.getChildren().add(new TaskComponent(t));
                }
            }
        }
    }

    @FXML
    private void onAddTaskButtonClicked(Event e)
    {
        if (this.selectedListName == null || this.selectedListName.getText() == null)
        {
            return;
        }
        TaskList list = PersistenceUtil.obtainTaskListRepository()
                .getTaskListByName(new Title(this.selectedListName.getText()));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddTaskForm.fxml"));
        Stage stage = new Stage();
        loader.setController(new AddTaskFormController(list.getId(), stage));
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
        // TODO: Refactor this. Extract it to another method to reuse it (e.g. in the initialization phase).
        final TextField newList = new TextField();
        newList.addEventFilter(KeyEvent.KEY_PRESSED, event ->
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
            Text newListName = new Text(input);
            newListName.getStyleClass().add("link");
            Title taskListTitle = new Title(input);
            newListName.addEventFilter(MouseEvent.MOUSE_CLICKED,
                    mouseEvent -> this.handleListNameClicked(mouseEvent, taskListTitle));
            this.listsContainer.getChildren().add(newListName);
            this.listsContainer.getChildren().remove(newList);
        });
        this.listsContainer.getChildren().add(newList);
    }

    @Override
    public void notifyObserver(Object message)
    {
        if (message instanceof String)
        {
            String messageString = (String) message;
            if (messageString.split(":")[1].equals(TaskComponent.Events.TASK_DELETE.toString()))
            {
                // TODO: update the UI
            }
        }
    }
}
