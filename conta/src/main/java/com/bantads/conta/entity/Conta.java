package com.bantads.conta.entity;

import java.io.Serializable;
import java.sql.Date;

public class Conta implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private Integer clie_codigo;
	private Integer gere_codigo;
	private Boolean aberta;
	private String abertaMsg;
	private Date abertaData;
	private Integer numero;
	private Date dtCadastro;
	private Double limite;
	private Double salario;

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public Integer getClie_codigo() {
		return clie_codigo;
	}

	public void setClie_codigo(Integer clie_codigo) {
		this.clie_codigo = clie_codigo;
	}

	public Integer getGere_codigo() {
		return gere_codigo;
	}

	public void setGere_codigo(Integer gere_codigo) {
		this.gere_codigo = gere_codigo;
	}

	public Boolean getAberta() {
		return aberta;
	}

	public void setAberta(Boolean aberta) {
		this.aberta = aberta;
	}

	public String getAbertaMsg() {
		return abertaMsg;
	}

	public void setAbertaMsg(String abertaMsg) {
		this.abertaMsg = abertaMsg;
	}

	public Date getAbertaData() {
		return abertaData;
	}

	public void setAbertaData(Date abertaData) {
		this.abertaData = abertaData;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public Date getDtCadastro() {
		return dtCadastro;
	}

	public void setDtCadastro(Date dtCadastro) {
		this.dtCadastro = dtCadastro;
	}

	public Double getLimite() {
		return limite;
	}

	public void setLimite(Double limite) {
		this.limite = limite;
	}

	public Double getSalario() {
		return salario;
	}

	public void setSalario(Double salario) {
		this.salario = salario;
	}

}
