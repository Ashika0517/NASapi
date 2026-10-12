package com.ashika.nasapi.service;

import com.ashika.nasapi.config.NasaApiProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class NasaService {

    private final RestClient restClient;
    private final NasaApiProperties properties;

    public NasaService(NasaApiProperties properties) {
        this.properties = properties;
        this.restClient = RestClient.builder()
                .baseUrl(properties.baseUrl())
                .defaultHeader("Accept", "application/json")
                .build();
    }

    public Map<String, Object> getApod(String date) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("api_key", properties.key());
        if (date != null && !date.isBlank()) {
            params.put("date", date);
        }
        return fetchFromNasa("/planetary/apod", params);
    }

    public Map<String, Object> getMarsRover(String sol, String camera) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("api_key", properties.key());
        if (sol != null && !sol.isBlank()) {
            params.put("sol", sol);
        }
        if (camera != null && !camera.isBlank()) {
            params.put("camera", camera);
        }
        return fetchFromNasa("/mars-photos/api/v1/rovers/curiosity/photos", params);
    }

    public Map<String, Object> getNeoFeed(String startDate, String endDate) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("api_key", properties.key());
        if (startDate != null && !startDate.isBlank()) {
            params.put("start_date", startDate);
        }
        if (endDate != null && !endDate.isBlank()) {
            params.put("end_date", endDate);
        }
        return fetchFromNasa("/neo/rest/v1/feed", params);
    }

    private Map<String, Object> fetchFromNasa(String endpoint, Map<String, String> params) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(properties.baseUrl())
                .path(endpoint);

        params.forEach((key, value) -> {
            if (value != null && !value.isBlank()) {
                builder.queryParam(key, value);
            }
        });

        String uri = builder.build().toUriString();

        try {
            return restClient.get()
                    .uri(uri)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });
        } catch (HttpClientErrorException ex) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", true);
            error.put("status", ex.getStatusCode().value());
            error.put("message", ex.getResponseBodyAsString() != null ? ex.getResponseBodyAsString() : "NASA API error");
            return error;
        } catch (Exception ex) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", true);
            error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            error.put("message", ex.getMessage());
            return error;
        }
    }
}
