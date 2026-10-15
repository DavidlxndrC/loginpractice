package co.edu.unicauca.saberpro.presentation.questions;

import co.edu.unicauca.saberpro.domain.model.Question;
import co.edu.unicauca.saberpro.domain.model.StatusQuestion;
import co.edu.unicauca.saberpro.infra.IObserver;
import co.edu.unicauca.saberpro.facade.QuestionBankFacade;

import javax.swing.*;
import java.awt.*;
import java.util.EnumMap;
import java.util.Map;

public class GUIObserver2 extends JFrame implements IObserver {

    private final QuestionBankFacade questionBank;

    private JLabel infoLabel;

    public GUIObserver2(QuestionBankFacade questionBank) {
        this.questionBank = questionBank;

        setTitle("Distribución de preguntas");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(320, 220);
        setLocationRelativeTo(null);
        setResizable(false);

        buildUI();
        questionBank.attach(this);
        update();
    }

    private void buildUI() {

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel("Distribución de preguntas (%)", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 13));

        infoLabel = new JLabel();
        infoLabel.setVerticalAlignment(SwingConstants.TOP);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(infoLabel, BorderLayout.CENTER);

        add(panel);
    }

    @Override
    public void update() {

        Map<StatusQuestion, Long> counts = countByStatus();
        long total = counts.values().stream().mapToLong(Long::longValue).sum();

        StringBuilder text = new StringBuilder("<html>");
        for (StatusQuestion status : StatusQuestion.values()) {
            long count = counts.getOrDefault(status, 0L);
            long percentage = total == 0 ? 0 : Math.round(100.0 * count / total);
            text.append(status).append(": ").append(percentage).append("%<br>");
        }
        text.append("</html>");

        infoLabel.setText(text.toString());
    }

    private Map<StatusQuestion, Long> countByStatus() {

        Map<StatusQuestion, Long> counts = new EnumMap<>(StatusQuestion.class);

        for (Question question : questionBank.getAllQuestions()) {
            counts.merge(question.getStatus(), 1L, Long::sum);
        }

        return counts;
    }
}
