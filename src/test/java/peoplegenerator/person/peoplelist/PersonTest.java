package peoplegenerator.person.peoplelist;

import org.junit.jupiter.api.*;
import org.specialiststeak.peoplegenerator.person.objects.Person;

import static org.specialiststeak.peoplegenerator.person.utils.Utils.startup;

public class PersonTest {

    Person person;

    @BeforeAll
    public static void setUp() {
        startup(true);
        startup(true);
    }

    @BeforeEach
    public void init() {
        person = new Person();
    }

    @Test
    @DisplayName("Test that the name is not null")
    public void testNameNotNull() {
        Assertions.assertNotNull(person.getName());
    }

    @Test
    @DisplayName("Test that the age is within expected range")
    public void testAgeRange() {
        Assertions.assertTrue(person.getAge() >= 0 && person.getAge() <= 120);
    }

    @Test
    @DisplayName("Test that the job is not null")
    public void testJobNotNull() {
        Assertions.assertNotNull(person.getJob());
    }

    @Test
    @DisplayName("Test that the income is within expected range")
    public void testIncomeRange() {
        Assertions.assertTrue(person.getIncomeUSD() >= 0);
    }

    @Test
    @DisplayName("Test that the credit score is within expected range")
    public void testCreditScoreRange() {
        Assertions.assertTrue(person.getCreditScore() >= 0);
    }

    @Test
    @DisplayName("Test that the married status is not null")
    public void testMarriedNotNull() {
        Assertions.assertNotNull(person.isMarried());
        assert true;
    }

    @Test
    @DisplayName("Test that the has children status is not null")
    public void testHasChildrenNotNull() {
        Assertions.assertNotNull(person.isHasChildren());
        assert true;
    }

    @Test
    @DisplayName("Test that the height is within expected range")
    public void testHeightRange() {
        Assertions.assertTrue(person.getHeight() >= 10);
    }
}