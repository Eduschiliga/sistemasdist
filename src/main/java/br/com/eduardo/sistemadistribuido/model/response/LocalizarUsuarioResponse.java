package br.com.eduardo.sistemadistribuido.model.response;

import br.com.eduardo.sistemadistribuido.model.dto.UsuarioSemAdminDTO;

public class LocalizarUsuarioResponse {
  private int status;
  private String operacao = "localizarUsuario";
  private UsuarioSemAdminDTO usuario;

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public UsuarioSemAdminDTO getUsuario() {
    return usuario;
  }

  public void setUsuario(UsuarioSemAdminDTO usuario) {
    this.usuario = usuario;
  }

  public String getOperacao() {
    return operacao;
  }

  public void setOperacao(String operacao) {
    this.operacao = operacao;
  }
}
