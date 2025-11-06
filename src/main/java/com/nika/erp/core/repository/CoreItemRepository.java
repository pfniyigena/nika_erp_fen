package com.nika.erp.core.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nika.erp.core.domain.CoreItem;
import com.nika.erp.invoicing.web.form.ItemForm;

public interface CoreItemRepository extends JpaRepository<CoreItem, UUID> {
	CoreItem findByInternalCode(String internalCode);

	List<CoreItem> findByItemNameContainingIgnoreCase(String itemName);

	@Query("""
			    SELECT new com.nika.erp.invoicing.web.form.ItemForm(
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
			    SELECT new com.nika.erp.invoicing.web.form.ItemForm(
			        b.itemName,
			        b.internalCode,
			        t.taxCode,
			        t.taxValue,
			        b.unitPrice
			    )
			    FROM CoreItem b INNER JOIN TaxType t ON b.tax.id = t.id
			    WHERE LOWER(b.itemName) LIKE LOWER(CONCAT('%', :itemName, '%'))
			""")
	List<ItemForm> findAllAsFormByItemNameContainingIgnoreCase(@Param("itemName") String itemName);

}
