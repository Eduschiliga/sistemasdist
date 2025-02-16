package br.com.eduardo.sistemadistribuido.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CadastrarUsuarioCategoriaResponse {
  private Integer status;
  private String operacao = "cadastrarUsuarioCategoria";
  private String mensagem;
}
