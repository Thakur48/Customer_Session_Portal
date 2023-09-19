package com.session.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.session.model.Session;

@Repository
public interface SessionRepository extends JpaRepository<Session, String> {

	Page<Session> findByStatus(char status, PageRequest pagable);
	long countByStatus(char status);
	Session findBySessionId(String sessionId);

}
