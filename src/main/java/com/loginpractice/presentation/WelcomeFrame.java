package com.loginpractice.presentation;

import com.loginpractice.service.QuestionService;
import com.loginpractice.service.UserService;

import javax.swing.*;
import java.awt.*;

public class WelcomeFrame extends JFrame {

    private final UserService userService;
    private final QuestionService questionService;

    public WelcomeFrame(UserService userService, QuestionService questionService) {
        this.userService = userService;
        this.questionService = questionService;

        setTitle("Sistema de Gestión de Usuarios");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 220);
        setLocationRelativeTo(null);
        setResizable(false);

        buildUI();
    }

    private void buildUI() {

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel titleLabel = new JLabel("Bienvenido");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));

        JButton loginButton = new JButton("Iniciar sesión");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.addActionListener(event -> {
            new LoginFrame(userService, questionService).setVisible(true);
            dispose();
        });

        JButton registerButton = new JButton("Registrarse");
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.addActionListener(event -> {
            new RegisterFrame(userService, questionService).setVisible(true);
            dispose();
        });

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(20));
        panel.add(loginButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(registerButton);

        add(panel);
    }
}