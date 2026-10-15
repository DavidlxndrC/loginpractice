package co.edu.unicauca.saberpro.presentation;

import co.edu.unicauca.saberpro.domain.model.User;
import co.edu.unicauca.saberpro.facade.ImportResult;
import co.edu.unicauca.saberpro.facade.QuestionBankFacade;
import co.edu.unicauca.saberpro.presentation.menu.RoleMenu;
import co.edu.unicauca.saberpro.presentation.menu.RoleMenuFactory;
import co.edu.unicauca.saberpro.presentation.questions.GUIEvaluation;
import co.edu.unicauca.saberpro.presentation.questions.GUIObserver1;
import co.edu.unicauca.saberpro.presentation.questions.GUIObserver2;
import co.edu.unicauca.saberpro.presentation.questions.GUIQuestionCreator;
import co.edu.unicauca.saberpro.presentation.questions.GUIQuestions;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {

    private static final String CREATE_QUESTION_OPTION = "Crear pregunta";
    private static final String MANAGE_QUESTIONS_OPTION = "Gestionar preguntas";
    private static final String IMPORT_QUESTIONS_OPTION = "Importar preguntas externas";
    private static final String TAKE_EVALUATION_OPTION = "Presentar evaluación";
    private static final String STATS_VIEW_OPTION = "Vista de estadísticas";
    private static final String CHART_VIEW_OPTION = "Vista gráfica";

    private final QuestionBankFacade questionBank;

    public DashboardFrame(User user, QuestionBankFacade questionBank) {
        this.questionBank = questionBank;

        RoleMenu roleMenu = RoleMenuFactory.create(user.getRole());

        setTitle("Tablero - " + user.getRole());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
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
                    case CREATE_QUESTION_OPTION:
                        new GUIQuestionCreator(questionBank).setVisible(true);
                        break;
                    case MANAGE_QUESTIONS_OPTION:
                        new GUIQuestions(questionBank).setVisible(true);
                        break;
                    case IMPORT_QUESTIONS_OPTION:
                        importExternalQuestions();
                        break;
                    case TAKE_EVALUATION_OPTION:
                        new GUIEvaluation(questionBank).setVisible(true);
                        break;
                    case STATS_VIEW_OPTION:
                        new GUIObserver1(questionBank).setVisible(true);
                        break;
                    case CHART_VIEW_OPTION:
                        new GUIObserver2(questionBank).setVisible(true);
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

    private void importExternalQuestions() {

        ImportResult result = questionBank.importExternalQuestions();

        StringBuilder message = new StringBuilder();
        message.append("Preguntas importadas como borrador: ").append(result.imported()).append("\n");
        message.append("Omitidas por estar repetidas: ").append(result.duplicates()).append("\n");
        message.append("Rechazadas por inválidas: ").append(result.errors().size());
        for (String error : result.errors()) {
            message.append("\n  - ").append(error);
        }

        JOptionPane.showMessageDialog(this, message.toString(), "Importación de preguntas externas",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
