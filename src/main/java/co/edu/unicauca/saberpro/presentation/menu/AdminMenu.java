package co.edu.unicauca.saberpro.presentation.menu;

import co.edu.unicauca.saberpro.domain.model.User;

public class AdminMenu implements RoleMenu {

    @Override
    public String getWelcomeMessage(User user) {
        return "Bienvenido, " + user.getFullName() + " (Administrador)";
    }

    @Override
    public String[] getOptions() {
        return new String[] {
                "Gestionar usuarios",
                "Gestionar roles y permisos",
                "Ver reportes del sistema",
                "Configuración general"
        };
    }
}