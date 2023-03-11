package org.specialiststeak.peoplegenerator.person.utils;

import com.github.javafaker.Faker;
import lombok.experimental.UtilityClass;

import java.util.*;

@UtilityClass
public final class Constants {
    public static List<String>[] CITY = new ArrayList[192];
    public static List<String>[] SUBCOUNTRY = new ArrayList[192];
    public static List<Integer>[] GEONAMEID = new ArrayList[192];
    public static List<String> EMPTY_LIST = new ArrayList<>();

    public static Calendar calendar = Calendar.getInstance();
    public static Date date = new Date();

    /**
     * Single instance of faker for general use
     */
    public static final Faker faker = new Faker();

    public static final Faker USfaker = new Faker(new Locale.Builder().setLanguage("en").setRegion("US").build());

    /**
     * Array of possible last names
     */
    public static String[] lastNames = new String[50_000];

    /**
     * Array of possible male first names
     */
    public static String[] maleFirstNames = new String[50_000];

    /**
     * Array of possible female first names
     */
    public static String[] femaleFirstNames = new String[50_000];

    /**
     * random instance for general use
     */
    public static SplittableRandom random = new SplittableRandom();

    /**
     * Array of possible jobs
     */
    public static String[] jobs = new String[1209];

    /**
     * Array of possible salaries
     */
    public static int[] salaries = new int[1209];

    /**
     * Selected line for jobs and salaries to ensure the values match, in a memory efficient way
     */
    public static int selectedLine;

    /**
     * Selected line for all things related to countries, such as phone #, country code, etc.
     */
    public static int selectedLine2;

    /**
     * Array of Fakers for each locale (not all work, but this is still better than using the default faker)
     */
    public static Faker[] fakers = new Faker[192];

    /**
     * Array of country names (in English)
     */
    public static String[] countries = new String[192];

    /**
     * Array of country codes (in English)
     */
    public static String[] countryCodes = new String[192];

    /**
     * Array of country numbers (in English)
     */
    public static int[] countryNumber = new int[192];

    /**
     * Array of country phone number lengths (in English)
     */
    public static int[] countryNumberLength = new int[192];

    /**
     * Average leaning of a person politically, assumed to be slightly left leaning
     */
    public static final double AVERAGE_LEANING = -0.1;

    /**
     * Standard deviation of people politically, for use in normal distribution function
     */
    public static final double STANDARD_DEVIATION = 0.5;

    /**
     * Array of possible blood types
     */
    public static final String[] bloodTypes = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};

    /**
     * Array of probabilities for each blood type
     */
    public static final int[] bloodTypeProbabilities = {31, 7, 10, 2, 3, 1, 39, 7};

    /**
     * Array of possible eye colors
     */
    public static final String[] eyeColors = {"AMBER", "BLUE", "BROWN", "GRAY", "GREEN", "HAZEL", "RED"};

    /**
     * Array of probabilities for each eye color
     */
    public static final int[] eyeColorProbabilities = {5, 10, 30, 20, 15, 15, 5};

    /**
     * Array of possible hair colors
     */
    public static final String[] EMAIL_SERVICE_PROVIDERS = {"gmail.com", "yahoo.com", "hotmail.com", "outlook.com", "aol.com", "icloud.com", "mail.com", "msn.com", "live.com", "zoho.com", "yandex.com", "protonmail.com", "gmx.com", "mail.ru", "inbox.com", "ymail.com", "hushmail.com", "gmx.net", "gmx.us", "gmx.de", "gmx.co.uk", "gmx.fr", "pokemail.net", ".gov.sg", ".gov.uk", "ky.edu"};

    /**
     * Array of probabilities for each email service provider
     */
    public static final int[] EMAIL_SERVICE_PROVIDER_PROBABILITIES = {50, 10, 10, 15, 5, 4, 3, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};

    /**
     * Array of possible email name formats
     */
    public static final String[] EMAIL_NAME_FORMATS = {"%s.%s", "%s%s", "%s", "%s_%s",};

    /**
     * Array of probabilities for each age range
     */
    public static final int[] ageDistribution = {32, 30, 23, 12, 2, 1};

    /**
     * Array of possible age ranges
     */
    public static final AgeRange[] ageRanges = {new AgeRange(1, 20), new AgeRange(20, 39), new AgeRange(40, 59), new AgeRange(60, 79), new AgeRange(80, 99), new AgeRange(100, 105)};

    /**
     * int[][] of possible male heights and their probabilities, in cm
     */
    public static final int[][] MALE_HEIGHTS = {{150, 5}, {155, 10}, {160, 20}, {165, 30}, {170, 20}, {175, 10}, {180, 5}};

    /**
     * int[][] of possible female heights and their probabilities, in cm
     */
    public static final int[][] FEMALE_HEIGHTS = {{145, 5}, {150, 10}, {155, 20}, {160, 30}, {165, 20}, {170, 10}, {175, 5}};

    /**
     * int[][] of possible other heights and their probabilities, in cm
     */
    public static final int[][] OTHER_HEIGHTS = {{145, 5}, {155, 10}, {160, 20}, {165, 34}, {170, 18}, {175, 8}, {180, 5}};

    /**
     * int[][] of possible male weights and their probabilities in KG
     */
    public static final int[][] MALE_WEIGHTS = {{50, 5}, {55, 10}, {60, 20}, {65, 30}, {70, 20}, {75, 10}, {80, 5}};

    /**
     * int[][] of all possible female weights and their probabilities in KG
     */
    public static final int[][] FEMALE_WEIGHTS = {{45, 5}, {50, 10}, {55, 20}, {60, 30}, {65, 20}, {70, 10}, {75, 5}};

    /**
     * int[][] of all possible other weights and their probabilities in KG
     */
    public static final int[][] OTHER_WEIGHTS = {{45, 5}, {55, 10}, {60, 20}, {65, 34}, {70, 18}, {75, 8}, {80, 5}};

    /**
     * Array of all possible religions
     */
    public static final String[] religions = {"Christianity", "Islam", "Hinduism", "Buddhism", "Folk Religions", "Other Religions", "Atheism", "Agnosticism", "Judaism", "Sikhism", "Chinese Traditional Religion", "Spiritism"};

    /**
     * Array of probabilities for each religion
     */
    public static final int[] religionProbabilities = {31, 24, 15, 6, 5, 4, 3, 3, 2, 2, 1, 1};
}
