package com.siss.api.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import com.siss.api.entities.PessoaFisica;
import com.siss.api.entities.Veiculo;

public interface PessoaFisicaRepository extends JpaRepository<PessoaFisica, Integer> {
	@Transactional(readOnly = true)
	Optional<PessoaFisica> findByCpf(String cpf);

	@Transactional(readOnly = true)
	Optional<PessoaFisica> findByUsuarioId(int usuarioId);

	@Transactional(readOnly = true)
	Optional<PessoaFisica> findByRg(String rg);

	@Query("SELECT pf FROM PessoaFisica pf inner join Veiculo vi on vi.pessoaFisica.id = pf.id and vi.placa like %:placa%")
	Optional<List<PessoaFisica>> findByVeiculoPlaca(@Param("placa") String placa);
}
