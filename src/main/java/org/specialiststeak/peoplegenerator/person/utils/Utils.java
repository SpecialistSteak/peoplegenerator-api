package org.specialiststeak.peoplegenerator.person.utils;

import com.github.javafaker.Faker;
import com.opencsv.exceptions.CsvValidationException;
import lombok.experimental.UtilityClass;
import org.specialiststeak.peoplegenerator.person.peoplelist.Person;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static org.specialiststeak.peoplegenerator.person.peoplelist.Constants.*;
import static org.specialiststeak.peoplegenerator.person.utils.Address.loadWorldCitiesCSV;
import static org.specialiststeak.peoplegenerator.person.utils.RateLimit.rateLimit;

@UtilityClass
public final class Utils {

    public static void main(String[] args) throws IOException {
        loadJobsCSV("src/main/java/org/specialiststeak/peoplegenerator/person/DATA/JOBS_SALARIES.csv");
        loadCountryCSV("src/main/java/org/specialiststeak/peoplegenerator/person/DATA/COUNTRYNAME_COUNTRYCODE.csv");
        loadAllNames();
        jobs = nullRemove(jobs);
        lastNames = nullRemove(lastNames);
        for (int i = 0; i < 100; i++) {
            System.out.println(makeEmail("John", "Doe"));
        }
    }

    public static void startup() throws IOException, CsvValidationException {
        long start = System.nanoTime();
        loadJobsCSV("src/main/java/org/specialiststeak/peoplegenerator/person/DATA/JOBS_SALARIES.csv");
        loadCountryCSV("src/main/java/org/specialiststeak/peoplegenerator/person/DATA/COUNTRYNAME_COUNTRYCODE.csv");
        loadWorldCitiesCSV();
        //Warmup the JVM
        for (int i = 0; i < 5000; i++) {
            try {
                rateLimit(null, 1);
            } catch (Exception ignored) {
            }
            new Person();
            var ex = new Address();
            String x = ex.toString();
            x = String.format("%s %s", x, x);
            getRandomIndexBasedOnProbabilities(new int[]{10, 30, 20, 40});
            generateLine();
            generateLine2();
            plusOrMinus(random.nextDouble());
        }
        selectedLine = 0;
        selectedLine2 = 0;
        loadAllNames();
        lastNames = duplicateRemove(lastNames);
        System.out.println("Warmup took " + (System.nanoTime() - start) / 1_000_000 + "ms");
    }

    public static <T> T[] duplicateRemove(T[] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[i] != null && arr[i].equals(arr[j])) {
                    arr[j] = null;
                }
            }
        }
        return nullRemove(arr);
    }

    private static <T> T[] nullRemove(T[] arr) {
        List<T> list = new ArrayList<>(Arrays.asList(arr));
        list.removeIf(Objects::isNull);
        return list.toArray(Arrays.copyOf(arr, list.size()));
    }


    public static int getRandomIndexBasedOnProbabilities(int[] array) {
        int cumulativeSum = 0;
        for (int i = 0; i < array.length; i++) {
            cumulativeSum += array[i];
            if (cumulativeSum > random.nextInt(100)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * @throws IOException if the file cannot be found
     */
    public static void loadAllNames() throws IOException {
        femaleFirstNames = new String[50000];
        maleFirstNames = new String[50000];
        lastNames = new String[50000];
        String line;
        int index = 0;
        try (var reader = new BufferedReader(new FileReader("src/main/java/org/specialiststeak/peoplegenerator/person/DATA/FemaleNames.csv"))) {
            while ((line = reader.readLine()) != null) {
                femaleFirstNames[index] = line;
                index++;
            }
        }
        index = 0;
        try (var reader2 = new BufferedReader(new FileReader("src/main/java/org/specialiststeak/peoplegenerator/person/DATA/MaleNames.csv"))) {
            while ((line = reader2.readLine()) != null) {
                maleFirstNames[index] = line;
                index++;
            }
        }
        index = 0;
        try (var reader3 = new BufferedReader(new FileReader("src/main/java/org/specialiststeak/peoplegenerator/person/DATA/Surnames.csv"))) {
            while ((line = reader3.readLine()) != null) {
                lastNames[index] = line;
                index++;
            }
        }
    }

    /**
     * @param fileName the name of the file to load
     */
    public static void loadCountryCSV(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
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

    /**
     * @param fileName name of the file to load
     */
    public static void loadJobsCSV(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line = reader.readLine();
            int numJobs = 0;
            while (line != null) {
                numJobs++;
                line = reader.readLine();
            }

            jobs = new String[numJobs];
            salaries = new int[numJobs];

            line = reader.readLine();
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

    public static void generateLine() {
        selectedLine = random.nextInt(jobs.length);
    }

    public static void generateLine2() {
        selectedLine2 = random.nextInt(countries.length);
    }

    /**
     * @param number number to add or subtract from
     * @return the number plus or minus a random amount
     */
    public static double plusOrMinus(double number) {
        double rand = random.nextDouble();
        if (rand < 0.5) {
            return number - random.nextDouble() / 2;
        } else {
            return number + random.nextDouble() / 2;
        }
    }

    public static int randomNegativify(int number) {
        return random.nextDouble() > 0.5 ? number : -number;
    }

    /**
     * @param list the list to get a random item from
     * @return a random item from the list
     */
    public static String randomItemFromList(List<String> list) {
        if (list.isEmpty()) {
            return null;
        }
        return list.get(random.nextInt(list.size()));
    }

    public static Integer randomIntItemFromList(List<Integer> list) {
        if (list.isEmpty()) {
            return null;
        }
        return list.get(random.nextInt(list.size()));
    }

    public static String makeEmail(String firstName, String lastName) {
        return (switch (EMAIL_NAME_FORMATS[random.nextInt(EMAIL_NAME_FORMATS.length)]) {
            case "%s.%s" -> (random.nextDouble() < 0.5 ? String.format("%s", firstName) : String.format("%s", lastName))
                    + "."
                    + (random.nextDouble() < 0.5 ? String.format("%s", lastName) : String.format("%s", firstName));
            case "%s%s" -> firstName + lastName;
            case "%s" -> random.nextDouble() < 0.5 ? String.format("%s", firstName) : String.format("%s", lastName);
            case "%s_%s" -> String.format("%s_%s", firstName, lastName);
            default -> "";
        } + "@" + EMAIL_SERVICE_PROVIDERS[getRandomIndexBasedOnProbabilities(EMAIL_SERVICE_PROVIDER_PROBABILITIES)]).toLowerCase();
    }

    public static void runInAnotherThread(Runnable code) {
        new Thread(code).start();
    }
}
