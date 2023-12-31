package com.session.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.session.model.SessionHist;

@Repository
public interface SessionHistRepository extends JpaRepository<SessionHist, Integer> {

}
