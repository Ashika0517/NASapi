# NASA Space Explorer

A Java Spring Boot application that pulls public NASA API data for:
- Astronomy Picture of the Day (APOD)
- Mars rover photos
- Near Earth Objects (NEO)

## Features
- REST API endpoints to fetch NASA data
- Lightweight frontend dashboard at `/`
- Configurable NASA API key via environment variable or `application.properties`

## Prerequisites
- Java 17+
- Maven 3.9+

## Run locally
1. Set your NASA API key:
   ```bash
   export NASA_API_KEY=your_api_key_here
   ```
2. Start the app:
   ```bash
   mvn spring-boot:run
   ```
3. Open:
   - http://localhost:8080/
   - http://localhost:8080/api/space/apod
   - http://localhost:8080/api/space/mars-rover?sol=1000&camera=fhaz
   - http://localhost:8080/api/space/neo?start_date=2024-01-01&end_date=2024-01-07

## Default API key
If you do not provide `NASA_API_KEY`, the app uses the public NASA demo key, which has limited usage.

## Project structure
- `src/main/java/com/ashika/nasapi` - Spring Boot application code
- `src/main/resources/static` - frontend UI
