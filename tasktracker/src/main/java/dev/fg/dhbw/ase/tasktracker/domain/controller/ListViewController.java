package dev.fg.dhbw.ase.tasktracker.domain.controller;

import dev.fg.dhbw.ase.tasktracker.domain.entities.User;
import javafx.fxml.FXML;
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

    public ListViewController(final Stage primaryStage, final User user)
    {
        this.primaryStage = primaryStage;
        this.user = user;
        init();
    }

    private void init()
    {
        // TODO: why is userInformation null?
        this.userInformation.setText(String.format("Logged in as %s", this.user.getEMail()));
    }
}
