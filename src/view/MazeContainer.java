/*
* Justin Sybrandt
*
* Description:
* Used to display a maze along with a title and a close button. This is created and managed by the Application
* Controller, and is displayed inside the ApplicationPane.
*
* This class handles the layout of its contained elements, and ensures that the mazeCanvas is set to an
* appropriate size.
*
* Important Values:
* DEFAULT_SIZE - determines the height and width of a container.
* closeButton - the X button at the top right corner used to delete this object.
*               the applicationController handles this callback.
* */

package view;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class MazeContainer extends BorderPane{

    private static double DEFAULT_SIZE = 400;

    private Label title;
    private MazeCanvas mazeCanvas;
    private Button closeButton;
    public MazeContainer(String title, MazeCanvas canvas){
        this.title = new Label(title);
        this.mazeCanvas = canvas;
        this.closeButton = new Button("X");
        BorderPane header = new BorderPane();
        header.setLeft(this.title);
        header.setRight(this.closeButton);
        HBox.setHgrow(this.title, Priority.ALWAYS);
        HBox.setHgrow(this.closeButton, Priority.NEVER);
        HBox.setHgrow(header, Priority.ALWAYS);
        setTop(header);
        setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        //Pane needed to enlarge the maze canvas
        Pane pane = new Pane();
        pane.getChildren().add(mazeCanvas);
        mazeCanvas.widthProperty().bind(pane.widthProperty());
        mazeCanvas.heightProperty().bind(pane.heightProperty());
        setCenter(pane);
        setPrefHeight(DEFAULT_SIZE);
        setPrefWidth(DEFAULT_SIZE);
    }

    public MazeCanvas getMazeCanvas(){return mazeCanvas;}
    public Button getCloseButton(){return closeButton;}

}
