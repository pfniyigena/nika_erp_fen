package com.nika.erp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.nika.erp.core.service.CoreItemService;
import com.nika.erp.core.service.CoreTaxpayerService;

@SpringBootApplication
public class NikaErpApplication {
	

	public static void main(String[] args) {
		SpringApplication.run(NikaErpApplication.class, args);
	}

	@Bean
	CommandLineRunner initData(CoreTaxpayerService coreTaxpayerService,CoreItemService coreItemService) {
		return vars -> {

			coreTaxpayerService.initTaxpayer();
			coreItemService.initItems();
		};
	}

}
