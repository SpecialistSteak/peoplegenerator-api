package org.specialiststeak.peoplegenerator.person.objects;

import com.google.gson.Gson;
import lombok.Data;

import static org.specialiststeak.peoplegenerator.person.utils.Utils.startup;

@Data
public class Lifestory {

    public static void main(String[] args) {
        startup(false);
        startup(true);
        Person p = new Person();
        String lifeStory = formatPerson(p);
        System.out.println("Person: " + p);
        System.out.println("Lifestory (String): " + lifeStory);
        Lifestory l = lifestoryFactory(p);
        System.out.println("Lifestory (Object): " + l);
    }

    private Person person;
    private String lifestory;

    public Lifestory(Person p, String formatPerson) {
        this.person = p;
        this.lifestory = formatPerson;
    }

    // Not *technically* a factory, but it's close enough, and I would like to test
    // how this feature is received before using dependency injection and properly making a factory.
    public static Lifestory lifestoryFactory(Person person) {
        return new Lifestory(person, formatPerson(person));
    }

    public static String formatPerson(Person person) {
        String genderPronoun;
        String genderPronoun2;
        switch (person.getGender()){
            case "Male" -> {
                genderPronoun = "He";
                genderPronoun2 = "His";
            }
            case "Female" -> {
                genderPronoun = "She";
                genderPronoun2 = "Her";
            }
            default -> {
                genderPronoun = "They";
                genderPronoun2 = "Their";
            }
        }
        return person.getName() + " is a " + person.getAge() + "-year-old who was born on " +
                person.getDoB() + ". " + person.getName().substring(0, person.getName().indexOf(" "))
                + " works as a " + person.getJob().toLowerCase() + " and lives in " +
                person.getAddress().getCity() + ", " + person.getAddress().getState() +
                ", " + person.getAddress().getCountry() + ". " +
                genderPronoun + " is " +
                (person.isMarried() ? "married" : "not married") + " and has " +
                (person.isHasChildren() ? "children" : "no children") + ". " +
                genderPronoun + " is " + person.getHeight() + "cm tall and weighs " +
                person.getWeight() + " kg," +
                " and has " + person.getEyeColor().toLowerCase() + " eyes and a blood type of " +
                person.getBloodType() + ". " +
                genderPronoun + " has a GPA of " + person.getGPA() +
                ", and " + (person.isHasDegree() ? "has" : "doesn't have")+ " a degree. " + genderPronoun2 + " income is $" +
                person.getIncomeUSD() +
                ", and " + genderPronoun.toLowerCase() + " has a credit score of " + person.getCreditScore() + ". " +
                (person.getCcNumber() == null ? "" :
                        genderPronoun2 + " credit card number is " + person.getCcNumber()) +
                ". " + genderPronoun + " uses " + person.getEmail() + " for " + genderPronoun2.toLowerCase()
                + " email address and " + person.getUsername() + " for " + genderPronoun2 +
                " username on various websites. " + genderPronoun + " follows " +
                person.getReligion() + " and " + genderPronoun2.toLowerCase() +
                " political leaning is " + person.getPoliticalLeaning() + ". " +
                genderPronoun + " has a geonameid of " + person.getAddress().getGeonameId() +
                " and " + genderPronoun2.toLowerCase() + " phone number is " +
                person.getAddress().getPhoneNumber() + ", and " + genderPronoun2.toLowerCase()
                + " IP address is " + person.getAddress().getIpAddress();
    }

    public String toString() {
        return new Gson().newBuilder().setPrettyPrinting().create().toJson(this);
    }
}