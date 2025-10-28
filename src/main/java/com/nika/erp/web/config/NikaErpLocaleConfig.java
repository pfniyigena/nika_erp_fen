package com.nika.erp.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import com.nika.erp.core.converter.StringToItemClassificationConverter;
import com.nika.erp.core.converter.StringToItemCountryConverter;
import com.nika.erp.core.service.CoreCountryService;
import com.nika.erp.core.service.CoreItemClassificationService;

import lombok.AllArgsConstructor;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Locale;

@Configuration
@AllArgsConstructor
public class NikaErpLocaleConfig implements WebMvcConfigurer {
	private final CoreItemClassificationService coreItemClassificationService;
	private final CoreCountryService coreCountryService;

	@Bean
	public LocaleResolver localeResolver() {
		CookieLocaleResolver resolver = new CookieLocaleResolver();
		resolver.setDefaultLocale(Locale.ENGLISH);
		return resolver;
	}

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
		interceptor.setParamName("lang");
		return interceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
	}

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(new StringToItemCountryConverter(coreCountryService));
		registry.addConverter(new StringToItemClassificationConverter(coreItemClassificationService));
	}
}
