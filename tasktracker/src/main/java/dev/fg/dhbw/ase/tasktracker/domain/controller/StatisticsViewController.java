package dev.fg.dhbw.ase.tasktracker.domain.controller;

import dev.fg.dhbw.ase.tasktracker.persistence.PersistenceUtil;
import dev.fg.dhbw.ase.tasktracker.domain.components.WidgetComponent;
import dev.fg.dhbw.ase.tasktracker.domain.entities.User;
import dev.fg.dhbw.ase.tasktracker.persistence.TaskListRepository;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;

public class StatisticsViewController
{
    @FXML
    private FlowPane statisticsContainer;
    private final TaskListRepository repository;
    private final User user;

    public StatisticsViewController(final User user)
    {
        this.repository = PersistenceUtil.obtainTaskListRepository();
        this.user = user;
    }

    public void addWidgetsToContainer() // TODO: This does not seem to be very elegant. This way you need to "remember"
                                        // to call this function! Is there a better way?
    {
        WidgetComponent totalTasksDoneWidget = this.createTotalTasksDoneWidget(); // TODO: make common Interface but
                                                                                  // different class for each widget? >
                                                                                  // Interface segregation pattern! >
                                                                                  // many client specific interfaces! Because this class will also grow indefinetely
        WidgetComponent totalTasksOpenWidget = this.createTotalTasksOpenWidget();
        WidgetComponent averageNumberOfTasksDoneWidget = this.createAverageNumberOfTasksDoneWidget();
        WidgetComponent averageProcessingTimeWidget = this.createAverageProcessingTimeWidget();

        this.statisticsContainer.getChildren().addAll(totalTasksDoneWidget.getRoot(), totalTasksOpenWidget.getRoot(),
                averageNumberOfTasksDoneWidget.getRoot(), averageProcessingTimeWidget.getRoot());
    }

    private WidgetComponent createTotalTasksDoneWidget()
    {
        int totalTasksDone = this.repository.getNumberOfDoneTasksForUser(this.user);
        Label totalTasksDoneLabel = new Label(String.valueOf(totalTasksDone));
        totalTasksDoneLabel.getStyleClass().add("label");
        return new WidgetComponent("Total tasks done", totalTasksDoneLabel);
    }

    private WidgetComponent createTotalTasksOpenWidget()
    {
        int totalTasksOpen = this.repository.getNumberOfOpenTasksForUser(this.user);
        Label totalTasksOpenLabel = new Label(String.valueOf(totalTasksOpen));
        totalTasksOpenLabel.getStyleClass().add("label");
        return new WidgetComponent("Total tasks open", totalTasksOpenLabel);
    }

    private WidgetComponent createAverageNumberOfTasksDoneWidget() // TODO: make those plain number over all time and also charts (two more widgets?)
    {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Test");
        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>(10, 10));
        series.getData().add(new XYChart.Data<>(30, 30));
        return new WidgetComponent("Average number of tasks done", chart);
    }

    private WidgetComponent createAverageProcessingTimeWidget()
    {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Test");
        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>(10, 10));
        series.getData().add(new XYChart.Data<>(30, 30));
        return new WidgetComponent("Average processing time", chart);
    }
}
