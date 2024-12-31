package br.com.eduardo.sistemadistribuido.model.request;

public class ExcluirUsuarioRequest extends LocalizarUsuarioRequest {
  public ExcluirUsuarioRequest() {
    this.setOperacao("excluirUsuario");
  }
}
