package com.niwe.erp.web.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.niwe.erp.common.api.dto.NiweCommonResponse;
import com.niwe.erp.common.api.dto.SaleRequest;
import com.niwe.erp.sale.service.ShelfService;
import com.niwe.erp.web.util.NiweErpApiUrlConstants;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = NiweErpApiUrlConstants.NIWE_API_V1 + NiweErpApiUrlConstants.API_SALES_URL)
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class SaleApiController {
	private final ShelfService shelfService;

	@PostMapping
	public ResponseEntity<NiweCommonResponse> receiveSale(@RequestBody SaleRequest request) {
		log.info("ReceiveSale from request: {}", request);
		shelfService.receiveSaleFromExternalShelf(request);
		NiweCommonResponse niweCommonResponse = new NiweCommonResponse(null, "000", "SUCCESS",0);
		log.info("ReceiveSale from response: {}", niweCommonResponse);
		return ResponseEntity.ok(niweCommonResponse);
	}
}
