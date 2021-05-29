package com.siss.api.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import com.siss.api.entities.Contato;

public interface ContatoRepository extends JpaRepository<Contato, Integer> {
	@Query("SELECT ct FROM Contato ct WHERE ct.pessoaFisica.id = :pessoaFisicaId")
	List<Contato> findByPessoaFisicaId(@Param("pessoaFisicaId") int pessoaFisicaId);
}