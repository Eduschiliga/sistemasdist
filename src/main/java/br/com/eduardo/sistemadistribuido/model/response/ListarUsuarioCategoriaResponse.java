package br.com.eduardo.sistemadistribuido.model.response;

import br.com.eduardo.sistemadistribuido.cliente.ClienteApplication;
import br.com.eduardo.sistemadistribuido.entity.Categoria;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ListarUsuarioCategoriaResponse {
  private String operacao = "listarUsuarioCategorias";
  private Integer status;
  private List<Categoria> categorias;
}
