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
		};
	}

}
