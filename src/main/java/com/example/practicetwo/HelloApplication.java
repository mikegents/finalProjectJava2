package com.example.practicetwo;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.control.Button;

import java.io.IOException;
import java.util.LinkedList;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        QuestionBank myBank = new QuestionBank();
        myBank.readMCQ("src/main/resources/mcq.txt");
        myBank.readTFQ("src/main/resources/tfq.txt");


        int[] indxes = {11, 0, 5};
        LinkedList<Question> exam = myBank.selectRandomQuestion(indxes);
        Exam myExam = new Exam(exam);
        myExam.printAllQuestions();

        VBox root = new VBox();


        MenuBar menuBarMain = buildMenuBar();
        root.getChildren().add(menuBarMain);


        HBox hboxGrade = new HBox();
        hboxGrade.setAlignment(Pos.CENTER);
        hboxGrade.getChildren().add(new Label("Grade:"));


        HBox hBoxBanner = buildBanner();
        root.getChildren().add(hBoxBanner);

        root.getChildren().add(hboxGrade);
        root.getChildren().add(new Separator());

        HBox hBoxButtons = buildFooter();
        hBoxButtons.setAlignment(Pos.CENTER);
        root.getChildren().add(hBoxButtons);


        Label helloWorldLabel = new Label("Hello World");
        Scene scene = new Scene(root, 800, 800);
        stage.setTitle("ChampExamen (TM) application (C)");
        stage.setScene(scene);
        stage.show();
    }

    private HBox buildBanner() {
        Image logoObj = new Image("/logo.png");
        ImageView imageViewLogo = new ImageView(logoObj);
        imageViewLogo.setPreserveRatio(true);
        imageViewLogo.setFitHeight(100);
        Image bannerObj = new Image("/banner.png");
        ImageView imageViewBanner = new ImageView(bannerObj);
        imageViewBanner.setPreserveRatio(true);
        imageViewBanner.setFitHeight(100);
        HBox hboxBanner = new HBox();
        hboxBanner.getChildren().addAll(imageViewLogo, imageViewBanner);
        return hboxBanner;
    }

    private HBox buildFooter() {
        HBox hboxFooter = new HBox();
        Button buttonClear = new Button("Clear");
        Button ButtonSave = new Button("Save");
        Button buttonSubmit = new Button("Submit");
        hboxFooter.getChildren().addAll(buttonClear, ButtonSave, buttonSubmit);
        return hboxFooter;
    }

    private MenuBar buildMenuBar() {
        MenuBar menuBarMain = new MenuBar();
        Menu menuFile = new Menu("File");
        Menu menuEdit = new Menu("Edit");
        Menu menuQuiz = new Menu("Quiz");
        Menu menuExtras = new Menu("Extras");
        Menu menuHelp = new Menu("Help");
        menuBarMain.getMenus().addAll(menuFile, menuEdit, menuQuiz, menuExtras, menuHelp);
        return menuBarMain;
    }

    public static void main(String[] args) {
        launch();
    }
}