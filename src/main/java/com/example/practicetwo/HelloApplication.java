package com.example.practicetwo;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class HelloApplication extends Application {
    private Exam myExam;
    private static int grade = 0;
    private static Label labelGrade = new Label("Grade: ");
    private List<ToggleGroup> toggleGroups = new ArrayList<>();

    @Override
    public void start(Stage stage) throws IOException {

        QuestionBank myBank = new QuestionBank();
        myBank.readMCQ("src/main/resources/mcq.txt");
        myBank.readTFQ("src/main/resources/tfq.txt");


        int[] indxes = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
        LinkedList<Question> exam = myBank.selectRandomQuestion(indxes);
        myExam = new Exam(exam);
        myExam.printAllQuestions();

        VBox root = new VBox();

        MenuBar menuBarMain = buildMenuBar();
        root.getChildren().add(menuBarMain);

        HBox hboxGrade = new HBox();
        hboxGrade.setAlignment(Pos.CENTER);
        hboxGrade.getChildren().add(labelGrade);

        VBox[] vboxQuestions = buildQuestionsVboxes();


        HBox hBoxBanner = buildBanner();
        root.getChildren().add(hBoxBanner);

        root.getChildren().add(hboxGrade);



        root.getChildren().addAll(vboxQuestions);
        root.getChildren().add(new Separator());

        VBox vBoxAllQuestions = new VBox(10);
        vBoxAllQuestions.getChildren().addAll(vboxQuestions);
        ScrollPane scrollPaneForQuestions = new ScrollPane(vBoxAllQuestions);
        scrollPaneForQuestions.setFitToWidth(true); // or any needed config
        root.getChildren().add(scrollPaneForQuestions);

        HBox hBoxButtons = buildFooter();
        hBoxButtons.setAlignment(Pos.CENTER);
        root.getChildren().add(hBoxButtons);


        Label helloWorldLabel = new Label("Hello World");
        Scene scene = new Scene(root, 800, 800);
        stage.setTitle("ChampExamen (TM) application (C)");
        stage.setScene(scene);
        stage.show();
    }

    private VBox[] buildQuestionsVboxes() {
        int numberOfQuestionsInExam = myExam.questions.size();
        VBox[] vBoxes = new VBox[numberOfQuestionsInExam];

        for (int i = 0; i<numberOfQuestionsInExam; i++) {
            int qNumber = i+1;
            Question question = myExam.getQuestion(qNumber);
            if (question.getQuestionType() == questionType.TFQ) {
                vBoxes[i] = buildTrueFalseQ(qNumber, (TFQQuestion) question);
            } else { // it is MCQ
                vBoxes[i] = buildMCQ(qNumber, (MCQuestion) question);
            }
        }
        return vBoxes;
    }

    public VBox buildTrueFalseQ(int questionNumber, TFQQuestion tfQuestion1){
        String qText = String.format("Q%d. %s", questionNumber, tfQuestion1.getQuestionText().trim());
        Label labelQuestionText = new Label(qText);
        RadioButton radioButtonTrue = new RadioButton("True ");
        RadioButton radioButtonFalse = new RadioButton("False");
        ToggleGroup toggleGroup = new ToggleGroup();
        radioButtonTrue.setToggleGroup(toggleGroup);
        radioButtonFalse.setToggleGroup(toggleGroup);
        toggleGroups.add(toggleGroup);
        // handle radio button click
        radioButtonTrue.setOnAction(e -> recordAnswer(questionNumber, "T"));
        radioButtonFalse.setOnAction(e -> recordAnswer(questionNumber, "F"));

        HBox hBox = new HBox(10, radioButtonTrue, radioButtonFalse);
        VBox vBox = new VBox(labelQuestionText, hBox);
        vBox.getChildren().add(new Separator());
        return vBox;
    }

    private void recordAnswer(int n, String t) {
        this.myExam.getSubmittedAnswers().put(n, t);
        System.out.println(n + " -> " + this.myExam.getSubmittedAnswers().get(n));
    }

    public VBox buildMCQ(int questionNumber, MCQuestion mcqQuestion){
        String qText = String.format("Q%d. %s", questionNumber, mcqQuestion.getQuestionText().trim());
        Label labelQuestionText = new Label(qText);
        VBox vBox = new VBox(labelQuestionText);

        LinkedList<String> options = mcqQuestion.getOptions();
        ToggleGroup toggleGroup = new ToggleGroup();
        String[] optionLetters = {"A", "B", "C", "D", "E", "F", "G"};
        for (int i=0; i<options.size(); i++){
            String s = options.get(i);
            RadioButton radioButton = new RadioButton(s);
            radioButton.setToggleGroup(toggleGroup);
            toggleGroups.add(toggleGroup);
            vBox.getChildren().add(radioButton);
            int finalI = i;
            radioButton.setOnAction(e -> recordAnswer(questionNumber, optionLetters[finalI]));
        }

        return vBox;
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
        HBox hboxFooter = new HBox(10);
        Button buttonClear = new Button("Clear");
        Button buttonSave = new Button("Save");
        Button buttonSubmit = new Button("Submit");

        // register to actions
        buttonClear.setOnAction(e -> clearExamAnswers());
        buttonSave.setOnAction(e -> saveExamAnswers());
        buttonSubmit.setOnAction(new SubmitEventHandler(this.myExam));

        hboxFooter.getChildren().addAll(buttonClear, buttonSave, buttonSubmit);
        return hboxFooter;
    }

    private void saveExamAnswers() {
    }

    private void clearExamAnswers() {
      for (ToggleGroup group : toggleGroups) {
          group.selectToggle(null);
        }
       myExam.clear();
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
    public static class SubmitEventHandler implements EventHandler<ActionEvent> {

        private Exam examObj;
        public SubmitEventHandler(Exam myExam) {
            this.examObj = myExam;
        }

        @Override
        public void handle(ActionEvent actionEvent) {
            System.out.println("Submit button clicked");
            int numberOfQuestions = this.examObj.getQuestions().size();
            grade = 0;
            for (int i=1; i<=numberOfQuestions; i++){
                Question question = this.examObj.getQuestion(i);
                String correctAnswer = question.getCorrectAnswer();
                String submittedAnswer = this.examObj.getSubmittedAnswer(i);
                System.out.println(correctAnswer + " : " + submittedAnswer);
                if (correctAnswer.equals(submittedAnswer)){
                    grade = grade + 1;
                }
            }
            System.out.println("Your grade is : " + grade);
            labelGrade.setText("Grade: " + grade);
        }
    }
}