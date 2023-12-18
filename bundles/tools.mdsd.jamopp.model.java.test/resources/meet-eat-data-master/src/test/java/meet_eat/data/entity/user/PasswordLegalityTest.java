package meet_eat.data.entity.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

@RunWith(Parameterized.class)
public class PasswordLegalityTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"abcdefghijk", false},
                {"password", false},
                {"Passwort1234", false},
                {"IchBinEinGlücklichesPasswort1!", true},
                {" ", false},
                {"", false},
                {"Test Test !234", true},
                {"12345678!", false},
                {"Einvalides!Passwort234", true},
                {"A!156aB", false},
                {"A!156aBc", true},
                {"aA!1eeeeeeeeeeeeeeeeeeeeeeeeeeee", true},
                {"aA!1eeeeeeeeeeeeeeeeeeeeeeeeeeeee", false},
                {"lowerUpperNumber1Special!", true},
                {"?lowerUpperNumber1Special!", true},
                {"123?lowerUpperNumber1Special!", true},
                {"123?lowerUpperNumber1Special!456", true},
                {"_123?lowerUpperN1Special!456", true},
                {"_1AbCdEfG", true},
                {"_1AbCdEf!", true},
                {null, false}
        });
    }

    private final String password;
    private final boolean isValid;

    public PasswordLegalityTest(String password, boolean isValid) {
        this.password = password;
        this.isValid = isValid;
    }

    @Test
    public void testIsLegalPassword() {
        // Assertions
        assertEquals(Password.isLegalPassword(password), isValid);
    }

    @Test
    public void testCreateHashedIllegalPassword() {
        if (!isValid) {
            assertThrows(IllegalArgumentException.class, () -> Password.createHashedPassword(password));
        }
    }
}