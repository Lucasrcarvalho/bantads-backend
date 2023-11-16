package com.bantads.gerente.entity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GerenteRepository extends JpaRepository<GerenteDTO, Integer> {

	public GerenteDTO findByCpf(String cpf);
}
