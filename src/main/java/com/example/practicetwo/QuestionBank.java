package com.example.practicetwo;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class QuestionBank {

    private LinkedList<Question> questions;


    public QuestionBank() {
        this.questions = new LinkedList<>();
    }

    public QuestionBank(LinkedList<Question> questions) {
        this.questions = new LinkedList<>(questions);
        this.questions.addAll(questions);

    }

    public LinkedList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(LinkedList<Question> questions) {
        this.questions = new LinkedList<>(questions);
        this.questions.addAll(questions);
    }

    public void printQuestions() {
        for (Question q : this.getQuestions()) {
            System.out.println(q);
        }
    }

    public void readMCQ(String fname) throws FileNotFoundException {

        try {
            File fileObj = new File(fname);
            Scanner sc = new Scanner(fileObj);

            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                String[] items = line.split("\\.");
                String questionText = items[1];
               // System.out.println(questionText);
                LinkedList<String> options = new LinkedList<>();
                String firstThreeLetters;
                do {
                    line = sc.nextLine().trim();
                    firstThreeLetters = line.substring(0, 3);
                    if (!firstThreeLetters.equals("ANS")) {
                        String optionText = line.substring(2);
                        options.add(optionText);
                    }
                } while (!firstThreeLetters.equals("ANS"));


                String correctAnswer = line.substring(4).trim();
                //Now the Line variable Contains the answer
                //System.out.println(line);

                MCQuestion question = new MCQuestion(questionText, correctAnswer, questionType.MCQ, options);
                questions.add(question);

            }
        } catch (FileNotFoundException e) {
            System.out.println("Error file not found " + fname);
        }

    }


    public void readTFQ(String fName) {

        try {
            File fileObj = new File(fName);
            Scanner myScanner = new Scanner(fileObj);

            while (myScanner.hasNext()) {
                String line = myScanner.nextLine();
                String[] items = line.split("\\.");
                String questionText = items[1];
                //System.out.println(questionText);
                line = myScanner.nextLine().trim();
                // now the line variable contains the answer
                String correctAnswer = line.substring(4).trim();
                TFQQuestion question = new TFQQuestion(questionText, correctAnswer, questionType.TFQ);

                questions.add(question);
                //System.out.println(line);
            }

        } catch (java.io.FileNotFoundException ex) {
            System.out.println("Error: File not found. " + fName);
        }
    }

    public LinkedList<Question> selectRandomQuestion(int[] indices) {
        LinkedList<Question> examQuestions = new LinkedList<>();
        int currentIndex =0;
        try {
            for (int i : indices) {
                currentIndex = i;
                Question question = this.getQuestions().get(i);
                examQuestions.add(question);
            }
        }catch (IndexOutOfBoundsException e) {
            System.out.println("Error: Index out of bounds" + "Question: " + currentIndex);
        }
            return examQuestions;
        }
    }


