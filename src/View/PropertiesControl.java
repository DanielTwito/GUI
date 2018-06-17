package View;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;

import javax.security.auth.login.Configuration;
import java.util.Properties;

public class PropertiesControl {

    @FXML
    public javafx.scene.control.Label lbl_GenAlgo;
    public javafx.scene.control.Label lbl_solAlgo;


    private StringProperty algoGenNum = new SimpleStringProperty();
    private StringProperty algoSolNum = new SimpleStringProperty();
    private String algoGen;
    private String algoSol;


    public String getAlgoSolNum() {
        get();
        return algoSol;
    }

    public String getAlgoGenNum() {
        get();
        return algoGen;
    }

    private void get() {

        Properties configFile;
        configFile = new Properties();

        try {
            configFile.load(this.getClass().getClassLoader().getResourceAsStream("config.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        algoGen = configFile.getProperty("GenerateMaze_method").equals("1") ? "Prim" : "Random";
        algoSol = configFile.getProperty("searching_algo");

    }



}
