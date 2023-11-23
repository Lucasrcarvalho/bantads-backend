package com.bantads.auth.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioDTO, Integer> {
	
	public UsuarioDTO findByLogin(String login);
	
	public UsuarioDTO deleteByLogin(UsuarioDTO usuario);
	
}
