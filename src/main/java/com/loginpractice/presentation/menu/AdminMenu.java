package com.loginpractice.presentation.menu;

import com.loginpractice.domain.User;

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