package com.session.dto;

import static com.session.constant.ConstantUsage.CUSTOMER_ID_ERR;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionCustomerDto {

	@NotNull(message = CUSTOMER_ID_ERR)
	private String customerId;

}
