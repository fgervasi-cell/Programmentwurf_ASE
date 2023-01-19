package dev.fg.dhbw.ase.tasktracker.domain.views;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class StartView extends BorderPane
{
    private Label eMailLabel = new Label("E-Mail");
    private Label passwordLabel = new Label("Password");
    private TextField eMailTextField = new TextField();
    private TextField passwordTextField = new TextField();
    private Button loginOrRegisterButton = new Button("Login or register");
    private Text continueWithoutAccountText = new Text("Continue without an account...");
    private Text headline = new Text("TaskTracker");
    
    public StartView()
    {
        createView();
    }

    private void createView()
    {
        loginOrRegisterButton.getStyleClass().setAll("btn", "btn-primary", "btn-lg");
        headline.getStyleClass().setAll("h1", "b");
        eMailLabel.getStyleClass().setAll("lbl", "lbl-default");
        passwordLabel.getStyleClass().setAll("lbl", "lbl-default");

        HBox topContainer = new HBox(headline);
        topContainer.setAlignment(Pos.CENTER);
        
        VBox eMailContainer = new VBox(10, eMailLabel, eMailTextField);
        eMailTextField.setMinWidth(300);
        VBox passwordContainer = new VBox(10, passwordLabel, passwordTextField);
        passwordContainer.setMinWidth(300);

        VBox innerCenterContainer = new VBox(30, eMailContainer, passwordContainer, loginOrRegisterButton);
        innerCenterContainer.setAlignment(Pos.CENTER);

        HBox centerContainer = new HBox(innerCenterContainer);
        centerContainer.setAlignment(Pos.CENTER);

        HBox bottomContainer = new HBox(continueWithoutAccountText);
        bottomContainer.setAlignment(Pos.CENTER);

        this.setTop(topContainer);
        this.setCenter(centerContainer);
        this.setBottom(bottomContainer);
    }
}
