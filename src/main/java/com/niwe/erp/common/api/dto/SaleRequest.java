package com.niwe.erp.common.api.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SaleRequest(@JsonProperty("niwe_header") NiweHeaderRequest niweHeaderRequest,
		@JsonProperty("external_reference") String externalReference,
		@JsonProperty("transaction_type") String transactionType, @JsonProperty("payment_method") String paymentMethod,
		@JsonProperty("confirmed_by") String confirmedBy, @JsonProperty("sale_date") String saleDate,
		List<SaleItemRequest> items) {

}
