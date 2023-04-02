package org.specialiststeak.peoplegenerator.person.objects;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import static org.specialiststeak.peoplegenerator.person.utils.Constants.random;

@Data
@AllArgsConstructor
public class Login {

    public static void main(String[] args) {
        var login = new Login("jon", "email");
        System.out.println(login);
        System.out.println("/=/=/=/=/=/=/=/=/=/=/=/=/=/=/=/=/=/=/=/=/=/=/=/=/=/=");
        System.out.println(login.generatePassword());
        System.out.println(login.generateSalt());
        System.out.println(login.generateMD5(login.password));
        System.out.println(login.generateSHA1(login.password, login.salt));
        System.out.println(login.generateSHA256(login.password, login.salt));
        System.out.println(login.generateUUID());
    }

    private static final String ALL_SYMBOLS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz~`!@#$%^&*()-_=+[{]}\\|;:'\",<.>/?";
    private static final MessageDigest md5Digest;
    private static final MessageDigest sha1Digest;
    private static final MessageDigest sha256Digest;

    static {
        try {
            md5Digest = MessageDigest.getInstance("MD5");
            sha1Digest = MessageDigest.getInstance("SHA-1");
            sha256Digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public Login(String username, String email) {
        this.uuid = generateUUID().toString();
        this.username = username;
        this.email = email;
        this.password = generatePassword();
        this.salt = generateSalt();
        this.md5 = generateMD5(password);
        this.sha1 = generateSHA1(password, salt);
        this.sha256 = generateSHA256(password, salt);
        this.has2fa = generateTwoFactorAuthentification();
    }

    public Login(Person p1) {
        this.uuid = generateUUID().toString();
        this.username = p1.getUsername();
        this.email = p1.getEmail();
        this.password = generatePassword();
        this.salt = generateSalt();
        this.md5 = generateMD5(password);
        this.sha1 = generateSHA1(password, salt);
        this.sha256 = generateSHA256(password, salt);
        this.has2fa = generateTwoFactorAuthentification();
    }

    private final String uuid;
    private final String username;
    private final String email;
    private final String password;
    private final String salt;
    private final String md5;
    private final String sha1;
    private final String sha256;
    private final boolean has2fa;

    UUID generateUUID() {
        return UUID.randomUUID();
    }

    private boolean generateTwoFactorAuthentification() {
        return random.nextDouble() < .3;
    }

    String generatePassword() {
        StringBuilder pw = new StringBuilder();
        byte length = (byte) (random.nextInt(20) + 8);
        for (int i = 0; i < length; i++) {
            pw.append(randomChar());
        }
        return pw.toString();
    }

    String generateSalt() {
        return generatePassword().substring(0, 8);
    }

    public String generateMD5(String password) {
        byte[] messageDigest = md5Digest.digest(password.getBytes());
        return bytesToHex(messageDigest);
    }

    public String generateSHA1(String password, String salt) {
        byte[] messageDigest = sha1Digest.digest((password + salt).getBytes());
        return bytesToHex(messageDigest);
    }

    public String generateSHA256(String password, String salt) {
        byte[] messageDigest = sha256Digest.digest((password + salt).getBytes());
        return bytesToHex(messageDigest);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }


    private char randomChar() {
        return ALL_SYMBOLS.charAt(random.nextInt(ALL_SYMBOLS.length()));
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}