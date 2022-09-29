package com.greg.banking_app.client;

import com.greg.banking_app.dto.NBPClient.NBPTableCDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class NBPClient {

    private final RestTemplate restTemplate;
    private static final Logger LOGGER = LoggerFactory.getLogger(NBPClient.class);

    @Value("${nbp.api.exchange.endpoint}")
    private String NBPApiEndpoint;

    public List<NBPTableCDto> getNBPTableC() {
        LOGGER.info("I am starting to download data from the NBP...");
        NBPTableCDto[] tableResponse = restTemplate.getForObject(
                NBPApiEndpoint + "/tables/c", NBPTableCDto[].class);

        try {
            LOGGER.info("Data from the NBP have been downloaded");
            return Optional.ofNullable(tableResponse)
                    .map(Arrays::asList)
                    .orElse(Collections.emptyList());
        } catch (RestClientException e) {
            LOGGER.error("I ran into a problem..." + e.getMessage(), e);
            return Collections.emptyList();
        }
    }
}
