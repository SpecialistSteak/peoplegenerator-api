package org.specialiststeak.peoplegenerator.person.utils;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;

class RateLimitConcurrencyTest {
    @Test
    void simultaneousRequestsFromOneClientOnlyAdmitOneRequest() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr(UUID.randomUUID().toString());
        CountDownLatch ready = new CountDownLatch(16);
        CountDownLatch start = new CountDownLatch(1);
        ExecutorService executor = Executors.newFixedThreadPool(16);
        try {
            var futures = new java.util.ArrayList<Future<Boolean>>();
            for (int index = 0; index < 16; index++) futures.add(executor.submit(() -> {
                ready.countDown();
                start.await();
                try {
                    RateLimit.rateLimit(request, 10, "concurrency-test");
                    return true;
                } catch (ResponseStatusException error) {
                    assertEquals(429, error.getStatusCode().value());
                    return false;
                }
            }));
            assertTrue(ready.await(5, TimeUnit.SECONDS));
            start.countDown();
            int accepted = 0;
            for (Future<Boolean> result : futures) if (result.get()) accepted++;
            assertEquals(1, accepted);
        } finally {
            executor.shutdownNow();
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    void cleanupRemovesOldClientsAndPreservesRecentRequests() {
        Map<String, Instant> history = (Map<String, Instant>) ReflectionTestUtils.getField(RateLimit.class, "personRequestHistory");
        String oldClient = UUID.randomUUID().toString(), recentClient = UUID.randomUUID().toString();
        history.put(oldClient, Instant.now().minusSeconds(120));
        history.put(recentClient, Instant.now());
        ((AtomicLong) ReflectionTestUtils.getField(RateLimit.class, "lastHistoryCleanup")).set(0);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr(UUID.randomUUID().toString());
        RateLimit.rateLimit(request, 10, "cleanup-test");
        assertFalse(history.containsKey(oldClient));
        assertTrue(history.containsKey(recentClient));
    }
}
