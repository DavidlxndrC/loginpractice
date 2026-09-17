package com.loginpractice.domain;

public class Question {

    private int id;
    private String name;
    private String statement;
    private QuestionDistractors options;
    private String correctAnswer;
    private StatusQuestion status;

    public Question(int id, String name, String statement, QuestionDistractors options,
                     String correctAnswer, StatusQuestion status) {

        this.id = id;
        this.name = name;
        this.statement = statement;
        this.options = options;
        this.correctAnswer = correctAnswer;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStatement() {
        return statement;
    }

    public QuestionDistractors getOptions() {
        return options;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public void setOptions(QuestionDistractors options) {
        this.options = options;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public StatusQuestion getStatus() {
        return status;
    }

    public void setStatus(StatusQuestion status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return id + " - " + name;
    }
}
