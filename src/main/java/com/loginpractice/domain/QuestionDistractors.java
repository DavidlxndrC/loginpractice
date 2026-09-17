package com.loginpractice.domain;

import java.util.ArrayList;
import java.util.List;

public class QuestionDistractors {

    private final List<String> options = new ArrayList<>();

    public QuestionDistractors() {
    }

    public QuestionDistractors(List<String> options) {
        this.options.addAll(options);
    }

    public void addOption(String option) {
        options.add(option);
    }

    public List<String> getOptions() {
        return options;
    }
}
