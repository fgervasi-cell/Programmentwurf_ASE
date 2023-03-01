package dev.fg.dhbw.ase.tasktracker.plugins.controller;

import java.io.IOException;

import dev.fg.dhbw.ase.tasktracker.application.UserService;
import dev.fg.dhbw.ase.tasktracker.domain.user.User;
import dev.fg.dhbw.ase.tasktracker.domain.vo.EMail;
import dev.fg.dhbw.ase.tasktracker.domain.vo.Password;
import dev.fg.dhbw.ase.tasktracker.plugins.persistence.PersistenceUtil;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class StartViewController
{
    private final Stage primaryStage;
    @FXML
    private TextField eMailTextField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private VBox passwordContainer;

    public StartViewController(final Stage primaryStage)
    {
        this.primaryStage = primaryStage;
    }

    @FXML
    private void initialize()
    {
        this.passwordField.addEventFilter(KeyEvent.KEY_PRESSED, event ->
        {
            if (event.getCode() == KeyCode.ENTER)
            {
                loginOrRegisterUser(event);
            }
        });
    }

    @FXML
    private void loginOrRegisterUser(Event e)
    {
        this.primaryStage.getScene().setCursor(Cursor.WAIT);
        UserService service = new UserService(PersistenceUtil.obtainUserRepository());
        User user = service.getUserByEMail(new EMail(eMailTextField.getText()));
        if (user != null)
        {
            if (service.userPasswordDoesMatch(user.getPassword(), new Password(passwordField.getText())))
            {
                login(user);
                return;
            }
            Text passwordDidNotMatchText = new Text("Password did not match.");
            passwordContainer.getChildren().add(passwordDidNotMatchText);
            this.primaryStage.getScene().setCursor(Cursor.DEFAULT);
            return;
        }
        user = new User(new EMail(eMailTextField.getText()), new Password(passwordField.getText()));
        service.registerUser(user);
        login(user);
    }

    @FXML
    public void onContinueWithoutAccount(Event e)
    {
        login(null);
    }

    private void login(final User user)
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ListView.fxml"));
        loader.setController(new ListViewController(this.primaryStage, user));
        try
        {
            BorderPane layout = loader.<BorderPane>load();
            Scene scene = new Scene(layout, this.primaryStage.getScene().widthProperty().doubleValue(),
                    this.primaryStage.getScene().heightProperty().doubleValue());
            this.primaryStage.setTitle("TaskTracker - List");
            this.primaryStage.setScene(scene);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
