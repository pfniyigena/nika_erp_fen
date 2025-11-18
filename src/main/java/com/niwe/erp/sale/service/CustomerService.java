package com.niwe.erp.sale.service;

import org.springframework.stereotype.Service;

import com.niwe.erp.sale.domain.Customer;
import com.niwe.erp.sale.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService {

	private final CustomerRepository customerRepository;

	public Customer save(Customer customer) {

		return customerRepository.findByCustomerTin(customer.getCustomerTin()).orElseGet(() -> {
					return customerRepository.save(customer);
				});
	}

}
