package com.session.model;

import static com.session.constant.ConstantUsage.COLUMN_CREATEDBY;
import static com.session.constant.ConstantUsage.COLUMN_CREATEDON;
import static com.session.constant.ConstantUsage.COLUMN_CUSTOMER_SESSION;
import static com.session.constant.ConstantUsage.COLUMN_REMARK;
import static com.session.constant.ConstantUsage.COLUMN_SESSIONNAME;
import static com.session.constant.ConstantUsage.COLUMN_STATUS;
import static com.session.constant.ConstantUsage.COLUMN_UPDATEDON;
import static com.session.constant.ConstantUsage.SESSION_ID;
import static com.session.constant.ConstantUsage.SESSION_STRATEGY;
import static com.session.constant.ConstantUsage.STATUS_ACTIVE;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
public class Session {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SESSION_ID)
	@GenericGenerator(name = SESSION_ID, strategy = SESSION_STRATEGY)
	@Column(updatable = false, nullable = false)
	private String sessionId;

	@Column(name = COLUMN_SESSIONNAME)
	private String sessionName;

	@Column(name = COLUMN_REMARK)
	private String remark;

	@CreatedBy
	@Column(name = COLUMN_CREATEDBY)
	private String createdBy;

	@CreationTimestamp
	@Column(nullable = false, updatable = false, name = COLUMN_CREATEDON)
	private LocalDateTime createdOn;

	@UpdateTimestamp
	@Column(nullable = false, name = COLUMN_UPDATEDON)
	private LocalDateTime updatedOn;

	@Column(name = COLUMN_STATUS)
	private char status;

	@ManyToOne()
	@JoinColumn(name = COLUMN_CUSTOMER_SESSION)
	private Customer customer;

	@PrePersist
	public void values() {
		status = STATUS_ACTIVE;
		createdOn = LocalDateTime.now();
		updatedOn = LocalDateTime.now();

	}

}
