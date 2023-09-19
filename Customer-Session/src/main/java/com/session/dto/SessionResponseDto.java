package com.session.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionResponseDto {

	private String sessionId;

	private String sessionName;

	private String remark;

	private String createdBy;

	private LocalDateTime createdOn;

	private LocalDateTime updatedOn;

	private char status;

	private String customerId;

	private String customerName;

	private String archiveFlag;

}
