package com.niwe.erp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.niwe.erp.core.domain.CoreTaxpayer;
import com.niwe.erp.core.repository.CoreRoleRepository;
import com.niwe.erp.core.service.CoreItemService;
import com.niwe.erp.core.service.CoreTaxpayerService;
import com.niwe.erp.core.service.CoreUserService;

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
