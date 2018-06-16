package View;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;


import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MazeDisplayer extends Displayer {
    private Maze maze;


    public void setMaze(Maze maze) {
        this.maze = maze;
        redraw();
    }

    public void redraw() {
        if (maze != null) {
            try {
                Image wallImage = new Image(new FileInputStream(imageFileNameWall.get()));
                GraphicsContext gc = getGraphicsContext2D();
                gc.clearRect(0, 0, getWidth(), getHeight());

                //Draw Maze
                for (int i = 0; i < maze.getRow(); i++) {
                    for (int j = 0; j <maze.getCol(); j++) {
                        if (maze.getValueAt(new Position(i,j)) == 1) {
                            gc.drawImage(wallImage, j * getCellWidth(), i * getCellHeight(), getCellWidth(), getCellHeight());
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    //region Properties
    private StringProperty imageFileNameWall = new SimpleStringProperty();



    public String getImageFileNameWall() {
        return imageFileNameWall.get();
    }

    public void setImageFileNameWall(String imageFileNameWall) {
        this.imageFileNameWall.set(imageFileNameWall);
    }
    //endregion

}
