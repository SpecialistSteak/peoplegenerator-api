package peoplegenerator.person.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.specialiststeak.peoplegenerator.person.objects.Person;
import org.specialiststeak.peoplegenerator.person.utils.Utils;

import java.util.Arrays;
import java.util.List;

import static org.specialiststeak.peoplegenerator.person.utils.Utils.startup;

class UtilsTest {
    @BeforeAll
    static void setup() {
        startup(true);
        startup(true);
    }


    @Test
    void testDuplicateRemove() {
        String[] arr = {"a", "b", "c", "b", "d"};
        String[] result = Utils.duplicateRemove(arr);
        String[] expected = {"a", "b", "c", "d"};
        Assertions.assertArrayEquals(expected, result);
    }

    @Test
    void testNullRemove() {
        Integer[] arr = {1, 2, null, 3, null};
        Integer[] result = Utils.nullRemove(arr);
        Integer[] expected = {1, 2, 3};
        Assertions.assertArrayEquals(expected, result);
    }

    @Test
    void testGetRandomIndexBasedOnProbabilities() {
        int[] array = {10, 30, 20, 40};
        int result = Utils.getRandomIndexBasedOnProbabilities(array);
        Assertions.assertTrue(result >= 0 && result <= 3);
    }

    @Test
    void testMakeEmail() {
        String firstName = "John";
        String lastName = "Doe";
        String email = Utils.makeEmail(firstName, lastName);
        Assertions.assertNotNull(email);
    }

    @Test
    void testRandomItemFromList() {
        List<String> list = Arrays.asList("a", "b", "c");
        String result = Utils.randomItemFromList(list);
        Assertions.assertTrue(list.contains(result));
    }

    @Test
    void testRandomIntItemFromList() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        Integer result = Utils.randomIntItemFromList(list);
        Assertions.assertTrue(list.contains(result));
    }

    @Test
    void testPlusOrMinus() {
        double number = 1.0;
        double result = Utils.plusOrMinus(number);
        Assertions.assertTrue(result >= 0.5 && result <= 1.5);
    }

    @Test
    void testRandomNegativify() {
        int number = 1;
        int result = Utils.randomNegativify(number);
        Assertions.assertTrue(result == number || result == -number);
    }

    @Test
    void testStartup() {
        Utils.startup(true);
        Person p1 = new Person();
        assert (p1.getName() != null);
        assert (p1.getAge() > -1);
        assert (p1.getGender() != null);
        assert (p1.getAddress() != null);
        assert (p1.getEmail() != null);
        assert (p1.getJob() != null);
        assert (p1.getDoB() != null);
        assert (p1.getIncomeUSD() > -1);
        assert (p1.getCreditScore() > -1);
        assert (p1.getHeight() > -1);
        assert (p1.getWeight() > -1);
        assert (p1.getEyeColor() != null);
        assert (p1.getGPA() > -1.0);
        assert (p1.getBloodType() != null);
        assert (p1.getUsername() != null);
        assert (p1.getPoliticalLeaning() > -1.01 && p1.getPoliticalLeaning() < 1.01);
        assert (p1.getReligion() != null);
    }
}