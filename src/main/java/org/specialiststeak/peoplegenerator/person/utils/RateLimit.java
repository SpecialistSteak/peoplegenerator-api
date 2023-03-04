package org.specialiststeak.peoplegenerator.person.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public final class RateLimit {
    private static final Map<String, Instant> personRequestHistory = new HashMap<>();

    public static void rateLimit(HttpServletRequest request, final long RATE_LIMIT_TIME_IN_SECONDS) {
        String clientIp = getClientIp(request);
        if (isRequestRateLimited(clientIp, RATE_LIMIT_TIME_IN_SECONDS)) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many requests from this IP address. Please try again later.");
        }
        personRequestHistory.put(clientIp, Instant.now());
        logIp(clientIp);
    }

    private static boolean isRequestRateLimited(String clientIp, final long RATE_LIMIT_TIME_IN_SECONDS) {
        Instant lastRequestTime = RateLimit.personRequestHistory.get(clientIp);
        return lastRequestTime != null && lastRequestTime.plusSeconds(RATE_LIMIT_TIME_IN_SECONDS).isAfter(Instant.now());
    }

    private static void logIp(String clientIp) {
        File file = new File("ip_log.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ignored) {}
        }
        try {
            Files.write(Paths.get("ip_log.txt"), (clientIp + "\n").getBytes(), StandardOpenOption.APPEND);
        } catch (IOException ignored) {}
    }

    private static String getClientIp(HttpServletRequest request) {
        String clientIp = request.getHeader("X-Forwarded-For");
        if (clientIp == null) {
            clientIp = request.getRemoteAddr();
        }
        return clientIp;
    }
}
