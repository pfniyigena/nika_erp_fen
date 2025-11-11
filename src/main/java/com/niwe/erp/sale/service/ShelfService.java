package com.niwe.erp.sale.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.niwe.erp.common.exception.ResourceNotFoundException;
import com.niwe.erp.common.service.SequenceNumberService;
import com.niwe.erp.sale.domain.Shelf;
import com.niwe.erp.sale.repository.ShelfRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShelfService {
	private final ShelfRepository shelfRepository;
	private final SequenceNumberService sequenceNumberService;

	public List<Shelf> findAll() {

		return shelfRepository.findAll();
	}

	public Shelf save(Shelf shelf) {
		Shelf savedShelf = null;
		String code = shelf.getInternalCode();
		if (code == null || code.isEmpty()) {
			code = sequenceNumberService.getNextShelfCode();
		}

		if (shelf.getId() != null) {

			Shelf exist = shelfRepository.getReferenceById(shelf.getId());
			exist.setInternalCode(code);
			exist.setName(shelf.getName());
			exist.setWarehouse(shelf.getWarehouse());

			savedShelf = shelfRepository.save(exist);
		} else {
			shelf.setInternalCode(code);
			savedShelf = shelfRepository.save(shelf);
		}

		return savedShelf;

	}

	public Shelf findById(String id) {

		return shelfRepository.findById(UUID.fromString(id))
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));

	}

}
