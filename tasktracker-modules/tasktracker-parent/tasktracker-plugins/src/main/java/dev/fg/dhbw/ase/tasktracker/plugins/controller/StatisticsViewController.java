package dev.fg.dhbw.ase.tasktracker.plugins.controller;

import dev.fg.dhbw.ase.tasktracker.plugins.persistence.PersistenceUtil;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import dev.fg.dhbw.ase.tasktracker.plugins.components.WidgetComponent;
import dev.fg.dhbw.ase.tasktracker.application.TaskListService;
import dev.fg.dhbw.ase.tasktracker.domain.task.Task;
import dev.fg.dhbw.ase.tasktracker.domain.user.User;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;

public class StatisticsViewController
{
    @FXML
    private FlowPane statisticsContainer;
    private final TaskListService service;
    private final User user;

    public StatisticsViewController(final User user)
    {
        this.service = new TaskListService(PersistenceUtil.obtainTaskListRepository(user));
        this.user = user;
    }

    public void addWidgetsToContainer()
    {
        WidgetComponent totalTasksDoneWidget = this.createTotalTasksDoneWidget(); 
        WidgetComponent totalTasksOpenWidget = this.createTotalTasksOpenWidget();
        WidgetComponent averageNumberOfTasksDoneWidget = this.createTasksDoneLastWeekLineChartWidget();
        WidgetComponent averageProcessingTimeWidget = this.createAverageProcessingTimeWidget();

        this.statisticsContainer.getChildren().addAll(totalTasksDoneWidget.getRoot(), totalTasksOpenWidget.getRoot(),
                averageNumberOfTasksDoneWidget.getRoot(), averageProcessingTimeWidget.getRoot());
    }

    private WidgetComponent createTotalTasksDoneWidget()
    {
        int totalTasksDone = this.service.getNumberOfTasksDone(this.user);
        Text totalTasksDoneLabel = new Text(String.valueOf(totalTasksDone));
        totalTasksDoneLabel.getStyleClass().add("label");
        return new WidgetComponent("Total tasks done", totalTasksDoneLabel);
    }

    private WidgetComponent createTotalTasksOpenWidget()
    {
        int totalTasksOpen = this.service.getTasksOpen(this.user);
        Text totalTasksOpenLabel = new Text(String.valueOf(totalTasksOpen));
        totalTasksOpenLabel.getStyleClass().add("label");
        return new WidgetComponent("Total tasks open", totalTasksOpenLabel);
    }

    private WidgetComponent createTasksDoneLastWeekLineChartWidget()
    {
        List<Task> tasksDoneLastWeek = this.service.getDoneTasksOfLastWeek(this.user);
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Day of the week");
        LineChart<String, Number> chart = new LineChart<>(xAxis, yAxis);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        Calendar today = Calendar.getInstance();
        today.add(Calendar.DAY_OF_WEEK, -6);
        for (int i = 0; i < 7; i++)
        {
            long yValue = tasksDoneLastWeek.stream().filter(task -> {
                Calendar c = Calendar.getInstance();
                c.setTime(task.getCompletionDate());
                return c.get(Calendar.DAY_OF_WEEK) == today.get(Calendar.DAY_OF_WEEK);
            }).count();
            series.getData().add(new XYChart.Data<>(today.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH), yValue));
            today.add(Calendar.DAY_OF_WEEK, 1);
        }
        chart.getData().add(series);
        return new WidgetComponent("Average number of tasks done", chart);
    }

    private WidgetComponent createAverageProcessingTimeWidget()
    {
        double averageProcessingTime = calculateAverageProcessingTime();
        return new WidgetComponent("Average processing time", new Text(String.valueOf(averageProcessingTime)));
    }

    private double calculateAverageProcessingTime()
    {
        List<Task> tasks = this.service.getTasksDone(this.user);
        int totalProcessingTime = 0;
        for (Task t : tasks)
        {
            Calendar c = Calendar.getInstance();
            c.setTime(t.getCompletionDate());
            int completionDay = c.get(Calendar.DAY_OF_YEAR);
            c.setTime(t.getCreationDate());
            int creationDay = c.get(Calendar.DAY_OF_YEAR);
            totalProcessingTime += (completionDay - creationDay);
        }
        return (double) totalProcessingTime / tasks.size();
    }
}
