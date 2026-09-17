package com.loginpractice.service;

import com.loginpractice.access.IQuestionRepository;
import com.loginpractice.domain.Question;
import com.loginpractice.domain.QuestionDistractors;
import com.loginpractice.domain.StatusQuestion;
import com.loginpractice.infra.Subject;

import java.util.List;
import java.util.Optional;

public class QuestionService extends Subject {

    private final IQuestionRepository questionRepository;

    public QuestionService(IQuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    public Optional<Question> getQuestionById(int id) {
        return questionRepository.findById(id);
    }

    public Question createQuestion(String name, String statement, QuestionDistractors options, String correctAnswer) {

        int newId = getAllQuestions().stream().mapToInt(Question::getId).max().orElse(0) + 1;

        Question question = new Question(newId, name, statement, options, correctAnswer, StatusQuestion.DRAFT);
        questionRepository.save(question);

        notifyObservers();

        return question;
    }

    public void updateContent(int id, String name, String statement, QuestionDistractors options, String correctAnswer) {

        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pregunta no encontrada: " + id));

        question.setName(name);
        question.setStatement(statement);
        question.setOptions(options);
        question.setCorrectAnswer(correctAnswer);

        questionRepository.update(question);
    }

    public void changeStatus(int id, StatusQuestion newStatus) {

        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pregunta no encontrada: " + id));

        question.setStatus(newStatus);
        questionRepository.update(question);

        notifyObservers();
    }
}
