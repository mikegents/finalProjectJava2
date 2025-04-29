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

    public void readMCQ(String fname) throws FileNotFoundException {

        try {
            File fileObj = new File(fname);
            Scanner sc = new Scanner(fileObj);

            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                String[] items = line.split(".");
                String questionText = items[1];
                System.out.println(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error file not found " + fname);
        }



        }


    }

