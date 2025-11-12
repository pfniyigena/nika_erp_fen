package com.niwe.erp.core.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.niwe.erp.core.domain.CoreItem;
import com.niwe.erp.invoicing.web.form.ItemForm;

public interface CoreItemRepository extends JpaRepository<CoreItem, UUID> {
	Optional<CoreItem> findByInternalCode(String internalCode);

	List<CoreItem> findByItemNameContainingIgnoreCaseOrItemCodeContainingIgnoreCaseOrBarcodeContainingIgnoreCase(
			String itemName, String itemCode, String barcode);

	@Query("""
			    SELECT new com.niwe.erp.invoicing.web.form.ItemForm(
			        b.itemName,
			        b.internalCode,
			        t.taxCode,
			        t.taxValue,
			        b.unitPrice
			    )
			    FROM CoreItem b INNER JOIN TaxType t ON b.tax.id = t.id
			""")
	List<ItemForm> findAllAsForm();

	@Query("""
			    SELECT new com.niwe.erp.invoicing.web.form.ItemForm(
			        b.itemName,
			        b.internalCode,
			        t.taxCode,
			        t.taxValue,
			        b.unitPrice
			    )
			    FROM CoreItem b
			    JOIN b.tax t
			    WHERE (:itemName IS NULL OR LOWER(b.itemName) LIKE LOWER(CONCAT('%', :itemName, '%')))
			       OR (:itemCode IS NULL OR LOWER(b.itemCode) LIKE LOWER(CONCAT('%', :itemCode, '%')))
			       OR (:internalCode IS NULL OR LOWER(b.internalCode) LIKE LOWER(CONCAT('%', :internalCode, '%')))
			       OR (:barcode IS NULL OR LOWER(b.barcode) LIKE LOWER(CONCAT('%', :barcode, '%')))
			""")
	List<ItemForm> findAllAsFormByItemNameContainingIgnoreCaseOrItemCodeContainingIgnoreCaseOrBarcodeContainingIgnoreCase(
			@Param("itemName") String itemName, @Param("itemCode") String itemCode,
			@Param("internalCode") String internalCode, @Param("barcode") String barcode);

	@Query("""
			SELECT i FROM CoreItem i
			WHERE
			    LOWER(i.itemName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
			    OR LOWER(i.itemCode) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
			    OR LOWER(i.barcode) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
			""")
	Page<CoreItem> searchItems(@Param("searchTerm") String searchTerm, Pageable pageable);
}
