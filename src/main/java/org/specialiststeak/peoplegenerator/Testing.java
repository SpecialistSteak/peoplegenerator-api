package org.specialiststeak.peoplegenerator;

import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@UtilityClass
public class Testing {
    public static void main(String[] args) throws IOException, InterruptedException {
        var uri = URI.create("http://localhost:8080/api/person/50000");
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        long end, start = System.nanoTime();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        end = System.nanoTime();
        System.out.println(response.body());
        System.out.println("Time taken: " + (end - start) / 1000000 + "ms");
    }
}

enum endpoints {
    SINGLE_PERSON("http://localhost:8080/api/person/"),
    MULTIPLE_PEOPLE("http://localhost:8080/api/person/50_000"),
    GENDER("http://localhost:8080/api/person/gender/"),
    NAME("http://localhost:8080/api/person/name/"),
    EMAIL("http://localhost:8080/api/person/email/"),
    AGE("http://localhost:8080/api/person/age/"),
    JOB("http://localhost:8080/api/person/job/"),
    DATE_OF_BIRTH("http://localhost:8080/api/person/dateofbirth/"),
    INCOME("http://localhost:8080/api/person/income/"),
    CREDIT_SCORE("http://localhost:8080/api/person/creditscore/"),
    CREDIT_CARD_NUMBER("http://localhost:8080/api/person/creditcardnumber/"),
    MARIITAL_STATUS("http://localhost:8080/api/person/maritalstatus/"),
    HAS_CHILDREN("http://localhost:8080/api/person/haschildren/"),
    HEIGHT("http://localhost:8080/api/person/height/"),
    WEIGHT("http://localhost:8080/api/person/weight/"),
    EYE_COLOR("http://localhost:8080/api/person/eyecolor/"),
    HAS_DEGREE("http://localhost:8080/api/person/hasdegree/"),
    GPA("http://localhost:8080/api/person/gpa/"),
    BLOOD_TYPE("http://localhost:8080/api/person/bloodtype/"),
    USERNAME("http://localhost:8080/api/person/username/"),
    RELIGION("http://localhost:8080/api/person/religion/"),
    POLITICAL_LEANING("http://localhost:8080/api/person/politicalleaning/"),
    ADDRESS("http://localhost:8080/api/address/");

    private final String url;

    endpoints(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
