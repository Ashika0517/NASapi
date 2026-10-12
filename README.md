# NASA Space Explorer

A Java Spring Boot project that fetches public NASA data and displays it in a simple browser-based dashboard.

## Features
- Astronomy Picture of the Day (APOD)
- Mars rover photos
- Near Earth Objects (NEO) feed
- Spring Boot REST API backend
- Simple static frontend UI

## Tech stack
- Java 17
- Spring Boot 3.3
- Maven

## Run locally
1. Ensure Java 17+ and Maven are installed.
2. Set your NASA API key:
   ```bash
   export NASA_API_KEY=your_real_api_key_here
   ```
3. Start the app:
   ```bash
   mvn spring-boot:run
   ```
4. Open the app in a browser:
   - http://localhost:8080/

## Available API endpoints
- GET http://localhost:8080/api/space/health
- GET http://localhost:8080/api/space/apod
- GET http://localhost:8080/api/space/apod?date=2024-01-01
- GET http://localhost:8080/api/space/mars-rover?sol=1000&camera=fhaz
- GET http://localhost:8080/api/space/neo?start_date=2024-01-01&end_date=2024-01-07

## Notes
- If `NASA_API_KEY` is not provided, the app uses the default demo key, but limits may apply.
- For production use, create your own API key from https://api.nasa.gov/.
