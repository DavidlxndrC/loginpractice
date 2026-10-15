package co.edu.unicauca.saberpro.templatemethod;

import co.edu.unicauca.saberpro.decorator.QuestionComponent;
import co.edu.unicauca.saberpro.domain.model.Question;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Caso de estudio: se responde con la letra de la opción y, opcionalmente, su
 * sustentación ("B: porque ..."). Letra correcta con sustentación = puntaje
 * completo; letra correcta sin sustentar = 70 % del puntaje; letra incorrecta = 0.
 */
public class CaseStudyEvaluator extends QuestionEvaluator {

    private static final Pattern ANSWER_FORMAT = Pattern.compile("^([A-Za-z])\\s*(?:[:\\-]\\s*(.+))?$", Pattern.DOTALL);
    private static final double UNSUPPORTED_ANSWER_FACTOR = 0.7;
    private static final int MIN_REASONING_LENGTH = 10;

    @Override
    protected Optional<String> validateFormat(Question question, String answer) {

        Matcher matcher = ANSWER_FORMAT.matcher(answer);

        return matcher.matches()
                ? super.validateFormat(question, matcher.group(1))
                : Optional.of("Formato inválido: use \"LETRA\" o \"LETRA: sustentación\" (por ejemplo, B: porque ...).");
    }

    @Override
    protected double calculateScore(QuestionComponent component, String answer) {

        Matcher matcher = ANSWER_FORMAT.matcher(answer);
        matcher.matches();

        String correctLabel = component.getQuestion().getCorrectOption().getLabel();
        if (!correctLabel.equalsIgnoreCase(matcher.group(1))) {
            return 0;
        }

        String reasoning = matcher.group(2);
        boolean supported = reasoning != null && reasoning.trim().length() >= MIN_REASONING_LENGTH;

        return supported ? component.getMaxScore() : component.getMaxScore() * UNSUPPORTED_ANSWER_FACTOR;
    }
}
