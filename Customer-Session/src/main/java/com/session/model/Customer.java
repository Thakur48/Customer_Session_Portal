package com.session.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.session.constant.ConstantUsage.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Customer {

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = CUSTOMER_ID)
	@GenericGenerator(name = CUSTOMER_ID, strategy = CUSTOMER_STRATEGY)
	@Column(updatable = false, nullable = false)
	private String customerId;

	@Column(name = COLUMN_CUSTOMERNAME)
	private String customerName;

	@OneToMany(mappedBy = CUSTOMER_LTRL)
	@Column(name = COLUMN_CUSTOMER_SESSION)
	private List<Session> sessions;

	public Customer(String customerId, String customerName) {
		this.customerId = customerId;
		this.customerName = customerName;
	}

}
