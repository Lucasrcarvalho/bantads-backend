package com.bantads.conta.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "contas_r" )
public class ContaRDTO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "cont_codigo")
	private Integer codigo;
	@Column(name = "cont_status")
	private String status;
	@Column(name = "cont_clie_codigo")
	private Integer clieCodigo;
	@Column(name = "cont_gere_codigo")
	private Integer gereCodigo;
	@Column(name = "cont_aberta")
	private Boolean aberta;
	@Column(name = "cont_abertamsg")
	private String abertaMsg;
	@Column(name = "cont_abertadata")
	private Date abertaData;
	@Column(name = "cont_numero")
	private Integer numero;
	@Column(name = "cont_dtcadastro")
	private Date dtCadastro;
	@Column(name = "cont_limite")
	private Double limite;
	@Column(name = "cont_saldo")
	private Double saldo;
	
	public ContaRDTO() {}
	
	public ContaRDTO(Integer clieCodigo) {
		this.clieCodigo = clieCodigo;
		this.dtCadastro = new Date();
	}
	
	public Integer getCodigo() {
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public Integer getClie_codigo() {
		return clieCodigo;
	}
	public void setClie_codigo(Integer clie_codigo) {
		this.clieCodigo = clie_codigo;
	}
	public Integer getGere_codigo() {
		return gereCodigo;
	}
	public void setGere_codigo(Integer gere_codigo) {
		this.gereCodigo = gere_codigo;
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

	public Integer getClieCodigo() {
		return clieCodigo;
	}

	public void setClieCodigo(Integer clieCodigo) {
		this.clieCodigo = clieCodigo;
	}

	public Integer getGereCodigo() {
		return gereCodigo;
	}

	public void setGereCodigo(Integer gereCodigo) {
		this.gereCodigo = gereCodigo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Double getSaldo() {
		return saldo;
	}

	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}
}
