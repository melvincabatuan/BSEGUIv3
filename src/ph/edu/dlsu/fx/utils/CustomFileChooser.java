package ph.edu.dlsu.fx.utils;

/**
 * Created by cobalt on 3/19/16.
 */

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;

public final class CustomFileChooser{

    private static Stage stage;
    private static String videoPath;
    private static String xmlPath;
    private static FileChooser fileChooser = new FileChooser();

    public static void chooseVideoAndXml() {

        stage = new Stage();
        stage.setTitle("Video and .xml File Chooser");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setMinWidth(250);

        fileChooser = new FileChooser();

        final Button openVideoButton = new Button("Open a Video...");
        openVideoButton.setFont(Font.font("Times New Roman", FontWeight.SEMI_BOLD, 25));

        final Button openXmlButton   = new Button("Open an xml...");
        openXmlButton.setFont(Font.font("Times New Roman", FontWeight.SEMI_BOLD, 25));

        openVideoButton.setOnAction(
                e -> {
                    fileChooser.setInitialDirectory(new File("res/video"));
                    File file = fileChooser.showOpenDialog(stage);
                    if (file != null) {
                        videoPath = file.getPath();
                    }
                    if (videoPath != null && xmlPath != null){
                        stage.close();
                    }
                });

        openXmlButton.setOnAction(
                e -> {
                    fileChooser.setInitialDirectory(new File("res"));
                    File file = fileChooser.showOpenDialog(stage);
                    if (file != null) {
                        xmlPath = file.getPath();
                    }
                    if (videoPath != null && xmlPath != null){
                        stage.close();
                    }
                });


        final GridPane inputGridPane = new GridPane();

        GridPane.setConstraints(openVideoButton, 0, 0);
        GridPane.setConstraints(openXmlButton, 1, 0);
        inputGridPane.setHgap(6);
        inputGridPane.setVgap(6);
        inputGridPane.getChildren().addAll(openVideoButton, openXmlButton);

        final Pane rootGroup = new VBox(12);
        rootGroup.getChildren().addAll(inputGridPane);
        rootGroup.setPadding(new Insets(12, 12, 12, 12));

        stage.setScene(new Scene(rootGroup));
        stage.showAndWait();
    }

    public static String getVideoPath() {
        return videoPath;
    }

    public static String getXmlPath() {
        return xmlPath;
    }
}