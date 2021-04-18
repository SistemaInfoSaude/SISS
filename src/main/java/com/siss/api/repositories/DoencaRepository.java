package com.siss.api.repositories;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import com.siss.api.entities.Doenca;
import com.siss.api.entities.Veiculo;

public interface DoencaRepository extends JpaRepository<Doenca, Integer> {
	@Query("SELECT do FROM Doenca do WHERE do.condicaoClinica.id = :condicaoClinicaId")
	List<Doenca> findByCondicaoClinicaId(@Param("condicaoClinicaId") int condicaoClinicaId);
}
