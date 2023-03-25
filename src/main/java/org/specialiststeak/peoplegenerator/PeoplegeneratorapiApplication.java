package org.specialiststeak.peoplegenerator;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.specialiststeak.peoplegenerator.person.peoplelist.Address;
import org.specialiststeak.peoplegenerator.person.peoplelist.Lifestory;
import org.specialiststeak.peoplegenerator.person.peoplelist.Person;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.IOException;

import static org.specialiststeak.peoplegenerator.person.peoplelist.Lifestory.lifestoryFactory;
import static org.specialiststeak.peoplegenerator.person.peoplelist.Person.*;
import static org.specialiststeak.peoplegenerator.person.utils.RateLimit.rateLimit;
import static org.specialiststeak.peoplegenerator.person.utils.Utils.compressByteArray;
import static org.specialiststeak.peoplegenerator.person.utils.Utils.startup;

@SpringBootApplication
@Controller
@EnableWebMvc
public class PeoplegeneratorapiApplication implements Runnable {

    @Override
    public void run() {
        System.out.println("Running");
    }

    public static void main(String[] args) {
        startup(true);
        personTimeTest();
        startup(true);
        SpringApplication.run(PeoplegeneratorapiApplication.class, args);
    }

    // Index page
    @GetMapping("/")
    public String index() {
        return "index";
    }

    // Get-started page
    @GetMapping({"/get-started", "/get-started/"})
    public String getStarted() {
        return "get-started";
    }

    // endpoints page
    @GetMapping({"/endpoints", "/endpoints/"})
    public String endpoints() {
        return "endpoints";
    }

    // fields page
    @GetMapping({"/fields", "/fields/"})
    public String fields() {
        return "fields";
    }

    // about page
    @GetMapping({"/about", "/about/"})
    public String about() {
        return "about";
    }


    // Returns a single person Object
    @GetMapping({"/api/person/", "/api/person"})
    @ResponseBody
    public Person getPerson(HttpServletRequest request) {
        rateLimit(request, 2);
        return new Person();
    }

    // Returns a number of people Objects in an array < 50_000
    @GetMapping(value = {"/api/person/{number}", "/api/person/{number}/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<byte[]> getCompressedPerson(@PathVariable int number, HttpServletRequest request) throws IOException {
        rateLimit(request, 10);
        if (number < 1 || number > 50_000) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(null);
        }

        byte[] compressedData = compressByteArray(new ObjectMapper().writeValueAsBytes(createPeople(number)));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setContentLength(compressedData.length);
        headers.set("Content-Encoding", "gzip");

        return new ResponseEntity<>(compressedData, headers, HttpStatus.OK);
    }

    // Returns a random gender String
    @GetMapping({"/api/person/gender/", "/api/person/gender"})
    @ResponseBody
    public String gender(HttpServletRequest request) {
        rateLimit(request, 1);
        return new Person().getGender();
    }


    // Returns a random name String
    @GetMapping({"/api/person/name/", "/api/person/name"})
    @ResponseBody
    public String name(HttpServletRequest request) {
        rateLimit(request, 1);
        return new Person().getName();
    }

    // Returns a random email String
    @GetMapping({"/api/person/email/", "/api/person/email"})
    @ResponseBody
    public String email(HttpServletRequest request) {
        rateLimit(request, 1);
        return new Person().getEmail();
    }

    // Returns a persons age as an int
    @GetMapping({"/api/person/age/", "/api/person/age"})
    @ResponseBody
    public int age(HttpServletRequest request) {
        rateLimit(request, 1);
        return generateAge();
    }

    // returns a persons job as a String
    @GetMapping({"/api/person/job/", "/api/person/job"})
    @ResponseBody
    public String job(HttpServletRequest request) {
        rateLimit(request, 1);
        return new Person().getJob();
    }

    // returns a persons date of birth as a String
    @GetMapping({"/api/person/dateofbirth/", "/api/person/dateofbirth"})
    public String dateofbirth(HttpServletRequest request) {
        rateLimit(request, 1);
        return new Person().getDateOfBirth();
    }

    // returns a person's income as an int
    @GetMapping({"/api/person/income/", "/api/person/income"})
    @ResponseBody
    public int income(HttpServletRequest request) {
        rateLimit(request, 1);
        return new Person().getIncomeInUSD();
    }

