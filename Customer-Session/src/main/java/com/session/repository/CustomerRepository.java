package com.session.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.session.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {

	//Customer findByCustomerId(String customerId);

}
