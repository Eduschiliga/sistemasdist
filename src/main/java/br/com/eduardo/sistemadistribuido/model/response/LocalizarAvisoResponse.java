package br.com.eduardo.sistemadistribuido.model.response;

import br.com.eduardo.sistemadistribuido.entity.Aviso;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LocalizarAvisoResponse {
  private String operacao = "localizarAviso";
  private Integer status;
  private Aviso aviso;
}
