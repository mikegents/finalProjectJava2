package com.example.practicetwo;

import java.util.LinkedList;

public class MCQuestion extends Question {

    LinkedList<String>options;

    public MCQuestion() {
        super();
        options = new LinkedList<>();
    }

    public MCQuestion(String questionText, String correctAnswer, questionType questionType,LinkedList<String>optionsParam) {
        super(questionText,correctAnswer,questionType);
        options.addAll(optionsParam);
    }
}
