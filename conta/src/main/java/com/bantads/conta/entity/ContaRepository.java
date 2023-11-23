package com.bantads.conta.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContaRepository extends JpaRepository<ContaDTO, Integer> {

	public ContaDTO findByClieCodigo(Integer clieCodigo);
	
}
