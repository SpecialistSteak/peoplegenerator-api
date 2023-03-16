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
import java.util.Objects;

import static org.specialiststeak.peoplegenerator.person.utils.Constants.*;

@UtilityClass
public class Loading {
    private static final String JOBS_CSV = "JOBS_SALARIES.csv";
    private static final String COUNTRIES_CSV = "COUNTRYNAME_COUNTRYCODE.csv";
    private static final String WORLD_CITIES_CSV = "world-cities.csv";
    private static final String FEMALE_NAMES_CSV = "FemaleNames.csv";
    private static final String MALE_NAMES_CSV = "MaleNames.csv";
    private static final String SURNAMES_CSV = "Surnames.csv";

    public static void loadAll_JAR(){
        try {
            loadWorldCitiesCSV_JAR();
            loadJobsCSV_JAR();
            loadAllNames_JAR();
            loadCountryCSV_JAR();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void loadAll(){
        try {
            loadWorldCitiesCSV();
            loadJobsCSV();
            loadAllNames();
            loadCountryCSV();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void loadAllNames_JAR() throws IOException {
        femaleFirstNames = loadNamesFromJAR(FEMALE_NAMES_CSV);
        maleFirstNames = loadNamesFromJAR(MALE_NAMES_CSV);
        lastNames = loadNamesFromJAR(SURNAMES_CSV);
    }

    private static String[] loadNamesFromJAR(String pathToFile) throws IOException {
        String line;
        int index = 0;
        String[] names = new String[50000]; // 50,000 is the max number of names in the csv files
        try (var inputStream = Loading.class.getResourceAsStream(pathToFile)) {
            try (var reader = new BufferedReader(new InputStreamReader(inputStream))) {
                while ((line = reader.readLine()) != null) {
                    names[index] = line;
                    index++;
                }
            }
        }
        return names;
    }

    private static void loadAllNames() throws IOException {
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

    private static void loadFakers(){
        Locale locale;
        for (int i = 0; i < fakers.length; i++) {
            locale = new Locale.Builder().setLanguage("en").setRegion(countryCodes[i]).build();
            fakers[i] = new Faker(locale);
            fakers[i].address().zipCode();
            fakers[i].address().cityName();
        }
    }

    private static void loadCountryCSV_JAR() {
        loadFakers();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(Loading.class.getResourceAsStream(COUNTRIES_CSV))))) {
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
    }

    private static void loadCountryCSV() {
        loadFakers();
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
    }

    private static void loadJobsCSV_JAR() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(Loading.class.getResourceAsStream(JOBS_CSV))))) {
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

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(Loading.class.getResourceAsStream(JOBS_CSV))))) {
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

    private static void loadJobsCSV() {
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

    private static void loadWorldCitiesCSV_JAR() throws IOException, CsvValidationException {
        InputStream inputStream = Loading.class.getResourceAsStream(WORLD_CITIES_CSV);
        Reader reader = new InputStreamReader(inputStream != null ? inputStream : System.in);
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

    private static void loadWorldCitiesCSV() throws IOException, CsvValidationException {
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
