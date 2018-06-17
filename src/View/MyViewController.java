
package View;
import ViewModel.ViewModel;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

public class MyViewController implements Observer, IView {

    @FXML
    private ViewModel viewModel;
    private boolean isReached;
    public MazeDisplayer mazeDisplayer;
    public CharacterDisplayer characterDisplayer;
    public SolutionDisplayer solutionDisplayer;
    public javafx.scene.layout.BorderPane rootPane;
    public javafx.scene.control.TextField txtfld_rowsNum;
    public javafx.scene.control.TextField txtfld_columnsNum;
    public javafx.scene.control.Label lbl_rowsNum;
    public javafx.scene.control.Label lbl_columnsNum;
    public javafx.scene.control.Button btn_generateMaze;
    public javafx.scene.control.Button btn_solveMaze;
   // public javafx.scene.control.MenuItem menu_save;

    private MediaPlayer mp;
    private MediaPlayer mp2;
    private boolean onCharPressed=false;

    public void setViewModel(ViewModel viewModel) {
        this.viewModel = viewModel;
        btn_solveMaze.setDisable(true);
      //  menu_save.setDisable(true);
        bindProperties(viewModel);




    }

    private void bindProperties(ViewModel viewModel) {
        lbl_rowsNum.textProperty().bind(viewModel.characterPositionRow);
        lbl_columnsNum.textProperty().bind(viewModel.characterPositionColumn);
        txtfld_rowsNum.textProperty().addListener((observable, oldValue, newValue) ->{
            if(!newValue.matches("\\d*"))
                txtfld_rowsNum.setText(newValue.replaceAll("[^\\d]",""));});
        txtfld_columnsNum.textProperty().addListener((observable, oldValue, newValue) ->{
            if(!newValue.matches("\\d*"))
                txtfld_columnsNum.setText(newValue.replaceAll("[^\\d]",""));});
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == viewModel) {

            if(arg.toString()=="maze") {
                displayMaze(viewModel.getMaze());
                displayCharactetr();
                Position end = viewModel.getMaze().getGoalPosition();
               characterDisplayer.drawAt(end.getRowIndex(), end.getColumnIndex(), characterDisplayer.getImageFileNameGoal());

            }

            if(arg.toString()=="character") {
                displayCharactetr();
                if (!isReached) {
                    Position end = viewModel.getMaze().getGoalPosition();
                    characterDisplayer.drawAt(end.getRowIndex(), end.getColumnIndex(), characterDisplayer.getImageFileNameGoal());
                }
            }

            if(arg.toString()=="solve")
                displaySolution();

            if(arg.toString()=="loaded") {
                Maze maze = viewModel.getMaze();
                if(maze!=null) {
                    solutionDisplayer.clearCanvas();
                    characterDisplayer.clearCanvas();
                    mazeDisplayer.clearCanvas();
                    int h= maze.getRow() , w = maze.getCol();
                    mazeDisplayer.setDimensions(h,w);
                    characterDisplayer.setDimensions(h,w);
                    solutionDisplayer.setDimensions(h,w);
                    displayMaze(maze);
                    displayCharactetr();
                    characterDisplayer.drawAt(maze.getGoalPosition().getRowIndex(),
                                              maze.getGoalPosition().getColumnIndex(), characterDisplayer.getImageFileNameGoal());



                }
                else
                    showAlert("File is Corrupted");
            }
            btn_generateMaze.setDisable(false);
        }
    }

    private void displaySolution() {
        btn_solveMaze.setDisable(false);
        solutionDisplayer.setList(viewModel.getSolution());
    }

    private void displayCharactetr(){
        int characterPositionRow = viewModel.getCharacterPositionRow();
        int characterPositionColumn = viewModel.getCharacterPositionColumn();
        characterDisplayer.setCharacterPosition(characterPositionRow, characterPositionColumn);
        this.characterPositionRow.set(characterPositionRow + "");
        this.characterPositionColumn.set(characterPositionColumn + "");
        if(viewModel.isReached())
            reachGoal();
    }

    @Override
    public void displayMaze(Maze maze) {
        btn_solveMaze.setDisable(false);
        //menu_save.setDisable(false);
        mazeDisplayer.setMaze(maze);
    }

    public void generateMaze() {
        isReached=false;
        String path = new File("resources\\Sound\\background.mp3").toURI().toString();
        mp = new MediaPlayer(new Media(path));
        if(mp2!=null)
                mp2.stop();
        try {
            int heigth = Integer.valueOf(txtfld_rowsNum.getText());
            int width = Integer.valueOf(txtfld_columnsNum.getText());

            //set canvas sizes for all 3 display layres
            mazeDisplayer.setDimensions(heigth,width);
            characterDisplayer.setDimensions(heigth,width);
            solutionDisplayer.setDimensions(heigth,width);

            btn_solveMaze.setDisable(false);
            btn_generateMaze.setDisable(true);
            solutionDisplayer.clearSolution();

            viewModel.generateMaze(width, heigth);
            mp.play();
        }catch (NumberFormatException e){showAlert("You Must Enter a Number");}
    }

    public void solveMaze(ActionEvent actionEvent) {
        btn_solveMaze.setDisable(true);
        viewModel.solveMaze();
    }

    public void reachGoal() {
        btn_solveMaze.setDisable(true);
        isReached=true;
        mp.stop();
        showAlert("Amazing..");
        String path = new File("resources\\Sound\\finish.mp3").toURI().toString();
        mp2 = new MediaPlayer( new Media(path));
        mp2.setStartTime(new Duration(15500));
        mp2.setStopTime(new Duration(45000));
        mp2.play();

    }

    private void showAlert(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(alertMessage);
        alert.show();
    }

    public void KeyPressed(KeyEvent keyEvent) {
        MediaPlayer mp1;
        String path = new File("resources\\Sound\\move.mp3").toURI().toString();
          mp1= new MediaPlayer(new Media(path));
        if(!isReached)
            mp1.setVolume(0.55);
        KeyCode kc =keyEvent.getCode();
        if(kc==KeyCode.DIGIT1 ||kc==KeyCode.DIGIT2 ||kc==KeyCode.DIGIT3
            ||kc==KeyCode.DIGIT4 ||kc==KeyCode.DIGIT6 ||kc==KeyCode.DIGIT7||kc==KeyCode.DIGIT8 || kc==KeyCode.DIGIT9
                ||kc==KeyCode.NUMPAD1 ||kc==KeyCode.NUMPAD2 ||kc==KeyCode.NUMPAD3
                ||kc==KeyCode.NUMPAD4 ||kc==KeyCode.NUMPAD6 ||kc==KeyCode.NUMPAD7||kc==KeyCode.NUMPAD8 || kc==KeyCode.NUMPAD9)
            mp1.play();
            viewModel.moveCharacter(keyEvent.getCode());
        keyEvent.consume();
    }

    //region String Property for Bindin
    public StringProperty characterPositionRow = new SimpleStringProperty();

    public StringProperty characterPositionColumn = new SimpleStringProperty();
    public String getCharacterPositionRow() {
        return characterPositionRow.get();
    }

    public StringProperty characterPositionRowProperty() {
        return characterPositionRow;
    }

    public String getCharacterPositionColumn() {
        return characterPositionColumn.get();
    }

    public StringProperty characterPositionColumnProperty() {
        return characterPositionColumn;
    }

    public void setResizeEvent(Scene scene) {
        scene.widthProperty().addListener((observableValue, oldSceneWidth, newSceneWidth) -> {
            mazeDisplayer.setCellWidth();
            mazeDisplayer.redraw();

            characterDisplayer.setCellWidth();
            characterDisplayer.redraw();

            if (!isReached) {
                Position end = viewModel.getMaze().getGoalPosition();
                characterDisplayer.drawAt(end.getRowIndex(), end.getColumnIndex(), characterDisplayer.getImageFileNameGoal());
            }

            solutionDisplayer.setCellWidth();
            solutionDisplayer.redraw();


        });
        scene.heightProperty().addListener((observableValue, oldSceneHeight, newSceneHeight) -> {
            mazeDisplayer.setCellHeight();
            mazeDisplayer.redraw();

            characterDisplayer.setCellHeight();
            characterDisplayer.redraw();
            if (!isReached) {
                Position end = viewModel.getMaze().getGoalPosition();
                characterDisplayer.drawAt(end.getRowIndex(), end.getColumnIndex(), characterDisplayer.getImageFileNameGoal());
            }

            solutionDisplayer.setCellHeight();
            solutionDisplayer.redraw();


        });
    }
    public void saveMaze(ActionEvent actionEvent){
        Stage stage = new Stage();
        FileChooser fileChooser=new FileChooser();
        FileChooser.ExtensionFilter extFilter= new FileChooser.ExtensionFilter("Data files (*.maze)","*.maze");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(stage);
        if(file!=null)
            viewModel.saveMaze(file);
    }

    public void loadMaze(ActionEvent actionEvent){
        Stage stage = new Stage();
        FileChooser fileChooser=new FileChooser();
        FileChooser.ExtensionFilter extFilter= new FileChooser.ExtensionFilter("Data files (*.maze)","*.maze");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(stage);
        if(file!=null)
            viewModel.loadMaze(file);

    }

    public void closeAction (ActionEvent actionEvent){
        Stage stage = (Stage)rootPane.getScene().getWindow();
        stage.fireEvent(new WindowEvent(stage,WindowEvent.WINDOW_CLOSE_REQUEST));
    }
    public void About(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            stage.setTitle("About Us!");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("About.fxml").openStream());
            Scene scene = new Scene(root, 400, 350);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
            stage.show();
        } catch (Exception e) {

        }
    }

    public void Properties(ActionEvent actionEvent) {

        try {
            Stage stage = new Stage();
            stage.setTitle("Properties");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("Properties.fxml").openStream());
            Scene scene = new Scene(root, 400, 350);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
            stage.show();
        } catch (Exception e) {

        }
    }

    public void scrollInOut(ScrollEvent scrollEvent) {
        try {
            viewModel.getMaze();
            ZoomOperator zoomOperator = new ZoomOperator();
            double zoomFactor;
            if (scrollEvent.isControlDown()) {
                zoomFactor = 1.5;
                double deltaY = scrollEvent.getDeltaY();
                if (deltaY < 0) {
                    zoomFactor = 1 / zoomFactor;
                }
                double x = scrollEvent.getSceneX();
                double y = scrollEvent.getSceneY();
                zoomOperator.zoom(mazeDisplayer,solutionDisplayer,characterDisplayer, zoomFactor,x, y);
                scrollEvent.consume();
            }
        } catch (NullPointerException e) {
            scrollEvent.consume();
        }
    }

    public void mousePressed(MouseEvent mouseEvent) {

        double col=mouseEvent.getSceneX()-220.0;
        double row=mouseEvent.getSceneY()-25.0;

        int col2=(int)(col/characterDisplayer.getCellWidth());
        int row2=(int)(row/characterDisplayer.getCellHeight());
        if(viewModel.getCharacterPositionColumn()==col2 && viewModel.getCharacterPositionRow()==row2)
            onCharPressed=true;
        System.out.println("col:"+col2+"   row:"+row2);

    }

    public void mouseReleased(MouseEvent mouseEvent) {


        int col2=(int)((mouseEvent.getSceneX()-220.0)/characterDisplayer.getCellWidth());
        int row2=(int)((mouseEvent.getSceneY()-25.0)/characterDisplayer.getCellHeight());
        if(onCharPressed)
            viewModel.moveCharacterMouse(row2,col2);
        //System.out.println("draggggggggggg!!!!! col:"+col2+"   row:"+row2);

    }
    //endregion

}