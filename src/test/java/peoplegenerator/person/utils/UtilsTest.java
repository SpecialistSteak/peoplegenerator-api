package peoplegenerator.person.utils;

import com.opencsv.exceptions.CsvValidationException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.specialiststeak.peoplegenerator.person.utils.Utils;

import java.io.IOException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.specialiststeak.peoplegenerator.person.utils.Utils.getRandomIndexBasedOnProbabilities;
import static org.specialiststeak.peoplegenerator.person.utils.Utils.startup;

class UtilsTest {
    @BeforeAll
    static void startp() throws CsvValidationException, IOException {
        startup(false);
    }

    @Test
    void testDuplicateRemove() {
        Integer[] arr = {1, 2, 3, 3, 4, 4, null};
        assertArrayEquals(new Integer[]{1, 2, 3, 4}, Utils.duplicateRemove(arr));
    }

    @Test
    void testNullRemove() {
        String[] arr = {"a", "b", null, "c", null, "d"};
        assertArrayEquals(new String[]{"a", "b", "c", "d"}, Utils.nullRemove(arr));
    }

    @Test
    void testGetRandomIndexBasedOnProbabilities() {
        for(int i = 0; i < 100; i++) {
            assertEquals(1, getRandomIndexBasedOnProbabilities(new int[]{0, 100}));
        }
    }

    @Test
    void testPlusOrMinus() {
        double result = Utils.plusOrMinus(1.0);
        assertTrue(result >= 0.0 && result <= 2.0);
    }

    @Test
    void testMakeEmail() {
        String email = Utils.makeEmail("John", "Doe");
        assertNotNull(email);
        assertFalse(email.isEmpty());
    }
}
