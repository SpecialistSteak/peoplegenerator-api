package org.specialiststeak.peoplegenerator.person.utils;

import com.github.javafaker.Faker;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import lombok.experimental.UtilityClass;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import static org.specialiststeak.peoplegenerator.person.utils.Constants.*;

@UtilityClass
public class Loading {
    public static final String JOBS_CSV =
            "src/main/java/org/specialiststeak/peoplegenerator/person/data/JOBS_SALARIES.csv";
    public static final String COUNTRIES_CSV =
            "src/main/java/org/specialiststeak/peoplegenerator/person/DATA/COUNTRYNAME_COUNTRYCODE.csv";
    public static final String WORLD_CITIES_CSV =
            "src/main/java/org/specialiststeak/peoplegenerator/person/DATA/world-cities.csv";
    public static final String FEMALE_NAMES_CSV =
            "src/main/java/org/specialiststeak/peoplegenerator/person/DATA/FemaleNames.csv";
    public static final String MALE_NAMES_CSV =
            "src/main/java/org/specialiststeak/peoplegenerator/person/DATA/MaleNames.csv";
    public static final String SURNAMES_CSV =
            "src/main/java/org/specialiststeak/peoplegenerator/person/DATA/Surnames.csv";

    public static void loadAllNames_jar() throws IOException {
        femaleFirstNames = new String[50000];
        maleFirstNames = new String[50000];
        lastNames = new String[50000];
        String line;
        int index = 0;
        try (var inputStream = Utils.class.getResourceAsStream("/path/to/FemaleNames.csv")) {
            try (var reader = new BufferedReader(new InputStreamReader(inputStream))) {
                while ((line = reader.readLine()) != null) {
                    femaleFirstNames[index] = line;
                    index++;
                }
            }
        }
        index = 0;
        try (var inputStream = Utils.class.getResourceAsStream("/path/to/MaleNames.csv")) {
            try (var reader = new BufferedReader(new InputStreamReader(inputStream))) {
                while ((line = reader.readLine()) != null) {
                    maleFirstNames[index] = line;
                    index++;
                }
            }
        }
        index = 0;
        try (var inputStream = Utils.class.getResourceAsStream("/path/to/Surnames.csv")) {
            try (var reader = new BufferedReader(new InputStreamReader(inputStream))) {
                while ((line = reader.readLine()) != null) {
                    lastNames[index] = line;
                    index++;
                }
            }
        }
    }

    public static void loadAllNames() throws IOException {
        femaleFirstNames = new String[50000];
        maleFirstNames = new String[50000];
        lastNames = new String[50000];
        String line;
        int index = 0;
        try (var reader = new BufferedReader(new FileReader(FEMALE_NAMES_CSV))) {
            while ((line = reader.readLine()) != null) {
                femaleFirstNames[index] = line;
                index++;
            }
        }
        index = 0;
        try (var reader2 = new BufferedReader(new FileReader(MALE_NAMES_CSV))) {
            while ((line = reader2.readLine()) != null) {
                maleFirstNames[index] = line;
                index++;
            }
        }
        index = 0;
        try (var reader3 = new BufferedReader(new FileReader(SURNAMES_CSV))) {
            while ((line = reader3.readLine()) != null) {
                lastNames[index] = line;
                index++;
            }
        }
    }

    public static void loadCountryCSV() {
        try (BufferedReader br = new BufferedReader(new FileReader(COUNTRIES_CSV))) {
            String line;
            int index = 0;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                countries[index] = values[0].replace("\"", "");
                countryCodes[index] = values[1].replace("\"", "");
                countryNumber[index] = Integer.parseInt(values[2].trim());
                countryNumberLength[index] = Integer.parseInt(values[3].trim());
                index++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Locale locale;
        for (int i = 0; i < fakers.length; i++) {
            locale = new Locale.Builder().setLanguage("en").setRegion(countryCodes[i]).build();
            fakers[i] = new Faker(locale);
            fakers[i].address().zipCode();
            fakers[i].address().cityName();
        }
    }

    public static void loadJobsCSV() {
        try (BufferedReader reader = new BufferedReader(new FileReader(JOBS_CSV))) {
            String line = reader.readLine();
            int numJobs = 0;
            while (line != null) {
                numJobs++;
                line = reader.readLine();
            }

            jobs = new String[numJobs];
            salaries = new int[numJobs];
        } catch (Exception s) {
            s.printStackTrace();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(JOBS_CSV))) {
            String line = reader.readLine();
            int index = 0;
            while (line != null) {
                String[] parts = line.split(",");
                jobs[index] = parts[0];
                salaries[index] = Integer.parseInt(parts[1]);
                index++;
                line = reader.readLine();
            }
        } catch (Exception s) {
            s.printStackTrace();
        }
    }

    public static void loadWorldCitiesCSV() throws IOException, CsvValidationException {
        Reader reader = new FileReader(WORLD_CITIES_CSV);
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
}
