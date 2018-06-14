package View;

import Model.Model;
import ViewModel.ViewModel;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.jws.WebParam;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Model model = new Model();
        ViewModel viewModel = new ViewModel(model);
        //---------
        primaryStage.setTitle("Hello World");
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("MyView.fxml").openStream());
        Scene scene = new Scene(root,600,600);
        primaryStage.setScene(scene);
        //---------
        MyViewController view = fxmlLoader.getController() ;
        view.setViewModel(viewModel);
        //---------
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
