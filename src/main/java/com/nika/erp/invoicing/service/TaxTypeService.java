package com.nika.erp.invoicing.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.nika.erp.core.domain.EFiscalYear;
import com.nika.erp.invoicing.domain.TaxType;
import com.nika.erp.invoicing.repository.TaxTypeRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class TaxTypeService {

	private TaxTypeRepository taxTypeRepository;

	public List<TaxType> findAll() {
		return taxTypeRepository.findAll();
	}

	public TaxType save(TaxType taxType) {

		if (taxType.getId() != null) {
			TaxType exist = taxTypeRepository.getReferenceById(taxType.getId());
			exist.setTaxName(taxType.getTaxName());
			exist.setTaxCode(taxType.getTaxCode());
			exist.setTaxValue(taxType.getTaxValue());
			exist.setDescription(taxType.getDescription());
			exist.setDisplayName(taxType.getDisplayName());
			return taxTypeRepository.save(exist);
		}

		return taxTypeRepository.save(taxType);

	}

	public TaxType findById(String id) {

		return taxTypeRepository.getReferenceById(UUID.fromString(id));
	}

	public TaxType findByCode(String taxCode) {

		return taxTypeRepository.findByTaxCode(taxCode);
	}

	public void initializeTaxes(EFiscalYear fiscalYear) {

		if (fiscalYear != null && findAll().isEmpty()) {
			switch (fiscalYear) {
			case RWANDA: {

				taxTypeRepository.saveAll(List.of(
						TaxType.builder().taxName("A").displayName("A-EX").taxCode("A").taxValue(new BigDecimal("0.00")).displayLevel(0)
								.build(),
						TaxType.builder().taxName("B").displayName("B").taxCode("B").taxValue(new BigDecimal("18.00")).displayLevel(1)
								.build(),
						TaxType.builder().taxName("C").displayName("C").taxCode("C").taxValue(new BigDecimal("0.00")).displayLevel(2)
								.build(),
						TaxType.builder().taxName("D").displayName("D").taxCode("D").taxValue(new BigDecimal("0.00")).displayLevel(3)
								.build()));

				break;
			}
			case CHAD: {
				taxTypeRepository.saveAll(List.of(
						TaxType.builder().taxName("EX").displayName("Exonere").taxCode("EX")
								.taxValue(new BigDecimal("0.00")).displayLevel(0)  .build(),
						TaxType.builder().taxName("TN").displayName("Taux Normal").taxCode("TN")
								.taxValue(new BigDecimal("19.25")).displayLevel(1) .build(),
						TaxType.builder().taxName("TR").displayName("Taux Reduit").taxCode("TR")
								.taxValue(new BigDecimal("9.9")).displayLevel(2)  .build(),
						TaxType.builder().taxName("XP").displayName("Exportation").taxCode("XP")
								.taxValue(new BigDecimal("0.00")).displayLevel(3) .build(),
						TaxType.builder().taxName("NA").displayName("Non assugetti").taxCode("NA")
								.taxValue(new BigDecimal("0.00")).displayLevel(4) .build()));

				break;
			}
			case GENERAL: {
				taxTypeRepository.saveAll(List.of(
						TaxType.builder().taxName("A").displayName("A-EX").taxCode("A").taxValue(new BigDecimal("0.00")).displayLevel(0)
								.build(),
						TaxType.builder().taxName("VAT").displayName("VAT").taxCode("VAT")
								.taxValue(new BigDecimal("18.00")).displayLevel(1)  .build()));

				break;
			}
			default:
				throw new IllegalArgumentException("Unexpected value: " + fiscalYear);
			}

		}

	}

}
