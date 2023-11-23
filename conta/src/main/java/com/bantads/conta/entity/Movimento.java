package com.bantads.conta.entity;


import java.io.Serializable;
import java.util.Date;

public class Movimento implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private Date data;
	private String tipo;
	private Integer contOrigem;
	private Integer contDestino;
	private Integer clieCodigo;
	private Double valor;
	
	public Movimento() {}
	
	public Movimento(String tipo) {
		this.data = new Date();
	}
	
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
