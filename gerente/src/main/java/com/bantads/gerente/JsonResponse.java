package com.bantads.gerente;

public class JsonResponse {
	  private Boolean sucesso;
	  private String mensagem;
	  private Object informacao;

	  public JsonResponse(Boolean sucesso, String mensagem, Object informacao) {
	    this.sucesso = sucesso;
	    this.mensagem = mensagem;
	    this.informacao = informacao;
	  }

	  public Boolean getSuccesso() {
	    return sucesso;
	  }

	  public void setSucesso(Boolean sucesso) {
	    this.sucesso = sucesso;
	  }

	  public String getMensagem() {
		    return mensagem;
	  }
	  
	  public void setMensagem(String mensagem) {
	    this.mensagem = mensagem;
	  }

	  public Object getInformacao() {
	    return informacao;
	  }

	  public void setInformacao(Object informacao) {
	    this.informacao = informacao;
	  }

	}
