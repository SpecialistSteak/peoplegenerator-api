package org.specialiststeak.peoplegenerator.person.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.specialiststeak.peoplegenerator.person.utils.WriteToFile.writeToFile;

@UtilityClass
public class RateLimit {
    private static final Map<String, Instant> personRequestHistory = new ConcurrentHashMap<>();
    private static final String SALT = "QIOEUF@#*Y@HFUHUQWHR(P#YUIFHQELRQ*RYPEFHO@#$OR@$R5344ewf7wea";

    public static void main(String[] args) {
        final String ip = "127.0.0.1";
        String hashed = sha256Hash(ip);
        System.out.println("IP: " + ip);
        System.out.println("Hashed: " + hashed);
        for (int i = 0; i < 1_000; i++) {
            new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    logIPToDatabase(ip, "Test");
                }
            }).start();
        }
    }

    public static void rateLimit(HttpServletRequest request, final long RATE_LIMIT_TIME_IN_SECONDS, final String WHERE) {
        String clientIp = request.getHeader("X-Forwarded-For");
        if (clientIp == null) {
            clientIp = request.getRemoteAddr();
        }

        Instant lastRequestTime = personRequestHistory.get(clientIp);
        if (lastRequestTime != null && lastRequestTime.plusSeconds(RATE_LIMIT_TIME_IN_SECONDS).isAfter(Instant.now())) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many requests from this IP address. Please try again later.");
        }
        personRequestHistory.put(clientIp, Instant.now());
        logIPToDatabase(clientIp, WHERE);
    }

    public static void rateLimitRequest(HttpServletRequest request, final double RATE_LIMIT_TIME_IN_SECONDS, final String WHERE) {
        String clientIp = request.getHeader("X-Forwarded-For");
        if (clientIp == null) {
            clientIp = request.getRemoteAddr();
        }

        Instant lastRequestTime = personRequestHistory.get(clientIp);
        Instant currentTime = Instant.now();
        if (lastRequestTime != null && lastRequestTime.plusMillis((long) (RATE_LIMIT_TIME_IN_SECONDS * 1000)).isAfter(currentTime)) {
            double timeRemaining = Duration.between(currentTime, lastRequestTime.plusMillis((long) (RATE_LIMIT_TIME_IN_SECONDS * 1000))).toMillis() / 1000.0;
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many requests from this IP address. Please try again in " + timeRemaining + " seconds.");
        }
        personRequestHistory.put(clientIp, currentTime);
        logIPToDatabase(clientIp, WHERE);
    }

    static void logIPToDatabase(String clientIp, String data) {
        writeToFile(data + ": " + sha256Hash(clientIp + SALT));
    }

    private static String sha256Hash(String ipAddress) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA3-256");
        } catch (NoSuchAlgorithmException e) {
            //Ignored :)
            System.out.println("CRITICAL ERROR: SHA3-256 does not exist!");
            e.printStackTrace();
        }
        assert digest != null;
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