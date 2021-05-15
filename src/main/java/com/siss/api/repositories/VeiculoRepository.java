package com.siss.api.repositories;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.siss.api.entities.Contato;
import com.siss.api.entities.Veiculo;

public interface VeiculoRepository extends JpaRepository<Veiculo, Integer> {
	@Query("SELECT ve FROM Veiculo ve WHERE ve.pessoaFisica.id = :pessoaFisicaId")
	List<Veiculo> findByPessoaFisicaId(@Param("pessoaFisicaId") int usuarioId);
}
