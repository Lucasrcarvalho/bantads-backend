package com.bantads.gerente.entity;

import java.io.Serializable;

public class Gerente implements Serializable {
	private static final long serialVersionUID = 1445197186359874015L;
	
	private Integer codigo;
	private String nome;
	private String email;
	private String cpf;
	public Gerente(int gere_codigo, String gere_nome, String gere_email, String gere_cpf) {
		super();
		this.codigo = gere_codigo;
		this.nome = gere_nome;
		this.email = gere_email;
		this.cpf = gere_cpf;
	}
	
	public Gerente () {}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
}
