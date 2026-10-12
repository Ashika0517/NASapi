package com.ashika.nasapi.service;

import com.ashika.nasapi.config.NasaApiProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

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
        var uri = buildUri("/planetary/apod", Map.of("api_key", properties.key(), "date", date));
        return fetch(uri);
    }

    public Map<String, Object> getMarsRover(String sol, String camera) {
        var uri = buildUri("/mars-photos/api/v1/rovers/curiosity/photos",
                Map.of("api_key", properties.key(), "sol", sol, "camera", camera));
        return fetch(uri);
    }

    public Map<String, Object> getNeoFeed(String startDate, String endDate) {
        var uri = buildUri("/neo/rest/v1/feed",
                Map.of("api_key", properties.key(), "start_date", startDate, "end_date", endDate));
        return fetch(uri);
    }

    private String buildUri(String path, Map<String, String> params) {
        var builder = UriComponentsBuilder.fromPath(path);
        params.forEach((key, value) -> {
            if (value != null && !value.isBlank()) {
                builder.queryParam(key, value);
            }
        });
        return builder.toUriString();
    }

    private Map<String, Object> fetch(String uri) {
        try {
            return restClient.get()
                    .uri(uri)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
        } catch (HttpClientErrorException ex) {
            return Map.of(
                    "error", true,
                    "status", ex.getStatusCode().value(),
                    "message", ex.getResponseBodyAsString()
            );
        } catch (Exception ex) {
            return Map.of(
                    "error", true,
                    "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "message", ex.getMessage()
            );
        }
    }
}
