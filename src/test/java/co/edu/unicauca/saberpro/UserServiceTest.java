package co.edu.unicauca.saberpro;

import co.edu.unicauca.saberpro.access.FakeUserRepository;
import co.edu.unicauca.saberpro.domain.model.Role;
import co.edu.unicauca.saberpro.domain.model.Status;
import co.edu.unicauca.saberpro.service.PasswordService;
import co.edu.unicauca.saberpro.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    private FakeUserRepository fakeUserRepository;
    private PasswordService passwordService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        fakeUserRepository = new FakeUserRepository();
        passwordService = new PasswordService();
        userService = new UserService(fakeUserRepository, passwordService);
    }

    @Test
    void shouldRegisterUserWithValidData() {

        userService.register(
                "juan123",
                "Juan Perez",
                Role.STUDENT,
                Status.ACTIVE,
                "Juan123!"
        );

        assertTrue(fakeUserRepository.findByUsername("juan123").isPresent());
        assertEquals(1, fakeUserRepository.size());
    }

    @Test
    void shouldStorePasswordAsHashNotPlainText() {

        userService.register(
                "juan123",
                "Juan Perez",
                Role.STUDENT,
                Status.ACTIVE,
                "Juan123!"
        );

        String storedHash = fakeUserRepository
                .findByUsername("juan123")
                .get()
                .getPasswordHash();

        assertNotEquals("Juan123!", storedHash);
        assertTrue(passwordService.verifyPassword("Juan123!", storedHash));
    }

    @Test
    void shouldThrowExceptionWhenUsernameIsBlank() {

        assertThrows(IllegalArgumentException.class, () ->
                userService.register(
                        "   ",
                        "Juan Perez",
                        Role.STUDENT,
                        Status.ACTIVE,
                        "Juan123!"
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenFullNameIsBlank() {

        assertThrows(IllegalArgumentException.class, () ->
                userService.register(
                        "juan123",
                        "",
                        Role.STUDENT,
                        Status.ACTIVE,
                        "Juan123!"
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenRoleIsNull() {

        assertThrows(IllegalArgumentException.class, () ->
                userService.register(
                        "juan123",
                        "Juan Perez",
                        null,
                        Status.ACTIVE,
                        "Juan123!"
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenStatusIsNull() {

        assertThrows(IllegalArgumentException.class, () ->
                userService.register(
                        "juan123",
                        "Juan Perez",
                        Role.STUDENT,
                        null,
                        "Juan123!"
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenUsernameAlreadyExists() {

        userService.register(
                "juan123",
                "Juan Perez",
                Role.STUDENT,
                Status.ACTIVE,
                "Juan123!"
        );

        assertThrows(IllegalArgumentException.class, () ->
                userService.register(
                        "juan123",
                        "Otro Nombre",
                        Role.TEACHER,
                        Status.ACTIVE,
                        "Otra123!"
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsInvalid() {

        assertThrows(IllegalArgumentException.class, () ->
                userService.register(
                        "juan123",
                        "Juan Perez",
                        Role.STUDENT,
                        Status.ACTIVE,
                        "abc"
                )
        );
    }
        @Test
    void shouldAuthenticateWithValidCredentials() {

        userService.register(
                "juan123",
                "Juan Perez",
                Role.STUDENT,
                Status.ACTIVE,
                "Juan123!"
        );

        var authenticatedUser = userService.authenticate("juan123", "Juan123!");

        assertEquals("juan123", authenticatedUser.getUserName());
    }

    @Test
    void shouldRejectAuthenticationWithWrongPassword() {

        userService.register(
                "juan123",
                "Juan Perez",
                Role.STUDENT,
                Status.ACTIVE,
                "Juan123!"
        );

        assertThrows(
                co.edu.unicauca.saberpro.service.AuthenticationException.class,
                () -> userService.authenticate("juan123", "WrongPass1!")
        );
    }

    @Test
    void shouldRejectAuthenticationForUnknownUsername() {

        assertThrows(
                co.edu.unicauca.saberpro.service.AuthenticationException.class,
                () -> userService.authenticate("noexiste", "Juan123!")
        );
    }

    @Test
    void shouldRejectAuthenticationForInactiveUser() {

        userService.register(
                "juan123",
                "Juan Perez",
                Role.STUDENT,
                Status.INACTIVE,
                "Juan123!"
        );

        assertThrows(
                co.edu.unicauca.saberpro.service.AuthenticationException.class,
                () -> userService.authenticate("juan123", "Juan123!")
        );
    }
}