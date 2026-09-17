package com.loginpractice.presentation;

import com.loginpractice.domain.User;
import com.loginpractice.microkernel.core.QuestionMicrokernel;
import com.loginpractice.service.QuestionGeneratorService;
import com.loginpractice.service.QuestionService;
import com.loginpractice.presentation.menu.RoleMenu;
import com.loginpractice.presentation.menu.RoleMenuFactory;
import com.loginpractice.presentation.questions.GUIObserver1;
import com.loginpractice.presentation.questions.GUIObserver2;
import com.loginpractice.presentation.questions.GUIQuestionGenerator;
import com.loginpractice.presentation.questions.GUIQuestions;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {

    private static final String MANAGE_QUESTIONS_OPTION = "Gestionar preguntas";
    private static final String GENERATE_QUESTIONS_OPTION = "Generar preguntas (Plugins)";
    private static final String STATS_VIEW_OPTION = "Vista de estadísticas";
    private static final String CHART_VIEW_OPTION = "Vista gráfica";

    private final QuestionService questionService;
    private final QuestionGeneratorService questionGeneratorService;

    public DashboardFrame(User user, QuestionService questionService) {
        this.questionService = questionService;
        this.questionGeneratorService = new QuestionGeneratorService(new QuestionMicrokernel(), questionService);

        RoleMenu roleMenu = RoleMenuFactory.create(user.getRole());

        setTitle("Tablero - " + user.getRole());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 350);
        setLocationRelativeTo(null);
        setResizable(false);

        buildUI(roleMenu, user);
    }

    private void buildUI(RoleMenu roleMenu, User user) {

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel welcomeLabel = new JLabel(roleMenu.getWelcomeMessage(user));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

        panel.add(welcomeLabel);
        panel.add(Box.createVerticalStrut(20));

        for (String option : roleMenu.getOptions()) {

            JButton optionButton = new JButton(option);
            optionButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            optionButton.setMaximumSize(new Dimension(250, 30));

            optionButton.addActionListener(event -> {
                switch (option) {
                    case MANAGE_QUESTIONS_OPTION:
                        new GUIQuestions(questionService).setVisible(true);
                        break;
                    case GENERATE_QUESTIONS_OPTION:
                        new GUIQuestionGenerator(questionGeneratorService).setVisible(true);
                        break;
                    case STATS_VIEW_OPTION:
                        new GUIObserver1(questionService).setVisible(true);
                        break;
                    case CHART_VIEW_OPTION:
                        new GUIObserver2(questionService).setVisible(true);
                        break;
                    default:
                        JOptionPane.showMessageDialog(
                                this,
                                "Función \"" + option + "\" próximamente disponible.",
                                "En desarrollo",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                }
            });

            panel.add(optionButton);
            panel.add(Box.createVerticalStrut(8));
        }

        add(panel);
    }
}