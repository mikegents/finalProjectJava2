package com.example.practicetwo;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class HelloApplication extends Application {
    private Exam myExam;
    private LinkedList<ToggleGroup> allToggleGroups = new LinkedList<>();
    private Label labelShowGrades = new Label("Grade: ");
    private int grade;
    private boolean isDarkMode = false;


    @Override
    public void start(Stage stage) throws IOException {
        QuestionBank myBank = new QuestionBank();
        myBank.readMCQ("src/main/resources/mcq.txt");
        myBank.readTFQ("src/main/resources/tfq.txt");



        List<Integer> indexList = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6));
        for (int i = 0; i < 3; i++) {
            indexList.add(ThreadLocalRandom.current().nextInt(7, 15));
        }
        int[] indexes = indexList.stream().mapToInt(i -> i).toArray();
        LinkedList<Question> exam = myBank.selectRandomQuestion(indexes);

        myExam = new Exam(exam);
        myExam.printAllQuestions();

        VBox root = new VBox(10);

        MenuBar menuBarMain = buildMenuBar();
        root.getChildren().add(menuBarMain);
        HBox hBoxBanner = buildBanner();
        root.getChildren().add(hBoxBanner);

        HBox hboxGrade = new HBox(labelShowGrades);
        hboxGrade.setAlignment(Pos.CENTER);
        root.getChildren().add(hboxGrade);
        root.getChildren().add(new Separator());

        VBox[] vboxQuestions = buildQuestionsVboxes();
        VBox vBoxAllQuestions = new VBox(10);
        vBoxAllQuestions.getChildren().addAll(vboxQuestions);

        ScrollPane scrollPaneForQuestions = new ScrollPane(vBoxAllQuestions);
        scrollPaneForQuestions.setFitToWidth(true);
        root.getChildren().add(scrollPaneForQuestions);

        root.getChildren().add(new Separator());
        HBox hBoxButtons = buildFooter();
        hBoxButtons.setAlignment(Pos.CENTER);
        root.getChildren().add(hBoxButtons);

        Scene scene = new Scene(root, 1200, 800);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        stage.setTitle("ChampExamen (TM) application (C)");
        stage.setScene(scene);
        stage.show();
    }

    private VBox[] buildQuestionsVboxes() {
        int numberOfQuestionsInExam = myExam.questions.size();
        VBox[] vBoxes = new VBox[numberOfQuestionsInExam];

        for (int i = 0; i < numberOfQuestionsInExam; i++) {
            int qNumber = i + 1;
            Question question = myExam.getQuestion(qNumber);
            if (question.getQuestionType() == questionType.TFQ) {
                vBoxes[i] = buildTrueFalseQ(qNumber, (TFQQuestion) question);
            } else {
                vBoxes[i] = buildMCQ(qNumber, (MCQuestion) question);
            }
        }
        return vBoxes;
    }

    public VBox buildTrueFalseQ(int questionNumber, TFQQuestion tfQuestion1) {
        String qText = String.format("Q%d. %s", questionNumber, tfQuestion1.getQuestionText().trim());
        Label labelQuestionText = new Label(qText);
        RadioButton radioButtonTrue = new RadioButton("True");
        RadioButton radioButtonFalse = new RadioButton("False");
        ToggleGroup toggleGroup = new ToggleGroup();
        radioButtonTrue.setToggleGroup(toggleGroup);
        radioButtonFalse.setToggleGroup(toggleGroup);
        allToggleGroups.add(toggleGroup);

        radioButtonTrue.setOnAction(e -> recordAnswer(questionNumber, "T"));
        radioButtonFalse.setOnAction(e -> recordAnswer(questionNumber, "F"));

        HBox hBox = new HBox(10, radioButtonTrue, radioButtonFalse);
        VBox vBox = new VBox(labelQuestionText, hBox, new Separator());
        return vBox;
    }

    public VBox buildMCQ(int questionNumber, MCQuestion mcqQuestion) {
        String qText = String.format("Q%d. %s", questionNumber, mcqQuestion.getQuestionText().trim());
        Label labelQuestionText = new Label(qText);
        VBox vBox = new VBox(labelQuestionText);

        LinkedList<String> options = mcqQuestion.getOptions();
        ToggleGroup toggleGroup = new ToggleGroup();
        allToggleGroups.add(toggleGroup);

        String[] optionLetters = {"A", "B", "C", "D", "E", "F", "G"};
        for (int i = 0; i < options.size(); i++) {
            String s = options.get(i);
            RadioButton radioButton = new RadioButton(s);
            radioButton.setToggleGroup(toggleGroup);
            vBox.getChildren().add(radioButton);
            int finalI = i;
            radioButton.setOnAction(e -> recordAnswer(questionNumber, optionLetters[finalI]));
        }
        vBox.getChildren().add(new Separator());
        return vBox;
    }

    private void recordAnswer(int n, String t) {
        this.myExam.getSubmittedAnswers().put(n, t);
        System.out.println(n + " -> " + this.myExam.getSubmittedAnswers().get(n));
    }

    private void clearExamAnswers() {
        for (ToggleGroup toggleGroup : allToggleGroups) {
            if (toggleGroup.getSelectedToggle() != null) {
                toggleGroup.getSelectedToggle().setSelected(false);
            }
        }
        myExam.getSubmittedAnswers().clear();
        labelShowGrades.setText("Grade: ");
        System.out.println("All answers cleared.");
    }

    private HBox buildFooter() {
        HBox hboxFooter = new HBox(10);
        Button buttonClear = new Button("Clear");
        Button buttonSave = new Button("Save");
        Button buttonSubmit = new Button("Submit");

        buttonClear.setOnAction(e -> clearExamAnswers());
        buttonSave.setOnAction(e -> saveExamAnswers());
        buttonSubmit.setOnAction(new SubmitEventHandler(this.myExam));

        hboxFooter.getChildren().addAll(buttonClear, buttonSave, buttonSubmit);
        return hboxFooter;
    }

    private void saveExamAnswers() {
        try (FileWriter writer = new FileWriter("saved_answers.txt")) {
            for (Map.Entry<Integer, String> entry : myExam.getSubmittedAnswers().entrySet()) {
                writer.write("Q" + entry.getKey() + ": " + entry.getValue() + "\n");
            }
            System.out.println("Your Answers have been saved");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Answers Saved");
            alert.setHeaderText(null);
            alert.setContentText("Your Answers have been saved");
            alert.showAndWait();
        } catch (IOException e) {
            System.out.println("Failed to save answers: " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Save Error");
            alert.setHeaderText("Failed to Save Answers");
            alert.setContentText("An error occurred while saving your answers. Please try again.");
            alert.showAndWait();
        }
    }

    private MenuBar buildMenuBar() {
        MenuBar menuBarMain = new MenuBar();


        Menu fileMenu = new Menu("File");
        MenuItem saveItem = new MenuItem("Save Answers");
        MenuItem clearItem = new MenuItem("Clear Answers");
        MenuItem exitItem = new MenuItem("Exit");
        saveItem.setOnAction(e -> saveExamAnswers());
        clearItem.setOnAction(e -> clearExamAnswers());
        exitItem.setOnAction(e -> System.exit(0));
        fileMenu.getItems().addAll(saveItem, clearItem, new SeparatorMenuItem(), exitItem);


        Menu editMenu = new Menu("Edit");
        MenuItem cutItem = new MenuItem("Cut");
        MenuItem copyItem = new MenuItem("Copy");
        MenuItem pasteItem = new MenuItem("Paste");
        editMenu.getItems().addAll(cutItem, copyItem, pasteItem);


        Menu quizMenu = new Menu("Quiz");
        MenuItem startItem = new MenuItem("Start Quiz");
        MenuItem viewResults = new MenuItem("View Results");
        viewResults.setOnAction(e -> viewResults());
        quizMenu.getItems().addAll(startItem, viewResults);


        Menu extrasMenu = new Menu("Extras");
        MenuItem motivationItem = new MenuItem("Motivate Me");
        MenuItem darkModeItem = new MenuItem("Dark Mode");
        motivationItem.setOnAction(e -> motivateMe());
        darkModeItem.setOnAction(e -> setDarkMode());
        extrasMenu.getItems().addAll(motivationItem, darkModeItem);


        Menu helpMenu = new Menu("Help");
        MenuItem helpItem = new MenuItem("Help");
        MenuItem aboutAppItem = new MenuItem("About App");
        helpItem.setOnAction(e -> showHelpDialog());
        aboutAppItem.setOnAction(e -> showAboutDialog());
        helpMenu.getItems().addAll(aboutAppItem, helpItem);

        menuBarMain.getMenus().addAll(fileMenu, editMenu, quizMenu, extrasMenu, helpMenu);
        return menuBarMain;
    }

    private void motivateMe() {
        String[] quotes = {
                "You’ve got this 💪",
                "One question at a time!",
                "Champions aren't born, they're built.",
                "Deep breaths. You're doing great!",
                "Believe in yourself. You’re smarter than you think."
        };
        String randomQuote = quotes[ThreadLocalRandom.current().nextInt(quotes.length)];
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Motivation Boost");
        alert.setHeaderText("Here's some motivation:");
        alert.setContentText(randomQuote);
        alert.showAndWait();
    }

    private void viewResults() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Results");
        alert.setHeaderText("Results: ");
        alert.setContentText("Your Grade is: " + String.valueOf(grade));
        alert.showAndWait();
    }

    



    private void setDarkMode() {
        Scene scene = labelShowGrades.getScene();
        scene.getStylesheets().clear();

        if (isDarkMode) {
            scene.getStylesheets().add(getClass().getResource("/light.css").toExternalForm());
            isDarkMode = false;
        } else {
            scene.getStylesheets().add(getClass().getResource("/dark.css").toExternalForm());
            isDarkMode = true;
        }
    }

    private void showHelpDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("It seems you need help...");
        alert.setHeaderText("ChampExamen");
        alert.setContentText("Honestly not sure, maybe ask the teacher ;) ");
        alert.showAndWait();
    }

    private void showAboutDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About ChampExamen");
        alert.setHeaderText("ChampExamen");
        alert.setContentText("Made by yours truly -Mike Freak");
        alert.showAndWait();
    }

    private HBox buildBanner() {
        // Load and scale logo
        Image logoObj = new Image("/logo.png");
        ImageView imageViewLogo = new ImageView(logoObj);
        imageViewLogo.setPreserveRatio(true);
        imageViewLogo.setFitHeight(180); // larger height for the logo

        // Load and scale banner
        Image bannerObj = new Image("/banner.png");
        ImageView imageViewBanner = new ImageView(bannerObj);

        imageViewBanner.setFitHeight(180); // match logo height
        imageViewBanner.setFitWidth(545);  // wider banner

        // Combine in a container with no spacing
        HBox hboxBanner = new HBox(0, imageViewLogo, imageViewBanner);
        hboxBanner.setAlignment(Pos.CENTER);// 0 spacing

        return hboxBanner;
    }

    public static void main(String[] args) {
        launch();
    }

    public class SubmitEventHandler implements EventHandler<ActionEvent> {
        private final Exam examObj;

        public SubmitEventHandler(Exam myExam) {
            this.examObj = myExam;
        }

        @Override
        public void handle(ActionEvent actionEvent) {
            int numberOfQuestions = this.examObj.getQuestions().size();
            grade = 0;
            for (int i = 1; i <= numberOfQuestions; i++) {
                Question question = this.examObj.getQuestions().get(i);
                String correctAnswer = question.getCorrectAnswer();
                String submittedAnswer = this.examObj.getSubmittedAnswers().get(i);
                System.out.println(correctAnswer + " : " + submittedAnswer);
                if (correctAnswer != null && correctAnswer.equalsIgnoreCase(submittedAnswer)) {
                    grade++;
                }
            }
            this.examObj.setGrade(grade);
            labelShowGrades.setText(String.format("Grade : %d out of %d (%.2f%%)", grade, numberOfQuestions, (100.0 * grade / numberOfQuestions)));
            System.out.println("Your grade is : " + grade);
        }
    }
}
