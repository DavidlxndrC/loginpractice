package co.edu.unicauca.saberpro.presentation;

import co.edu.unicauca.saberpro.domain.model.Role;
import co.edu.unicauca.saberpro.domain.model.Status;
import co.edu.unicauca.saberpro.facade.QuestionBankFacade;
import co.edu.unicauca.saberpro.service.UserService;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {

    private final UserService userService;
    private final QuestionBankFacade questionBank;

    private JTextField usernameField;
    private JTextField fullNameField;
    private JComboBox<Role> roleComboBox;
    private JComboBox<Status> statusComboBox;
    private JPasswordField passwordField;

    public RegisterFrame(UserService userService, QuestionBankFacade questionBank) {
        this.userService = userService;
        this.questionBank = questionBank;

        setTitle("Registro de Usuarios");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 360);
        setLocationRelativeTo(null);
        setResizable(false);

        buildForm();
    }

    private void buildForm() {

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(6, 6, 6, 6);
        constraints.fill = GridBagConstraints.HORIZONTAL;

        usernameField = new JTextField(20);
        fullNameField = new JTextField(20);
        roleComboBox = new JComboBox<>(Role.values());
        statusComboBox = new JComboBox<>(Status.values());
        passwordField = new JPasswordField(20);

        addRow(panel, constraints, 0, "Usuario:", usernameField);
        addRow(panel, constraints, 1, "Nombre completo:", fullNameField);
        addRow(panel, constraints, 2, "Rol:", roleComboBox);
        addRow(panel, constraints, 3, "Estado:", statusComboBox);
        addRow(panel, constraints, 4, "Contraseña:", passwordField);

        JButton registerButton = new JButton("Registrar");
        registerButton.addActionListener(event -> handleRegister());

        JButton backButton = new JButton("Volver");
        backButton.addActionListener(event -> {
            new WelcomeFrame(userService, questionBank).setVisible(true);
            dispose();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);

        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        panel.add(buttonPanel, constraints);

        add(panel);
    }

    private void addRow(JPanel panel, GridBagConstraints constraints,
                         int row, String label, JComponent field) {

        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.gridwidth = 1;
        constraints.weightx = 0;
        panel.add(new JLabel(label), constraints);

        constraints.gridx = 1;
        constraints.weightx = 1;
        panel.add(field, constraints);
    }

    private void handleRegister() {

        String username = usernameField.getText().trim();
        String fullName = fullNameField.getText().trim();
        Role role = (Role) roleComboBox.getSelectedItem();
        Status status = (Status) statusComboBox.getSelectedItem();
        String password = new String(passwordField.getPassword());

        try {
            userService.register(username, fullName, role, status, password);

            JOptionPane.showMessageDialog(
                    this,
                    "Usuario registrado correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );

            clearForm();

        } catch (IllegalArgumentException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    exception.getMessage(),
                    "Error de validación",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (RuntimeException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    "Ocurrió un error inesperado: " + exception.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void clearForm() {
        usernameField.setText("");
        fullNameField.setText("");
        passwordField.setText("");
        roleComboBox.setSelectedIndex(0);
        statusComboBox.setSelectedIndex(0);
    }
}