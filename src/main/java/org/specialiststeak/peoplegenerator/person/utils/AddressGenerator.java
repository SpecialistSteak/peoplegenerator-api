package org.specialiststeak.peoplegenerator.person.utils;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import lombok.Data;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

import static org.specialiststeak.peoplegenerator.person.peoplelist.Constants.*;
import static org.specialiststeak.peoplegenerator.person.utils.Utils.*;

@Data
public class AddressGenerator {

    private String streetAddress;
    private String city;
    private String state;
    private String country;
    private String zipCode;

    // Maybe include geoname id in future?
    public static void main(String[] args) throws IOException, CsvValidationException {
        loadCountryCSV("src/main/java/org/specialiststeak/peoplegenerator/person/DATA/COUNTRYNAME_COUNTRYCODE.csv");
        loadLists();
        long start = System.nanoTime();
        for(int i = 0; i < 1_000; i++) {
            System.out.println(new AddressGenerator());
            System.out.println("-----------------------------------------------------------------");
        }
        System.out.println("Process completed. Took: " + (System.nanoTime() - start) + "ns");
    }

    public static void loadLists() throws IOException, CsvValidationException {
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
                GEONAMEID[countryIndex].add(line[3]);
            }
        }
        csvReader.close();
    }

    public AddressGenerator() {
        generateLine2();
        this.streetAddress = generateStreetAddress();
        this.city = generateCity();
        this.state = generateState();
        this.country = generateCountry();
        if(!this.country.equals("United States")) {
            this.zipCode = generateZipCode().substring(0, 4);
        } else {
            try {
                this.zipCode = USfaker.address().zipCodeByState(this.state);
            } catch (Exception e) {
                this.zipCode = USfaker.address().zipCode();
            }
        }
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
        return streetAddress + ", " + city + " " + state + ", " + country + ", " + zipCode;
    }
}