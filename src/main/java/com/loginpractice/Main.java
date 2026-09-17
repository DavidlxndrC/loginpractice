package com.loginpractice;

import com.loginpractice.access.DatabaseInitializer;
import com.loginpractice.access.IQuestionRepository;
import com.loginpractice.access.IUserRepository;
import com.loginpractice.access.QuestionImplRepository;
import com.loginpractice.access.UserRepositoryFactory;
import com.loginpractice.service.PasswordService;
import com.loginpractice.service.QuestionService;
import com.loginpractice.service.UserService;
import com.loginpractice.presentation.WelcomeFrame;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        try {
            DatabaseInitializer.initialize();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        IUserRepository userRepository = UserRepositoryFactory.getInstance().getRepository("default");

        UserService userService = new UserService(userRepository, new PasswordService());

        IQuestionRepository questionRepository = new QuestionImplRepository();
        QuestionService questionService = new QuestionService(questionRepository);

        SwingUtilities.invokeLater(() -> {
            WelcomeFrame frame = new WelcomeFrame(userService, questionService);
            frame.setVisible(true);
        });
    }
}