package com.loginpractice;

import com.loginpractice.access.FakeQuestionRepository;
import com.loginpractice.domain.Question;
import com.loginpractice.domain.QuestionDistractors;
import com.loginpractice.domain.StatusQuestion;
import com.loginpractice.infra.IObserver;
import com.loginpractice.service.QuestionService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QuestionServiceTest {

    private FakeQuestionRepository fakeQuestionRepository;
    private QuestionService questionService;

    @BeforeEach
    void setUp() {
        fakeQuestionRepository = new FakeQuestionRepository();
        questionService = new QuestionService(fakeQuestionRepository);

        fakeQuestionRepository.save(new Question(
                1, "Pregunta sobre DDD", "¿Cuál es el objetivo principal de DDD?",
                new QuestionDistractors(List.of("A", "B", "C", "D")),
                "B", StatusQuestion.DRAFT
        ));
    }

    @Test
    void shouldChangeQuestionStatus() {

        questionService.changeStatus(1, StatusQuestion.PENDING_REVIEW);

        StatusQuestion updatedStatus = questionService.getQuestionById(1).get().getStatus();
        assertEquals(StatusQuestion.PENDING_REVIEW, updatedStatus);
    }

    @Test
    void shouldNotifyObserversWhenStatusChanges() {

        boolean[] wasNotified = { false };
        IObserver observer = () -> wasNotified[0] = true;

        questionService.attach(observer);
        questionService.changeStatus(1, StatusQuestion.DELETED);

        assertTrue(wasNotified[0]);
    }

    @Test
    void shouldNotNotifyDetachedObservers() {

        boolean[] wasNotified = { false };
        IObserver observer = () -> wasNotified[0] = true;

        questionService.attach(observer);
        questionService.detach(observer);
        questionService.changeStatus(1, StatusQuestion.DELETED);

        assertFalse(wasNotified[0]);
    }

    @Test
    void shouldThrowExceptionWhenQuestionDoesNotExist() {

        assertThrows(IllegalArgumentException.class, () ->
                questionService.changeStatus(999, StatusQuestion.DELETED));
    }
}
