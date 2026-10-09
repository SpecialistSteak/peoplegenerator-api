# Local load test

These measurements were taken on October 9, 2026, after the concurrency fixes in `0acee53`. The tested working tree also included the pending website, endpoint, and build updates. The application generated a new profile for every request.

The host was an Apple M4 Pro Mac with 12 logical CPUs and 24 GiB of memory, running macOS 27.0.1. The API ran in Docker with two CPU cores, a hard 2 GiB memory limit, and no additional swap allowance. Java used a 256 MiB initial heap and a 1,536 MiB maximum heap. The runtime was Eclipse Temurin 22.

A separate Node.js 22.23.2 load generator ran on the same Mac and connected through Docker's published localhost port. It used HTTP keep-alive and up to four worker processes. Each concurrency level ran for 20 seconds after a 15-second warm-up. A separate 60-second run measured sustained throughput at 32 concurrent requests. The concurrency levels ran sequentially against the same application process.

The load generator cycled through 4,096 simulated client identities per worker using `X-Forwarded-For`. This exercised generation throughput across many identities while keeping the per-client limiter active. It does not represent the request rate allowed for one client. Every response was consumed; the first response from each connection worker was also parsed and checked for a profile and address. The sustained test does not validate every field of every response.

## Results

Container memory peaked at 1.16 GiB across the throughput sweep and sustained run. There were no out-of-memory events, and the application remained running until the test container was deliberately stopped.

The one-minute run completed **1,157,452 requests**, all with HTTP 200, averaging **19,290 requests per second**. Its p95 latency was **2.9 ms** and its p99 was **25.9 ms**. The longest response took about one second.

| Concurrent requests | Successful requests/s | p95 latency | p99 latency | HTTP 200 | HTTP 429 |
| --- | ---: | ---: | ---: | ---: | ---: |
| 1 | 3,774 | 0.4 ms | 1.0 ms | 75,472 | 0 |
| 8 | 14,108 | 1.1 ms | 1.7 ms | 282,173 | 0 |
| 16 | 20,792 | 1.4 ms | 2.7 ms | 415,861 | 0 |
| 32 | 23,650 | 2.2 ms | 18.7 ms | 473,401 | 0 |
| 64 | 21,513 | 4.7 ms | 34.7 ms | 455,500 | 0 |
| 128 | 18,519 | 18.2 ms | 201.4 ms | 405,337 | 16 |

At 128 concurrent requests, 16 requests received HTTP 429 from the per-client limiter. The chart counts only successful responses. No stage had an HTTP 5xx response or a network error. The [recorded results](results.json) include the warm-up and sustained run as well as each concurrency level.

The README chart puts successful throughput above the 95th- and 99th-percentile response times, with the same concurrency axis for both. The 99th percentile is the response time within which 99% of measured requests completed. Its logarithmic scale makes the sub-millisecond results and the 201 ms tail at high concurrency readable together. Both panels use the 20-second sweep; the one-minute run is reported separately. Latency includes all response statuses.

To regenerate the chart, install Matplotlib and run `python3 chart.py` from this directory.

## Bulk requests

A separate test used the same build and resource limits. Of 32 simultaneous requests for 50,000 people, four completed in 6.5–6.9 seconds and 28 received HTTP 503 with a ten-second Retry-After header. A subsequent request for 1,000 people completed successfully. Peak container memory was 1.28 GiB, with no out-of-memory events.

That test checked the country/code pairing and response field counts for all 251,002 profiles it received. There were no mismatches. It also sent 16 simultaneous bulk requests from one identity; one succeeded and 15 received HTTP 429. The two source-data entries named Congo were both treated as valid country/code pairs.

The four-request limit covers bulk generation, JSON serialization, and compression. It does not limit all HTTP connections. The [bulk results](bulk-verification.json) contain individual statuses and timings.

## Reproducing the test

Build the application from the repository root with JDK 22.

```sh
bash mvnw clean package

docker run -d --name peoplegenerator-benchmark \
  --memory=2g --memory-swap=2g --cpus=2 \
  -p 127.0.0.1:8096:8080 \
  -v "$PWD/target/peoplegeneratorapi-1.2.0.jar:/app/app.jar:ro" \
  -w /tmp eclipse-temurin:22-jre \
  java -Xms256m -Xmx1536m -XX:+ExitOnOutOfMemoryError -jar /app/app.jar
```

Wait for the application to start, then run the included load generator. It requires Node.js and no additional packages.

```sh
cd docs/benchmarks
TARGET_PORT=8096 CLIENT_ID_POOL=4096 node parallel.mjs stages.json results.json
node verify.mjs
docker stop peoplegenerator-benchmark
docker rm peoplegenerator-benchmark
```

The scripts write their results to the current directory. Run them in a temporary copy if you want to preserve the published results.

These are local measurements from one run on one machine. Docker networking and the load generator share the host, and higher concurrency does not necessarily increase throughput. The numbers are not a production capacity guarantee.

The tested JAR's SHA-256 was `7f9fb2aa533eb74f199e3fefb83adfd77cd259175f363819c812fd8b203f7bd0`.
