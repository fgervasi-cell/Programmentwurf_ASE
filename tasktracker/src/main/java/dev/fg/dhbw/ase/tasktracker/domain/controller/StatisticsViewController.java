package dev.fg.dhbw.ase.tasktracker.domain.controller;

import java.util.ArrayList;
import java.util.List;

import dev.fg.dhbw.ase.tasktracker.persistence.PersistenceUtil;
import dev.fg.dhbw.ase.tasktracker.domain.components.WidgetComponent;
import dev.fg.dhbw.ase.tasktracker.domain.entities.Task;
import dev.fg.dhbw.ase.tasktracker.domain.entities.User;
import dev.fg.dhbw.ase.tasktracker.persistence.TaskListRepository;
import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;

public class StatisticsViewController
{
    @FXML
    private FlowPane statisticsContainer;
    private List<Task> tasks = new ArrayList<>();

    public StatisticsViewController(final User user)
    {
        initializeTaskList(user);

        WidgetComponent totalTasksDoneWidget = new WidgetComponent("Total tasks done");
        WidgetComponent totalTasksOpenWidget = new WidgetComponent("Total tasks open");
        WidgetComponent averageNumberOfTasksDoneWidget = new WidgetComponent("Average number of tasks done");
        WidgetComponent averageProcessingTimeWidget = new WidgetComponent("Average processing time");

        this.statisticsContainer.getChildren().addAll(totalTasksDoneWidget.getRoot(), totalTasksOpenWidget.getRoot(),
                averageNumberOfTasksDoneWidget.getRoot(), averageProcessingTimeWidget.getRoot());
    }

    private void initializeTaskList(User user)
    {
        TaskListRepository repository = PersistenceUtil.obtainTaskListRepository();
        // TODO: this.tasks = repository.getAllTasksForUser(user);
    }
}
