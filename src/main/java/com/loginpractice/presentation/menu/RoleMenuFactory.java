package com.loginpractice.presentation.menu;

import com.loginpractice.domain.Role;

public class RoleMenuFactory {

    public static RoleMenu create(Role role) {

        return switch (role) {
            case ADMIN -> new AdminMenu();
            case QUESTION_AUTHOR -> new QuestionAuthorMenu();
            case REVIEWER -> new ReviewerMenu();
            case TEACHER -> new TeacherMenu();
            case STUDENT -> new StudentMenu();
        };
    }
}