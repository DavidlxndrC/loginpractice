package co.edu.unicauca.saberpro;

import co.edu.unicauca.saberpro.builder.QuestionBuilder;
import co.edu.unicauca.saberpro.builder.QuestionDirector;
import co.edu.unicauca.saberpro.decorator.BasicQuestion;
import co.edu.unicauca.saberpro.decorator.FeedbackQuestion;
import co.edu.unicauca.saberpro.decorator.HighDifficultyQuestion;
import co.edu.unicauca.saberpro.decorator.QuestionComponent;
import co.edu.unicauca.saberpro.domain.model.Question;
import co.edu.unicauca.saberpro.domain.model.QuestionClassification;
import co.edu.unicauca.saberpro.templatemethod.EvaluationResult;
import co.edu.unicauca.saberpro.templatemethod.MultipleChoiceEvaluator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QuestionDecoratorTest {

    private static final double DELTA = 0.0001;

    private Question question;

    @BeforeEach
    void setUp() {
        question = new QuestionDirector(new QuestionBuilder()).buildMultipleChoice(
                "Pregunta", "¿?", List.of("a", "b", "c", "d"), 1,
                "La opción b es correcta porque sí.", new QuestionClassification("c", "n", "k"));
    }

    @Test
    void basicQuestionHasOnePointAndNoFeedback() {

        QuestionComponent basic = new BasicQuestion(question);

        assertEquals(1.0, basic.getMaxScore(), DELTA);
        assertEquals("", basic.getFeedback());
        assertSame(question, basic.getQuestion());
    }

    @Test
    void highDifficultyShouldMultiplyTheScore() {

        QuestionComponent decorated = new HighDifficultyQuestion(new BasicQuestion(question), 3.0);

        assertEquals(3.0, decorated.getMaxScore(), DELTA);
        assertTrue(decorated.getDescription().contains("Alta dificultad"));
    }

    @Test
    void highDifficultyDefaultMultiplierIsTwo() {

        assertEquals(2.0, new HighDifficultyQuestion(new BasicQuestion(question)).getMaxScore(), DELTA);
    }

    @Test
    void highDifficultyShouldRejectNonPositiveMultiplier() {

        assertThrows(IllegalArgumentException.class,
                () -> new HighDifficultyQuestion(new BasicQuestion(question), 0));
    }

    @Test
    void feedbackShouldAttachTheExplanation() {

        QuestionComponent decorated = new FeedbackQuestion(new BasicQuestion(question), "Explicación técnica.");

        assertEquals("Explicación técnica.", decorated.getFeedback());
    }

    @Test
    void feedbackShouldDefaultToTheQuestionJustification() {

        QuestionComponent decorated = new FeedbackQuestion(new BasicQuestion(question));

        assertEquals("La opción b es correcta porque sí.", decorated.getFeedback());
    }

    @Test
    void decoratorsShouldBeComposableInAnyOrder() {

        QuestionComponent a = new FeedbackQuestion(new HighDifficultyQuestion(new BasicQuestion(question)));
        QuestionComponent b = new HighDifficultyQuestion(new FeedbackQuestion(new BasicQuestion(question)));

        assertEquals(2.0, a.getMaxScore(), DELTA);
        assertEquals(2.0, b.getMaxScore(), DELTA);
        assertEquals(a.getFeedback(), b.getFeedback());
        assertTrue(a.getDescription().contains("Alta dificultad") && a.getDescription().contains("retroalimentación"));
    }

    @Test
    void decoratorsShouldNotModifyTheWrappedQuestion() {

        String statementBefore = question.getStatement();

        new HighDifficultyQuestion(new FeedbackQuestion(new BasicQuestion(question)));

        assertEquals(statementBefore, question.getStatement());
        assertEquals(1.0, new BasicQuestion(question).getMaxScore(), DELTA);
    }

    @Test
    void evaluatorShouldUseDecoratedScoreAndFeedback() {

        QuestionComponent decorated = new FeedbackQuestion(new HighDifficultyQuestion(new BasicQuestion(question)));

        EvaluationResult result = new MultipleChoiceEvaluator().evaluate(decorated, "B");

        assertEquals(2.0, result.getScore(), DELTA);
        assertEquals(2.0, result.getMaxScore(), DELTA);
        assertTrue(result.getFeedback().contains("La opción b es correcta porque sí."));
    }
}
