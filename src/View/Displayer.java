package View;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public abstract class Displayer extends Canvas {
    private int height;
    private int width;
    private double cellHeight;
    private double cellWidth;

    public void setDimensions(int height, int width){
        this.height=height;
        this.width=width;
        setCellHeight();
        setCellWidth();
    }
    public double getCellHeight() {
        return cellHeight;
    }

    public double getCellWidth() {
        return cellWidth;
    }

    public void setCellHeight(){
        double canvasHeight = getHeight();
        this.cellHeight=canvasHeight/height;
    }

    public void setCellWidth(){
        double canvasWidth = getWidth();
        this.cellWidth=canvasWidth/width;
    }
    public void drawAt( int row,int col,String path){

        try {
            Image characterImage = new Image(new FileInputStream(path));
            GraphicsContext gc = getGraphicsContext2D();

            //Draw Character
            gc.drawImage(characterImage, col * getCellWidth(), row * getCellHeight(), getCellWidth(), getCellHeight());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void clearCanvas(){
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
    }



}
