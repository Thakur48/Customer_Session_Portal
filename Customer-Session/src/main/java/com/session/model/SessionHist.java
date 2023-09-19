package com.session.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SessionHist {

	@Id
	private String sessionId;

	private String sessionName;

	private String remark;

	private String createdBy;

	private LocalDateTime createdOn;

	private LocalDateTime updatedOn;

	private char status;

	private String customerId;

	private String customerName;

}
