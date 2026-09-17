package com.loginpractice.presentation.questions;

import com.loginpractice.domain.Question;
import com.loginpractice.microkernel.pipeline.QuestionValidationException;
import com.loginpractice.microkernel.common.entities.QuestionClassification;
import com.loginpractice.microkernel.common.entities.QuestionRequest;
import com.loginpractice.service.QuestionGeneratorService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.List;

public class GUIQuestionGenerator extends JFrame {

    private final QuestionGeneratorService generatorService;

    private JComboBox<String> typeCombo;
    private JTextField titleField;
    private JTextArea contentArea;
    private JTextArea optionsArea;
    private JTextField correctAnswerField;
    private JTextField competencyField;
    private JTextField difficultyLevelField;
    private JTextField categoryField;
    private JTextField mediaUrlField;

    public GUIQuestionGenerator(QuestionGeneratorService generatorService) {
        this.generatorService = generatorService;

        setTitle("Banco de Preguntas Saber PRO - Generador de preguntas (Microkernel)");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(480, 620);
        setLocationRelativeTo(null);
        setResizable(false);

        buildUI();
    }

    private void buildUI() {

        JPanel root = new JPanel(new GridBagLayout());
        root.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(4, 4, 4, 4);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;

        typeCombo = new JComboBox<>(generatorService.getAvailableTypes().toArray(new String[0]));
        titleField = new JTextField();
        contentArea = new JTextArea(3, 20);
        optionsArea = new JTextArea(4, 20);
        correctAnswerField = new JTextField();
        competencyField = new JTextField();
        difficultyLevelField = new JTextField();
        categoryField = new JTextField();
        mediaUrlField = new JTextField();

        typeCombo.addItemListener(this::handleTypeChanged);
        updateMediaUrlFieldState();

        addRow(root, constraints, "Tipo de pregunta:", typeCombo);
        addRow(root, constraints, "Título:", titleField);
        addRow(root, constraints, "Enunciado / caso:", new JScrollPane(contentArea));
        addRow(root, constraints, "Opciones (una por línea):", new JScrollPane(optionsArea));
        addRow(root, constraints, "Respuesta correcta:", correctAnswerField);
        addRow(root, constraints, "Competencia:", competencyField);
        addRow(root, constraints, "Nivel de dificultad:", difficultyLevelField);
        addRow(root, constraints, "Categoría:", categoryField);
        addRow(root, constraints, "URL recurso multimedia (solo tipo Multimedia):", mediaUrlField);

        JButton generateButton = new JButton("Generar pregunta");
        generateButton.addActionListener(event -> handleGenerate());
        constraints.gridy++;
        root.add(generateButton, constraints);

        add(root);
    }

    private void addRow(JPanel panel, GridBagConstraints constraints, String label, JComponent field) {
        constraints.gridy++;
        panel.add(new JLabel(label), constraints);
        constraints.gridy++;
        panel.add(field, constraints);
    }

    private void handleTypeChanged(ItemEvent event) {
        if (event.getStateChange() == ItemEvent.SELECTED) {
            updateMediaUrlFieldState();
        }
    }

    private void updateMediaUrlFieldState() {
        boolean isMultimedia = "MULTIMEDIA".equalsIgnoreCase((String) typeCombo.getSelectedItem());
        mediaUrlField.setEnabled(isMultimedia);
    }

    private void handleGenerate() {

        String type = (String) typeCombo.getSelectedItem();
        String title = titleField.getText().trim();
        String content = contentArea.getText().trim();
        List<String> options = optionsArea.getText().lines()
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .toList();
        String correctAnswer = correctAnswerField.getText().trim();

        QuestionClassification classification = new QuestionClassification(
                competencyField.getText().trim(),
                difficultyLevelField.getText().trim(),
                categoryField.getText().trim()
        );

        String mediaUrl = mediaUrlField.getText().trim();

        QuestionRequest request = new QuestionRequest(
                type, title, content, options, correctAnswer, classification,
                mediaUrl.isEmpty() ? null : mediaUrl
        );

        try {

            Question created = generatorService.generateAndSave(request);

            JOptionPane.showMessageDialog(this,
                    "Pregunta generada y guardada con id " + created.getId() + ".",
                    "Pregunta generada", JOptionPane.INFORMATION_MESSAGE);

            dispose();

        } catch (QuestionValidationException ex) {

            JOptionPane.showMessageDialog(this,
                    "No se pudo generar la pregunta:\n- " + String.join("\n- ", ex.getErrors()),
                    "Errores de validación", JOptionPane.WARNING_MESSAGE);

        } catch (IllegalArgumentException ex) {

            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
