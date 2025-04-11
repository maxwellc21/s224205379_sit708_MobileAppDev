// models/Question.java
package com.quizapplication.models;

import java.util.List;

public class Question {
    public String questionType;
    public String question;
    public List<String> options;
    public List<Integer> correctAnswers;
    public String hint;
    public String explanation;
}
