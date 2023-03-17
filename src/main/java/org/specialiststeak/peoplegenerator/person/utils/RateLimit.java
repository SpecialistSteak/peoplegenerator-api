package org.specialiststeak.peoplegenerator.person.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.specialiststeak.peoplegenerator.person.temp.TimeTester.runCode;

@UtilityClass
public class RateLimit {
    private static final Map<String, Instant> personRequestHistory = new ConcurrentHashMap<>();

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

//        logIPToDatabase(clientIp);
    }

    static void logIPToDatabase(String clientIp) {
        String ip = sha256Hash(clientIp);
        //TODO: This should be a database, not a file. Make sure to run it on another thread.
    }

    public static void main(String[] args) {
        String ip = "127.0.0.1";
        String hashed = sha256Hash(ip);
        System.out.println("IP: " + ip);
        System.out.println("Hashed: " + hashed);
        System.out.println("Ratelimit Took: " +
            runCode(() -> {
                try {
                    rateLimitMock(null, 2);
                } catch (Exception ignored) {
                    //Ignored :)
                }
            }) + "ns"
        );
    }

    private static void rateLimitMock(HttpServletRequest request, final long RATE_LIMIT_TIME_IN_SECONDS) {
        String clientIp = "";
        try {
            clientIp = request.getHeader("X-Forwarded-For");
        } catch (Exception ignored) {
            //Ignored :)
        }
        try {
            if (clientIp == null) {
                clientIp = request.getRemoteAddr();
            }
        } catch (Exception ignored) {
            //Ignored :)
        }

        Instant lastRequestTime = personRequestHistory.get(clientIp);
        if (lastRequestTime != null && lastRequestTime.plusSeconds(RATE_LIMIT_TIME_IN_SECONDS).isAfter(Instant.now())) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many requests from this IP address. Please try again later.");
        }
        personRequestHistory.put(clientIp, Instant.now());

        logIPToDatabase("127.0.0.1");
    }

    private static String sha256Hash(String ipAddress) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA3-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] hash = digest.digest((ipAddress).getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}