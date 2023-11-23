package com.bantads.conta.rabbit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Message implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Long codigo;
	private String origem;
	private String destino;
	private String data;
	private List<String> dests;
	private Integer etapa;
	private String operacao;
	private Boolean estorno;
	private String erro;
	private Integer codErro;
	
	public Message() {
		this.erro = "";
		this.dests = new ArrayList<>();
		this.etapa = -1;
		this.codErro = 200;
		this.estorno = false;
	}
	
	public Message(String origin, String destino, String data) {
		this.origem = origin;
		this.destino = destino;
		this.data = data;
		this.codErro = 200;
		this.estorno = false;
	}
	
	public Message(Integer codErro, String erro) {
		this.codErro = codErro;
		this.erro = erro;
		this.estorno = false;
		this.etapa = -1;
	}
	
	public String getOrigem() {
		return origem;
	}
	public void setOrigem(String origem) {
		this.origem = origem;
	}
	public String getDestino() {
		return destino;
	}
	public void setDestino(String destino) {
		this.destino = destino;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}

	public List<String> getDests() {
		return dests;
	}

	public void setDests(List<String> dests) {
		this.dests = dests;
	}

	public String getOperacao() {
		return operacao;
	}

	public void setOperacao(String operacao) {
		this.operacao = operacao;
	}

	public String getErro() {
		return erro;
	}

	public void setErro(String erro) {
		this.erro = erro;
	}

	public Integer getEtapa() {
		return etapa;
	}

	public void setEtapa(Integer etapa) {
		this.etapa = etapa;
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public Integer getCodErro() {
		return codErro;
	}

	public void setCodErro(Integer codErro) {
		this.codErro = codErro;
	}

	public Boolean getEstorno() {
		return estorno;
	}

	public void setEstorno(Boolean estorno) {
		this.estorno = estorno;
	}
}
