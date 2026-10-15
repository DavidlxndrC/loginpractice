package co.edu.unicauca.saberpro;

import co.edu.unicauca.saberpro.access.FakeQuestionRepository;
import co.edu.unicauca.saberpro.builder.QuestionBuilder;
import co.edu.unicauca.saberpro.builder.QuestionDirector;
import co.edu.unicauca.saberpro.domain.model.Question;
import co.edu.unicauca.saberpro.domain.model.QuestionClassification;
import co.edu.unicauca.saberpro.domain.model.StatusQuestion;
import co.edu.unicauca.saberpro.infra.IObserver;
import co.edu.unicauca.saberpro.service.QuestionService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QuestionServiceTest {

    private FakeQuestionRepository fakeQuestionRepository;
    private QuestionService questionService;
    private QuestionDirector director;

    @BeforeEach
    void setUp() {
        fakeQuestionRepository = new FakeQuestionRepository();
        questionService = new QuestionService(fakeQuestionRepository);
        director = new QuestionDirector(new QuestionBuilder());

        questionService.addQuestion(newQuestion("Pregunta sobre DDD"));
    }

    private Question newQuestion(String name) {
        return director.buildMultipleChoice(name, "¿Cuál es el objetivo principal de DDD?",
                List.of("A", "B", "C", "D"), 1, "j", new QuestionClassification("c", "n", "k"));
    }

    @Test
    void shouldAssignIdsWhenAddingQuestions() {

        Question second = questionService.addQuestion(newQuestion("Otra"));

        assertEquals(2, second.getId());
        assertEquals(2, fakeQuestionRepository.size());
    }

    @Test
    void shouldApplyLifecycleTransition() {

        questionService.applyTransition(1, Question::submitForReview);

        StatusQuestion updatedStatus = questionService.getQuestionById(1).get().getStatus();
        assertEquals(StatusQuestion.PENDING_REVIEW, updatedStatus);
    }

    @Test
    void shouldKeepStatusWhenTransitionIsNotAllowed() {

        assertThrows(IllegalStateException.class, () -> questionService.applyTransition(1, Question::approve));

        assertEquals(StatusQuestion.DRAFT, questionService.getQuestionById(1).get().getStatus());
    }

    @Test
    void shouldUpdateContentOnlyInDraft() {

        questionService.updateContent(1, newQuestion("Nombre nuevo"));
        assertEquals("Nombre nuevo", questionService.getQuestionById(1).get().getName());

        questionService.applyTransition(1, Question::submitForReview);
        assertThrows(IllegalStateException.class, () -> questionService.updateContent(1, newQuestion("X")));
    }

    @Test
    void shouldNotifyObserversWhenStatusChanges() {

        boolean[] wasNotified = { false };
        IObserver observer = () -> wasNotified[0] = true;

        questionService.attach(observer);
        questionService.applyTransition(1, Question::submitForReview);

        assertTrue(wasNotified[0]);
    }

    @Test
    void shouldNotNotifyDetachedObservers() {

        boolean[] wasNotified = { false };
        IObserver observer = () -> wasNotified[0] = true;

        questionService.attach(observer);
        questionService.detach(observer);
        questionService.applyTransition(1, Question::submitForReview);

        assertFalse(wasNotified[0]);
    }

    @Test
    void shouldThrowExceptionWhenQuestionDoesNotExist() {

        assertThrows(IllegalArgumentException.class, () ->
                questionService.applyTransition(999, Question::submitForReview));
    }
}
