package com.bantads.auth.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "usuarios" )
public class UsuarioDTO {

	//private static final long serialVersionUID = 5291667169024445031L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "usua_codigo")
	private Integer codigo;
	
	@Column(name = "usua_login")
	private String login;
	
	@Column(name = "usua_senha")
	private String senha;
	
	@Column(name = "usua_role")
	private String role;
	
	@Transient
	private String token;
	
	public UsuarioDTO() {
		super();
	}

	public UsuarioDTO(String login, String senha) {
		this.login = login;
		this.senha = senha;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
