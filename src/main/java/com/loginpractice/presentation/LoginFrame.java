package com.loginpractice.presentation;

import com.loginpractice.domain.User;
import com.loginpractice.service.AuthenticationException;
import com.loginpractice.service.QuestionService;
import com.loginpractice.service.UserService;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private final UserService userService;
    private final QuestionService questionService;

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame(UserService userService, QuestionService questionService) {
        this.userService = userService;
        this.questionService = questionService;

        setTitle("Iniciar sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 220);
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

        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);

        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(new JLabel("Usuario:"), constraints);

        constraints.gridx = 1;
        panel.add(usernameField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(new JLabel("Contraseña:"), constraints);

        constraints.gridx = 1;
        panel.add(passwordField, constraints);

        JButton loginButton = new JButton("Ingresar");
        loginButton.addActionListener(event -> handleLogin());

        JButton backButton = new JButton("Volver");
        backButton.addActionListener(event -> {
            new WelcomeFrame(userService, questionService).setVisible(true);
            dispose();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginButton);
        buttonPanel.add(backButton);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        panel.add(buttonPanel, constraints);

        add(panel);
    }

    private void handleLogin() {

        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        try {
            User user = userService.authenticate(username, password);

            new DashboardFrame(user, questionService).setVisible(true);
            dispose();

        } catch (AuthenticationException | IllegalArgumentException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    exception.getMessage(),
                    "Error de inicio de sesión",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}