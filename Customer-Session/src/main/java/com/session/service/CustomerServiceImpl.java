package com.session.service;

import static com.session.constant.ConstantUsage.CUSTOMER_NAME_INVALID_EXCEPTION;
import static com.session.constant.ConstantUsage.CUSTOMER_NOT_FOUND;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.session.constant.ConstantUsage;
import com.session.dto.CustomerRequestDto;
import com.session.dto.CustomerResponseDto;
import com.session.exception.CustomerNameInvalidException;
import com.session.exception.CustomerNotFoundExcpetion;
import com.session.mapper.Mapper;
import com.session.model.Customer;
import com.session.repository.CustomerRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomerServiceImpl implements ICustomerService {

	@Autowired
	Mapper mapper;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	ConstantUsage constant;

	/**
	 * Creates a new customer based on the provided customer request data.
	 *
	 * @param customerRequestDto The DTO containing the customer data to be created.
	 * @return A response DTO containing information about the created customer.
	 * @throws CustomerNameInvalidException If the customer name provided is invalid
	 *                                      or empty.
	 */

	@Override
	public CustomerResponseDto createCustomer(CustomerRequestDto customerRequestDto)
			throws CustomerNameInvalidException {

		if (!(customerRequestDto.getCustomerName().matches("[A-Z][a-z]+( [A-Z][a-z]+)?"))) {
			throw new CustomerNameInvalidException(CUSTOMER_NAME_INVALID_EXCEPTION);
		}

		Customer customer = mapper.convertCustomerRequestToEntity(customerRequestDto);

		Customer savedCustomer = customerRepository.save(customer);

		log.info("savedSession" + savedCustomer);

		return mapper.convertCustomerEntityToResponse(savedCustomer);

	}

	/**
	 * Retrieves a customer based on the provided customer ID.
	 *
	 * @param customerId The ID of the customer to retrieve.
	 * @return A response DTO containing information about the retrieved customer.
	 * @throws CustomerNotFoundExcpetion If a customer with the provided ID is not
	 *                                   found.
	 */

	@Override
	public CustomerResponseDto getCustomerById(String customerId) throws CustomerNotFoundExcpetion {

		Optional<Customer> customer = customerRepository.findById(customerId);

		if (customer.isEmpty()) {

			throw new CustomerNotFoundExcpetion(CUSTOMER_NOT_FOUND);

		} else {

			return mapper.convertCustomerEntityToResponse(customer.get());

		}

	}

}
