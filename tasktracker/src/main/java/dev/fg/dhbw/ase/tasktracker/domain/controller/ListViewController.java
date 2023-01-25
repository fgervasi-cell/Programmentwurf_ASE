package dev.fg.dhbw.ase.tasktracker.domain.controller;

import java.util.List;

import dev.fg.dhbw.ase.tasktracker.domain.components.TaskComponent;
import dev.fg.dhbw.ase.tasktracker.domain.entities.Task;
import dev.fg.dhbw.ase.tasktracker.domain.entities.TaskList;
import dev.fg.dhbw.ase.tasktracker.domain.entities.User;
import dev.fg.dhbw.ase.tasktracker.domain.vo.TaskTitle;
import dev.fg.dhbw.ase.tasktracker.persistence.PersistenceUtil;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ListViewController
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
        this.userInformation.setText(String.format("Logged in as %s", this.user.getEMail().getMailAdress()));
        List<TaskList> taskLists = PersistenceUtil.obtainTaskListRepository().getTaskListsForUser(this.user.getId());
        for (TaskList taskList : taskLists)
        {
            final TaskTitle taskListTitle = taskList.getTitle();
            Text listName = new Text(taskListTitle.getTitle());
            listName.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
                if (event.getButton() == MouseButton.PRIMARY)
                {
                    this.selectedListName.setText(taskListTitle.getTitle());
                    List<Task> tasksForList = PersistenceUtil.obtainTaskListRepository().getTasksOfTaskListByTaskListName(taskListTitle);
                    for (Task t : tasksForList)
                    {
                        if (!t.isDone())
                        {
                            this.taskContainer.getChildren().add(new TaskComponent(t.getTitle().getTitle(), t.getDueDate()));
                        }
                    }
                }
            });
            this.listsContainer.getChildren().add(listName);
        }
    }

    @FXML
    private void onAddTaskButtonClicked(Event e)
    {
        
    }

    @FXML
    private void onAddListButtonClicked(Event e)
    {
        final TextField newList = new TextField();
        newList.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
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
            PersistenceUtil.obtainTaskListRepository().createNewTaskListForUser(new TaskTitle(input), user);
            this.listsContainer.getChildren().add(new Text(input));
            this.listsContainer.getChildren().remove(newList);
        });
        this.listsContainer.getChildren().add(newList);
    }
}
