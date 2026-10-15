package co.edu.unicauca.saberpro;

import co.edu.unicauca.saberpro.builder.QuestionBuilder;
import co.edu.unicauca.saberpro.builder.QuestionDirector;
import co.edu.unicauca.saberpro.domain.model.Question;
import co.edu.unicauca.saberpro.domain.model.QuestionClassification;
import co.edu.unicauca.saberpro.domain.model.StatusQuestion;
import co.edu.unicauca.saberpro.state.ApprovedState;
import co.edu.unicauca.saberpro.state.DraftState;
import co.edu.unicauca.saberpro.state.PendingReviewState;
import co.edu.unicauca.saberpro.state.PublishedState;
import co.edu.unicauca.saberpro.state.ReviewState;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QuestionStateTest {

    private QuestionDirector director;
    private Question question;

    @BeforeEach
    void setUp() {
        director = new QuestionDirector(new QuestionBuilder());
        question = newQuestion("Original");
    }

    private Question newQuestion(String name) {
        return director.buildMultipleChoice(name, "¿Enunciado?", List.of("a", "b", "c", "d"), 0, "j",
                new QuestionClassification("c", "n", "k"));
    }

    @Test
    void shouldStartInDraftState() {

        assertInstanceOf(DraftState.class, question.getState());
        assertEquals(StatusQuestion.DRAFT, question.getStatus());
    }

    @Test
    void shouldFollowTheCompleteLifecycle() {

        question.submitForReview();
        assertInstanceOf(PendingReviewState.class, question.getState());

        question.startReview();
        assertInstanceOf(ReviewState.class, question.getState());

        question.approve();
        assertInstanceOf(ApprovedState.class, question.getState());

        question.publish();
        assertInstanceOf(PublishedState.class, question.getState());
        assertEquals(StatusQuestion.PUBLISHED, question.getStatus());
    }

    @Test
    void draftShouldAllowFullEdition() {

        question.edit(newQuestion("Editada"));

        assertEquals("Editada", question.getName());
    }

    @Test
    void pendingReviewShouldBlockAuthorEdition() {

        question.submitForReview();

        assertThrows(IllegalStateException.class, () -> question.edit(newQuestion("Editada")));
        assertEquals("Original", question.getName());
    }

    @Test
    void reviewerShouldBeAbleToRequestCorrectionsAndAuthorEditAgain() {

        question.submitForReview();
        question.startReview();
        question.requestCorrections();

        assertInstanceOf(DraftState.class, question.getState());
        assertDoesNotThrow(() -> question.edit(newQuestion("Corregida")));
        assertEquals("Corregida", question.getName());
    }

    @Test
    void shouldRejectInvalidTransitionsWithoutChangingState() {

        assertThrows(IllegalStateException.class, () -> question.approve());
        assertThrows(IllegalStateException.class, () -> question.publish());
        assertThrows(IllegalStateException.class, () -> question.startReview());
        assertEquals(StatusQuestion.DRAFT, question.getStatus());

        question.submitForReview();
        assertThrows(IllegalStateException.class, () -> question.submitForReview());
        assertThrows(IllegalStateException.class, () -> question.approve());
        assertEquals(StatusQuestion.PENDING_REVIEW, question.getStatus());
    }

    @Test
    void approvedQuestionCannotBeEditedOrSentBack() {

        question.submitForReview();
        question.startReview();
        question.approve();

        assertThrows(IllegalStateException.class, () -> question.edit(newQuestion("X")));
        assertThrows(IllegalStateException.class, () -> question.requestCorrections());
    }

    @Test
    void publishedIsAFinalState() {

        question.submitForReview();
        question.startReview();
        question.approve();
        question.publish();

        assertThrows(IllegalStateException.class, () -> question.submitForReview());
        assertThrows(IllegalStateException.class, () -> question.publish());
        assertThrows(IllegalStateException.class, () -> question.edit(newQuestion("X")));
    }

    @Test
    void rejectionMessageShouldMentionTheCurrentState() {

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> question.approve());

        assertTrue(ex.getMessage().contains("Borrador"));
    }
}
