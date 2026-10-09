# API reference

These routes use GET and generate new data each time you call them. You can include or omit the trailing slash. The examples assume the server is running at `http://localhost:8080`.

[Back to the README](../README.md)

| Route | Response |
| --- | --- |
| `/api/person/` | One person as a JSON object |
| `/api/person/{count}` | A gzip-compressed JSON array of 1 to 50,000 people |
| `/api/address/` | One address as a JSON object |
| `/api/lifestory/` | A JSON object with `person` and `lifestory` fields |

The life story is a text template filled with the generated person's fields.

### Individual fields

Each of these routes generates a fresh value. Calling several field endpoints will not return fields from the same person. Request `/api/person/` when you need a complete record.

| Route | Response | Person field |
| --- | --- | --- |
| `/api/person/name/` | text | `name` |
| `/api/person/age/` | integer | `age` |
| `/api/person/job/` | text | `job` |
| `/api/person/dateofbirth/` | text | `doB` |
| `/api/person/income/` | integer | `incomeUSD` |
| `/api/person/creditscore/` | integer | `creditScore` |
| `/api/person/creditcardnumber/` | text or empty body | `ccNumber` |
| `/api/person/maritalstatus/` | boolean | `married` |
| `/api/person/haschildren/` | boolean | `hasChildren` |
| `/api/person/height/` | number | `height` |
| `/api/person/weight/` | number | `weight` |
| `/api/person/eyecolor/` | text | `eyeColor` |
| `/api/person/email/` | text | `email` |
| `/api/person/gender/` | text | `gender` |
| `/api/person/hasdegree/` | boolean | `hasDegree` |
| `/api/person/gpa/` | number | `gpa` |
| `/api/person/bloodtype/` | text | `bloodType` |
| `/api/person/username/` | text | `username` |
| `/api/person/politicalleaning/` | number | `politicalLeaning` |
| `/api/person/religion/` | text | `religion` |

When you request a name or another text field, read the response with `response.text()` in JavaScript. You can use `response.json()` for numbers, booleans, and complete profiles. If the generated person is under 18, their `ccNumber` is null and the individual credit-card endpoint returns an empty response.

### Gzip, errors, and rate limits

Bulk requests always return an array compressed with gzip, even when you ask for one person. Add `--compressed` when using curl so it can read the response. Browsers and Python requests handle decompression for you. If you use Java's `HttpClient`, the website's [getting started page](../src/main/resources/templates/get-started.html) includes an example of how to decompress the response.

| Status | Meaning |
| --- | --- |
| `200` | The request succeeded |
| `400` | The bulk count is outside 1 to 50,000, or the path parameter cannot be parsed as an integer |
| `429` | The request arrived before the rate-limit interval elapsed |
| `503` | Four batches are already being generated. Wait before trying again; the response includes a 10-second Retry-After header. |

Wait at least 10 seconds after your last accepted API request before requesting a batch. For other requests, the interval is 50 milliseconds. These limits apply per IP address and cover all API routes.

The server checks the rate limit before validating a batch size, so an invalid count also starts the waiting period. Counts outside the allowed range return HTTP 400 with an empty response. Other errors may include a response body, so clients should check the status before trying to parse it.

The limiter stores recent requests in memory and removes entries older than a minute during periodic cleanup when requests arrive. Restarting the application also clears its history. It reads `X-Forwarded-For` when present and otherwise uses the remote address. This is a basic limiter for a single server, not a shared limit across multiple instances. A deployment behind a proxy should overwrite client-supplied forwarding headers.

## Response fields

### Person

| Field | Type | Description |
| --- | --- | --- |
| `name` | string | The person's full name. |
| `age` | integer | Age in years. |
| `job` | string | Job title, Student, or Retired. |
| `doB` | string | A Java Date.toString() value, including the server time zone. It is not ISO 8601. |
| `incomeUSD` | integer | Generated income in US dollars. |
| `creditScore` | integer | Generated credit score; 0 for people under 18. |
| `ccNumber` | string or null | Generated credit card number; null for people under 18. |
| `married` | boolean | Whether the person is married. |
| `hasChildren` | boolean | Whether the person has children. |
| `height` | number | Height in centimeters. |
| `weight` | number | Weight in kilograms. |
| `eyeColor` | string | Eye color in uppercase. |
| `email` | string | Generated email address. |
| `gender` | string | Male, Female, or Other. |
| `hasDegree` | boolean | Whether the person has a degree. |
| `gpa` | number | Grade point average from 0 to 4. |
| `bloodType` | string | Blood type, including the Rh factor. |
| `username` | string | Generated username. |
| `politicalLeaning` | number | Generated position on a scale from -1 (left) to 1 (right). |
| `religion` | string | Generated religious affiliation. |
| `address` | object | The address object described below. |

### Address

| Field | Type | Description |
| --- | --- | --- |
| `streetAddress` | string | Generated street address. |
| `city` | string | City selected from the bundled world-cities data. |
| `state` | string | State or province selected within the country. |
| `country` | string | Country name. |
| `zip` | string | Generated postal code. |
| `geonameId` | integer | GeoNames ID selected within the country, or -1 if unavailable. |
| `phoneNumber` | string | Country calling code and generated digits. |
| `ipAddress` | string | Generated IPv4 address. |
| `countryCode` | string | Two-letter country code. |

### Life story

| Field | Type | Description |
| --- | --- | --- |
| `person` | object | A complete person object |
| `lifestory` | string | A paragraph assembled from that person's fields |

### Example response

```json
{
  "name": "Carlton Wilkinson",
  "age": 51,
  "job": "Dental Hygienist",
  "incomeUSD": 70222,
  "creditScore": 457,
  "ccNumber": "6759-8169-3924-9789-13",
  "married": false,
  "hasChildren": true,
  "height": 160.0,
  "weight": 79.7,
  "eyeColor": "BROWN",
  "email": "wilkinson.wilkinson@hotmail.com",
  "gender": "Male",
  "hasDegree": true,
  "bloodType": "B-",
  "username": "carlton59",
  "politicalLeaning": -0.2,
  "religion": "Islam",
  "address": {
    "streetAddress": "25396 Delmar Circle",
    "city": "Tbilisi",
    "state": "Kvemo Kartli",
    "country": "Georgia",
    "zip": "4751",
    "geonameId": 610824,
    "phoneNumber": "+995 600764915",
    "ipAddress": "206.245.134.79",
    "countryCode": "GE"
  },
  "doB": "Sat Jun 26 12:18:34 EST 1976",
  "gpa": 2.6
}
```

The data is synthetic and the generator makes some age-based choices, such as Student or Retired job titles. It is not a model of a real population. City and state values can be selected independently within a country, and the generated birth date may not exactly match the age. Email addresses, phone numbers, and IP addresses are not restricted to reserved test ranges.

The server generates at most four batches at once. Additional bulk requests receive HTTP 503 so they cannot exhaust the heap by generating many large arrays together.

## Running a deployment

The rate limiter stores its history in memory, so restarting the application clears it. Accepted API requests append the route and a salted hash of the client IP to `ip.log` in the working directory.

Moesif analytics are disabled unless you set `MOESIF_APPLICATION_ID` to your own application ID. When enabled, the filter records request and response bodies.

The generated OpenAPI specification is available at `/v3/api-docs`, with an interactive explorer at `/swagger-ui/index.html`. The specification does not fully describe the compressed array response or the rate limits, so refer to this page for those details.
