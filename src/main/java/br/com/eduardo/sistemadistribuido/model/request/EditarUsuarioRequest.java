package br.com.eduardo.sistemadistribuido.model.request;

import br.com.eduardo.sistemadistribuido.cliente.ClienteApplication;
import br.com.eduardo.sistemadistribuido.model.dto.UsuarioSemAdminDTO;

public class EditarUsuarioRequest {
  private String operacao = "editarUsuario";
  private String token = ClienteApplication.token;
  private UsuarioSemAdminDTO usuario = new UsuarioSemAdminDTO();

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

  public UsuarioSemAdminDTO getUsuario() {
    return usuario;
  }

  public void setUsuario(UsuarioSemAdminDTO usuario) {
    this.usuario = usuario;
  }
}
