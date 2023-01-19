package dev.fg.dhbw.ase.tasktracker.domain.controller;

import java.io.IOException;
import java.util.logging.Logger;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import dev.fg.dhbw.ase.tasktracker.domain.entities.User;
import dev.fg.dhbw.ase.tasktracker.domain.vo.EMail;
import dev.fg.dhbw.ase.tasktracker.domain.vo.Password;
import dev.fg.dhbw.ase.tasktracker.persistence.UserDatabaseRepository;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class StartViewController
{
    private static final Logger LOG = Logger.getLogger(StartViewController.class.getName());
    private final Stage primaryStage;
    @FXML
    private TextField eMailTextField;
    @FXML
    private PasswordField passwordField;

    public StartViewController(final Stage primaryStage)
    {
        this.primaryStage = primaryStage;
    }

    @FXML
    private void loginOrRegisterUser(Event e)
    {
        this.primaryStage.getScene().setCursor(Cursor.WAIT);
        Session session = createSession();
        UserDatabaseRepository userRepository = new UserDatabaseRepository(session);
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
            return;
        }
        LOG.info("User does not exist. Creating new user and logging in...");
        user = new User(new EMail(eMailTextField.getText()), new Password(passwordField.getText()));
        userRepository.createUser(user);
        session.close();
        login(user);
    }

    @FXML
    public void onContinueWithoutAccount(Event e)
    {
        System.out.println("Continue without an account...");
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

    private Session createSession()
    {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        SessionFactory sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        return sessionFactory.openSession();
    }
}
