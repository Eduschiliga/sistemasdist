package br.com.eduardo.sistemadistribuido.model.request;

import br.com.eduardo.sistemadistribuido.cliente.ClienteApplication;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ListarUsuarioAvisosRequest {
  private String operacao = "listarUsuarioAvisos";
  private String token = ClienteApplication.token;
  private String ra = ClienteApplication.token;
}
