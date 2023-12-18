package meet_eat.server.service;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

public class PasswordValueSupplierTest {

    @Test
    public void testConstructor() {
        // Test data
        int basicCharCount = 20;
        int specialCharCount = 3;
        int digitCount = 7;

        // Execution
        PasswordValueSupplier passwordSupplier = new PasswordValueSupplier(basicCharCount, specialCharCount, digitCount);

        // Assertions
        assertNotNull(passwordSupplier);
        assertEquals(basicCharCount, passwordSupplier.getBasicCharCount());
        assertEquals(specialCharCount, passwordSupplier.getSpecialCharCount());
        assertEquals(digitCount, passwordSupplier.getDigitCount());
    }

    @Test
    public void testLength() {
        // Test data
        int basicCharCount = 20;
        int specialCharCount = 3;
        int digitCount = 7;

        // Execution
        PasswordValueSupplier passwordSupplier = new PasswordValueSupplier(basicCharCount, specialCharCount, digitCount);

        // Assertions
        assertEquals(basicCharCount + specialCharCount + digitCount, passwordSupplier.get().length());
    }

    @Test
    public void testRandomness() {
        // Test data
        int basicCharCount = 20;
        int specialCharCount = 3;
        int digitCount = 7;

        // Execution
        PasswordValueSupplier passwordSupplier = new PasswordValueSupplier(basicCharCount, specialCharCount, digitCount);
        String passwordValueFst = passwordSupplier.get();
        String passwordValueSnd = passwordSupplier.get();

        // Assertions
        assertNotEquals(passwordValueFst, passwordValueSnd);
    }

    @Test
    public void testSetBasicCharCount() {
        // Test data
        int initBasicCharCount = 20;
        int setBasicCharCount = 25;
        int specialCharCount = 3;
        int digitCount = 7;

        // Execution
        PasswordValueSupplier passwordSupplier = new PasswordValueSupplier(initBasicCharCount, specialCharCount, digitCount);
        passwordSupplier.setBasicCharCount(setBasicCharCount);

        // Assertions
        assertEquals(setBasicCharCount, passwordSupplier.getBasicCharCount());
    }

    @Test
    public void testSetSpecialCharCount() {
        // Test data
        int basicCharCount = 20;
        int initSpecialCharCount = 3;
        int setSpecialCharCount = 5;
        int digitCount = 7;

        // Execution
        PasswordValueSupplier passwordSupplier = new PasswordValueSupplier(basicCharCount, initSpecialCharCount, digitCount);
        passwordSupplier.setSpecialCharCount(setSpecialCharCount);

        // Assertions
        assertEquals(setSpecialCharCount, passwordSupplier.getSpecialCharCount());
    }

    @Test
    public void testSetDigitCount() {
        // Test data
        int basicCharCount = 20;
        int specialCharCount = 3;
        int initDigitCount = 7;
        int setDigitCount = 19;

        // Execution
        PasswordValueSupplier passwordSupplier = new PasswordValueSupplier(basicCharCount, specialCharCount, initDigitCount);
        passwordSupplier.setDigitCount(setDigitCount);

        // Assertions
        assertEquals(setDigitCount, passwordSupplier.getDigitCount());
    }
}
