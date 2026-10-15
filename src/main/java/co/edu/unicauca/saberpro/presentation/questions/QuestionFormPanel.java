package co.edu.unicauca.saberpro.presentation.questions;

import co.edu.unicauca.saberpro.domain.model.Option;
import co.edu.unicauca.saberpro.domain.model.Question;
import co.edu.unicauca.saberpro.domain.model.QuestionClassification;
import co.edu.unicauca.saberpro.domain.model.QuestionType;
import co.edu.unicauca.saberpro.facade.QuestionForm;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Formulario reutilizable de pregunta (contexto, enunciado, opciones A-D,
 * respuesta correcta, justificación y clasificación). Lo usan la pantalla de
 * creación y la de gestión. No valida nada: solo captura datos y los entrega
 * como QuestionForm; las reglas las aplica el Builder a través del Facade.
 */
public class QuestionFormPanel extends JPanel {

    private static final String[] LABELS = {"A", "B", "C", "D"};

    private final JComboBox<QuestionType> typeCombo = new JComboBox<>(QuestionType.values());
    private final JTextField nameField = new JTextField(28);
    private final JTextArea contextArea = new JTextArea(3, 28);
    private final JTextArea statementArea = new JTextArea(3, 28);
    private final JTextField[] optionFields = {
            new JTextField(28), new JTextField(28), new JTextField(28), new JTextField(28)
    };
    private final JComboBox<String> correctCombo = new JComboBox<>(LABELS);
    private final JTextArea justificationArea = new JTextArea(2, 28);
    private final JTextField competencyField = new JTextField(28);
    private final JTextField difficultyField = new JTextField(28);
    private final JTextField categoryField = new JTextField(28);

    private boolean editable = true;

    public QuestionFormPanel() {

        setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(3, 4, 3, 4);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;
        constraints.gridx = 0;
        constraints.gridy = 0;

        addRow(constraints, "Tipo de pregunta:", typeCombo);
        addRow(constraints, "Nombre:", nameField);
        addRow(constraints, "Contexto (obligatorio en caso de estudio):", new JScrollPane(contextArea));
        addRow(constraints, "Enunciado:", new JScrollPane(statementArea));
        for (int i = 0; i < optionFields.length; i++) {
            addRow(constraints, "Opción " + LABELS[i] + ":", optionFields[i]);
        }
        addRow(constraints, "Respuesta correcta:", correctCombo);
        addRow(constraints, "Justificación:", new JScrollPane(justificationArea));
        addRow(constraints, "Competencia:", competencyField);
        addRow(constraints, "Nivel de dificultad:", difficultyField);
        addRow(constraints, "Categoría:", categoryField);

        typeCombo.addActionListener(event -> onTypeChanged());
        onTypeChanged();
    }

    private void addRow(GridBagConstraints constraints, String label, JComponent field) {
        constraints.gridy++;
        add(new JLabel(label), constraints);
        constraints.gridy++;
        add(field, constraints);
    }

    /** Verdadero/Falso usa solo dos opciones: se rellenan solas y se deshabilitan C y D. */
    private void onTypeChanged() {

        boolean trueFalse = typeCombo.getSelectedItem() == QuestionType.TRUE_FALSE;

        if (trueFalse) {
            optionFields[0].setText("Verdadero");
            optionFields[1].setText("Falso");
            optionFields[2].setText("");
            optionFields[3].setText("");
            if (correctCombo.getSelectedIndex() > 1) {
                correctCombo.setSelectedIndex(0);
            }
        }

        refreshEnabledState();
    }

    private void refreshEnabledState() {

        boolean trueFalse = typeCombo.getSelectedItem() == QuestionType.TRUE_FALSE;

        typeCombo.setEnabled(editable);
        nameField.setEditable(editable);
        contextArea.setEditable(editable);
        statementArea.setEditable(editable);
        justificationArea.setEditable(editable);
        competencyField.setEditable(editable);
        difficultyField.setEditable(editable);
        categoryField.setEditable(editable);
        correctCombo.setEnabled(editable);

        for (int i = 0; i < optionFields.length; i++) {
            boolean usable = !trueFalse || i < QuestionType.TRUE_FALSE.getOptionCount();
            optionFields[i].setEditable(editable && usable && !trueFalse);
            optionFields[i].setEnabled(usable);
        }
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
        refreshEnabledState();
    }

    public void clear() {
        typeCombo.setSelectedItem(QuestionType.MULTIPLE_CHOICE);
        nameField.setText("");
        contextArea.setText("");
        statementArea.setText("");
        for (JTextField field : optionFields) {
            field.setText("");
        }
        correctCombo.setSelectedIndex(0);
        justificationArea.setText("");
        competencyField.setText("");
        difficultyField.setText("");
        categoryField.setText("");
        refreshEnabledState();
    }

    public void load(Question question) {

        typeCombo.setSelectedItem(question.getType());
        nameField.setText(question.getName());
        contextArea.setText(question.getContext().getText());
        statementArea.setText(question.getStatement());

        List<Option> options = question.getOptions();
        for (int i = 0; i < optionFields.length; i++) {
            optionFields[i].setText(i < options.size() ? options.get(i).getText() : "");
        }
        correctCombo.setSelectedIndex(options.indexOf(question.getCorrectOption()));

        justificationArea.setText(question.getJustification());
        competencyField.setText(question.getClassification().getCompetency());
        difficultyField.setText(question.getClassification().getDifficultyLevel());
        categoryField.setText(question.getClassification().getCategory());

        refreshEnabledState();
    }

    public QuestionForm getForm() {

        QuestionType type = (QuestionType) typeCombo.getSelectedItem();

        List<String> options = new ArrayList<>();
        for (int i = 0; i < type.getOptionCount(); i++) {
            options.add(optionFields[i].getText());
        }

        return new QuestionForm(
                type,
                nameField.getText(),
                contextArea.getText(),
                statementArea.getText(),
                options,
                correctCombo.getSelectedIndex(),
                justificationArea.getText(),
                new QuestionClassification(
                        competencyField.getText(), difficultyField.getText(), categoryField.getText())
        );
    }
}
