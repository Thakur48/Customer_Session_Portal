package com.session.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.session.constant.ConstantUsage;
import com.session.dto.CustomerRequestDto;
import com.session.dto.CustomerResponseDto;
import com.session.exception.CustomerNameInvalidException;
import com.session.exception.CustomerNotFoundExcpetion;
import com.session.mapper.Mapper;
import com.session.model.Customer;
import com.session.repository.CustomerRepository;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@InjectMocks
	CustomerServiceImpl customerService;

	@Mock
	Customer customer;

	@Mock
	Mapper mapper;

	@Mock
	CustomerRepository customerRepository;

	@Mock
	ConstantUsage constantUsage;

	/**
	 * Test case to verify successful creation of a customer.
	 */

	@Test
	void testCreateCustomer_Success() {

		CustomerRequestDto customerRequestDto = new CustomerRequestDto("Thakur");

		Customer customer = new Customer("1", "Thakur");
		Customer savedCustomer = new Customer("1", "Thakur");
		CustomerResponseDto expectedResponseDto = new CustomerResponseDto("1", "Thakur");

		when(mapper.convertCustomerRequestToEntity(customerRequestDto)).thenReturn(customer);
		when(customerRepository.save(customer)).thenReturn(savedCustomer);
		when(mapper.convertCustomerEntityToResponse(savedCustomer)).thenReturn(expectedResponseDto);

		CustomerResponseDto result = customerService.createCustomer(customerRequestDto);

		assertEquals(expectedResponseDto, result);
		verify(mapper, times(1)).convertCustomerRequestToEntity(customerRequestDto);
		verify(customerRepository, times(1)).save(customer);
		verify(mapper, times(1)).convertCustomerEntityToResponse(savedCustomer);

	}

	/**
	 * Test case to verify handling of invalid customer name during creation.
	 *
	 * @throws CustomerNameInvalidException If the customer name is invalid.
	 */

	@Test
	void testCreateCustomer_InvalidName() throws CustomerNameInvalidException {
		CustomerRequestDto requestDto = new CustomerRequestDto(" ");
		// requestDto.setCustomerName(" ");

		assertThrows(CustomerNameInvalidException.class, () -> customerService.createCustomer(requestDto));

	}

	/**
	 * Test case to verify successful retrieval of a customer by ID.
	 *
	 * @throws CustomerNotFoundExcpetion If the customer is not found.
	 */

	@Test
	void testGetCustomerById_Success() throws CustomerNotFoundExcpetion {
		String customerId = "1";
		Customer customer = new Customer(customerId, "Thakur");
		CustomerResponseDto expectedResponseDto = new CustomerResponseDto("1", "Thakur");

		when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
		when(mapper.convertCustomerEntityToResponse(customer)).thenReturn(expectedResponseDto);

		CustomerResponseDto result = customerService.getCustomerById(customerId);

		assertEquals(expectedResponseDto, result);
		verify(customerRepository, times(1)).findById(customerId);
		verify(mapper, times(1)).convertCustomerEntityToResponse(customer);
	}

	/**
	 * Test case to verify handling of customer not found during retrieval by ID.
	 *
	 * @throws CustomerNotFoundExcpetion If the customer is not found.
	 */
	@Test
	void testGetCustomerById_CustomerNotFound() throws CustomerNotFoundExcpetion {
		String customerId = "456";

		when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

		assertThrows(CustomerNotFoundExcpetion.class, () -> customerService.getCustomerById(customerId));

	}

}
