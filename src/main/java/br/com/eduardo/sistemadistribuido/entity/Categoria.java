package br.com.eduardo.sistemadistribuido.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "categoria")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Categoria {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long categoriaId;

  @Column(name = "nome")
  private String nome;

  public long getCategoriaId() {
    return categoriaId;
  }

  public void setCategoriaId(long categoriaId) {
    this.categoriaId = categoriaId;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }
}
