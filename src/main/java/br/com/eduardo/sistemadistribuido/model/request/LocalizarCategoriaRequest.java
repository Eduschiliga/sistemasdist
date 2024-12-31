package br.com.eduardo.sistemadistribuido.model.request;

import br.com.eduardo.sistemadistribuido.cliente.ClienteApplication;

public class LocalizarCategoriaRequest {
  private String operacao = "localizarCategoria";
  private String token = ClienteApplication.token;
  private int id;

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

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
}
