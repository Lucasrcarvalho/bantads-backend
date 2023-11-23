package com.bantads.conta.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "movimentos_r" )
public class MovimentoRDTO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "movi_codigo")
	private Integer codigo;
	@Column(name = "movi_data")
	private Date data;
	@Column(name = "movi_tipo")
	private String tipo;
	@Column(name = "movi_cont_origem")
	private Integer contOrigem;
	@Column(name = "movi_cont_destino")
	private Integer contDestino;
	@Column(name = "movi_clie_codigo")
	private Integer clieCodigo;
	@Column(name = "movi_valor")
	private Double valor;
	
	public Integer getCodigo() {
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public Integer getContOrigem() {
		return contOrigem;
	}
	public void setContOrigem(Integer contOrigem) {
		this.contOrigem = contOrigem;
	}
	public Integer getContDestino() {
		return contDestino;
	}
	public void setContDestino(Integer contDestino) {
		this.contDestino = contDestino;
	}
	public Integer getClieCodigo() {
		return clieCodigo;
	}
	public void setClieCodigo(Integer clieCodigo) {
		this.clieCodigo = clieCodigo;
	}
	public Double getValor() {
		return valor;
	}
	public void setValor(Double valor) {
		this.valor = valor;
	}
	
}

