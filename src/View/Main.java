package View;

import Model.Model;
import ViewModel.ViewModel;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import javax.jws.WebParam;
import java.io.File;
import java.util.Optional;

public class Main extends Application {
    Model model;
    @Override
    public void start(Stage primaryStage) throws Exception {
        model = new Model();
        model.startServers();
        ViewModel viewModel = new ViewModel(model);
        model.addObserver(viewModel);
        //---------
        primaryStage.setTitle("The Amazing Maze");
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("MyView.fxml").openStream());
        Scene scene = new Scene(root, 1100, 600);
        scene.getStylesheets().add(getClass().getResource("MyStyle.css").toExternalForm());
        primaryStage.setScene(scene);
        //---------
        MyViewController view = fxmlLoader.getController();
        view.setResizeEvent(scene);
        view.setViewModel(viewModel);
        viewModel.addObserver(view);
        //---------
        SetStageCloseEvent(primaryStage);
        primaryStage.show();

    }


    private void SetStageCloseEvent(Stage primaryStage) {
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent windowEvent) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

                alert.setContentText("you sure you want to end this AWSOME game and get back to your BORING life? ");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    model.stopServers();
                } else {
                    // ... user chose CANCEL or closed the dialog
                    windowEvent.consume();
                }
            }
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}
