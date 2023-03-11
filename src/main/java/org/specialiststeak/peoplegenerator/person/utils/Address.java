package org.specialiststeak.peoplegenerator.person.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.Data;

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
        int countryNumLength = countryNumberLength[selectedLine2];
        StringBuilder pn = new StringBuilder();
        pn.append("+");
        pn.append(countryNumber[selectedLine2]);
        pn.append(" ");

        for (int i = 0; i < countryNumLength; i++) {
            pn.append(random.nextInt(10));
        }

        return pn.toString();
    }

    public String generateIPAddress() {
        return random.nextInt(256) + "." +
                random.nextInt(256) + "." +
                random.nextInt(256) + "." +
                random.nextInt(256);
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

    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            return objectMapper.writeValueAsString(this);
        } catch (Exception e) {
            return "Error";
        }
    }
}