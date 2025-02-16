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
public class ListarUsuarioCategoriaRequest {
  private String operacao = "listarUsuarioCategorias";
  private String token = ClienteApplication.token;
  private String ra = ClienteApplication.token;
}
