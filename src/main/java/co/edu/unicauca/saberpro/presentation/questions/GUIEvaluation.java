package co.edu.unicauca.saberpro.presentation.questions;

import co.edu.unicauca.saberpro.domain.model.Option;
import co.edu.unicauca.saberpro.domain.model.Question;
import co.edu.unicauca.saberpro.domain.model.StatusQuestion;
import co.edu.unicauca.saberpro.facade.QuestionBankFacade;
import co.edu.unicauca.saberpro.templatemethod.EvaluationResult;

import javax.swing.*;
import java.awt.*;

/**
 * Presentación de una pregunta publicada y evaluación de la respuesta.
 * Las casillas aplican los decoradores (alta dificultad y retroalimentación)
 * y el Facade escoge el evaluador según el tipo de pregunta.
 */
public class GUIEvaluation extends JFrame {

    private final QuestionBankFacade questionBank;

    private JComboBox<Question> questionCombo;
    private JTextArea questionArea;
    private JTextField answerField;
    private JCheckBox highDifficultyBox;
    private JCheckBox feedbackBox;
    private JTextArea resultArea;

    public GUIEvaluation(QuestionBankFacade questionBank) {
        this.questionBank = questionBank;

        setTitle("Banco de Preguntas Saber PRO - Presentar evaluación");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(520, 560);
        setLocationRelativeTo(null);

        buildUI();
        loadQuestions();
    }

    private void buildUI() {

        questionCombo = new JComboBox<>();
        questionCombo.addActionListener(event -> showSelectedQuestion());

        questionArea = new JTextArea(9, 30);
        questionArea.setEditable(false);
        questionArea.setLineWrap(true);
        questionArea.setWrapStyleWord(true);

        answerField = new JTextField();
        highDifficultyBox = new JCheckBox("Pregunta de alta dificultad (puntaje x2)");
        feedbackBox = new JCheckBox("Mostrar retroalimentación detallada");

        JButton evaluateButton = new JButton("Evaluar respuesta");
        evaluateButton.addActionListener(event -> handleEvaluate());

        resultArea = new JTextArea(5, 30);
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);

        JPanel form = new JPanel(new GridLayout(0, 1, 4, 4));
        form.add(new JLabel("Pregunta publicada:"));
        form.add(questionCombo);

        JPanel answerPanel = new JPanel(new GridLayout(0, 1, 4, 4));
        answerPanel.add(new JLabel("Su respuesta (letra de la opción; V/F en verdadero/falso; \"B: sustentación\" en casos):"));
        answerPanel.add(answerField);
        answerPanel.add(highDifficultyBox);
        answerPanel.add(feedbackBox);
        answerPanel.add(evaluateButton);

        JPanel center = new JPanel(new BorderLayout(6, 6));
        center.add(new JScrollPane(questionArea), BorderLayout.CENTER);
        center.add(answerPanel, BorderLayout.SOUTH);

        JPanel root = new JPanel(new BorderLayout(8, 8));
        root.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        root.add(form, BorderLayout.NORTH);
        root.add(center, BorderLayout.CENTER);
        root.add(new JScrollPane(resultArea), BorderLayout.SOUTH);

        add(root);
    }

    private void loadQuestions() {

        DefaultComboBoxModel<Question> model = new DefaultComboBoxModel<>();
        for (Question question : questionBank.getQuestionsByStatus(StatusQuestion.PUBLISHED)) {
            model.addElement(question);
        }

        questionCombo.setModel(model);
        showSelectedQuestion();
    }

    private void showSelectedQuestion() {

        Question question = (Question) questionCombo.getSelectedItem();

        if (question == null) {
            questionArea.setText("No hay preguntas publicadas todavía.");
            return;
        }

        StringBuilder text = new StringBuilder();
        text.append("[").append(question.getType()).append("]\n");
        if (!question.getContext().isEmpty()) {
            text.append(question.getContext().getText()).append("\n\n");
        }
        text.append(question.getStatement()).append("\n\n");
        for (Option option : question.getOptions()) {
            text.append(option).append("\n");
        }

        questionArea.setText(text.toString());
        questionArea.setCaretPosition(0);
        resultArea.setText("");
        answerField.setText("");
    }

    private void handleEvaluate() {

        Question question = (Question) questionCombo.getSelectedItem();

        if (question == null) {
            return;
        }

        try {

            EvaluationResult result = questionBank.evaluateAnswer(
                    question.getId(), answerField.getText(),
                    highDifficultyBox.isSelected(), feedbackBox.isSelected());

            resultArea.setText(result.toString());

        } catch (IllegalStateException ex) {

            JOptionPane.showMessageDialog(this, ex.getMessage(), "No se puede evaluar", JOptionPane.WARNING_MESSAGE);
        }
    }
}
