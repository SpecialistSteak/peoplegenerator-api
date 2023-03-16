package org.specialiststeak.peoplegenerator.person.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class RateLimit {
    private static final Map<String, Instant> personRequestHistory = new HashMap<>();

    public static void rateLimit(HttpServletRequest request, final long RATE_LIMIT_TIME_IN_SECONDS) {
        String clientIp = request.getHeader("X-Forwarded-For");
        if (clientIp == null) {
            clientIp = request.getRemoteAddr();
        }

        Instant lastRequestTime = personRequestHistory.get(clientIp);
        if (lastRequestTime != null && lastRequestTime.plusSeconds(RATE_LIMIT_TIME_IN_SECONDS).isAfter(Instant.now())) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many requests from this IP address. Please try again later.");
        }
        personRequestHistory.put(clientIp, Instant.now());

//        ipLog(clientIp);
    }
    void ipLog(String clientIp){
        //TODO: This should be a database, not a file. Make sure to run it on another thread.
    }
}