package com.niwe.erp.sale.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import org.springframework.stereotype.Service;

import com.niwe.erp.sale.domain.DailySalesSummary;
import com.niwe.erp.sale.domain.DailySalesSummaryPayment;
import com.niwe.erp.sale.domain.Sale;
import com.niwe.erp.sale.repository.DailySalesSummaryPaymentRepository;
import com.niwe.erp.sale.repository.DailySalesSummaryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DailySalesSummaryService {

	private final DailySalesSummaryRepository dailySalesSummaryRepository;
	private final DailySalesSummaryPaymentRepository dailySalesSummaryPaymentRepository;
	

	public DailySalesSummary save(Sale sale) {

		LocalDate saleDate = LocalDate.ofInstant(sale.getSaleDate(), ZoneId.systemDefault());

		DailySalesSummary summary = dailySalesSummaryRepository.findBySummaryDate(saleDate).orElseGet(() -> {
			DailySalesSummary s = new DailySalesSummary();
			s.setSummaryDate(saleDate);
			return dailySalesSummaryRepository.save(s);
		});

		// 3. Get or create payment-method daily summary
		DailySalesSummaryPayment paymentSummary = dailySalesSummaryPaymentRepository
				.findBySummaryIdAndPaymentMethodId(summary.getId(), sale.getPaymentMethod().getId()).orElseGet(() -> {
					DailySalesSummaryPayment p = new DailySalesSummaryPayment();
					p.setSummary(summary);
					p.setPaymentMethod(sale.getPaymentMethod());
					return dailySalesSummaryPaymentRepository.save(p);
				});

		// 4. Update daily summary totals
		summary.setTotalSalesTaxesInclusive(summary.getTotalSalesTaxesInclusive().add(sale.getTotalAmount()));
		summary.setNumberOfReceipts(summary.getNumberOfReceipts() + 1);
		dailySalesSummaryRepository.save(summary);

		// 5. Update payment method totals
		paymentSummary.setTotalAmount(paymentSummary.getTotalAmount().add(sale.getTotalAmount()));
		paymentSummary.setNumberOfTransactions(paymentSummary.getNumberOfTransactions() + 1);

		dailySalesSummaryPaymentRepository.save(paymentSummary);
		return summary;
	}

	public List<DailySalesSummary> findAll() {
		return dailySalesSummaryRepository.findAll();
	}

}
