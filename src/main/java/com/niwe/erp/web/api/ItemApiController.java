package com.niwe.erp.web.api;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.niwe.erp.common.api.dto.NiweCommonResponse;
import com.niwe.erp.common.api.dto.NiweCommonRequest;
import com.niwe.erp.core.service.CoreItemService;
import com.niwe.erp.sale.domain.Shelf;
import com.niwe.erp.sale.service.ShelfService;
import com.niwe.erp.core.dto.CoreItemListDTO;
import com.niwe.erp.web.util.NiweErpApiUrlConstants;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = NiweErpApiUrlConstants.NIWE_API + NiweErpApiUrlConstants.API_ITEMS_URL)
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class ItemApiController {
	private final CoreItemService coreItemService;
	private final ShelfService shelfService;

	@PostMapping(headers =NiweErpApiUrlConstants.NIWE_API_V1)
	public ResponseEntity<NiweCommonResponse> getItems(@RequestBody NiweCommonRequest niweCommonRequest) {
		log.info("Get Items from request: {}", niweCommonRequest);

		Shelf shelf = shelfService.findByInternalCode(niweCommonRequest.shelfCode());

		Sort sort = Sort.by("modifiedAt").ascending();

		Pageable pageable = PageRequest.of(niweCommonRequest.pageSize(), niweCommonRequest.recordSize(), sort);
		Page<CoreItemListDTO> page = coreItemService.findAllAsDto(pageable);
		List<CoreItemListDTO> list = page.getContent();
		shelf.setLastSyn(LocalDateTime.now());
		shelfService.save(shelf);
		NiweCommonResponse niweCommonResponse = new NiweCommonResponse(list, "000", "SUCCESS", page.getTotalPages());
		log.info("Get Items from response: {}", niweCommonResponse);
		return ResponseEntity.ok(niweCommonResponse);
	}

	@PostMapping(value="/updates",headers =NiweErpApiUrlConstants.NIWE_API_V1)
	public ResponseEntity<NiweCommonResponse> getUpdatedItems(@RequestBody NiweCommonRequest niweCommonRequest) {
		log.info("Get product Items from: {}", niweCommonRequest);
		Sort sort = Sort.by("lastUpdated").ascending();
		Shelf shelf = shelfService.findByInternalCode(niweCommonRequest.shelfCode());
		Pageable pageable = PageRequest.of(niweCommonRequest.pageSize(), niweCommonRequest.recordSize(), sort);
		Page<CoreItemListDTO> page = coreItemService.findByLastUpdatedAfter(shelf.getLastSyn(),  pageable);
		List<CoreItemListDTO> list = page.getContent(); 
		shelf.setLastSyn(LocalDateTime.now());
		shelfService.save(shelf);
		NiweCommonResponse niweCommonResponse = new NiweCommonResponse(list, "000", "SUCCESS", page.getTotalPages());
		log.info("Get Items from response: {}", niweCommonResponse);
		return ResponseEntity.ok(niweCommonResponse);
	}
}
