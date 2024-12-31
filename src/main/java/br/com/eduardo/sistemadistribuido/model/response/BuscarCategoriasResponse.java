package br.com.eduardo.sistemadistribuido.model.response;

import br.com.eduardo.sistemadistribuido.entity.Categoria;

import java.util.List;

public class BuscarCategoriasResponse {
  private int status;
  private String operacao;
  private List<Categoria> listacategorias;

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

  public List<Categoria> getListacategorias() {
    return listacategorias;
  }

  public void setListacategorias(List<Categoria> listacategorias) {
    this.listacategorias = listacategorias;
  }
}
