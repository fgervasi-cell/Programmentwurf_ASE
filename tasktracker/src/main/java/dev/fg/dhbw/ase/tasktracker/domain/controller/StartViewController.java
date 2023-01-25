package dev.fg.dhbw.ase.tasktracker.domain.controller;

import java.io.IOException;
import java.util.logging.Logger;

import dev.fg.dhbw.ase.tasktracker.domain.entities.User;
import dev.fg.dhbw.ase.tasktracker.domain.vo.EMail;
import dev.fg.dhbw.ase.tasktracker.domain.vo.Password;
import dev.fg.dhbw.ase.tasktracker.persistence.PersistenceUtil;
import dev.fg.dhbw.ase.tasktracker.persistence.UserRepository;
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
    private static final Logger LOG = Logger.getLogger(StartViewController.class.getName());
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
        this.passwordField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
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
        UserRepository userRepository = PersistenceUtil.obtainUserRepository();
        User user = userRepository.getUserByEMail(new EMail(eMailTextField.getText()));
        if (user != null)
        {
            LOG.info("User does already exist. Checking password...");
            if (user.getPassword().getPassword().equals(this.passwordField.getText()))
            {
                LOG.info("Password correct. Logging in...");
                login(user);
                return;
            }
            LOG.info("Password did not match.");
            Text passwordDidNotMatchText = new Text("Password did not match.");
            passwordContainer.getChildren().add(passwordDidNotMatchText);
            this.primaryStage.getScene().setCursor(Cursor.DEFAULT);
            return;
        }
        LOG.info("User does not exist. Creating new user and logging in...");
        user = new User(new EMail(eMailTextField.getText()), new Password(passwordField.getText()));
        userRepository.createUser(user);
        login(user);
    }

    @FXML
    public void onContinueWithoutAccount(Event e)
    {
        LOG.info("Continue without an account...");
        login(null);
    }

    private void login(final User user)
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ListView.fxml"));
        loader.setController(new ListViewController(this.primaryStage, user));
        try
        {
            BorderPane layout = loader.<BorderPane>load();
            Scene scene = new Scene(layout);
            this.primaryStage.setTitle("TaskTracker - List");
            this.primaryStage.setScene(scene);
        }
        catch (IOException e)
        {
            // TODO: show error in UI and handle it accordingly
            e.printStackTrace();
        }
    }
}
