package com.siss.api.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import com.siss.api.entities.PessoaFisica;

public interface PessoaFisicaRepository extends JpaRepository<PessoaFisica, Integer> {
	@Transactional(readOnly = true)
	Optional<PessoaFisica> findByCpf(String cpf);
	
	@Transactional(readOnly = true)
	Optional<PessoaFisica> findByUsuarioId(int usuarioId);
}
