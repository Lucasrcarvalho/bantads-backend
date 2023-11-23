package com.bantads.conta.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovimentoRepository extends JpaRepository<MovimentoDTO, Integer>  {

	MovimentoDTO findByClieCodigo(Integer clieCodigo);
	
}
