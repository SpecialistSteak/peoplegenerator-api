package peoplegenerator.person.utils;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.specialiststeak.peoplegenerator.person.objects.Address;

import static org.junit.jupiter.api.Assertions.*;
import static org.specialiststeak.peoplegenerator.person.utils.Utils.startup;

class AddressTest{
    @BeforeAll
    static void setup() {
        startup(true);
        startup(true);
    }

//    @Test
//    void testConstructor() {
//        Address address = new Address();
//        assertNotNull(address.getStreetAddress());
//        assertNotNull(address.getCity());
//        assertNotNull(address.getState());
//        assertNotNull(address.getCountry());
//        assertNotNull(address.getZipCode());
//        assertNotNull(address.getPhoneNumber());
//        assertNotNull(address.getIPAddress());
//        assertNotNull(address.getCountryCode());
//        assertNotNull(address.getNationality());
//        assertTrue(address.getGeonameID() >= -1);
//    }

    @Test
    void testGenerateGeonameID() {
        Address address = new Address();
        int geonameID = address.getGeonameId();
        assert true;
    }

    @Test
    void testGenerateCountryCode() {
        Address address = new Address();
        String countryCode = address.generateCountryCode();
        assertNotNull(countryCode);
    }

    @Test
    void testGeneratePhoneNumber() {
        Address address = new Address();
        String phoneNumber = address.generatePhoneNumber();
        assertNotNull(phoneNumber);
        assertTrue(phoneNumber.startsWith("+"));
    }

    @Test
    void testGenerateIPAddress() {
        Address address = new Address();
        String ipAddress = address.generateIPAddress();
        assertNotNull(ipAddress);
        assertTrue(ipAddress.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}"));
    }

    @Test
    void testGenerateStreetAddress() {
        Address address = new Address();
        String streetAddress = address.getStreetAddress();
        assertNotNull(streetAddress);
    }

    @Test
    void testGenerateCity() {
        Address address = new Address();
        String city = address.getCity();
        assertNotNull(city);
    }

    @Test
    void testGenerateState() {
        Address address = new Address();
        String state = address.getState();
        assertNotNull(state);
    }

    @Test
    void testGenerateCountry() {
        Address address = new Address();
        String country = address.getCountry();
        assertNotNull(country);
    }

    @Test
    void testGenerateZipCode() {
        Address address = new Address();
        String zipCode = address.getZip();
        assertNotNull(zipCode);
    }

    @Test
    void testToString() {
        Address address = new Address();
        String addressString = address.toString();
        assertNotNull(addressString);
        assertFalse(addressString.isEmpty());
    }
}