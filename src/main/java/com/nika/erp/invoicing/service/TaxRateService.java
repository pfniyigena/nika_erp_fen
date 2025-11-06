package com.nika.erp.invoicing.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.nika.erp.core.domain.EFiscalYear;
import com.nika.erp.invoicing.domain.TaxRate;
import com.nika.erp.invoicing.repository.TaxRateRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class TaxRateService {

	private TaxRateRepository taxRateRepository;

	public List<TaxRate> findAll() {
		return taxRateRepository.findAll();
	}

	public TaxRate save(TaxRate taxRate) {

		if (taxRate.getId() != null) {
			TaxRate exist = taxRateRepository.getReferenceById(taxRate.getId());
			exist.setTaxName(taxRate.getTaxName());
			exist.setCode(taxRate.getCode());
			exist.setTaxValue(taxRate.getTaxValue());
			exist.setDescription(taxRate.getDescription());
			exist.setDisplayName(taxRate.getDisplayName());
			return taxRateRepository.save(exist);
		}

		return taxRateRepository.save(taxRate);

	}

	public TaxRate findById(String id) {

		return taxRateRepository.getReferenceById(UUID.fromString(id));
	}

	public TaxRate findByCode(String code) {

		return taxRateRepository.findByCode(code);
	}

	public void initializeTaxes(EFiscalYear fiscalYear) {

		if (fiscalYear != null && findAll().isEmpty()) {
			switch (fiscalYear) {
			case RWANDA: {

				taxRateRepository.saveAll(List.of(
						TaxRate.builder().taxName("A").displayName("A-EX").code("A").taxValue(new BigDecimal("0.00"))
								.build(),
						TaxRate.builder().taxName("B").displayName("B").code("B").taxValue(new BigDecimal("18.00"))
								.build(),
						TaxRate.builder().taxName("C").displayName("C").code("C").taxValue(new BigDecimal("0.00"))
								.build(),
						TaxRate.builder().taxName("D").displayName("D").code("D").taxValue(new BigDecimal("0.00"))
								.build()));

				break;
			}
			case CHAD: {
				taxRateRepository.saveAll(List.of(
						TaxRate.builder().taxName("EX").displayName("Exonere").code("EX")
								.taxValue(new BigDecimal("0.00")).build(),
						TaxRate.builder().taxName("TN").displayName("Taux Normal").code("TN")
								.taxValue(new BigDecimal("19.25")).build(),
						TaxRate.builder().taxName("TR").displayName("Taux Reduit").code("TR")
								.taxValue(new BigDecimal("9.9")).build(),
						TaxRate.builder().taxName("XP").displayName("Exportation").code("XP")
								.taxValue(new BigDecimal("0.00")).build(),
						TaxRate.builder().taxName("NA").displayName("Non assugetti").code("NA")
								.taxValue(new BigDecimal("0.00")).build()));

				break;
			}
			case GENERAL: {
				taxRateRepository.saveAll(List.of(
						TaxRate.builder().taxName("A").displayName("A-EX").code("A").taxValue(new BigDecimal("0.00"))
								.build(),
						TaxRate.builder().taxName("VAT").displayName("VAT").code("VAT")
								.taxValue(new BigDecimal("18.00")).build()));

				break;
			}
			default:
				throw new IllegalArgumentException("Unexpected value: " + fiscalYear);
			}

		}

	}

}
