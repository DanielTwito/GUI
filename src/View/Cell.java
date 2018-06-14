package View;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Cell extends Canvas {
    private double width;
    private double height;
    private String imageFilePath;

    public Cell(double width, double height, String imageFilePath) {
        super(width, height);
        this.width = width;
        this.height = height;
        this.imageFilePath = imageFilePath;
    }

    public void draw() {
        String path=imageFilePath.substring(23,imageFilePath.length()-1);
        if (new File(path).exists()) {
            try {
                GraphicsContext gc = getGraphicsContext2D();
                gc.clearRect(0, 0, getWidth(), getHeight());
                Image image = new Image(new FileInputStream(path));
                gc.drawImage(image, 0, 0, width, height);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }
}
