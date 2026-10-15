package co.edu.unicauca.saberpro.presentation.questions;

import co.edu.unicauca.saberpro.domain.model.Question;
import co.edu.unicauca.saberpro.domain.model.StatusQuestion;
import co.edu.unicauca.saberpro.facade.QuestionBankFacade;
import co.edu.unicauca.saberpro.validator.QuestionValidationException;

import javax.swing.*;
import java.awt.*;
import java.util.function.BiConsumer;

/**
 * Gestión de preguntas existentes: ver, editar (solo en Borrador) y mover la
 * pregunta por su ciclo de vida. Qué se permite en cada estado lo decide el
 * patrón State; esta pantalla solo muestra el motivo cuando una acción se rechaza.
 */
public class GUIQuestions extends JFrame {

    /** Acciones del ciclo de vida disponibles en la pantalla. */
    private enum LifecycleAction {

        SUBMIT("Enviar a revisión", QuestionBankFacade::submitForReview),
        START_REVIEW("Iniciar revisión", QuestionBankFacade::startReview),
        APPROVE("Aprobar", QuestionBankFacade::approve),
        REQUEST_CORRECTIONS("Solicitar correcciones", QuestionBankFacade::requestCorrections),
        PUBLISH("Publicar", QuestionBankFacade::publish);

        private final String label;
        private final BiConsumer<QuestionBankFacade, Integer> action;

        LifecycleAction(String label, BiConsumer<QuestionBankFacade, Integer> action) {
            this.label = label;
            this.action = action;
        }

        void apply(QuestionBankFacade questionBank, int id) {
            action.accept(questionBank, id);
        }

        @Override
        public String toString() {
            return label;
        }
    }

    private final QuestionBankFacade questionBank;

    private JComboBox<Question> questionCombo;
    private JPanel detailPanel;
    private final QuestionFormPanel formPanel = new QuestionFormPanel();
    private JTextField currentStatusField;
    private JButton saveButton;
    private JComboBox<LifecycleAction> actionCombo;

    private Integer editingQuestionId;

    public GUIQuestions(QuestionBankFacade questionBank) {
        this.questionBank = questionBank;

        setTitle("Banco de Preguntas Saber PRO - Gestión de preguntas");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(520, 140);
        setLocationRelativeTo(null);

        buildUI();
        loadQuestions();
    }

    private void buildUI() {

        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        root.add(buildSelectorPanel(), BorderLayout.NORTH);

        detailPanel = buildDetailPanel();
        detailPanel.setVisible(false);
        root.add(detailPanel, BorderLayout.CENTER);

        add(root);
    }

    private JPanel buildSelectorPanel() {

        JPanel panel = new JPanel(new BorderLayout(8, 8));
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
        loadButton.addActionListener(event -> handleLoadQuestion());

        panel.add(questionCombo, BorderLayout.CENTER);
        panel.add(loadButton, BorderLayout.EAST);

        return panel;
    }

    private JPanel buildDetailPanel() {

        JPanel panel = new JPanel(new BorderLayout(8, 8));

        currentStatusField = new JTextField();
        currentStatusField.setEditable(false);
        JPanel statusPanel = new JPanel(new BorderLayout(6, 6));
        statusPanel.add(new JLabel("Estado actual:"), BorderLayout.WEST);
        statusPanel.add(currentStatusField, BorderLayout.CENTER);

        saveButton = new JButton("Guardar cambios");
        saveButton.addActionListener(event -> handleSave());

        actionCombo = new JComboBox<>(LifecycleAction.values());
        JButton actionButton = new JButton("Aplicar acción");
        actionButton.addActionListener(event -> handleLifecycleAction());

        JPanel actionPanel = new JPanel(new BorderLayout(6, 6));
        actionPanel.add(new JLabel("Acción:"), BorderLayout.WEST);
        actionPanel.add(actionCombo, BorderLayout.CENTER);
        actionPanel.add(actionButton, BorderLayout.EAST);

        JPanel south = new JPanel(new GridLayout(0, 1, 6, 6));
        south.add(saveButton);
        south.add(actionPanel);

        panel.add(statusPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(formPanel), BorderLayout.CENTER);
        panel.add(south, BorderLayout.SOUTH);

        return panel;
    }

    private void loadQuestions() {

        DefaultComboBoxModel<Question> model = new DefaultComboBoxModel<>();
        model.addElement(null);
        for (Question question : questionBank.getAllQuestions()) {
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
                            + "Para crear una nueva usa \"Crear pregunta\".",
                    "Ninguna pregunta seleccionada", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        showQuestion(selected);

        detailPanel.setVisible(true);
        setSize(520, 720);
        setLocationRelativeTo(null);
    }

    private void showQuestion(Question question) {

        editingQuestionId = question.getId();

        formPanel.load(question);
        currentStatusField.setText(question.getStatus().toString());

        // Solo el Borrador admite edición; para el resto, el formulario es de solo lectura.
        boolean editable = question.getStatus() == StatusQuestion.DRAFT;
        formPanel.setEditable(editable);
        saveButton.setVisible(editable);
    }

    private void handleSave() {

        try {

            questionBank.updateQuestion(editingQuestionId, formPanel.getForm());
            JOptionPane.showMessageDialog(this, "Cambios guardados.");
            refreshAfterChange();

        } catch (QuestionValidationException ex) {

            JOptionPane.showMessageDialog(this,
                    "No se pudo guardar la pregunta:\n- " + String.join("\n- ", ex.getErrors()),
                    "Errores de validación", JOptionPane.WARNING_MESSAGE);

        } catch (IllegalStateException ex) {

            JOptionPane.showMessageDialog(this, ex.getMessage(), "Acción no permitida", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void handleLifecycleAction() {

        if (editingQuestionId == null) {
            return;
        }

        LifecycleAction action = (LifecycleAction) actionCombo.getSelectedItem();

        try {

            action.apply(questionBank, editingQuestionId);
            JOptionPane.showMessageDialog(this, "Acción aplicada: " + action + ".");
            refreshAfterChange();

        } catch (QuestionValidationException ex) {

            JOptionPane.showMessageDialog(this,
                    "La pregunta no cumple las reglas:\n- " + String.join("\n- ", ex.getErrors()),
                    "Errores de validación", JOptionPane.WARNING_MESSAGE);

        } catch (IllegalStateException ex) {

            // El estado actual rechazó la transición (patrón State)
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Acción no permitida", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void refreshAfterChange() {

        int id = editingQuestionId;
        loadQuestions();

        for (int index = 0; index < questionCombo.getItemCount(); index++) {
            Question item = questionCombo.getItemAt(index);
            if (item != null && item.getId() == id) {
                questionCombo.setSelectedIndex(index);
                showQuestion(item);
                return;
            }
        }
    }
}
