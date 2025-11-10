package com.nika.erp.inventory.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.nika.erp.core.domain.CoreItem;
import com.nika.erp.core.domain.CoreTaxpayerBranch;
import com.nika.erp.core.repository.CoreItemRepository;
import com.nika.erp.inventory.domain.EStockOperation;
import com.nika.erp.inventory.domain.MovementType;
import com.nika.erp.inventory.domain.Shelf;
import com.nika.erp.inventory.domain.ShelfSale;
import com.nika.erp.inventory.domain.Warehouse;
import com.nika.erp.inventory.repository.ShelfRepository;
import com.nika.erp.inventory.repository.ShelfSaleRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class ShelfSaleService {

	private final CoreItemRepository coreItemRepository;
	private final ShelfRepository shelfRepository;
	private final WarehouseStockService warehouseStockService;
	private final ShelfSaleRepository shelfSaleRepository;

	public Boolean createShelfSale(String shelfId, String itemId, BigDecimal quantity, BigDecimal totalPrice) {
		Shelf shelf = shelfRepository.getReferenceById(UUID.fromString(shelfId));
		CoreTaxpayerBranch branch = shelf.getBranch();
		CoreItem item = coreItemRepository.getReferenceById(UUID.fromString(itemId));
		Warehouse branchWarehouse = shelf.getWarehouse();
		warehouseStockService.updateProductQuantity(branchWarehouse, item, quantity, MovementType.SALE, "SEL",
				EStockOperation.OUT);
		/*
		 * if (!stockDeducted) { return false; }
		 */
		ShelfSale sale = new ShelfSale();
		sale.setBranch(branch);
		sale.setItem(item);
		sale.setQuantity(quantity);
		sale.setTotalPrice(totalPrice);
		sale.setSaleDate(LocalDateTime.now());
		shelfSaleRepository.save(sale);
		return true;
	}

}
