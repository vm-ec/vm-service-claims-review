package com.vm.service.claimsreview.connector;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Map;

@Slf4j
@Component
public class AiMetricsConnector {

    @Value("${aimetrics.url}")
    private String metricsUrl;

    private final WebClient webClient;

    public AiMetricsConnector(WebClient webClient) {
        this.webClient = webClient;
    }

    public String fetchMetrics() {
        return get(metricsUrl, null, null);
    }

    public String get(String uri, Map<String, String> headers, Map<String, String> queryParams) {
        try {
            log.info("GET request to: {}", uri);
            
            WebClient.RequestHeadersSpec<?> spec = webClient.get()
                    .uri(uriBuilder -> {
                        uriBuilder.path(uri);
                        if (queryParams != null) {
                            queryParams.forEach(uriBuilder::queryParam);
                        }
                        return uriBuilder.build();
                    });
            
            if (headers != null) {
                headers.forEach(spec::header);
            }
            
            String response = spec.retrieve()
                    .bodyToMono(String.class)
                    .block();
            
            log.debug("GET request successful");
            return response;
            
        } catch (WebClientResponseException e) {
            log.error("GET request failed. Status: {}, Response: {}", 
                    e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("GET request failed", e);
        } catch (Exception e) {
            log.error("Unexpected error during GET request", e);
            throw new RuntimeException("Unexpected error during GET request", e);
        }
    }

    public String post(String uri, Object body, Map<String, String> headers) {
        try {
            log.info("POST request to: {}", uri);
            
            WebClient.RequestBodySpec spec = webClient.post().uri(uri);
            
            if (headers != null) {
                headers.forEach(spec::header);
            }
            
            String response = spec.bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            
            log.debug("POST request successful");
            return response;
            
        } catch (WebClientResponseException e) {
            log.error("POST request failed. Status: {}, Response: {}", 
                    e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("POST request failed", e);
        } catch (Exception e) {
            log.error("Unexpected error during POST request", e);
            throw new RuntimeException("Unexpected error during POST request", e);
        }
    }

    public String put(String uri, Object body, Map<String, String> headers) {
        try {
            log.info("PUT request to: {}", uri);
            
            WebClient.RequestBodySpec spec = webClient.put().uri(uri);
            
            if (headers != null) {
                headers.forEach(spec::header);
            }
            
            String response = spec.bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            
            log.debug("PUT request successful");
            return response;
            
        } catch (WebClientResponseException e) {
            log.error("PUT request failed. Status: {}, Response: {}", 
                    e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("PUT request failed", e);
        } catch (Exception e) {
            log.error("Unexpected error during PUT request", e);
            throw new RuntimeException("Unexpected error during PUT request", e);
        }
    }

    public String delete(String uri, Map<String, String> headers) {
        try {
            log.info("DELETE request to: {}", uri);
            
            WebClient.RequestHeadersSpec<?> spec = webClient.delete().uri(uri);
            
            if (headers != null) {
                headers.forEach(spec::header);
            }
            
            String response = spec.retrieve()
                    .bodyToMono(String.class)
                    .block();
            
            log.debug("DELETE request successful");
            return response;
            
        } catch (WebClientResponseException e) {
            log.error("DELETE request failed. Status: {}, Response: {}", 
                    e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("DELETE request failed", e);
        } catch (Exception e) {
            log.error("Unexpected error during DELETE request", e);
            throw new RuntimeException("Unexpected error during DELETE request", e);
        }
    }
}
