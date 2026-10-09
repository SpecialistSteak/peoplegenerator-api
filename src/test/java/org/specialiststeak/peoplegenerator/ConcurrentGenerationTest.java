package org.specialiststeak.peoplegenerator;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.specialiststeak.peoplegenerator.person.objects.Person;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.specialiststeak.peoplegenerator.person.utils.Constants.*;
import static org.specialiststeak.peoplegenerator.person.utils.Utils.startup;

class ConcurrentGenerationTest {
    @BeforeAll
    static void loadData() {
        startup(false);
    }

    @Test
    void parallelGenerationKeepsCountryCodesConsistentAndInternalIndexesOutOfJson() throws Exception {
        Map<String, Set<String>> codes = new HashMap<>();
        for (int index = 0; index < countries.length; index++) codes.computeIfAbsent(countries[index], country -> new HashSet<>()).add(countryCodes[index]);
        Person[] people = Person.createPeople(5000);
        for (Person person : people) {
            assertTrue(codes.get(person.getAddress().getCountry()).contains(person.getAddress().getCountryCode()));
        }
        var json = new ObjectMapper().valueToTree(people[0]);
        assertEquals(21, json.size());
        assertEquals(9, json.get("address").size());
    }

    @Test
    void generatingAnotherPersonDoesNotChangeAnExistingPersonsJob() {
        Person person = new Person().setAge(30);
        String job = person.generateJob();
        Person.createPeople(100);
        assertEquals(job, person.generateJob());
    }
}
