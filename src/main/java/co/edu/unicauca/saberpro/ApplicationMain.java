package co.edu.unicauca.saberpro;

import co.edu.unicauca.saberpro.access.DatabaseInitializer;
import co.edu.unicauca.saberpro.access.IQuestionRepository;
import co.edu.unicauca.saberpro.access.IUserRepository;
import co.edu.unicauca.saberpro.access.QuestionImplRepository;
import co.edu.unicauca.saberpro.access.UserRepositoryFactory;
import co.edu.unicauca.saberpro.adapter.ExternalQuestionAdapter;
import co.edu.unicauca.saberpro.adapter.ExternalQuestionService;
import co.edu.unicauca.saberpro.adapter.QuestionProvider;
import co.edu.unicauca.saberpro.facade.QuestionBankFacade;
import co.edu.unicauca.saberpro.presentation.WelcomeFrame;
import co.edu.unicauca.saberpro.service.PasswordService;
import co.edu.unicauca.saberpro.service.QuestionService;
import co.edu.unicauca.saberpro.service.UserService;
import co.edu.unicauca.saberpro.validator.QuestionValidator;

import javax.swing.SwingUtilities;

public class ApplicationMain {

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

        // Adapter: el banco consume el servicio externo a través de QuestionProvider
        QuestionProvider externalProvider = new ExternalQuestionAdapter(new ExternalQuestionService());

        // Facade: único punto de acceso de la capa de presentación al banco de preguntas
        QuestionBankFacade questionBank = new QuestionBankFacade(
                questionService, new QuestionValidator(), externalProvider);

        SwingUtilities.invokeLater(() -> {
            WelcomeFrame frame = new WelcomeFrame(userService, questionBank);
            frame.setVisible(true);
        });
    }
}
