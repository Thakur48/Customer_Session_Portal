package com.session.controller;

import static com.session.constant.ConstantUsage.CUSTOMER_MAPPING;
import static com.session.constant.ConstantUsage.GET_BY_CUSTOMERID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.session.dto.CustomerRequestDto;
import com.session.dto.CustomerResponseDto;
import com.session.exception.CustomerNameInvalidException;
import com.session.exception.CustomerNotFoundExcpetion;
import com.session.service.ICustomerService;

@RestController
public class CustomerController {

	@Autowired
	ICustomerService customerService;

	/**
	 * Creates a new customer based on the provided customer request data.
	 *
	 * @param customerRequestDto The DTO containing the customer data to be created.
	 * @return A response entity containing the created customer response DTO.
	 * @throws CustomerNameInvalidException If the customer name provided is invalid
	 *                                      or empty.
	 */

	@PostMapping(CUSTOMER_MAPPING)
	public ResponseEntity<CustomerResponseDto> createCustomer(@RequestBody CustomerRequestDto customerRequestDto)
			throws CustomerNameInvalidException {

		CustomerResponseDto customerResponseDto = customerService.createCustomer(customerRequestDto);

		return new ResponseEntity<>(customerResponseDto, HttpStatus.OK);

	}

	/**
	 * Retrieves a customer based on the provided customer ID.
	 *
	 * @param customerId The ID of the customer to retrieve.
	 * @return A response entity containing the retrieved customer response DTO.
	 * @throws CustomerNotFoundExcpetion If a customer with the provided ID is not
	 *                                   found.
	 */

	@GetMapping(GET_BY_CUSTOMERID)
	public ResponseEntity<CustomerResponseDto> getCustomerById(@PathVariable @Valid String customerId)
			throws CustomerNotFoundExcpetion {
		CustomerResponseDto customerResponseDto = customerService.getCustomerById(customerId);

		return new ResponseEntity<>(customerResponseDto, HttpStatus.OK);

	}

}
