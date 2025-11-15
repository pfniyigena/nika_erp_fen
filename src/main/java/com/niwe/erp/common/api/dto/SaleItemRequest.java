package com.niwe.erp.common.api.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SaleItemRequest(@JsonProperty("item_code") String itemCode, @JsonProperty("tax_code") String taxCode,
		@JsonProperty("tax_rate") BigDecimal taxRate, @JsonProperty("item_sequence") int itemSequence,
		@JsonProperty("unitPrice") BigDecimal unitPrice, @JsonProperty("quantity") BigDecimal quantity) {

}
