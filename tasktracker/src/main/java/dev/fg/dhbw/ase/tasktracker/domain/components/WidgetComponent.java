package dev.fg.dhbw.ase.tasktracker.domain.components;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class WidgetComponent
{
    @FXML
    private Text title;
    @FXML
    private HBox widgetContainer;
    private VBox root;

    public WidgetComponent(String title, Node data)
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/WidgetComponent.fxml"));
        loader.setController(this);
        try
        {
            this.root = loader.<VBox>load();
            this.title.setText(title);
            this.root.getChildren().add(data);
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public VBox getRoot()
    {
        return this.root;
    }
}
