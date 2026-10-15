package co.edu.unicauca.saberpro.presentation.menu;

import co.edu.unicauca.saberpro.domain.model.User;

public class QuestionAuthorMenu implements RoleMenu {

    @Override
    public String getWelcomeMessage(User user) {
        return "Bienvenido, " + user.getFullName() + " (Autor de preguntas)";
    }

    @Override
    public String[] getOptions() {
        return new String[] {
                "Crear pregunta",
                "Gestionar preguntas",
                "Importar preguntas externas",
                "Vista de estadísticas",
                "Vista gráfica",
                "Ver estado de revisión",
                "Historial de preguntas enviadas"
        };
    }
}
