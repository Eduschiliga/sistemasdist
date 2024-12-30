package br.com.eduardo.sistemadistribuido.model.request;

import br.com.eduardo.sistemadistribuido.cliente.ClienteApplication;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class LocalizarUsuarioRequest {
  private String operacao = "localizarUsuario";
  private String token = ClienteApplication.token;
  private String ra = ClienteApplication.token;

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

  public String getRa() {
    return ra;
  }

  public void setRa(String ra) {
    this.ra = ra;
  }
}
