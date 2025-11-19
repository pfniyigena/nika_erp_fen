package com.niwe.erp.sale.service;

import java.util.List;

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

	public List<Customer> findAll() {

		return customerRepository.findAll();
	}

}
