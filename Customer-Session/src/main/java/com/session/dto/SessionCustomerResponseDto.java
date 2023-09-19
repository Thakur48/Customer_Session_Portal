package com.session.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionCustomerResponseDto {
	
	private String custometId;
	
	private String customerName;

}
