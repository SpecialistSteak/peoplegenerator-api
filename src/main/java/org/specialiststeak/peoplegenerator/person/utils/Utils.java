package org.specialiststeak.peoplegenerator.person.utils;

import com.opencsv.exceptions.CsvValidationException;
import lombok.experimental.UtilityClass;
import org.specialiststeak.peoplegenerator.person.peoplelist.Person;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.specialiststeak.peoplegenerator.person.utils.Constants.*;
import static org.specialiststeak.peoplegenerator.person.utils.Loading.*;
import static org.specialiststeak.peoplegenerator.person.utils.RateLimit.rateLimit;

@UtilityClass
public final class Utils {

    public static void startup(boolean warmup) {
        long start = System.nanoTime();
        loadAll_JAR();
        lastNames = duplicateRemove(lastNames);
        if (warmup) {
            for (int i = 0; i < 5000; i++) {
                try {
                    rateLimit(null, 1);
                } catch (Exception ignored) {
                }
                new Person();
                var ex = new Address();
                ex.toString().format("%s %s", "jeff", "is");
                getRandomIndexBasedOnProbabilities(new int[]{10, 30, 20, 40});
                generateLine();
                generateLine2();
                plusOrMinus(random.nextDouble());
            }
            selectedLine = 0;
            selectedLine2 = 0;
        }
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

    public static <T> T[] nullRemove(T[] arr) {
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
