package com.nika.erp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.nika.erp.core.domain.CoreTaxpayer;
import com.nika.erp.core.repository.CoreRoleRepository;
import com.nika.erp.core.service.CoreItemService;
import com.nika.erp.core.service.CoreTaxpayerService;
import com.nika.erp.core.service.CoreUserService;

@SpringBootApplication
@EnableJpaAuditing
public class NikaErpApplication {

	public static void main(String[] args) {
		SpringApplication.run(NikaErpApplication.class, args);
	}

	@Bean
	CommandLineRunner initData(CoreTaxpayerService coreTaxpayerService, CoreItemService coreItemService,
			CoreUserService coreUserService, CoreRoleRepository coreRoleRepository) {
		return vars -> {

			CoreTaxpayer coreTaxpayer=	coreTaxpayerService.initTaxpayer();
			coreUserService.initUser(coreTaxpayer);
			// coreItemService.initItems();
		};
	}

}
