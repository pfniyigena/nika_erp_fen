package com.niwe.erp.common.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NiweHeaderRequest(@JsonProperty("tin_number") String tinNumber,
		@JsonProperty("shelf_code") String shelfCode) {

}
