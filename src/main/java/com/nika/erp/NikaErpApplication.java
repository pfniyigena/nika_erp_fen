package com.nika.erp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.nika.erp.core.service.CoreTaxpayerService;

@SpringBootApplication
public class NikaErpApplication {

	public static void main(String[] args) {
		SpringApplication.run(NikaErpApplication.class, args);
	}

	@Bean
	CommandLineRunner initData(CustomerRepository customerRepository, CoreTaxpayerService coreTaxpayerService) {
		return _ -> {

			coreTaxpayerService.initTaxpayer();
			customerRepository.save(new Customer(null, "John Doe", "john@example.com", "123 Main St"));
			customerRepository.save(new Customer(null, "Acme Corp", "acme@example.com", "456 Business Rd"));
		};
	}

}
