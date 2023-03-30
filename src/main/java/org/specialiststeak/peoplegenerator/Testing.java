package org.specialiststeak.peoplegenerator;

import lombok.experimental.UtilityClass;
import org.specialiststeak.peoplegenerator.person.objects.Person;

import java.io.IOException;

import static org.specialiststeak.peoplegenerator.person.objects.Person.createPeople;
import static org.specialiststeak.peoplegenerator.person.utils.Utils.startup;

@UtilityClass
public class Testing {
    public static void main(String[] args) throws IOException, InterruptedException {
//        for (Endpoints endpts : Endpoints.values()) {
//            var uri = URI.create(endpts.getUrl());
//            var client = HttpClient.newHttpClient();
//            var request = HttpRequest.newBuilder()
//                    .GET()
//                    .uri(uri)
//                    .build();
//            long end;
//            long start = System.nanoTime();
//            var response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
//            end = System.nanoTime();
//            System.out.println(response.body());
//            System.out.println("Time taken: " + (end - start) / 1000000 + "ms");
//        }
        startup(true);
        startup(true);
        testSpeed(50_000);
    }

    public static void testSpeed(int num_iterations) {
        long start, end;
        start = System.nanoTime();
        Person[] p = createPeople(num_iterations);
        end = System.nanoTime();
        long time1 = end - start;

        start = System.nanoTime();
        Person[] p2 = new Person[num_iterations];
        for (int i = 0; i < num_iterations; i++) {
            p2[i] = new Person();
        }
        end = System.nanoTime();
        long time2 = end - start;

        System.out.println("CreatePeople Method: " + time1 + "ns");
        System.out.println("Person Constructor:  " + time2 + "ns");
    }
}

enum Endpoints {
    SINGLE_PERSON("http://localhost/api/person/"),
    MULTIPLE_PEOPLE("http://localhost/api/person/50_000"),
    GENDER("http://localhost/api/person/gender/"),
    NAME("http://localhost/api/person/name/"),
    EMAIL("http://localhost/api/person/email/"),
    AGE("http://localhost/api/person/age/"),
    JOB("http://localhost/api/person/job/"),
    DATE_OF_BIRTH("http://localhost/api/person/dateofbirth/"),
    INCOME("http://localhost/api/person/income/"),
    CREDIT_SCORE("http://localhost/api/person/creditscore/"),
    CREDIT_CARD_NUMBER("http://localhost/api/person/creditcardnumber/"),
    MARIITAL_STATUS("http://localhost/api/person/maritalstatus/"),
    HAS_CHILDREN("http://localhost/api/person/haschildren/"),
    HEIGHT("http://localhost/api/person/height/"),
    WEIGHT("http://localhost/api/person/weight/"),
    EYE_COLOR("http://localhost/api/person/eyecolor/"),
    HAS_DEGREE("http://localhost/api/person/hasdegree/"),
    GPA("http://localhost/api/person/gpa/"),
    BLOOD_TYPE("http://localhost/api/person/bloodtype/"),
    USERNAME("http://localhost/api/person/username/"),
    RELIGION("http://localhost/api/person/religion/"),
    POLITICAL_LEANING("http://localhost/api/person/politicalleaning/"),
    ADDRESS("http://localhost/api/address/"),
    LIFESTORY("http://localhost/api/person/lifestory/");

    private final String url;

    Endpoints(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}