package com.loginpractice.presentation.questions;

import com.loginpractice.domain.Question;
import com.loginpractice.domain.StatusQuestion;
import com.loginpractice.infra.IObserver;
import com.loginpractice.service.QuestionService;

import javax.swing.*;
import java.awt.*;
import java.util.EnumMap;
import java.util.Map;

public class GUIObserver1 extends JFrame implements IObserver {

    private final QuestionService questionService;

    private JLabel countsLabel;

    public GUIObserver1(QuestionService questionService) {
        this.questionService = questionService;

        setTitle("Preguntas por estado");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(280, 180);
        setLocationRelativeTo(null);
        setResizable(false);

        buildUI();
        questionService.attach(this);
        update();
    }

    private void buildUI() {

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel("Preguntas por estado", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 13));

        countsLabel = new JLabel();
        countsLabel.setVerticalAlignment(SwingConstants.TOP);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(countsLabel, BorderLayout.CENTER);

        add(panel);
    }

    @Override
    public void update() {

        Map<StatusQuestion, Long> counts = countByStatus();

        StringBuilder text = new StringBuilder("<html>");
        for (StatusQuestion status : StatusQuestion.values()) {
            text.append(status).append(": ").append(counts.getOrDefault(status, 0L)).append("<br>");
        }
        text.append("</html>");

        countsLabel.setText(text.toString());
    }

    private Map<StatusQuestion, Long> countByStatus() {

        Map<StatusQuestion, Long> counts = new EnumMap<>(StatusQuestion.class);

        for (Question question : questionService.getAllQuestions()) {
            counts.merge(question.getStatus(), 1L, Long::sum);
        }

        return counts;
    }
}
