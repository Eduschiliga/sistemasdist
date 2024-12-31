package br.com.eduardo.sistemadistribuido.model.request;

import br.com.eduardo.sistemadistribuido.cliente.ClienteApplication;

public class OperacaoTokenRequest {
  private String operacao;
  private String token = ClienteApplication.token;

  public String getOperacao() {
    return operacao;
  }

  public void setOperacao(String operacao) {
    this.operacao = operacao;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

}
