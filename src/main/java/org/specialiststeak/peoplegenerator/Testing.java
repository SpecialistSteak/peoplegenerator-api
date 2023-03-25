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
        for (endpoints endpoints : endpoints.values()) {
            var uri = URI.create(endpoints.getUrl());
            var client = HttpClient.newHttpClient();
            var request = HttpRequest.newBuilder()
                    .GET()
                    .uri(uri)
                    .build();
            long end;
            long start = System.nanoTime();
            var response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            end = System.nanoTime();
            System.out.println(response.body());
            System.out.println("Time taken: " + (end - start) / 1000000 + "ms");
        }
    }

    enum endpoints {
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

        endpoints(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }
    }
}