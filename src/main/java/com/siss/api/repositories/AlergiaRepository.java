package com.siss.api.repositories;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import com.siss.api.entities.Alergia;
import com.siss.api.entities.Doenca;

public interface AlergiaRepository extends JpaRepository<Alergia, Integer> {
	@Query("SELECT al FROM Alergia al WHERE al.condicaoClinica.id = :condicaoClinicaId")
	List<Alergia> findByCondicaoClinicaId(@Param("condicaoClinicaId") int condicaoClinicaId);
}
