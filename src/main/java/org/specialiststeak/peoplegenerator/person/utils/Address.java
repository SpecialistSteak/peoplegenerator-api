package org.specialiststeak.peoplegenerator.person.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import lombok.Data;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;

import static org.specialiststeak.peoplegenerator.person.peoplelist.Constants.*;
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

    public static void main(String[] args) throws IOException, CsvValidationException {
        loadCountryCSV("src/main/java/org/specialiststeak/peoplegenerator/person/DATA/COUNTRYNAME_COUNTRYCODE.csv");
        loadWorldCitiesCSV();
        startup();
        long[] start2 = new long[1_000];
        long[] end2 = new long[1_000];
        for (int i = 0; i < 1_000; i++) {
            start2[i] = System.nanoTime();
            System.out.println(new Address());
            end2[i] = System.nanoTime();
            System.out.println("-----------------------------------------------------------------");
        }
        long sum = 0;
        for (int i = 0; i < 1_000; i++) {
            sum += (end2[i] - start2[i]);
        }
        System.out.println("Average time: " + sum / 1_000 + " nanoseconds");
    }

    public static void loadWorldCitiesCSV() throws IOException, CsvValidationException {
        File file = new File("src/main/java/org/specialiststeak/peoplegenerator/person/DATA/world-cities.csv");
        Reader reader = new FileReader(file);
        CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
        String[] line;
        while ((line = csvReader.readNext()) != null) {
            String country = line[1];
            int countryIndex = Arrays.asList(countries).indexOf(country);
            if (countryIndex != -1) {
                if (CITY[countryIndex] == null) {
                    CITY[countryIndex] = new ArrayList<>();
                }
                if (SUBCOUNTRY[countryIndex] == null) {
                    SUBCOUNTRY[countryIndex] = new ArrayList<>();
                }
                if (GEONAMEID[countryIndex] == null) {
                    GEONAMEID[countryIndex] = new ArrayList<>();
                }
                CITY[countryIndex].add(line[0]);
                SUBCOUNTRY[countryIndex].add(line[2]);
                GEONAMEID[countryIndex].add(Integer.valueOf(line[3]));
            }
        }
        csvReader.close();
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