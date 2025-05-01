package com.example.practicetwo;

public class TFQQuestion extends Question {


    public TFQQuestion() {
        super();
    }

    public TFQQuestion(String questionText, String correctAnswer, questionType questionType) {
        super(questionText, correctAnswer, questionType);
    }

    @Override
    public String toString() {
        return "TFQQuestion{" + this.getQuestionText()+"\n" +
                "\nANS " + this.getCorrectAnswer();
    }
}