    // returns a person's credit score as an int
    @GetMapping({"/api/person/creditscore/", "/api/person/creditscore"})
    @ResponseBody
    public int creditscore(HttpServletRequest request) {
        rateLimit(request, 1);
        return new Person().getCreditScore();
    }

    // returns a person's credit card number as a String
    @GetMapping({"/api/person/creditcardnumber/", "/api/person/creditcardnumber"})
    @ResponseBody
    public String creditcardnumber(HttpServletRequest request) {
        rateLimit(request, 1);
        return new Person().getCreditCardNumber();
    }

    // returns a person's marital status as a boolean
    @GetMapping({"/api/person/maritalstatus/", "/api/person/maritalstatus"})
    @ResponseBody
    public boolean maritalstatus(HttpServletRequest request) {
        rateLimit(request, 1);
        return new Person().isHasChildren();
    }

    // returns a person's has children as a boolean
    @GetMapping({"/api/person/haschildren/", "/api/person/haschildren"})
    @ResponseBody
    public boolean haschildren(HttpServletRequest request) {
        rateLimit(request, 1);
        return new Person().isMarried();
    }

    // returns a person's height as a double
    @GetMapping({"/api/person/height/", "/api/person/height"})
    @ResponseBody
    public double height(HttpServletRequest request) {
        rateLimit(request, 1);
        return new Person().getHeight();
    }

    // returns a person's weight as a double
    @GetMapping({"/api/person/weight/", "/api/person/weight"})
    @ResponseBody
    public double weight(HttpServletRequest request) {
        rateLimit(request, 1);
        return new Person().getWeight();
    }

    // return's a person's eye color as a string
    @GetMapping({"/api/person/eyecolor/", "/api/person/eyecolor"})
    @ResponseBody
    public String eyecolor(HttpServletRequest request) {
        rateLimit(request, 1);
        return generateEyeColor();
    }

    // returns whether a person has a degree or not as a boolean
    @GetMapping({"/api/person/hasdegree/", "/api/person/hasdegree"})
    @ResponseBody
    public boolean hasdegree(HttpServletRequest request) {
        rateLimit(request, 1);
        return new Person().generateHasDegree();
    }

    // returns a person's GPA as a double
    @GetMapping({"/api/person/gpa/", "/api/person/gpa"})
    @ResponseBody
    public double gpa(HttpServletRequest request) {
        rateLimit(request, 1);
        return generateGPA();
    }

    // returns a person's blood type as a string
    @GetMapping({"/api/person/bloodtype/", "/api/person/bloodtype"})
    @ResponseBody
    public String bloodtype(HttpServletRequest request) {
        rateLimit(request, 1);
        return generateBloodType();
    }

    // Returns a person's username as a String
    @GetMapping({"/api/person/username/", "/api/person/username"})
    @ResponseBody
    public String username(HttpServletRequest request) {
        rateLimit(request, 1);
        return new Person().getUsername();
    }

    // returns a person's religion as a String
    @GetMapping({"/api/person/religion/", "/api/person/religion"})
    @ResponseBody
    public String religion(HttpServletRequest request) {
        rateLimit(request, 1);
        return new Person().getReligion();
    }

    // returns a person's political leaning as a double
    @GetMapping({"/api/person/politicalleaning/", "/api/person/politicalleaning"})
    @ResponseBody
    public double politicalLeaning(HttpServletRequest request) {
        rateLimit(request, 1);
        return new Person().getPoliticalLeaning();
    }

    // returns an address object
    @GetMapping({"/api/address", "/api/address/"})
    @ResponseBody
    public Address generateAddress(HttpServletRequest request) {
        rateLimit(request, 1);
        return new Address();
    }

    //returns a lifestory object containing a lifestory string and a person object
    @GetMapping({"/api/lifestory", "/api/lifestory/"})
    @ResponseBody
    public Lifestory generateLifeStory(HttpServletRequest request) {
        rateLimit(request, 1);
        return lifestoryFactory(new Person());
    }

//    @PostMapping("/api/feedback/")
//    @ResponseBody
//    public ResponseEntity<String> feedback(@RequestBody String feedback) {
//        /*Feedback logic goes here...*/
//        return ResponseEntity.ok()
//                .contentType(MediaType.APPLICATION_JSON)
//                .body("Thank you for your feedback!");
//    }
}