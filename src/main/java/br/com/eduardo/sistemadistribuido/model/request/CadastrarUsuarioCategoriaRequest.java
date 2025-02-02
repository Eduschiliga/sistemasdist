package br.com.eduardo.sistemadistribuido.model.request;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class CadastrarUsuarioCategoriaRequest {
  private String operacao = "cadastrarUsuarioCategoria";
  private String token;
  private String ra;
  private Integer categoria;
}
