package com.session.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.session.dto.CustomerRequestDto;
import com.session.dto.CustomerResponseDto;
import com.session.dto.SessionCustomerResponseDto;
import com.session.dto.SessionRequestDto;
import com.session.dto.SessionResponseDto;
import com.session.model.Customer;
import com.session.model.Session;
import com.session.model.SessionHist;

@Component
public class Mapper {

	@Autowired
	ModelMapper modelMapper;

	public SessionRequestDto convertEntityToRequestDto(Session session) {
		return modelMapper.map(session, SessionRequestDto.class);

	}

	public SessionResponseDto convertRequestDtoToResponseDto(SessionRequestDto sessionRequestDto) {
		return modelMapper.map(sessionRequestDto, SessionResponseDto.class);

	}

	public Session convertRequestDtoToEntity(SessionRequestDto sessionRequestDto) {
		return modelMapper.map(sessionRequestDto, Session.class);

	}

	public SessionResponseDto convertEntityToResponseDto(Session session) {
		return modelMapper.map(session, SessionResponseDto.class);

	}

	public SessionHist convertSessionToSessionHist(Session session) {
		return modelMapper.map(session, SessionHist.class);
	}

	public SessionResponseDto convertSessionHistToSessionResponseDto(SessionHist sessionHist) {
		return modelMapper.map(sessionHist, SessionResponseDto.class);
	}

	public Customer convertCustomerRequestToEntity(CustomerRequestDto customerRequestDto) {
		return modelMapper.map(customerRequestDto, Customer.class);

	}

	public CustomerResponseDto convertCustomerEntityToResponse(Customer customer) {
		return modelMapper.map(customer, CustomerResponseDto.class);

	}

	public SessionCustomerResponseDto convertCustomerEntityToSessionCustomerResponseDto(Customer customer) {
		return modelMapper.map(customer, SessionCustomerResponseDto.class);

	}


}
