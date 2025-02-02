package br.com.eduardo.sistemadistribuido.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "aviso_categoria_usuario")
public class AvisoCategoriaUsuario {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "aviso_categoria_id")
  private Long avisoCategoriaId;

  @Column(name = "ra_usuario")
  private String ra;

  @ToString.Exclude
  @OneToMany(cascade = CascadeType.ALL)
  private List<Categoria> categoria = new ArrayList<>();

  public List<Categoria> getCategoria() {
    return categoria;
  }

  public void setCategoria(List<Categoria> categoria) {
    this.categoria = categoria;
  }
}
