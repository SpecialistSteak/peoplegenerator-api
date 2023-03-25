package org.specialiststeak.peoplegenerator.person.peoplelist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.Data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

import static java.util.concurrent.Executors.newFixedThreadPool;
import static org.specialiststeak.peoplegenerator.person.timetesting.TimeTester.runCode;
import static org.specialiststeak.peoplegenerator.person.utils.Constants.*;
import static org.specialiststeak.peoplegenerator.person.utils.Utils.*;

@Data
public class Person {
    private String name;
    private int age;
    private String job;
    private String dateOfBirth;
    private int incomeInUSD;
    private int creditScore;
    private String creditCardNumber;
    private boolean married;
    private boolean hasChildren;
    private double height;
    private double weight;
    private String eyeColor;
    private String email;
    private String gender;
    private boolean hasDegree;
    private double GPA;
    private String bloodType;
    private String username;
    private double politicalLeaning;
    private String religion;
    private Address address;

    //useragent ex: Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:47.0) Gecko/20100101 Firefox/47.0
    //zodiac sign (based on date of birth)
    //MAC address (based on IP address)
    //credit card expiration date (within 5 years, 5% chance of being expired)

    public static Person[] createPeople(int number) {
        Person[] people = new Person[number];
        if(number < 100){
            for(int i = 0; i < number; i++){
                people[i] = new Person();
            }
            return people;
        }

        int numThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = newFixedThreadPool(numThreads);
        try {
            int chunkSize = number / numThreads;
            List<Future<?>> futures = new ArrayList<>();
            for (int i = 0; i < numThreads; i++) {
                int start = i * chunkSize;
                int end = (i == numThreads - 1) ? number : start + chunkSize;
                futures.add(executor.submit(() -> {
                    for (int j = start; j < end; j++) {
                        people[j] = new Person();
                    }
                }));
            }
            for (Future<?> future : futures) {
                future.get();
            }
            executor.shutdown();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            executor.shutdown();
        }
        return people;
    }

    private static final String FEMALE = "Female";

    public Person() {
        generateLine();
        generateLine2();
        this.address = new Address();
        this.gender = generateGender();
        this.name = generateName();
        this.email = generateEmail();
        this.age = generateAge();
        this.job = generateJob();
        this.dateOfBirth = generateDateOfBirth();
        this.incomeInUSD = generateIncome();
        this.creditScore = generateCreditScore();
        this.creditCardNumber = generateCreditCardNumber();
        this.married = generateMarried();
        this.hasChildren = generateHasChildren();
        this.height = generateHeight();
        this.weight = generateWeight();
        this.eyeColor = generateEyeColor();
        this.hasDegree = generateHasDegree();
        this.GPA = generateGPA();
        this.bloodType = generateBloodType();
        this.username = generateUsername();
        this.politicalLeaning = generatePoliticalLeaning();
        this.religion = generateReligion();
    }

    public static void main(String[] args) {
        startup(false);
        personTimeTest();
        System.out.print("\033[H\033[2J");
        System.out.flush();
        startup(false);
        System.out.println("0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0");
        personTimeTest();
        for (int i = 0; i < 1000; ++i) {
            System.out.println(new Person());
        }
    }

    public static void personTimeTest() {
        Person p1;
        long startTime = System.nanoTime();
        p1 = new Person();
        System.out.println("Time taken: " + (System.nanoTime() - startTime) + "ns");
        System.out.println("Gender:             " + runCode(Person::generateGender));
        System.out.println("Name:               " + runCode(p1::generateName));
        System.out.println("Email:              " + runCode(p1::generateEmail));
        System.out.println("Age:                " + runCode(Person::generateAge));
        System.out.println("Job:                " + runCode(p1::generateJob));
        System.out.println("Date of Birth:      " + runCode(p1::generateDateOfBirth));
        System.out.println("Income:             " + runCode(p1::generateIncome));
        System.out.println("Credit Score:       " + runCode(p1::generateCreditScore));
        System.out.println("Credit Card Number: " + runCode(p1::generateCreditCardNumber));
        System.out.println("Married:            " + runCode(p1::generateMarried));
        System.out.println("Has Children:       " + runCode(p1::generateHasChildren));
        System.out.println("Height:             " + runCode(p1::generateHeight));
        System.out.println("Weight:             " + runCode(p1::generateWeight));
        System.out.println("Eye Color:          " + runCode(Person::generateEyeColor));
        System.out.println("Has Degree:         " + runCode(p1::generateHasDegree));
        System.out.println("GPA:                " + runCode(Person::generateGPA));
        System.out.println("Blood Type:         " + runCode(Person::generateBloodType));
        System.out.println("Username:           " + runCode(p1::generateUsername));
        System.out.println("Political Leaning:  " + runCode(Person::generatePoliticalLeaning));
        System.out.println("Religion:           " + runCode(p1::generateReligion));
        System.out.println("Address:            " + runCode(Address::new));
    }

