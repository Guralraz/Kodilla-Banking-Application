package com.greg.banking_app.dto.NBPClient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NBPCurrencyDto {

    @JsonProperty("code")
    private String currencyCode;

    @JsonProperty("bid")
    private BigDecimal buyRate;

    @JsonProperty("ask")
    private BigDecimal sellRate;
}
