package co.edu.unicauca.saberpro;

import co.edu.unicauca.saberpro.service.PasswordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordServiceTest {

    private PasswordService passwordService;

    @BeforeEach
    void setUp() {
        passwordService = new PasswordService();
    }

    @Test
    void shouldAcceptValidPassword() {

        assertTrue(
                passwordService.isValidPassword("Juan123!")
        );
    }

    @Test
    void shouldRejectPasswordShorterThanSixCharacters() {

        assertFalse(
                passwordService.isValidPassword("Ju1!")
        );
    }

    @Test
    void shouldRejectPasswordWithoutUppercase() {

        assertFalse(
                passwordService.isValidPassword("juan123!")
        );
    }

    @Test
    void shouldRejectPasswordWithoutDigit() {

        assertFalse(
                passwordService.isValidPassword("JuanABC!")
        );
    }

    @Test
    void shouldRejectPasswordWithoutSpecialCharacter() {

        assertFalse(
                passwordService.isValidPassword("Juan123")
        );
    }
}