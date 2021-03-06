
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Observable;
import java.util.Observer;

public class MyViewController implements Observer, IView {

    @FXML
    private ViewModel viewModel;
    private boolean isReached;
    public MazeDisplayer mazeDisplayer;
    public CharacterDisplayer characterDisplayer;
    public SolutionDisplayer solutionDisplayer;
    public Canvas picDisplayer;
    public javafx.scene.layout.BorderPane rootPane;
    public javafx.scene.control.TextField txtfld_rowsNum;
    public javafx.scene.control.TextField txtfld_columnsNum;
    public javafx.scene.control.Label lbl_rowsNum;
    public javafx.scene.control.Label lbl_columnsNum;
    public javafx.scene.control.Button btn_generateMaze;
    public javafx.scene.control.Button btn_solveMaze;
    // public javafx.scene.control.MenuItem menu_save;

    private MediaPlayer backgroundSound = new MediaPlayer(new Media( new File("resources\\Sound\\background.mp3").toURI().toString()));
    private MediaPlayer finishSound = new MediaPlayer(new Media(new File("resources\\Sound\\finish.mp3").toURI().toString()));
    private MediaPlayer moveSound;
    private boolean onCharPressed=false;
    private boolean solvePressed;

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
                isReached = viewModel.isReached();
                String path = new File("resources\\Sound\\move.mp3").toURI().toString();
                moveSound= new MediaPlayer(new Media(path));
                displayCharactetr();
                moveSound.play();
                if (!isReached) {
                    Position end = viewModel.getMaze().getGoalPosition();
                    characterDisplayer.drawAt(end.getRowIndex(), end.getColumnIndex(), characterDisplayer.getImageFileNameGoal());
                }
            }

            if(arg.toString()=="solve")
                displaySolution();

            if(arg.toString()=="loaded") {
                GraphicsContext gc = picDisplayer.getGraphicsContext2D();
                gc.clearRect(0, 0, picDisplayer.getWidth(), picDisplayer.getHeight());
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
                    if(backgroundSound != null)
                        backgroundSound.stop();
                    if(finishSound != null)
                        finishSound.stop();
                    backgroundSound.play();
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
        solvePressed=false;
        GraphicsContext gc = picDisplayer.getGraphicsContext2D();
        gc.clearRect(0, 0, picDisplayer.getWidth(), picDisplayer.getHeight());
        if(backgroundSound !=null)
            backgroundSound.stop();
        isReached=false;
        String path = new File("resources\\Sound\\background.mp3").toURI().toString();
        backgroundSound = new MediaPlayer(new Media(path));
        if(finishSound !=null) {
            finishSound.stop();
        }
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
            backgroundSound.play();
        }catch (NumberFormatException e){showAlert("You Must Enter a Number");}
    }

    public void solveMaze(ActionEvent actionEvent) {
        solvePressed=true;
        btn_solveMaze.setDisable(true);
        viewModel.solveMaze();
    }

    public void reachGoal() {
        btn_solveMaze.setDisable(true);
        isReached=true;
        backgroundSound.stop();
        if(!solvePressed)
            showAlert("Amazing..!!!\nYou have reached the goal by yourself!!\n Bravo, you found El Professor!!");
        else
            showAlert("Thats good!\nYou have reached the goal according App solution.\nNow try by yourself.");
        finishSound.setStartTime(new Duration(13000));
        finishSound.play();

    }

    private void showAlert(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(alertMessage);
        alert.show();
    }



    public void KeyPressed(KeyEvent keyEvent) {
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
        if(finishSound !=null)
            finishSound.stop();


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

    public void mouseDrag(MouseEvent mouseEvent) {


        int col2=(int)((mouseEvent.getSceneX()-220.0)/characterDisplayer.getCellWidth());
        int row2=(int)((mouseEvent.getSceneY()-25.0)/characterDisplayer.getCellHeight());
        if(onCharPressed)
            viewModel.moveCharacterMouse(row2,col2);
        //System.out.println("draggggggggggg!!!!! col:"+col2+"   row:"+row2);

    }

    public void mouseReleased(MouseEvent mouseEvent) {

        onCharPressed=false;

    }

    public void GameRules(ActionEvent actionEvent) {


        try {
            Stage stage = new Stage();
            stage.setTitle("Game Rules");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("GameRules.fxml").openStream());
            Scene scene = new Scene(root, 650, 450);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
            stage.show();
        } catch (Exception e) {

        }
    }

    public void entryShow() {

        GraphicsContext gc = picDisplayer.getGraphicsContext2D();
        gc.clearRect(0, 0, picDisplayer.getWidth(), picDisplayer.getHeight());

        try {
            gc.drawImage(new Image(new FileInputStream("resources/Images/opening.jpg")), 0, 0, picDisplayer.getWidth(), picDisplayer.getHeight());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    //endregion

}