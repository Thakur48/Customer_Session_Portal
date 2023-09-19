package com.session.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.session.dto.CustomerRequestDto;
import com.session.dto.CustomerResponseDto;
import com.session.exception.CustomerNameInvalidException;
import com.session.exception.CustomerNotFoundExcpetion;

@Service
@Validated
public interface ICustomerService {

	CustomerResponseDto createCustomer(CustomerRequestDto customerRequestDto) throws CustomerNameInvalidException;

	CustomerResponseDto getCustomerById(String id) throws CustomerNotFoundExcpetion;
	
	 


}
