package peoplegenerator.person.utils;

import com.opencsv.exceptions.CsvValidationException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.specialiststeak.peoplegenerator.person.utils.Address;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.specialiststeak.peoplegenerator.person.utils.Utils.startup;

class AddressTest {

    @BeforeAll
    static void startp() throws CsvValidationException, IOException {
        startup(false);
    }

    @Test
    void testConstructor() {
        Address address = new Address();
        assertNotNull(address.getStreetAddress());
        assertNotNull(address.getCity());
        assertNotNull(address.getState());
        assertNotNull(address.getCountry());
        assertNotNull(address.getZipCode());
        assertNotNull(address.getCountryCode());
        assertNotNull(address.getNationality());
        assertNotNull(address.getPhoneNumber());
        assertNotNull(address.getIPAddress());
        assertTrue(address.getGeonameID() >= -1);
    }

    @Test
    void testGenerateCountryCode() {
        Address address = new Address();
        String countryCode = address.generateCountryCode();
        assertNotNull(countryCode);
        assertEquals(2, countryCode.length());
        assertTrue(Character.isUpperCase(countryCode.charAt(0)));
        assertTrue(Character.isUpperCase(countryCode.charAt(1)));
    }

    @Test
    void testGeneratePhoneNumber() {
        Address address = new Address();
        String phoneNumber = address.generatePhoneNumber();
        assertNotNull(phoneNumber);
        assertTrue(phoneNumber.startsWith("+"));
        assertTrue(phoneNumber.contains(" "));
        assertTrue(phoneNumber.length() > 3);
    }

    @Test
    void testGenerateIPAddress() {
        Address address = new Address();
        String ipAddress = address.generateIPAddress();
        assertNotNull(ipAddress);
        String[] parts = ipAddress.split("\\.");
        assertEquals(4, parts.length);
        assertTrue(Integer.parseInt(parts[0]) < 256);
        assertTrue(Integer.parseInt(parts[1]) < 256);
        assertTrue(Integer.parseInt(parts[2]) < 256);
        assertTrue(Integer.parseInt(parts[3]) < 256);
    }

}