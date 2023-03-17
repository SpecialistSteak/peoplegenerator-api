package org.specialiststeak.peoplegenerator.person.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.Data;

import static org.specialiststeak.peoplegenerator.person.temp.TimeTester.runCode;
import static org.specialiststeak.peoplegenerator.person.utils.Constants.*;
import static org.specialiststeak.peoplegenerator.person.utils.Utils.*;

@Data
public class Address {
    private String streetAddress;
    private String city;
    private String state;
    private String country;
    private String zipCode;
    private int geonameID;
    private String phoneNumber;
    private String IPAddress;
    private String countryCode;
    private String nationality;

    public Address() {
        generateLine2();
        var address = fakers[selectedLine2].address();
        this.streetAddress = generateStreetAddress();
        this.city = generateCity();
        this.state = generateState();
        this.country = generateCountry();
        if (!this.country.equals("United States")) {
            this.zipCode = generateZipCode().substring(0, 4);
        } else {
            try {
                this.zipCode = USfaker.address().zipCodeByState(this.state);
            } catch (Exception e) {
                this.zipCode = USfaker.address().zipCode();
            }
        }
        this.nationality = country;
        this.countryCode = generateCountryCode();
        this.phoneNumber = generatePhoneNumber();
        this.IPAddress = generateIPAddress();
        this.geonameID = generateGeonameID();
    }

    private static void timeTest(){
        Address a = new Address();
        System.out.println("New Address:    " + runCode(Address::new) + "ns");
        System.out.println("Geoname ID:     " + runCode(a::generateGeonameID) + "ns");
        System.out.println("Country Code:   " + runCode(a::generateCountryCode) + "ns");
        System.out.println("Phone Number:   " + runCode(a::generatePhoneNumber) + "ns");
        System.out.println("IP Address:     " + runCode(a::generateIPAddress) + "ns");
        System.out.println("Street Address: " + runCode(a::generateStreetAddress) + "ns");
        System.out.println("City:           " + runCode(a::generateCity) + "ns");
        System.out.println("State:          " + runCode(a::generateState) + "ns");
        System.out.println("Country:        " + runCode(a::generateCountry) + "ns");
        System.out.println("Zip Code:       " + runCode(a::generateZipCode) + "ns");
    }

    public static void main(String[] args) {
        startup(true);
        startup(true);
        timeTest();
    }

    private int generateGeonameID() {
        if (GEONAMEID[selectedLine2] == null || GEONAMEID[selectedLine2].isEmpty()) {
            return -1;
        }
        Integer gn = randomIntItemFromList(GEONAMEID[selectedLine2]);
        return gn != null ? gn : -1;
    }

    public String generateCountryCode() {
        return countryCodes[selectedLine2];
    }

    public String generatePhoneNumber() {
        StringBuilder pn = new StringBuilder();
        pn.append("+").append(countryNumber[selectedLine2]).append(" ");

        for (int i = 0; i < countryNumberLength[selectedLine2]; i++) {
            pn.append(random.nextInt(10));
        }

        return pn.toString();
    }

    public String generateIPAddress() {
        return new StringBuilder()
                .append(random.nextInt(256)).append(".")
                .append(random.nextInt(256)).append(".")
                .append(random.nextInt(256)).append(".")
                .append(random.nextInt(256))
                .toString();
    }

    private String generateStreetAddress() {
        return fakers[selectedLine2].address().streetAddress();
    }

    private String generateCity() {
        return randomItemFromList(CITY[selectedLine2] != null ?
                CITY[selectedLine2] : EMPTY_LIST);
    }

    private String generateState() {
        return randomItemFromList(SUBCOUNTRY[selectedLine2] != null ?
                SUBCOUNTRY[selectedLine2] : EMPTY_LIST);
    }

    private String generateCountry() {
        return countries[selectedLine2];
    }

    private String generateZipCode() {
        return fakers[selectedLine2].address().zipCode();
    }

    @Override
    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            return objectMapper.writeValueAsString(this);
        } catch (Exception e) {
            return "Error converting to JSON";
        }
    }
}