package br.com.eduardo.sistemadistribuido.model.response;

import br.com.eduardo.sistemadistribuido.entity.Categoria;

public class LocalizarCategoriaResponse {
  private int status;
  private String operacao;
  private Categoria categoria;

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getOperacao() {
    return operacao;
  }

  public void setOperacao(String operacao) {
    this.operacao = operacao;
  }

  public Categoria getCategoria() {
    return categoria;
  }

  public void setCategoria(Categoria categoria) {
    this.categoria = categoria;
  }
}
