package com.session.dto;

import static com.session.constant.ConstantUsage.CUSTOMER_ID_ERR;
import static com.session.constant.ConstantUsage.SESSION_NAME_ERR;
import static com.session.constant.ConstantUsage.SESSION_REMARK_ERR;
import static com.session.constant.ConstantUsage.SESSION_UPDATEDBY_ERR;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionRequestDto {

	@NotBlank(message = SESSION_NAME_ERR)
	private String sessionName;

	@NotBlank(message = SESSION_REMARK_ERR)
	private String remark;

	@NotBlank(message = SESSION_UPDATEDBY_ERR)
	private String createdBy;

	@NotNull(message = CUSTOMER_ID_ERR)
	private String customerId;

}
