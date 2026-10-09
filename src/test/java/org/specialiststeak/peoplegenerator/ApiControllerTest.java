package org.specialiststeak.peoplegenerator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.specialiststeak.peoplegenerator.person.objects.Person;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ApiControllerTest {
    private MockMvc mvc;
    private String client;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(new PeoplegeneratorapiApplication()).build();
        client = UUID.randomUUID().toString();
    }

    @Test
    void dateOfBirthReturnsTextInsteadOfResolvingAView() throws Exception {
        try (MockedConstruction<Person> ignored = mockConstruction(Person.class, (person, context) ->
                when(person.getDoB()).thenReturn("Tue Sep 21 11:25:21 UTC 1993"))) {
            mvc.perform(get("/api/person/dateofbirth").header("X-Forwarded-For", client))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Tue Sep 21 11:25:21 UTC 1993"));
        }
    }

    @Test
    void maritalStatusUsesMarriageRatherThanChildren() throws Exception {
        try (MockedConstruction<Person> ignored = mockConstruction(Person.class, (person, context) -> {
            when(person.isMarried()).thenReturn(true);
            when(person.isHasChildren()).thenReturn(false);
        })) {
            mvc.perform(get("/api/person/maritalstatus").header("X-Forwarded-For", client))
                    .andExpect(status().isOk()).andExpect(content().string("true"));
        }
    }

    @Test
    void hasChildrenUsesChildrenRatherThanMarriage() throws Exception {
        try (MockedConstruction<Person> ignored = mockConstruction(Person.class, (person, context) -> {
            when(person.isMarried()).thenReturn(true);
            when(person.isHasChildren()).thenReturn(false);
        })) {
            mvc.perform(get("/api/person/haschildren").header("X-Forwarded-For", client))
                    .andExpect(status().isOk()).andExpect(content().string("false"));
        }
    }

    @Test
    void bulkRequestsRejectCountsOutsideTheDocumentedRange() throws Exception {
        for (int count : new int[]{0, -1, 50001}) {
            mvc.perform(get("/api/person/" + count).header("X-Forwarded-For", UUID.randomUUID().toString()))
                    .andExpect(status().isBadRequest());
        }
    }

    @Test
    void bulkRequestsShareTheSingleRequestRateLimitHistory() throws Exception {
        try (MockedConstruction<Person> ignored = mockConstruction(Person.class, (person, context) ->
                when(person.getName()).thenReturn("Alex Example"))) {
            mvc.perform(get("/api/person/name").header("X-Forwarded-For", client)).andExpect(status().isOk());
            mvc.perform(get("/api/person/1").header("X-Forwarded-For", client)).andExpect(status().isTooManyRequests());
        }
    }
}