    public String generateName() {
        StringBuilder n = new StringBuilder();
        switch (getGender()) {
            case "Male" -> n.append(maleFirstNames[random.nextInt(maleFirstNames.length)]);
            case FEMALE -> n.append(femaleFirstNames[random.nextInt(femaleFirstNames.length)]);
            default ->
                    n.append(random.nextDouble() > 0.5 ? maleFirstNames[random.nextInt(maleFirstNames.length)] : femaleFirstNames[random.nextInt(femaleFirstNames.length)]);
        }
        return n.append(" ").append(lastNames[random.nextInt(lastNames.length)]).toString();
    }

    public static String generateBloodType() {
        return bloodTypes[getRandomIndexBasedOnProbabilities(bloodTypeProbabilities)];
    }

    public static int generateAge() {
        AgeRange ageRange = ageRanges[getRandomIndexBasedOnProbabilities(ageDistribution)];
        return random.nextInt(ageRange.high() - ageRange.low()) + ageRange.low();
    }

    public String generateDateOfBirth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, -getAge());
        calendar.add(Calendar.MONTH, random.nextInt(12));
        calendar.add(Calendar.DAY_OF_MONTH, random.nextInt(365));
        calendar.add(Calendar.HOUR_OF_DAY, random.nextInt(24));
        calendar.add(Calendar.MINUTE, random.nextInt(60));
        calendar.add(Calendar.SECOND, random.nextInt(60));
        return String.valueOf(calendar.getTime());
    }

    public String generateJob() {
        if (getAge() < 18) {
            return "Student";
        } else if (getAge() > 69) {
            return "Retired";
        }
        return jobs[selectedLine];
    }

    public int generateIncome() {
        return (getAge() < 18 || getAge() > 69) ? 0 : salaries[selectedLine] + random.nextInt(10000);
    }

    public int generateCreditScore() {
        if (getAge() < 18) {
            return 0;
        }
        int salary = salaries[random.nextInt(salaries.length)];

        if (salary >= 100000) {
            return 800 + randomNegativify(random.nextInt(50));
        } else if (salary >= 80000) {
            return 750 + randomNegativify(random.nextInt(50));
        } else if (salary >= 60000) {
            return 600 + randomNegativify(random.nextInt(50));
        } else if (salary >= 40000) {
            return 500 + randomNegativify(random.nextInt(50));
        } else if (salary >= 20000) {
            return 400 + randomNegativify(random.nextInt(50));
        }
        return 550 + randomNegativify(random.nextInt(50));
    }

    public String generateCreditCardNumber() {
        return getAge() >= 18 ? faker.finance().creditCard() : null;
    }

    public boolean generateMarried() {
        if (age < 16) {
            return false;
        }
        if (age <= 18) {
            return random.nextDouble() < 0.05;
        }
        return random.nextDouble() < 0.5;
    }

    public boolean generateHasChildren() {
        int randomNumber = random.nextInt(100);

        if (age < 20) {
            return randomNumber < 1;
        } else if (age < 40) {
            if (married) {
                return randomNumber < 70;
            }
            return randomNumber < 10;
        }
        return randomNumber < 60;
    }

    public double generateHeight() {
        int[][] heights;
        if (gender.equals("Male")) {
            heights = MALE_HEIGHTS;
        } else if (gender.equals(FEMALE)) {
            heights = FEMALE_HEIGHTS;
        } else {
            heights = OTHER_HEIGHTS;
        }
        int randomValue = random.nextInt(100);
        int cumulativeProb = 0;
        for (int[] h : heights) {
            cumulativeProb += h[1];
            if (randomValue < cumulativeProb) {
                return Math.round(((h[0] + random.nextDouble()) * 10) / 10);
            }
        }
        return 0;
    }

    public double generateWeight() {
        int[][] weights;
        if (gender.equals("Male")) {
            weights = MALE_WEIGHTS;
        } else if (gender.equals(FEMALE)) {
            weights = FEMALE_WEIGHTS;
        } else {
            weights = OTHER_WEIGHTS;
        }
        int randomValue = random.nextInt(100);
        int cumulativeProb = 0;
        for (int[] w : weights) {
            cumulativeProb += w[1];
            if (randomValue < cumulativeProb) {
                return Math.round((w[0] + (random.nextDouble() - 0.49) * 2) * 10.0) / 10.0;
            }
        }
        return 0;
    }

    public static String generateEyeColor() {
        return eyeColors[getRandomIndexBasedOnProbabilities(eyeColorProbabilities)];
    }

    public String generateEmail() {
        String[] nameArray = getName().split(" ");
        try {
            return makeEmail(nameArray[0], nameArray[1]);
        } catch (Exception e) {
            return makeEmail("John", "Doe");
        }
    }

    public static String generateGender() {
        return switch (random.nextInt(100) / 49) {
            case 0 -> "Male";
            case 1 -> FEMALE;
            default -> "Other";
        };
    }

    public boolean generateHasDegree() {
        if (getGPA() >= 2.0) {
            return random.nextDouble() < 0.65;
        } else {
            return random.nextDouble() < 0.65 / 2.0;
        }
    }

    public static double generateGPA() {
        int rand = random.nextInt(100);
        double gpa;
        if (rand < 5) {
            gpa = plusOrMinus(4.0);
        } else if (rand < 10) {
            gpa = plusOrMinus(3.8);
        } else if (rand < 16) {
            gpa = plusOrMinus(3.6);
        } else if (rand < 25) {
            gpa = plusOrMinus(3.2);
        } else if (rand < 40) {
            gpa = plusOrMinus(3.0);
        } else if (rand < 60) {
            gpa = plusOrMinus(2.8);
        } else if (rand < 85) {
            gpa = plusOrMinus(2.0);
        } else if (rand < 90) {
            gpa = plusOrMinus(1.0);
        } else if (rand < 98) {
            gpa = 0.0;
        } else {
            gpa = random.nextDouble() * 4.0;
        }
        gpa = Math.round(gpa * 10.0) / 10.0;
        if (gpa > 4.0) {
            gpa = 4.0;
        } else if (gpa < 0.0) {
            gpa = 0.0;
        }
        return gpa;
    }

    public String generateUsername() {
        return name.substring(0, name.indexOf(" ")).toLowerCase().trim() + random.nextInt(100);
    }

    public static double generatePoliticalLeaning() {
        double leaning = random.nextGaussian() * STANDARD_DEVIATION + AVERAGE_LEANING;
        if (leaning > 1.0) {
            leaning = 1.0;
        } else if (leaning < -1.0) {
            leaning = -1.0;
        }
        return Math.round(leaning * 100.0) / 100.0;
    }

    public String generateReligion() {
        switch (this.address.getNationality()) {
            case "United States", "Mexico", "Brazil", "Canada", "Italy", "France", "Spain", "United Kingdom", "Poland", "Argentina", "Australia", "Germany", "Colombia", "South Africa", "Philippines", "Russia", "Chile", "Peru", "Ukraine", "Netherlands", "Belgium", "Switzerland", "Portugal", "Sweden", "Austria", "Norway", "Ireland", "Denmark", "Finland", "Greece", "Czech Republic", "Romania", "Hungary", "Slovakia", "Bulgaria", "Croatia", "Serbia", "Slovenia", "Latvia", "Estonia", "Lithuania", "Iceland" -> {
                if (random.nextInt(100) < 37) {
                    return "Christian";
                }
                return religions[getRandomIndexBasedOnProbabilities(religionProbabilities)];
            }
            case "Israel" -> {
                if (random.nextInt(100) < 74) {
                    return "Jewish";
                }
                return religions[getRandomIndexBasedOnProbabilities(religionProbabilities)];
            }
            case "Indonesia", "Pakistan", "Bangladesh", "Nigeria", "Egypt" -> {
                if (random.nextInt(100) < 85) {
                    return "Muslim";
                }
                return religions[getRandomIndexBasedOnProbabilities(religionProbabilities)];
            }
            case "India", "Sri Lanka", "Nepal", "Bhutan" -> {
                if (random.nextInt(100) < 75) {
                    return "Hindu";
                }
                return religions[getRandomIndexBasedOnProbabilities(religionProbabilities)];
            }
            case "Iran" -> {
                if (random.nextInt(100) < 95) {
                    return "Muslim";
                }
                return religions[getRandomIndexBasedOnProbabilities(religionProbabilities)];
            }
            default -> {
                return religions[getRandomIndexBasedOnProbabilities(religionProbabilities)];
            }
        }
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString(this);
        } catch (Exception e) {
            return "Error";
        }
    }
}