package com.loginpractice.service;

import com.loginpractice.access.IUserRepository;
import com.loginpractice.domain.Role;
import com.loginpractice.domain.Status;
import com.loginpractice.domain.User;

public class UserService {

    private final IUserRepository userRepository;
    private final PasswordService passwordService;

    public UserService( IUserRepository userRepository, PasswordService passwordService) {

        this.userRepository = userRepository;
        this.passwordService = passwordService;
    }

    public void register(String userName, String fullName, Role role,Status status, String password) {

        if (userName == null || userName.isBlank()) {
            throw new IllegalArgumentException("El nombre de usuario es necesario");
        }

        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("El nombre completo es necesario");
        }

        if (role == null) {
            throw new IllegalArgumentException("El rol es necesario");
        }

        if (status == null) {
            throw new IllegalArgumentException("El estado es necesario");
        }

        if (userRepository.findByUsername(userName).isPresent()) {
            throw new IllegalArgumentException("Este nombre de usuario ya existe");
        }

        if (!passwordService.isValidPassword(password)) {
            throw new IllegalArgumentException("la contraseña no cumple con el formato requerido");
        }

        String passwordHash = passwordService.hashPassword(password);

        User user = new User(userName, fullName, role, status, passwordHash);

        userRepository.save(user);
    }

    public User authenticate(String userName, String password) {

        if (userName == null || userName.isBlank()) {
            throw new IllegalArgumentException("El nombre de usuario es necesario");
        }

        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("La contraseña es necesaria");
        }

        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new AuthenticationException("Usuario o contraseña incorrectos"));

        if (user.getStatus() == Status.INACTIVE) {
            throw new AuthenticationException("El usuario está inactivo. Contacte al administrador.");
        }

        boolean passwordMatches = passwordService.verifyPassword(
                password,
                user.getPasswordHash()
        );

        if (!passwordMatches) {
            throw new AuthenticationException("Usuario o contraseña incorrectos");
        }

        return user;
    }
}