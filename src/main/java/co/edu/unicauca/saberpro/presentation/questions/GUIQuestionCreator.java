package co.edu.unicauca.saberpro.presentation.questions;

import co.edu.unicauca.saberpro.domain.model.Question;
import co.edu.unicauca.saberpro.facade.QuestionBankFacade;
import co.edu.unicauca.saberpro.validator.QuestionValidationException;

import javax.swing.*;
import java.awt.*;

/**
 * Pantalla para crear una pregunta. Los datos se envían al Facade, que usa el
 * Builder: si no hay exactamente el número de opciones del tipo y 1 respuesta
 * correcta, se muestran los errores y no se guarda nada.
 */
public class GUIQuestionCreator extends JFrame {

    private final QuestionBankFacade questionBank;
    private final QuestionFormPanel formPanel = new QuestionFormPanel();

    public GUIQuestionCreator(QuestionBankFacade questionBank) {
        this.questionBank = questionBank;

        setTitle("Banco de Preguntas Saber PRO - Crear pregunta");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JButton createButton = new JButton("Crear pregunta");
        createButton.addActionListener(event -> handleCreate());

        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        root.add(new JScrollPane(formPanel), BorderLayout.CENTER);
        root.add(createButton, BorderLayout.SOUTH);

        add(root);
        setSize(520, 700);
        setLocationRelativeTo(null);
    }

    private void handleCreate() {

        try {

            Question created = questionBank.createQuestion(formPanel.getForm());

            JOptionPane.showMessageDialog(this,
                    "Pregunta creada con id " + created.getId() + " (estado: " + created.getStatus() + ").",
                    "Pregunta creada", JOptionPane.INFORMATION_MESSAGE);

            formPanel.clear();

        } catch (QuestionValidationException ex) {

            JOptionPane.showMessageDialog(this,
                    "No se pudo crear la pregunta:\n- " + String.join("\n- ", ex.getErrors()),
                    "Errores de validación", JOptionPane.WARNING_MESSAGE);
        }
    }
}
