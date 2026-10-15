package co.edu.unicauca.saberpro;

import co.edu.unicauca.saberpro.access.FakeQuestionRepository;
import co.edu.unicauca.saberpro.adapter.ExternalQuestionAdapter;
import co.edu.unicauca.saberpro.adapter.ExternalQuestionService;
import co.edu.unicauca.saberpro.domain.model.Question;
import co.edu.unicauca.saberpro.domain.model.QuestionClassification;
import co.edu.unicauca.saberpro.domain.model.QuestionType;
import co.edu.unicauca.saberpro.domain.model.StatusQuestion;
import co.edu.unicauca.saberpro.facade.ImportResult;
import co.edu.unicauca.saberpro.facade.QuestionBankFacade;
import co.edu.unicauca.saberpro.facade.QuestionForm;
import co.edu.unicauca.saberpro.service.QuestionService;
import co.edu.unicauca.saberpro.templatemethod.EvaluationResult;
import co.edu.unicauca.saberpro.validator.QuestionValidationException;
import co.edu.unicauca.saberpro.validator.QuestionValidator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QuestionBankFacadeTest {

    private static final double DELTA = 0.0001;

    private FakeQuestionRepository repository;
    private QuestionBankFacade facade;

    @BeforeEach
    void setUp() {
        repository = new FakeQuestionRepository();
        facade = new QuestionBankFacade(
                new QuestionService(repository),
                new QuestionValidator(),
                new ExternalQuestionAdapter(new ExternalQuestionService()));
    }

    private QuestionForm validForm(String statement) {
        return new QuestionForm(QuestionType.MULTIPLE_CHOICE, "Pregunta", "", statement,
                List.of("a", "b", "c", "d"), 1, "Porque b.",
                new QuestionClassification("c", "n", "k"));
    }

    private Question createPublished() {
        Question question = facade.createQuestion(validForm("¿Enunciado publicado?"));
        facade.submitForReview(question.getId());
        facade.startReview(question.getId());
        facade.approve(question.getId());
        facade.publish(question.getId());
        return question;
    }

    @Test
    void shouldCreateQuestionsAsDraftWithConsecutiveIds() {

        Question first = facade.createQuestion(validForm("¿Uno?"));
        Question second = facade.createQuestion(validForm("¿Dos?"));

        assertEquals(1, first.getId());
        assertEquals(2, second.getId());
        assertEquals(StatusQuestion.DRAFT, first.getStatus());
        assertEquals(2, repository.size());
    }

    @Test
    void shouldNotSaveInvalidQuestions() {

        QuestionForm threeOptions = new QuestionForm(QuestionType.MULTIPLE_CHOICE, "N", "", "¿?",
                List.of("a", "b", "c"), 0, "", new QuestionClassification("", "", ""));

        assertThrows(QuestionValidationException.class, () -> facade.createQuestion(threeOptions));
        assertEquals(0, repository.size());
    }

    @Test
    void shouldEditOnlyWhileDraft() {

        Question question = facade.createQuestion(validForm("¿Original?"));

        facade.updateQuestion(question.getId(), validForm("¿Editada?"));
        assertEquals("¿Editada?", facade.findQuestion(question.getId()).orElseThrow().getStatement());

        facade.submitForReview(question.getId());
        assertThrows(IllegalStateException.class,
                () -> facade.updateQuestion(question.getId(), validForm("¿Otra vez?")));
    }

    @Test
    void shouldNotApplyInvalidEditToTheStoredQuestion() {

        Question question = facade.createQuestion(validForm("¿Original?"));
        QuestionForm invalid = new QuestionForm(QuestionType.MULTIPLE_CHOICE, "N", "", "¿X?",
                List.of("a", "b", "c", "d"), -1, "", new QuestionClassification("", "", ""));

        assertThrows(QuestionValidationException.class, () -> facade.updateQuestion(question.getId(), invalid));
        assertEquals("¿Original?", facade.findQuestion(question.getId()).orElseThrow().getStatement());
    }

    @Test
    void shouldDriveTheWholeLifecycleThroughTheFacade() {

        Question question = createPublished();

        assertEquals(StatusQuestion.PUBLISHED, facade.findQuestion(question.getId()).orElseThrow().getStatus());
        assertEquals(1, facade.getQuestionsByStatus(StatusQuestion.PUBLISHED).size());
        assertTrue(facade.getQuestionsByStatus(StatusQuestion.DRAFT).isEmpty());
    }

    @Test
    void shouldRejectInvalidTransitionsAndThrowForUnknownIds() {

        Question question = facade.createQuestion(validForm("¿?"));

        assertThrows(IllegalStateException.class, () -> facade.approve(question.getId()));
        assertThrows(IllegalArgumentException.class, () -> facade.publish(999));
        assertThrows(IllegalArgumentException.class, () -> facade.submitForReview(999));
    }

    @Test
    void shouldImportExternalQuestionsAsDraftAndReportInvalidOnes() {

        ImportResult result = facade.importExternalQuestions();

        assertEquals(3, result.imported());
        assertEquals(0, result.duplicates());
        assertEquals(1, result.errors().size());
        assertEquals(3, facade.getQuestionsByStatus(StatusQuestion.DRAFT).size());
    }

    @Test
    void shouldNotImportTheSameQuestionsTwice() {

        facade.importExternalQuestions();
        ImportResult second = facade.importExternalQuestions();

        assertEquals(0, second.imported());
        assertEquals(3, second.duplicates());
        assertEquals(3, repository.size());
    }

    @Test
    void shouldEvaluateWithTheEvaluatorOfTheQuestionType() {

        Question question = createPublished();

        EvaluationResult correct = facade.evaluateAnswer(question.getId(), "B", false, false);
        EvaluationResult wrong = facade.evaluateAnswer(question.getId(), "A", false, false);

        assertEquals(1.0, correct.getScore(), DELTA);
        assertEquals(0, wrong.getScore(), DELTA);
    }

    @Test
    void shouldApplyDecoratorsWhenRequested() {

        Question question = createPublished();

        EvaluationResult result = facade.evaluateAnswer(question.getId(), "B", true, true);

        assertEquals(2.0, result.getScore(), DELTA);
        assertTrue(result.getFeedback().contains("Porque b."));
    }

    @Test
    void shouldOnlyEvaluatePublishedQuestions() {

        Question draft = facade.createQuestion(validForm("¿Borrador?"));

        assertThrows(IllegalStateException.class, () -> facade.evaluateAnswer(draft.getId(), "B", false, false));
    }

    @Test
    void shouldNotifyObserversWhenTheBankChanges() {

        int[] notifications = {0};
        facade.attach(() -> notifications[0]++);

        Question question = facade.createQuestion(validForm("¿?"));
        facade.submitForReview(question.getId());

        assertEquals(2, notifications[0]);
    }
}
