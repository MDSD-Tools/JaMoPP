package meet_eat.data.entity.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

@RunWith(Parameterized.class)
public class EmailLegalityTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"test@example.com", true},
                {"moritz@gstuer.com", true},
                {"test@", false},
                {"@example.com", false},
                {"example.com", false},
                {"a@b.c", true},
                {"valid@example.example.com", true},
                {"test@example", false},
                {"vrkspmxszme.c173.b491@572924.b394.k651.example.com", true},
                {"vrrbj382.rmlpzebki499.u364.oorvvfschw96@c226.bpxaar-76.example.com", true},
                {"vrrbj382.rmlpzebki499.u364.oorvvfschw96@c226.bpxaar-76.example.", false},
                {"519437.336.87.424@385.example.com", true},
                {"aBc@.com", false},
                {"aBc@example..com", false},
                {"test.@example.com", false},
                {"test@.example.com", false},
                {".test@example.com", false},
                {"test@example...", false},
                {"1@example.com", true},
                {"123@example.com", true},
                {"123@123.example.com", true},
                {"bjoern@tolle-implementierungsphase.example.com", true},
                {null, false}
        });
    }

    private final String emailAddress;
    private final boolean isValid;

    public EmailLegalityTest(String emailAddress, boolean isValid) {
        this.emailAddress = emailAddress;
        this.isValid = isValid;
    }

    @Test
    public void testIsLegalEmailAddress() {
        // Assertions
        assertEquals(Email.isLegalEmailAddress(this.emailAddress), this.isValid);
    }

    @Test
    public void testIllegalEmailAddress() {
        if (!isValid) {
            assertThrows(IllegalArgumentException.class, () -> new Email(emailAddress));
        }
    }
}