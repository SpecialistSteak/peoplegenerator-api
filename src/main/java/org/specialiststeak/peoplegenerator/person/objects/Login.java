package org.specialiststeak.peoplegenerator.person.objects;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import static org.specialiststeak.peoplegenerator.person.utils.Constants.random;

@Data
@EqualsAndHashCode
public class Login {

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

    private final String uuid;
    private final String username;
    private final String password;
    private final String salt;
    private final String md5;
    private final String sha1;
    private final String sha256;

    public Login() {
        this.uuid = "uuid";
        this.username = "username";
        this.password = "password";
        this.salt = "salt";
        this.md5 = "md5";
        this.sha1 = "sha1";
        this.sha256 = "sha256";
    }

    public Login(String uuid, String username, String password, String salt, String md5, String sha1, String sha256) {
        this.uuid = uuid;
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.md5 = md5;
        this.sha1 = sha1;
        this.sha256 = sha256;
    }

    UUID generateUUID() {
        return UUID.randomUUID();
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

    public static void main(String[] args) {
        var login = new Login();
        System.out.println(login.generatePassword());
        System.out.println(login.generateSalt());
        System.out.println(login.generateMD5(login.password));
        System.out.println(login.generateSHA1(login.password, login.salt));
        System.out.println(login.generateSHA256(login.password, login.salt));
        System.out.println(login.generateUUID());
    }
}