package peoplegenerator.person.utils;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.specialiststeak.peoplegenerator.person.utils.Constants;
import org.specialiststeak.peoplegenerator.person.utils.Loading;

import static org.specialiststeak.peoplegenerator.person.utils.Utils.startup;

public class LoadingTest {

    @BeforeAll
    public static void setup() {
        Assertions.assertDoesNotThrow(() -> startup(true));
        Assertions.assertDoesNotThrow(() -> startup(true));
    }

    @Test
    public void testStartup() {
        Assertions.assertDoesNotThrow(() -> startup(true));
        Assertions.assertDoesNotThrow(() -> startup(false));
    }

    @Test
    public void testLoadAll() {
        Assertions.assertDoesNotThrow(Loading::loadAll);
    }

    @Test
    public void testLoadAll_JAR() {
        Assertions.assertDoesNotThrow(Loading::loadAll_JAR);
    }

    @Test
    void testFemaleFirstNamesLoaded() {
        Assertions.assertNotNull(Constants.femaleFirstNames);
        Assertions.assertNotEquals(0, Constants.femaleFirstNames.length);
    }

    @Test
    void testMaleFirstNamesLoaded() {
        Assertions.assertNotNull(Constants.maleFirstNames);
        Assertions.assertNotEquals(0, Constants.maleFirstNames.length);
    }

    @Test
    void testLastNamesLoaded() {
        Assertions.assertNotNull(Constants.lastNames);
        Assertions.assertNotEquals(0, Constants.lastNames.length);
    }

    @Test
    void testCountriesLoaded() {
        Assertions.assertNotNull(Constants.countries);
        Assertions.assertNotEquals(0, Constants.countries.length);
    }

    @Test
    void testCountryCodesLoaded() {
        Assertions.assertNotNull(Constants.countryCodes);
        Assertions.assertNotEquals(0, Constants.countryCodes.length);
    }

    @Test
    void testCountryNumberLoaded() {
        Assertions.assertNotNull(Constants.countryNumber);
        Assertions.assertNotEquals(0, Constants.countryNumber.length);
    }

    @Test
    void testCountryNumberLengthLoaded() {
        Assertions.assertNotNull(Constants.countryNumberLength);
        Assertions.assertNotEquals(0, Constants.countryNumberLength.length);
    }

    @Test
    void testJobsLoaded() {
        Assertions.assertNotNull(Constants.jobs);
        Assertions.assertNotEquals(0, Constants.jobs.length);
    }

    @Test
    void testSalariesLoaded() {
        Assertions.assertNotNull(Constants.salaries);
        Assertions.assertNotEquals(0, Constants.salaries.length);
    }
}