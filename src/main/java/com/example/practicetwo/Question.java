package com.example.practicetwo;

public class Question {

    private String questionText;

    private String correctAnswer;

    private questionType questionType;


    public Question(String questionText, String correctAnswer, questionType questionType) {
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
        this.questionType = questionType;
    }

    public Question() {

        this.questionText = "";
        this.correctAnswer = "";
        this.questionType = questionType;

    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public questionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(questionType questionType) {
        this.questionType = questionType;
    }
}
