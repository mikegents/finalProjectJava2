package com.example.practicetwo;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Label;

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

        System.out.println("Hello World");
        Label helloWorldLabel = new Label("Hello World");
        Scene scene = new Scene(helloWorldLabel, 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}