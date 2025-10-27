package com.nika.erp.core.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nika.erp.core.domain.CoreItem;
import com.nika.erp.invoicing.web.form.ItemForm;

public interface CoreItemRepository extends JpaRepository<CoreItem, UUID> {

	List<CoreItem> findByItemNameContainingIgnoreCase(String itemName);

	@Query("""
			    SELECT new com.nika.erp.invoicing.web.form.ItemForm(
			        b.itemName,
			        b.itemCode,
			        b.unitPrice
			    )
			    FROM CoreItem b
			""")
	List<ItemForm> findAllAsForm();
	
	@Query("""
		    SELECT new com.nika.erp.invoicing.web.form.ItemForm(
		        b.itemName,
		        b.itemCode,
		        b.unitPrice
		    )
		    FROM CoreItem b
		    WHERE LOWER(b.itemName) LIKE LOWER(CONCAT('%', :itemName, '%'))
		""")
List<ItemForm> findAllAsFormByItemNameContainingIgnoreCase(@Param("itemName") String itemName);
}
