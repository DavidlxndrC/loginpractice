package co.edu.unicauca.saberpro;

import co.edu.unicauca.saberpro.builder.QuestionBuilder;
import co.edu.unicauca.saberpro.builder.QuestionDirector;
import co.edu.unicauca.saberpro.decorator.BasicQuestion;
import co.edu.unicauca.saberpro.decorator.QuestionComponent;
import co.edu.unicauca.saberpro.domain.model.Question;
import co.edu.unicauca.saberpro.domain.model.QuestionClassification;
import co.edu.unicauca.saberpro.templatemethod.CaseStudyEvaluator;
import co.edu.unicauca.saberpro.templatemethod.EvaluationResult;
import co.edu.unicauca.saberpro.templatemethod.MultipleChoiceEvaluator;
import co.edu.unicauca.saberpro.templatemethod.TrueFalseEvaluator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QuestionEvaluatorTest {

    private static final double DELTA = 0.0001;

    private QuestionComponent multipleChoice;
    private QuestionComponent trueFalse;
    private QuestionComponent caseStudy;

    @BeforeEach
    void setUp() {
        QuestionDirector director = new QuestionDirector(new QuestionBuilder());
        QuestionClassification classification = new QuestionClassification("c", "n", "k");

        Question mc = director.buildMultipleChoice("MC", "¿?", List.of("a", "b", "c", "d"), 1, "j", classification);
        Question tf = director.buildTrueFalse("TF", "¿?", true, "j", classification);
        Question cs = director.buildCaseStudy("CS", "caso", "¿?", List.of("a", "b", "c", "d"), 2, "j", classification);

        multipleChoice = new BasicQuestion(mc);
        trueFalse = new BasicQuestion(tf);
        caseStudy = new BasicQuestion(cs);
    }

    // ------------------------------------------------------- selección múltiple

    @Test
    void multipleChoiceCorrectAnswerGetsFullScore() {

        EvaluationResult result = new MultipleChoiceEvaluator().evaluate(multipleChoice, "b");

        assertTrue(result.isFormatValid());
        assertEquals(1.0, result.getScore(), DELTA);
        assertTrue(result.isFullScore());
        assertTrue(result.getFeedback().contains("correcta"));
    }

    @Test
    void multipleChoiceWrongAnswerGetsZeroAndShowsCorrectOption() {

        EvaluationResult result = new MultipleChoiceEvaluator().evaluate(multipleChoice, "A");

        assertEquals(0, result.getScore(), DELTA);
        assertFalse(result.isFullScore());
        assertTrue(result.getFeedback().contains("B. b"));
    }

    @Test
    void multipleChoiceShouldRejectBadFormatWithoutScoring() {

        EvaluationResult result = new MultipleChoiceEvaluator().evaluate(multipleChoice, "Z");

        assertFalse(result.isFormatValid());
        assertEquals(0, result.getScore(), DELTA);
        assertTrue(result.getFeedback().contains("Formato inválido"));
    }

    @Test
    void shouldTreatNullAndBlankAnswersAsInvalidFormat() {

        assertFalse(new MultipleChoiceEvaluator().evaluate(multipleChoice, null).isFormatValid());
        assertFalse(new MultipleChoiceEvaluator().evaluate(multipleChoice, "   ").isFormatValid());
    }

    @Test
    void shouldTrimTheAnswerBeforeValidating() {

        assertTrue(new MultipleChoiceEvaluator().evaluate(multipleChoice, "  B ").isFullScore());
    }

    // ----------------------------------------------------------- verdadero/falso

    @Test
    void trueFalseAcceptsLettersAndWords() {

        TrueFalseEvaluator evaluator = new TrueFalseEvaluator();

        assertTrue(evaluator.evaluate(trueFalse, "V").isFullScore());
        assertTrue(evaluator.evaluate(trueFalse, "verdadero").isFullScore());
        assertEquals(0, evaluator.evaluate(trueFalse, "F").getScore(), DELTA);
        assertEquals(0, evaluator.evaluate(trueFalse, "Falso").getScore(), DELTA);
    }

    @Test
    void trueFalseRejectsOtherAnswers() {

        EvaluationResult result = new TrueFalseEvaluator().evaluate(trueFalse, "A");

        assertFalse(result.isFormatValid());
    }

    // --------------------------------------------------------- caso de estudio

    @Test
    void caseStudyWithCorrectLetterAndReasoningGetsFullScore() {

        EvaluationResult result = new CaseStudyEvaluator().evaluate(caseStudy, "C: porque encapsula la persistencia");

        assertTrue(result.isFullScore());
    }

    @Test
    void caseStudyWithCorrectLetterButNoReasoningGetsPartialScore() {

        EvaluationResult result = new CaseStudyEvaluator().evaluate(caseStudy, "C");

        assertEquals(0.7, result.getScore(), DELTA);
        assertFalse(result.isFullScore());
        assertTrue(result.getFeedback().contains("parcialmente"));
    }

    @Test
    void caseStudyWithWrongLetterGetsZero() {

        EvaluationResult result = new CaseStudyEvaluator().evaluate(caseStudy, "A: porque sí, es la primera opción");

        assertEquals(0, result.getScore(), DELTA);
    }

    @Test
    void caseStudyRejectsMalformedAnswers() {

        assertFalse(new CaseStudyEvaluator().evaluate(caseStudy, "creo que la c").isFormatValid());
        assertFalse(new CaseStudyEvaluator().evaluate(caseStudy, "Z: sustentación larga").isFormatValid());
    }
}
