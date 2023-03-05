package org.specialiststeak.peoplegenerator;

import com.opencsv.exceptions.CsvValidationException;
import jakarta.servlet.http.HttpServletRequest;
import org.specialiststeak.peoplegenerator.person.peoplelist.Person;
import org.specialiststeak.peoplegenerator.person.utils.Address;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import static org.specialiststeak.peoplegenerator.person.peoplelist.Person.*;
import static org.specialiststeak.peoplegenerator.person.utils.RateLimit.rateLimit;
import static org.specialiststeak.peoplegenerator.person.utils.Utils.startup;

@SpringBootApplication
@Controller
@EnableWebMvc
public class PeoplegeneratorapiApplication {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/get-started")
    public String getStarted() {
        return "get-started";
    }

    @GetMapping("/endpoints")
    public String endpoints() {
        return "endpoints";
    }

    @GetMapping("/fields")
    public String fields() {
        return "fields";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    public static void main(String[] args) throws IOException, CsvValidationException {
        startup();
        personTimeTest();
        System.out.print("\033[H\033[2J");
        System.out.flush();
        startup();
        SpringApplication.run(PeoplegeneratorapiApplication.class, args);
    }

    @GetMapping("/api/person/")
    @ResponseBody
    public Person getPerson(HttpServletRequest request) {
        rateLimit(request, 2);
        return new Person();
    }

    @GetMapping("/api/person/" + "{number}")
    @ResponseBody
    public ResponseEntity<Person[]> getPerson(@PathVariable int number, HttpServletRequest request) {
        rateLimit(request, 10);
        if (number <= 0 || number > 50000) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(null);
        }

        List<Person> people = new ArrayList<>(number);
        for (int i = 0; i < number; i++) {
            people.add(new Person());
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(people.toArray(new Person[0]));
    }

    @GetMapping("/api/person/gender/")
    @ResponseBody
    public String gender(HttpServletRequest request) {
        rateLimit(request, 1);
        return new Person().getGender();
    }

    @GetMapping("/api/person/name/")
    @ResponseBody
    public String name(HttpServletRequest request) {
        rateLimit(request, 1);
        return new Person().getName();
    }

    @GetMapping("/api/person/email/")
    @ResponseBody
    public String email(HttpServletRequest request) {
        rateLimit(request, 1);
        return new Person().getEmail();
    }

    @GetMapping("/api/person/age/")
    @ResponseBody
    public int age(HttpServletRequest request) {
        rateLimit(request, 1);
        return generateAge();
    }

    @GetMapping("/api/person/job/")
    @ResponseBody
    public String job(HttpServletRequest request) {
        rateLimit(request, 1);
        return new Person().getJob();
    }

    @GetMapping("/api/person/dateofbirth/")
    public String dateofbirth(HttpServletRequest request) {
        rateLimit(request, 1);
        return new Person().getDateOfBirth();
    }

    @GetMapping("/api/person/income/")
    @ResponseBody
    public int income(HttpServletRequest request) {
        rateLimit(request, 1);
        return new Person().getIncomeInUSD();
    }

    @GetMapping("/api/person/creditscore/")
    @ResponseBody
    public int creditscore(HttpServletRequest request) {
        rateLimit(request, 1);
        return new Person().getCreditScore();
    }

    @GetMapping("/api/person/creditcardnumber/")
    @ResponseBody
    public String creditcardnumber(HttpServletRequest request) {
        rateLimit(request, 1);
        return new Person().getCreditCardNumber();
    }

    @GetMapping("/api/person/maritalstatus/")
    @ResponseBody
    public boolean maritalstatus(HttpServletRequest request) {
        rateLimit(request, 1);
        return new Person().isHasChildren();
    }

    @GetMapping("/api/person/haschildren/")
    @ResponseBody
    public boolean haschildren(HttpServletRequest request) {
        rateLimit(request, 1);
        return new Person().isMarried();
    }

    @GetMapping("/api/person/height/")
    @ResponseBody
    public double height(HttpServletRequest request) {
        rateLimit(request, 1);
        return new Person().getHeight();
    }

    @GetMapping("/api/person/weight/")
    @ResponseBody
    public double weight(HttpServletRequest request) {
        rateLimit(request, 1);
        return new Person().getWeight();
    }

    @GetMapping("/api/person/eyecolor/")
    @ResponseBody
    public String eyecolor(HttpServletRequest request) {
        rateLimit(request, 1);
        return generateEyeColor();
    }

    @GetMapping("/api/person/hasdegree/")
    @ResponseBody
    public boolean hasdegree(HttpServletRequest request) {
        rateLimit(request, 1);
        return generateHasDegree();
    }

    @GetMapping("/api/person/gpa/")
    @ResponseBody
    public double gpa(HttpServletRequest request) {
        rateLimit(request, 1);
        return generateGPA();
    }

    @GetMapping("/api/person/bloodtype/")
    @ResponseBody
    public String bloodtype(HttpServletRequest request) {
        rateLimit(request, 1);
        return generateBloodType();
    }

    @GetMapping("/api/person/username/")
    @ResponseBody
    public String username(HttpServletRequest request) {
        rateLimit(request, 1);
        return new Person().getUsername();
    }

    @GetMapping("/api/person/religion/")
    @ResponseBody
    public String religion(HttpServletRequest request) {
        rateLimit(request, 1);
        return new Person().getReligion();
    }

    @GetMapping("/api/person/politicalleaning/")
    @ResponseBody
    public double politicalleaning(HttpServletRequest request) {
        rateLimit(request, 1);
        return new Person().getPoliticalLeaning();
    }

    @GetMapping("/api/location/")
    @ResponseBody
    public Address getAddress(HttpServletRequest request) {
        rateLimit(request, 2);
        Person p = new Person();
        return new Address(
                p.getAddress(),
                p.getNationality(),
                p.getCountryCode(),
                p.getPhoneNumber(),
                p.getIPAddress()
        );
    }

    @PostMapping("/api/feedback/")
    @ResponseBody
    public ResponseEntity<String> feedback(@RequestParam String feedback, HttpServletRequest request) {
        rateLimit(request, 10);
        if (feedback.length() > 1000) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Feedback is too long");
        }

        feedback = feedback.replaceAll("\\\\", "");
        try {
            Path filePath = Paths.get("feedback.txt");
            if (Files.notExists(filePath)) {
                Files.createFile(filePath);
            }

            byte[] feedbackBytes = feedback.getBytes(StandardCharsets.UTF_8);
            if (Files.size(filePath) + feedbackBytes.length > 100000) {
                throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE,
                        "Feedback file is too large. Please try again in a few days.");
            }

            Files.write(filePath, feedbackBytes, StandardOpenOption.APPEND);
            Files.write(filePath, "\n".getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to write feedback to file");
        }

        return ResponseEntity.ok("Thank you for your feedback!");
    }
}