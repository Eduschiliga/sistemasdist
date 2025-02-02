package br.com.eduardo.sistemadistribuido.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "categoria")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Categoria {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "categoria_id")
  private Integer categoriaId;

  @Column(name = "nome")
  private String nome;

  @Override
  public String toString() {
    return this.nome;
  }

  public long getCategoriaId() {
    return categoriaId;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }
}
