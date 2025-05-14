package com.example.practicetwo;

import java.util.HashMap;
import java.util.LinkedList;

public class Exam {

    HashMap<Integer, Question> questions;
    HashMap<Integer, String> submittedAnswers;
    private int grade;

    public Exam() {
        this.questions = new HashMap<>();
        this.submittedAnswers = new HashMap<>();
    }

    public Exam(HashMap<Integer, Question> questions, HashMap<Integer, String> submittedAnswers) {
        this.questions = new HashMap<>();
        this.questions.putAll(questions);
        this.submittedAnswers = new HashMap<>();
        this.submittedAnswers.putAll(submittedAnswers);
    }

    public void clearQuestions() {
        this.questions.clear();
        this.submittedAnswers.clear();
    }

    public Question getQuestion(int i) {
        return this.questions.get(i);
    }

    public String getSubmittedAnswer(int i) {
        return this.submittedAnswers.get(i);
    }

    public void printAllQuestions() {
        for (int key : questions.keySet()) {
            System.out.println(this.questions.get(key));
        }

    }

    public HashMap<Integer, Question> getQuestions() {
        return questions;
    }


    public HashMap<Integer, String> getSubmittedAnswers() {
        return submittedAnswers;
    }

    public void setSubmittedAnswers(HashMap<Integer, String> submittedAnswers) {
        this.submittedAnswers = new HashMap<>();
        this.submittedAnswers.putAll(submittedAnswers);
    }

    public Exam(LinkedList<Question> questionList) {
        this.questions = new HashMap<>();
        this.submittedAnswers = new HashMap<>();
        int i = 1;
        for (Question questions : questionList) {
            this.questions.put(i, questions);
            this.submittedAnswers.put(i, ""); // important initialize tbe submittedAnswers hashmap
            i++;
        }


    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public void clearGrade() {
        this.grade = 0;
    }
}

