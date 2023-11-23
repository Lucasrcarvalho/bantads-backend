package com.bantads.auth.entity;

import java.io.Serializable;

public class Usuario implements Serializable {
private static final long serialVersionUID = 5291667169024445031L;
	
	private Integer codigo;
	private String login;
	private String senha;
	private String role;
	private String token;

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
