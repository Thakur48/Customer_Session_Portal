package com.session.dto;

import javax.validation.constraints.NotBlank;
import static com.session.constant.ConstantUsage.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditSessionRequestDto {

	@NotBlank(message = SESSION_NAME_ERR)
	private String sessionName;

	@NotBlank(message = SESSION_REMARK_ERR)
	private String remark;

}
