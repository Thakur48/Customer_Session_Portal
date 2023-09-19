package com.session.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.session.dto.CustomerRequestDto;
import com.session.dto.CustomerResponseDto;
import com.session.exception.CustomerNotFoundExcpetion;
import com.session.service.CustomerServiceImpl;

@ExtendWith(MockitoExtension.class)

class CustomerControllerTest {

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@InjectMocks
	CustomerController customerController;

	@Mock
	CustomerServiceImpl customerService;

	/**
	 * Test case to verify successful creation of a customer.
	 */

	@Test
	void testCreateCustomer_Success() {

		CustomerRequestDto requestDto = new CustomerRequestDto();
		requestDto.setCustomerName("Thakur");

		CustomerResponseDto responseDto = new CustomerResponseDto("1", "Thakur");

		when(customerService.createCustomer(requestDto)).thenReturn(responseDto);

		ResponseEntity<CustomerResponseDto> responseEntity = customerController.createCustomer(requestDto);

		assertNotNull(responseEntity);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(responseDto, responseEntity.getBody());
	}

	/**
	 * Test case to verify successful retrieval of a customer by ID.
	 *
	 * @throws CustomerNotFoundExcpetion If the customer is not found.
	 */

	@Test
	void testGetCustomerById_Success() throws CustomerNotFoundExcpetion {

		String customerId = "123";
		CustomerResponseDto responseDto = new CustomerResponseDto("123", "Thakur");

		when(customerService.getCustomerById(customerId)).thenReturn(responseDto);

		ResponseEntity<CustomerResponseDto> responseEntity = customerController.getCustomerById(customerId);

		assertNotNull(responseEntity);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(responseDto, responseEntity.getBody());
	}

	/**
	 * Test case to verify handling of customer not found when retrieving a customer
	 * by ID.
	 *
	 * @throws CustomerNotFoundExcpetion If the customer is not found.
	 */

	@Test
	void testGetCustomerById_CustomerNotFound() throws CustomerNotFoundExcpetion {
		String customerId = "456";

		when(customerService.getCustomerById(customerId)).thenThrow(CustomerNotFoundExcpetion.class);

		assertThrows(CustomerNotFoundExcpetion.class, () -> customerController.getCustomerById(customerId));

	}
}
