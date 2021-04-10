package com.siss.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import com.siss.api.entities.Regra;

public interface RegraRepository extends JpaRepository<Regra, Integer> {

	@Transactional(readOnly = true)
	Regra findByNome(String nome);
}