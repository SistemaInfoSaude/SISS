package com.siss.api.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.siss.api.entities.Veiculo;
import com.siss.api.exceptions.ConsistenciaException;
import com.siss.api.repositories.VeiculoRepository;

@Service
public class VeiculoService {
	private static final Logger log = LoggerFactory.getLogger(VeiculoService.class);

	@Autowired
	private VeiculoRepository veiculoRepository;

	@Autowired
	private PessoaFisicaService pessoaFisicaService;

	public Optional<Veiculo> buscarPorId(int id) throws ConsistenciaException {
		log.info("Service: buscando o veiculo com o id: {}", id);
		Optional<Veiculo> veiculo = veiculoRepository.findById(id);

		if (!veiculo.isPresent()) {
			log.info("Service: Nenhum veiculo com id: {} foi encontrado", id);
			throw new ConsistenciaException("Nenhuma veiculo com id: {} foi encontrado", id);
		}
		return veiculo;
	}

	public Optional<List<Veiculo>> buscarPorPessoaFisicaId(int pessoaFisicaId) throws ConsistenciaException {
		log.info("Service: buscando os veiculos da PF de id: {}", pessoaFisicaId);
		
		Optional<List<Veiculo>> veiculos = Optional.ofNullable(veiculoRepository.findByPessoaFisicaId(pessoaFisicaId));
		if (!veiculos.isPresent() || veiculos.get().size() < 1) {
			log.info("Service: Nenhum veiculos encontrado para a PF de id: {}", pessoaFisicaId);
			throw new ConsistenciaException("Nenhum veiculo encontrado para a PF de id: {}", pessoaFisicaId);
		}
		return veiculos;
	}

	public Veiculo salvar(Veiculo veiculo) throws ConsistenciaException {
		log.info("Service: salvando o veiculo: {}", veiculo.toString());
		int pfId = veiculo.getPessoaFisica().getId();

		if (veiculo.getId() > 0) {
			Veiculo veiculoExistente = buscarPorId(veiculo.getId()).get();
			veiculo.setDataCadastro(veiculoExistente.getDataCadastro());
		}

		try {
			if (!pessoaFisicaService.buscarPorId(pfId).isPresent()) {
				throw new ConsistenciaException("Nenhuma PF com id: {} encontrado!", pfId);
			}
			return veiculoRepository.save(veiculo);
		} catch (DataIntegrityViolationException e) {
			log.info("Service: Inconsistência de dados.");
			throw new ConsistenciaException("Inconsistência de dados");
		}
	}
}