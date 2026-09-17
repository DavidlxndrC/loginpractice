package com.loginpractice.presentation.questions;

import com.loginpractice.domain.Question;
import com.loginpractice.domain.QuestionDistractors;
import com.loginpractice.domain.StatusQuestion;
import com.loginpractice.microkernel.pipeline.ContentValidationFilter;
import com.loginpractice.microkernel.pipeline.CorrectAnswerValidationFilter;
import com.loginpractice.microkernel.pipeline.OptionsValidationFilter;
import com.loginpractice.microkernel.pipeline.Pipeline;
import com.loginpractice.microkernel.pipeline.QuestionValidationException;
import com.loginpractice.microkernel.common.entities.QuestionRequest;
import com.loginpractice.service.QuestionService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GUIQuestions extends JFrame {

    private final QuestionService questionService;

    private JComboBox<Question> questionCombo;
    private JPanel formPanel;

    private JTextField idField;
    private JTextField nameField;
    private JTextArea statementArea;
    private JTextArea optionsArea;
    private JTextField correctAnswerField;
    private JTextField currentStatusField;
    private JButton saveButton;

    private JPanel statusChangePanel;
    private JComboBox<StatusQuestion> newStatusCombo;

    private Integer editingQuestionId;

    public GUIQuestions(QuestionService questionService) {
        this.questionService = questionService;

        setTitle("Banco de Preguntas Saber PRO - Gestión de preguntas");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(460, 220);
        setLocationRelativeTo(null);
        setResizable(false);

        buildUI();
        loadQuestions();
    }

    private void buildUI() {

        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        root.add(buildSelectorPanel(), BorderLayout.NORTH);

        formPanel = buildFormPanel();
        formPanel.setVisible(false);
        root.add(formPanel, BorderLayout.CENTER);

        add(root);
    }

    private JPanel buildSelectorPanel() {

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("Seleccionar pregunta"));

        questionCombo = new JComboBox<>();
        questionCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                            boolean isSelected, boolean cellHasFocus) {
                String text = (value == null) ? "Seleccione una pregunta" : value.toString();
                return super.getListCellRendererComponent(list, text, index, isSelected, cellHasFocus);
            }
        });

        JButton loadButton = new JButton("Cargar pregunta");
        loadButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        loadButton.addActionListener(event -> handleLoadQuestion());

        questionCombo.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(new JLabel("Pregunta:"));
        panel.add(Box.createVerticalStrut(5));
        panel.add(questionCombo);
        panel.add(Box.createVerticalStrut(10));
        panel.add(loadButton);

        return panel;
    }

    private JPanel buildFormPanel() {

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Formulario de pregunta"));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(4, 4, 4, 4);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;

        idField = new JTextField();
        idField.setEditable(false);
        nameField = new JTextField();
        statementArea = new JTextArea(3, 20);
        optionsArea = new JTextArea(4, 20);
        correctAnswerField = new JTextField();
        currentStatusField = new JTextField();
        currentStatusField.setEditable(false);

        addRow(panel, constraints, "Id:", idField);
        addRow(panel, constraints, "Nombre:", nameField);
        addRow(panel, constraints, "Pregunta:", new JScrollPane(statementArea));
        addRow(panel, constraints, "Opciones (una por línea):", new JScrollPane(optionsArea));
        addRow(panel, constraints, "Respuesta correcta:", correctAnswerField);
        addRow(panel, constraints, "Estado actual:", currentStatusField);

        saveButton = new JButton("Guardar cambios");
        saveButton.addActionListener(event -> handleSave());
        constraints.gridy++;
        panel.add(saveButton, constraints);

        statusChangePanel = buildStatusChangePanel();
        constraints.gridy++;
        panel.add(statusChangePanel, constraints);

        return panel;
    }

    private JPanel buildStatusChangePanel() {

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(4, 4, 4, 4);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;

        newStatusCombo = new JComboBox<>(StatusQuestion.values());

        JButton updateStatusButton = new JButton("Actualizar estado");
        updateStatusButton.addActionListener(event -> handleUpdateStatus());

        panel.add(new JLabel("Nuevo estado:"), constraints);
        constraints.gridy++;
        panel.add(newStatusCombo, constraints);
        constraints.gridy++;
        panel.add(updateStatusButton, constraints);

        return panel;
    }

    private void addRow(JPanel panel, GridBagConstraints constraints, String label, JComponent field) {
        constraints.gridy++;
        panel.add(new JLabel(label), constraints);
        constraints.gridy++;
        panel.add(field, constraints);
    }

    private void loadQuestions() {

        DefaultComboBoxModel<Question> model = new DefaultComboBoxModel<>();
        model.addElement(null);
        for (Question question : questionService.getAllQuestions()) {
            model.addElement(question);
        }

        questionCombo.setModel(model);
        questionCombo.setSelectedIndex(0);
    }

    private void handleLoadQuestion() {

        Question selected = (Question) questionCombo.getSelectedItem();

        if (selected == null) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona una pregunta existente de la lista.\n"
                            + "Para crear preguntas nuevas usa \"Generar preguntas (Plugins)\".",
                    "Ninguna pregunta seleccionada", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        enterEditMode(selected);

        formPanel.setVisible(true);
        pack();
        setLocationRelativeTo(null);
    }

    private void enterEditMode(Question question) {

        editingQuestionId = question.getId();

        idField.setText(String.valueOf(question.getId()));
        nameField.setText(question.getName());
        statementArea.setText(question.getStatement());
        optionsArea.setText(String.join("\n", question.getOptions().getOptions()));
        correctAnswerField.setText(question.getCorrectAnswer());
        currentStatusField.setText(question.getStatus().toString());

        boolean editable = question.getStatus() == StatusQuestion.DRAFT;
        setContentEditable(editable);

        saveButton.setVisible(editable);

        newStatusCombo.setSelectedItem(question.getStatus());
        statusChangePanel.setVisible(true);
    }

    private void setContentEditable(boolean editable) {
        nameField.setEditable(editable);
        statementArea.setEditable(editable);
        optionsArea.setEditable(editable);
        correctAnswerField.setEditable(editable);
    }

    private void handleSave() {

        String name = nameField.getText().trim();
        String statement = statementArea.getText().trim();
        String correctAnswer = correctAnswerField.getText().trim();
        QuestionDistractors options = parseOptions();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "El nombre de la pregunta no puede estar vacío.",
                    "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            runValidationPipeline(name, statement, options, correctAnswer);
        } catch (QuestionValidationException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo guardar la pregunta:\n- " + String.join("\n- ", ex.getErrors()),
                    "Errores de validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        questionService.updateContent(editingQuestionId, name, statement, options, correctAnswer);
        JOptionPane.showMessageDialog(this, "Cambios guardados.");
        loadQuestions();
        selectQuestionInCombo(editingQuestionId);
    }

    /**
     * Reusa el mismo pipeline de validación del módulo de generación de
     * preguntas (Tuberías y Filtros) para asegurar que una edición cumpla
     * las mismas reglas de calidad que una pregunta generada: contenido
     * válido, opciones válidas y respuesta correcta consistente.
     *
     * No se incluye ClassificationFilter porque com.loginpractice.domain.Question
     * no almacena competencia/nivel/categoría como datos propios.
     */
    private void runValidationPipeline(String name, String statement, QuestionDistractors options,
                                        String correctAnswer) {

        QuestionRequest request = new QuestionRequest(
                null, name, statement, options.getOptions(), correctAnswer, null, null
        );

        Pipeline<QuestionRequest> pipeline = new Pipeline<>(List.of(
                new ContentValidationFilter(),
                new OptionsValidationFilter(),
                new CorrectAnswerValidationFilter()
        ));

        pipeline.execute(request);
    }

    private void handleUpdateStatus() {

        if (editingQuestionId == null) {
            return;
        }

        StatusQuestion newStatus = (StatusQuestion) newStatusCombo.getSelectedItem();
        questionService.changeStatus(editingQuestionId, newStatus);

        Question updated = questionService.getQuestionById(editingQuestionId).orElseThrow();

        loadQuestions();
        selectQuestionInCombo(editingQuestionId);
        enterEditMode(updated);

        JOptionPane.showMessageDialog(this, "Estado actualizado.");
    }

    private void selectQuestionInCombo(int id) {

        for (int index = 0; index < questionCombo.getItemCount(); index++) {
            Question item = questionCombo.getItemAt(index);
            if (item != null && item.getId() == id) {
                questionCombo.setSelectedIndex(index);
                return;
            }
        }
    }

    private QuestionDistractors parseOptions() {

        List<String> lines = statementLines(optionsArea.getText());
        return new QuestionDistractors(lines);
    }

    private List<String> statementLines(String text) {
        return text.lines()
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .toList();
    }
}
